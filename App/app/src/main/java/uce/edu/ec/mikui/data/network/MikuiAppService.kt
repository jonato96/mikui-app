package uce.edu.ec.mikui.data.network

import retrofit2.Response
import uce.edu.ec.mikui.core.RetrofitInstance
import uce.edu.ec.mikui.data.modelo.Get
import uce.edu.ec.mikui.data.modelo.Post

class MikuiAppService {

    suspend fun getPing(): Response<Get> {
        return RetrofitInstance.api.getPing()
    }

    suspend fun pushPost(post: Post): Response<Post> {
        return RetrofitInstance.api.pushPost(post)
    }
}