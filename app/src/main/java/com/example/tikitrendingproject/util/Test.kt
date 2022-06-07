package com.example.tikitrendingproject.util

import kotlinx.coroutines.*

class Test {
    suspend fun funA() {
        print("fun a")
        delay(10000)
    }

    suspend fun funB() {
        print("fun a")
        delay(10000)
    }

    //    fun main(){
//        GlobalScope.launch {
//            funA()
//            funB()
//        }
//    }
    // Để dừng và hủy bỏ một coroutine đang chạy. Ta có thể dùng method cancel() của biến Job
    fun main() = runBlocking(Dispatchers.IO) {
        val job =
            launch {
                repeat(1000) { i ->
                    println("I'm sleeping $i ...")
                    delay(500L)
                }
            }
        delay(1300L) // delay a bit
        println("main: I'm tired of waiting!")
        job.cancel() // cancels the job
        println("main: Now I can quit.")
    }

    fun main3() = runBlocking {
        val int: Deferred<Int> = async { printInt() }
        val str: Deferred<String> = async { return@async "Sun" }
        val unit: Deferred<Unit> = async { }
        println("Int = ${int.await()}")
        println("String = ${str.await()}")
    }

    fun printInt(): Int {
        return 10
    }

    fun main4() = runBlocking<Unit> {
        val request = launch { // it spawns two other jobs, one with GlobalScope 
            GlobalScope.launch {
                println("job1: GlobalScope and execute independently!")
                delay(1000)
                println("job1: I am not affected by cancellation") // line code 1 này vẫn được in ra mặc dù bị delay 1000ms } 
                // and the other inherits the parent context 
                launch {
                    delay(100)
                    println("job2: I am a child of the request coroutine")
                    delay(1000)
                    println("job2: I will not execute this line if my parent request is cancelled")
                }
            }
        }
        delay(500)
        request.cancel()
        // cancel processing of the request
        delay(1000) // delay a second to see what happens
        println("main: Who has survived request cancellation?")

    }

}


