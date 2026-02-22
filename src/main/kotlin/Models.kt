package com.example

import org.jetbrains.exposed.sql.*
import kotlinx.serialization.Serializable

// Database Table Definitions
object Posts : Table("posts") {
    val id = integer("id").autoIncrement()
    val headline = varchar("headline", 255)
    val text = varchar("text", 255)
    val author = varchar("author", 255)
    override val primaryKey = PrimaryKey(id)
}

object Commands : Table("commands") {
    val id = integer("id").autoIncrement()
    val command = varchar("command", 255)
    val explaination = varchar("explaination", 255)
    override val primaryKey = PrimaryKey(id)
}

object Memes: Table("memes") {
  val id = integer("id").autoIncrement()
  val url = varchar("url", 255)
  val alttext = varchar("alttext", 255)
  val addtext = varchar("addtext", 255)
  override val primaryKey = PrimaryKey(id)
}

// Serialization Classes (For JSON)
@Serializable
data class Post(val id: Int, val headline: String, val text: String, val author: String)

@Serializable
data class Command(val id: Int, val command: String, val explaination: String)

@Serializable
data class Meme(val id: Int, val url: String, val alttext: String, val addtext: String)
