package com.example.sentrycallnew

object HashUtil {

    fun normalize(input: String?): String {
        if (input == null) return ""

        return input
            .lowercase()
            .replace(Regex("[^a-z0-9 ]"), "")
            .replace(Regex("\\s+"), " ")
            .trim()
    }

    fun sha256(input: String): String {
        val bytes = java.security.MessageDigest
            .getInstance("SHA-256")
            .digest(input.toByteArray())

        return bytes.joinToString("") {
            "%02x".format(it)
        }
    }

    fun getSecureHash(name: String?): String {
        val clean = normalize(name)
        return sha256(clean)
    }
}