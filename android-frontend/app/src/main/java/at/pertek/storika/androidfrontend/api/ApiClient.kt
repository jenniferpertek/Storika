package at.pertek.storika.androidfrontend.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    private const val BASE_URL = "http://10.0.2.2:5050/api" // für Emulator

    val categoryApi: CategoryApi by lazy {
        retrofit.create(CategoryApi::class.java)
    }

    val itemApi: ItemApi by lazy {
        retrofit.create(ItemApi::class.java)
    }

    val compartmentApi: CompartmentApi by lazy {
        retrofit.create(CompartmentApi::class.java)
    }

    val locationApi: LocationApi by lazy {
        retrofit.create(LocationApi::class.java)
    }

    val storageUnitApi: StorageUnitApi by lazy {
        retrofit.create(StorageUnitApi::class.java)
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

}
