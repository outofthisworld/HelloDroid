package ac.nz.hellodroid.app.ui.view;

import ac.nz.hellodroid.app.canvas.drawable.DrawableObjectType;
import ac.nz.hellodroid.app.canvas.drawable.IDrawableItem;
import ac.nz.hellodroid.app.functional.Consumer;
import android.content.Context;
import android.graphics.*;
import android.util.DisplayMetrics;
import android.view.InputEvent;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import java.nio.Buffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by Dale on 12/03/16.
 */
public class CanvasView extends View implements ScaleGestureDetector.OnScaleGestureListener, View.OnClickListener {

    /**
     * The current paint object used to draw objects onto the canvas.
     */
    private static final Paint paint = new Paint();
    /**
     * The draw queue containing CanvasDrawEvents which is added to via various input events encapsulating information to
     * draw an object onto the canvas.
     */
    private final Queue<CanvasDrawAction> drawQueue = new ArrayBlockingQueue<>(10);
    /**
     * The IDrawableItem to be drawn to this canvas. @see IDrawableItem.
     */
    private IDrawableItem currentDrawableObjectType = DrawableObjectType.FREE_HAND;
    /**
     * The CanvasDrawAction that encapsulates information from various input events.
     */
    private CanvasDrawAction drawAction = null;

    /**
     * The ScaleGestureDetector used to scale this canvas.
     */
    private ScaleGestureDetector scaleGestureDetector;

    /**
     * The current bitmap that is being drawn to before being drawn onto this views canvas.
     */
    private Bitmap cBitmap;

    /**
     * The cached previous bitmap before any changes were made the the canvas, used to undo draw events
     */
    private Bitmap pBitmap;

    /**
     * The canvas used to draw onto the {@link CanvasView#cBitmap}
     */
    private Canvas c;


    private int scaleFactor = 1;

    /**
     * Instantiates a new Canvas view.
     *
     * @param context the context
     */
    public CanvasView(Context context) {
        super(context);
        setOnTouchListener(new CanvasOnTouchListener());
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(20);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        scaleGestureDetector = new ScaleGestureDetector(context, this);
    }

    public static Paint getPaint() {
        return paint;
    }

