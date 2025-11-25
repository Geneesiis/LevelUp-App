package com.example.levelup.data.remote

import com.example.levelup.model.ProductoApi
import retrofit2.http.GET

interface ApiService {

    @GET("products")
    suspend fun getProductos(): List<ProductoApi>

    @GET("products/category/electronics")
    suspend fun getProductosElectronics(): List<ProductoApi>
}