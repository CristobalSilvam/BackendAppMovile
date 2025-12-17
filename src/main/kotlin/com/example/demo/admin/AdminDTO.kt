package com.example.demo.admin

import com.example.demo.user.UserRole

data class UserManagementResponse(
    val id: Long,
    val email: String,
    val role: UserRole
)

data class UpdateUserRoleRequest(
    val userId: Long,
    val newRole: UserRole
)