
package com.example.mi_pequeno_emprendedor.feature.inventario.presentation.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mi_pequeno_emprendedor.feature.inventario.domain.model.Producto
import com.example.mi_pequeno_emprendedor.feature.inventario.presentation.viewmodel.InventarioViewModel
import com.example.mi_pequeno_emprendedor.ui.components.NavegacionInferior

private val AzulCarpeta = Color(0xFF0066E8)
private val AzulOscuroCarpeta = Color(0xFF003BA6)
private val TextoCarpeta = Color(0xFF101560)
private val GrisCarpeta = Color(0xFF667092)
private val VerdeCarpeta = Color(0xFF008E57)
private val RojoCarpeta = Color(0xFFFF564B)
private val BordeCarpeta = Color(0xFFE4EBF6)

@Composable
fun CarpetaScreen(
    nombreCarpeta: String,
    inventarioViewModel: InventarioViewModel,
    alVolver: () -> Unit,
    alAbrirProducto: (Int) -> Unit = {},
    alCambiarSeccion: (String) -> Unit = {}
) {
    var busqueda by rememberSaveable(nombreCarpeta) {
        mutableStateOf("")
    }
    var filtro by rememberSaveable(nombreCarpeta) {
        mutableStateOf("Todos")
    }
    var menuFiltros by remember { mutableStateOf(false) }
    var operacion by remember { mutableStateOf<String?>(null) }

    val productos = inventarioViewModel
        .obtenerProductosDeCarpeta(nombreCarpeta)

    val productosVisibles = productos.filter { producto ->
        producto.nombre.contains(busqueda.trim(), ignoreCase = true) &&
                when (filtro) {
                    "Disponibles" -> producto.existencias > 0
                    "Stock bajo" -> producto.existencias in 1..3
                    "Agotados" -> producto.existencias == 0
                    else -> true
                }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.White,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            NavegacionInferior(
                seccionSeleccionada = "Inicio",
                alCambiarSeccion = alCambiarSeccion
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color.White)
                .verticalScroll(rememberScrollState())
        ) {
            // Encabezado sin menú de hamburguesa
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(190.dp)
                    .background(
                        Brush.linearGradient(
                            listOf(
                                Color(0xFF0878FF),
                                AzulOscuroCarpeta
                            )
                        )
                    )
            ) {
                Canvas(Modifier.fillMaxSize()) {
                    val w = size.width
                    val h = size.height
                    val onda = Path().apply {
                        moveTo(0f, h * 0.8f)
                        cubicTo(
                            w * 0.3f, h * 0.6f,
                            w * 0.45f, h * 1.05f,
                            w * 0.68f, h * 0.8f
                        )
                        cubicTo(
                            w * 0.84f, h * 0.68f,
                            w * 0.9f, h * 0.8f,
                            w, h * 0.68f
                        )
                        lineTo(w, h)
                        lineTo(0f, h)
                        close()
                    }
                    drawPath(
                        onda,
                        Color(0xFF49A2FF).copy(alpha = 0.45f)
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .statusBarsPadding()
                        .padding(18.dp),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Text(
                        "Inventario",
                        color = Color.White,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "Inventario  ›  $nombreCarpeta",
                        color = Color(0xFFE0EDFF),
                        fontSize = 15.sp
                    )
                }
            }

            Column(
                modifier = Modifier.padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Surface(
                    onClick = alVolver,
                    color = Color(0xFFF0F5FF),
                    shape = RoundedCornerShape(13.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(
                            horizontal = 12.dp,
                            vertical = 10.dp
                        ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = null,
                            tint = AzulCarpeta
                        )
                        Spacer(Modifier.width(9.dp))
                        Text(
                            "Ver todas las carpetas",
                            color = AzulCarpeta,
                            fontSize = 14.sp
                        )
                    }
                }

                // Información de la carpeta
                Surface(
                    shape = RoundedCornerShape(15.dp),
                    color = Color.White,
                    border = BorderStroke(1.dp, BordeCarpeta),
                    shadowElevation = 3.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Inventory2,
                            contentDescription = null,
                            tint = AzulCarpeta,
                            modifier = Modifier.size(55.dp)
                        )
                        Spacer(Modifier.width(16.dp))
                        Column {
                            Text(
                                nombreCarpeta,
                                fontSize = 23.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextoCarpeta
                            )
                            Text(
                                "${productos.size} productos",
                                color = Color(0xFF716FE3),
                                fontSize = 16.sp
                            )
                        }
                    }
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = busqueda,
                        onValueChange = { busqueda = it },
                        modifier = Modifier.weight(1f),
                        placeholder = {
                            Text(
                                "Buscar producto",
                                fontSize = 12.sp
                            )
                        },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Search,
                                contentDescription = null,
                                tint = AzulCarpeta
                            )
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp)
                    )

                    Box {
                        TextButton(onClick = { menuFiltros = true }) {
                            Icon(
                                Icons.Default.FilterList,
                                contentDescription = null
                            )
                            Text("Filtrar")
                        }

                        DropdownMenu(
                            expanded = menuFiltros,
                            onDismissRequest = { menuFiltros = false }
                        ) {
                            listOf(
                                "Todos",
                                "Disponibles",
                                "Stock bajo",
                                "Agotados"
                            ).forEach { opcion ->
                                DropdownMenuItem(
                                    text = { Text(opcion) },
                                    onClick = {
                                        filtro = opcion
                                        menuFiltros = false
                                    }
                                )
                            }
                        }
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Color(0xFFEAF3FF),
                            RoundedCornerShape(14.dp)
                        )
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Info,
                        contentDescription = null,
                        tint = AzulCarpeta
                    )
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text(
                            "Movimientos por lote o caja",
                            color = AzulCarpeta,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "Registra entradas o bajas de mercancía.",
                            color = GrisCarpeta,
                            fontSize = 12.sp
                        )
                    }
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(7.dp)
                ) {
                    AccionCarpeta(
                        "Nuevo producto",
                        "Registrar producto",
                        "+",
                        AzulCarpeta,
                        Color(0xFFEAF3FF),
                        Modifier.weight(1f)
                    ) { operacion = "Nuevo" }

                    AccionCarpeta(
                        "Entrada de mercancía",
                        "Agregar unidades",
                        "+",
                        VerdeCarpeta,
                        Color(0xFFE5FAF0),
                        Modifier.weight(1f)
                    ) { operacion = "Entrada" }

                    AccionCarpeta(
                        "Registrar baja",
                        "Descontar unidades",
                        "−",
                        RojoCarpeta,
                        Color(0xFFFFECEA),
                        Modifier.weight(1f)
                    ) { operacion = "Baja" }
                }

                Text(
                    "Productos de la carpeta",
                    color = TextoCarpeta,
                    fontSize = 21.sp,
                    fontWeight = FontWeight.Bold
                )

                if (productosVisibles.isEmpty()) {
                    Text(
                        "No se encontraron productos.",
                        color = GrisCarpeta,
                        modifier = Modifier.padding(16.dp)
                    )
                } else {
                    productosVisibles.forEach { producto ->
                        ProductoCompartido(
                            producto = producto,
                            alAbrir = { alAbrirProducto(producto.id) }
                        )
                    }
                }
            }
        }
    }

    when (operacion) {
        "Nuevo" -> {
            DialogoProductoCompartido(
                carpeta = nombreCarpeta,
                inventarioViewModel = inventarioViewModel,
                alCerrar = { operacion = null }
            )
        }

        "Entrada", "Baja" -> {
            DialogoMovimientoCompartido(
                tipo = operacion!!,
                productos = productos,
                inventarioViewModel = inventarioViewModel,
                alCerrar = { operacion = null }
            )
        }
    }
}

