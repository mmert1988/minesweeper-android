package de.moonstarlabs.android.minesweeper.task;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.HorizontalScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;

import de.moonstarlabs.android.minesweeper.MainActivity;
import de.moonstarlabs.android.minesweeper.R;
import de.moonstarlabs.android.minesweeper.model.RectangularField;
import de.moonstarlabs.android.minesweeper.widget.RectangularFieldAdapter;

/**
 * Task-Klasse mit der Aufgabe, FieldView auf MainActivity zu refreshen.
 */
public class RefreshFieldViewTask implements Runnable {
    private final MainActivity activity;
    private final RectangularField field;
    private final HorizontalScrollView scrollView;
    
    /**
     * 
     * Creates a new instance of {@link RefreshFieldViewTask}.
     * @param activity MainActivity instance
     * @param field RectangularField instance
     * @param scrollView HorizontalScrollView instance, containing the view for RectangularField
     */
    public RefreshFieldViewTask(final MainActivity activity, final RectangularField field, final HorizontalScrollView scrollView) {
        this.activity = activity;
        this.field = field;
        this.scrollView = scrollView;
    }
    
    @Override
    public void run() {
        RectangularFieldAdapter adapter = new RectangularFieldAdapter(activity, field);
        adapter.setOnCellClickListener(activity);
        adapter.setOnCellLongClickListener(activity);
        scrollView.removeAllViews();
        scrollView.addView(createView(adapter));
    }
    
    private TableLayout createView(final RectangularFieldAdapter adapter) {
        LayoutInflater inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        TableLayout table = (TableLayout)inflater.inflate(R.layout.rectangular_field_view, null);
        
        for (int i = 0; i < adapter.getRows(); i++) {
            TableRow row = (TableRow)inflater.inflate(R.layout.cell_row, null);
            row.setGravity(Gravity.CENTER_HORIZONTAL);
            table.addView(row);
        }
        int position = 0;
        for (int i = 0; i < adapter.getRows(); i++) {
            TableRow row = (TableRow)table.getChildAt(i);
            for (int j = 0; j < adapter.getColumns(); j++) {
                View child = adapter.getView(position, null, null);
                child.measure(MeasureSpec.EXACTLY, MeasureSpec.EXACTLY);
                row.addView(child);
                position++;
            }
        }
        TableLayout.LayoutParams params = new TableLayout.LayoutParams(
                android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
                android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
        table.setLayoutParams(params);
        return table;
    }
    
}

