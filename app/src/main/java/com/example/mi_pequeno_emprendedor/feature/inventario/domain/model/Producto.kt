package com.example.mi_pequeno_emprendedor.feature.inventario.domain.model

data class Producto(
    val id: Int,
    val nombre: String,
    val presentacion: String,
    val carpeta: String,
    val precioCompra: Double,
    val precioVenta: Double,
    val existencias: Int,
    val ultimaEntrada: String
)
