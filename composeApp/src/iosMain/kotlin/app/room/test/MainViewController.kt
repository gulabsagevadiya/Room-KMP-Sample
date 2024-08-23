package app.room.test

import androidx.compose.ui.window.ComposeUIViewController
import app.room.test.di.initKoin

fun MainViewController() = ComposeUIViewController(configure = { initKoin() }) {
  App()
}