import java.time.LocalDate

enum class EstadoPedido {
    EN_PREPARACION,
    LISTO,
    EN_CAMINO
}

class Pedido(
    val id: Int,
    val cliente: Cliente,
    @field:Promocionable
    val productos: List<Producto>,
    val cantidades: List<Int>,
    var estado: EstadoPedido = EstadoPedido.EN_PREPARACION,
    val notas: String? = null,
    val repartidor: Repartidor? = null,
    val fechaPedido: LocalDate = LocalDate.now()
) {

    // Periodo de promoción: noviembre de 2026
    companion object {
        val FECHA_INICIO_PROMOCION = LocalDate.of(2026, 11, 1)
        val FECHA_FIN_PROMOCION = LocalDate.of(2026, 11, 30)
    }

    init {
        require(productos.isNotEmpty()) {
            "El pedido debe contener al menos un producto."
        }

        require(productos.size == cantidades.size) {
            "Cada producto debe tener una cantidad asociada."
        }

        require(cantidades.all { it in 1..100 }) {
            "Las cantidades deben estar entre 1 y 100."
        }
    }

    // Calcula el subtotal
    fun calcularSubtotal(): Double {
        return productos.indices.sumOf { indice ->
            productos[indice].precio * cantidades[indice]
        }
    }

    // Verifica si el pedido está dentro del periodo promocional
    fun estaEnPeriodoPromocional(): Boolean {
        return fechaPedido in FECHA_INICIO_PROMOCION..FECHA_FIN_PROMOCION
    }

    // Calcula el valor de los productos promocionables
    fun calcularSubtotalPromocionable(): Double {
        return productos.indices.sumOf { indice ->
            val producto = productos[indice]

            val esPromocionable = producto.javaClass
                .isAnnotationPresent(Promocionable::class.java)

            if (esPromocionable) {
                producto.precio * cantidades[indice]
            } else {
                0.0
            }
        }
    }

    // Descuento original: 10% por más de tres productos
    fun calcularDescuento(): Double {
        val totalProductos = cantidades.sum()

        val descuentoPorCantidad = if (totalProductos > 3) {
            calcularSubtotal() * 0.10
        } else {
            0.0
        }

        // Descuento adicional del 5% en periodo promocional
        val descuentoPromocional = if (estaEnPeriodoPromocional()) {
            calcularSubtotalPromocionable() * 0.05
        } else {
            0.0
        }

        return descuentoPorCantidad + descuentoPromocional
    }

    // Calcula el IVA del 19%
    fun calcularImpuesto(): Double {
        return (calcularSubtotal() - calcularDescuento()) * 0.19
    }

    // Calcula el total
    fun calcularTotal(): Double {
        return calcularSubtotal() - calcularDescuento() + calcularImpuesto()
    }

    // Cambia el estado del pedido
    fun cambiarEstado(nuevoEstado: EstadoPedido) {
        estado = nuevoEstado
    }

    // Sobrecarga del operador + para combinar dos pedidos
    operator fun plus(otro: Pedido): Pedido {
        require(cliente == otro.cliente) {
            "Solo se pueden combinar pedidos del mismo cliente."
        }

        return Pedido(
            id = this.id,
            cliente = this.cliente,
            productos = this.productos + otro.productos,
            cantidades = this.cantidades + otro.cantidades,
            notas = listOfNotNull(this.notas, otro.notas)
                .joinToString(" | ")
                .ifBlank { null },
            repartidor = this.repartidor ?: otro.repartidor,
            fechaPedido = maxOf(this.fechaPedido, otro.fechaPedido)
        )
    }

    // Desestructuración del pedido
    operator fun component1(): Cliente = cliente
    operator fun component2(): List<Producto> = productos
    operator fun component3(): Double = calcularTotal()

    // Genera el resumen del pedido
    fun generarResumen(): String {

        val detalleProductos = productos.indices.joinToString("\n") { indice ->
            val producto = productos[indice]
            val cantidad = cantidades[indice]

            "- ${producto.nombre} x $cantidad: ${
                formatearPrecio(producto.precio * cantidad)
            }"
        }

        return """
            ========== RESUMEN DEL PEDIDO ==========
            Pedido N°: $id
            Cliente: ${cliente.nombre}
            Teléfono: ${cliente.contacto}
            Correo: ${cliente.correo}
            Dirección: ${cliente.direccion}
            Fecha: $fechaPedido

            PRODUCTOS:
            $detalleProductos

            Subtotal: ${formatearPrecio(calcularSubtotal())}
            Descuento: ${formatearPrecio(calcularDescuento())}
            IVA (19%): ${formatearPrecio(calcularImpuesto())}
            TOTAL: ${formatearPrecio(calcularTotal())}

            ¿Está en periodo promocional?: ${
            if (estaEnPeriodoPromocional()) "Sí" else "No"
        }

            Estado: ${estado.name.replace("_", " ")}
            Notas: ${notas ?: "Sin notas"}
            Repartidor: ${repartidor?.nombre ?: "No asignado"}
            ========================================
        """.trimIndent()
    }
}
