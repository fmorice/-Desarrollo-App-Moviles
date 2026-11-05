import java.util.Locale
import java.time.LocalDate

fun main() {

    val inventario = Inventario()

    // Catálogo de productos
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

    // Almacenar los pedidos del día
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
        println("7. Demostrar funcionalidades Semana 3")
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

                if (telefono.isBlank()) {
                    println("No se pudo registrar el teléfono.")
                    continue
                }

                println("\nIngrese el correo electrónico:")
                val correo = validarCorreo(readlnOrNull())

                if (correo.isBlank()) {
                    println("No se pudo registrar el correo.")
                    continue
                }

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

                // VALIDAR CANTIDAD
                println("\nIngrese la cantidad de pizzas:")

                val cantidad = validarCantidad(readlnOrNull())

                if (cantidad == null) {
                    println("No se pudo registrar la cantidad.")
                    continue
                }

                // VALIDAR FECHA
                println("\nIngrese la fecha del pedido (AAAA-MM-DD):")
                println("Ejemplo: 2026-11-05")

                val fechaPedido = validarFecha(readlnOrNull())

                if (fechaPedido == null) {
                    println("No se pudo registrar la fecha.")
                    continue
                }

                // NOTAS DEL PEDIDO
                println("\n¿Desea agregar alguna nota al pedido?")
                println("Escriba su comentario o presione Enter para continuar:")

                val notas = readlnOrNull()
                    ?.trim()
                    ?.takeIf { it.isNotBlank() }

                // ASIGNAR REPARTIDOR
                val repartidor = asignarRepartidor()

                // VERIFICAR Y ACTUALIZAR INVENTARIO
                println("\nVerificando disponibilidad de ingredientes...")

                if (inventario.actualizarInventario(producto, cantidad)) {

                    val nuevoPedido = Pedido(
                        id = siguienteId,
                        cliente = cliente,
                        productos = listOf(producto),
                        cantidades = listOf(cantidad),
                        estado = EstadoPedido.EN_PREPARACION,
                        notas = notas,
                        repartidor = repartidor,
                        fechaPedido = fechaPedido
                    )

                    pedidosDelDia = pedidosDelDia + nuevoPedido
                    siguienteId++

                    println("\n============================================")
                    println("       ¡PEDIDO REGISTRADO CORRECTAMENTE!")
                    println("============================================")

                    println(nuevoPedido.generarResumen())

                    // NOTIFICAR AL CLIENTE
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

                println("\nIngrese el número del pedido:")
                val idPedido = readlnOrNull()?.toIntOrNull()

                val pedidoSeleccionado = pedidosDelDia.find {
                    it.id == idPedido
                }

                if (pedidoSeleccionado == null) {
                    println("No se encontró el pedido.")
                    continue
                }

                println("\nSeleccione el nuevo estado:")
                println("1. En preparación")
                println("2. Listo")
                println("3. En camino")

                when (readlnOrNull()?.trim()) {
                    "1" -> pedidoSeleccionado.cambiarEstado(
                        EstadoPedido.EN_PREPARACION
                    )

                    "2" -> pedidoSeleccionado.cambiarEstado(
                        EstadoPedido.LISTO
                    )

                    "3" -> pedidoSeleccionado.cambiarEstado(
                        EstadoPedido.EN_CAMINO
                    )

                    else -> {
                        println("Estado no válido.")
                        continue
                    }
                }

                println("\nEstado actualizado correctamente.")
                println("Nuevo estado: ${pedidoSeleccionado.estado}")
            }

            // OPCIÓN 3: MOSTRAR REPORTE
            "3" -> {

                println("\n============================================")
                println("             REPORTE DE PEDIDOS")
                println("============================================")

                if (pedidosDelDia.isEmpty()) {
                    println("No hay pedidos registrados.")
                } else {

                    for (pedido in pedidosDelDia) {
                        println(pedido.generarResumen())
                    }

                    println("\nTotal de pedidos: ${pedidosDelDia.size}")

                    val totalVentas = pedidosDelDia.sumOf {
                        it.calcularTotal()
                    }

                    println("Total de ventas: ${formatearPrecio(totalVentas)}")

                    val pedidosPendientes = pedidosDelDia.count {
                        it.estado == EstadoPedido.EN_PREPARACION
                    }

                    val pedidosListos = pedidosDelDia.count {
                        it.estado == EstadoPedido.LISTO
                    }

                    val pedidosEnCamino = pedidosDelDia.count {
                        it.estado == EstadoPedido.EN_CAMINO
                    }

                    println("\nPedidos en preparación: $pedidosPendientes")
                    println("Pedidos listos: $pedidosListos")
                    println("Pedidos en camino: $pedidosEnCamino")
                }
            }

            // OPCIÓN 4: MOSTRAR INVENTARIO
            "4" -> {
                inventario.mostrarInventario()
            }

            // OPCIÓN 5: REABASTECER INGREDIENTES
            "5" -> {

                println("\nIngrese el nombre del ingrediente:")
                val ingrediente = readlnOrNull()?.trim().orEmpty()

                if (ingrediente.isBlank()) {
                    println("Debe ingresar un ingrediente.")
                    continue
                }

                println("Ingrese la cantidad que desea agregar:")

                val cantidad = validarCantidad(readlnOrNull())

                if (cantidad == null) {
                    println("No se pudo registrar la cantidad.")
                    continue
                }

                reabastecerInventario(
                    inventario,
                    ingrediente,
                    cantidad
                )
            }

            // OPCIÓN 6: VALIDAR PRECIO
            "6" -> {

                println("\nIngrese el precio que desea validar:")

                val precio = validarPrecio(readlnOrNull())

                if (precio != null) {
                    println("Precio válido: ${formatearPrecio(precio)}")
                }
            }

            // OPCIÓN 7: DEMOSTRAR FUNCIONALIDADES SEMANA 3
            "7" -> {
                mostrarDemostracionSemana3(catalogo)
            }

            // OPCIÓN 0: SALIR
            "0" -> {
                continuar = false
                println("\nGracias por utilizar el sistema de la pizzería.")
            }

            else -> {
                println("\nOpción no válida. Intente nuevamente.")
            }
        }
    }
}

