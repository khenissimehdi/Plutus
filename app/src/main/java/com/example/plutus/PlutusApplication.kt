package com.example.plutus

import android.app.Application
import kotlinx.coroutines.runBlocking


class PlutusApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        Graph.provide(this)
        Graph.pop()
    }
}

