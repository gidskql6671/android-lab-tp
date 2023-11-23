package knu.dong.teamproject.dto

import kotlinx.serialization.Serializable

@Serializable //이거 없으면 에러 나는 이유?
data class LoginRequestDto(val email: String, val password: String)