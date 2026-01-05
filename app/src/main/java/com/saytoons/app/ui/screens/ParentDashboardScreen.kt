package com.saytoons.app.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons

import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.saytoons.app.ParentDashboardViewModel
import com.saytoons.app.R
import com.saytoons.app.data.Kid

val SoftBlueButton = Color(0xFF64B5F6)
val TextBlack = Color(0xFF1D1B20)
val TextGray = Color(0xFF9E9E9E)
val IconGreen = Color(0xFF4CAF50)
val StarYellow = Color(0xFFFFD700)

data class RoutineConfig(
    val id: String,
    val title: String,
    val iconRes: Int
)

@Composable
fun ParentDashboardScreen(
    viewModel: ParentDashboardViewModel = viewModel(),
    onAddKidClick: () -> Unit,
    onBackClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    val kids by viewModel.kids.collectAsState()
    val scrollState = rememberScrollState()


    var selectedKid by remember { mutableStateOf<Kid?>(null) }

    LaunchedEffect(kids) {

        if (selectedKid == null && kids.isNotEmpty()) {
            selectedKid = kids[0]
        }
    }


    val routineList = listOf(
        RoutineConfig("brushing", "Brushing Teeth", R.drawable.ic_toothbrush),
        RoutineConfig("bedtime", "Bedtime Routine", R.drawable.ic_bed),
        RoutineConfig("dressed", "Get Dressed", R.drawable.ic_hanger),
        RoutineConfig("homework", "Homework", R.drawable.ic_book),
        RoutineConfig("school", "School Routine", R.drawable.ic_school_icon),
        RoutineConfig("marriage", "Marriage Function", R.drawable.marriage_icon),
        RoutineConfig("hospital", "Hospital Visit", R.drawable.hospital_icon),
        RoutineConfig("transport", "Transportation", R.drawable.transportation_icon),
        RoutineConfig("animals", "Learn Animals", R.drawable.animal_icon),
        RoutineConfig("birds", "Learn Birds", R.drawable.bird_icon),
        RoutineConfig("shapes", "Learn Shapes", R.drawable.shapes_icon)
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.dashboard),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .padding(top = 240.dp)
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {

            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                items(kids) { kid ->
                    KidCard(
                        kid = kid,
                        routines = routineList,
                        isSelected = (kid.id == selectedKid?.id),
                        onClick = { selectedKid = kid }
                    )
                }
                item {
                    AddChildCard(onAddKidClick = onAddKidClick)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))


            val currentKid = selectedKid ?: kids.firstOrNull()

            if (currentKid != null) {

                Text(
                    text = "Viewing: ${currentKid.name}",
                    fontSize = 14.sp,
                    color = SoftBlueButton,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 24.dp, bottom = 4.dp)
                )

                RoutineOverviewSection(currentKid, routineList)
                Spacer(modifier = Modifier.height(12.dp))
                DailyProgressSection(currentKid)
            }

            Spacer(modifier = Modifier.height(50.dp))
        }

        SayToonsBackButton(onClick = onBackClick)


        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(24.dp)
                .size(59.dp)
                .border(BorderStroke(3.dp, Color(0XFFb20000)), shape = CircleShape)
                .shadow(6.dp, CircleShape)
                .clip(CircleShape)
                .background(Color.Red)
                .clickable { onLogoutClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.logout_icon),
                contentDescription = "Logout",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
fun KidCard(
    kid: Kid,
    routines: List<RoutineConfig>,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val avatarRes = if (kid.gender.equals("Female", ignoreCase = true)) R.drawable.girl else R.drawable.kid


    val borderColor = if (isSelected) SoftBlueButton else Color.Transparent
    val borderWidth = if (isSelected) 4.dp else 0.dp

    Card(
        modifier = Modifier
            .width(280.dp)
            .height(260.dp)
            .shadow(12.dp, RoundedCornerShape(32.dp), spotColor = Color(0xFF448AFF).copy(alpha = 0.2f))
            .border(borderWidth, borderColor, RoundedCornerShape(32.dp)) // Apply Border
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            ),
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            // Header
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = avatarRes),
                    contentDescription = null,
                    modifier = Modifier.size(60.dp).clip(RoundedCornerShape(18.dp)),
                    contentScale = ContentScale.Fit
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(kid.name, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = TextBlack)
                    Text("Age: ${kid.ageRange}", fontSize = 14.sp, color = TextGray)
                }
                Spacer(modifier = Modifier.weight(1f))
                Icon(Icons.Rounded.Star, null, tint = StarYellow, modifier = Modifier.size(28.dp))
                Text("${kid.totalStars}", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextBlack)
            }

            Spacer(modifier = Modifier.height(24.dp))


            val topRoutines = remember(kid.completedTasksToday) {
                routines.sortedByDescending { kid.completedTasksToday[it.id] ?: 0 }.take(2)
            }

            topRoutines.forEachIndexed { index, routine ->
                val count = kid.completedTasksToday[routine.id] ?: 0
                TaskItemWithBar(
                    title = routine.title,
                    current = count,
                    max = 7,
                    iconRes = routine.iconRes
                )
                if (index < topRoutines.size - 1) {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
fun AddChildCard(onAddKidClick: () -> Unit) {
    Card(
        modifier = Modifier
            .width(280.dp)
            .height(240.dp)
            .shadow(
                elevation = 12.dp,
                shape = RoundedCornerShape(32.dp),
                spotColor = Color(0xFF448AFF).copy(alpha = 0.2f)
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onAddKidClick
            ),
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.7f))
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(Icons.Outlined.Add, null, modifier = Modifier.size(80.dp), tint = Color.Gray)
            Text("Add a Child", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
        }
    }
}

