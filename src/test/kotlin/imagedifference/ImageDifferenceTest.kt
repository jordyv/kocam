package imagedifference

import nl.jordyversmissen.kocam.imagedifference.ImageDifference
import org.junit.jupiter.api.Test
import java.io.File
import javax.imageio.ImageIO

class ImageDifferenceTest {
    @Test
    fun `test picture output`() {
        val firstImage = ImageIO.read(File("./test/images/1.jpg"))
        val secondImage = ImageIO.read(File("./test/images/2.jpg"))
        val difference = ImageDifference(firstImage, secondImage)
        difference.saveDifferenceImage(File("./output/test_difference_picture.jpg"))
        difference.saveComparisonImage(File("./output/test_comparison_picture.jpg"))
    }
}
