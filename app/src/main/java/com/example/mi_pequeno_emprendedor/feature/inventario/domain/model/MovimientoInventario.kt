
package com.example.mi_pequeno_emprendedor.feature.inventario.domain.model

enum class TipoMovimiento {
    ENTRADA,
    BAJA,
    AJUSTE
}

data class MovimientoInventario(
    val id: Int,
    val productoId: Int,
    val nombreProducto: String,
    val tipo: TipoMovimiento,
    val cantidad: Int,
    val existenciasAnteriores: Int,
    val existenciasNuevas: Int,
    val fecha: String
)
