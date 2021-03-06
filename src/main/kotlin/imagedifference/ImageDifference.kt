package nl.jordyversmissen.kocam.imagedifference

import nl.jordyversmissen.kocam.storage.ImageStorageAdapter
import java.awt.Color
import java.awt.Font
import java.awt.image.BandedSampleModel
import java.awt.image.BufferedImage
import java.awt.image.WritableRaster
import java.io.File

class ImageDifference(
    private val imageStorageAdapter: ImageStorageAdapter,
    private val firstImage: BufferedImage,
    private val secondImage: BufferedImage
) {
    /**
     * Cached storage for images difference matrix
     */
    private val differenceMatrix: Array<IntArray> by lazy {
        getDifferenceMatrix(firstImage, secondImage)
    }

    /**
     * Threshold for pixel difference calculation.
     * Pixel is flagged as different when the result of the algorithm is more than this value.
     */
    private val pixelDifferenceThreshold: Float = 0.2F

    /**
     * Says if the two pixels equal or not. The rule is the difference between two pixels
     * need to be more then x%.
     * @param rgb1 the RGB value of the Pixel of the Image1.
     * @param rgb2 the RGB value of the Pixel of the Image2.
     * @return {@code true} if they' are difference, {@code false} otherwise.
     */
    private fun isDifferentPixel(rgb1: Int, rgb2: Int): Boolean {
        val red1 = rgb1 shr 16 and 0xff
        val green1 = rgb1 shr 8 and 0xff
        val blue1 = rgb1 and 0xff
        val red2 = rgb2 shr 16 and 0xff
        val green2 = rgb2 shr 8 and 0xff
        val blue2 = rgb2 and 0xff
        val result = (Math.sqrt(
            Math.pow((red2 - red1).toDouble(), 2.0) +
                    Math.pow((green2 - green1).toDouble(), 2.0) +
                    Math.pow((blue2 - blue1).toDouble(), 2.0)
        ) / Math.sqrt(Math.pow(255.0, 2.0) * 3))
        return result > pixelDifferenceThreshold
    }

    /**
     * Get binary matrix with "0" and "1" for every pixel. If the pixels are difference set it as "1", otherwise "0".
     * @param image1 `BufferedImage` object of the first image.
     * @param image2 `BufferedImage` object of the second image.
     * @return populated binary matrix.
     */
    private fun getDifferenceMatrix(image1: BufferedImage, image2: BufferedImage): Array<IntArray> {
        val matrix = Array(image1.width) { IntArray(image1.height) }
        for (y in 0 until image1.height) {
            for (x in 0 until image1.width) {
                matrix[x][y] = if (isDifferentPixel(image1.getRGB(x, y), image2.getRGB(x, y))) 1 else 0
            }
        }
        return matrix
    }

    /**
     * Get percentage of different pixels between the two images.
     * @return percentage
     */
    fun getDifferencePercentage(): Float {
        var totalCells = 0F
        var differenceCount = 0F

        for (row in differenceMatrix) {
            totalCells += row.count()
            differenceCount += row.filter { it != 0 }.count()
        }

        return (100 * differenceCount) / totalCells
    }

    private fun createDifferenceImage(): BufferedImage {
        val newImage = BufferedImage(
            firstImage.colorModel,
            firstImage.copyData(null),
            firstImage.colorModel.isAlphaPremultiplied,
            null
        )
        val graphics = newImage.createGraphics()
        graphics.color = Color(255, 0, 0, 50)

        for (x in 0..(differenceMatrix.size - 1)) {
            for (y in 0..(differenceMatrix[x].size - 1)) {
                if (differenceMatrix[x][y] != 0) {
                    graphics.drawRect(x, y, 1, 1)
                }
            }
        }

        graphics.apply {
            this.color = Color.WHITE
            this.font = Font("Heltevica", Font.BOLD, 16)
        }
        graphics.drawString(
            String.format("%.4f%%", getDifferencePercentage()),
            firstImage.data.width - 100,
            firstImage.data.height - 30
        )

        return newImage
    }

    private fun createComparisonImage(): BufferedImage {
        val sampleModel = BandedSampleModel(
            secondImage.sampleModel.dataType,
            secondImage.sampleModel.width * 3,
            secondImage.sampleModel.height,
            secondImage.sampleModel.numBands
        )
        val newImage = BufferedImage(
            secondImage.colorModel,
            WritableRaster.createWritableRaster(sampleModel, null),
            secondImage.colorModel.isAlphaPremultiplied,
            null
        )
        val graphics = newImage.createGraphics()
        graphics.drawImage(firstImage, 0, 0, null)
        graphics.drawImage(createDifferenceImage(), firstImage.data.width, 0, null)
        graphics.drawImage(secondImage, firstImage.data.width * 2, 0, null)

        return newImage
    }

    /**
     * Save a new image with all differences between the two images highlighted with a red rectangle.
     * @param outputFile file where to save the image
     */
    fun saveDifferenceImage(outputFile: File) {
        imageStorageAdapter.store(createDifferenceImage(), outputFile)
    }

    /**
     * Save a image with the first, difference and second image side by side
     * @param outputFile file where to save the image
     */
    fun saveComparisonImage(outputFile: File) {
        imageStorageAdapter.store(createComparisonImage(), outputFile)
    }
}
