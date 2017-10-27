package com.zw.funvideo.videoplayer;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowInsets;
import android.widget.FrameLayout;

/**
 * @author zsj
 */

public class InsetsFrameLayout extends FrameLayout {


    public InsetsFrameLayout(Context context) {
        this(context, null);
    }

    public InsetsFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public InsetsFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setOnApplyWindowInsetsListener(new OnApplyWindowInsetsListener() {
            @Override
            public WindowInsets onApplyWindowInsets(View view, WindowInsets windowInsets) {
                int r = windowInsets.getSystemWindowInsetRight();

                setPadding(getPaddingLeft(), getPaddingTop(), getPaddingRight() + r,
                        getPaddingBottom());

                setOnApplyWindowInsetsListener(null);

                return windowInsets.consumeSystemWindowInsets();
            }
        });


    }
}
