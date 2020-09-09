/*
 * Copyright 2014-2019 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package io.ktor.server.benchmarks

import io.ktor.server.benchmarks.cio.*
import io.ktor.server.benchmarks.jetty.*
import io.ktor.server.benchmarks.netty.*
import org.openjdk.jmh.annotations.*

@State(Scope.Benchmark)
public abstract class PlatformBenchmark {
    private val httpClient = OkHttpBenchmarkClient()
    private val port = 5678

    public abstract fun runServer(port: Int)
    public abstract fun stopServer()

    @Setup
    public fun setupServer() {
        runServer(port)
    }

    @TearDown
    public fun shutdownServer() {
        stopServer()
    }

    @Setup
    public fun configureClient() {
        httpClient.setup()
    }

    @TearDown
    public fun shutdownClient() {
        httpClient.shutdown()
    }

    private fun load(url: String) {
        httpClient.load(url)
    }

    @Benchmark
    public fun sayOK() {
        load("http://localhost:$port/sayOK")
    }
}

/*
Benchmark                      Mode  Cnt   Score   Error   Units

JettyPlatformBenchmark.sayOK  thrpt   20  42.875 ± 1.089  ops/ms
NettyPlatformBenchmark.sayOK  thrpt   20  61.736 ± 1.792  ops/ms
*/

public fun main(args: Array<String>) {
    benchmark(args) {
        threads = 32
        run<NettyPlatformBenchmark>()
        run<JettyPlatformBenchmark>()
        run<CIOPlatformBenchmark>()
    }
}
