package knu.dong.teamproject.dto

data class UserInfoDto(
    val id: Long,
    val email: String,
    val rateLimit: Int,
    val currentUsedCount: Int
)
