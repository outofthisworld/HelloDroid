package ac.nz.hellodroid.app.palette;

import ac.nz.hellodroid.app.R;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

/**
 * Created by Dale on 12/03/16.
 */
public class PaletteColor extends View {
    private int color;

    public PaletteColor(int c,int paletteWidth,int paletteHeight, Context context) {
        super(context);
        this.color = c;
        setMinimumWidth(paletteWidth);
        setMinimumHeight(paletteHeight);
        setBackgroundColor(c);
    }

    public int getPaletteColor() {
        return this.color;
    }

    public int getRed(){
        return Color.red(color);
    }

    public int getBlue(){
        return Color.blue(color);
    }

    public int getAlphaColor(){return Color.alpha(color);}

    public int getGreen(){
        return Color.green(color);
    }
}
