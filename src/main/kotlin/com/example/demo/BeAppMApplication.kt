package com.example.demo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication
// Forzamos a Spring a buscar Repositorios y Entidades en tu paquete base
@EnableJpaRepositories(basePackages = ["com.example.demo"])
@EntityScan(basePackages = ["com.example.demo"])
class BeAppMApplication

fun main(args: Array<String>) {
    runApplication<BeAppMApplication>(*args)
}