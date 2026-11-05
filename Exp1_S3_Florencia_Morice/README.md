# Pizzería - Sistema de Gestión

## Descripción del proyecto

Este proyecto consiste en una aplicación de consola desarrollada en Kotlin para gestionar los pedidos de una pizzería.

El sistema permite registrar clientes y pedidos, administrar los estados de los pedidos, consultar y actualizar el inventario de ingredientes, calcular precios y aplicar descuentos.

Además, incorpora funcionalidades avanzadas de Kotlin correspondientes a la Semana 3, como sobrecarga de operadores, comparación de objetos, desestructuración, anotaciones personalizadas y Reflection.

## Tecnologías utilizadas

- Kotlin
- IntelliJ IDEA
- JDK 17

## Funcionalidades principales

La aplicación cuenta con un menú principal que permite realizar las siguientes operaciones:

1. **Registrar nuevo pedido:** permite ingresar los datos del cliente, seleccionar productos, indicar cantidades, agregar notas y asignar un repartidor.
2. **Cambiar estado de un pedido:** permite actualizar el estado a En preparación, Listo o En camino.
3. **Mostrar reporte de pedidos:** presenta el detalle de los pedidos, sus totales y el estado de cada uno.
4. **Mostrar inventario:** permite consultar las cantidades disponibles de los ingredientes.
5. **Reabastecer ingredientes:** permite agregar unidades al inventario.
6. **Validar precio:** comprueba que el precio ingresado sea mayor que cero.
7. **Demostrar funcionalidades Semana 3:** ejecuta ejemplos de las funcionalidades avanzadas implementadas.

## Funcionalidades de Kotlin - Semana 3

El proyecto incorpora los siguientes conceptos:

- **Validaciones:** correo electrónico, teléfono, fechas y cantidades numéricas entre 1 y 100.
- **Anotaciones personalizadas:** implementación de la anotación `@Promocionable` en clases y propiedades.
- **Reflection:** permite consultar y mostrar las propiedades y métodos de las clases `Producto` y `Pedido`.
- **Sobrecarga de operadores:** implementación del operador `+` para combinar pedidos del mismo cliente.
- **Comparación de objetos:** utilización de `==` para comparar clientes y productos.
- **Equals y HashCode:** redefinición de estos métodos para comparar objetos según sus características.
- **HashSet:** utilización de colecciones para evitar elementos duplicados.
- **Desestructuración:** implementación de componentes para extraer información de clientes y pedidos.

## Organización del proyecto

El proyecto está organizado en los siguientes archivos:

| Archivo | Descripción |
|---|---|
| `Main.kt` | Contiene el menú principal y la ejecución de la aplicación. |
| `Usuario.kt` | Define los usuarios, clientes y repartidores. |
| `Producto.kt` | Define los productos y la anotación personalizada. |
| `Pedido.kt` | Gestiona los pedidos, cálculos, descuentos y estados. |
| `Inventario.kt` | Administra el stock de ingredientes. |
| `Funciones.kt` | Contiene las validaciones y funciones auxiliares. |

## Cálculos y promociones

El sistema contempla los siguientes cálculos:

- Descuento del 10% cuando el pedido supera las 3 unidades.
- Descuento promocional del 5% sobre productos promocionables durante noviembre de 2026.
- Cálculo del IVA del 19%.
- Cálculo del total final del pedido.

## Ejecución del proyecto

Para ejecutar la aplicación:

1. Abrir el proyecto en IntelliJ IDEA.
2. Configurar JDK 17.
3. Abrir el archivo `Main.kt`.
4. Ejecutar la función `main()`.
5. Utilizar las opciones disponibles en el menú de consola.

## Conclusión

El desarrollo permitió aplicar conceptos fundamentales y avanzados de Kotlin mediante una aplicación práctica de gestión de una pizzería.

Se incorporaron validaciones, manejo de objetos, colecciones, sobrecarga de operadores, anotaciones personalizadas y Reflection, integrando estos contenidos en un sistema funcional y organizado.