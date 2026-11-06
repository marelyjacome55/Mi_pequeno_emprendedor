package com.example.mi_pequeno_emprendedor.ui.venta

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mi_pequeno_emprendedor.data.venta.ItemVenta
import com.example.mi_pequeno_emprendedor.data.venta.ProductoVenta
import com.example.mi_pequeno_emprendedor.data.venta.VentaRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class VentaUiState(
    val productos: List<ProductoVenta> = emptyList(),
    val nombreNegocio: String = "Mi negocio",
    val cargando: Boolean = true,
    val error: String? = null,
    val guardandoVenta: Boolean = false,
    val ventaExitosa: Boolean = false,
    val errorVenta: String? = null
)

class VentaViewModel(
    private val repositorio: VentaRepository = VentaRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(VentaUiState())
    val uiState: StateFlow<VentaUiState> = _uiState.asStateFlow()

    init {
        cargarProductos()
    }

    fun cargarProductos() {
        viewModelScope.launch {
            _uiState.update { it.copy(cargando = true, error = null) }
            try {
                val productos = repositorio.obtenerProductos()
                val negocio = runCatching { repositorio.obtenerNombreNegocio() }.getOrNull()
                _uiState.update {
                    it.copy(
                        productos = productos,
                        nombreNegocio = negocio ?: "Mi negocio",
                        cargando = false
                    )
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        cargando = false,
                        error = "No se pudo cargar el inventario"
                    )
                }
            }
        }
    }

    fun registrarVenta(
        items: List<ItemVenta>,
        metodo: String,
        efectivoCentavos: Int,
        cambioCentavos: Int
    ) {
        if (items.isEmpty() || _uiState.value.guardandoVenta) return
        viewModelScope.launch {
            _uiState.update { it.copy(guardandoVenta = true, errorVenta = null) }
            try {
                repositorio.registrarVenta(items, metodo, efectivoCentavos, cambioCentavos)
                _uiState.update { it.copy(guardandoVenta = false, ventaExitosa = true) }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        guardandoVenta = false,
                        errorVenta = e.message ?: "No se pudo registrar la venta"
                    )
                }
            }
        }
    }

    fun consumirVentaExitosa() = _uiState.update { it.copy(ventaExitosa = false) }

    fun consumirErrorVenta() = _uiState.update { it.copy(errorVenta = null) }
}
