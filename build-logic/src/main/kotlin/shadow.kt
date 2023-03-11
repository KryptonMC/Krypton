import org.gradle.api.tasks.util.PatternFilterable

fun PatternFilterable.fastutilExclusions(vararg names: String): PatternFilterable = exclude(names.map(::excludeFastutil))

private fun excludeFastutil(name: String): String = "it/unimi/dsi/fastutil/$name/**"

fun PatternFilterable.dataExclusions(minecraft: String, vararg names: String): PatternFilterable {
    val minecraftPrefix = minecraft.replace('.', '_')
    return exclude(names.map { "${minecraftPrefix}_$it.json" })
}
