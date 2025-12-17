package com.example.demo.user

import com.example.demo.user.dto.UserResponse
import com.example.demo.user.dto.toResponse
import org.springframework.stereotype.Service
import java.lang.IllegalArgumentException

@Service
class UserService(
    private val userRepository: UserRepository
) {

    // --- OBTENER TODOS LOS USUARIOS (Solo Admin) ---
    fun getAllUsers(requestingUserId: Long): List<UserResponse> {
        // 1. Buscamos quién está pidiendo la lista
        val requester = userRepository.findById(requestingUserId)
            .orElseThrow { IllegalArgumentException("Usuario solicitante no encontrado") }

        // 2. Verificamos que sea ADMIN
        if (requester.role != UserRole.ADMIN) {
            throw IllegalArgumentException("Acceso denegado. Solo los administradores pueden ver esta lista.")
        }

        // 3. Devolvemos todos convertidos a DTO
        return userRepository.findAll().map { it.toResponse() }
    }

    // --- ELIMINAR UN USUARIO (Solo Admin) ---
    fun deleteUser(userIdToDelete: Long, requestingUserId: Long) {
        val requester = userRepository.findById(requestingUserId)
            .orElseThrow { IllegalArgumentException("Usuario solicitante no encontrado") }

        if (requester.role != UserRole.ADMIN) {
            throw IllegalArgumentException("No tienes permisos de Administrador.")
        }

        if (!userRepository.existsById(userIdToDelete)) {
            throw IllegalArgumentException("El usuario a eliminar no existe.")
        }

        userRepository.deleteById(userIdToDelete)
    }
}