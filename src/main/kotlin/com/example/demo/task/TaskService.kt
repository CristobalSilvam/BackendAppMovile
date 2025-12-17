package com.example.demo.task 

import com.example.demo.task.TaskModel 
import com.example.demo.user.UserRepository
import com.example.demo.user.UserRole
import com.example.demo.group.GroupRepository
import org.springframework.stereotype.Service
import java.lang.IllegalArgumentException

@Service
class TaskService(
    private val taskRepository: TaskRepository,
    private val userRepository: UserRepository,
    private val groupRepository: GroupRepository 
) {

    // --- GET (Leer Todas con Filtro de Privacidad) ---
    fun getAllTasks(requestingUserId: Long): List<TaskResponse> {
        val user = userRepository.findById(requestingUserId)
            .orElseThrow { IllegalArgumentException("Usuario no encontrado") }

        val tasks = when (user.role) {
            // 1. ADMIN: Ve todas las tareas de todos los usuarios
            UserRole.ADMIN -> taskRepository.findAll()

            // 2. LEADER: Ve sus tareas + las tareas de los miembros de su grupo
            UserRole.LEADER -> {
                val myTasks = taskRepository.findByUser(user)
                val memberTasks = groupRepository.findByLeader(user)
                    .flatMap { it.members }
                    .flatMap { taskRepository.findByUser(it) }
                (myTasks + memberTasks).distinctBy { it.id }
            }

            // 3. USER y PREMIUM: Solo ven las tareas que les pertenecen
            else -> taskRepository.findByUser(user)
        }
        
        return tasks.map { it.toResponse() }
    }

    // --- POST (Crear Tarea vinculada al Usuario) ---
    fun createTask(request: TaskCreateRequest, userId: Long): TaskResponse {
        val userOwner = userRepository.findById(userId)
            .orElseThrow { IllegalArgumentException("Usuario no encontrado") }

        // Validación de negocio para Usuario Base (Límite de tareas)
        if (userOwner.role == UserRole.USER) {
            val taskCount = taskRepository.findByUser(userOwner).size
            if (taskCount >= 10) { // Ejemplo: límite de 10 tareas para gratuitos
                throw IllegalArgumentException("Has alcanzado el límite de tareas. ¡Pásate a PREMIUM!")
            }
        }

        if (request.title.isBlank()) {
            throw IllegalArgumentException("El título no puede estar vacío")
        }

        val task = TaskModel(
            title = request.title,
            description = request.description,
            location = request.location,
            priority = request.priority,
            isCompleted = false,
            user = userOwner 
        )

        return taskRepository.save(task).toResponse()
    }

    // --- PUT (Actualizar con Jerarquía de Roles) ---
    fun updateTask(taskId: Long, requestingUserId: Long, request: TaskUpdateRequest): TaskResponse? {
        val existingTask = taskRepository.findById(taskId).orElse(null) ?: return null
        val userRequesting = userRepository.findById(requestingUserId)
            .orElseThrow { IllegalArgumentException("Usuario no encontrado") }

        // Lógica de permisos
        val canEdit = when {
            userRequesting.role == UserRole.ADMIN -> true
            existingTask.user.id == userRequesting.id -> true
            userRequesting.role == UserRole.LEADER -> {
                groupRepository.existsByLeaderAndMembersContaining(userRequesting, existingTask.user)
            }
            else -> false
        }

        if (!canEdit) {
            throw IllegalArgumentException("No tienes permisos para modificar esta tarea.")
        }

        existingTask.title = request.title
        existingTask.description = request.description
        existingTask.location = request.location
        existingTask.priority = request.priority
        existingTask.isCompleted = request.isCompleted

        return taskRepository.save(existingTask).toResponse()
    }

    // --- DELETE (Borrar con Jerarquía de Roles) ---
    fun deleteTask(taskId: Long, requestingUserId: Long): Boolean {
        val existingTask = taskRepository.findById(taskId).orElse(null) ?: return false
        val userRequesting = userRepository.findById(requestingUserId).orElse(null) ?: return false

        val canDelete = when {
            userRequesting.role == UserRole.ADMIN -> true
            existingTask.user.id == userRequesting.id -> true
            userRequesting.role == UserRole.LEADER -> {
                groupRepository.existsByLeaderAndMembersContaining(userRequesting, existingTask.user)
            }
            else -> false
        }

        if (!canDelete) {
            throw IllegalArgumentException("No tienes permisos para eliminar esta tarea.")
        }

        taskRepository.deleteById(taskId)
        return true
    }
}