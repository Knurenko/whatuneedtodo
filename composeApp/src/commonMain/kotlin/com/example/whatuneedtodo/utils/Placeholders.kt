package com.example.whatuneedtodo.utils

import com.example.whatuneedtodo.domain.model.TodoModel
import com.example.whatuneedtodo.domain.model.TodoPriority

object Placeholders {
    val todoList: List<TodoModel> = listOf(
        TodoModel(1, "Buy groceries", 1714456800000L, TodoPriority.Medium, "Milk, Eggs, Bread, and Coffee"),
        TodoModel(2, "Finish project report", 1714543200000L, TodoPriority.Urgent, "Draft the final version and send it to the manager"),
        TodoModel(3, "Gym workout", 1714629600000L, TodoPriority.High, "Focus on legs and cardio today"),
        TodoModel(4, "Call mom", 1714716000000L, TodoPriority.Low, "Check in and see how she is doing"),
        TodoModel(5, "Car service", 1714802400000L, TodoPriority.High, "Oil change and tire rotation"),
        TodoModel(6, "Read a book", 1714888800000L, TodoPriority.Medium, "Read at least 30 pages of the current book"),
        TodoModel(7, "Water the plants", 1714975200000L, TodoPriority.Low, "Don't forget the ones in the balcony"),
        TodoModel(8, "Fix the leaky faucet", 1715061600000L, TodoPriority.Urgent, "Kitchen faucet is dripping constantly"),
        TodoModel(9, "Plan weekend trip", 1715148000000L, TodoPriority.Medium, "Look for hotels and nearby attractions"),
        TodoModel(10, "Update resume", 1715234400000L, TodoPriority.High, "Add recent projects and skills"),
        TodoModel(11, "Clean the house", 1715320800000L, TodoPriority.Medium, "Vacuum and dust all rooms"),
        TodoModel(12, "Pay bills", 1715407200000L, TodoPriority.Urgent, "Electricity, water, and internet bills are due"),
        TodoModel(13, "Organize desk", 1715493600000L, TodoPriority.Low, "Clear out old papers and file important ones"),
        TodoModel(14, "Walk the dog", 1715580000000L, TodoPriority.Medium, "Go for a long walk in the park"),
        TodoModel(15, "Prepare for interview", 1715666400000L, TodoPriority.Urgent, "Research the company and practice common questions"),
        TodoModel(16, "Bake a cake", 1715752800000L, TodoPriority.High, "Try the new chocolate lava cake recipe"),
        TodoModel(17, "Learn Kotlin Multiplatform", 1715839200000L, TodoPriority.Urgent, "Watch tutorials and build a sample app"),
        TodoModel(18, "Meditate", 1715925600000L, TodoPriority.Low, "10 minutes of guided meditation"),
        TodoModel(19, "Write a blog post", 1716012000000L, TodoPriority.Medium, "Topic: Benefits of using Jetpack Compose"),
        TodoModel(20, "Buy new sneakers", 1716098400000L, TodoPriority.Low, "Look for comfortable running shoes")
    )
}