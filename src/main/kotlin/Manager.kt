package nl.jordyversmissen.kocam

import nl.jordyversmissen.kocam.imagedifference.ImageDifferenceFactory
import nl.jordyversmissen.kocam.imagesource.ImageSource
import org.slf4j.Logger
import java.awt.image.BufferedImage
import java.io.File
import java.time.LocalDateTime
import java.util.*
import kotlin.concurrent.thread
import kotlin.concurrent.timer

class Manager(
    private val imageSource: ImageSource,
    private val imageDifferenceFactory: ImageDifferenceFactory,
    private val logger: Logger,
    private val options: ManagerOptions
) {
    private var timer: Timer? = null
    private var previousImage: BufferedImage? = null

    private fun handleTimerTick() {
        val image = imageSource.retrieveImage()
        previousImage?.let { previousImage ->
            val imageDifference = imageDifferenceFactory.create(previousImage, image)
            val differencePercentage = imageDifference.getDifferencePercentage()

            logger.debug("Calculated difference percentage of {}", differencePercentage)

            if (differencePercentage > options.alertThreshold) {
                thread(true, true, null, "image save") {
                    val outputFile = File(options.dataDirectory + "/" + LocalDateTime.now() + ".jpeg")
                    imageDifference.saveDifferenceImage(outputFile)
                    logger.info("Saved difference image to {}", outputFile.absolutePath)
                }
            }
        }
        previousImage = image
    }

    fun start() {
        logger.info("Start manager with options {}", options)

        timer?.cancel()
        timer = timer("image refresh timer", false, 0L, options.interval) {
            handleTimerTick()
        }
    }
}
