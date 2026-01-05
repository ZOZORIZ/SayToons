package com.saytoons.app.ui.screens


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChildCare
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saytoons.app.R

@Composable
fun AddKidScreen(
    onAddClick: (String, String, String) -> Unit,
    onBackClick: () -> Unit,
    isLoading: Boolean
) {
    var kidName by remember { mutableStateOf("") }
    var selectedAgeRange by remember { mutableStateOf("2-3") }
    var selectedGender by remember { mutableStateOf("Male") }

    val scrollState = rememberScrollState()

    Box(modifier = Modifier.fillMaxSize()) {

        Image(
            painter = painterResource(id = R.drawable.add_child_background), // Make sure you have this image
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )


        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .width(290.dp)
                .height(320.dp)
                .offset(y = 60.dp)
                .verticalScroll(scrollState),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(bottom = 20.dp)
            ) {


                Row(verticalAlignment = Alignment.CenterVertically) {
                    CustomTextField(
                        value = kidName,
                        onValueChange = { kidName = it },
                        placeholder = "Child's Name",
                        modifier = Modifier.height(50.dp).width(400.dp),
                        leadingIcon = { Icon(Icons.Outlined.Face, null, tint = Color(0xFFFFB74D)) }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))


                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Person, contentDescription = null, tint = Color(0xFF64B5F6), modifier = Modifier.offset(x=15.dp).size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Child Gender", color = Color.Gray, modifier = Modifier.offset(x=15.dp), fontSize = 14.sp)
                }

                Spacer(modifier = Modifier.height(0.dp))


                Row(
                    modifier = Modifier
                        .height(60.dp)
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

                Spacer(modifier = Modifier.height(16.dp))


                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.ChildCare, contentDescription = null, tint = Color(0xFF64B5F6), modifier = Modifier.offset(x=15.dp).size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Child Age", color = Color.Gray, modifier = Modifier.offset(x=15.dp), fontSize = 14.sp)
                }

                Spacer(modifier = Modifier.height(8.dp))


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
            }
        }


        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 40.dp)
                .width(290.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_done),
                contentDescription = "Add Child",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .offset(y=-90.dp)
                    .fillMaxWidth()
                    .height(60.dp)
                    .alpha(if (isLoading || kidName.isBlank()) 0.5f else 1f)
                    .clickable(
                        enabled = !isLoading && kidName.isNotBlank(),
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        onAddClick(kidName, selectedGender, selectedAgeRange)
                    }
            )
        }


        SayToonsBackButton(onClick = onBackClick)
    }
}




