package me.bmkil.application

import ShoppingListItem
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.html.*
import io.ktor.server.http.content.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.compression.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.html.*

val shoppingList = mutableListOf(
    ShoppingListItem("Cucumbers 🥒", 1),
    ShoppingListItem("Tomatoes 🍅", 2),
    ShoppingListItem("Orange Juice 🍊", 3)
)

fun HTML.home() {
    head {
        title("Full Stack Shopping List")
    }
    body {
        div {
            id = "root"
        }
        script(src = "/static/Full-Stack-Shopping-List.js") {}
    }
}

fun main() {
    embeddedServer(Netty, port = 9090) {

        install(ContentNegotiation) { json() }
        install(Compression) { gzip() }
        install(CORS) {
            allowHeader(HttpHeaders.ContentType)
            allowMethod(HttpMethod.Get)
            allowMethod(HttpMethod.Post)
            allowMethod(HttpMethod.Delete)
            anyHost()
        }

        routing {
            get("/") {
                call.respondHtml(HttpStatusCode.OK, HTML::home)
            }
            static("/static") {
                resources()
            }

            route(ShoppingListItem.path) {
                get {
                    println("Server - Get Shopping list")
                    call.respond(shoppingList)
                }
                post {
                    println("Server - Adding Item in Shopping List")
                    shoppingList += call.receive<ShoppingListItem>()
                    call.respond(HttpStatusCode.OK)
                }
                delete("/{id}") {
                    val id = call.parameters["id"]?.toInt() ?: error("Invalid delete request")
                    shoppingList.removeIf { it.id == id }
                    call.respond(HttpStatusCode.OK)
                }
            }
        }
    }.start(wait = true)
}
