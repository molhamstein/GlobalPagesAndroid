package com.brainsocket.mainlibrary.Views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.brainsocket.mainlibrary.Enums.LayoutStatesEnum;
import com.brainsocket.mainlibrary.Listeners.OnRefreshLayoutListener;
import com.brainsocket.mainlibrary.R;


/**
 * Created by windows 8.1 on 2017-03-21.
 */

public class Stateslayoutview extends RelativeLayout implements View.OnClickListener {

    Context context;
    View SuccessLayout, SecondryLayout;
    OnRefreshLayoutListener onRefreshLayoutListener;
    int NodataResId = -1;
    int NoConnectionResId = -1;
    int WaitingResId = -1;
    int NopermissionResId = -1;

    int RefreshbtnRes = -1;
    int PermissionbtnRes = -1;

    public Stateslayoutview(Context context) {
        super(context);
    }

    public Stateslayoutview(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public Stateslayoutview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    void init(Context context, AttributeSet attrs) {
        try {
            this.context = context;
            SuccessLayout = this.getChildAt(0);
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Stateslayoutview, 0, 0);

            NodataResId = a.getResourceId(R.styleable.Stateslayoutview_NoDataLayout, -1);
            NoConnectionResId = a.getResourceId(R.styleable.Stateslayoutview_NoConnectionLayout, -1);
            WaitingResId = a.getResourceId(R.styleable.Stateslayoutview_WaitingLayout, -1);

            NopermissionResId = a.getResourceId(R.styleable.Stateslayoutview_NoPermisionLayout, -1);

            RefreshbtnRes = a.getResourceId(R.styleable.Stateslayoutview_RefreshbtnRes, -1);
            PermissionbtnRes = a.getResourceId(R.styleable.Stateslayoutview_PermissionbtnRes, -1);
            a.recycle();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    void Initlayers() {
        SuccessLayout = this.getChildAt(0);
        SecondryLayout = new View(context);
        this.addView(SecondryLayout, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }

    public void FlipLayout(LayoutStatesEnum statesEnum) {

        switch (statesEnum) {
            case Noconnectionlayout:
                if (NoConnectionResId != -1) {
                    ReplaceLayout(NoConnectionResId);
                }
                if (SuccessLayout != null)
                    SuccessLayout.setVisibility(GONE);
                break;
            case Nodatalayout:
                if (NodataResId != -1) {
                    ReplaceLayout(NodataResId);
                }
                if (SuccessLayout != null)
                    SuccessLayout.setVisibility(GONE);
                break;
            case Waitinglayout:
                if (WaitingResId != -1) {
                    ReplaceLayout(WaitingResId);
                }
                if (SuccessLayout != null)
                    SuccessLayout.setVisibility(GONE);
                break;
            case SuccessLayout:
                if (SecondryLayout != null)
                    SecondryLayout.setVisibility(GONE);
                if (SuccessLayout != null)
                    SuccessLayout.setVisibility(VISIBLE);
                break;

            case NopermissionLayout:
                if (NopermissionResId != -1) {
                    ReplaceLayout(NopermissionResId);
                }
                if (SuccessLayout != null)
                    SuccessLayout.setVisibility(GONE);
                break;

        }
    }

    public void ReplaceLayout(int LayoutId) {
        this.removeView(SecondryLayout);
        SecondryLayout = inflate(context, LayoutId, null);
        SecondryLayout.setVisibility(VISIBLE);
        this.addView(SecondryLayout, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        if (RefreshbtnRes != -1) {
            View view = SecondryLayout.findViewById(RefreshbtnRes);
            if (view != null)
                view.setOnClickListener(this);
        }
        if (PermissionbtnRes != -1) {
            View view = SecondryLayout.findViewById(PermissionbtnRes);
            if (view != null)
                view.setOnClickListener(this);
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        Initlayers();
    }

    public void setOnRefreshLayoutListener(OnRefreshLayoutListener onRefreshLayoutListener) {
        this.onRefreshLayoutListener = onRefreshLayoutListener;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == RefreshbtnRes) {
            if (onRefreshLayoutListener != null) {
                onRefreshLayoutListener.onRefresh();
            }
        } else if (v.getId() == PermissionbtnRes) {
            if (onRefreshLayoutListener != null) {
                onRefreshLayoutListener.onRequestPermission();
            }
        }

    }
}
