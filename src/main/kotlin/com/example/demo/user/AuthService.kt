package com.example.demo.user 


import com.example.demo.user.UserModel
import com.example.demo.user.AuthResponse
import com.example.demo.user.LoginRequest
import com.example.demo.user.RegisterRequest
import com.example.demo.user.UserRepository

import org.springframework.stereotype.Service
import java.lang.IllegalArgumentException

@Service // Anotación clave: le dice a Spring que esto es un Servicio
class AuthService(
    private val userRepository: UserRepository // 1. Depende del Repositorio
) {

    // --- Lógica para POST (Registrar Usuario) ---
    fun register(request: RegisterRequest) {
        // 1. VALIDACIÓN (Lógica de Negocio)
        if (request.email.isBlank() || request.password.isBlank() || request.confirmPassword.isBlank()) {
            throw IllegalArgumentException("Todos los campos son obligatorios.")
        }
        if (request.password != request.confirmPassword) {
            throw IllegalArgumentException("Las contraseñas no coinciden")
        }
        if (userRepository.findByEmail(request.email) != null) {
            throw IllegalArgumentException("El email ya está en uso")
        }
        if (request.password.length < 6) {
            throw IllegalArgumentException("La contraseña debe tener al menos 6 caracteres.")
        }
        if (!request.email.contains("@") || !request.email.contains(".")) {
             throw IllegalArgumentException("El formato del email es incorrecto.")
        }

        // 2. CREACIÓN (Lógica de Mapeo)
        val user = UserModel(
            email = request.email,
            // ¡IMPORTANTE! En un proyecto real, DEBES hashear la contraseña.
            // Para la evaluación, la guardamos en texto plano por simplicidad.
            passwordHash = request.password
        )
        
        // 3. GUARDADO
        userRepository.save(user)
    }

    // --- Lógica para POST (Iniciar Sesión) ---
    fun login(request: LoginRequest): AuthResponse {
        // 1. BÚSQUEDA
        val user = userRepository.findByEmail(request.email)
            // Lanza una excepción si el email no existe
            ?: throw IllegalArgumentException("Email o contraseña incorrectos")

        // 2. VALIDACIÓN (Comprobación de contraseña)
        if (user.passwordHash != request.password) {
            throw IllegalArgumentException("Email o contraseña incorrectos")
        }

        // 3. ÉXITO: Devolver una respuesta (simulando un token JWT)
        return AuthResponse(
            email = user.email,
            token = "simulated-jwt-token-for-${user.id}"
        )
    }
}