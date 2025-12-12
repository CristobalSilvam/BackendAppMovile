package com.example.demo.task

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.lang.IllegalArgumentException

@RestController
@RequestMapping("/api/tasks")
class TaskController(
    private val taskService: TaskService 
) {

    // --- GET (Leer Todas) ---
    @GetMapping
    fun getAllTasks(): ResponseEntity<List<TaskResponse>> {
        val taskResponses = taskService.getAllTasks()
        return ResponseEntity.ok(taskResponses)
    }

    // --- POST (Crear) ---
    @PostMapping
    fun createTask(@RequestBody request: TaskCreateRequest): ResponseEntity<Any> {
        return try {
            val savedTask = taskService.createTask(request)
            ResponseEntity.ok(savedTask)
        } catch (e: IllegalArgumentException) {
            // Si el servicio lanza un error de validación, devuelve 400 Bad Request
            ResponseEntity.badRequest().body(e.message)
        }
    }

    // --- PUT (Actualizar) ---
    @PutMapping("/{id}")
    fun updateTask(@PathVariable id: Int, @RequestBody request: TaskUpdateRequest): ResponseEntity<TaskResponse> {
        val updatedTask = taskService.updateTask(id, request)
        
        // Si el servicio devuelve null (no encontrado), devuelve 404
        return updatedTask?.let {
            ResponseEntity.ok(it)
        } ?: ResponseEntity.notFound().build()
    }

    // --- DELETE (Borrar) ---
    @DeleteMapping("/{id}")
    fun deleteTask(@PathVariable id: Long): ResponseEntity<Unit> {
        val success = taskService.deleteTask(id)

        // Si el servicio devuelve false (no encontrado), devuelve 404
        return if (success) {
            ResponseEntity.noContent().build() // 204 Éxito
        } else {
            ResponseEntity.notFound().build() // 404 No Encontrado
        }
    }
}