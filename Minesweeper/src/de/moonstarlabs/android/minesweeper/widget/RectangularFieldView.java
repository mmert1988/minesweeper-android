package de.moonstarlabs.android.minesweeper.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.GridView;
import android.widget.ListAdapter;

public class RectangularFieldView extends GridView {
	
	private static final String LOGTAG = RectangularFieldView.class.getSimpleName();

	public RectangularFieldView(Context context, AttributeSet attrs) {
		super(context, attrs);
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
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		Log.d(LOGTAG, "onLayout(" + changed + ", " + l + ", " + t + ", " + r +", " + b + ")");
		super.onLayout(changed, l, t, r, b);
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
	
	@Override
	public void setAdapter(ListAdapter adapter) {
		Log.d(LOGTAG, "setAdapter(" + adapter + ")");
		super.setAdapter(adapter);
	}
	
	@Override
	public ListAdapter getAdapter() {
		Log.d(LOGTAG, "getAdapter()");
		return super.getAdapter();
	}
}
