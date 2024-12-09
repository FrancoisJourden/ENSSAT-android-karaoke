package fr.singwithme.barbotaudJourden

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import fr.singwithme.barbotaudJourden.model.MusicModel
import fr.singwithme.barbotaudJourden.ui.theme.SingWithMeTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse

sealed class MusicDetailsState{
    data class Success(val music: MusicModel): MusicDetailsState()
    object Loading: MusicDetailsState()
    data class Error(val message: String): MusicDetailsState()
}

class RenderMusicActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val path = intent.getStringExtra("path")!!
            SingWithMeTheme {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                        .padding(32.dp)
                ) { innerPadding ->
                    Body(Modifier.padding(innerPadding), path)
                }
            }
        }
    }

    fun getMusic(path: String, onResult: (MusicDetailsState) -> Unit){
        lifecycleScope.launch{
            try{
                val response = withContext(Dispatchers.IO){
                    MusicDetailApi.retrofitService.getMusic(path).awaitResponse()
                }
                if(!response.isSuccessful){
                    onResult(MusicDetailsState.Error("Error: ${response.code()}"))
                    return@launch
                }
                val music = response.body()!!
                onResult(MusicDetailsState.Success(music))
            }catch (e: Exception){
                onResult(MusicDetailsState.Error(e.message.toString()))
            }
        }
    }
}

@Composable
fun Int.pxToDp() = with(LocalDensity.current) { this@pxToDp.toDp() }

@Composable
fun Body(modifier: Modifier = Modifier, path: String){
    var musicDetailsState by remember { mutableStateOf<MusicDetailsState>(MusicDetailsState.Loading) }
    val activity = LocalContext.current as RenderMusicActivity

    LaunchedEffect(Unit) {
        activity.getMusic(path) {
            musicDetailsState = it
        }
    }

    when (val state = musicDetailsState){
        is MusicDetailsState.Success -> {
            val music = state.music
            KaraokeTextAnimate(
                music.lyrics.map { it.duration.inWholeMilliseconds.toInt() },
                music.lyrics.map { it.text }
            )
        }
        is MusicDetailsState.Loading -> {
            CircularProgressIndicator()
        }
        is MusicDetailsState.Error -> {
            Text("Error: ${state.message}")
        }
    }
}

@Composable
fun KaraokeSimpleText(text: String, progress: Float) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp
    var redTextWidth by remember { mutableIntStateOf(0) }
    Box(
        Modifier
            .onGloballyPositioned { coordinates ->
                // This will be the size of the Column.
                redTextWidth = coordinates.size.width
            }
            .height(screenHeight)
            .padding(20.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text,
            fontSize = 20.sp,
        );
        Text(
            text,
            modifier = Modifier.width(redTextWidth.pxToDp() * progress),
            softWrap = false,
            fontSize = 20.sp,
            color = Color.Red
        );
    }
}

@Composable
fun KaraokeText(list: List<String>, current: Int, progress: Float) {
    val word = list[current];
    KaraokeSimpleText(word, progress)
}

@Composable
fun KaraokeTextAnimate(duration: List<Int>, list: List<String>) {
    var counter by remember { mutableIntStateOf(0) }
    var karaokeAnimation = remember { Animatable(0f) }
    val listSize = list.size
    val effect: Unit = LaunchedEffect(Unit) {
        karaokeAnimation.animateTo(1f, tween(duration[0], easing = LinearEasing))
    }
    LaunchedEffect(Unit) {
        while (counter < listSize - 1) {
            //delay(duration.toLong())
            counter++
            //Reset all animation
            karaokeAnimation.snapTo(0f)
            karaokeAnimation.animateTo(1f, tween(duration[counter], easing = LinearEasing))
        }
    }
    KaraokeText(list, counter, karaokeAnimation.value)
}

@Preview
@Composable
fun PreviewText() {
    KaraokeSimpleText("Hallo world", 0.6f)
}


