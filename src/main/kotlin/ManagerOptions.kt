package nl.jordyversmissen.kocam

data class ManagerOptions(
    val dataDirectory: String,
    val alertThreshold: Float,
    val interval: Long = 1000L
)
