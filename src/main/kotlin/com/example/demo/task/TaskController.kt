package com.example.demo.task

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/tasks")
class TaskController(private val taskService: TaskService) {

    // Obtener tareas según el rol del usuario
    @GetMapping
    fun getAll(@RequestParam userId: Long): ResponseEntity<List<TaskResponse>> {
        return ResponseEntity.ok(taskService.getAllTasks(userId))
    }

    // Crear tarea vinculada al usuario
    @PostMapping
    fun create(@RequestParam userId: Long, @RequestBody request: TaskCreateRequest): ResponseEntity<Any> {
        return try {
            val task = taskService.createTask(request, userId)
            ResponseEntity.ok(task)
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(e.message)
        }
    }

    // Actualizar tarea (con chequeo de permisos)
    @PutMapping("/{id}")
    fun update(
        @PathVariable id: Long,
        @RequestParam userId: Long,
        @RequestBody request: TaskUpdateRequest
    ): ResponseEntity<Any> {
        return try {
            val updated = taskService.updateTask(id, userId, request)
            ResponseEntity.ok(updated)
        } catch (e: Exception) {
            ResponseEntity.status(403).body(e.message)
        }
    }

    // Eliminar tarea (con chequeo de permisos)
    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long, @RequestParam userId: Long): ResponseEntity<Any> {
        return try {
            taskService.deleteTask(id, userId)
            ResponseEntity.noContent().build()
        } catch (e: Exception) {
            ResponseEntity.status(403).body(e.message)
        }
    }
}