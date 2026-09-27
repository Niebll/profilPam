package com.example.anative.modul_4

import kotlinx.coroutines.delay
import kotlin.random.Random

class Repo {
    companion object {
        suspend fun getData() : Int {
            delay(2000) // simulating network
            return Random.nextInt(100, 1000)
        }
    }
}