package com.example.demo.user

import jakarta.persistence.*

@Entity
@Table(name = "users") // Nombre de la tabla en la base de datos
data class UserModel(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(unique = true, nullable = false) // El email debe ser único
    val email: String,

    @Column(nullable = false)
    val passwordHash: String, // Guardaremos la contraseña (simulada) aquí
    
    @Enumerated(EnumType.STRING) // Guarda el nombre del rol (ej: "ADMIN") en la DB
    @Column(nullable = false)
    val role: UserRole = UserRole.USER // Por defecto todos son usuarios base
    // Aquí podrías añadir 'nombre', 'apellido', etc.
)