package nl.jordyversmissen.kocam.storage

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

class BaseImageStorageAdapter : ImageStorageAdapter {
    override fun store(image: BufferedImage, file: File) {
        ImageIO.write(image, "jpeg", file)
    }
}
