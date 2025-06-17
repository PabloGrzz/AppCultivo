package com.example.frontendplantas.models

//nse si esta es necesaria o podria tener una classe Usuario con username pass y email y usar esa...por ahora divide y winnearas


data class RegisterRequest(
    val username:String,
    val password:String,
    val email:String
)
