package com.example.demo.task

// Para crear una nueva tarea (no necesita ID ni isCompleted)
data class TaskCreateRequest(
    val title: String,
    val description: String?,
    val location: String?,
    val priority: String
)

// Para actualizar una tarea existente
data class TaskUpdateRequest(
    val title: String,
    val description: String?,
    val location: String?,
    val priority: String,
    val isCompleted: Boolean
)

// La respuesta estándar del servidor al devolver una tarea
data class TaskResponse(
    val id: Long,
    val title: String,
    val description: String?,
    val location: String?,
    val priority: String,
    val isCompleted: Boolean
)

// Helper para convertir la Entidad (Task) a un DTO (TaskResponse)
fun TaskModel.toResponse(): TaskResponse {
    return TaskResponse(
        id = this.id!!, // Asumimos que la respuesta siempre tiene un ID
        title = this.title,
        description = this.description,
        location = this.location,
        priority = this.priority,
        isCompleted = this.isCompleted
    )
}