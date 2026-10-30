// 1. Validar número de teléfono
fun validarTelefono(telefonoIngresado: String?): String {
    val telefono = telefonoIngresado?.trim()

    return telefono?.takeIf {
        val digitos = it.filter { caracter -> caracter.isDigit() }
        digitos.length in 9..12
    } ?: "(000) 000-0000"
}

// 2. Validar correo electrónico
fun validarCorreo(correoIngresado: String?): String {
    val correo = correoIngresado?.trim()

    return correo?.takeIf {
        it.matches(Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"))
    } ?: "correo@invalido.com"
}

// 3. Asignar el repartidor disponible más cercano
fun asignarRepartidor(): Repartidor? {

    val repartidores = listOf(
        Repartidor(
            nombre = "Juan Pérez",
            contacto = "912345678",
            disponible = true,
            distanciaKm = 2.5
        ),
        Repartidor(
            nombre = "María González",
            contacto = "987654321",
            disponible = true,
            distanciaKm = 1.2
        ),
        Repartidor(
            nombre = "Pedro Soto",
            contacto = "923456789",
            disponible = false,
            distanciaKm = 0.8
        )
    )

    return repartidores
        .filter { it.disponible }
        .sortedBy { it.distanciaKm }
        .find { it.disponible }
}

// 4. Notificar al cliente si está registrado
fun notificarCliente(cliente: Cliente?) {

    cliente?.let {
        println("Notificación enviada a ${it.nombre}.")
        println("Contacto: ${it.contacto}")
    } ?: println("No se pudo enviar la notificación: cliente no registrado.")
}

// 5. Validar el ingreso de un precio utilizando try-catch
fun validarPrecio(precioIngresado: String?): Double? {

    return try {
        val precio = precioIngresado
            ?.trim()
            ?.replace(",", ".")
            ?.toDouble()
            ?: throw IllegalArgumentException("El precio no puede estar vacío.")

        require(precio > 0) {
            "El precio debe ser mayor que cero."
        }

        precio

    } catch (e: NumberFormatException) {
        println("Error: ingrese un precio numérico válido.")
        null

    } catch (e: IllegalArgumentException) {
        println("Error: ${e.message}")
        null
    }
}

// 6. Manejar errores al reabastecer el inventario
fun reabastecerInventario(
    inventario: Inventario,
    ingrediente: String,
    cantidad: Int
) {
    try {
        inventario.reabastecerIngrediente(ingrediente, cantidad)
    } catch (e: IllegalArgumentException) {
        println("Error al actualizar inventario: ${e.message}")
    }
}
