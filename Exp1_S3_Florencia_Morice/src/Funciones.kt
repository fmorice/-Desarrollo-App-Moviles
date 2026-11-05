import java.time.LocalDate
import java.time.format.DateTimeParseException

// 1. VALIDAR Y FORMATEAR TELÉFONO
// Formato solicitado: +XX (XXX) XXX-XXXX
fun validarTelefono(telefonoIngresado: String?): String {
    var telefono = telefonoIngresado

    while (true) {
        val digitos = telefono.orEmpty().filter { it.isDigit() }

        if (digitos.length == 12) {
            return "+${digitos.substring(0, 2)} " +
                    "(${digitos.substring(2, 5)}) " +
                    "${digitos.substring(5, 8)}-" +
                    digitos.substring(8, 12)
        }

        println("Error: el teléfono debe contener exactamente 12 dígitos.")
        println("Ejemplo: 569123456789")
        println("Ingrese nuevamente el teléfono:")

        telefono = readlnOrNull()

        if (telefono == null) return ""
    }
}

// 2. VALIDAR CORREO ELECTRÓNICO
fun validarCorreo(correoIngresado: String?): String {
    val patronCorreo = Regex(
        "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    )

    var correo = correoIngresado?.trim()

    while (true) {
        if (correo != null && patronCorreo.matches(correo)) {
            return correo
        }

        println("Error: el correo electrónico no tiene un formato válido.")
        println("Ejemplo: cliente@gmail.com")
        println("Ingrese nuevamente el correo:")

        correo = readlnOrNull()?.trim()

        if (correo == null) return ""
    }
}

// 3. VALIDAR CANTIDAD ENTRE 1 Y 100
fun validarCantidad(cantidadIngresada: String?): Int? {
    var entrada = cantidadIngresada

    while (true) {
        val cantidad = entrada?.trim()?.toIntOrNull()

        if (cantidad != null && cantidad in 1..100) {
            return cantidad
        }

        println("Error: la cantidad debe ser un número entre 1 y 100.")
        println("Ingrese nuevamente la cantidad:")

        entrada = readlnOrNull()

        if (entrada == null) return null
    }
}

// 4. VALIDAR FECHA
// Formato requerido: AAAA-MM-DD
fun validarFecha(fechaIngresada: String?): LocalDate? {
    var entrada = fechaIngresada

    while (true) {
        try {
            val fecha = entrada?.trim()?.let {
                LocalDate.parse(it)
            }

            if (fecha != null) return fecha

        } catch (e: DateTimeParseException) {
            // La fecha ingresada no tiene el formato correcto.
        }

        println("Error: ingrese una fecha válida.")
        println("Formato requerido: AAAA-MM-DD")
        println("Ejemplo: 2026-11-05")
        println("Ingrese nuevamente la fecha:")

        entrada = readlnOrNull()

        if (entrada == null) return null
    }
}

// 5. ASIGNAR EL REPARTIDOR DISPONIBLE MÁS CERCANO
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
        .minByOrNull { it.distanciaKm }
}

// 6. NOTIFICAR AL CLIENTE
fun notificarCliente(cliente: Cliente?) {
    cliente?.let {
        println("Notificación enviada a ${it.nombre}.")
        println("Contacto: ${it.contacto}")
    } ?: println("No se pudo enviar la notificación: cliente no registrado.")
}

// 7. VALIDAR PRECIO UTILIZANDO TRY-CATCH
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

// 8. MANEJAR ERRORES AL REABASTECER INVENTARIO
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

// 9. REFLECTION: MOSTRAR PROPIEDADES Y MÉTODOS
fun mostrarReflection(objeto: Any) {
    val clase = objeto.javaClass

    println("\n========== ANÁLISIS CON REFLECTION ==========")
    println("Clase analizada: ${clase.simpleName}")

    println("\nPROPIEDADES:")

    val propiedades = clase.declaredFields
        .filter { !it.isSynthetic }

    if (propiedades.isEmpty()) {
        println("No se encontraron propiedades.")
    } else {
        propiedades.forEach {
            println("- ${it.name}: ${it.type.simpleName}")
        }
    }

    println("\nMÉTODOS:")

    val metodos = clase.declaredMethods
        .filter { !it.isSynthetic && !it.name.contains("\$") }

    if (metodos.isEmpty()) {
        println("No se encontraron métodos.")
    } else {
        metodos.forEach {
            println("- ${it.name}()")
        }
    }

    println("============================================")
}