import org.ornet.pat.localization.db.v1.LocalizationServiceHandler
import org.ornet.pat.mdib.v2.PAT_MDIB_V2
import org.somda.dsl.biceps.installPostProcessor
import org.somda.dsl.rendering.jaxb.renderTo
import java.io.File

fun main() {
    val outputDir = File("generated_mdibs")

    installPostProcessor(LocalizationServiceHandler(outputDir))
    listOf(
        PAT_MDIB_V2
    ).onEach {
        it.renderTo(outputDir)
    }
}