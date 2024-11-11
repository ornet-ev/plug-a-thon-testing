import org.ornet.pat.mdib.v2.PAT_MDIB_V2
import org.somda.dsl.rendering.jaxb.renderTo
import java.io.File

fun main() {
    listOf(
        PAT_MDIB_V2
    ).onEach {
        it.renderTo(File("generated_mdibs"))
    }
}