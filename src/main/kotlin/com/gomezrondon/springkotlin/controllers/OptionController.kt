package com.gomezrondon.springkotlin.controllers

import com.gomezrondon.springkotlin.service.IndexService
import com.gomezrondon.springkotlin.service.SearchService
import com.gomezrondon.springkotlin.utils.convertToJson
import com.gomezrondon.springkotlin.utils.loadFolders
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime
import org.springframework.web.bind.annotation.PathVariable


@RestController
@RequestMapping("/v1")
class OptionController (private val searchService: SearchService
                        ,private val indexService: IndexService){

    @GetMapping("/test")
    fun getTest() {
        println("test EndPoint ${LocalDateTime.now()}!!")
    }

    //http://localhost:8080/v1/options/1/javier%20gomez
    @GetMapping("/options/{option}/{word}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getOption(@PathVariable option: String, @PathVariable word: String=""): String {
        println("Option Selected: $option")

        when(option) {
            "1" -> {
                val result = searchService.searchByFileName(word)
                return convertToJson(result)
            }
            "2" -> {
                val result = searchService.searchInFile(word)
                return convertToJson(result)
            }
            "3" -> {
                val result = ArrayList<String>(searchService.searchByFileName(word))
                result.add("-------------------------------------------------------")
                val result2 = searchService.searchInFile(word)

                result.addAll(result2)
                result.add("Total: ${result.size}")
                return convertToJson(result)
            }
            else -> {
                println("Error wrong option")
            }
        }

        return ""
    }


    @GetMapping("/options/{option}", produces = [MediaType.TEXT_PLAIN_VALUE])
    fun getResetOption(@PathVariable option: String): String {
        println("Option Selected: $option")

        when(option) {
            "0" -> { // index files in folders
                indexService.resetIndexSnapShot()
                return "Finish Resetting..."
            }
            "2" ->{ //index All Now
                val folders = loadFolders()
                indexService.createIdexes()
                indexService.resetIndexSnapShot()
                indexService.indexFilesByName(folders)
                indexService.indexTextFiles(folders)
                return "Finish now-indexing All..."
            }
            "3" -> { // create index
                indexService.createIdexes()
                return "Finish creating indexes..."
            }
            else -> {
                println("Error wrong option")
            }
        }

        return "Finish Resetting..."
    }

}
