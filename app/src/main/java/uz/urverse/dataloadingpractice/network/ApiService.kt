package uz.urverse.dataloadingpractice.network

import retrofit2.http.GET
import uz.urverse.dataloadingpractice.model.ApiResponse

interface ApiService {
    @GET("/photos/1")
    suspend fun fetchData(): ApiResponse
}