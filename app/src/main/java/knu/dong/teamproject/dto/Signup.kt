package knu.dong.teamproject.dto

import kotlinx.serialization.Serializable

@Serializable
data class Signup(val email: String, val verify: Int, val password:String)