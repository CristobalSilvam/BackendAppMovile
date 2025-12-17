package com.example.demo.group

import com.example.demo.user.UserRole

data class GroupMemberResponse(
    val id: Long,
    val email: String,
    val role: UserRole
)


data class AddMemberRequest(
    val leaderId: Long,
    val memberEmail: String,
    val groupName: String = "Mi Equipo Principal" 
)