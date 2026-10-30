data class Producto(
    val nombre: String,
    val precio: Double,
    val ingredientes: List<String>
) {

    fun detallarProducto(): String {
        return """
            Pizza: $nombre
            Precio: ${formatearPrecio(precio)}
            Ingredientes: ${ingredientes.joinToString(", ")}
        """.trimIndent()
    }
}
