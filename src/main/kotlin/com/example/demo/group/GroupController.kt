package com.example.demo.group

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/groups")
class GroupController(private val groupService: GroupService) {

    @GetMapping("/my-team")
    fun getTeam(@RequestParam leaderId: Long): ResponseEntity<List<GroupMemberResponse>> {
        return ResponseEntity.ok(groupService.getMyGroupMembers(leaderId))
    }

    @PostMapping("/add-member")
    fun addMember(@RequestBody request: AddMemberRequest): ResponseEntity<Any> {
        return try {
            groupService.addMemberToGroup(request)
            ResponseEntity.ok(mapOf("message" to "Miembro añadido exitosamente"))
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(e.message)
        }
    }
}