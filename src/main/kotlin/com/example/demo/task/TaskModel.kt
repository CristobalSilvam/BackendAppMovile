package com.example.demo.task 


import com.example.demo.user.UserModel
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "tasks")
data class TaskModel(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    var title: String,

    var description: String? = null,
    var location: String? = null,

    @Column(nullable = false)
    var priority: String, // ALTA, MEDIA, BAJA

    @Column(name = "is_completed", nullable = false)
    var isCompleted: Boolean = false,

    val reminderTime: LocalDateTime? = null,

    //Relación con Usuario
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    val user: UserModel
)