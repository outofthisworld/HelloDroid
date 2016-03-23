package ac.nz.hellodroid.app.canvas;

import ac.nz.hellodroid.app.AppController;
import ac.nz.hellodroid.app.ui.view.CanvasView;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Dale on 22/03/16.
 */
public class CanvasOverlay extends View implements View.OnTouchListener,GestureDetector.OnGestureListener,GestureDetector.OnDoubleTapListener {
    private final Paint p = new Paint();
    private final RectF overlay = new RectF();
    private final GestureDetector g;
    private final CanvasView c;
    private boolean moveOverlay = true;

    public CanvasOverlay(CanvasView view,Context context) {
        super(context);
        this.c = view;
        setOnTouchListener(this);
        g = new GestureDetector(getContext(),this);
        g.setOnDoubleTapListener(this);
        p.setColor(Color.BLACK);
        p.setStyle(Paint.Style.STROKE);
        setBackgroundColor(Color.argb(0x08,00,00,00));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(overlay,p);
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Log.v(AppController.LOG_TAG,"On touch canvas overlay");
        g.onTouchEvent(event);
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    overlay.left = event.getX();
                    overlay.top = event.getY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    overlay.right = event.getX();
                    overlay.bottom = event.getY();
                    break;
                case MotionEvent.ACTION_UP:
                    moveOverlay = false;
                    break;
            }
        invalidate();
        return true;
    }


    @Override
    public boolean onDown(MotionEvent e) {
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return true;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        if(!overlay.contains(e.getX(),e.getY())) {
            resetOverlay();
        }
        return true;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        return true;
    }

    public void resetOverlay(){
        Log.v(AppController.LOG_TAG,"Resetting overlay");
        moveOverlay = true;
        overlay.left = 0;
        overlay.top = 0;
        overlay.right = 0;
        overlay.bottom = 0;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return true;
    }

}


