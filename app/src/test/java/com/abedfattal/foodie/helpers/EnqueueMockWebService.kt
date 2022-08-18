package com.abedfattal.foodie.helpers

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.buffer
import okio.source
import java.nio.charset.StandardCharsets

interface EnqueueMockWebService {

    val endpointsWithResponseFile: Map<String, String>

    fun createMockResponse(): MockResponse = MockResponse()

    fun buildFile(file: String, code: Int) = "api-response/$file-$code.json"

    fun MockWebServer.enqueueResponse(endpoint: String, code: Int) {
        val file = buildFile(file = endpointsWithResponseFile.getValue(endpoint), code)
        val inputStream = javaClass.classLoader?.getResourceAsStream(file)
            ?: throw IllegalArgumentException("Can't find resource file with:$file")

        val source = inputStream.source().buffer()
        enqueue(
            createMockResponse()
                .setResponseCode(code)
                .setBody(source.readString(StandardCharsets.UTF_8))
        )
    }

}