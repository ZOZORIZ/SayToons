package com.saytoons.app.ui.screens

import android.app.TimePickerDialog
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saytoons.app.R
import java.util.Calendar

@Composable
fun CreateParentAccountScreen(
    onCreateClick: (String, String, String, List<String>, String, String) -> Unit,
    onBackClick: () -> Unit,
    onGoogleSignInClick: () -> Unit,
    isLoading: Boolean
) {
    var parentName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    val kidNames = remember { mutableStateListOf("") }
    var selectedAgeRange by remember { mutableStateOf("2-3") }
    var selectedGender by remember { mutableStateOf("Male") }
    var dailyReminderTime by remember { mutableStateOf("5:00 PM") }

    val context = LocalContext.current
    val scrollState = rememberScrollState()

    Box(modifier = Modifier.fillMaxSize()) {

        Image(
            painter = painterResource(id = R.drawable.register),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )


        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .width(290.dp)
                .height(280.dp)
                .offset(y = 55.dp)
                .verticalScroll(scrollState),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(bottom = 20.dp)
            ) {

                CustomTextField(
                    value = parentName, onValueChange = { parentName = it }, placeholder = "Name",
                    modifier = Modifier.height(50.dp).width(400.dp),
                    leadingIcon = { Icon(Icons.Default.Person, null, tint = Color(0xFFFFB74D)) }
                )
                Spacer(modifier = Modifier.height(5.dp))

                CustomTextField(
                    value = email, onValueChange = { email = it }, placeholder = "parent@email.com",
                    modifier = Modifier.height(50.dp).width(400.dp),
                    leadingIcon = { Icon(Icons.Default.Email, null, tint = Color(0xFF64B5F6)) },
                    trailingIcon = { IconButton(onClick = { passwordVisible = !passwordVisible }) { Icon(if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff, null, tint = Color(0xFF90A4AE)) } }
                )
                Spacer(modifier = Modifier.height(5.dp))

                CustomTextField(
                    value = password, onValueChange = { password = it }, placeholder = "Password",
                    modifier = Modifier.height(50.dp).width(400.dp),
                    visualTransformation = if (passwordVisible) androidx.compose.ui.text.input.VisualTransformation.None else PasswordVisualTransformation(),
                    leadingIcon = { Icon(Icons.Default.Lock, null, tint = Color(0xFF90A4AE)) }
                )
                Spacer(modifier = Modifier.height(5.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    CustomTextField(
                        value = if (kidNames.isNotEmpty()) kidNames[0] else "",
                        onValueChange = { if (kidNames.isNotEmpty()) kidNames[0] = it else kidNames.add(it) },
                        placeholder = "Alex",
                        modifier = Modifier.height(50.dp).weight(1f).width(400.dp),
                        leadingIcon = { Icon(Icons.Outlined.Face, null, tint = Color(0xFFFFB74D)) },
                        trailingIcon = {
                            Surface(shape = RoundedCornerShape(50), color = Color(0xFFF0F4F8), border = BorderStroke(1.dp, Color(0xFFC0D7E8)), modifier = Modifier.padding(end = 8.dp).clickable(interactionSource = remember { MutableInteractionSource() }, indication = null) { kidNames.add("") }) {
                                Row(modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) { Icon(Icons.Default.Add, null, modifier = Modifier.size(14.dp), tint = Color.Gray); Text("Add", fontSize = 12.sp, color = Color.Gray) }
                            }
                        }
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))


                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Person, contentDescription = null, tint = Color(0xFF64B5F6), modifier = Modifier.offset(x=15.dp).size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Child Gender", color = Color.Gray, modifier = Modifier.offset(x=15.dp), fontSize = 14.sp)
                }

                Spacer(modifier = Modifier.height(0.dp))

                Row(
                    modifier = Modifier
                        .height(50.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    GenderImageOption(
                        imageRes = R.drawable.ic_boy,
                        isSelected = selectedGender == "Male",
                        onClick = { selectedGender = "Male" }
                    )

                    GenderImageOption(
                        imageRes = R.drawable.ic_girl,
                        isSelected = selectedGender == "Female",
                        onClick = { selectedGender = "Female" }
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))


                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.ChildCare, contentDescription = null, tint = Color(0xFF64B5F6), modifier = Modifier.offset(x=15.dp).size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Child Age", color = Color.Gray, modifier = Modifier.offset(x=15.dp), fontSize = 14.sp)
                }

                Spacer(modifier = Modifier.height(5.dp))


                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .border(1.dp, Color(0xFFE0E8F0), RoundedCornerShape(50.dp))
                        .background(Color.White, RoundedCornerShape(50.dp)),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Row(
                        modifier = Modifier.padding(2.dp).background(Color(0xFFF0F4F8), RoundedCornerShape(50.dp))
                    ) {
                        AgeSegment("2-3", selectedAgeRange == "2-3") { selectedAgeRange = "2-3" }
                        AgeSegment("4-5", selectedAgeRange == "4-5") { selectedAgeRange = "4-5" }
                        AgeSegment("6-8", selectedAgeRange == "6-8") { selectedAgeRange = "6-8" }
                    }
                }

                Spacer(modifier = Modifier.height(5.dp))


                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Notifications, null, tint = Color(0xFFFFB74D), modifier = Modifier.offset(x=15.dp).size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Daily Reminder Time", color = Color.Gray, modifier = Modifier.offset(x=15.dp), fontSize = 14.sp)
                    }

                    Surface(
                        shape = RoundedCornerShape(50),
                        color = Color.White,
                        border = BorderStroke(1.dp, Color(0xFFE0E8F0)),
                        modifier = Modifier.clickable(interactionSource = remember { MutableInteractionSource() }, indication = null) {
                            val calendar = Calendar.getInstance()
                            TimePickerDialog(context, { _, h, m ->
                                val ampm = if (h >= 12) "PM" else "AM"
                                val hour = if (h % 12 == 0) 12 else h % 12
                                dailyReminderTime = String.format("%d:%02d %s", hour, m, ampm)
                            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show()
                        }
                    ) {
                        Row(modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                            Text(dailyReminderTime, fontSize = 14.sp, color = Color.Gray)
                            Icon(Icons.Default.ArrowDropDown, null, tint = Color.Gray)
                        }
                    }
                }
            }
        }


        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 20.dp)
                .width(290.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    painter = painterResource(id = R.drawable.create_acc_button),
                    contentDescription = "Create Account",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .alpha(if (isLoading) 0.5f else 1f)
                        .clickable(enabled = !isLoading, interactionSource = remember { MutableInteractionSource() }, indication = null) {
                            onCreateClick(parentName, email, password, kidNames.toList(), selectedAgeRange, selectedGender)
                        },
                    contentScale = ContentScale.Fit
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text("Continue with", fontSize = 14.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(10.dp))
                Image(
                    painter = painterResource(id = R.drawable.googe_button),
                    contentDescription = "Google Sign In",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .alpha(if (isLoading) 0.5f else 1f)
                        .clickable(
                            enabled = !isLoading,
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {

                            onGoogleSignInClick()
                        },
                    contentScale = ContentScale.Fit
                )
            }
        }


        SayToonsBackButton(onClick = onBackClick)
    }
}


@Composable
fun GenderImageOption(
    imageRes: Int,
    isSelected: Boolean,
    onClick: () -> Unit
) {

    val alpha = if (isSelected) 1f else 0.4f

    Image(
        painter = painterResource(id = imageRes),
        contentDescription = null,
        modifier = Modifier
            .size(120.dp)
            .alpha(alpha)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            ),
        contentScale = ContentScale.Fit
    )
}

@Composable
fun AgeSegment(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(50),
        color = if (isSelected) Color(0xFF64B5F6) else Color.Transparent,
        modifier = Modifier.clickable(interactionSource = remember { MutableInteractionSource() }, indication = null, onClick = onClick)
    ) {
        Text(text = text, color = if (isSelected) Color.White else Color.Gray, fontSize = 13.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp))
    }
}