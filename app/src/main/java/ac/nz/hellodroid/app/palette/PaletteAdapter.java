package ac.nz.hellodroid.app.palette;

import ac.nz.hellodroid.app.AppController;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;

/**
 * Created by Dale on 12/03/16.
 */
public class PaletteAdapter extends BaseAdapter implements View.OnClickListener {
    private final int paletteItemWidth = 60;
    private final int paletteItemHeight = 60;
    private final PaletteColor[] paletteColors;
    private ColorClickListener paletteClickListener = null;
    private Context appContext;

    public PaletteAdapter(Context appContext){
        paletteColors = new PaletteColor[]{
                new PaletteColor(Color.BLACK,paletteItemWidth,paletteItemHeight,appContext),
                new PaletteColor(Color.BLUE,paletteItemWidth,paletteItemHeight,appContext),
                new PaletteColor(Color.CYAN,paletteItemWidth,paletteItemHeight,appContext),
                new PaletteColor(Color.GRAY,paletteItemWidth,paletteItemHeight,appContext),
                new PaletteColor(Color.GREEN,paletteItemWidth,paletteItemHeight,appContext),
                new PaletteColor(Color.MAGENTA,paletteItemWidth,paletteItemHeight,appContext),
                new PaletteColor(Color.MAGENTA,paletteItemWidth,paletteItemHeight,appContext),
                new PaletteColor(Color.rgb(100,210,200),paletteItemWidth,paletteItemHeight,appContext),
                new PaletteColor(Color.rgb(79,44,200),paletteItemWidth,paletteItemHeight,appContext),
        };
        this.appContext =appContext;
        for(PaletteColor p:paletteColors){p.setOnClickListener(this);}
    }

    public void setOnPaletteClickListener(ColorClickListener p){
        this.paletteClickListener = p;
    }

    @Override
    public int getCount() {
        return paletteColors.length;
    }

    @Override
    public Object getItem(int position) {
        return paletteColors[position];
    }

    @Override
    public long getItemId(int position) {
        return paletteColors[position].getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return paletteColors[position];
    }


    @Override
    public void onClick(View v) {
        if(!(v instanceof PaletteColor))
            return;

        PaletteColor p = ((PaletteColor) v);

        Log.v(AppController.LOG_TAG,"Selected new color");
        Toast.makeText(appContext,"Selected RGB color: ("+p.getRed()+","+p.getGreen()+","+p.getBlue()+")" ,Toast.LENGTH_SHORT);

        paletteClickListener.colorPicked(p.getPaletteColor());
    }
}
