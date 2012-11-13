package de.moonstarlabs.android.minesweeper.widget;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.widget.ImageButton;

public class CellView extends ImageButton {
	private Context mContext;
	private Resources mResources;

	public CellView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		mResources = context.getResources();
	}

}
