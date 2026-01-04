package es.uniovi.converter

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

data class Rates(
    val USD: Double
)

data class ExchangeRateResponse(
    val amount: Double,
    val base: String,
    val date: String,
    val rates: es.uniovi.converter.Rates
)

interface ExchangeRateApi {
    @GET("latest")
    suspend fun convert(
        @Query("from") from: String,
        @Query("to") to: String,
        @Query("amount") amount: Double
    ): Response<es.uniovi.converter.ExchangeRateResponse>
}

object RetrofitClient {
    private const val BASE_URL = "https://api.frankfurter.app/"

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(es.uniovi.converter.RetrofitClient.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: es.uniovi.converter.ExchangeRateApi by lazy {
        es.uniovi.converter.RetrofitClient.retrofit.create(es.uniovi.converter.ExchangeRateApi::class.java)
    }
}