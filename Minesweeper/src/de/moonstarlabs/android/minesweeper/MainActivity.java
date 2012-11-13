package de.moonstarlabs.android.minesweeper;

import java.util.HashSet;
import java.util.Set;

import android.app.Activity;
import android.os.Bundle;
import android.util.Pair;
import android.view.Menu;
import android.widget.GridView;
import de.moonstarlabs.android.minesweeper.model.Field;
import de.moonstarlabs.android.minesweeper.model.RectangularField;
import de.moonstarlabs.android.minesweeper.widget.FieldAdapter;
import de.moonstarlabs.android.minesweeper.widget.RectangularFieldView;

public class MainActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Set<Pair<Integer, Integer>> mineCoords = new HashSet<Pair<Integer, Integer>>();
		mineCoords.add(new Pair<Integer, Integer>(0, 0));
		mineCoords.add(new Pair<Integer, Integer>(1, 1));
		mineCoords.add(new Pair<Integer, Integer>(2, 2));
		mineCoords.add(new Pair<Integer, Integer>(3, 3));
		mineCoords.add(new Pair<Integer, Integer>(4, 4));
		Field field = new RectangularField(5, 5, mineCoords);
		field.getCell(0).open();
		field.getCell(1).open();
		FieldAdapter adapter = new FieldAdapter(this, field);
//		FieldView fieldView = (FieldView) findViewById(R.id.fieldView);
		RectangularFieldView fieldView = (RectangularFieldView) findViewById(R.id.fieldView);
		fieldView.setStretchMode(GridView.NO_STRETCH);
		fieldView.setNumColumns(5);
		fieldView.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
}
