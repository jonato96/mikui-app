package uce.edu.ec.mikui.data

import retrofit2.Response
import uce.edu.ec.mikui.core.RetrofitInstance
import uce.edu.ec.mikui.data.modelo.Get

class DataSource {

    suspend fun getPing(): Response<Get>{
        return RetrofitInstance.api.getPing()
    }

}