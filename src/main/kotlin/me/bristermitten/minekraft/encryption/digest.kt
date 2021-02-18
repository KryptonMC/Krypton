package me.bristermitten.minekraft.encryption

import java.math.BigInteger
import java.security.MessageDigest

fun MessageDigest.hexDigest() = BigInteger(this.digest()).toString(16)