package com.gomezrondon.springkotlin.entities

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import java.io.File


data class Paquete(val file: File, var lines:List<String> )


@Document(collection = "documentx")
data class DataFile(@Id @Field("doc_id") val id:String

                    , @Indexed(name = "mane_index_60", expireAfterSeconds = 60)
                    val name:String = "NO-NAME"
                    , val type:String = "data-file"
                    , val path:String="",
                    @Field("doc_texto")
                    val lines:List<String>){

}


