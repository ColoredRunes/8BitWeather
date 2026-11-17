package util;

import javafx.scene.image.Image;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.InputStream;

public class ImageGrabber {
    /**
     * @param base ex: "/images/menu"
     * @param file_base_name ex: "menu"
     * @param fileType ex: ".png"
     * @return
     */
    public static javafx.scene.image.Image getImage(String base, String file_base_name, String fileType) {
        // sending blank "" arg as path is only meant for use in copy.
        if (file_base_name != null && !file_base_name.isEmpty()) {
            if (!(base.endsWith("/") || base.endsWith("\\"))) {
                base = base + "/";
            }
            String p = base + file_base_name + fileType;
            util.Debug.println(p);
            Image im = new Image(ImageGrabber.class.getResourceAsStream(p));
            return im;
        }
        return null;
    }

    public static java.awt.Image getImageAwt(String base, String file_base_name, String fileType) {
        // sending blank "" arg as path is only meant for use in copy.
        if (file_base_name != null && !file_base_name.isEmpty()) {
            if (!(base.endsWith("/") || base.endsWith("\\"))) {
                base = base + "/";
            }
            String p = base + file_base_name + fileType;
            util.Debug.println(p);

            try (InputStream is = ImageGrabber.class.getResourceAsStream(p)) {
                if (is == null) {
                    util.Debug.println("Resource not found: " + p);
                    return null;
                }
                return ImageIO.read(is);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }
}
