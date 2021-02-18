package me.bristermitten.minekraft.extension

import java.net.InetAddress

fun String.toInetAddress() = InetAddress.getByName(this)