package com.example.github_app

import java.io.BufferedReader

object TestUtilities {

    fun getResponseBodyFromJsonFile(filename: String): String {
        val body = javaClass.classLoader?.getResourceAsStream(filename)
        val reader = BufferedReader(body!!.reader())
        var content = ""
        reader.use { r ->
            content = r.readText()
        }
        return content
    }
}