package knu.dong.teamproject.dto

import kotlinx.serialization.Serializable

@Serializable
data class SendChatReqDto(val chatbotId: Long, val message: String)
