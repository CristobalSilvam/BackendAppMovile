package com.example.demo.user

import com.example.demo.user.UserModel

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<UserModel, Long> {
    // Spring Data JPA entiende este nombre de función y crea la consulta SQL por ti:
    // "SELECT * FROM users WHERE email = ?"
    fun findByEmail(email: String): UserModel?
}