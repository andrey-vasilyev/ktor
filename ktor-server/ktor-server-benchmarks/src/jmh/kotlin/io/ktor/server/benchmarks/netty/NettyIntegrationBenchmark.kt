/*
 * Copyright 2014-2019 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package io.ktor.server.benchmarks.netty

import io.ktor.application.*
import io.ktor.server.benchmarks.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

public class NettyIntegrationBenchmark : IntegrationBenchmark<NettyApplicationEngine>() {
    override fun createServer(port: Int, main: Application.() -> Unit): NettyApplicationEngine {
        return embeddedServer(Netty, port, module = main)
    }
}

public class NettyAsyncIntegrationBenchmark : AsyncIntegrationBenchmark<NettyApplicationEngine>() {
    override fun createServer(port: Int, main: Application.() -> Unit): NettyApplicationEngine {
        return embeddedServer(Netty, port, module = main)
    }
}
