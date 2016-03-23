package ac.nz.hellodroid.app;

import ac.nz.hellodroid.app.canvas.CanvasOverlay;
import ac.nz.hellodroid.app.dialog.GenericDialog;
import ac.nz.hellodroid.app.ui.palette.ColorClickListener;
import ac.nz.hellodroid.app.ui.palette.PaletteAdapter;
import ac.nz.hellodroid.app.ui.tabs.ViewPagerAdapter;
import ac.nz.hellodroid.app.ui.view.CanvasView;
import ac.nz.hellodroid.app.ui.view.NoSwipeViewPager;
import ac.nz.hellodroid.app.utils.files.BitmapFileSaveTask;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.*;
import android.widget.*;
import com.pes.androidmaterialcolorpickerdialog.ColorPicker;

/**
 * The type Main activity.
 */
public class AppController extends Activity {
    /**
     * The constant LOG_TAG.
     */
    public static final String LOG_TAG = "main_activity";
    private final ColorClickListener colorPickerListener = new ColorClickListener() {
        @Override
        public void colorPicked(int color) {
            (findViewById(R.id.paletteColor)).setBackgroundColor(color);
            CanvasView.getPaint().setColor(color);
        }

        @Override
        public int currentColor() {
            return CanvasView.getPaint().getColor();
        }
    };
    private final Button.OnClickListener clearButtonOnClickListnener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };
    private final Button.OnClickListener selectionButtonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };
    private final Button.OnClickListener shapeButtonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            GenericDialog.showDialog(AppController.this, R.layout.activity_main, "Shape button title", "Select the shape you would like to draw", "Select", "cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
        }
    };
    private final Button.OnClickListener eraserOnClickButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
        }
    };
    /**
     * The canvasView.
     */
    private CanvasView c;
    private final Button.OnClickListener undoButtonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            c.undo();
        }
    };

    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Create a new canvas view
        c = new CanvasView(this);

        //Create the palette adapter
        PaletteAdapter paletteAdapter = new PaletteAdapter(this);
        paletteAdapter.setOnPaletteClickListener(colorPickerListener);

        //Set the adapter for the grid view
        ((GridView) findViewById(R.id.gridView)).setAdapter(paletteAdapter);
        ((GridView) findViewById(R.id.gridView)).setGravity(GridView.STRETCH_COLUMN_WIDTH);
        ((GridView) findViewById(R.id.gridView)).setVerticalSpacing(40);

        ViewPager v = new NoSwipeViewPager(this);
        v.setOffscreenPageLimit(ViewPagerAdapter.MAX_PAGES);
        v.setAdapter(new ViewPagerAdapter(this));

        TabLayout t = new TabLayout(this);
        t.setupWithViewPager(v);
        t.setTabMode(TabLayout.MODE_FIXED);
        t.setTabTextColors(Color.BLUE, Color.MAGENTA);

        RelativeLayout r = (RelativeLayout) findViewById(R.id.relativeLayout);

        //Create layout params for tab view
        RelativeLayout.LayoutParams tabParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tabParams.addRule(RelativeLayout.BELOW, R.id.relativeLayout2);
        tabParams.addRule(RelativeLayout.RIGHT_OF, R.id.sidePanel);
        tabParams.addRule(RelativeLayout.ABOVE, v.getId());

        //Create layout params for view pager
        RelativeLayout.LayoutParams pagerLayout = createActivityLayout();
        pagerLayout.setMargins(0, 190, 0, 0);

        //Add tab view and pager to relative layout
        r.addView(t, tabParams);
        r.addView(v, pagerLayout);

        //Set up button listeners
        ImageButton saveButton = (ImageButton) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new SaveButtonClickListener());

        //ImageButton eraser = (ImageButton) findViewById(R.id.eraser);
        //eraser.setOnClickListener(c);

        ImageButton colorPicker = (ImageButton) findViewById(R.id.colorPicker);
        colorPicker.setOnClickListener(new ColorPickerListener(this, colorPickerListener));

        ImageButton shapeButton = (ImageButton) findViewById(R.id.shapeButton);
        shapeButton.setOnClickListener(shapeButtonOnClickListener);

        ImageButton selectionButton = (ImageButton) findViewById(R.id.selectionButton);
        selectionButton.setOnClickListener(selectionButtonOnClickListener);

        Button undoButton = (Button) findViewById(R.id.undoButton);
        undoButton.setOnClickListener(undoButtonOnClickListener);

        findViewById(R.id.paletteColor).setBackgroundColor(CanvasView.getPaint().getColor());

        //Set up canvas overlay
        View canvasOverlayView = LayoutInflater.from(this).inflate(R.layout.activity_main, null);
        ((RelativeLayout) canvasOverlayView.findViewById(R.id.relativeLayout)).addView(new CanvasOverlay(c, this), createActivityLayout());
        canvasOverlayView.findViewById(R.id.sidePanel).setEnabled(false);
        canvasOverlayView.findViewById(R.id.sidePanel).setVisibility(View.INVISIBLE);
        canvasOverlayView.setBackgroundColor(Color.TRANSPARENT);
        addContentView(canvasOverlayView, createActivityLayout());
        canvasOverlayView.setVisibility(View.INVISIBLE);
    }

    /**
     * Creates the relative layout params for this activity.
     *
     * @return {@link android.view.ViewGroup.MarginLayoutParams} with rules specific to the layout of this activity.
     */
    protected RelativeLayout.LayoutParams createActivityLayout() {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.RIGHT_OF, R.id.sidePanel);
        layoutParams.addRule(RelativeLayout.ABOVE, R.id.bToolbar);
        layoutParams.addRule(RelativeLayout.BELOW, R.id.relativeLayout2);
        return layoutParams;
    }

    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * @class ColorPickerListener
     * A listener for a color picker button.
     */
    private static final class ColorPickerListener implements View.OnClickListener {
        private final Activity a;
        private final ColorClickListener listener;

        /**
         * Instantiates a new Color picker listener.
         *
         * @param a        the a
         * @param listener the listener
         */
        public ColorPickerListener(Activity a, ColorClickListener listener) {
            this.a = a;
            this.listener = listener;
        }

        @Override
        public void onClick(View v) {
            //32 bit ARGB color
            int currentColor = listener.currentColor();
            int[] a = argbToRGB(currentColor);

            assert a.length == 3;

            final ColorPicker cp = new ColorPicker(this.a, a[0], a[1], a[2]);
            cp.show();
            cp.findViewById(R.id.okColorButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.colorPicked(cp.getColor());
                    cp.dismiss();
                }
            });
        }

        /**
         * Takes a 32 bit ARGB value and retrieves the RGB components, and returns them in an int array.
         *
         * @param currentColor the 32bit ARGB value
         * @return an int[] consisting of the RGB components of the 32 bit ARGB value.
         */
        private final int[] argbToRGB(int currentColor) {
            int[] rgb = new int[3];

            //Shift each color to the least significant byte and mask out the higher bits (ignore the alpha channel)
            rgb[0] = 0x000000FF & (currentColor >> 16);
            rgb[1] = 0x000000FF & (currentColor >> 8);
            rgb[2] = 0x000000FF & (currentColor);

            return rgb;
        }
    }

    private final class SaveButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            GenericDialog.showDialog(AppController.this, R.layout.save_dialog, getResources().getString(R.string.saveDialogTitle), "", getResources().getString(R.string.saveButton), getResources().getString(R.string.clearButton), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String fileName = ((EditText) ((AlertDialog) dialog).findViewById(R.id.fileEntry)).getText().toString();
                    String suffix = ((Spinner) ((AlertDialog) dialog).findViewById(R.id.spinner)).getSelectedItem().toString();
                    if (isExternalStorageWritable() && fileName.length() != 0) {
                        saveBitmap(fileName, suffix);
                    } else if (!isExternalStorageWritable()) {
                        Toast.makeText(AppController.this, getString(R.string.saveFileErrorTwo), Toast.LENGTH_LONG).show();
                    } else {
                        ((AlertDialog) dialog).setMessage("Enter valid name");
                    }
                }
            });
        }

        /**
         * Saves a bitmap with the given name and suffix.
         *
         * @return void
         */
        private void saveBitmap(String fileName, String suffix) {
            new BitmapFileSaveTask(c.getcBitmap(), Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath(),
                    fileName, suffix) {
                @Override
                protected void onSaveAttempted(final boolean success) {
                    //Post notification on UI thread
                    AppController.this.c.post(new Runnable() {
                        @Override
                        public void run() {
                            if (!success) {
                                Toast.makeText(getApplicationContext(), getString(R.string.saveFileError), Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getApplicationContext(), getString(R.string.saveFileSuccess), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }.saveAsync();
        }

        /**
         * Is external storage writable boolean.
         *
         * @return the boolean specifying whether it is possible to write to external storage.
         */
        private boolean isExternalStorageWritable() {
            String state = Environment.getExternalStorageState();
            return Environment.MEDIA_MOUNTED.equals(state);
        }
    }
}
