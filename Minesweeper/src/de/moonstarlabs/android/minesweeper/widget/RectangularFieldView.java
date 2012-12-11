package de.moonstarlabs.android.minesweeper.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListAdapter;

public class RectangularFieldView extends GridView {

	private static final String LOGTAG = RectangularFieldView.class.getSimpleName();

	private ListAdapter adapter;

	public RectangularFieldView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public void setAdapter(ListAdapter adapter) {
		this.adapter = adapter;
		removeAllViewsInLayout();
		requestLayout();
	}

	@Override
	public ListAdapter getAdapter() {
		return this.adapter;
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		Log.d(LOGTAG, "onLayout(" + changed + ", " + l + ", " + t + ", " + r + ", " + b + ")");
		super.onLayout(changed, l, t, r, b);

		if (adapter == null) {
			return;
		}

		if (getChildCount() == 0) {
			int position = 0;
			int bottomEdge = 0;

			while (bottomEdge < getHeight() && position < adapter.getCount()) {
				View newBottomChild = adapter.getView(position, null, this);
				addAndMeasureChild(newBottomChild);
				bottomEdge += newBottomChild.getMeasuredHeight();
				position++;
			}
		}

		positionItems();
	}

	private void positionItems() {
		int top = 0; 
		 
	    for (int index = 0; index < getChildCount(); index++) { 
	        View child = getChildAt(index); 
	 
	        int width = child.getMeasuredWidth(); 
	        int height = child.getMeasuredHeight(); 
	        int left = (getWidth() - width) / 2; 
	 
	        child.layout(left, top, left + width, top + height); 
	        top += height;
	    }
	}

	private void addAndMeasureChild(View child) {
		ViewGroup.LayoutParams params = child.getLayoutParams();
		if (params == null) {
			params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		}
		addViewInLayout(child, -1, params, true);

		int itemWidth = getWidth();
		child.measure(MeasureSpec.EXACTLY | itemWidth, MeasureSpec.UNSPECIFIED);
	}

	@Override
	public void setSelection(int position) {
		throw new UnsupportedOperationException();
	}

	@Override
	public View getSelectedView() {
		throw new UnsupportedOperationException();
	}

	// wird nicht aufgerufen:
	@Override
	protected void measureChild(View child, int parentWidthMeasureSpec, int parentHeightMeasureSpec) {
		Log.d(LOGTAG, "measureChild(" + child + ", " + parentWidthMeasureSpec + ", " + parentHeightMeasureSpec + ")");
		super.measureChild(child, parentWidthMeasureSpec, parentHeightMeasureSpec);
	}

	// wird nicht aufgerufen:
	@Override
	protected void measureChildren(int widthMeasureSpec, int heightMeasureSpec) {
		Log.d(LOGTAG, "measureChildren(" + widthMeasureSpec + ", " + heightMeasureSpec + ")");
		super.measureChildren(widthMeasureSpec, heightMeasureSpec);
	}

	// wird nicht aufgerufen:
	@Override
	protected void measureChildWithMargins(View child, int parentWidthMeasureSpec, int widthUsed,
			int parentHeightMeasureSpec, int heightUsed) {
		Log.d(LOGTAG, "measureChild(" + child + ", " + parentWidthMeasureSpec + ", " + widthUsed + ", "
				+ parentHeightMeasureSpec + ", " + heightUsed + ")");
		super.measureChildWithMargins(child, parentWidthMeasureSpec, widthUsed, parentHeightMeasureSpec, heightUsed);
	}

	@Override
	protected void onFinishInflate() {
		Log.d(LOGTAG, "onFinishInflate()");
		super.onFinishInflate();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		Log.d(LOGTAG, "onMeasure(" + widthMeasureSpec + ", " + heightMeasureSpec + ")");
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	protected void layoutChildren() {
		Log.d(LOGTAG, "layoutChildren()");
		super.layoutChildren();
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		Log.d(LOGTAG, "onSizeChanged( " + w + ", " + h + ", " + oldw + ", " + oldh + ")");
		super.onSizeChanged(w, h, oldw, oldh);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		Log.d(LOGTAG, "onDraw(" + canvas + ")");
		super.onDraw(canvas);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Log.d(LOGTAG, "onKeyDown(" + keyCode + ", " + event + ")");
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		Log.d(LOGTAG, "onKeyUp(" + keyCode + ", " + event + ")");
		return super.onKeyUp(keyCode, event);
	}

	@Override
	public boolean onTrackballEvent(MotionEvent event) {
		Log.d(LOGTAG, "onTrackballEvent(" + event + ")");
		return super.onTrackballEvent(event);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		Log.d(LOGTAG, "onTouchEvent(" + ev + ")");
		return super.onTouchEvent(ev);
	}

	@Override
	protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
		Log.d(LOGTAG, "onFocusChanged(" + gainFocus + ", " + direction + ", " + previouslyFocusedRect + ")");
		super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
	}

	@Override
	public void onWindowFocusChanged(boolean hasWindowFocus) {
		Log.d(LOGTAG, "onWindowFocusChanged(" + hasWindowFocus + ")");
		super.onWindowFocusChanged(hasWindowFocus);
	}

	@Override
	protected void onAttachedToWindow() {
		Log.d(LOGTAG, "onAttachedToWindow()");
		super.onAttachedToWindow();
	}

	@Override
	protected void onDetachedFromWindow() {
		Log.d(LOGTAG, "onDetachedFromWindow()");
		super.onDetachedFromWindow();
	}

	@Override
	protected void onWindowVisibilityChanged(int visibility) {
		Log.d(LOGTAG, "onWindowVisibilityChanged(" + visibility + ")");
		super.onWindowVisibilityChanged(visibility);
	}
}
