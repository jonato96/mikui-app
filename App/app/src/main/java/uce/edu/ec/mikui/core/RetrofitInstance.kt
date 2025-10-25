package uce.edu.ec.mikui.core

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import uce.edu.ec.mikui.data.network.MikuiApi
import uce.edu.ec.mikui.util.Constantes.Companion.BASE_URL
import java.util.concurrent.TimeUnit

//Objeto RetrofitInstance  que crea una instancia de retrofit que trabaja con la url base
object RetrofitInstance {

    // Cliente con timeouts personalizados
    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS) // espera para conectar
        .readTimeout(60, TimeUnit.SECONDS)    // espera para leer respuesta
        .writeTimeout(60, TimeUnit.SECONDS)   // espera para enviar datos
        .build()

    private val retrofit by lazy{
        Retrofit.Builder()
            .baseUrl(BASE_URL)//url base para las peticiones
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    val api: MikuiApi by lazy {
        retrofit.create(MikuiApi::class.java)
    }

}