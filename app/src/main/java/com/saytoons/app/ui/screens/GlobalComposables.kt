package com.saytoons.app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saytoons.app.R
import com.saytoons.app.ui.theme.TextDarkBlue

//Transition
@Composable
fun ThinkingScreenOverlay() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.toony_think),
                contentDescription = "Toony is Thinking",
                modifier = Modifier.size(250.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Hold on, Toony is Thinking...",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = TextDarkBlue
            )
        }
    }
}