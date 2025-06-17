package com.example.frontendplantas.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.palette.graphics.Palette

class PaletteViewModel : ViewModel() {

    val paletteLiveData = MutableLiveData<Palette>()

    fun actualizarPaleta (palette: Palette){
        paletteLiveData.postValue(palette)
    }
}