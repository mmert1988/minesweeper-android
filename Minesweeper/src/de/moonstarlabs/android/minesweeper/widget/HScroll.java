package de.moonstarlabs.android.minesweeper.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;

/**
 * TODO: Document type HScroll.
 */
public class HScroll extends HorizontalScrollView {
    
    public HScroll(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
    }
    
    public HScroll(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }
    
    public HScroll(final Context context) {
        super(context);
    }
    
    @Override
    public boolean onTouchEvent(final MotionEvent ev) {
        return false;
    }
    
}

