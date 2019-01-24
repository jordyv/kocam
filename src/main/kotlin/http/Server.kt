package nl.jordyversmissen.kocam.http

import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.http.content.resource
import io.ktor.http.content.resources
import io.ktor.http.content.static
import io.ktor.jackson.jackson
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.route
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import nl.jordyversmissen.kocam.dataDirectory
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.streams.toList

class Server(
    private val port: Int = 8080
) {
    fun start() {
        embeddedServer(Netty, port) {
            install(ContentNegotiation) {
                jackson {}
            }
            routing {
                static {
                    resource("/", "web-ui/index.html")
                    resource("*", "web-ui/index.html")
                    static("/statics") {
                        resources("web-ui/statics")
                    }
                }
                route("/api") {
                    get("/images") {
                        call.respond(getImagesResponse())
                    }
                }
            }
        }.start()
    }

    private fun getImagesResponse(): Map<String, Any> = mapOf(
        "images" to Files.list(Paths.get(dataDirectory)).toList().map {
            mapOf(it.toFile().lastModified() / 1000 to it.fileName.toString())
        }
    )
}
