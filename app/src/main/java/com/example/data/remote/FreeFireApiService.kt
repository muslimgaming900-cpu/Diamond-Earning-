package com.example.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

@JsonClass(generateAdapter = true)
data class PlayerCheckResponse(
    @Json(name = "status") val status: String? = null,
    @Json(name = "nickname") val nickname: String? = null,
    @Json(name = "region") val region: String? = null,
    @Json(name = "uid") val uid: String? = null
)

@JsonClass(generateAdapter = true)
data class TopUpRequest(
    @Json(name = "uid") val uid: String,
    @Json(name = "diamonds") val diamonds: Int,
    @Json(name = "server") val server: String = "GLOBAL",
    @Json(name = "app_id") val appId: String = "com.aistudio.diamondrewards",
    @Json(name = "request_token") val requestToken: String
)

@JsonClass(generateAdapter = true)
data class TopUpResponse(
    @Json(name = "success") val success: Boolean,
    @Json(name = "tx_id") val txId: String?,
    @Json(name = "message") val message: String?,
    @Json(name = "diamonds_delivered") val diamondsDelivered: Int?,
    @Json(name = "delivery_time") val deliveryTime: Long?
)

interface FreeFireApiService {
    @GET("api/player/verify")
    suspend fun verifyPlayer(
        @Query("uid") uid: String,
        @Query("region") region: String = "GLOBAL"
    ): Response<PlayerCheckResponse>

    @POST("api/topup/deliver")
    suspend fun processTopUp(
        @Header("X-API-Signature") signature: String,
        @Body request: TopUpRequest
    ): Response<TopUpResponse>

    companion object {
        private const val BASE_URL = "https://freefireapi.com.br/"

        fun create(): FreeFireApiService {
            val logging = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BASIC
            }

            val client = OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .addInterceptor(logging)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
                .create(FreeFireApiService::class.java)
        }
    }
}
