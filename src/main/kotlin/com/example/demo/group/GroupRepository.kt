package com.example.demo.group

import com.example.demo.user.UserModel
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface GroupRepository : JpaRepository<GroupModel, Long> {
        
    fun findByLeader(leader: UserModel): List<GroupModel>
    
    fun existsByLeaderAndMembersContaining(leader: UserModel, member: UserModel): Boolean
}