package uz.urverse.dataloadingpractice.network

import retrofit2.http.GET
import retrofit2.http.Query
import uz.urverse.dataloadingpractice.model.ApiResponse

interface ApiService {

    @GET("photos")
    suspend fun getData(
        @Query("_page") page: Int,
        @Query("_limit") limit: Int
    ): List<ApiResponse>
}
