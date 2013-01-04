package de.moonstarlabs.android.minesweeper.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * TODO: Document type VScroll.
 */
public class VScroll extends ScrollView {
    
    public VScroll(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
    }
    
    public VScroll(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }
    
    public VScroll(final Context context) {
        super(context);
    }
    
    @Override
    public boolean onTouchEvent(final MotionEvent ev) {
        return false;
    }
    
}

