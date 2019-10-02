package com.gomezrondon.springkotlin.service

import com.gomezrondon.springkotlin.repositories.DataFileRepository
import org.springframework.stereotype.Service

@Service
class SearchServiceImpl(private val service: DataFileService
                        , private val repository: DataFileRepository): SearchService {

    override fun searchByFileName(fileName: String): List<String> {
        val list: List<String> = service.findByFileNameWithRegex(fileName) ?: emptyList()
        return list
    }

    override fun searchInFile(word: String): List<String> {

        val searchWord = """(\w){3,}""".toRegex().findAll(word).map { it.value }.map { it.toLowerCase() }.joinToString(" ")
        val resultList = repository.findByTextSearch(searchWord)?.filter { dataFile ->
            val find = dataFile.lines.find { it1 -> it1.contains(searchWord) }
            !find.isNullOrEmpty()
        }?.map { it.path } ?: emptyList()

        return  resultList
    }


}