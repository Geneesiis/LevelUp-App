package com.example.levelup.model

import com.google.gson.annotations.SerializedName

data class ProductoApi(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("price") val price: Double,
    @SerializedName("description") val description: String,
    @SerializedName("category") val category: String,
    @SerializedName("image") val image: String
)

// Función para convertir ProductoApi → Producto (tu modelo local)
fun ProductoApi.toProducto(): Producto {
    return Producto(
        id = this.id.toString(),
        nombre = this.title,
        descripcion = this.description,
        precio = this.price,
        imagen = this.image,
        categoria = this.category,
        stock = 100 // Stock por defecto ya que la API no lo proporciona
    )
}