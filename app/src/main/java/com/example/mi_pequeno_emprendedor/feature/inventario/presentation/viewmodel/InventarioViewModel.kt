
package com.example.mi_pequeno_emprendedor.feature.inventario.presentation.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.mi_pequeno_emprendedor.feature.inventario.domain.model.Producto
import com.example.mi_pequeno_emprendedor.feature.inventario.domain.model.MovimientoInventario
import com.example.mi_pequeno_emprendedor.feature.inventario.domain.model.TipoMovimiento
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class InventarioViewModel : ViewModel() {

    // Carpetas compartidas por todas las pantallas
    val carpetas = mutableStateListOf(
        "Bebidas",
        "Botanas",
        "Dulces",
        "Accesorios",
        "Sin clasificar"
    )

    // Productos de prueba
    val productos = mutableStateListOf(
        Producto(
            1, "Coca-Cola", "600 ml", "Bebidas",
            14.0, 18.0, 18, "Hoy, 8:15 AM"
        ),
        Producto(
            2, "Agua Ciel", "600 ml", "Bebidas",
            8.0, 12.0, 12, "Hoy, 9:20 AM"
        ),
        Producto(
            3, "Fanta", "600 ml", "Bebidas",
            14.0, 18.0, 6, "Ayer, 6:30 PM"
        ),
        Producto(
            4, "Sprite", "600 ml", "Bebidas",
            14.0, 18.0, 3, "Hoy, 8:15 AM"
        )
    )

    val movimientos = mutableStateListOf<MovimientoInventario>()

    // Indicadores dinámicos
    val productosActivos: Int
        get() = productos.count { it.existencias > 0 }

    val productosStockBajo: Int
        get() = productos.count { it.existencias in 1..3 }

    val productosAgotados: Int
        get() = productos.count { it.existencias == 0 }

    fun agregarCarpeta(nombre: String): Boolean {
        val limpio = nombre.trim()

        if (limpio.isBlank()) return false

        if (limpio.equals("Todos los productos", ignoreCase = true)) {
            return false
        }

        if (carpetas.any { it.equals(limpio, ignoreCase = true) }) {
            return false
        }

        carpetas.add(limpio)
        return true
    }

    fun obtenerProducto(id: Int): Producto? =
        productos.find { it.id == id }

    fun obtenerProductosDeCarpeta(nombre: String): List<Producto> {
        return if (nombre == "Todos los productos") {
            productos.toList()
        } else {
            productos.filter {
                it.carpeta.equals(nombre, ignoreCase = true)
            }
        }
    }

    fun agregarProducto(
        nombre: String,
        presentacion: String,
        carpeta: String,
        precioCompra: Double,
        precioVenta: Double,
        existencias: Int
    ): Boolean {
        if (
            nombre.isBlank() ||
            presentacion.isBlank() ||
            !carpetas.contains(carpeta) ||
            !precioCompra.isFinite() ||
            !precioVenta.isFinite() ||
            precioCompra < 0 ||
            precioVenta < 0 ||
            existencias < 0
        ) return false

        val nuevoId = (productos.maxOfOrNull { it.id } ?: 0) + 1

        val producto = Producto(
            id = nuevoId,
            nombre = nombre.trim(),
            presentacion = presentacion.trim(),
            carpeta = carpeta,
            precioCompra = precioCompra,
            precioVenta = precioVenta,
            existencias = existencias,
            ultimaEntrada = "Registro inicial"
        )

        productos.add(producto)

        if (existencias > 0) {
            registrarMovimiento(
                producto,
                TipoMovimiento.ENTRADA,
                existencias,
                0,
                existencias
            )
        }

        return true
    }

    fun registrarEntrada(id: Int, cantidad: Int): Boolean =
        modificarExistencias(id, cantidad, TipoMovimiento.ENTRADA)

    fun registrarBaja(id: Int, cantidad: Int): Boolean =
        modificarExistencias(id, cantidad, TipoMovimiento.BAJA)

    fun aumentarUnaUnidad(id: Int): Boolean =
        modificarExistencias(id, 1, TipoMovimiento.AJUSTE)

    fun disminuirUnaUnidad(id: Int): Boolean =
        modificarExistencias(id, -1, TipoMovimiento.AJUSTE)

    private fun modificarExistencias(
        id: Int,
        cantidad: Int,
        tipo: TipoMovimiento
    ): Boolean {
        val indice = productos.indexOfFirst { it.id == id }
        if (indice == -1) return false

        val anterior = productos[indice]

        val diferencia = when (tipo) {
            TipoMovimiento.ENTRADA -> {
                if (cantidad <= 0) return false
                cantidad
            }
            TipoMovimiento.BAJA -> {
                if (cantidad <= 0) return false
                -cantidad
            }
            TipoMovimiento.AJUSTE -> {
                if (cantidad != 1 && cantidad != -1) return false
                cantidad
            }
        }

        val nuevoStock = anterior.existencias.toLong() +
                diferencia.toLong()

        if (nuevoStock < 0 || nuevoStock > Int.MAX_VALUE) {
            return false
        }

        productos[indice] = anterior.copy(
            existencias = nuevoStock.toInt(),
            ultimaEntrada = if (diferencia > 0) {
                fechaActual()
            } else {
                anterior.ultimaEntrada
            }
        )

        registrarMovimiento(
            anterior,
            tipo,
            diferencia,
            anterior.existencias,
            nuevoStock.toInt()
        )

        return true
    }

    private fun registrarMovimiento(
        producto: Producto,
        tipo: TipoMovimiento,
        cantidad: Int,
        anterior: Int,
        nuevo: Int
    ) {
        val id = (movimientos.maxOfOrNull { it.id } ?: 0) + 1

        movimientos.add(
            0,
            MovimientoInventario(
                id = id,
                productoId = producto.id,
                nombreProducto = producto.nombre,
                tipo = tipo,
                cantidad = cantidad,
                existenciasAnteriores = anterior,
                existenciasNuevas = nuevo,
                fecha = fechaActual()
            )
        )
    }

    private fun fechaActual(): String {
        val formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
        return LocalDateTime.now().format(formato)
    }

    // =====================================
    // EDITAR INFORMACIÓN DEL PRODUCTO
    // =====================================

    fun editarProducto(
        id: Int,
        nombre: String,
        presentacion: String,
        precioCompra: Double,
        precioVenta: Double
    ): Boolean {

        if (
            nombre.isBlank() ||
            presentacion.isBlank() ||
            !precioCompra.isFinite() ||
            !precioVenta.isFinite() ||
            precioCompra < 0 ||
            precioVenta < 0
        ) {
            return false
        }

        val indice = productos.indexOfFirst {
            it.id == id
        }

        if (indice == -1) {
            return false
        }

        val productoActual = productos[indice]

        productos[indice] = productoActual.copy(
            nombre = nombre.trim(),
            presentacion = presentacion.trim(),
            precioCompra = precioCompra,
            precioVenta = precioVenta
        )

        return true
    }

}
