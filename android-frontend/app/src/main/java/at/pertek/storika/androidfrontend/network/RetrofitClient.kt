package at.pertek.storika.androidfrontend.network

import at.pertek.storika.androidfrontend.api.InventoryApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2:8081"

    val instance: InventoryApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(InventoryApi::class.java)
    }
}
