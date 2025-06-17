package com.example.frontendplantas.repository

import com.example.frontendplantas.core.RetrofitClient
import com.example.frontendplantas.models.Imagenes
import com.example.frontendplantas.models.Lote
import com.example.frontendplantas.models.Planta
import com.example.frontendplantas.models.ProcesoConFecha
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PlantaRepository {

    // Plantas
    suspend fun obtenerPlantas(): Result<List<Planta>> = withContext(Dispatchers.IO) {
        try {
            val response = RetrofitClient.webService.obtenerPlantas()
            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(Exception("Error ${response.code()}: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun obtenerPlantaPorID(plantaID: Long): Result<Planta> = withContext(Dispatchers.IO) {
        try {
            val response = RetrofitClient.webService.obtenerPlantaPorID(plantaID)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error ${response.code()}: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun obtenerPlantaPorNombre(nombrePlanta: String): Result<List<Planta>> = withContext(Dispatchers.IO) {
        try {
            val response = RetrofitClient.webService.obtenerPlantaPorNombre(nombrePlanta)
            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(Exception("Error ${response.code()}: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun crearPlanta(planta: Planta): Result<Planta> = withContext(Dispatchers.IO) {
        try {
            val response = RetrofitClient.webService.createPlanta(planta)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error ${response.code()}: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun eliminarPlanta(plantaID: Long): Result<Boolean> = withContext(Dispatchers.IO) {
        try {
            val response = RetrofitClient.webService.eliminarPlanta(plantaID)
            if (response.isSuccessful) {
                Result.success(true)
            } else {
                Result.failure(Exception("Error ${response.code()}: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Lotes
    suspend fun getLoteById(loteID: Long): Result<Lote> = withContext(Dispatchers.IO) {
        try {
            val response = RetrofitClient.webService.getLoteById(loteID)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error ${response.code()}: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun crearLote(lote: Lote): Result<Lote> = withContext(Dispatchers.IO) {
        try {
            val response = RetrofitClient.webService.createLote(lote)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error ${response.code()}: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun eliminarLote(loteID: Long): Result<Boolean> = withContext(Dispatchers.IO) {
        try {
            val response = RetrofitClient.webService.eliminarLote(loteID)
            if (response.isSuccessful) {
                Result.success(true)
            } else {
                Result.failure(Exception("Error ${response.code()}: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun agregarProcesoALote(loteId: Long, proceso: ProcesoConFecha): Result<Lote> = withContext(Dispatchers.IO) {
        try {
            val response = RetrofitClient.webService.agregarProcesoAElLote(loteId, proceso)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error ${response.code()}: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Imágenes
    suspend fun obtenerImagenesGenerales(plantaId: Long): Result<List<Imagenes>> = withContext(Dispatchers.IO) {
        try {
            val response = RetrofitClient.webService.obtenerImagenesGenerales(plantaId)
            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(Exception("Error ${response.code()}: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun obtenerImagenesPersonales(plantaId: Long, usuarioId: Long): Result<List<Imagenes>> = withContext(Dispatchers.IO) {
        try {
            val response = RetrofitClient.webService.obtenerImagenesPersonales(plantaId, usuarioId)
            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(Exception("Error ${response.code()}: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}