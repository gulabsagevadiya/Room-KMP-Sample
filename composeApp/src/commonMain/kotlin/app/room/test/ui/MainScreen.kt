package app.room.test.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.room.test.repository.SampleDataRepo
import app.room.test.utils.addOrRemoveMonth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import org.jetbrains.compose.resources.painterResource
import roomsample.composeapp.generated.resources.Res
import roomsample.composeapp.generated.resources.ic_back

@Composable
fun MainScreen(uiState: MainScreenUiState, interActor: MainScreenInterActor) {
  Column(modifier = Modifier.fillMaxSize().background(color = Color.LightGray)) {
    Row(
      modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).padding(top = 12.dp)
        .background(color = Color.Gray, shape = RoundedCornerShape(8.dp)),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = "All Data",
        color = if (uiState.showAllData) MaterialTheme.colors.primary else Color.LightGray,
        textAlign = TextAlign.Center,
        modifier = Modifier.weight(1f).clickable { interActor.showAllData(true) })
      Text(
        text = "Filtered Data",
        color = if (uiState.showAllData) Color.LightGray else MaterialTheme.colors.primary,
        textAlign = TextAlign.Center,
        modifier = Modifier.weight(1f).clickable { interActor.showAllData(false) })
    }
    if (uiState.showAllData) {
      Text("All Dates", modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp), textAlign = TextAlign.Center)
      Divider(modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp))
      LazyVerticalGrid(columns = GridCells.Fixed(3)) {
        items(uiState.allData) {
          Text(it.toString())
        }
      }
    } else {
      Row(
        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text("Filtered Dates", modifier = Modifier)
        Spacer(modifier = Modifier.weight(1f))
        Icon(
          painter = painterResource(Res.drawable.ic_back),
          contentDescription = null,
          modifier = Modifier.clickable { interActor.changeMonth(-1) })
        Text(
          text = uiState.selectedMonth,
          modifier = Modifier.clickable { interActor.changeMonth(1) })
        Icon(
          painter = painterResource(Res.drawable.ic_back),
          contentDescription = null,
          modifier = Modifier.rotate(180f).clickable { interActor.changeMonth(1) })
      }

      Divider(modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp))

      LazyVerticalGrid(columns = GridCells.Fixed(3)) {
        items(uiState.filteredData) {
          Text(it.toString())
        }
      }
    }
  }
}

data class MainScreenUiState(
  val showAllData: Boolean = true,
  val selectedMonth: String = "08-2024",
  val filteredData: List<LocalDate> = emptyList(),
  val allData: List<LocalDate> = emptyList(),
)

interface MainScreenInterActor {
  fun showAllData(boolean: Boolean)
  fun changeMonth(int: Int)
}

@OptIn(ExperimentalCoroutinesApi::class)
class MainScreenViewModel(sampleDataRepo: SampleDataRepo) : ViewModel() {

  private val _uiState = MutableStateFlow(MainScreenUiState())
  val uiState = _uiState.asStateFlow()

  val interActor = object : MainScreenInterActor {
    override fun showAllData(boolean: Boolean) {
      _uiState.update { state ->
        state.copy(showAllData = boolean)
      }
    }

    override fun changeMonth(int: Int) {
      _uiState.update { state ->
        state.copy(selectedMonth = state.selectedMonth.addOrRemoveMonth(int))
      }
    }
  }

  init {
    viewModelScope.launch {
      launch {
        sampleDataRepo.getAllDates().collect {
          println("AllDates: $it")
          _uiState.update { state ->
            state.copy(allData = it)
          }
        }
      }
      launch {
        uiState.map { it.selectedMonth }.distinctUntilChanged().flatMapLatest {
          sampleDataRepo.getMonthWiseDates(it)
        }.collect {
          println("FilteredDates: $it")
          _uiState.update { state ->
            state.copy(filteredData = it)
          }
        }
      }
    }
  }

}