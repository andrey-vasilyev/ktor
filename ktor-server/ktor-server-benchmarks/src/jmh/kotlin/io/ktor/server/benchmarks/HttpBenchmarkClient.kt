/*
 * Copyright 2014-2019 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

@file:Suppress("KDocMissingDocumentation")

package io.ktor.server.benchmarks

import okhttp3.*
import org.apache.http.client.methods.*
import org.apache.http.impl.client.*
import java.net.*

public interface HttpBenchmarkClient {
    public fun setup()
    public fun shutdown()
    public fun load(url: String)
}

public class UrlHttpBenchmarkClient : HttpBenchmarkClient {
    override fun setup() {}
    override fun shutdown() {}
    override fun load(url: String) {
        val urlConnection = URL(url).openConnection() as HttpURLConnection
        urlConnection.setRequestProperty("Accept-Encoding", "gzip")
        val stream = urlConnection.inputStream
        val buf = ByteArray(8192)
        while (stream.read(buf) != -1);
        stream.close()
    }
}

public class ApacheHttpBenchmarkClient : HttpBenchmarkClient {
    var httpClient: CloseableHttpClient? = null

    override fun setup() {
        val builder = HttpClientBuilder.create()
        httpClient = builder.build()
    }

    override fun shutdown() {
        httpClient!!.close()
        httpClient = null
    }

    override fun load(url: String) {
        val httpGet = HttpGet(url)
        val response = httpClient!!.execute(httpGet)
        val stream = response.entity.content
        val buf = ByteArray(8192)
        while (stream.read(buf) != -1);
        stream.close()
        response.close()
    }
}

public class OkHttpBenchmarkClient : HttpBenchmarkClient {
    var httpClient: OkHttpClient? = null

    override fun setup() {
        httpClient = OkHttpClient()
    }

    override fun shutdown() {
        httpClient = null
    }

    override fun load(url: String) {
        val request = Request.Builder().url(url).build()
        val response = httpClient!!.newCall(request).execute()
        response.body()?.byteStream()?.use { stream ->
            val buf = ByteArray(8192)
            while (stream.read(buf) != -1);
        }
        response.close()
    }
}
