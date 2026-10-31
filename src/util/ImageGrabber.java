package util;

import javafx.scene.image.Image;

public class ImageGrabber {
    /**
     * @param base ex: "/images/menu"
     * @param file_base_name ex: "menu"
     * @param fileType ex: ".png"
     * @return
     */
    public static Image getImage(String base,String file_base_name,String fileType) {

        
        // sending blank "" arg as path is only meant for use in copy.
        if (file_base_name != "") {
            if (!(base.endsWith("/") || base.endsWith("\\\\"))) {
                base = base + "/";
            }
            String p = base + file_base_name + fileType;
            util.Debug.print(p);
            Image im = new Image(ImageGrabber.class.getResourceAsStream(p));
            return im;
        }
        else return null;
    }
}
