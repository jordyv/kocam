package nl.jordyversmissen.kocam.storage

import java.awt.image.BufferedImage
import java.io.File

interface ImageStorageAdapter {
    fun store(image: BufferedImage, file: File)
}
