package me.bristermitten.minekraft

import com.google.common.collect.Sets

object SessionStorage
{
	val sessions: MutableSet<Session> = Sets.newConcurrentHashSet()

	fun pulse()
	{
		sessions.forEach {
			it.pulse()
		}
	}
}
