package se.ohou.bettertest

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BetterTestApplication

fun main(args: Array<String>) {
    runApplication<BetterTestApplication>(*args)
}
