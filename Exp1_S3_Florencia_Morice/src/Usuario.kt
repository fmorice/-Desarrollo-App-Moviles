open class Usuario(
    val nombre: String,
    val contacto: String
)

class Cliente(
    nombre: String,
    contacto: String,
    val direccion: String,
    val correo: String
) : Usuario(nombre, contacto) {

    // Comparación de clientes por nombre y correo
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Cliente) return false

        return nombre == other.nombre &&
                correo == other.correo
    }

    // Generación de hash según los atributos clave
    override fun hashCode(): Int {
        var resultado = nombre.hashCode()
        resultado = 31 * resultado + correo.hashCode()
        return resultado
    }

    // Desestructuración del cliente
    operator fun component1(): String = nombre
    operator fun component2(): String = correo
    operator fun component3(): String = contacto
}

class Repartidor(
    nombre: String,
    contacto: String,
    var disponible: Boolean = true,
    val distanciaKm: Double
) : Usuario(nombre, contacto) {

    fun entregarPedido(): String {
        return "El repartidor $nombre está realizando la entrega."
    }
}