@Composable
fun TaskItemWithBar(title: String, current: Int, max: Int, iconRes: Int) {

    val progress = current.toFloat() / max.toFloat()

    Column(modifier = Modifier.fillMaxWidth()) {

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                tint = IconGreen,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = title,
                fontSize = 16.sp,
                color = TextBlack,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "$current/$max",
                fontSize = 14.sp,
                color = TextGray
            )
        }

        Spacer(modifier = Modifier.height(8.dp))


        RoundedProgressBar(progress = progress)
    }
}


@Composable
fun RoundedProgressBar(
    progress: Float,
    modifier: Modifier = Modifier
) {
    LinearProgressIndicator(
        progress = { progress },
        modifier = modifier
            .fillMaxWidth()
            .height(8.dp)
            .clip(RoundedCornerShape(4.dp)),
        color = IconGreen,
        trackColor = IconGreen.copy(alpha = 0.2f),
        strokeCap = StrokeCap.Round,
    )
}

@Composable
fun TaskItem(title: String, count: String, iconRes: Int) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            tint = IconGreen,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(title, fontSize = 16.sp, color = TextBlack, fontWeight = FontWeight.Medium)
        Spacer(modifier = Modifier.weight(1f))
        Text(count, fontSize = 14.sp, color = TextGray)
    }
}

@Composable
fun RoutineOverviewSection(kid: Kid, routines: List<RoutineConfig>) {
    Text(
        "Routine Overview",
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(start = 20.dp, bottom = 12.dp),
        color = TextBlack
    )
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .shadow(8.dp, RoundedCornerShape(24.dp), spotColor = Color.Blue.copy(0.1f)),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {


            val allRoutines = routines.sortedByDescending { kid.completedTasksToday[it.id] ?: 0 }

            allRoutines.forEachIndexed { index, routine ->
                val count = kid.completedTasksToday[routine.id] ?: 0

                RoutineOverviewRow(
                    title = routine.title,
                    count = "$count/7 days",
                    iconRes = routine.iconRes
                )

                if (index < allRoutines.size - 1) {
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
}

@Composable
fun RoutineOverviewRow(title: String, count: String, iconRes: Int) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            tint = SoftBlueButton,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(title, fontSize = 16.sp, color = TextBlack)
        Spacer(modifier = Modifier.weight(1f))
        Text(count, fontSize = 14.sp, color = TextGray)
        Spacer(modifier = Modifier.width(8.dp))
    }
}

@Composable
fun DailyProgressSection(kid: Kid) {
    Text(
        "Daily Progress",
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(start = 20.dp, bottom = 10.dp),
        color = TextBlack
    )
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(120.dp)
            .shadow(8.dp, RoundedCornerShape(24.dp), spotColor = Color.Blue.copy(0.1f)),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val days = listOf("MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN")


            val calendar = java.util.Calendar.getInstance()
            val dayFormat = java.text.SimpleDateFormat("EEE", java.util.Locale.US)
            val todayString = dayFormat.format(calendar.time).uppercase()

            days.forEach { day ->

                val starsForDay = kid.weeklyHistory[day] ?: 0
                val isStarFilled = starsForDay > 0

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_star_large),
                        contentDescription = null,

                        tint = if (isStarFilled) StarYellow else Color(0xFFEEEEEE),
                        modifier = Modifier.size(40.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = day,
                        fontSize = 10.sp,
                        fontWeight = if (day == todayString) FontWeight.Bold else FontWeight.Normal,
                        color = if (day == todayString) SoftBlueButton else Color.LightGray
                    )
                }
            }
        }
    }
}

