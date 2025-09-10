package com.example.bbltripplanner.common.utils

import okhttp3.Request

object RequestUtils {
    fun Request.toCurl(): String {
        val curlCmd = StringBuilder("curl -X ${this.method} \"${this.url}\"")

        headers.forEach { header ->
            curlCmd.append(" -H \"${header.first}: ${header.second}\"")
        }

        this.body?.let { body ->
            val buffer = okio.Buffer()
            body.writeTo(buffer)
            val bodyString = buffer.readUtf8()
            curlCmd.append(" -d '$bodyString'")
        }

        return curlCmd.toString()
    }
}