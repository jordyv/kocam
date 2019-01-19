package nl.jordyversmissen.kocam

import nl.jordyversmissen.kocam.imagedifference.ImageDifferenceFactory
import nl.jordyversmissen.kocam.imagesource.IPCameraImageSource
import org.slf4j.LoggerFactory
import java.net.URL

internal fun env(name: String, default: String = "") = System.getenv(name) ?: default
internal fun env(name: String, default: Int) = System.getenv(name).toIntOrNull() ?: default
internal fun env(name: String, default: Float) = System.getenv(name).toFloatOrNull() ?: default
internal fun env(name: String, default: Long) = System.getenv(name).toLongOrNull() ?: default

val threshold: Float = env("THRESHOLD", 0.1F)
val cameraImageUrl: URL = URL(env("CAMERA_IMAGE_URL", "unknown"))
val dataDirectory: String = env("DATA_DIRECTORY", "output")
val interval: Long = env("INTERVAL", 5000L)
val logLevel: String = env("LOG_LEVEL", "INFO")

fun main() {
    val logger = LoggerFactory.getLogger("kocam")

    val manager = Manager(
        IPCameraImageSource(cameraImageUrl),
        ImageDifferenceFactory(),
        logger,
        ManagerOptions(dataDirectory = dataDirectory, alertThreshold = threshold, interval = interval)
    )

    manager.start()
}
