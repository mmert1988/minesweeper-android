package de.moonstarlabs.android.minesweeper.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageButton;

/**
 * Erweiterung der ImageButton-Klasse speziel für Zellen.
 */
public class CellView extends ImageButton {
    
    /**
     * 
     * Creates a new instance of {@link CellView}.
     * @param context Application Context
     * @param attrs Android attribute
     */
    public CellView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }
    
}
