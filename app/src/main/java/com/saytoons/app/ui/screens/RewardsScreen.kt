package com.saytoons.app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saytoons.app.R
import com.saytoons.app.ui.theme.ParentModeBlueBottom
import com.saytoons.app.ui.theme.ParentModeBlueTop
import com.saytoons.app.ui.theme.ParentModeBorder

@OptIn(ExperimentalTextApi::class)
@Composable
fun RewardsScreen(
    totalStars: Int,
    onBackClick: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {

        Image(
            painter = painterResource(id = R.drawable.rewards_background),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )


        Image(
            painter = painterResource(id = R.drawable.ic_star),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 150.dp, start = 40.dp)
                .size(60.dp)
                .offset(x = (-20).dp, y = 20.dp)
        )
        Image(
            painter = painterResource(id = R.drawable.ic_star),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 150.dp, end = 40.dp)
                .size(80.dp)
                .offset(x = 10.dp, y = (-20).dp)
        )
        Image(
            painter = painterResource(id = R.drawable.ic_star),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 200.dp, end = 60.dp)
                .size(40.dp)
        )


        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                painter = painterResource(id = R.drawable.ic_star),
                contentDescription = null,
                modifier = Modifier
                    .offset(y=-35.dp)
                    .size(120.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))


            val goldenGradientBrush = Brush.verticalGradient(
                colors = listOf(
                    Color(0xFFFFF176),
                    Color(0xFFFFB300),
                    Color(0xFFFF6F00)
                )
            )


            Text(
                text = "$totalStars",
                fontSize = 200.sp,
                fontWeight = FontWeight.ExtraBold,

                style = TextStyle(
                    brush = goldenGradientBrush,
                    shadow = androidx.compose.ui.graphics.Shadow(
                        color = Color.Black.copy(alpha = 0.4f),
                        blurRadius = 12f,
                        offset = androidx.compose.ui.geometry.Offset(4f, 4f)
                    )
                )
            )
        }


        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(24.dp)
                .size(60.dp)
                .shadow(8.dp, CircleShape, spotColor = ParentModeBorder)
                .clip(CircleShape)
                .background(
                    brush = Brush.verticalGradient(listOf(ParentModeBlueTop, ParentModeBlueBottom))
                )
                .border(3.dp, ParentModeBorder, CircleShape)
                .clickable { onBackClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = android.R.drawable.ic_menu_revert),
                contentDescription = "Back",
                tint = Color.White,
                modifier = Modifier.size(30.dp)
            )
        }
    }
}