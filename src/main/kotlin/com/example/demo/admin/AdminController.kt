package com.example.demo.admin

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/admin")
class AdminController(private val adminService: AdminService) {

    @GetMapping("/users")
    fun listUsers(@RequestParam adminId: Long): ResponseEntity<List<UserManagementResponse>> {
        return ResponseEntity.ok(adminService.getAllUsers(adminId))
    }

    @PutMapping("/users/role")
    fun changeRole(
        @RequestParam adminId: Long, 
        @RequestBody request: UpdateUserRoleRequest
    ): ResponseEntity<Any> {
        adminService.updateUserRole(adminId, request)
        return ResponseEntity.ok(mapOf("message" to "Rol actualizado exitosamente"))
    }

    @DeleteMapping("/users/{userId}")
    fun removeUser(
        @RequestParam adminId: Long, 
        @PathVariable userId: Long
    ): ResponseEntity<Any> {
        adminService.deleteUser(adminId, userId)
        return ResponseEntity.noContent().build()
    }
}