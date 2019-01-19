package nl.jordyversmissen.kocam.imagesource

import java.awt.image.BufferedImage
import java.net.URL
import javax.imageio.ImageIO

class IPCameraImageSource(private val imageUrl: URL) : ImageSource {
    override fun retrieveImage(): BufferedImage {
        return ImageIO.read(imageUrl)
    }
}
