enum class EstadoPedido {
    EN_PREPARACION,
    LISTO,
    EN_CAMINO
}

class Pedido(
    val id: Int,
    val cliente: Cliente,
    val productos: List<Producto>,
    val cantidades: List<Int>,
    var estado: EstadoPedido = EstadoPedido.EN_PREPARACION,
    val notas: String? = null,
    val repartidor: Repartidor? = null
) {

    init {
        require(productos.isNotEmpty()) {
            "El pedido debe contener al menos un producto."
        }

        require(productos.size == cantidades.size) {
            "Cada producto debe tener una cantidad asociada."
        }

        require(cantidades.all { it > 0 }) {
            "Las cantidades deben ser mayores que cero."
        }
    }

    fun calcularSubtotal(): Double {
        return productos.indices.sumOf { indice ->
            productos[indice].precio * cantidades[indice]
        }
    }

    fun calcularDescuento(): Double {
        val totalPizzas = cantidades.sum()

        return if (totalPizzas > 3) {
            calcularSubtotal() * 0.10
        } else {
            0.0
        }
    }

    fun calcularImpuesto(): Double {
        return (calcularSubtotal() - calcularDescuento()) * 0.19
    }

    fun calcularTotal(): Double {
        return calcularSubtotal() - calcularDescuento() + calcularImpuesto()
    }

    fun cambiarEstado(nuevoEstado: EstadoPedido) {
        estado = nuevoEstado
    }

    fun generarResumen(): String {
        val detalleProductos = productos.indices.joinToString("\n") { indice ->
            val producto = productos[indice]
            val cantidad = cantidades[indice]

            "- ${producto.nombre} x $cantidad: ${formatearPrecio(producto.precio * cantidad)}"
        }

        return """
            ========== RESUMEN DEL PEDIDO ==========
            Pedido N°: $id
            Cliente: ${cliente.nombre}
            Teléfono: ${cliente.contacto}
            Correo: ${cliente.correo}
            Dirección: ${cliente.direccion}
            
            PRODUCTOS:
            $detalleProductos
            
            Subtotal: ${formatearPrecio(calcularSubtotal())}
            Descuento: ${formatearPrecio(calcularDescuento())}
            IVA (19%): ${formatearPrecio(calcularImpuesto())}
            TOTAL: ${formatearPrecio(calcularTotal())}
            
            Estado: ${estado.name.replace("_", " ")}
            Notas: ${notas ?: "Sin notas"}
            Repartidor: ${repartidor?.nombre ?: "No asignado"}
            ========================================
        """.trimIndent()
    }
}