    public Canvas getC() {
        return c;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //Save a copy of the previous version of the bitmap for undo
        pBitmap = Bitmap.createScaledBitmap(cBitmap, cBitmap.getWidth(), cBitmap.getHeight(), false);

        CanvasDrawAction item;

        // The queue allows a number of Canvas draw actions to queue up before a call to invalidate
        // in which case they are drawn as a unit.
        while ((item = drawQueue.poll()) != null) {
            currentDrawableObjectType.draw(c, item, paint);
        }

        canvas.scale(scaleFactor, scaleFactor);
        canvas.drawBitmap(cBitmap, 0, 0, paint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        //Create the bitmap used for drawing operations
        cBitmap = Bitmap.createBitmap(new DisplayMetrics(), w, h, Bitmap.Config.ARGB_8888);

        //A new canvas drawing onto the newly created bitmap
        c = new Canvas(cBitmap);
    }

    /**
     * Add pixels to backing bitmap buffer.
     *
     * @param b the b
     */
    public void addPixelsToBackingBitmapBuffer(Buffer b) {
        cBitmap.copyPixelsFromBuffer(b);
    }

    /**
     * Gets bitmap.
     *
     * @return the bitmap
     */
    public Bitmap getpBitmap() {
        return pBitmap;
    }

    /**
     * Undo.
     */
    public void undo() {
        if (getpBitmap() != null)
            setcBitmap(getpBitmap());
    }

    /**
     * returns the bitmap that is used by the containing canvas.
     *
     * @return the bitmap drawn by this canvas.
     */
    public Bitmap getcBitmap() {
        return cBitmap;
    }

    /**
     * Setc bitmap image drawn by this canvas.
     *
     * @param cBitmap the c bitmap
     */
    public void setcBitmap(Bitmap cBitmap) {
        this.cBitmap = cBitmap;
        c.setBitmap(cBitmap);

        invalidate();
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        return true;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {
    }

    @Override
    public void onClick(View v) {
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
    }

    public static final class CanvasDrawAction<T extends InputEvent> {
        private final Path path;
        private final Path pointerPath;
        private final float srcX;
        private final float srcY;
        private float currentX;
        private float currentY;
        private boolean isFinished = false;
        private T inputEvent = null;
        private HashMap<Integer, PointF> pointerCoords = new HashMap<>(10, 10);

        public CanvasDrawAction(float srcX, float srcY) {
            this.srcX = srcX;
            this.srcY = srcY;
            this.currentX = srcX;
            this.currentY = srcY;
            this.path = new Path();
            this.pointerPath = new Path();
            this.path.moveTo(srcX, srcY);
        }

        public Path getSecondaryInputPath() {return pointerPath;}

        public float getSrcX() {
            return srcX;
        }

        public float getSrcY() {
            return srcY;
        }

        public float getCurrentX() {
            return currentX;
        }

        public void setCurrentX(float currentX) {
            this.currentX = currentX;
        }

        public float getCurrentY() {
            return currentY;
        }

        public void setCurrentY(float currentY) {
            this.currentY = currentY;
        }

        public int getMultiTouchCount() {
            return pointerCoords == null ? 0 : pointerCoords.size();
        }

        public boolean isFinished() {
            return isFinished;
        }

        void setFinished(boolean finished) {
            isFinished = finished;
        }

        public void addPointer(int i, PointF p) {
            this.pointerCoords.put(i, p);
        }

        public void forEachPointer(Consumer<Map.Entry<Integer, PointF>> c) {
            for (Map.Entry<Integer, PointF> p : pointerCoords.entrySet()) {
                c.consume(p);
            }
        }

        public T getInputEvent() {
            return inputEvent;
        }

        public void setInputEvent(T inputEvent) {
            this.inputEvent = inputEvent;
        }

        public Path getDrawPath() {
            return path;
        }
    }

    private final class CanvasOnTouchListener implements OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            scaleGestureDetector.onTouchEvent(event);

            handle(event);
            conditionalAddToDrawQueue(drawAction, event.getAction());
            v.invalidate();

            drawAction.setCurrentX(event.getX());
            drawAction.setCurrentY(event.getY());

            return true;
        }

        public void handle(MotionEvent event) {
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                //User touches the screen
                case MotionEvent.ACTION_POINTER_DOWN: {
                    getMultiTouchCoordinates(event);
                    break;
                }
                case MotionEvent.ACTION_DOWN: {
                    paint.setStrokeWidth(event.getPressure() * 40);
                    touchStart(event);
                    break;
                }
                //User moves their pointer position whilst still holding down
                case MotionEvent.ACTION_MOVE: {
                    touchMove(event);
                    break;
                }
                //One or more points has gone up
                case MotionEvent.ACTION_POINTER_UP:
                case MotionEvent.ACTION_UP: {
                    touchEnd(event);
                    break;
                }
            }
        }

        public void touchStart(MotionEvent event) {
            drawAction = new CanvasDrawAction(event.getX(), event.getY());
        }

        public void touchMove(final MotionEvent event) {
            constructDrawActionPath(drawAction.getDrawPath(), drawAction.getCurrentX(), drawAction.getCurrentY(), event.getX(), event.getY());
            drawAction.forEachPointer(new Consumer<Map.Entry<Integer, PointF>>() {
                @Override
                public void consume(Map.Entry<Integer, PointF> p) {
                    PointF origCoords = p.getValue();
                    MotionEvent.PointerCoords newCoords = new MotionEvent.PointerCoords();
                    int pointerIndex = event.findPointerIndex(p.getKey());

                    if (pointerIndex == -1)
                        return;

                    event.getPointerCoords(pointerIndex, newCoords);
                    constructDrawActionPath(drawAction.getSecondaryInputPath(),
                            origCoords.x, origCoords.y, newCoords.x, newCoords.y);
                    drawAction.addPointer(event.getPointerId(pointerIndex), new PointF(newCoords.x, newCoords.y));
                }
            });
        }

        public void constructDrawActionPath(Path p, float origX, float origY, float newX, float newY) {
            p.moveTo(origX, origY);
            p.quadTo(origX, origY, newX, newY);
            p.lineTo(newX, newY);
        }

        public void touchEnd(final MotionEvent event) {
            drawAction.setFinished(true);
        }

        public void conditionalAddToDrawQueue(CanvasDrawAction action, int actionType) {
            if (currentDrawableObjectType == DrawableObjectType.LINE && actionType == MotionEvent.ACTION_MOVE)
                return;

            drawQueue.add(drawAction);
        }

        public void getMultiTouchCoordinates(MotionEvent e) {
            for (int i = 0; i < e.getPointerCount(); i++) {
                MotionEvent.PointerCoords p = new MotionEvent.PointerCoords();
                e.getPointerCoords(i, p);
                drawAction.addPointer(e.getPointerId(i), new PointF(p.x, p.y));
            }
        }

    }
}
