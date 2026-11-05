@Target(
    AnnotationTarget.CLASS,
    AnnotationTarget.FIELD,
    AnnotationTarget.PROPERTY
)
@Retention(AnnotationRetention.RUNTIME)
annotation class Promocionable

@Promocionable
data class Producto(
    val nombre: String,
    val precio: Double,
    val ingredientes: List<String>,
    val categoria: String = "pizza"
) {

    // Compara productos por nombre y categoría
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Producto) return false

        return nombre.equals(other.nombre, ignoreCase = true) &&
                categoria.equals(other.categoria, ignoreCase = true)
    }

    // Genera un código hash según nombre y categoría
    override fun hashCode(): Int {
        return 31 * nombre.lowercase().hashCode() +
                categoria.lowercase().hashCode()
    }

    // Muestra el detalle del producto
    fun detallarProducto(): String {
        return """
            Pizza: $nombre
            Precio: ${formatearPrecio(precio)}
            Ingredientes: ${ingredientes.joinToString(", ")}
            Categoría: $categoria
        """.trimIndent()
    }
}
