package com.spartanai.nova.data

data class Recipe(
    val title: String,
    val ingredients: List<String>,
    val instructions: String
)

object BakingRecipes {
    val recipes = listOf(
        Recipe(
            title = "Perfect Vanilla Cupcakes",
            ingredients = listOf(
                "1 1/2 cups all-purpose flour",
                "1 cup granulated sugar",
                "1 1/2 tsp baking powder",
                "1/2 tsp salt",
                "1/2 cup unsalted butter, softened",
                "1 large egg",
                "1/2 cup whole milk",
                "2 tsp vanilla extract"
            ),
            instructions = "1. Preheat oven to 350°F (175°C). Line a cupcake pan with liners.\n" +
                    "2. In a large bowl, whisk together flour, sugar, baking powder, and salt.\n" +
                    "3. Add butter, egg, milk, and vanilla. Beat on medium speed for 2 minutes.\n" +
                    "4. Fill liners 2/3 full.\n" +
                    "5. Bake for 18-20 minutes or until a toothpick comes out clean."
        ),
        Recipe(
            title = "Classic Chocolate Chip Cookies",
            ingredients = listOf(
                "2 1/4 cups all-purpose flour",
                "1/2 tsp baking soda",
                "1 cup unsalted butter, melted",
                "1/2 cup white sugar",
                "1 cup packed brown sugar",
                "1 tbsp vanilla extract",
                "1/2 tsp salt",
                "2 large eggs",
                "2 cups semi-sweet chocolate chips"
            ),
            instructions = "1. Preheat oven to 325°F (165°C). Line baking sheets with parchment paper.\n" +
                    "2. Sift together the flour and baking soda; set aside.\n" +
                    "3. In a medium bowl, cream together the melted butter, brown sugar and white sugar until well blended.\n" +
                    "4. Beat in the vanilla, salt, and eggs until light and creamy.\n" +
                    "5. Mix in the sifted ingredients until just blended. Stir in the chocolate chips by hand using a wooden spoon.\n" +
                    "6. Drop cookie dough by heaping tablespoons onto the prepared baking sheets.\n" +
                    "7. Bake for 15 to 17 minutes in the preheated oven, or until the edges are lightly toasted."
        ),
        Recipe(
            title = "Velvety Red Velvet Cake",
            ingredients = listOf(
                "2 1/2 cups all-purpose flour",
                "1 1/2 cups sugar",
                "1 tsp baking soda",
                "1 tsp cocoa powder",
                "1 tsp salt",
                "1 cup buttermilk",
                "2 large eggs",
                "1 1/2 cups vegetable oil",
                "1 tsp white vinegar",
                "Red food coloring",
                "1 tsp vanilla extract"
            ),
            instructions = "1. Preheat oven to 350°F. Grease and flour two 9-inch cake pans.\n" +
                    "2. Whisk together flour, sugar, soda, cocoa, and salt.\n" +
                    "3. In another bowl, whisk together buttermilk, eggs, oil, vinegar, food coloring, and vanilla.\n" +
                    "4. Gradually add wet ingredients to dry and mix until smooth.\n" +
                    "5. Pour into pans and bake for 30-35 minutes."
        )
    )
}
