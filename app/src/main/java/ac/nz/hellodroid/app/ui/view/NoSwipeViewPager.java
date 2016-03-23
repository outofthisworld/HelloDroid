package ac.nz.hellodroid.app.ui.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Dale on 23/03/16.
 * <p/>
 * Makes swipe inactive on a view pager.
 * <p/>
 * Overrides {@link ViewPager#onInterceptTouchEvent(MotionEvent)}
 * and {@link ViewPager#onTouchEvent(MotionEvent)} and provides no implementation.
 */
public class NoSwipeViewPager extends ViewPager {

    public NoSwipeViewPager(Context context) {
        super(context);
    }

    public NoSwipeViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        // Never allow swiping to switch between pages
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Never allow swiping to switch between pages
        return false;
    }
}
