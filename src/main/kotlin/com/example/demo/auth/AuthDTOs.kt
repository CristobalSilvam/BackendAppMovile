package com.example.demo.auth

import com.example.demo.user.UserRole

// Lo que tu app Android enviará para Registrarse
data class RegisterRequest(
    val email: String,
    val password: String,
    val confirmPassword: String,
    val role: String
)

// Lo que tu app Android enviará para Iniciar Sesión
data class LoginRequest(
    val email: String,
    val password: String
)

// Lo que el servidor responderá si el login es exitoso
data class AuthResponse(
    val id: Long,
    val email: String,
    val token: String, // Simularemos un token
    val role: UserRole // Añadimos el rol aquí
)