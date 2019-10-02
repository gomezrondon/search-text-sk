package com.gomezrondon.springkotlin.service

import com.gomezrondon.springkotlin.entities.DataFile
import com.gomezrondon.springkotlin.entities.Paquete
import com.gomezrondon.springkotlin.utils.*
import com.mongodb.client.MongoCollection
import com.mongodb.client.model.*
import org.bson.Document
import org.springframework.stereotype.Service
import java.io.File


@Service
class IndexServiceImpl(private val service: DataFileService, private val collection: MongoCollection<Document>):IndexService {


    override fun createIdexes() {

        val listIndexes = collection.listIndexes()

        var indexExist = false
        listIndexes.forEach {
            for ((k,v) in it) {
                // println("K: $k value: $v")
                if (k == "name" && v == "doc_texto_text") {
                    indexExist= true
                    println("Index: $v  Exist!")
                }
            }
        }

        if (!indexExist) {
            collection.createIndex(Indexes.text("doc_texto"))
            println("Index: doc_texto_text Created!")
        }
    }

    override fun resetIndexSnapShot() {
        File("repository").walkTopDown()
                .filter { it.isFile }
                .filter { it.name.startsWith("md5_")}
                .forEach {
                    println("DELETING $it !!!")
                    it.delete()
                }

    }


    override fun indexFilesByName(folders: List<String>) {

        folders.parallelStream().forEach { folder ->
            //    folders.forEach { folder ->
            val folderName = getFolderName(folder)
            getListOfFilesInFolder(folder, folderName)
            val new_md5 = getMD5(folderName)

            var folderHasChanged = checkFolders(folderName, new_md5)

            println(folderHasChanged.toString() + " "+folder)

            if (folderHasChanged) {
                indexOnlyByfileName(listOf(folder)) // write to mongo
            }
            saveMD5(folderName, new_md5)
        }

    }


     private fun indexOnlyByfileName(folders: List<String>) {
        val noSearchList = dontSearchList()


        folders.parallelStream().forEach { folder ->
            //folders.forEach { folder ->
            var DFList = mutableListOf<DataFile>()
            val existList: List<String>? = service.findByPathWithRegex(folder)

            File(folder).walkTopDown()
                    .filter { it.isFile}
                    .filter{filterBlackListPath(noSearchList, it) }
                    .filter { !existList?.contains(it.absolutePath.toString().md5())!! }
                    .forEach {
                        // build the document
                        val dataFile = DataFile(id = it.absolutePath.toString().md5()
                                ,name = it.name.toLowerCase()
                                ,path = it.absolutePath.toLowerCase()
                                ,lines = listOf() )
                      //  service.insertDataFile(dataFile)
                        DFList.add(dataFile)
                    }

            if (DFList.isNotEmpty()) {
                service.insertDataFile(DFList.toList())
            }

        }

        println("Finish readBinaryfiles...")

    }

    override fun indexTextFiles(folders: List<String>) {

        val noSearchList = dontSearchList()
        val textFileList = listOf("txt","sql","java","py","bat","csv","kt","kts")

        print("Inserting: ")

        folders.parallelStream().forEach { folder ->
          val existList: List<String> = service.findByPathWithRegex(folder) ?: mutableListOf()
            File(folder).walkTopDown()
                    .filter { textFileList.contains(it.extension) }
                    .filter{filterBlackListPath(noSearchList, it) }
                    .map { Paquete(it, it.readLines() ) }
                    .map {
                        val wordList =it.lines.flatMap { """(\w){3,}""".toRegex().findAll(it).map { it.value }.map { it.toLowerCase() }.toList() }
                        it.lines = wordList
                        it
                    }
                    .forEach {
                        writeToMongo(it, existList)
                    }
        }
        println("Finish readTextFile...")
    }


    private fun writeToMongo( it: Paquete,  existList: List<String>) {

        var lineas = mutableListOf<String>()
        var countLetters = 0
        var line = StringBuilder("")

        it.lines.forEach { word ->
            if (countLetters > 500) {
                line.append(word + " ")
                //out.write(line.toString() + "\n")
                lineas.add(line.toString())
                line = StringBuilder("")
                countLetters = 0
            } else {
                line.append(word + " ")
            }
            countLetters = line.length
        }
        if (line.isNotEmpty()) {
            lineas.add(line.toString())
        }

        // build the document
        val dataFile = DataFile(id = it.file.absolutePath.toString().md5()
                ,name = it.file.name.toLowerCase()
                ,path = it.file.absolutePath.toLowerCase()
                ,lines = lineas )

        if (existList.contains(it.file.absolutePath.toString().md5()) ) {
            service.updateDataFile(dataFile)
        }else{
            service.insertDataFile(dataFile)
        }

        print(".")


    }

}