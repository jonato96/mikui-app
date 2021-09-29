package uce.edu.ec.mikui.data.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import uce.edu.ec.mikui.data.modelo.Get
import uce.edu.ec.mikui.data.modelo.Post

interface MikuiApi {

    @GET("ping")
    suspend fun getPing(): Response<Get>

    @POST("prediccion")
    suspend fun pushPost(
        @Body post: Post
    ): Response<Post>

}