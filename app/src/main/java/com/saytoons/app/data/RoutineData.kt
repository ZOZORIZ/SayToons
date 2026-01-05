package com.saytoons.app.data

import androidx.compose.runtime.mutableStateOf
import com.saytoons.app.R


data class Task(
    val title: String,
    val instruction: String,
    val expectedSpeech: String,
    val initialImageRes: Int,
    val aiPrompt: String
)


object RoutineRepository {


    val morningRoutine = listOf(
        Task("Brush Teeth", "Brush your teeth!", "I brushed my teeth", R.drawable.img_brush_teeth, "cute 3d cartoon boy brushing teeth happily, sparkles, pixar style"),
        Task("Wash Face", "Wash your face!", "I washed my face", R.drawable.img_wash_face, "cute 3d cartoon boy washing face with water splash, pixar style"),
        Task("Get Dressed", "Put on your clothes!", "I am dressed", R.drawable.img_dress_up, "cute 3d cartoon boy wearing colorful t-shirt, pixar style"),
        Task("Eat Breakfast", "Eat your yummy food!", "I ate my breakfast", R.drawable.img_breakfast, "cute 3d cartoon boy eating cereal, pixar style")
    )

    val nightRoutine = listOf(
        Task("Toy Cleanup", "Put your toys away!", "I cleaned up my toys", R.drawable.img_toys, "cute 3d bedroom with toys in box, pixar style"),
        Task("Put on Pajamas", "Wear your pajamas!", "I am in my pajamas", R.drawable.img_pajamas, "cute 3d cartoon boy in blue pajamas, pixar style"),
        Task("Story Time", "Read a book!", "I read a book", R.drawable.img_reading, "cute 3d cartoon boy reading a book in bed, pixar style"),
        Task("Go to Sleep", "Goodnight!", "Goodnight", R.drawable.img_sleep, "cute 3d cartoon boy sleeping in bed, pixar style")
    )

    val mealRoutine = listOf(
        Task("Wash Hands", "Wash hands with soap!", "I washed my hands", R.drawable.img_wash_hands, "cute 3d cartoon hands with soap bubbles, pixar style"),
        Task("Eat Veggies", "Finish your veggies!", "I ate my veggies", R.drawable.img_veggies, "cute 3d cartoon boy eating broccoli and smiling, pixar style"),
        Task("Drink Water", "Drink your water!", "I drank my water", R.drawable.img_drink_water, "cute 3d cartoon boy holding glass of water, pixar style")
    )


    val schoolRoutine = listOf(
        Task("Pack Bag", "Put your books in the bag!", "I packed my bag", R.drawable.ic_morning, "cute 3d cartoon school bag with books, pixar style"),
        Task("Wear Uniform", "Put on your uniform!", "I am wearing my uniform", R.drawable.ic_morning, "cute 3d cartoon boy wearing school uniform, pixar style"),
        Task("Wear Shoes", "Put on your shoes!", "I put on my shoes", R.drawable.ic_morning, "cute 3d cartoon boy tying shoes, pixar style"),
        Task("Bus Waiting", "Wait for the school bus!", "I am waiting for the bus", R.drawable.ic_morning, "cute 3d cartoon yellow school bus arriving, pixar style")
    )


    val marriageRoutine = listOf(
        Task("Wear Party Dress", "Put on your fancy dress!", "I am looking fancy", R.drawable.ic_star, "cute 3d cartoon boy wearing suit or tuxedo for wedding, pixar style"),
        Task("Greet Guests", "Say hello to everyone!", "Hello everyone", R.drawable.ic_star, "cute 3d cartoon boy waving hand happily at wedding, pixar style"),
        Task("Meet The Bride and Groom", "Say hi to the bride and groom!", "Hi bride and groom", R.drawable.ic_star,  "cute 3d cartoon boy sitting on chair quietly, pixar style"),
        Task("Dance", "Dance to the music!", "I am dancing", R.drawable.ic_star, "cute 3d cartoon boy dancing at a party, fun lighting, pixar style")
    )


