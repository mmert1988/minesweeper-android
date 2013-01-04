package de.moonstarlabs.android.minesweeper.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

public class RectangularFieldView extends AdapterView<RectangularFieldAdapter> {
    private static final String LOGTAG = RectangularFieldView.class.getSimpleName();
    
    private RectangularFieldAdapter adapter;
    private int mTouchStartY;
    private int mListTopStart;
    
    private int mListTop;
    
    public RectangularFieldView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }
    
    @Override
    public void setAdapter(final RectangularFieldAdapter adapter) {
        this.adapter = adapter;
        removeAllViewsInLayout();
        requestLayout();
    }
    
    @Override
    public RectangularFieldAdapter getAdapter() {
        return adapter;
    }
    
    @Override
    protected void onFinishInflate() {
        Log.d(LOGTAG, "onFinishInflate()");
        
        super.onFinishInflate();
    }
    
    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        Log.d(LOGTAG, "onMeasure(" + widthMeasureSpec + ", " + heightMeasureSpec + ")");
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
    
    @Override
    protected void onLayout(final boolean changed, final int l, final int t, final int r,
            final int b) {
        Log.d(LOGTAG, "onLayout(" + changed + ", " + l + ", " + t + ", " + r + ", " + b + ")");
        
        if (adapter == null) {
            return;
        }
        
        if (getChildCount() == 0) {
            int position = 0;
            
            for (int i = 0; i < adapter.getRows(); i++) {
                for (int j = 0; j < adapter.getColumns(); j++) {
                    View child = adapter.getView(position, null, this);
                    ViewGroup.LayoutParams params = child.getLayoutParams();
                    if (params == null) {
                        params = new LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
                                android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
                    }
                    addViewInLayout(child, -1, params, true);
                    child.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
                    position++;
                }
            }
        }
        
        int childWidth = getChildAt(0).getMeasuredWidth();
        int childHeight = getChildAt(0).getMeasuredHeight();
        
        int index = 0;
        for (int i = 0; i < adapter.getRows(); i++) {
            for (int j = 0; j < adapter.getColumns(); j++) {
                int left = childWidth * j;
                int top = childHeight * i;
                
                View child = getChildAt(index);
                child.layout(left, top, left + childWidth, top + childHeight);
                
                index++;
            }
        }
        int right = l + (adapter.getColumns() + 1) * childWidth;
        int bottom = t + (adapter.getRows() + 1) * childHeight;
        super.onLayout(changed, l, t, right, bottom);
    }
    
    @Override
    public void setSelection(final int position) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public View getSelectedView() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean onTouchEvent(final MotionEvent ev) {
        Log.d(LOGTAG, "onTouchEvent(" + ev + ")");
        if (getChildCount() == 0) {
            return false;
        }
        
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mTouchStartY = (int)ev.getY();
                mListTopStart = getChildAt(0).getTop();
                break;
            case MotionEvent.ACTION_MOVE:
                int scrolledDistance = (int)ev.getY() - mTouchStartY;
                mListTop = mListTopStart + scrolledDistance;
                break;
            default:
                break;
        }
        return true;
    }
}
