package com.example.mi_pequeno_emprendedor.data.venta

import com.example.mi_pequeno_emprendedor.core.supabase.SupabaseConfig
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

data class ProductoVenta(
    val id: Long,
    val nombre: String,
    val detalle: String,
    val precio: Int,
    val stock: Int
)

data class ItemVenta(
    val id: Long,
    val nombre: String,
    val precio: Int,
    val cantidad: Int
)

@Serializable
private data class FilaNegocio(val nombre: String? = null)

@Serializable
private data class FilaProducto(
    val id: Long,
    val nombre: String,
    val precio: Int,
    val stock: Int,
    val detalle: String? = null
)

class VentaRepository(
    private val cliente: SupabaseClient = SupabaseConfig.client
) {

    suspend fun obtenerProductos(): List<ProductoVenta> =
        cliente.postgrest.rpc("mis_productos")
            .decodeList<FilaProducto>()
            .map { fila ->
                ProductoVenta(
                    id = fila.id,
                    nombre = fila.nombre,
                    detalle = fila.detalle.orEmpty(),
                    precio = fila.precio,
                    stock = fila.stock
                )
            }

    suspend fun obtenerNombreNegocio(): String? =
        cliente.postgrest.rpc("mi_negocio")
            .decodeSingleOrNull<FilaNegocio>()
            ?.nombre

    suspend fun registrarVenta(
        items: List<ItemVenta>,
        metodo: String,
        efectivoCentavos: Int,
        cambioCentavos: Int
    ) {
        cliente.postgrest.rpc(
            "registrar_venta",
            buildJsonObject {
                put("items", buildJsonArray {
                    items.forEach { item ->
                        add(buildJsonObject {
                            put("id", item.id)
                            put("nombre", item.nombre)
                            put("cantidad", item.cantidad)
                            put("precio", item.precio * 100)
                            put("subtotal", item.precio * item.cantidad * 100)
                        })
                    }
                })
                put("metodo", metodo)
                put("efectivo", efectivoCentavos)
                put("cambio", cambioCentavos)
            }
        )
    }
}
