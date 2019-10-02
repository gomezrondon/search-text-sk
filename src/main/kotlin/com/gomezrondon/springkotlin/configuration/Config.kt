package com.gomezrondon.springkotlin.configuration

import com.mongodb.MongoClient
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import org.bson.Document
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AppConfig {
    /*
   * Use the standard Mongo driver API to create a com.mongodb.MongoClient instance.
   */

    @Value("\${spring.data.mongodb.database}")
    private val dbName = ""

    @Value("\${spring.data.mongodb.host}")
    private val mongoHost = ""

    @Bean
    fun mongoClient(): MongoClient {
        return MongoClient(mongoHost)
    }

    @Bean
    fun getMongoDB(mongoClient: MongoClient): MongoDatabase {
        return mongoClient.getDatabase(dbName)
    }

    @Bean
    fun getMongoCollection(mongoDB: MongoDatabase): MongoCollection<Document> {
        val collection = mongoDB.getCollection("documentx")
        return collection
    }


}