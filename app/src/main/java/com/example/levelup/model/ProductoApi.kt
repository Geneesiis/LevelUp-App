// app/src/main/java/com/example/levelup/model/ProductoApi.kt
package com.example.levelup.model

import com.google.gson.annotations.SerializedName

data class ProductoApi(
    @SerializedName("id")
    val id: Int,

    @SerializedName("title")
    val title: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("price")
    val price: Double,

    @SerializedName("image")
    val image: String,

    @SerializedName("category")
    val category: String
)

fun ProductoApi.toProducto(): Producto {
    return Producto(
        id = this.id.toString(),
        nombre = this.title,
        descripcion = this.description,
        precio = this.price,
        imagen = this.image,
        categoria = this.category,
        stock = (10..50).random()
    )
}