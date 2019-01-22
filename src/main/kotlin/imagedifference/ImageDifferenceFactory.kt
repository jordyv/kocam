package nl.jordyversmissen.kocam.imagedifference

import nl.jordyversmissen.kocam.storage.ImageStorageAdapter
import java.awt.image.BufferedImage

class ImageDifferenceFactory(private val imageStorageAdapter: ImageStorageAdapter) {
    fun create(firstImage: BufferedImage, secondImage: BufferedImage): ImageDifference =
        ImageDifference(imageStorageAdapter, firstImage, secondImage)
}
