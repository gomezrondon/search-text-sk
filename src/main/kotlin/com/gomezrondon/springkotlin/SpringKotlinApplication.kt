package com.gomezrondon.springkotlin

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class SpringKotlinApplication

fun main(args: Array<String>) {
	runApplication<SpringKotlinApplication>(*args)
}

