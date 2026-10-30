import java.util.Locale

fun main() {

    val inventario = Inventario()

    // Catálogo de productos1

    val catalogo = listOf(
        Producto(
            "Pepperoni",
            8000.0,
            listOf("Queso", "Salsa de tomate", "Pepperoni")
        ),
        Producto(
            "Napolitana",
            7500.0,
            listOf("Queso", "Salsa de tomate", "Tomate", "Orégano")
        ),
        Producto(
            "Vegetariana",
            8500.0,
            listOf(
                "Queso", "Salsa de tomate", "Champiñones",
                "Pimentón", "Aceitunas"
            )
        )
    )

    // Arreglo para almacenar los pedidos del día
    var pedidosDelDia: Array<Pedido> = emptyArray()

    var siguienteId = 1
    var continuar = true

    println("============================================")
    println("       PIZZERÍA - SISTEMA DE GESTIÓN")
    println("============================================")

    while (continuar) {

        println("\n--------------- MENÚ PRINCIPAL ---------------")
        println("1. Registrar nuevo pedido")
        println("2. Cambiar estado de un pedido")
        println("3. Mostrar reporte de pedidos")
        println("4. Mostrar inventario")
        println("5. Reabastecer ingredientes")
        println("6. Validar precio")
        println("0. Salir")
        println("----------------------------------------------")

        println("\nSeleccione una opción:")

        when (readlnOrNull()?.trim()) {

            // OPCIÓN 1: REGISTRAR PEDIDO
            "1" -> {

                println("\n============================================")
                println("             REGISTRO DEL CLIENTE")
                println("============================================")

                println("\nIngrese el nombre del cliente:")
                val nombre = readlnOrNull()
                    ?.trim()
                    ?.takeIf { it.isNotBlank() }
                    ?: "Cliente no registrado"

                println("\nIngrese la dirección de entrega:")
                val direccion = readlnOrNull()
                    ?.trim()
                    ?.takeIf { it.isNotBlank() }
                    ?: "Dirección no registrada"

                println("\nIngrese el teléfono:")
                val telefono = validarTelefono(readlnOrNull())

                println("\nIngrese el correo electrónico:")
                val correo = validarCorreo(readlnOrNull())

                val cliente = Cliente(
                    nombre = nombre,
                    contacto = telefono,
                    direccion = direccion,
                    correo = correo
                )

                println("\nCliente registrado correctamente.")

                // CATÁLOGO DE PIZZAS
                println("\n============================================")
                println("                MENÚ DE PIZZAS")
                println("============================================")

                for (i in catalogo.indices) {
                    println("\n${i + 1}. ${catalogo[i].nombre}")
                    println("   Precio: ${formatearPrecio(catalogo[i].precio)}")
                    println(
                        "   Ingredientes: ${
                            catalogo[i].ingredientes.joinToString(", ")
                        }"
                    )
                }

                println("\nSeleccione una pizza (1-${catalogo.size}):")

                val opcionPizza = readlnOrNull()?.toIntOrNull()

                val producto = catalogo.getOrNull(
                    (opcionPizza ?: 0) - 1
                )

                if (producto == null) {
                    println("\nSelección de pizza no válida.")
                    continue
                }

                // CANTIDAD DE PIZZAS
                println("\nIngrese la cantidad de pizzas:")

                val cantidad = try {
                    val valor = readlnOrNull()?.toIntOrNull()
                        ?: throw IllegalArgumentException(
                            "Debe ingresar un número entero."
                        )

                    require(valor > 0) {
                        "La cantidad debe ser mayor que cero."
                    }

                    valor

                } catch (e: IllegalArgumentException) {
                    println("\nError: ${e.message}")
                    continue
                }

                // NOTAS DEL PEDIDO
                println("\n¿Desea agregar alguna nota al pedido?")
                println("Escriba su comentario o presione Enter para continuar:")

                val notas = readlnOrNull()
                    ?.trim()
                    ?.takeIf { it.isNotBlank() }

                // ASIGNACIÓN DEL REPARTIDOR
                val repartidor = asignarRepartidor()

                // VERIFICACIÓN Y ACTUALIZACIÓN DEL INVENTARIO
                println("\nVerificando disponibilidad de ingredientes...")

                if (inventario.actualizarInventario(producto, cantidad)) {

                    // CREACIÓN DEL PEDIDO
                    val nuevoPedido = Pedido(
                        id = siguienteId,
                        cliente = cliente,
                        productos = listOf(producto),
                        cantidades = listOf(cantidad),
                        estado = EstadoPedido.EN_PREPARACION,
                        notas = notas,
                        repartidor = repartidor
                    )

                    pedidosDelDia = pedidosDelDia + nuevoPedido
                    siguienteId++

                    println("\n============================================")
                    println("       ¡PEDIDO REGISTRADO CORRECTAMENTE!")
                    println("============================================")

                    println(nuevoPedido.generarResumen())

                    // NOTIFICACIÓN AL CLIENTE
                    println("\nNOTIFICACIÓN AL CLIENTE")
                    notificarCliente(cliente)

                } else {
                    println("\nNo fue posible confirmar el pedido.")
                    println("Revise la disponibilidad de ingredientes.")
                }
            }

            // OPCIÓN 2: CAMBIAR ESTADO
            "2" -> {

                if (pedidosDelDia.isEmpty()) {
                    println("\nTodavía no hay pedidos registrados.")
                    continue
                }

                println("\n============================================")
                println("             PEDIDOS REGISTRADOS")
                println("============================================")

                for (pedido in pedidosDelDia) {
                    println(
                        "Pedido ${pedido.id} | " +
                                "${pedido.cliente.nombre} | " +
                                "${pedido.estado.name.replace("_", " ")}"
                    )
                }

                println("\nIngrese el ID del pedido que desea modificar:")

                val id = readlnOrNull()?.toIntOrNull()

                val pedido = pedidosDelDia.find { it.id == id }

                if (pedido == null) {
                    println("\nNo se encontró el pedido.")
                    continue
                }

                println("\nSeleccione el nuevo estado:")
                println("1. En preparación")
                println("2. Listo")
                println("3. En camino")

                val nuevoEstado = when (readlnOrNull()?.trim()) {
                    "1" -> EstadoPedido.EN_PREPARACION
                    "2" -> EstadoPedido.LISTO
                    "3" -> EstadoPedido.EN_CAMINO
                    else -> null
                }

                if (nuevoEstado != null) {

                    pedido.cambiarEstado(nuevoEstado)

                    println("\nEstado actualizado correctamente.")

                    if (nuevoEstado == EstadoPedido.EN_CAMINO) {
                        pedido.repartidor?.let {
                            println("\n${it.entregarPedido()}")
                        } ?: println("No hay repartidor asignado.")
                    }

                } else {
                    println("\nEstado no válido.")
                }
            }

            // OPCIÓN 3: REPORTE DE PEDIDOS
            "3" -> {

                if (pedidosDelDia.isEmpty()) {
                    println("\nNo existen pedidos registrados.")
                    continue
                }

                println("\n============================================")
                println("             REPORTE DE PEDIDOS")
                println("============================================")

                // Recorrido del arreglo mediante for
                for (pedido in pedidosDelDia) {

                    val totalPizzas = pedido.cantidades.sum()

                    println("\n--------------------------------------------")
                    println("Pedido N°: ${pedido.id}")
                    println("Cliente: ${pedido.cliente.nombre}")
                    println("Pizzas pedidas: $totalPizzas")
                    println(
                        "Total: ${formatearPrecio(pedido.calcularTotal())}"
                    )
                    println(
                        "Estado: ${pedido.estado.name.replace("_", " ")}"
                    )
                }

                // FILTRAR PEDIDOS COMPLETADOS
                val pedidosCompletados = pedidosDelDia.filter {
                    it.estado == EstadoPedido.LISTO ||
                            it.estado == EstadoPedido.EN_CAMINO
                }

                // FILTRAR PEDIDOS PENDIENTES
                val pedidosPendientes = pedidosDelDia.filter {
                    it.estado == EstadoPedido.EN_PREPARACION
                }

                println("\n============================================")
                println("             PEDIDOS COMPLETADOS")
                println("============================================")

                if (pedidosCompletados.isEmpty()) {
                    println("No hay pedidos completados.")
                } else {
                    pedidosCompletados.forEach {
                        println(
                            "Pedido ${it.id} - ${it.cliente.nombre}"
                        )
                    }
                }

                println("\n============================================")
                println("              PEDIDOS PENDIENTES")
                println("============================================")

                if (pedidosPendientes.isEmpty()) {
                    println("No hay pedidos pendientes.")
                } else {
                    pedidosPendientes.forEach {
                        println(
                            "Pedido ${it.id} - ${it.cliente.nombre}"
                        )
                    }
                }

                println("\n============================================")
                println("Total de pedidos: ${pedidosDelDia.size}")
                println("Completados: ${pedidosCompletados.size}")
                println("Pendientes: ${pedidosPendientes.size}")
                println("============================================")
            }

            // OPCIÓN 4: MOSTRAR INVENTARIO
            "4" -> {
                inventario.mostrarInventario()
            }

            // OPCIÓN 5: REABASTECER INGREDIENTES
            "5" -> {

                inventario.mostrarInventario()

                println("\nIngrese el nombre del ingrediente que desea reabastecer:")
                val ingrediente = readlnOrNull()?.trim().orEmpty()

                println("\nIngrese la cantidad que desea agregar:")
                val cantidad = readlnOrNull()?.toIntOrNull()

                if (ingrediente.isBlank() || cantidad == null) {
                    println("\nDebe ingresar un ingrediente y una cantidad válida.")
                } else {
                    reabastecerInventario(
                        inventario,
                        ingrediente,
                        cantidad
                    )
                }
            }

            // OPCIÓN 6: VALIDAR PRECIO
            "6" -> {

                println("\nIngrese el precio que desea validar:")

                val precio = validarPrecio(readlnOrNull())

                if (precio != null) {
                    println(
                        "\nPrecio válido: ${formatearPrecio(precio)}"
                    )
                }
            }

            // OPCIÓN 0: SALIR
            "0" -> {
                continuar = false

                println("\n============================================")
                println("Gracias por utilizar nuestro sistema.")
                println("¡Esperamos atenderlo nuevamente!")
                println("============================================")
            }

            else -> {
                println("\nOpción no válida. Intente nuevamente.")
            }
        }
    }
}

// FUNCIÓN PARA FORMATEAR PRECIOS EN PESOS CHILENOS
fun formatearPrecio(valor: Double): String {
    return "$" + String.format(
        Locale.forLanguageTag("es-CL"),
        "%,.0f",
        valor
    )
}
