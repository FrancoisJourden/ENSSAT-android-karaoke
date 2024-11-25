package fr.singwithme.barbotaudJourden

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import fr.singwithme.barbotaudJourden.model.MusicListModel
import fr.singwithme.barbotaudJourden.ui.theme.SingWithMeTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse

sealed interface MusicListState {
    data class Success(val musics: List<MusicListModel>) : MusicListState
    object Loading : MusicListState
    data class Error(val message: String) : MusicListState
}

class MusicListActivity : ComponentActivity() {

    fun getMusics(onResult: (MusicListState) -> Unit) {
        lifecycleScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    MusicApi.retrofitService.getMusics().awaitResponse()
                }
                if (!response.isSuccessful) {
                    onResult(MusicListState.Error("Error: ${response.code()}"))
                    return@launch
                }
                val musics = response.body() ?: emptyList()
                onResult(MusicListState.Success(musics))
            } catch (e: Exception) {
                onResult(MusicListState.Error((e.message + e.printStackTrace())))
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SingWithMeTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            title = { Text("SingWithMe") }
                        )
                    }
                ) { innerPadding ->
                    MusicList(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun MusicList(modifier: Modifier = Modifier) {
    val musicListState = remember { mutableStateOf<MusicListState>(MusicListState.Loading) }
    val activity = LocalContext.current as MusicListActivity

    LaunchedEffect(Unit) {
        activity.getMusics { state ->
            musicListState.value = state
        }
    }

    when (val state = musicListState.value) {
        is MusicListState.Success -> {
            val musics = state.musics
            LazyColumn(modifier = modifier) {
                items(musics.size) { i ->
                    MusicListItem(musics[i])
                    if (i < musics.size - 1)
                        HorizontalDivider()

                }
            }
        }

        is MusicListState.Loading -> {
            CircularProgressIndicator()
        }

        is MusicListState.Error -> {
            Text(state.message)
        }
    }
}

@Composable
fun MusicListItem(music: MusicListModel, modifier: Modifier = Modifier) {
    ListItem(
        modifier = modifier
            .clickable {
                if (music.locked == true) return@clickable
                // TODO: open music
            }
            .animateContentSize(),
        headlineContent = { Text(music.name) },
        supportingContent = { Text(music.artist) },
        trailingContent = {
            if (music.locked == true) Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = "Locked"
            )
        }
    )
}

@Preview
@Composable
fun PreviewMusicListItem() {
    MusicListItem(
        music = MusicListModel(
            name = "Music",
            artist = "Artist",
            locked = false,
            path = null
        )
    )
}

@Preview
@Composable
fun PreviewMusicListItemLocked() {
    MusicListItem(
        music = MusicListModel(
            name = "Music",
            artist = "Artist",
            locked = true,
            path = null
        )
    )
}