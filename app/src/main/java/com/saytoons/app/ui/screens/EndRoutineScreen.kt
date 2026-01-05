package com.saytoons.app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saytoons.app.R
import org.intellij.lang.annotations.JdkConstants

@Composable
fun EndRoutineScreen(
    onHomeClick: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {

        Image(
            painter = painterResource(id = R.drawable.end_success),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Text(
            text = "Great job!",
            color = SoftBlueButton,
            modifier = Modifier
                .padding(top=240.dp)
                .align(Alignment.TopCenter),

            fontSize = 40.sp,
            fontWeight = FontWeight.ExtraBold
        )


        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 60.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.home),
                contentDescription = "Home",
                modifier = Modifier
                    .size(170.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onHomeClick
                    )
            )
        }
    }
}