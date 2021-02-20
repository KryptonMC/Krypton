package org.kryptonmc.krypton.extension

import java.net.InetAddress

fun String.toInetAddress() = InetAddress.getByName(this)