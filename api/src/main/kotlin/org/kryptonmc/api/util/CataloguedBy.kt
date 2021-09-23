package org.kryptonmc.api.util

import java.lang.annotation.Inherited
import kotlin.reflect.KClass

/**
 * Marker annotation indicating that the annotated type is catalogued by the
 * given [catalogue] type.
 *
 * For requirements of catalogue types, see the [Catalogue] annotation.
 *
 * @param catalogue the type of the catalogue, the type that is annotated with
 * [Catalogue]
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
@Inherited
@MustBeDocumented
// Also, for all of you americans out there, it's "catalogue", not "catalog",
// that's why this annotation is named as such. This is one of the spellings
// I won't budge on.
public annotation class CataloguedBy(public val catalogue: KClass<*>)
