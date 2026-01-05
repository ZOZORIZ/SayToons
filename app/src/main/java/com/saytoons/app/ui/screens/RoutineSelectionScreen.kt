package com.saytoons.app.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saytoons.app.R
import com.saytoons.app.data.RoutineRepository
import com.saytoons.app.ui.theme.*
import com.saytoons.app.data.Kid


data class Routine(
    val title: String,
    val isLocked: Boolean,
    val mainColor: Color,
    val lightColor: Color,
    val darkColor: Color,
    val imageRes: Int,
    val maxStars: Int,
    val earnedStars: Int
)

@Composable
fun RoutineSelectionScreen(
    kid: Kid?,
    onRoutineSelected: (String) -> Unit,
    onBackClick: () -> Unit
) {

    fun isLocked(routineName: String): Boolean {
        val unlocked = kid?.unlockedRoutines ?: listOf("morning")

        return !unlocked.any { it.equals(routineName, ignoreCase = true) }
    }


    //remove
    val morningScore = kid?.routineScores?.get("morning") ?: 0
    val bedtimeScore = kid?.routineScores?.get("bedtime") ?: 0
    val mealScore = kid?.routineScores?.get("meal") ?: 0
    val schoolScore = kid?.routineScores?.get("school") ?: 0
    val marriageScore = kid?.routineScores?.get("marriage") ?: 0
    val hospitalScore = kid?.routineScores?.get("hospital") ?: 0
    val transportScore = kid?.routineScores?.get("transport") ?: 0
    val animalsScore = kid?.routineScores?.get("animals") ?: 0
    val birdsScore = kid?.routineScores?.get("birds") ?: 0
    val shapesScore = kid?.routineScores?.get("shapes") ?: 0



    val morningRoutine = Routine(
        title = "Morning Routine",
        isLocked = false, // Always unlocked
        mainColor = MorningYellow,
        lightColor = Color(0xFFFFF59D),
        darkColor = Color(0xFFF57F17),
        imageRes = R.drawable.ic_morning,
        maxStars = 8,
        earnedStars = kid?.routineScores?.get("morning") ?: 0
    )

    val bedtimeRoutine = Routine(
        title = "Bedtime Routine",
        isLocked = isLocked("Bedtime Routine"), // <--- Checks DB
        mainColor = BedtimePurple,
        lightColor = Color(0xFFE1BEE7),
        darkColor = Color(0xFF4A148C),
        imageRes = R.drawable.ic_bedtime,
        maxStars = 8,
        earnedStars = kid?.routineScores?.get("bedtime") ?: 0
    )

    val mealtimeRoutine = Routine(
        title = "Mealtime Routine",
        isLocked = isLocked("Mealtime Routine"),
        mainColor = MealtimeGray,
        lightColor = Color(0xFFCFD8DC),
        darkColor = Color(0xFF37474F),
        imageRes = R.drawable.ic_meal,
        maxStars = 6,
        earnedStars = kid?.routineScores?.get("meal") ?: 0
    )


    val schoolRoutine = Routine(
        title = "School Routine",
        isLocked = isLocked("School Routine"),
        mainColor = Color(0xFF42A5F5),
        lightColor = Color(0xFFBBDEFB),
        darkColor = Color(0xFF1565C0),
        imageRes = R.drawable.ic_school,
        maxStars = 8,
        earnedStars = kid?.routineScores?.get("school") ?: 0
    )

    val transportRoutine = Routine(
        title = "Transportation Routine",
        isLocked = isLocked("Transportation Routine"),
        mainColor = Color(0xFFFFCA28),
        lightColor = Color(0xFFFFF8E1),
        darkColor = Color(0xFFFF6F00),
        imageRes = R.drawable.ic_transport,
        maxStars = 8,
        earnedStars = kid?.routineScores?.get("transport") ?: 0
    )

    val animalsRoutine = Routine(
        title = "Animals Routine",
        isLocked = isLocked("Animals Routine"),
        mainColor = Color(0xFF66BB6A),
        lightColor = Color(0xFFC8E6C9),
        darkColor = Color(0xFF2E7D32),
        imageRes = R.drawable.ic_animals,
        maxStars = 8,
        earnedStars = kid?.routineScores?.get("animals") ?: 0
    )

    val birdsRoutine = Routine(
        title = "Birds Routine",
        isLocked = isLocked("Birds Routine"),
        mainColor = Color(0xFF26C6DA),
        lightColor = Color(0xFFB2EBF2),
        darkColor = Color(0xFF00838F),
        imageRes = R.drawable.ic_birds,
        maxStars = 8,
        earnedStars = kid?.routineScores?.get("birds") ?: 0
    )

    val shapesRoutine = Routine(
        title = "Shapes Routine",
        isLocked = isLocked("Shapes Routine"),
        mainColor = Color(0xFFAB47BC),
        lightColor = Color(0xFFE1BEE7),
        darkColor = Color(0xFF6A1B9A),
        imageRes = R.drawable.ic_shapes,
        maxStars = 8,
        earnedStars = kid?.routineScores?.get("shapes") ?: 0
    )

    val hospitalRoutine = Routine(
        title = "Hospital Routine",
        isLocked = isLocked("Hospital Routine"),
        mainColor = Color(0xFFEF5350),
        lightColor = Color(0xFFFFCDD2),
        darkColor = Color(0xFFC62828),
        imageRes = R.drawable.ic_hospital,
        maxStars = 8,
        earnedStars = kid?.routineScores?.get("hospital") ?: 0
    )

    val marriageRoutine = Routine(
        title = "Marriage Routine",
        isLocked = isLocked("Marriage Routine"),
        mainColor = Color(0xFFEC407A),
        lightColor = Color(0xFFF8BBD0),
        darkColor = Color(0xFFAD1457),
        imageRes = R.drawable.ic_mar,
        maxStars = 8,
        earnedStars = kid?.routineScores?.get("marriage") ?: 0
    )

    val routines = listOf(
        morningRoutine,
        bedtimeRoutine,
        mealtimeRoutine,
        schoolRoutine,
        transportRoutine,
        animalsRoutine,
        birdsRoutine,
        shapesRoutine,
        hospitalRoutine,
        marriageRoutine
    )

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        Image(
            painter = painterResource(id = R.drawable.routine_selection_background),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )


        Box(
            modifier = Modifier
                .padding(top=150.dp)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(420.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                items(routines) { routine ->

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        GradientRoutineCard(
                            routine = routine,
                            onStartClick = { if(!routine.isLocked) onRoutineSelected(routine.title) }
                        )

                        Spacer(modifier = Modifier.height(16.dp))


                        StarScoreDisplay(
                            earned = routine.earnedStars,
                            max = routine.maxStars,
                            isLocked = routine.isLocked
                        )
                    }
                }
            }
        }


        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(24.dp)
                .size(60.dp)
                .shadow(8.dp, CircleShape, spotColor = ParentModeBorder)
                .clip(CircleShape)
                .background(Brush.verticalGradient(listOf(ParentModeBlueTop, ParentModeBlueBottom)))
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

