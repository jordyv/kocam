package nl.jordyversmissen.kocam.imagesource

import java.awt.image.BufferedImage

interface ImageSource {
    fun retrieveImage(): BufferedImage
}
