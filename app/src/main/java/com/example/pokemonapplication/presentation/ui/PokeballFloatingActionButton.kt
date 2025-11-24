package com.example.pokemonapplication.presentation.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pokemonapplication.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokeballFloatingActionButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    animationDurationMillis: Int = 800,
    maxScale: Float = 1.25f,
    iconSizeDp: Int = 72
) {
    val scope = rememberCoroutineScope()
    val progress = remember { Animatable(0f) }
    val iconSize = iconSizeDp.dp
    val tooltipState = rememberTooltipState()

    TooltipBox(
        state = tooltipState,
        positionProvider = TooltipDefaults.rememberTooltipPositionProvider(TooltipAnchorPosition.Above),
        tooltip = {
            PlainTooltip {
                Text(text = stringResource(id = R.string.favorites))
            }
        },
        modifier = modifier
    ) {
        FloatingActionButton(
            onClick = {
                onClick()
                scope.launch {
                    progress.animateTo(
                        1f,
                        animationSpec = tween(durationMillis = animationDurationMillis)
                    )
                    progress.animateTo(
                        0f,
                        animationSpec = tween(durationMillis = animationDurationMillis / 3)
                    )
                }
            }, modifier = Modifier.size(iconSize),
            elevation = FloatingActionButtonDefaults.elevation(),
            shape = CircleShape
        ) {
            val p = progress.value
            val scale = 1f + (maxScale - 1f) * p
            val rotation = 360f * p

            Image(
                painter = painterResource(id = R.drawable.ic_pokeball),
                contentDescription = stringResource(id = R.string.favorites),
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                        rotationZ = rotation
                    }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PokeballFloatingActionButtonPreview() {
    PokeballFloatingActionButton(onClick = {})
}
