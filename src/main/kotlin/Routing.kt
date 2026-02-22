package com.example

import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.sql.Connection
import java.sql.DriverManager
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.selectAll
import kotlinx.coroutines.Dispatchers
import com.example.dbQuery
import com.example.Post
import com.example.Posts
import com.example.Meme
import com.example.Memes
import com.example.Command
import com.example.Commands


fun Application.configureRouting() {
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            call.respondText(text = "500: $cause" , status = HttpStatusCode.InternalServerError)
        }
    }
    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        get("/memes") {
          val allMemes = dbQuery {
            // SelectAll() gets everything from the 'memes' table
            Memes.selectAll().map {
                Meme(
                    id = it[Memes.id],
                    url = it[Memes.url],
                    alttext = it[Memes.alttext],
                    addtext = it[Memes.addtext]
                )
            }
          }
        
          // If the list is empty, it returns [], otherwise it sends the JSON
            call.respond(allMemes)
        }

get("/posts") {
        val allPosts = dbQuery {
            Posts.selectAll().map {
                Post(
                    id = it[Posts.id],
                    headline = it[Posts.headline],
                    text = it[Posts.text],
                    author = it[Posts.author]
                )
            }
        }
        call.respond(allPosts)
    }

    get("/commands") {
        val allCommands = dbQuery {
            Commands.selectAll().map {
                Command(
                    id = it[Commands.id],
                    command = it[Commands.command],
                    explaination = it[Commands.explaination] // Uses the SQL-matching name
                )
            }
        }
        call.respond(allCommands)
    }
    }
}
