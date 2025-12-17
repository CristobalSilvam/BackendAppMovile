package com.example.demo.group

import com.example.demo.user.UserRepository
import com.example.demo.user.UserRole
import org.springframework.stereotype.Service
import java.lang.IllegalArgumentException

@Service
class GroupService(
    private val groupRepository: GroupRepository,
    private val userRepository: UserRepository
) {

    fun getMyGroupMembers(leaderId: Long): List<GroupMemberResponse> {
        val leader = userRepository.findById(leaderId)
            .orElseThrow { IllegalArgumentException("Líder no encontrado") }

        // Permitimos que ADMIN también vea grupos para debugging
        if (leader.role != UserRole.LEADER && leader.role != UserRole.ADMIN) {
            throw IllegalArgumentException("No tienes permisos de líder")
        }

        val groups = groupRepository.findByLeader(leader)
        return groups.flatMap { it.members }
            .map { GroupMemberResponse(it.id!!, it.email, it.role) }
            .distinctBy { it.id }
    }

    fun addMemberToGroup(request: AddMemberRequest) {
        val leader = userRepository.findById(request.leaderId)
            .orElseThrow { IllegalArgumentException("Líder no encontrado") }
        
        val memberToAdd = userRepository.findByEmail(request.memberEmail)
            ?: throw IllegalArgumentException("Usuario no encontrado (email incorrecto)")

        // CORRECCIÓN AQUÍ: Usamos 'request.groupName' en lugar de 'request.name'
        val group = groupRepository.findByLeader(leader).firstOrNull() 
            ?: GroupModel(name = request.groupName, leader = leader)

        val updatedMembers = group.members.toMutableList()
        
        // Evitamos duplicados
        val isAlreadyMember = updatedMembers.any { it.id == memberToAdd.id }
        if (!isAlreadyMember) {
            updatedMembers.add(memberToAdd)
        }
        
        // Guardamos el grupo con la lista actualizada
        // .copy es muy útil para crear una nueva instancia con un campo modificado
        groupRepository.save(group.copy(members = updatedMembers))
    }
    
    fun removeMemberFromGroup(leaderId: Long, memberId: Long) {
        val leader = userRepository.findById(leaderId)
            .orElseThrow { IllegalArgumentException("Líder no encontrado") }

        val group = groupRepository.findByLeader(leader).firstOrNull() 
            ?: throw IllegalArgumentException("No tienes un grupo asignado")

        val memberToRemove = group.members.find { it.id == memberId }
            ?: throw IllegalArgumentException("El usuario no pertenece a tu grupo")

        val updatedMembers = group.members.toMutableList()
        updatedMembers.remove(memberToRemove)
        
        groupRepository.save(group.copy(members = updatedMembers))
    }
}