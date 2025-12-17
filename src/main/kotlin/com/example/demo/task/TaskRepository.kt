package com.example.demo.task

import com.example.demo.user.UserModel
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TaskRepository : JpaRepository<TaskModel, Long> {
    // Aquí puedes añadir consultas personalizadas si las necesitas
    fun findByUser(user: UserModel): List<TaskModel>
}