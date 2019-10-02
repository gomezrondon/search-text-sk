package com.gomezrondon.springkotlin.repositories

import com.gomezrondon.springkotlin.entities.DataFile
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import org.springframework.stereotype.Repository

//https://www.technicalkeeda.com/spring-tutorials/spring-4-mongodb-repository-example
//https://www.baeldung.com/spring-data-mongodb-tutorial

@Repository
interface DataFileRepository: MongoRepository<DataFile, String> {

    @Query("{'type': ?0 }")
    fun findByType(model: String?): DataFile? // funciona!!

    @Query("{ path: { \$regex: ?0 } }")
    fun findByRegex(regex: String): List<DataFile>?

    @Query("{ name: { \$regex: ?0 } }")
    fun findNameByRegex(regex: String): List<DataFile>?

    @Query("{ \$text: { \$search: ?0 } }")
    fun findByTextSearch(search: String): List<DataFile>?

}