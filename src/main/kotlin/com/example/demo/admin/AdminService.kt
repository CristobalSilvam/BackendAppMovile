package com.example.demo.admin

import com.example.demo.group.GroupRepository
import com.example.demo.user.UserRepository
import com.example.demo.user.UserRole
import com.example.demo.user.UserModel
import org.springframework.stereotype.Service

@Service
class AdminService(
    private val userRepository: UserRepository,
    private val groupRepository: GroupRepository
) {

    // 1. Ver todos los usuarios del sistema
    fun getAllUsers(adminId: Long): List<UserManagementResponse> {
        checkAdminPrivileges(adminId)
        return userRepository.findAll().map { 
            UserManagementResponse(it.id!!, it.email, it.role) 
        }
    }

    // 2. Cambiar el rol de cualquier usuario (ej: de USER a PREMIUM)
    fun updateUserRole(adminId: Long, request: UpdateUserRoleRequest) {
        checkAdminPrivileges(adminId)
        val user = userRepository.findById(request.userId)
            .orElseThrow { IllegalArgumentException("Usuario no encontrado") }
        
        // Actualizamos el rol y guardamos
        val updatedUser = user.copy(role = request.newRole)
        userRepository.save(updatedUser)
    }

    // 3. Eliminar un usuario definitivamente
    fun deleteUser(adminId: Long, userIdToDelete: Long) {
        checkAdminPrivileges(adminId)
        if (adminId == userIdToDelete) throw IllegalArgumentException("No puedes eliminarte a ti mismo")
        
        userRepository.deleteById(userIdToDelete)
    }

    // Función interna para validar que quien opera es ADMIN
    private fun checkAdminPrivileges(adminId: Long) {
        val admin = userRepository.findById(adminId).orElseThrow { IllegalArgumentException("Admin no encontrado") }
        if (admin.role != UserRole.ADMIN) {
            throw IllegalArgumentException("Acceso denegado: Se requieren permisos de Administrador")
        }
    }
}