package com.montreal.wtm.utils.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;


public class GridResizeView extends GridView {

    public GridResizeView(Context context) {
        super(context);
    }

    public GridResizeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GridResizeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightSpec;

        if (getLayoutParams().height == LayoutParams.WRAP_CONTENT) {
            heightSpec = MeasureSpec.makeMeasureSpec(
                    Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        } else {
            heightSpec = heightMeasureSpec;
        }
        super.onMeasure(widthMeasureSpec, heightSpec);
    }
}

