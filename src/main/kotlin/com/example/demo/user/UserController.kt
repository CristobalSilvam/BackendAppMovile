package com.example.demo.user

import com.example.demo.user.dto.UserResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users")
class UserController(private val userService: UserService) {

    // GET /api/users?userId=1 (El ID del admin que hace la petición)
    @GetMapping
    fun getAllUsers(@RequestParam userId: Long): ResponseEntity<Any> {
        return try {
            val users = userService.getAllUsers(userId)
            ResponseEntity.ok(users)
        } catch (e: Exception) {
            // Si no es admin, devolvemos 403 Forbidden
            ResponseEntity.status(403).body(e.message)
        }
    }

    // DELETE /api/users/{id}?userId=1
    @DeleteMapping("/{id}")
    fun deleteUser(
        @PathVariable id: Long,      // El ID del usuario a borrar
        @RequestParam userId: Long   // El ID del admin que ordena el borrado
    ): ResponseEntity<Any> {
        return try {
            userService.deleteUser(id, userId)
            ResponseEntity.noContent().build()
        } catch (e: Exception) {
            ResponseEntity.status(403).body(e.message)
        }
    }
}