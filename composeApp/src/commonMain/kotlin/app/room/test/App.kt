package app.room.test

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.room.test.ui.MainScreen
import app.room.test.ui.MainScreenViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinContext
import org.koin.compose.koinInject

@Composable
@Preview
fun App() {
    KoinContext {
        MaterialTheme {
            val viewModel = koinInject<MainScreenViewModel>()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            MainScreen(uiState, viewModel.interActor)
        }
    }
}