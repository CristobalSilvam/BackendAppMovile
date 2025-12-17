package com.example.demo.group

import com.example.demo.user.UserModel
import jakarta.persistence.*


@Entity
@Table(name = "user_groups")
data class GroupModel(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val name: String,

    @ManyToOne
    @JoinColumn(name = "leader_id", nullable = false)
    val leader: UserModel, // Este debe ser un usuario con rol LEADER

    @ManyToMany
    @JoinTable(
        name = "group_members",
        joinColumns = [JoinColumn(name = "group_id")],
        inverseJoinColumns = [JoinColumn(name = "user_id")]
    )
    val members: List<UserModel> = mutableListOf()
)