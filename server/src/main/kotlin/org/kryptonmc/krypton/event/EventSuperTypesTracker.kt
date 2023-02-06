package org.kryptonmc.krypton.event

object EventSuperTypesTracker {

    private val superTypes = HashMap<Class<*>, Collection<Class<*>>>()

    fun getSuperTypesOf(type: Class<*>): Collection<Class<*>> {
        val existing = superTypes.get(type)
        if (existing != null) return existing

        val types = findSuperTypes(type)
        superTypes.put(type, types)
        return types
    }

    private fun findSuperTypes(type: Class<*>): Collection<Class<*>> {
        val result = ArrayList<Class<*>>()
        val superclass = type.superclass
        if (superclass != null && superclass !== Any::class.java) result.add(superclass)
        result.addAll(type.interfaces)
        return result
    }
}
