package com.saytoons.app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saytoons.app.R
import com.saytoons.app.ui.screens.SayToonsBackButton
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParentLockScreen(
    onUnlockSuccess: () -> Unit,
    onUnlockFailed: () -> Unit
) {
    var userAnswer by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }


    val mathProblem = remember { generateMathProblem() }

    Box(modifier = Modifier.fillMaxSize()) {

        Image(
            painter = painterResource(id = R.drawable.locked),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )


        Column(
            modifier = Modifier
                .offset(y=100.dp)
                .align(Alignment.Center)
                .padding(horizontal = 40.dp)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            Spacer(modifier = Modifier.height(124.dp))

            Text(
                text = mathProblem.question,
                fontSize = 40.sp,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.offset(y=50.dp),
                color = Color(0xFF448AFF)
            )

            Spacer(modifier = Modifier.height(24.dp))


            Box(
                modifier = Modifier
                    .offset(y = 30.dp)
                    .width(250.dp)
                    .height(50.dp)
                    .clip(RoundedCornerShape(50))
                    .background(Color.White)
                    .border(4.dp, Color.White, RoundedCornerShape(50))
                    .border(1.dp, SoftBlueButton, RoundedCornerShape(50))
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                OutlinedTextField(
                    value = userAnswer,
                    onValueChange = {
                        userAnswer = it
                        isError = false
                    },
                    placeholder = { Text("Answer", color = Color.Gray,modifier = Modifier.offset(y = -3.dp)) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    isError = isError,
                    textStyle = TextStyle(color = SoftBlueButton, fontSize = 16.sp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        errorBorderColor = Color.Transparent,
                        cursorColor = SoftBlueButton,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        errorContainerColor = Color.Transparent
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }


            Text(
                text = "Incorrect, try again!",
                color = if (isError) Color.Red else Color.Transparent,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 44.dp)
            )

            Spacer(modifier = Modifier.height(0.dp))

            // --- SUBMIT BUTTON (Image) ---
            Image(
                painter = painterResource(id = R.drawable.ic_done),
                contentDescription = "Submit",
                modifier = Modifier
                    .height(300.dp)
                    .offset(y=-20.dp)
                    .size(180.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        if (userAnswer.trim() == mathProblem.answer.toString()) {
                            onUnlockSuccess()
                        } else {
                            isError = true

                            userAnswer = ""

                        }
                    }
            )
        }


        SayToonsBackButton(
            onClick = onUnlockFailed,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(24.dp)
        )
    }
}


data class MathQuestion(val question: String, val answer: Int)

fun generateMathProblem(): MathQuestion {
    val isMultiplication = Random.nextBoolean()

    return if (isMultiplication) {

        val a = Random.nextInt(2, 10)
        val b = Random.nextInt(2, 10)
        MathQuestion("$a x $b = ?", a * b)
    } else {

        val divisor = Random.nextInt(2, 10)
        val answer = Random.nextInt(2, 10)
        val dividend = divisor * answer
        MathQuestion("$dividend ÷ $divisor = ?", answer)
    }
}


@Composable
fun SayToonsBackButton(onClick: () -> Unit, modifier: Modifier = Modifier) {

    Box(
        modifier = modifier
            .size(60.dp)
            .shadow(8.dp, androidx.compose.foundation.shape.CircleShape, spotColor = com.saytoons.app.ui.theme.ParentModeBorder)
            .clip(androidx.compose.foundation.shape.CircleShape)
            .background(androidx.compose.ui.graphics.Brush.verticalGradient(listOf(com.saytoons.app.ui.theme.ParentModeBlueTop, com.saytoons.app.ui.theme.ParentModeBlueBottom)))
            .border(3.dp, com.saytoons.app.ui.theme.ParentModeBorder, androidx.compose.foundation.shape.CircleShape)
            .clickable { onClick() },
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