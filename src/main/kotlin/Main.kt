import org.ornet.pat.mdib.v2.patMdibV2
import org.somda.dsl.rendering.jaxb.renderTo
import java.io.File

fun main() {
    val outputDir = File("generated_mdibs")
    val mdib = patMdibV2(outputDir)
    mdib.renderTo(outputDir)
}