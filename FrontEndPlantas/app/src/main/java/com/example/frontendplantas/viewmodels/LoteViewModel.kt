package com.example.frontendplantas.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontendplantas.core.RetrofitClient
import com.example.frontendplantas.models.Lote
import com.example.frontendplantas.models.Planta
import com.example.frontendplantas.models.ProcesoConFecha
import com.example.frontendplantas.repository.PlantaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class LoteViewModel : ViewModel() {

    private val plantaRepository = PlantaRepository()

    private val _loteCreado = MutableLiveData<Lote>()
    val loteCreado: LiveData<Lote?> get() = _loteCreado

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _errorCreacion = MutableLiveData<String>()
    val errorCreacion: LiveData<String> get() = _errorCreacion

    private val _loteSolo = MutableLiveData<Lote>()
    val loteSolo: LiveData<Lote> get() = _loteSolo

    private val _loteEliminacion = MutableLiveData<String>()
    val loteEliminacion : LiveData<String> get() = _loteEliminacion

    private val _errorEliminacion = MutableLiveData<String>()
    val errorEliminacion: LiveData<String> get() = _errorEliminacion

    private val _loteActualizado = MutableLiveData<Lote>()
    val loteActualizado: LiveData<Lote> get() = _loteActualizado

    private val _errorProceso = MutableLiveData<String>()
    val errorProceso: LiveData<String> get() = _errorProceso

    private val _currentLote = MutableLiveData<Lote?>()
    val currentLote: LiveData<Lote?> get() = _currentLote

    private val _loteState = MutableLiveData<LoteState>()
    val loteState: LiveData<LoteState> get() = _loteState

    private val _operationState = MutableLiveData<OperationState>()
    val operationState: LiveData<OperationState> get() = _operationState

    sealed class LoteState {
        object Loading : LoteState()
        data class Success(val lote: Lote) : LoteState()
        data class Error(val message: String) : LoteState()
    }

    sealed class OperationState {
        object Loading : OperationState()
        data class Success(val message: String) : OperationState()
        data class Error(val message: String) : OperationState()
    }

    fun loadLoteData(loteId: Long) {
        viewModelScope.launch {
            val response = RetrofitClient.webService.getLoteById(loteId)
            if (response.isSuccessful) {
                _currentLote.value = response.body()
            } else {
                // Manejar error
            }
        }
    }

    /*fun resetLoteCreado() {
        _loteCreado.value = null
    }*/

    fun createLote(lote: Lote) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _isLoading.postValue(true)
                Log.d("LoteViewModel", "Enviando solicitud para crear lote")
                Log.d("LoteViewModel", "Planta ID: ${lote.planta?.id}")
                Log.d("LoteViewModel", "Procesos: ${lote.procesos}")

                val response = RetrofitClient.webService.createLote(lote)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        Log.d("LoteViewModel", "Lote creado exitosamente: ${response.body()}")
                        _loteCreado.value = response.body()?.copy() // 👈 Usa copy()!
                    } else {
                        Log.e("LoteViewModel", "Error al crear lote. Código: ${response.code()}")
                        Log.e("LoteViewModel", "Mensaje de error: ${response.errorBody()?.string()}")
                        _errorCreacion.value = "Error al crear el lote: ${response.code()}"
                    }
                    _isLoading.value = false
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("LoteViewModel", "Error de conexión: ${e.message}", e)
                    _errorCreacion.value = "Error de conexión: ${e.message}"
                    _isLoading.value = false
                }
            }
        }
    }

    fun getLoteById(id: Long) {
        viewModelScope.launch {
            try {
                _loteState.value = LoteState.Loading
                val loteResult = plantaRepository.getLoteById(id)

                if (loteResult.isSuccess) {
                    val lote = loteResult.getOrNull()
                    if (lote != null) {
                        if (lote.planta == null && lote.plantaId != null) {
                            val plantaResult = plantaRepository.obtenerPlantaPorID(lote.plantaId)
                            if (plantaResult.isSuccess) {
                                val planta = plantaResult.getOrNull()
                                val loteConPlanta = lote.copy(planta = planta)
                                _loteState.value = LoteState.Success(loteConPlanta)
                            } else {
                                _loteState.value = LoteState.Success(lote)
                            }
                        } else {
                            _loteState.value = LoteState.Success(lote)
                        }
                    } else {
                        _loteState.value = LoteState.Error("Lote no encontrado")
                    }
                } else {
                    _loteState.value = LoteState.Error("Error al cargar lote: ${loteResult.exceptionOrNull()?.message}")
                }
            } catch (e: Exception) {
                _loteState.value = LoteState.Error(e.message ?: "Error desconocido")
            }
        }
    }



    fun eliminarLote(loteId: Long) {
        _operationState.value = OperationState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitClient.webService.eliminarLote(loteId)
                if (response.isSuccessful) {
                    _operationState.postValue(OperationState.Success("Lote eliminado"))
                } else {
                    _operationState.postValue(OperationState.Error("Error al eliminar"))
                }
            } catch (e: Exception) {
                _operationState.postValue(OperationState.Error(e.message ?: "Error desconocido"))
            }
        }
    }

    fun agregarProcesoALote(loteId: Long, proceso: ProcesoConFecha) {
        _operationState.value = OperationState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitClient.webService.agregarProcesoAElLote(loteId, proceso)
                if (response.isSuccessful) {
                    _operationState.postValue(OperationState.Success("Proceso agregado"))
                    getLoteById(loteId) // Refrescar datos
                } else {
                    _operationState.postValue(OperationState.Error("Error al agregar proceso"))
                }
            } catch (e: Exception) {
                _operationState.postValue(OperationState.Error(e.message ?: "Error desconocido"))
            }
        }
    }
}