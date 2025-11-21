package com.example.demo.task

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TaskRepository : JpaRepository<TaskModel, Long> {
    // Aquí puedes añadir consultas personalizadas si las necesitas
    // ej: fun findByPriority(priority: String): List<Task>
}