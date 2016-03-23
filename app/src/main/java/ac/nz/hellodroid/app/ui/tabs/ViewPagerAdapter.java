package ac.nz.hellodroid.app.ui.tabs;

import ac.nz.hellodroid.app.ui.view.CanvasView;
import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Dale on 22/03/16.
 * <p/>
 * An adapter for a view pager, used to swipe between multiple different views.
 */
public class ViewPagerAdapter extends PagerAdapter {
    public static final int MAX_PAGES = 4;
    private final Context c;

    public ViewPagerAdapter(Context c) {
        this.c = c;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        CanvasView cv = new CanvasView(c);
        container.addView(cv);
        return cv;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
        super.restoreState(state, loader);
    }

    @Override
    public Parcelable saveState() {
        new Parcelable() {
            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {

            }
        };
        return super.saveState();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "Canvas " + ++position;
    }

    @Override
    public int getCount() {
        return MAX_PAGES;
    }
}
