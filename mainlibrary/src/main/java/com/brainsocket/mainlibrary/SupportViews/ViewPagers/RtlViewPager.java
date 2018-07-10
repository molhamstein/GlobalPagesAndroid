package com.brainsocket.mainlibrary.SupportViews.ViewPagers;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.os.ParcelableCompat;
import android.support.v4.os.ParcelableCompatCreatorCallbacks;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;


import com.brainsocket.mainlibrary.Enums.SwipeDirection;
import com.brainsocket.mainlibrary.R;

import java.util.HashMap;

public class RtlViewPager extends ViewPager {
    private int mLayoutDirection = ViewCompat.LAYOUT_DIRECTION_LTR;
    private HashMap<OnPageChangeListener, ReversingOnPageChangeListener> mPageChangeListeners = new HashMap<>();

    private float initialXValue;
    private SwipeDirection direction = SwipeDirection.none;

    public RtlViewPager(Context context) {
        super(context);
    }

    public RtlViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        initail(context, attrs);
    }

    public void initail(Context context, AttributeSet attrs) {
        this.direction = SwipeDirection.all;
        try {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RtlViewPager, 0, 0);
            this.direction = this.direction.setValue(a.getInt(R.styleable.RtlViewPager_SwipePager, SwipeDirection.all.getValue()));
            a.recycle();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onRtlPropertiesChanged(int layoutDirection) {
        super.onRtlPropertiesChanged(layoutDirection);
        int viewCompatLayoutDirection = layoutDirection == View.LAYOUT_DIRECTION_RTL ? ViewCompat.LAYOUT_DIRECTION_RTL : ViewCompat.LAYOUT_DIRECTION_LTR;
        if (viewCompatLayoutDirection != mLayoutDirection) {
            PagerAdapter adapter = super.getAdapter();
            int position = 0;
            if (adapter != null) {
                position = getCurrentItem();
            }
            mLayoutDirection = viewCompatLayoutDirection;
            if (adapter != null) {
                adapter.notifyDataSetChanged();
                setCurrentItem(position);
            }
        }
    }

    @Override
    public void setAdapter(PagerAdapter adapter) {
        if (adapter != null) {
            adapter = new ReversingAdapter(adapter);
        }
        super.setAdapter(adapter);
        setCurrentItem(0);
    }

    @Override
    public PagerAdapter getAdapter() {
        ReversingAdapter adapter = (ReversingAdapter) super.getAdapter();
        return adapter == null ? null : adapter.getDelegate();
    }

    private boolean isRtl() {
        return mLayoutDirection == ViewCompat.LAYOUT_DIRECTION_RTL;
    }

    @Override
    public int getCurrentItem() {
        int item = super.getCurrentItem();
        PagerAdapter adapter = super.getAdapter();
        if (adapter != null && isRtl()) {
            item = adapter.getCount() - item - 1;
        }
        return item;
    }

    @Override
    public void setCurrentItem(int position, boolean smoothScroll) {
        PagerAdapter adapter = super.getAdapter();
        if (adapter != null && isRtl()) {
            position = adapter.getCount() - position - 1;
        }
        super.setCurrentItem(position, smoothScroll);
    }

    @Override
    public void setCurrentItem(int position) {
        PagerAdapter adapter = super.getAdapter();
        if (adapter != null && isRtl()) {
            position = adapter.getCount() - position - 1;
        }
        super.setCurrentItem(position);
    }

    public static class SavedState implements Parcelable {
        private final Parcelable mViewPagerSavedState;
        private final int mLayoutDirection;

        private SavedState(Parcelable viewPagerSavedState, int layoutDirection) {
            mViewPagerSavedState = viewPagerSavedState;
            mLayoutDirection = layoutDirection;
        }

        private SavedState(Parcel in, ClassLoader loader) {
            if (loader == null) {
                loader = getClass().getClassLoader();
            }
            mViewPagerSavedState = in.readParcelable(loader);
            mLayoutDirection = in.readInt();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            out.writeParcelable(mViewPagerSavedState, flags);
            out.writeInt(mLayoutDirection);
        }

        // The `CREATOR` field is used to create the parcelable from a parcel, even though it is never referenced directly.
        public static final Creator<SavedState> CREATOR = ParcelableCompat.newCreator(new ParcelableCompatCreatorCallbacks<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel in, ClassLoader loader) {
                return new SavedState(in, loader);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        });
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        return new SavedState(superState, mLayoutDirection);
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }

        SavedState ss = (SavedState) state;
        mLayoutDirection = ss.mLayoutDirection;
        super.onRestoreInstanceState(ss.mViewPagerSavedState);
    }

    @Override
    public void setOnPageChangeListener(OnPageChangeListener listener) {
        super.setOnPageChangeListener(new ReversingOnPageChangeListener(listener));
    }

    @Override
    public void addOnPageChangeListener(OnPageChangeListener listener) {
        ReversingOnPageChangeListener reversingListener = new ReversingOnPageChangeListener(listener);
        mPageChangeListeners.put(listener, reversingListener);
        super.addOnPageChangeListener(reversingListener);
    }

    @Override
    public void removeOnPageChangeListener(OnPageChangeListener listener) {
        ReversingOnPageChangeListener reverseListener = mPageChangeListeners.remove(listener);
        if (reverseListener != null) {
            super.removeOnPageChangeListener(reverseListener);
        }
    }

    @Override
    public void clearOnPageChangeListeners() {
        super.clearOnPageChangeListeners();
        mPageChangeListeners.clear();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.UNSPECIFIED) {
            int height = 0;
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
                int h = child.getMeasuredHeight();
                if (h > height) {
                    height = h;
                }
            }
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private class ReversingOnPageChangeListener implements OnPageChangeListener {
        private final OnPageChangeListener mListener;

        public ReversingOnPageChangeListener(OnPageChangeListener listener) {
            mListener = listener;
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            // The documentation says that `getPageWidth(...)` returns the fraction of the _measured_ width that that page takes up.  However, the code seems to
            // use the width so we will here too.
            final int width = getWidth();
            PagerAdapter adapter = RtlViewPager.super.getAdapter();
            if (isRtl() && adapter != null) {
                int count = adapter.getCount();
                int remainingWidth = (int) (width * (1 - adapter.getPageWidth(position))) + positionOffsetPixels;
                while (position < count && remainingWidth > 0) {
                    position += 1;
                    remainingWidth -= (int) (width * adapter.getPageWidth(position));
                }
                position = count - position - 1;
                positionOffsetPixels = -remainingWidth;
                positionOffset = positionOffsetPixels / (width * adapter.getPageWidth(position));
            }
            mListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }

        @Override
        public void onPageSelected(int position) {
            PagerAdapter adapter = RtlViewPager.super.getAdapter();
            if (isRtl() && adapter != null) {
                position = adapter.getCount() - position - 1;
            }
            mListener.onPageSelected(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            mListener.onPageScrollStateChanged(state);
        }
    }

    private class ReversingAdapter extends DelegatingPagerAdapter {
        public ReversingAdapter(@NonNull PagerAdapter adapter) {
            super(adapter);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            if (isRtl()) {
                position = getCount() - position - 1;
            }
            super.destroyItem(container, position, object);
        }

        @Override
        public void destroyItem(View container, int position, Object object) {
            if (isRtl()) {
                position = getCount() - position - 1;
            }
            super.destroyItem(container, position, object);
        }

        @Override
        public int getItemPosition(Object object) {
            int position = super.getItemPosition(object);
            if (isRtl()) {
                if (position == POSITION_UNCHANGED || position == POSITION_NONE) {
                    // We can't accept POSITION_UNCHANGED when in RTL mode because adding items to the end of the collection adds them to the beginning of the
                    // ViewPager.  Items whose positions do not change from the perspective of the wrapped adapter actually do change from the perspective of
                    // the ViewPager.
                    position = POSITION_NONE;
                } else {
                    position = getCount() - position - 1;
                }
            }
            return position;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (isRtl()) {
                position = getCount() - position - 1;
            }
            return super.getPageTitle(position);
        }

        @Override
        public float getPageWidth(int position) {
            if (isRtl()) {
                position = getCount() - position - 1;
            }
            return super.getPageWidth(position);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            if (isRtl()) {
                position = getCount() - position - 1;
            }
            return super.instantiateItem(container, position);
        }

        @Override
        public Object instantiateItem(View container, int position) {
            if (isRtl()) {
                position = getCount() - position - 1;
            }
            return super.instantiateItem(container, position);
        }

        @Override
        public void setPrimaryItem(View container, int position, Object object) {
            if (isRtl()) {
                position = getCount() - position - 1;
            }
            super.setPrimaryItem(container, position, object);
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            if (isRtl()) {
                position = getCount() - position - 1;
            }
            super.setPrimaryItem(container, position, object);
        }
    }


    /*NoneSwipeViewPager*/
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (this.IsSwipeAllowed(event)) {
            return super.onTouchEvent(event);
        }

        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (this.IsSwipeAllowed(event)) {
            return super.onInterceptTouchEvent(event);
        }

        return false;
    }

    private boolean IsSwipeAllowed(MotionEvent event) {
        if (this.direction == SwipeDirection.all) return true;

        if (direction == SwipeDirection.none)//disable any swipe
            return false;

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            initialXValue = event.getX();
            return true;
        }

        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            try {
                float diffX = event.getX() - initialXValue;
                if (diffX > 0 && direction == SwipeDirection.right) {
                    // swipe from left to right detected
                    return false;
                } else if (diffX < 0 && direction == SwipeDirection.left) {
                    // swipe from right to left detected
                    return false;
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }

        return true;
    }

    public void setAllowedSwipeDirection(SwipeDirection direction) {
        this.direction = direction;
    }


}