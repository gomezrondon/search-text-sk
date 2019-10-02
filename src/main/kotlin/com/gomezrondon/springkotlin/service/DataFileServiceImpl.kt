package com.gomezrondon.springkotlin.service


import com.gomezrondon.springkotlin.entities.DataFile
import com.gomezrondon.springkotlin.repositories.DataFileRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class DataFileServiceImpl(private val repository: DataFileRepository) : DataFileService {

    override fun findByType(type: String?): DataFile? {
        return repository.findByType(type)
    }

    override fun findByPathWithRegex(folder: String): List<String>? {
        var word = "^$folder.*".replace("""\""","""\\""").toLowerCase()
        return repository.findByRegex(word)?.map { it.id }
    }

    override fun findByFileNameWithRegex(fileName: String): List<String>? {
        var word = """.*$fileName.*""".toLowerCase()
        return repository.findNameByRegex(word)?.map { it.path.toLowerCase() }
    }

    override fun findById(Id: String): Optional<DataFile> {
        return repository.findById(Id)
    }

    override fun insertDataFile(dataFile: DataFile) {
        repository.insert(dataFile)
    }

    override fun insertDataFile(dataFiles: List<DataFile>) {
        repository.insert(dataFiles)
    }

    override fun updateDataFile(dataFile: DataFile) {
        repository.save(dataFile)
    }

    override fun updateDataFile(dataFiles: List<DataFile>) {
        repository.saveAll(dataFiles)
    }

}
