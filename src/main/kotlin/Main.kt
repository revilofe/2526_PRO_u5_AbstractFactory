// ============================================================
// Ejemplo didáctico del patrón Abstract Factory en Kotlin
// Contexto: un juego de aventuras que puede ambientarse
// en un mundo medieval o en uno futurista.
//
// Objetivo: crear familias de objetos relacionados sin
// acoplar el cliente a clases concretas.
// ============================================================

// 1) Productos abstractos:
//    Definen "qué" puede hacer cada tipo de producto.
interface Arma {
    fun atacar(): String
}

interface Vehiculo {
    fun mover(): String
}

interface Armadura {
    fun proteger(): String
}

// 2) Fábrica abstracta:
//    Declara los métodos para crear cada tipo de producto.
interface MundoFactory {
    fun crearArma(): Arma
    fun crearVehiculo(): Vehiculo
    fun crearArmadura(): Armadura
}

// 3) Productos concretos para el MUNDO MEDIEVAL:
//    Implementan los contratos de los productos abstractos.
class Espada : Arma {
    override fun atacar(): String = "⚔️  La espada hace *clang* contra el enemigo."
}

class Caballo : Vehiculo {
    override fun mover(): String = "🐎  El caballo galopa entre los árboles."
}

class CotaDeMalla : Armadura {
    override fun proteger(): String = "🛡️  La cota de malla absorbe el impacto."
}

// 4) Productos concretos para el MUNDO FUTURISTA.
class Laser : Arma {
    override fun atacar(): String = "🔫  El láser dispara un rayo de luz azul."
}

class MotoJet : Vehiculo {
    override fun mover(): String = "🏍️  La moto-jet planea a ras del suelo."
}

class EscudoDeEnergia : Armadura {
    override fun proteger(): String = "🛡️  El escudo de energía desvía el daño."
}

// 5) Fábricas concretas:
//    Crean una familia coherente de productos.
class MundoMedievalFactory : MundoFactory {
    override fun crearArma(): Arma = Espada()
    override fun crearVehiculo(): Vehiculo = Caballo()
    override fun crearArmadura(): Armadura = CotaDeMalla()
}

class MundoFuturistaFactory : MundoFactory {
    override fun crearArma(): Arma = Laser()
    override fun crearVehiculo(): Vehiculo = MotoJet()
    override fun crearArmadura(): Armadura = EscudoDeEnergia()
}

// 6) Clase abstracta de dominio:
//    Representa a un explorador, reutiliza comportamiento
//    común y trabaja con productos abstractos.
abstract class Explorador(
    protected val arma: Arma,
    protected val vehiculo: Vehiculo,
    protected val armadura: Armadura
) {
    // Comportamiento común que usa la familia de productos.
    fun explorar(): String = buildString {
        appendLine("🧭  El explorador se prepara para la misión...")
        appendLine(armadura.proteger())
        appendLine(vehiculo.mover())
        appendLine(arma.atacar())
        appendLine("✅  Misión completada en este mundo.")
    }
}

// 7) Personaje concreto que hereda de la clase abstracta.
class ExploradorCurioso(
    arma: Arma,
    vehiculo: Vehiculo,
    armadura: Armadura
) : Explorador(arma, vehiculo, armadura)

// 8) Cliente:
//    Solo conoce la fábrica abstracta y los productos abstractos.
class Juego(private val factory: MundoFactory) {

    // Crear al personaje con el equipamiento adecuado.
    private val explorador: Explorador = ExploradorCurioso(
        arma = factory.crearArma(),
        vehiculo = factory.crearVehiculo(),
        armadura = factory.crearArmadura()
    )

    fun iniciarMision(): String = explorador.explorar()
}

// 9) Etapa de configuración:
//    Aquí se decide qué fábrica concreta usar (fuera del cliente).
enum class TipoMundo { MEDIEVAL, FUTURISTA }

data class ConfiguracionMundo(val tipo: TipoMundo)

object ConfiguradorJuego {
    // Mapea la configuración a la fábrica concreta adecuada.
    fun crearJuego(config: ConfiguracionMundo): Juego {
        val factory = when (config.tipo) {
            TipoMundo.MEDIEVAL -> MundoMedievalFactory()
            TipoMundo.FUTURISTA -> MundoFuturistaFactory()
        }
        return Juego(factory)
    }
}

// 10) Punto de entrada:
//     Crea configuraciones de ejemplo y ejecuta el juego.
fun main() {
    val configuraciones = listOf(
        ConfiguracionMundo(TipoMundo.MEDIEVAL),
        ConfiguracionMundo(TipoMundo.FUTURISTA)
    )

    configuraciones.forEach { config ->
        // Mostrar el nombre del mundo en un formato legible.
        val nombre = config.tipo.name.lowercase().replaceFirstChar { it.uppercase() }
        println("=== Mundo $nombre ===")
        val juego = ConfiguradorJuego.crearJuego(config)
        println(juego.iniciarMision())
        println()
    }

    // Idea para los alumnos:
    // Intenten crear un tercer mundo (por ejemplo "Submarino")
    // y vean que el cliente (Juego) no necesita modificarse.
}
