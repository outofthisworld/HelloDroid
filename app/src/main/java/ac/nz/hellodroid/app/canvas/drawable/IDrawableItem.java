package ac.nz.hellodroid.app.canvas.drawable;

import ac.nz.hellodroid.app.ui.view.CanvasView;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.InputEvent;

/**
 * The interface IDrawableItem.
 *
 * @param <T> the Canvas object to be drawn to.
 * @param <U> the @class CanvasDrawAction specifying the required details to draw the primitive.
 * @param <V> the paint object used to draw the primitive.
 */
public interface IDrawableItem<T extends Canvas,U extends CanvasView.CanvasDrawAction<? extends InputEvent>,V extends Paint> {
    /**
     * Draw.
     *
     * @param c     the canvas to be drawn to.
     * @param event the CanvasDrawAction specifying the drawing details.
     * @param paint the paint object used to draw the primitive.
     */
    void draw(T c, U event, V paint);
}