// DEMOSTRACIÓN DE LOS REQUISITOS DE LA SEMANA 3
fun mostrarDemostracionSemana3(catalogo: List<Producto>) {

    println("\n============================================")
    println("      DEMOSTRACIÓN DE FUNCIONALIDADES")
    println("============================================")

    // CREAR CLIENTES Y PRODUCTOS
    val cliente = Cliente(
        nombre = "Florencia",
        contacto = "569123456789",
        direccion = "Santiago",
        correo = "florencia@gmail.com"
    )

    val clienteDuplicado = Cliente(
        nombre = "Florencia",
        contacto = "569987654321",
        direccion = "Otra dirección",
        correo = "florencia@gmail.com"
    )

    val productoUno = catalogo[0]
    val productoDuplicado = Producto(
        nombre = "Pepperoni",
        precio = 9000.0,
        ingredientes = listOf("Queso", "Pepperoni")
    )

    // 1. SOBRECARGA DEL OPERADOR +
    println("\n1. SOBRECARGA DEL OPERADOR +")

    val pedidoUno = Pedido(
        id = 100,
        cliente = cliente,
        productos = listOf(catalogo[0]),
        cantidades = listOf(2)
    )

    val pedidoDos = Pedido(
        id = 101,
        cliente = cliente,
        productos = listOf(catalogo[1]),
        cantidades = listOf(1)
    )

    val pedidoCombinado = pedidoUno + pedidoDos

    println("Se combinaron dos pedidos.")
    println("Cantidad de productos: ${pedidoCombinado.productos.size}")
    println("Total combinado: ${formatearPrecio(pedidoCombinado.calcularTotal())}")

    // 2. COMPARACIÓN CON ==
    println("\n2. COMPARACIÓN DE OBJETOS CON ==")

    println("¿Los clientes son iguales?: ${cliente == clienteDuplicado}")
    println("¿Los productos son iguales?: ${productoUno == productoDuplicado}")

    // 3. EQUALS, HASHCODE Y HASHSET
    println("\n3. EQUALS, HASHCODE Y HASHSET")

    val conjuntoClientes = hashSetOf(cliente, clienteDuplicado)
    val conjuntoProductos = hashSetOf(productoUno, productoDuplicado)

    println("Clientes originales: 2")
    println("Clientes sin duplicados: ${conjuntoClientes.size}")

    println("Productos originales: 2")
    println("Productos sin duplicados: ${conjuntoProductos.size}")

    // 4. DESTRUCTURACIÓN DE CLIENTE
    println("\n4. DESTRUCTURACIÓN DE CLIENTE")

    val (nombreCliente, correoCliente, telefonoCliente) = cliente

    println("Nombre: $nombreCliente")
    println("Correo: $correoCliente")
    println("Teléfono: $telefonoCliente")

    // 5. DESTRUCTURACIÓN DE PEDIDO
    println("\n5. DESTRUCTURACIÓN DE PEDIDO")

    val (clientePedido, productosPedido, totalPedido) = pedidoCombinado

    println("Cliente: ${clientePedido.nombre}")
    println("Productos: ${productosPedido.size}")
    println("Total: ${formatearPrecio(totalPedido)}")

    // 6. ANOTACIONES PERSONALIZADAS
    println("\n6. ANOTACIONES PERSONALIZADAS")

    val productoAnotado = Producto::class.java
        .isAnnotationPresent(Promocionable::class.java)

    val pedidoAnotado = Pedido::class.java
        .getDeclaredField("productos")
        .isAnnotationPresent(Promocionable::class.java)

    println("¿La clase Producto tiene @Promocionable?: $productoAnotado")
    println("¿La propiedad productos de Pedido tiene @Promocionable?: $pedidoAnotado")

    // 7. REFLECTION
    println("\n7. REFLECTION")

    println("\nAnálisis de Producto:")
    mostrarReflection(productoUno)

    println("\nAnálisis de Pedido:")
    mostrarReflection(pedidoCombinado)

    // 8. VALIDACIÓN DE PERIODO PROMOCIONAL
    println("\n8. VALIDACIÓN DE PROMOCIONES")

    println("Fecha actual: ${LocalDate.now()}")
    println(
        "¿El pedido está en periodo promocional?: " +
                if (pedidoCombinado.estaEnPeriodoPromocional()) "Sí" else "No"
    )

    println("\n============================================")
    println("     FIN DE LA DEMOSTRACIÓN SEMANA 3")
    println("============================================")
}

// FORMATEAR PRECIOS EN PESOS CHILENOS
fun formatearPrecio(valor: Double): String {
    return "$" + String.format(
        Locale.forLanguageTag("es-CL"),
        "%,.0f",
        valor
    )
}
