package de.moonstarlabs.android.minesweeper;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import de.moonstarlabs.android.minesweeper.game.ClickModeState;
import de.moonstarlabs.android.minesweeper.game.Game;
import de.moonstarlabs.android.minesweeper.game.Game.DifficultyLevel;
import de.moonstarlabs.android.minesweeper.game.OpenCellModeState;
import de.moonstarlabs.android.minesweeper.game.ToggleMarkModeState;
import de.moonstarlabs.android.minesweeper.widget.FieldAdapter.OnItemClickListener;
import de.moonstarlabs.android.minesweeper.widget.FieldAdapter.OnItemLongClickListener;

public class MainActivity extends Activity implements OnClickListener, OnItemClickListener, OnItemLongClickListener {
	private static final ClickModeState openCellClickModeState = new OpenCellModeState();
	private static final ClickModeState setFlagClickModeState = new ToggleMarkModeState();
	
	private Game game;
	private ImageButton switchClickModeButton;
	private ClickModeState clickModeState;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		game = new Game(this, DifficultyLevel.EASY);
		
		switchClickModeButton = (ImageButton) findViewById(R.id.switchClickMode);
		setOpenCellMode();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.switchClickMode:
			clickModeState.switchClickMode(this);
			break;
		}
	}
	
	public void setOpenCellMode() {
		clickModeState = openCellClickModeState;
		switchClickModeButton.setImageResource(R.drawable.mine);
	}
	
	public void setToggleMarkMode() {
		clickModeState = setFlagClickModeState;
		switchClickModeButton.setImageResource(R.drawable.flag);
	}

	@Override
	public void onItemLongClick(View item, int position) {
		clickModeState.longClickOn(game.getField(), position);
	}

	@Override
	public void onItemClick(View item, int position) {
		clickModeState.clickOn(game.getField(), position);
	}

}
