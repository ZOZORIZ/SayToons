package com.saytoons.app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.saytoons.app.R
import com.saytoons.app.ui.theme.*

@Composable
fun KidHomeScreen(
    onRoutineClick: () -> Unit,
    onRewardsClick: () -> Unit,
    onBackClick: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        Image(
            painter = painterResource(id = R.drawable.routines_background),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )


        Row(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(top = 270.dp)
                .padding(bottom = 80.dp)
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Image(
                painter = painterResource(id = R.drawable.ic_house),
                contentDescription = "Routines",
                modifier = Modifier
                    .size(130.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null, // Removes the ripple
                        onClick = onRoutineClick
                    )
            )


            Image(
                painter = painterResource(id = R.drawable.ic_star_btn),
                contentDescription = "Rewards",
                modifier = Modifier
                    .size(145.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null, // Removes the ripple
                        onClick = onRewardsClick
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