package com.example.frontendplantas.network

import com.example.frontendplantas.models.AuthResponse
import com.example.frontendplantas.models.Imagenes
import com.example.frontendplantas.models.LoginRequest
import com.example.frontendplantas.models.Lote
import com.example.frontendplantas.models.Planta
import com.example.frontendplantas.models.ProcesoConFecha
import com.example.frontendplantas.models.RegisterRequest
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface WebService {

    @GET("plantas")
    suspend fun obtenerPlantas(): Response<List<Planta>>

    @POST("plantas/crear")
    suspend fun createPlanta(
        @Body planta: Planta
    ):Response<Planta>

    @GET("plantas/nombre/{nombre}")
    suspend fun obtenerPlantaPorNombre(
        @Path("nombre") nombrePlanta:String
    ): Response<List<Planta>>

    @GET("plantas/{id}")
    suspend fun obtenerPlantaPorID(
        @Path("id") IDPlanta : Long
    ): Response<Planta>

    @DELETE("plantas/{id}")
    suspend fun eliminarPlanta(
        @Path("id") id: Long
    ): Response<String>

    @GET("imagenes/{plantaId}/general")
    suspend fun obtenerImagenesGenerales(
        @Path("plantaId") plantaId: Long
    ): Response<List<Imagenes>>

    @GET("/imagenes/{plantaId}/personal/{usuarioId}")
    suspend fun obtenerImagenesPersonales(
        @Path("plantaId") plantaId: Long,
        @Path("usuarioId") usuarioId : Long
    ): Response<List<Imagenes>>

    @Multipart
    @POST("imagenes/generales/{plantaId}")
    suspend fun subirImagenGeneral(
        @Path("plantaId") plantaId: Long,
        @Part imagen: MultipartBody.Part
    ): Response<Imagenes>

    @Multipart
    @POST("imagenes/personales/{plantaId}")
    suspend fun subirImagenPersonal(
        @Path("plantaId") plantaId: Long,
        @Part imagen: MultipartBody.Part
    ): Response<Imagenes>

    @POST("auth/login")
    suspend fun hacerLogin(
        @Body request : LoginRequest
    ):Response<AuthResponse> // devuelve el token

    @POST("auth/register")
    suspend fun registrarse(
        @Body request: RegisterRequest
    ):Response<AuthResponse> // al registrarte tambien te devuelve el token, para que no tengas que hacer el login despues de registrarte
    // -> esto se hace si no requiere verificacion de email por ejemplo

    @GET("lote/{id}")
    suspend fun getLoteById(
        @Path("id") IDLote : Long
    ): Response<Lote>

    @POST("lote/crear")
    suspend fun createLote(
        @Body lote: Lote
    ):Response<Lote>

    @DELETE("lote/{id}")
    suspend fun eliminarLote(
        @Path("id") id: Long
    ): Response<String>

    @POST("lote/{id}/proceso")
    suspend fun agregarProcesoAElLote(
        @Path("id") id: Long,
        @Body procesoConFecha: ProcesoConFecha
    ): Response<Lote>
}