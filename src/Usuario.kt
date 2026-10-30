open class Usuario(
    val nombre: String,
    val contacto: String
)

class Cliente(
    nombre: String,
    contacto: String,
    val direccion: String,
    val correo: String
) : Usuario(nombre, contacto)

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
