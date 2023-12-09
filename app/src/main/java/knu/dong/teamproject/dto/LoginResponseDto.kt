package knu.dong.teamproject.dto

import kotlinx.serialization.Serializable

@Serializable()
data class LoginResponseDto(val id: Long, val email: String)
