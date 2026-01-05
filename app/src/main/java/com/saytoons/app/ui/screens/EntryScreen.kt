package com.saytoons.app.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saytoons.app.R
import com.saytoons.app.data.Kid

@Composable
fun EntryScreen(
    kids: List<Kid>,
    selectedKid: Kid?,
    onKidSelected: (Kid) -> Unit,
    onKidModeClick: () -> Unit,
    onParentModeClick: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        Image(
            painter = painterResource(id = R.drawable.first_screen_background),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )


        if (kids.isNotEmpty() && selectedKid != null) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 60.dp)
            ) {
                KidSelector(
                    kids = kids,
                    currentKid = selectedKid,
                    onKidSelected = onKidSelected
                )
            }
        }


        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 60.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                painter = painterResource(id = R.drawable.toony_happy),
                contentDescription = "Mascot",
                modifier = Modifier
                    .offset(y=45.dp)
                    .height(420.dp)
                    .fillMaxWidth(),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(0.dp))


            Image(
                painter = painterResource(id = R.drawable.btn_kid_mode),
                contentDescription = "Kid Mode",
                modifier = Modifier
                    .width(260.dp)
                    .height(100.dp)
                    .clickable(
                        interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() },
                        indication = null
                    ) { onKidModeClick() },
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(16.dp))


            Image(
                painter = painterResource(id = R.drawable.btn_parent_mode),
                contentDescription = "Parent Mode",
                modifier = Modifier
                    .width(260.dp)
                    .height(100.dp)
                    .clickable(
                        interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() },
                        indication = null
                    ) { onParentModeClick() },
                contentScale = ContentScale.Fit
            )
        }
    }
}


@Composable
fun KidSelector(
    kids: List<Kid>,
    currentKid: Kid,
    onKidSelected: (Kid) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }


    val avatarRes = if (currentKid.gender.equals("Female", ignoreCase = true)) {
        R.drawable.girl
    } else {
        R.drawable.kid
    }

    Box {

        Surface(
            shape = RoundedCornerShape(50),
            color = Color.White,
            border = BorderStroke(2.dp, Color(0xFF64B5F6)),
            shadowElevation = 8.dp,
            modifier = Modifier
                .height(56.dp)
                .width(200.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { expanded = true }
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 8.dp)
            ) {

                Image(
                    painter = painterResource(id = avatarRes),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .border(1.dp, Color.Gray, CircleShape)
                )

                Spacer(modifier = Modifier.width(12.dp))


                Text(
                    text = currentKid.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1D1B20),
                    modifier = Modifier.weight(1f)
                )


                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Select Kid",
                    tint = Color.Gray
                )
            }
        }


        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .width(200.dp)
                .background(Color.White)
        ) {
            kids.forEach { kid ->
                val kidAvatar = if (kid.gender.equals("Female", ignoreCase = true)) R.drawable.girl else R.drawable.kid

                DropdownMenuItem(
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                painter = painterResource(id = kidAvatar),
                                contentDescription = null,
                                modifier = Modifier.size(30.dp).clip(CircleShape)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = kid.name,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.Black // <--- Text is now Black
                            )
                        }
                    },
                    onClick = {
                        onKidSelected(kid)
                        expanded = false
                    },
                    trailingIcon = {
                        if (kid.id == currentKid.id) {
                            Icon(Icons.Default.Check, null, tint = Color(0xFF64B5F6))
                        }
                    }
                )
            }
        }
    }
}