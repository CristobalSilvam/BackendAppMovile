package com.example.demo.auth

import com.example.demo.user.UserRepository
import com.example.demo.user.UserModel
import com.example.demo.user.UserRole
import org.springframework.stereotype.Service
import java.lang.IllegalArgumentException

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val jwtService: JwtService
) {

    // --- Lógica para POST (Registrar Usuario) ---
    fun register(request: RegisterRequest) {
        if (request.email.isBlank() || request.password.isBlank()) {
            throw IllegalArgumentException("Todos los campos son obligatorios.")
        }
        if (request.password != request.confirmPassword) {
            throw IllegalArgumentException("Las contraseñas no coinciden")
        }

        val existingUser = userRepository.findByEmail(request.email)
        if (existingUser != null) {
            throw IllegalArgumentException("El email ya está en uso")
        }

        val selectedRole = try {
            val roleEnum = UserRole.valueOf(request.role.uppercase())
            if (roleEnum == UserRole.ADMIN) UserRole.USER else roleEnum
        } catch (e: Exception) {
            UserRole.USER
        }

        val newUser = UserModel(
            email = request.email,
            passwordHash = request.password, 
            role = selectedRole
        )

        userRepository.save(newUser)
    }

    // --- Lógica para POST (Iniciar Sesión) ---
    fun login(request: LoginRequest): AuthResponse {
        // DEBUG: Imprimimos en consola qué está llegando
        println("--- LOGIN DEBUG ---")
        println("Email recibido: ${request.email}")
        println("Pass recibido: ${request.password}")

        val user = userRepository.findByEmail(request.email)

        if (user == null) {
            println("ERROR: Usuario no encontrado en BD")
            throw IllegalArgumentException("Email o contraseña incorrectos")
        }

        // DEBUG: Comparamos contraseñas
        println("Pass en BD: ${user.passwordHash}")
        
        if (user.passwordHash != request.password) {
            println("ERROR: Las contraseñas no coinciden")
            throw IllegalArgumentException("Email o contraseña incorrectos")
        }

        println("EXITO: Credenciales correctas. Generando Token...")

        // Generar Token
        val jwtToken = jwtService.generateToken(user.email, user.role.name)

        return AuthResponse(
            id = user.id!!,
            email = user.email,
            token = jwtToken,
            role = user.role
        )
    }
}