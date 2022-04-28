package ar.edu.unsam.algo

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

val mapper = jacksonObjectMapper()

/*data class User (
    val id: Int,
    val username: String,
    val password: String,
    val fullName: String
)

val user = User(102, "test", "pass12", "Test User")
val userJson: String = mapper.writeValueAsString(user)*/
