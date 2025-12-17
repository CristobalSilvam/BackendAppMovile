package com.example.demo.auth

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.lang.IllegalArgumentException 

import com.example.demo.auth.LoginRequest
import com.example.demo.auth.RegisterRequest

@RestController
@RequestMapping("/api/auth") // URL base
class AuthController(
    private val authService: AuthService 
) {

    // Endpoint: POST /api/auth/register
    @PostMapping("/register")
    fun register(@RequestBody request: RegisterRequest): ResponseEntity<Any> {
        return try {
            // Delega la lógica de registro al servicio
            authService.register(request)
            ResponseEntity.ok("Usuario registrado exitosamente")
        } catch (e: IllegalArgumentException) {
            // Si el servicio lanza un error de validación, devuelve 400 Bad Request
            ResponseEntity.badRequest().body(mapOf("error" to e.message))
        } catch (e: Exception) {
            ResponseEntity.internalServerError().body(mapOf("error" to "Error interno del servidor"))
        }
    }

    // Endpoint: POST /api/auth/login
    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest): ResponseEntity<Any> {
        return try {
            // Delega la lógica de login al servicio
            val authResponse = authService.login(request)
            ResponseEntity.ok(authResponse)
        } catch (e: IllegalArgumentException) {
            // Si el servicio lanza error (email no existe, pass incorrecta), devuelve 401 Unauthorized
            ResponseEntity.status(401).body(mapOf("error" to e.message))
        } catch (e: Exception) {
            ResponseEntity.internalServerError().body(mapOf("error" to "Error interno del servidor"))
        }
    }
}