    val hospitalRoutine = listOf(
        Task("Check In", "Wait at the desk!", "I am waiting nicely", R.drawable.ic_star, "cute 3d cartoon hospital reception desk, friendly nurse, pixar style"),
        Task("Doctor Visit", "Say ahh for the doctor!", "Ahh", R.drawable.ic_star, "cute 3d cartoon doctor with stethoscope smiling, pixar style"),
        Task("Be Brave", "You are very brave!", "I am brave", R.drawable.ic_star, "cute 3d cartoon boy flexing arm muscles, superhero cape reflection, pixar style"),
        Task("Get Sticker", "Collect your reward sticker!", "I got a sticker", R.drawable.ic_star, "cute 3d cartoon hand holding a star sticker, pixar style")
    )


    val transportRoutine = listOf(
        Task("Car", "Say 'Car'!", "Car", R.drawable.ic_star, "cute 3d cartoon red car zooming, pixar style"),
        Task("Bus", "Say 'Bus'!", "Bus", R.drawable.ic_star, "cute 3d cartoon yellow bus, pixar style"),
        Task("Train", "Say 'Choo Choo'!", "Choo Choo", R.drawable.ic_star, "cute 3d cartoon steam train puffing smoke, pixar style"),
        Task("Airplane", "Say 'Airplane'!", "Airplane", R.drawable.ic_star, "cute 3d cartoon airplane flying in clouds, pixar style")
    )


    val animalsRoutine = listOf(
        Task("Dog", "What does a dog say?", "Woof woof", R.drawable.ic_star, "cute 3d cartoon puppy dog wagging tail, pixar style"),
        Task("Cat", "What does a cat say?", "Meow", R.drawable.ic_star, "cute 3d cartoon kitten playing with yarn, pixar style"),
        Task("Lion", "What does a lion say?", "Roar", R.drawable.ic_star, "cute 3d cartoon lion with big mane, pixar style"),
        Task("Elephant", "Say 'Elephant'!", "Elephant", R.drawable.ic_star, "cute 3d cartoon elephant spraying water, pixar style")
    )


    val birdsRoutine = listOf(
        Task("Parrot", "Say 'Parrot'!", "Parrot", R.drawable.ic_star, "cute 3d colorful cartoon parrot, pixar style"),
        Task("Duck", "What does a duck say?", "Quack quack", R.drawable.ic_star, "cute 3d cartoon duck in pond, pixar style"),
        Task("Owl", "What does an owl say?", "Hoot hoot", R.drawable.ic_star, "cute 3d cartoon owl on tree branch night, pixar style"),
        Task("Peacock", "Say 'Peacock'!", "Peacock", R.drawable.ic_star, "cute 3d cartoon peacock showing feathers, pixar style")
    )


    val shapesRoutine = listOf(
        Task("Circle", "Find something round!", "Circle", R.drawable.ic_star, "cute 3d colorful circle shape character smiling, pixar style"),
        Task("Square", "Say 'Square'!", "Square", R.drawable.ic_star, "cute 3d colorful square shape character, pixar style"),
        Task("Triangle", "Say 'Triangle'!", "Triangle", R.drawable.ic_star, "cute 3d colorful triangle shape character, pixar style"),
        Task("Star", "Say 'Twinkle Star'!", "Twinkle Star", R.drawable.ic_star, "cute 3d golden star glowing, pixar style")
    )

    fun getRoutine(routineId: String): List<Task> {
        return when (routineId.lowercase().trim()) {
            "morning routine" -> morningRoutine
            "bedtime routine" -> nightRoutine
            "mealtime routine" -> mealRoutine
            "school routine" -> schoolRoutine
            "marriage routine" -> marriageRoutine
            "hospital routine" -> hospitalRoutine
            "transportation routine" -> transportRoutine
            "animals routine" -> animalsRoutine
            "birds routine" -> birdsRoutine
            "shapes routine" -> shapesRoutine
            else -> morningRoutine
        }
    }
}