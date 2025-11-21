package com.example.demo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan 
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication
@EnableJpaRepositories(basePackages = ["com.example.demo.user", "com.example.demo.task"])
@ComponentScan(basePackages = ["com.example.demo", "com.example.demo.user", "com.example.demo.task"])class BeAppMApplication

fun main(args: Array<String>) {
	runApplication<BeAppMApplication>(*args)
}
