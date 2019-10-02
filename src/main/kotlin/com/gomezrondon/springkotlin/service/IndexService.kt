package com.gomezrondon.springkotlin.service

interface IndexService {

   fun indexFilesByName(folders: List<String>)

//   fun indexOnlyByfileName(folders: List<String>)

   fun indexTextFiles(folders: List<String>)

   fun resetIndexSnapShot()

   fun createIdexes()
}