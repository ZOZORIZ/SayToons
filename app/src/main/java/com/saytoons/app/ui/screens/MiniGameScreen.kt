    package com.saytoons.app.ui.screens

    import androidx.compose.foundation.Image
    import androidx.compose.foundation.background
    import androidx.compose.foundation.border
    import androidx.compose.foundation.clickable
    import androidx.compose.foundation.layout.*
    import androidx.compose.foundation.lazy.grid.GridCells
    import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
    import androidx.compose.foundation.shape.CircleShape
    import androidx.compose.foundation.shape.RoundedCornerShape
    import androidx.compose.material.icons.Icons
    import androidx.compose.material.icons.filled.Check
    import androidx.compose.material.icons.filled.Close
    import androidx.compose.material.icons.filled.Refresh
    import androidx.compose.material.icons.filled.Star
    import androidx.compose.material3.*
    import androidx.compose.runtime.*
    import androidx.compose.ui.Alignment
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.draw.alpha
    import androidx.compose.ui.draw.clip
    import androidx.compose.ui.graphics.Color
    import androidx.compose.ui.layout.ContentScale
    import androidx.compose.ui.res.painterResource
    import androidx.compose.ui.text.font.FontWeight
    import androidx.compose.ui.unit.dp
    import androidx.compose.ui.unit.sp
    import com.saytoons.app.R
    import kotlinx.coroutines.delay
    import kotlin.random.Random
    import androidx.compose.foundation.gestures.detectDragGestures
    import androidx.compose.ui.input.pointer.pointerInput
    import androidx.compose.ui.geometry.Offset
    import androidx.compose.ui.layout.onGloballyPositioned
    import androidx.compose.ui.unit.IntSize

    // --- MINI GAME ROUTER ---
    @Composable
    fun MiniGameScreen(
        onGameFinished: (Int) -> Unit // Returns stars earned (0 or 2)
    ) {
        // 0: Tic Tac Toe, 1: Memory Match
        val gameId by remember { mutableStateOf(Random.nextInt(0, 3)) }

        val backgroundRes = when (gameId) {
            0 -> R.drawable.mini_game_background
            1 -> R.drawable.mini_game_background
            2 -> R.drawable.mini_game_background // Find Path Background
            else -> R.drawable.background_routine
        }

        Box(modifier = Modifier.fillMaxSize()) {
            // Shared Background
            Image(
                painter = painterResource(id = backgroundRes),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Column(modifier = Modifier.fillMaxSize()) {
                // Header with Skip Button
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = { onGameFinished(0) }, // Skip = 0 stars
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.8f))
                    ) {
                        Text("Skip", color = Color.Gray)
                    }
                }

                // Game Content
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    when (gameId) {
                        0 -> TicTacToeGame(onWin = { onGameFinished(2) })
                        1 -> MemoryMatchGame(onWin = { onGameFinished(2) })
                        2 -> FindPathGame(onWin = { onGameFinished(2) }) // <--- ADD THIS
                        else -> Text("Coming Soon!")
                    }
                }
            }
        }
    }

    // --- GAME 1: TIC TAC TOE (Kid vs Toony/AI) ---
    @Composable
    fun TicTacToeGame(onWin: () -> Unit) {
        var board by remember { mutableStateOf(List(9) { "" }) }
        var isPlayerTurn by remember { mutableStateOf(true) } // Player is X
        var winner by remember { mutableStateOf<String?>(null) }

        fun checkWin(currentBoard: List<String>): String? {
            val patterns = listOf(
                listOf(0,1,2), listOf(3,4,5), listOf(6,7,8), // Rows
                listOf(0,3,6), listOf(1,4,7), listOf(2,5,8), // Cols
                listOf(0,4,8), listOf(2,4,6) // Diagonals
            )
            for (pattern in patterns) {
                val (a,b,c) = pattern
                if (currentBoard[a].isNotEmpty() && currentBoard[a] == currentBoard[b] && currentBoard[a] == currentBoard[c]) {
                    return currentBoard[a]
                }
            }
            if (currentBoard.none { it.isEmpty() }) return "Draw"
            return null
        }

        // --- AUTOMATIC HANDLING OF WIN/LOSS ---
        LaunchedEffect(winner) {
            if (winner == "X") {
                // WIN: Wait 1 second and Go to Success
                delay(1000)
                onWin()
            } else if (winner != null) {
                // LOSS/DRAW: Wait 1.5 seconds and Reset Board automatically
                delay(1500)
                board = List(9) { "" }
                winner = null
                isPlayerTurn = true
            }
        }

        // AI Turn (Simple Random)
        LaunchedEffect(isPlayerTurn, winner) {
            if (!isPlayerTurn && winner == null) {
                delay(1000) // AI thinks
                val emptyIndices = board.indices.filter { board[it].isEmpty() }
                if (emptyIndices.isNotEmpty()) {
                    val move = emptyIndices.random()
                    val newBoard = board.toMutableList()
                    newBoard[move] = "O"
                    board = newBoard
                    val w = checkWin(newBoard)
                    if (w != null) {
                        winner = w
                    } else {
                        isPlayerTurn = true
                    }
                }
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(horizontal = 16.dp)
        ) {

            Text(
                text = "TIC TAC TOE",
                fontSize = 35.sp,
                fontWeight = FontWeight.Black,
                color = SoftBlueButton,
                modifier = Modifier
                    .offset(x=60.dp)
                    .offset(y = (-80).dp),

            )

            Spacer(modifier = Modifier.height(24.dp))


            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(0.95f)),
                elevation = CardDefaults.cardElevation(10.dp),
                modifier = Modifier
                    .padding(start=20.dp)
            ) {
                Column(
                    modifier = Modifier

                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {

                    for (i in 0..2) {
                        Row {
                            for (j in 0..2) {
                                val index = i * 3 + j
                                Box(
                                    modifier = Modifier
                                        .size(85.dp)
                                        .padding(4.dp)
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(Color(0xFFF5F5F5)) // Light gray slots
                                        .border(2.dp, if(board[index].isEmpty()) Color.Transparent else Color.LightGray.copy(0.5f), RoundedCornerShape(16.dp))
                                        .clickable(enabled = isPlayerTurn && board[index].isEmpty() && winner == null) {
                                            val newBoard = board.toMutableList()
                                            newBoard[index] = "X"
                                            board = newBoard
                                            val w = checkWin(newBoard)
                                            if (w != null) {
                                                winner = w
                                            } else {
                                                isPlayerTurn = false
                                            }
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (board[index] == "X") {
                                        Icon(Icons.Default.Close, null, tint = SoftBlueButton, modifier = Modifier.size(50.dp))
                                    } else if (board[index] == "O") {
                                        Icon(Icons.Default.Check, null, tint = Color(0xFFFF9800), modifier = Modifier.size(50.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))


            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .background(Color.Black.copy(alpha = 0.6f), RoundedCornerShape(50))
                    .padding(horizontal = 20.dp, vertical = 10.dp)
            ) {
                if (winner != null) {

                    Text(
                        text = if (winner == "X") "You Won!" else if (winner == "Draw") "It's a Draw!" else "Toony Won!",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                } else if (isPlayerTurn) {
                    Text("Your Turn", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                } else {

                    Image(
                        painter = painterResource(id = R.drawable.toony_happy),
                        contentDescription = null,
                        modifier = Modifier.size(30.dp).clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text("Toony is thinking...", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }
            }
        }
    }

    @Composable
    fun MemoryMatchGame(onWin: () -> Unit) {
        val icons = remember {
            listOf(
                Icons.Default.Star, Icons.Default.Star,
                Icons.Default.Refresh, Icons.Default.Refresh,
                Icons.Default.Close, Icons.Default.Close,
                Icons.Default.Check, Icons.Default.Check
            ).shuffled()
        }

        var matchedIndices by remember { mutableStateOf(setOf<Int>()) }
        var flippedIndices by remember { mutableStateOf(listOf<Int>()) }


        LaunchedEffect(matchedIndices) {
            if (matchedIndices.size == icons.size && icons.isNotEmpty()) {
                delay(500) // Small celebration pause
                onWin()
            }
        }


        LaunchedEffect(flippedIndices) {
            if (flippedIndices.size == 2) {
                delay(1000)
                val idx1 = flippedIndices[0]
                val idx2 = flippedIndices[1]

                if (icons[idx1] == icons[idx2]) {
                    matchedIndices = matchedIndices + idx1 + idx2
                }
                flippedIndices = emptyList()
            }
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "MEMORY MATCH",
                fontSize = 25.sp,
                fontWeight = FontWeight.Black,
                color = SoftBlueButton,
                modifier = Modifier
                    .offset(x=50.dp)
                    .offset(y = (-160).dp),
            )

            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.2f))
            ) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(icons.size) { index ->
                        val isMatched = matchedIndices.contains(index)
                        val isFlipped = flippedIndices.contains(index)

                        Box(
                            modifier = Modifier
                                .height(80.dp)
                                .alpha(if (isMatched) 0f else 1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isFlipped) Color.White else SoftBlueButton)
                                .border(2.dp, Color.White, RoundedCornerShape(12.dp))
                                .clickable(
                                    enabled = !isMatched && !isFlipped && flippedIndices.size < 2
                                ) {
                                    flippedIndices = flippedIndices + index
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            if (isFlipped) {
                                Icon(icons[index], null, tint = SoftBlueButton)
                            } else {
                                Text("?", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun FindPathGame(onWin: () -> Unit) {

        val gridSize = 5

        val mazeLayout = remember {
            listOf(
                listOf(0, 0, 1, 0, 0),
                listOf(1, 0, 1, 0, 1),
                listOf(0, 0, 0, 0, 0),
                listOf(0, 1, 1, 1, 0),
                listOf(0, 0, 0, 1, 0)
            )
        }

        var pathPoints by remember { mutableStateOf(setOf<Pair<Int, Int>>()) }
        var currentPoint by remember { mutableStateOf(0 to 0) }
        var feedbackMessage by remember { mutableStateOf("Guide the Dot to the Star!") }
        var feedbackColor by remember { mutableStateOf(SoftBlueButton) }
        var size by remember { mutableStateOf(IntSize.Zero) }
        var gameFinished by remember { mutableStateOf(false) }



        fun getGridCoordinate(offset: Offset, width: Int, height: Int): Pair<Int, Int>? {
            if (width == 0 || height == 0) return null
            val col = (offset.x / (width / gridSize)).toInt()
            val row = (offset.y / (height / gridSize)).toInt()
            if (col in 0 until gridSize && row in 0 until gridSize) {
                return row to col
            }
            return null
        }


        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "MAZE RUNNER",
                fontSize = 30.sp,
                fontWeight = FontWeight.Black,
                color = SoftBlueButton,

                modifier = Modifier
                    .offset(x=50.dp)
                    .offset(y = (-70).dp),

            )

            Spacer(modifier = Modifier.height(8.dp))


            Text(
                text = feedbackMessage,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = feedbackColor,
                modifier = Modifier
                    .background(Color.White, RoundedCornerShape(12.dp))
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))


            Box(
                modifier = Modifier
                    .size(320.dp)
                    .background(Color.White.copy(alpha = 0.8f), RoundedCornerShape(16.dp))
                    .border(4.dp, SoftBlueButton, RoundedCornerShape(16.dp))
                    .padding(8.dp)
                    .onGloballyPositioned { size = it.size }
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDragStart = { offset ->
                                // Must start at (0,0)
                                val startNode = getGridCoordinate(offset, size.width, size.height)
                                if (startNode == (0 to 0)) {
                                    pathPoints = setOf(0 to 0)
                                    feedbackMessage = "Go!"
                                    feedbackColor = IconGreen
                                }
                            },
                            onDrag = { change, _ ->
                                val pos = getGridCoordinate(change.position, size.width, size.height)


                                if (pos != null && pos != currentPoint && !gameFinished) {
                                    currentPoint = pos
                                    val (row, col) = pos

                                    if (mazeLayout[row][col] == 1) {

                                        pathPoints = setOf()
                                        feedbackMessage = "Oops! Hit a wall."
                                        feedbackColor = Color.Red
                                    } else if (row == 4 && col == 4) {

                                        if (!pathPoints.contains(4 to 4)) {
                                            pathPoints = pathPoints + pos
                                            gameFinished = true
                                            onWin()
                                        }
                                    } else {

                                        pathPoints = pathPoints + pos
                                    }
                                }
                            }
                        )
                    }
            ) {

                Column {
                    for (row in 0 until gridSize) {
                        Row(modifier = Modifier.weight(1f)) {
                            for (col in 0 until gridSize) {
                                val isObstacle = mazeLayout[row][col] == 1
                                val isStart = row == 0 && col == 0
                                val isEnd = row == 4 && col == 4
                                val isPath = pathPoints.contains(row to col)

                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxHeight()
                                        .padding(2.dp)
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(
                                            when {
                                                isStart -> Color(0xFF64B5F6)
                                                isEnd -> Color(0xFFFFD700)
                                                isObstacle -> Color(0xFFEF5350)
                                                isPath -> Color(0xFF81C784)
                                                else -> Color(0xFFECEFF1)
                                            }
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (isStart) Text("START", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                    if (isEnd) Icon(Icons.Default.Star, null, tint = Color.White)
                                    if (isObstacle) Icon(Icons.Default.Close, null, tint = Color.White.copy(0.5f))
                                }
                            }
                        }
                    }
                }
            }
        }
    }