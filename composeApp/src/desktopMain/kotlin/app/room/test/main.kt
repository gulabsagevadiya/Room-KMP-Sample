package app.room.test

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import app.room.test.di.initKoin

fun main() = application {
    initKoin()
    Window(
        onCloseRequest = ::exitApplication,
        title = "RoomSample",
    ) {
        App()
    }
}