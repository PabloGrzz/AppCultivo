package com.example.frontendplantas.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontendplantas.core.RetrofitClient
import com.example.frontendplantas.models.Planta
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class PlantasViewModel : ViewModel() {

    // Lista de plantas
    val _listaPlantas = MutableLiveData<List<Planta>>()
    val listaPlantas: LiveData<List<Planta>> get() = _listaPlantas

    private val _listaPlantasNombre = MutableLiveData<List<Planta>>()
    val listaPlantasNombre: LiveData<List<Planta>> get() = _listaPlantasNombre

    // Detalle de una planta individual
    private val _plantaDetalle = MutableLiveData<Planta>()
    val plantaDetalle: LiveData<Planta> get() = _plantaDetalle

    private val _resultadoEliminacion = MutableLiveData<String>()
    val resultadoEliminacion: LiveData<String> get() = _resultadoEliminacion

    private val _plantaCreada = MutableLiveData<Planta>()
    val plantaCreada: LiveData<Planta> get() = _plantaCreada

    private val _errorCreacion = MutableLiveData<String>()
    val errorCreacion: LiveData<String> get() = _errorCreacion

    // Estado de carga y errores
    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _error = MutableLiveData<String?>(null)
    val error: LiveData<String?> get() = _error

    private val _searchQuery = MutableLiveData<String>("")
    val searchQuery: LiveData<String> get() = _searchQuery

    private val _plantasFiltradas = MediatorLiveData<List<Planta>>()
    val plantasFiltradas: LiveData<List<Planta>> get() = _plantasFiltradas

    init {
        _plantasFiltradas.addSource(_listaPlantas) { plantas ->
            // Asegúrate de inicializar las plantas filtradas con todas las plantas
            // cuando se carga la lista de plantas por primera vez
            aplicarFiltro()
        }
        _plantasFiltradas.addSource(_searchQuery) {
            aplicarFiltro()
        }
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
        aplicarFiltro()
    }

    private fun aplicarFiltro() {
        val query = _searchQuery.value ?: ""
        val plantas = _listaPlantas.value ?: emptyList()

        _plantasFiltradas.value = if (query.isBlank()) {
            plantas
        } else {
            plantas.filter { planta ->
                (planta.nombre?.contains(query, ignoreCase = true) ?: false) ||
                        (planta.nombreCientifico?.contains(query, ignoreCase = true) ?: false)
            }
        }
    }

    fun createPlanta(planta: Planta) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _isLoading.postValue(true)
                val response = RetrofitClient.webService.createPlanta(planta)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        _plantaCreada.value = response.body() // Planta creada con éxito
                        // Actualizar la lista de plantas después de crear una nueva
                        obtenerPlantas()
                    } else {
                        _errorCreacion.value = "Error al crear la planta: ${response.code()}"
                    }
                    _isLoading.value = false
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _errorCreacion.value = "Error de conexión: ${e.message}"
                    _isLoading.value = false
                }
            }
        }
    }

    // En PlantasViewModel.kt
    fun obtenerPlantas() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                println("🔄 Obteniendo plantas...") // Debug
                _isLoading.postValue(true)
                _error.postValue(null)

                val response = RetrofitClient.webService.obtenerPlantas()
                println("📡 Respuesta: ${response.code()}") // Debug

                if (response.isSuccessful) {
                    val plantasRecibidas = response.body() ?: emptyList()
                    println("✅ Plantas recibidas: ${plantasRecibidas.size}") // Debug
                    _listaPlantas.postValue(plantasRecibidas)
                } else {
                    println("❌ Error: ${response.errorBody()?.string()}") // Debug
                    _error.postValue("Error: ${response.code()}")
                }
            } catch (e: Exception) {
                println("⚠️ Excepción: ${e.message}") // Debug
                _error.postValue("Error: ${e.message}")
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    fun findPlantaByName(nombrePlanta: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _isLoading.postValue(true)
                _error.postValue(null)

                val response = RetrofitClient.webService.obtenerPlantaPorNombre(nombrePlanta)
                if (response.isSuccessful) {
                    _listaPlantasNombre.postValue(response.body())
                } else {
                    _error.postValue("Error: ${response.code()}")
                    _listaPlantasNombre.postValue(emptyList())
                }
            } catch (e: Exception) {
                _error.postValue("Error de conexión: ${e.message}")
                _listaPlantasNombre.postValue(emptyList())
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    fun obtenerPlantaPorID(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _isLoading.postValue(true)
                _error.postValue(null)

                val response = RetrofitClient.webService.obtenerPlantaPorID(id)
                if (response.isSuccessful) {
                    _plantaDetalle.postValue(response.body())
                } else {
                    _error.postValue("Error al obtener la planta: ${response.code()}")
                }
            } catch (e: Exception) {
                _error.postValue("Error de conexión: ${e.message}")
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    fun eliminarPlanta(plantaID: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _isLoading.postValue(true)
                val response = RetrofitClient.webService.eliminarPlanta(plantaID)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        _resultadoEliminacion.value = "Planta eliminada exitosamente"
                        // Actualizar la lista de plantas después de eliminar
                        obtenerPlantas()
                    } else {
                        _resultadoEliminacion.value = "Error al eliminar la planta: ${response.code()}"
                    }
                    _isLoading.value = false
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _resultadoEliminacion.value = "Error de conexión: ${e.message}"
                    _isLoading.value = false
                }
            }
        }
    }
}