@Composable
private fun AccionCarpeta(
    titulo: String,
    descripcion: String,
    simbolo: String,
    color: Color,
    fondo: Color,
    modifier: Modifier,
    alClick: () -> Unit
) {
    Surface(
        onClick = alClick,
        modifier = modifier.height(130.dp),
        color = fondo,
        shape = RoundedCornerShape(14.dp)
    ) {
        Column(
            modifier = Modifier.padding(5.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                simbolo,
                fontSize = 34.sp,
                color = color,
                fontWeight = FontWeight.Bold
            )
            Text(
                titulo,
                color = color,
                fontSize = 12.sp,
                lineHeight = 15.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(4.dp))
            Text(
                descripcion,
                color = GrisCarpeta,
                fontSize = 10.sp,
                lineHeight = 13.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun ProductoCompartido(
    producto: Producto,
    alAbrir: () -> Unit
) {
    Surface(
        onClick = alAbrir,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(15.dp),
        color = Color.White,
        border = BorderStroke(1.dp, BordeCarpeta),
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(65.dp)
                    .background(
                        Color(0xFFF0F5FF),
                        RoundedCornerShape(10.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Inventory2,
                    contentDescription = null,
                    tint = AzulCarpeta,
                    modifier = Modifier.size(34.dp)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    producto.nombre,
                    color = TextoCarpeta,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    producto.presentacion,
                    color = Color(0xFF716FE3),
                    fontSize = 13.sp
                )
                Spacer(Modifier.height(5.dp))
                Text(
                    "Última entrada: ${producto.ultimaEntrada}",
                    color = GrisCarpeta,
                    fontSize = 11.sp,
                    lineHeight = 14.sp
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Surface(
                    color = if (producto.existencias <= 3)
                        Color(0xFFFFE6E3)
                    else
                        Color(0xFFD8F8E5),
                    shape = CircleShape
                ) {
                    Text(
                        "${producto.existencias} uds",
                        modifier = Modifier.padding(
                            horizontal = 9.dp,
                            vertical = 6.dp
                        ),
                        color = if (producto.existencias <= 3)
                            RojoCarpeta else VerdeCarpeta,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                if (producto.existencias <= 3) {
                    Text(
                        if (producto.existencias == 0)
                            "Agotado" else "Stock bajo",
                        color = RojoCarpeta,
                        fontSize = 10.sp
                    )
                }
            }

            Icon(
                Icons.Default.ChevronRight,
                contentDescription = "Ver producto",
                tint = Color(0xFF6059D5)
            )
        }
    }
}

@Composable
private fun DialogoProductoCompartido(
    carpeta: String,
    inventarioViewModel: InventarioViewModel,
    alCerrar: () -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var presentacion by remember { mutableStateOf("") }
    var precioCompra by remember { mutableStateOf("") }
    var precioVenta by remember { mutableStateOf("") }
    var existencias by remember { mutableStateOf("") }

    // Desde "Todos los productos" se debe elegir una carpeta real.
    var carpetaElegida by remember {
        mutableStateOf(
            if (carpeta == "Todos los productos") {
                inventarioViewModel.carpetas.firstOrNull().orEmpty()
            } else {
                carpeta
            }
        )
    }
    var mostrarCarpetas by remember { mutableStateOf(false) }

    val compra = precioCompra.toDoubleOrNull()
    val venta = precioVenta.toDoubleOrNull()
    val cantidad = existencias.toIntOrNull()

    val valido = nombre.isNotBlank() &&
            presentacion.isNotBlank() &&
            compra != null && compra.isFinite() && compra >= 0 &&
            venta != null && venta.isFinite() && venta >= 0 &&
            cantidad != null && cantidad >= 0 &&
            carpetaElegida.isNotBlank()

    AlertDialog(
        onDismissRequest = alCerrar,
        title = { Text("Nuevo producto") },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (carpeta == "Todos los productos") {
                    Text("Carpeta")
                    Box {
                        OutlinedButton(
                            onClick = { mostrarCarpetas = true }
                        ) {
                            Text(carpetaElegida)
                        }
                        DropdownMenu(
                            expanded = mostrarCarpetas,
                            onDismissRequest = {
                                mostrarCarpetas = false
                            }
                        ) {
                            inventarioViewModel.carpetas.forEach { nombreCarpeta ->
                                DropdownMenuItem(
                                    text = { Text(nombreCarpeta) },
                                    onClick = {
                                        carpetaElegida = nombreCarpeta
                                        mostrarCarpetas = false
                                    }
                                )
                            }
                        }
                    }
                }

                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = presentacion,
                    onValueChange = { presentacion = it },
                    label = { Text("Presentación") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = precioCompra,
                    onValueChange = { precioCompra = it },
                    label = { Text("Precio de compra") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = precioVenta,
                    onValueChange = { precioVenta = it },
                    label = { Text("Precio de venta") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = existencias,
                    onValueChange = { existencias = it },
                    label = { Text("Existencias iniciales") },
                    singleLine = true
                )
            }
        },
        confirmButton = {
            TextButton(
                enabled = valido,
                onClick = {
                    val guardado = inventarioViewModel.agregarProducto(
                        nombre = nombre,
                        presentacion = presentacion,
                        carpeta = carpetaElegida,
                        precioCompra = compra!!,
                        precioVenta = venta!!,
                        existencias = cantidad!!
                    )

                    if (guardado) alCerrar()
                }
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = alCerrar) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
private fun DialogoMovimientoCompartido(
    tipo: String,
    productos: List<Producto>,
    inventarioViewModel: InventarioViewModel,
    alCerrar: () -> Unit
) {
    var productoId by remember {
        mutableStateOf(productos.firstOrNull()?.id)
    }
    var cantidadTexto by remember { mutableStateOf("") }
    var desplegado by remember { mutableStateOf(false) }

    val seleccionado = productos.find { it.id == productoId }
    val cantidad = cantidadTexto.toIntOrNull()

    val valido = seleccionado != null &&
            cantidad != null &&
            cantidad > 0 &&
            (tipo == "Entrada" || cantidad <= seleccionado.existencias)

    AlertDialog(
        onDismissRequest = alCerrar,
        title = {
            Text(
                if (tipo == "Entrada")
                    "Entrada de mercancía"
                else
                    "Registrar baja"
            )
        },
        text = {
            Column {
                Text("Selecciona un producto")
                Spacer(Modifier.height(8.dp))

                Box {
                    OutlinedButton(
                        onClick = { desplegado = true },
                        enabled = productos.isNotEmpty()
                    ) {
                        Text(seleccionado?.nombre ?: "Sin productos")
                    }

                    DropdownMenu(
                        expanded = desplegado,
                        onDismissRequest = { desplegado = false }
                    ) {
                        productos.forEach { producto ->
                            DropdownMenuItem(
                                text = { Text(producto.nombre) },
                                onClick = {
                                    productoId = producto.id
                                    desplegado = false
                                }
                            )
                        }
                    }
                }

                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value = cantidadTexto,
                    onValueChange = { cantidadTexto = it },
                    label = { Text("Cantidad de unidades") },
                    singleLine = true
                )

                if (tipo == "Baja" && seleccionado != null) {
                    Text(
                        "Disponibles: ${seleccionado.existencias}",
                        color = GrisCarpeta,
                        fontSize = 12.sp
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                enabled = valido,
                onClick = {
                    val id = productoId ?: return@TextButton
                    val unidades = cantidad ?: return@TextButton

                    val exito = if (tipo == "Entrada") {
                        inventarioViewModel.registrarEntrada(id, unidades)
                    } else {
                        inventarioViewModel.registrarBaja(id, unidades)
                    }

                    if (exito) alCerrar()
                }
            ) {
                Text("Confirmar")
            }
        },
        dismissButton = {
            TextButton(onClick = alCerrar) {
                Text("Cancelar")
            }
        }
    )
}
