package fr.singwithme.barbotaudJourden

import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFontFamilyResolver
import androidx.compose.ui.text.Paragraph
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.isFinite
import androidx.compose.ui.unit.isSpecified
import androidx.compose.ui.unit.isUnspecified
import androidx.compose.ui.unit.sp
import fr.singwithme.barbotaudJourden.ui.theme.SingWithMeTheme
import kotlin.math.absoluteValue
import kotlin.math.ceil


var durationList = listOf<Int>(1000, 4000, 2000, 2000 ,3000, 5000);
var words = listOf<String>(" ", "Savinien Barbotaud", "François Jourden", " ", "Ewen Piepers", "Noan Perrot");

class RenderMusicActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SingWithMeTheme {
                Scaffold(modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(32.dp)) { innerPadding ->
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
        val configuration = LocalConfiguration.current
        val screenWidth = configuration.screenWidthDp.dp
        val screenHeight = configuration.screenHeightDp.dp
        var redTextWidth by remember { mutableStateOf(0) }
        Box(
            Modifier.onGloballyPositioned { coordinates ->
                // This will be the size of the Column.
                redTextWidth = coordinates.size.width
            }.height(screenHeight).padding(20.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text,
                color = Color.Black,
                fontSize = 20.sp,
            );
            Text(
                text,
                modifier = Modifier.width(redTextWidth.pxToDp()*progress),
                softWrap = false,
                fontSize = 20.sp,
                color = Color.Red
            );
        }
        //Log.d("textWidth", (redTextWidth.pxToDp()*progress).toString())
    }

    @Composable
    fun KaraokeText(list: List<String>, current: Int, progress: Float) {
        val word = list[current];
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

    @Preview
    @Composable
    fun PreviewText() {
        KaraokeSimpleText("Hallo world", 0.6f)
    }




    @Composable
    fun AutoSizeText(
        text: String,
        modifier: Modifier = Modifier,
        acceptableError: Dp = 5.dp,
        maxFontSize: TextUnit = TextUnit.Unspecified,
        color: Color = Color.Unspecified,
        fontStyle: FontStyle? = null,
        fontWeight: FontWeight? = null,
        fontFamily: FontFamily? = null,
        letterSpacing: TextUnit = TextUnit.Unspecified,
        textDecoration: TextDecoration? = null,
        textAlign: TextAlign? = null,
        contentAlignment: Alignment? = null,
        lineHeight: TextUnit = TextUnit.Unspecified,
        maxLines: Int = Int.MAX_VALUE,
        onTextLayout: (TextLayoutResult) -> Unit = {},
        style: TextStyle = LocalTextStyle.current
    ) {
        val alignment: Alignment = contentAlignment ?: when (textAlign) {
            TextAlign.Left -> Alignment.TopStart
            TextAlign.Right -> Alignment.TopEnd
            TextAlign.Center -> Alignment.Center
            TextAlign.Justify -> Alignment.TopCenter
            TextAlign.Start -> Alignment.TopStart
            TextAlign.End -> Alignment.TopEnd
            else -> Alignment.TopStart
        }
        BoxWithConstraints(modifier = modifier, contentAlignment = alignment) {
            var shrunkFontSize = if (maxFontSize.isSpecified) maxFontSize else 100.sp

            val calculateIntrinsics = @Composable {
                val mergedStyle = style.merge(
                    textAlign?.let {
                        TextStyle(
                            color = color,
                            fontSize = shrunkFontSize,
                            fontWeight = fontWeight,
                            textAlign = it,
                            lineHeight = lineHeight,
                            fontFamily = fontFamily,
                            textDecoration = textDecoration,
                            fontStyle = fontStyle,
                            letterSpacing = letterSpacing
                        )
                    }
                )
                Paragraph(
                    text = text,
                    style = mergedStyle,
                    constraints = Constraints(maxWidth = ceil(LocalDensity.current.run { maxWidth.toPx() }).toInt()),
                    density = LocalDensity.current,
                    fontFamilyResolver = LocalFontFamilyResolver.current,
                    spanStyles = listOf(),
                    placeholders = listOf(),
                    maxLines = maxLines,
                    ellipsis = false
                )
            }

            var intrinsics = calculateIntrinsics()

            val targetWidth = maxWidth - acceptableError / 2f

            check(targetWidth.isFinite || maxFontSize.isSpecified) { "maxFontSize must be specified if the target with isn't finite!" }

            with(LocalDensity.current) {
                // this loop will attempt to quickly find the correct size font by scaling it by the error
                // it only runs if the max font size isn't specified or the font must be smaller
                // minIntrinsicWidth is "The width for text if all soft wrap opportunities were taken."
                if (maxFontSize.isUnspecified || targetWidth < intrinsics.minIntrinsicWidth.toDp())
                    while ((targetWidth - intrinsics.minIntrinsicWidth.toDp()).toPx().absoluteValue.toDp() > acceptableError / 2f) {
                        shrunkFontSize *= targetWidth.toPx() / intrinsics.minIntrinsicWidth
                        intrinsics = calculateIntrinsics()
                    }
                // checks if the text fits in the bounds and scales it by 90% until it does
                while (intrinsics.didExceedMaxLines || maxHeight < intrinsics.height.toDp() || maxWidth < intrinsics.minIntrinsicWidth.toDp()) {
                    shrunkFontSize *= 0.9f
                    intrinsics = calculateIntrinsics()
                }
            }

            if (maxFontSize.isSpecified && shrunkFontSize > maxFontSize)
                shrunkFontSize = maxFontSize

            Text(
                text = text,
                color = color,
                fontSize = shrunkFontSize,
                fontStyle = fontStyle,
                fontWeight = fontWeight,
                fontFamily = fontFamily,
                letterSpacing = letterSpacing,
                textDecoration = textDecoration,
                textAlign = textAlign,
                lineHeight = lineHeight,
                onTextLayout = onTextLayout,
                maxLines = maxLines,
                style = style
            )
        }
    }

}


