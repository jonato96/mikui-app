package uce.edu.ec.mikui.core

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import uce.edu.ec.mikui.data.network.MikuiApi
import uce.edu.ec.mikui.util.Constantes.Companion.BASE_URL
//Objeto RetrofitInstance  que crea una instancia de retrofit que trabaja con la url base
object RetrofitInstance {

    private val retrofit by lazy{
        Retrofit.Builder()
            .baseUrl(BASE_URL)//url base para las peticiones
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: MikuiApi by lazy {
        retrofit.create(MikuiApi::class.java)
    }

}