@Composable
fun StarScoreDisplay(earned: Int, max: Int, isLocked: Boolean) {
    Surface(
        color = Color.Black.copy(alpha = 0.6f),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.3f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isLocked) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    tint = Color.LightGray,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Locked",
                    color = Color.LightGray,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            } else {
                Icon(
                    painter = painterResource(id = R.drawable.ic_star_large),
                    contentDescription = null,
                    tint = Color(0xFFFFD700), // Gold
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "$earned / $max",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
        }
    }
}

@Composable
fun GradientRoutineCard(routine: Routine, onStartClick: () -> Unit) {
    val innerShape = RoundedCornerShape(16.dp)

    Box(
        modifier = Modifier
            .width(200.dp)
            .height(340.dp)
            .shadow(elevation = 10.dp, shape = RoundedCornerShape(24.dp), spotColor = routine.mainColor.copy(alpha = 0.5f))
            .clip(RoundedCornerShape(24.dp))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color.White, routine.lightColor)
                )
            )
            .border(width = 5.dp, color = routine.mainColor, shape = RoundedCornerShape(24.dp))
            .padding(10.dp)
            .border(width = 2.dp, color = routine.mainColor, shape = innerShape)
            .padding(2.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Image Area
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(innerShape),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = routine.imageRes),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                if (routine.isLocked) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Gray.copy(alpha = 0.6f))
                            .clip(innerShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Lock, "Locked", Modifier.size(50.dp), tint = Color.White)
                    }
                }
            }
            Spacer(modifier = Modifier.height(0.dp))
            Text(
                text = routine.title,
                textAlign = TextAlign.Center,
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold,
                color = routine.darkColor,
                lineHeight = 30.sp,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))


            if (!routine.isLocked) {

                val isDone = routine.earnedStars > 0

                Button(
                    onClick = onStartClick,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    contentPadding = PaddingValues(),
                    shape = RoundedCornerShape(50),
                    modifier = Modifier
                        .padding(start = 12.dp, end = 12.dp, bottom = 12.dp)
                        .fillMaxWidth()
                        .height(48.dp)
                        .background(
                            brush = Brush.horizontalGradient(
                                listOf(Color(0xFF43A047), Color(0xFF66BB6A))
                            ),
                            shape = RoundedCornerShape(50)
                        )
                ) {
                    Text(
                        if (isDone) "Redo" else "Start",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
            } else {
                Box(
                    modifier = Modifier
                        .padding(bottom = 12.dp)
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Color.LightGray),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Star, null, tint = Color.White)
                }
            }
        }
    }
}