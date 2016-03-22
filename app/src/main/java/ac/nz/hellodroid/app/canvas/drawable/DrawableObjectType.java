package ac.nz.hellodroid.app.canvas.drawable;

import ac.nz.hellodroid.app.canvas.CanvasView;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;

/**
 * Created by Dale on 16/03/16.
 *
 * Represents a drawable object type that can be drawn onto a canvas.
 */
public enum DrawableObjectType implements IDrawableItem<Canvas,CanvasView.CanvasDrawAction<MotionEvent>,Paint> {
    /**
     * The Line.
     */
    LINE {
        @Override
        public void draw(Canvas c, CanvasView.CanvasDrawAction event, Paint p) {
            c.drawLine(event.getSrcX(),event.getSrcY(),event.getCurrentX(),event.getCurrentY(),p);
        }
    },
    /**
     * The Rectangle.
     */
    RECTANGLE {
        @Override
        public void draw(Canvas c, CanvasView.CanvasDrawAction event, Paint p) {
            //p.setStyle(Paint.Style.FILL);
            c.drawRect(event.getSrcX(),event.getSrcY(),event.getCurrentX(),event.getCurrentY(),p);
        }
    },
    /**
     * The Triangle.
     */
    TRIANGLE {
        @Override
        public void draw(Canvas c, CanvasView.CanvasDrawAction event, Paint p) {
        }
    },
    /**
     * Draws a circle on the canvas uses the cartesian distance between center x,y and the drag x,y
     * as the radius of the circle to make its size changeable upon drag.
     */
    CIRCLE {
        @Override
        public void draw(Canvas c, CanvasView.CanvasDrawAction event, Paint p) {
          //  p.setStyle(Paint.Style.FILL);
            c.drawCircle(event.getSrcX(),event.getSrcY(),
                    (float) Math.sqrt(Math.pow(event.getCurrentX()-event.getSrcX(),2)
                            + Math.pow(event.getCurrentY()-event.getCurrentY(),2)),p);
        }
    },
    /**
     * Draws free hand stokes on the canvas and handles multiple pointers
     */
    FREE_HAND {
        @Override
        public void draw(Canvas c, final CanvasView.CanvasDrawAction<MotionEvent> drawAction, Paint p) {
                c.drawPath(drawAction.getDrawPath(), p);
                c.drawPath(drawAction.getSecondaryInputPath(), p);

        }
    }
}