package com.example.dracodemo

/**
 * A small demo data model — one "target" item shown in the simulator list.
 * Purely illustrative; it holds display values only.
 */
data class DemoTarget(
    val id: Int,
    val name: String,
    val distance: Float,
    val radius: Float
) {
    companion object {
        /** A fixed set of sample targets for the demo screen. */
        fun samples(): List<DemoTarget> = listOf(
            DemoTarget(1, "Alpha", distance = 12.5f, radius = 8f),
            DemoTarget(2, "Bravo", distance = 48.0f, radius = 24f),
            DemoTarget(3, "Charlie", distance = 120.0f, radius = 60f),
            DemoTarget(4, "Delta", distance = 300.0f, radius = 140f),
        )
    }
}
