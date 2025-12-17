package com.example.demo.user.dto // Ajusta a tu paquete

import com.example.demo.user.UserModel

// Lo que el Admin verá de cada usuario
data class UserResponse(
    val id: Long,
    val email: String,
    val role: String
    // val name: String // Descomenta si tu UserModel tiene nombre
)

// --- EXTENSIONES (Mappers) ---

// Convierte el Modelo de Base de Datos a la Respuesta para el Admin
fun UserModel.toResponse(): UserResponse {
    return UserResponse(
        id = this.id!!,
        email = this.email,
        role = this.role.name
        // name = this.name ?: "" 
    )
}