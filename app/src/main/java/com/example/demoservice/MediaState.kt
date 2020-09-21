package com.example.demoservice

annotation class MediaState {
    companion object {
        var IDLE = 0
        var PLAYING = 1
        var PAUSED = 2
        var STOPPED = 3
    }
}
