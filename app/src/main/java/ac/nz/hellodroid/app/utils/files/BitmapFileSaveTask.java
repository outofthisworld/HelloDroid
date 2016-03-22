package ac.nz.hellodroid.app.utils.files;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

/**
 * Created by Dale on 17/03/16.
 */
public abstract class BitmapFileSaveTask extends FileSaveTask {
    private Bitmap bm;

    /**
     * Instantiates a new Bitmap file save task.
     *
     * @param bm         the bitmap object to save
     * @param savePath   the  file path where the bitmap will be saved.
     * @param fileName   the file name
     * @param fileSuffix the file extension, or suffix e.g png,jpeg.
     */
    public BitmapFileSaveTask(Bitmap bm, CharSequence savePath, CharSequence fileName, CharSequence fileSuffix) {
        super(savePath, fileName, fileSuffix);
        this.bm = bm;
    }

    @Override
    public ByteBuffer getFileBytes() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(bm.getRowBytes() * bm.getHeight());
        bm.compress(Bitmap.CompressFormat.valueOf(getSuffix().toString()), 100, byteArrayOutputStream);
        return ByteBuffer.wrap(byteArrayOutputStream.toByteArray());
    }
}
