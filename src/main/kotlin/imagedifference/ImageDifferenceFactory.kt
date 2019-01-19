package nl.jordyversmissen.kocam.imagedifference

import java.awt.image.BufferedImage

class ImageDifferenceFactory {
    fun create(firstImage: BufferedImage, secondImage: BufferedImage): ImageDifference =
        ImageDifference(firstImage, secondImage)
}
