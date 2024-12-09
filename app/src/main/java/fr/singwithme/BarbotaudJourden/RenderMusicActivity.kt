package fr.singwithme.BarbotaudJourden

import android.os.Bundle
import android.util.Log
import android.widget.TextView
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import fr.singwithme.BarbotaudJourden.ui.theme.SingWithMeTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


var durationList = listOf<Int>(1000, 4000, 2000, 2000 ,3000, 5000);
var words = listOf<String>(" ", "Savinien Barbotaud", "François Jourden", " ", "Ewen Piepers", "Noan Perrot");

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val renderMusic = RenderMusic();
        enableEdgeToEdge()
        setContent {
            SingWithMeTheme {
                Scaffold(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).padding(32.dp)) { innerPadding ->
                    //KaraokeTextAnimate(durationList, words)
                    renderMusic.KaraokeTextAnimate(durationList, words)
                }
            }
        }
    }
}


