package org.jik.notification_proto.api

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface APIS {

    @POST("/info")
    @Headers("accept: application/json",
            "content-type: application/json")
    fun post_users(
            @Body jsonparams: PostModel
    ): Call<String>

    @HTTP(method = "DELETE", path = "/delete", hasBody = true)
    @Headers("accept: application/json",
            "content-type: application/json")
    fun delete_users(
            @Body jsonparams: DeleteModel
    ): Call<String>

    @HTTP(method = "PATCH", path = "/update", hasBody = true)
    @Headers("accept: application/json",
            "content-type: application/json")
    fun update_users(
            @Body jsonparams: UpdateModel
    ): Call<String>

    @POST("/student")
    @Headers("accept: application/json",
            "content-type: application/json")
    fun initial_users(
            @Body jsonparams: InitialModel
    ): Call<String>

    @GET("/popular")
    @Headers("accept: application/json",
            "content-type: application/json")
    fun get_users(
            @Query("keyword") keyword : String,
            @Query("count(*)") count : String,
    ): Call<List<GetModel>>

    companion object { // static 처럼 공유객체로 사용가능함. 모든 인스턴스가 공유하는 객체로서 동작함.
        private const val BASE_URL = "https://notice-alarm.inuappcenter.kr" // 주소

        fun create(): APIS {

            val gson : Gson =   GsonBuilder().setLenient().create();

            return Retrofit.Builder()
                    .baseUrl(BASE_URL)
//                  .client(client)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()
                    .create(APIS::class.java)
        }
    }
}