package com.example.demo.task 

import com.example.demo.task.TaskModel 

import org.springframework.stereotype.Service
import java.lang.IllegalArgumentException

@Service // Anotación clave: le dice a Spring que esto es un Servicio
class TaskService(
    private val taskRepository: TaskRepository // 1. Depende del Repositorio
) {

    // --- Lógica para GET (Leer Todas) ---
    fun getAllTasks(): List<TaskResponse> {
        val tasks = taskRepository.findAll()
        // Convierte la lista de Entidades a una lista de DTOs de Respuesta
        return tasks.map { it.toResponse() }
    }

    // --- Lógica para POST (Crear Tarea) ---
    fun createTask(request: TaskCreateRequest): TaskResponse {
        // 1. VALIDACIÓN (Lógica de Negocio)
        if (request.title.isBlank()) {
            throw IllegalArgumentException("El título no puede estar vacío")
        }
        if (request.priority !in listOf("ALTA", "MEDIA", "BAJA")) {
            throw IllegalArgumentException("La prioridad no es válida")
        }

        // 2. CREACIÓN (Lógica de Mapeo)
        val task = TaskModel(
            title = request.title,
            description = request.description,
            location = request.location,
            priority = request.priority,
            isCompleted = false // Tareas nuevas siempre están incompletas
        )

        // 3. GUARDADO
        val savedTask = taskRepository.save(task)
        return savedTask.toResponse()
    }

    // --- Lógica para PUT (Actualizar Tarea) ---
    fun updateTask(id: Long, request: TaskUpdateRequest): TaskResponse? {
        // 1. BÚSQUEDA
        val existingTask = taskRepository.findById(id)
            .orElse(null) ?: return null // Devuelve null si no la encuentra

        // 2. ACTUALIZACIÓN (Lógica de Mapeo)
        existingTask.title = request.title
        existingTask.description = request.description
        existingTask.location = request.location
        existingTask.priority = request.priority
        existingTask.isCompleted = request.isCompleted

        // 3. GUARDADO
        val updatedTask = taskRepository.save(existingTask)
        return updatedTask.toResponse()
    }

    // --- Lógica para DELETE (Borrar Tarea) ---
    fun deleteTask(id: Long): Boolean {
        // 1. BÚSQUEDA
        if (!taskRepository.existsById(id)) {
            return false // Falla si no existe
        }
        
        // 2. BORRADO
        taskRepository.deleteById(id)
        return true // Éxito
    }
}