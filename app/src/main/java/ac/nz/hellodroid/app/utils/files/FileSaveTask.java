package ac.nz.hellodroid.app.utils.files;

import ac.nz.hellodroid.app.AppController;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created by Dale on 17/03/16.
 */
public abstract class FileSaveTask {
    /**
     * The path to save the file to.
     */
    private CharSequence path;
    /**
     * The name of the file.
     */
    private CharSequence fileName;
    /**
     * The file suffix, e.g png.
     */
    private CharSequence suffix;

    /**
     * The constant FILE_SAVE_LOG_TAG used for logging.
     */
    public static final String FILE_SAVE_LOG_TAG = "fs";

    /**
     * The default uncaught exception handler.
     */
    private Thread.UncaughtExceptionHandler asyncThreadExceptionHandler = new Thread.UncaughtExceptionHandler() {
        @Override
        public void uncaughtException(Thread thread, Throwable ex) {
            onSaveAttempted(false);
        }
    };

    /**
     * Instantiates a new File save task.
     *
     * A file save task is responsible for saving a file to the underlying storage medium and creating the file if necessary.
     *
     * The file can either be saved via the calling thread in which case the calling thread will block until the file has been saved. Alternatively
     * the file can be saved asynchronously and the calling thread continue. Once a call to one of the save methods is called, the onSaveAttempted
     * method is invoked detailing the outcome of the attempted save.
     *
     * If this class is used for asynchronous saving, the invokee may wish to set the {@link java.lang.Thread.UncaughtExceptionHandler#} via the method
     * {@link FileSaveTask#setAsyncThreadExceptionHandler(Thread.UncaughtExceptionHandler)} to handle
     * runtime exceptions within the thread. By default, it is set to call @method onSaveCompleted with false as the parameter.
     *
     * @param savePath   the save path
     * @param fileName   the file name
     * @param fileSuffix the file suffix
     */
    public FileSaveTask(CharSequence savePath, CharSequence fileName, CharSequence fileSuffix){
        this.path = savePath;
        this.fileName = fileName;
        this.suffix = fileSuffix;
    }

    /**
     * Save.
     *
     * @throws IOException the io exception
     */
    public void save() throws IOException {
        File f;

        if((f = createFile()) == null) {
            Log.v(FILE_SAVE_LOG_TAG," Error when creating file. " + fileName);
            onSaveAttempted(false);
        }

      
        try(FileOutputStream fos = new FileOutputStream(f)){
            fos.getChannel().write(getFileBytes());
            fos.flush();
        }
        
        onSaveAttempted(true);
    }

    /**
     * Attempts to Asnychonously write the given bytes to the disk and then informs the invoker of
     * the outcome via the method {@link FileSaveTask#onSaveAttempted(boolean)}
     */
    public void saveAsync(){
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    save();
                } catch (IOException e) {
                    Log.v(AppController.LOG_TAG,e.getMessage());
                    throw new RuntimeException();
                }
            }
        });
        t.setUncaughtExceptionHandler(asyncThreadExceptionHandler);
        t.start();
    }

    /**
     * Create file file.
     *
     * @return the file
     */
    protected File createFile(){
        File file = new File(path+File.separator+fileName.toString()+ "." + suffix);

        Log.v(AppController.LOG_TAG,"Creating file: " + file.getAbsolutePath());
        if(file.exists())
            file.delete();

        try {
            if(!file.createNewFile())
                return null;

            file.setExecutable(true);
            file.setReadable(true);
            file.setWritable(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * Gets suffix.
     *
     * @return the suffix
     */
    public CharSequence getSuffix() {
        return suffix;
    }

    /**
     * Gets path.
     *
     * @return the path
     */
    public CharSequence getPath() {
        return path;
    }

    /**
     * Gets file name.
     *
     * @return the file name
     */
    public CharSequence getFileName() {
        return fileName;
    }

    /**
     * Sets path.
     *
     * @param path the path
     */
    public void setPath(CharSequence path) {
        this.path = path;
    }

    /**
     * Sets file name.
     *
     * @param fileName the file name
     */
    public void setFileName(CharSequence fileName) {
        this.fileName = fileName;
    }

    /**
     * Sets suffix.
     *
     * @param suffix the suffix
     */
    public void setSuffix(CharSequence suffix) {
        this.suffix = suffix;
    }

    /**
     * Set async thread exception handler.
     *
     * @param handler the handler
     */
    public void setAsyncThreadExceptionHandler(Thread.UncaughtExceptionHandler handler){
        this.asyncThreadExceptionHandler = handler;
    }

    /**
     * Gets file bytes.
     *
     * @return the file bytes
     */
    public abstract ByteBuffer getFileBytes();

    /**
     * On save attempted.
     *
     * @param success the success
     */
    protected abstract void onSaveAttempted(boolean success);
}
