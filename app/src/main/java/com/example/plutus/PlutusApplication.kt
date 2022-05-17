package com.example.plutus

import android.app.Application


class PlutusApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        Graph.provide(this)
    }
}

