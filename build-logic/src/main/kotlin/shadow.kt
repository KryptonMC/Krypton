import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import com.github.jengelman.gradle.plugins.shadow.transformers.Transformer
import org.gradle.api.tasks.util.PatternFilterable

inline fun <reified T : Transformer> ShadowJar.transform(): ShadowJar = transform(T::class.java)

fun PatternFilterable.fastutilExclusions(vararg names: String): PatternFilterable = exclude(names.map(::excludeFastutil))

private fun excludeFastutil(name: String): String = "it/unimi/dsi/fastutil/$name/**"
