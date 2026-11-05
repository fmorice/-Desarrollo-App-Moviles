
class Inventario {

    // Map: ingrediente y cantidad disponible
    private val ingredientes = mutableMapOf(
        "Queso" to 50,
        "Salsa de tomate" to 40,
        "Pepperoni" to 30,
        "Tomate" to 20,
        "Orégano" to 25,
        "Champiñones" to 20,
        "Pimentón" to 15,
        "Aceitunas" to 15
    )

    // Cantidad de ingredientes necesarios por pizza
    private val receta = mapOf(
        "Queso" to 2,
        "Salsa de tomate" to 1,
        "Pepperoni" to 2,
        "Tomate" to 1,
        "Orégano" to 1,
        "Champiñones" to 2,
        "Pimentón" to 1,
        "Aceitunas" to 1
    )

    // Verifica la disponibilidad de ingredientes
    fun verificarDisponibilidad(producto: Producto, cantidad: Int): Boolean {

        if (cantidad <= 0) {
            println("La cantidad debe ser mayor que cero.")
            return false
        }

        val faltantes = mutableListOf<String>()

        for (ingrediente in producto.ingredientes) {

            val disponible = ingredientes[ingrediente] ?: 0
            val necesario = (receta[ingrediente] ?: 1) * cantidad

            when {
                disponible >= necesario -> {
                    println("$ingrediente: Stock suficiente.")
                }

                else -> {
                    faltantes.add(
                        "$ingrediente (necesarios: $necesario, disponibles: $disponible)"
                    )
                }
            }
        }

        if (faltantes.isNotEmpty()) {
            println("\nNo hay suficientes ingredientes:")
            faltantes.forEach { println("- $it") }
            println("Pasos para reabastecer:")
            println("1. Revisar el inventario.")
            println("2. Solicitar los ingredientes faltantes.")
            println("3. Actualizar las cantidades disponibles.")
            return false
        }

        return true
    }

    // Descuenta automáticamente los ingredientes utilizados
    fun actualizarInventario(producto: Producto, cantidad: Int): Boolean {

        if (!verificarDisponibilidad(producto, cantidad)) {
            return false
        }

        producto.ingredientes.forEach { ingrediente ->
            val necesario = (receta[ingrediente] ?: 1) * cantidad
            ingredientes[ingrediente] =
                (ingredientes[ingrediente] ?: 0) - necesario
        }

        println("Inventario actualizado correctamente.")
        return true
    }

    // Muestra las cantidades disponibles
    fun mostrarInventario() {
        println("\n========== INVENTARIO ==========")

        for ((ingrediente, cantidad) in ingredientes) {
            println("$ingrediente: $cantidad unidades")
        }

        println("================================")
    }

    // Permite reabastecer ingredientes
    fun reabastecerIngrediente(nombre: String, cantidad: Int) {
        require(cantidad > 0) {
            "La cantidad de reabastecimiento debe ser positiva."
        }

        ingredientes[nombre] = (ingredientes[nombre] ?: 0) + cantidad

        println("$nombre reabastecido correctamente.")
    }
}
