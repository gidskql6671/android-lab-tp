package knu.dong.teamproject.dto

import kotlinx.serialization.Serializable

@Serializable
data class Signup(val emailg: String, val verify: Int, val password:String)