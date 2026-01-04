package es.uniovi.converter

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _euroToDollar = MutableLiveData<Double>(1.16)
    val euroToDollar: LiveData<Double> get() = _euroToDollar

    private var yaDescargado: Boolean = false

    init {
        Log.d("MainViewModel", "ViewModel created! Fetching data...")
        fetchExchangeRate()
    }

    private fun fetchExchangeRate() {
        if (yaDescargado) return

        viewModelScope.launch {
            try {
                val response = RetrofitClient.api.convert("EUR", "USD", 1.0)
                val exchangeRateResponse = response.body()
                if (!response.isSuccessful || exchangeRateResponse == null) {
                    Log.e("MainViewModel", "Error al obtener el cambio: ${response.code()}")
                    return@launch
                }
                val rate = exchangeRateResponse.rates.USD
                _euroToDollar.postValue(rate)
                yaDescargado = true
                Log.d("MainViewModel", "Cambio actualizado: $rate")
            } catch (e: Exception) {
                Log.e("MainViewModel", "Excepción al obtener el cambio", e)
            }
        }
    }
}