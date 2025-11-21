package com.example.demo.user

// Lo que tu app Android enviará para Registrarse
data class RegisterRequest(
    val email: String,
    val password: String,
    val confirmPassword: String
)

// Lo que tu app Android enviará para Iniciar Sesión
data class LoginRequest(
    val email: String,
    val password: String
)

// Lo que el servidor responderá si el login es exitoso
data class AuthResponse(
    val email: String,
    val token: String // Simularemos un token
)