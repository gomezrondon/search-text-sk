package com.gomezrondon.springkotlin.utils

import com.google.gson.GsonBuilder
import java.io.File
import java.math.BigInteger
import java.security.MessageDigest
import java.util.concurrent.TimeUnit

fun getMD5(index_name:String):String {
    val f_name = "repository" + File.separator + "output_$index_name.txt"

    if (File(f_name).exists()) {
        val md5_hash_str = File(f_name).readLines()
                .map { it.replace("""\s""".toRegex(), "") }
                .filter { !it.contains("bytesfree|bytes".toRegex()) }
                .joinToString("").md5()

        return md5_hash_str
    }
    return ""
}


fun String.md5(): String {
    val md = MessageDigest.getInstance("MD5")
    return BigInteger(1, md.digest(this.toByteArray())).toString(16).padStart(32, '0')
}


fun saveMD5(index_name: String, new_md5: String) {
    val f_name_md5 = "repository" + File.separator + "md5_$index_name.txt"
    File(f_name_md5).writeText(new_md5)
}


fun checkFolders(index_name: String, new_md5: String): Boolean {
    var folderHasChanged = false
    val f_name_md5 = "repository" + File.separator + "md5_$index_name.txt"
    if (File(f_name_md5).exists()) {
        val old_md5 = File(f_name_md5).readLines().joinToString ("")
        if (new_md5 != old_md5) {
            folderHasChanged = true
        }
    }else{
        saveMD5(index_name, new_md5)
        folderHasChanged = true
    }
    return folderHasChanged
}



public fun getFolderName(folder: String) = folder.split(File.separator).last().toLowerCase()

fun getListOfFilesInFolder(folder: String, index_name:String) {
    val f_name = "repository" + File.separator + "output_$index_name.txt"
    "cmd.exe /c cd $folder & dir /s /A-D".runCommand(timeout = 15, outPutFile = f_name)
}

fun String.runCommand(workingDir: File? = null, timeout:Long, outPutFile:String) {
    if (File(outPutFile).exists()) {
        File(outPutFile).delete()
    }
    val process = ProcessBuilder(*split(" ").toTypedArray())
            .directory(workingDir)
            .redirectOutput(ProcessBuilder.Redirect.appendTo(File(outPutFile)))
            .redirectError(ProcessBuilder.Redirect.INHERIT)
            .start()

    // File(outPutFile).readLines().forEach { println(it) }

    if (!process.waitFor(timeout, TimeUnit.SECONDS)) {
        process.destroy()
        throw RuntimeException("execution timed out: $this")
    }
    if (process.exitValue() != 0) {
        throw RuntimeException("execution failed with code ${process.exitValue()}: $this")
    }


}

fun loadFolders() = File("repository${File.separator}white-list.txt").readLines().filter { !it.startsWith("--") }
fun dontSearchList() = File("repository${File.separator}black-list.txt").readLines()
fun loadWordFiles() = File("repository${File.separator}words").listFiles().asList()


fun filterBlackListPath(noSearchList: List<String>, it: File): Boolean {
    var exist = true
    for (dir: String in noSearchList) {
        if (it.absolutePath.startsWith(dir)) {
            exist = false
        }
    }
    return exist
}


fun convertToJson(objet:Any):String{
    val gson = GsonBuilder().setPrettyPrinting().create() // for pretty print feature
    val jsonStr : String = gson.toJson(objet)
    return jsonStr
}