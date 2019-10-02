package com.gomezrondon.springkotlin.service

import com.gomezrondon.springkotlin.entities.DataFile
import java.util.*


interface DataFileService {

    fun insertDataFile(dataFile: DataFile)

    fun insertDataFile(dataFiles: List<DataFile>)

    fun updateDataFile(dataFile: DataFile)

    fun updateDataFile(dataFiles: List<DataFile>)

    fun findById(Id: String): Optional<DataFile>

    fun findByPathWithRegex(folder:String): List<String>?

    fun findByType(type: String?): DataFile?

    fun findByFileNameWithRegex(folder: String): List<String>?
}