package util;

import javafx.scene.image.Image;

public class ImageGrabber {

    public static Image getImage(String base,String file_base_name,String fileType) {

        
        // sending blank "" arg as path is only meant for use in copy.
        if (file_base_name != "") {
            if (!(base.endsWith("/") || base.endsWith("\\\\"))) {
                base = base + "/";
            }
            String p = base + file_base_name + fileType;
            Image im = new Image(ImageGrabber.class.getResourceAsStream(p));
            return im;
        }
        else return null;
    }
}
