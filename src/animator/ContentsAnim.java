package animator;

import java.util.Arrays;

import javafx.scene.image.Image;

/**
 * A class that represents an animated content unit. It manages the loading of
 * images, updating of animation indices, and retrieval of the current image for
 * display. The ContentsAnim class provides methods to update the animation
 * index based on time, retrieve the current frame index, and create copies of
 * the animation unit with different starting frames.
 */
public class ContentsAnim implements Cloneable {
    public Image[] imageArray;

    // public int index;
    private class Index {
        private int current;
        private int arrLen; // ~120ms per frame
        private long speed; // ~120ms per frame
        private long nextUpdate;

        public Index(int arrLen, int start, long speed) {
            this.current = start;
            this.speed = speed; // ~120ms per frame
            this.nextUpdate = 0;
            this.arrLen = arrLen;
        }

        public void update(long now) {
            if (now >= nextUpdate) {
                current = (current + 1) % arrLen;
                nextUpdate = now + speed;
                // drawSprite();
            }
        }

        public int getCurrentFrameIndex() {
            return current;
        }
    }

    public final Index index;
    public final String fileBaseName;
    public final String path;
    private int size;

    /**
     * a constructor for the ContentsAnim class that takes in a file base name,
     * size, starting index,
     * and animation speed. It initializes the index, file base name, path, image
     * array, and size.
     * The constructor loads the images from the specified path and file base name
     * into the image array.
     * 
     * @param path           a string representing the path to the images
     * @param file_base_name a string representing the base name of the image files
     * @param size           an integer representing the number of images
     */

    public ContentsAnim(String file_base_name, int size, int startingIndex, long animSpeed) {
        this.index = new Index(size, startingIndex, animSpeed);
        this.fileBaseName = file_base_name;
        this.path = "/images";
        this.imageArray = new Image[size];
        this.size = size;

        String base = "/images";
        // sending blank "" arg as path is only meant for use in copy.
        if (file_base_name != "") {
            if (!(base.endsWith("/") || base.endsWith("\\\\"))) {
                base = base + "/";
            }
            for (int i = 0; i < size; i++) {
                String p = base + file_base_name + (i + 1) + ".png";
                Image im = new Image(
                        getClass().getResourceAsStream(p));
                imageArray[i] = im;
            }
        }
    }

    public ContentsAnim(Image[] ims, int size, int startingIndex, long animSpeed) {
        this("", size, startingIndex, animSpeed);
        this.imageArray = Arrays.copyOf(ims, ims.length);
    }

    public void updateIndex(long l) {
        if (size > 1)
            this.index.update(l);
    }

    public int getCurrentFrameIndex() {
        return index.getCurrentFrameIndex();
    }

    public Image getCurrentImage() {
        return imageArray[getCurrentFrameIndex()];
    }

    public ContentsAnim copy(int startFrame) {
        ContentsAnim c = new ContentsAnim(
                // reuse same path/base/size
                // but skip reloading images
                "", this.imageArray.length, startFrame, this.index.speed);

        // reuse the same image array (images are immutable)
        c.imageArray = this.imageArray;

        return c;
    }

}