package ac.nz.hellodroid.app.palette;

/**
 * Created by Dale on 12/03/16.
 */
public interface ColorClickListener {
     /**
      * Triggered when a color is picked via the observer.
      *
      * @param color the color that was clicked.
      */
     void colorPicked(int color);

     /**
      * Returns the current color within the implementing class before the listener is triggered.
      *
      * @return a 32 bit ARGB int color.
      */
     int currentColor();
}
