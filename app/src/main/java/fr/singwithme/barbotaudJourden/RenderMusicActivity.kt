package fr.singwithme.barbotaudJourden

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import fr.singwithme.BarbotaudJourden.ui.theme.SingWithMeTheme
import androidx.compose.ui.unit.dp


var durationList = listOf<Int>(1000, 4000, 2000, 2000 ,3000, 5000);
var words = listOf<String>(" ", "Savinien Barbotaud", "François Jourden", " ", "Ewen Piepers", "Noan Perrot");

class RenderMusicActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SingWithMeTheme {
                Scaffold(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).padding(32.dp)) { innerPadding ->
                    //KaraokeTextAnimate(durationList, words)
                    KaraokeTextAnimate(durationList, words)
                }
            }
        }
    }

    @Composable
    fun Int.pxToDp() = with(LocalDensity.current) { this@pxToDp.toDp() }

    @Composable
    fun KaraokeSimpleText(text: String, progress: Float) {
        var redTextWidth by remember { mutableStateOf(0) }
        Box(
            Modifier.onGloballyPositioned { coordinates ->
                // This will be the size of the Column.
                redTextWidth = coordinates.size.width
            }
        ) {
            Text(text, color = Color.Red);
            Text(text, modifier = Modifier.width(redTextWidth.pxToDp()*progress), softWrap = false);
        }
        //Log.d("textWidth", (redTextWidth.pxToDp()*progress).toString())
    }

    @Composable
    fun KaraokeText(list: List<String>, current: Int, progress: Float) {
        val word = list[current];
        Log.d("textWidth", word)
        KaraokeSimpleText(word, progress)
    }

    @Composable
    fun KaraokeTextAnimate(duration: List<Int>, list: List<String>) {
        var counter by remember { mutableStateOf(0) }
        var karaokeAnimation = remember { Animatable(0f) }
        val listSize = list.size
        val effect:Unit = LaunchedEffect(Unit) {
            karaokeAnimation.animateTo(1f, tween(duration[0], easing = LinearEasing))
        }
        LaunchedEffect(Unit) {
            while (counter < listSize-1) {
                //delay(duration.toLong())
                counter++
                //Reset all animation
                karaokeAnimation.snapTo(0f)
                karaokeAnimation.animateTo(1f, tween(duration[counter], easing = LinearEasing))
            }
        }
        KaraokeText(list, counter, karaokeAnimation.value)
    }
}


