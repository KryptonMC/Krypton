package me.bristermitten.minekraft.encryption

import java.security.KeyPair
import java.security.KeyPairGenerator

object Encryption
{
    val keyPair: KeyPair = genKeyPair()

    private fun genKeyPair(): KeyPair
    {
        val generator = KeyPairGenerator.getInstance("RSA")
        generator.initialize(1024)
        return generator.generateKeyPair()
    }
}
