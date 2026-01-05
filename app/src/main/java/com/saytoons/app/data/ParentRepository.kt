package com.saytoons.app.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.snapshots
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

data class Kid(
    val id: String = "",
    val name: String = "",
    val gender: String = "",
    val totalStars: Int = 0,
    val ageRange: String = "",
    val unlockedRoutines: List<String> = listOf("morning"),

    val completedTasksToday: Map<String, Int> = emptyMap(),

    val weeklyHistory: Map<String, Int> = emptyMap(),
    val lastWeekOfYear: Int = -1,
    val routineScores: Map<String, Int> = emptyMap()
)
class ParentRepository {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()


    fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    suspend fun loginParent(email: String, pass: String): Result<Boolean> {
        return try {
            auth.signInWithEmailAndPassword(email, pass).await()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    suspend fun createParentAccount(
        email: String,
        pass: String,
        parentName: String,
        kidNames: List<String>,
        selectedAgeRange: String,
        selectedGender: String
    ): Result<Boolean> {
        return try {
            auth.createUserWithEmailAndPassword(email, pass).await()
            val userId = auth.currentUser!!.uid

            val userMap = hashMapOf(
                "email" to email,
                "parentName" to parentName,

                "selectedAgeRange" to selectedAgeRange
            )

            db.collection("users").document(userId).set(userMap).await()


            kidNames.forEach { kidName ->
                if (kidName.isNotBlank()) {
                    addKid(kidName, selectedGender, selectedAgeRange)
                }
            }
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun addKid(name: String, gender: String, ageRange: String): Result<Boolean> {
        val userId = auth.currentUser?.uid ?: return Result.failure(Exception("Not Logged In"))

        val newKid = hashMapOf(
            "name" to name,
            "gender" to gender,
            "ageRange" to ageRange,
            "totalStars" to 0,
            "createdAt" to System.currentTimeMillis()
        )

        return try {
            db.collection("users").document(userId)
                .collection("kids")
                .add(newKid)
                .await()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

  //remove
    suspend fun addStarToKid(kidId: String) {
        val userId = auth.currentUser?.uid ?: return
        
        try {
            val kidRef = db.collection("users").document(userId)
                .collection("kids").document(kidId)

            db.runTransaction { transaction ->
                val snapshot = transaction.get(kidRef)
                val currentStars = snapshot.getLong("totalStars") ?: 0
                transaction.update(kidRef, "totalStars", currentStars + 1)
            }.await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    //next card incremenet
    fun getKidsFlow(): Flow<List<Kid>>? {
        val userId = auth.currentUser?.uid ?: return null

        return db.collection("users").document(userId)
            .collection("kids")
            .snapshots()
            .map { snapshot ->
                snapshot.documents.mapNotNull { doc ->
                    doc.toObject<Kid>()?.copy(id = doc.id)
                }
            }
    }

    //unlock next routine
    suspend fun unlockRoutine(kidId: String, newRoutineId: String) {
        val userId = auth.currentUser?.uid ?: return

        try {
            val kidRef = db.collection("users").document(userId)
                .collection("kids").document(kidId)


            kidRef.update("unlockedRoutines", FieldValue.arrayUnion(newRoutineId)).await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    //calculate total stars
    suspend fun addStars(kidId: String, starsEarned: Int) {
        if (starsEarned <= 0) return

        val userId = auth.currentUser?.uid ?: return
        val kidRef = db.collection("users").document(userId)
            .collection("kids").document(kidId)

        try {
            db.runTransaction { transaction ->
                val snapshot = transaction.get(kidRef)


                val calendar = java.util.Calendar.getInstance()
                val currentWeek = calendar.get(java.util.Calendar.WEEK_OF_YEAR)


                val dayFormat = java.text.SimpleDateFormat("EEE", java.util.Locale.US)
                val currentDayKey = dayFormat.format(calendar.time).uppercase()

                //montue
                val storedWeek = snapshot.getLong("lastWeekOfYear")?.toInt() ?: -1
                var history = snapshot.get("weeklyHistory") as? MutableMap<String, Int> ?: mutableMapOf()
                val currentTotal = snapshot.getLong("totalStars") ?: 0

              //weeklyreset
                if (storedWeek != currentWeek) {
                    history = mutableMapOf()
                }


                val starsToday = history[currentDayKey] ?: 0
                history[currentDayKey] = starsToday + starsEarned

                //firebase
                transaction.update(kidRef, "totalStars", currentTotal + starsEarned)
                transaction.update(kidRef, "weeklyHistory", history)
                transaction.update(kidRef, "lastWeekOfYear", currentWeek)

            }.await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    suspend fun completeTask(kidId: String, taskKey: String) {
        val userId = auth.currentUser?.uid ?: return
        try {
            val kidRef = db.collection("users").document(userId)
                .collection("kids").document(kidId)


            db.runTransaction { transaction ->
                val snapshot = transaction.get(kidRef)

                val currentTasks = snapshot.get("completedTasksToday") as? MutableMap<String, Long> ?: mutableMapOf()

                val currentVal = currentTasks[taskKey] ?: 0
                currentTasks[taskKey] = currentVal + 1

                transaction.update(kidRef, "completedTasksToday", currentTasks)
            }.await()
        } catch (e: Exception) { e.printStackTrace() }
    }

    //database sync
    suspend fun updateRoutineScore(kidId: String, routineKey: String, score: Int) {
        val userId = auth.currentUser?.uid ?: return
        try {
            val kidRef = db.collection("users").document(userId)
                .collection("kids").document(kidId)


            kidRef.update("routineScores.$routineKey", score).await()
        } catch (e: Exception) { e.printStackTrace() }
    }

    suspend fun signInWithGoogle(idToken: String): Result<Boolean> {
        return try {
            val credential = com.google.firebase.auth.GoogleAuthProvider.getCredential(idToken, null)
            val authResult = auth.signInWithCredential(credential).await()
            val user = authResult.user

            if (user != null) {

                val doc = db.collection("users").document(user.uid).get().await()
                if (!doc.exists()) {
                    val userMap = hashMapOf(
                        "email" to user.email,
                        "parentName" to (user.displayName ?: "Parent"),
                        "createdAt" to System.currentTimeMillis()
                    )
                    db.collection("users").document(user.uid).set(userMap).await()
                }
                Result.success(true)
            } else {
                Result.failure(Exception("Google Sign In Failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}