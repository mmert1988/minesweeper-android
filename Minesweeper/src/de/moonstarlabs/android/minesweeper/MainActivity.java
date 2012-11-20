package de.moonstarlabs.android.minesweeper;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Chronometer;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import de.moonstarlabs.android.minesweeper.game.ClickModeState;
import de.moonstarlabs.android.minesweeper.game.Game;
import de.moonstarlabs.android.minesweeper.game.Game.DifficultyLevel;
import de.moonstarlabs.android.minesweeper.game.Game.Status;
import de.moonstarlabs.android.minesweeper.game.GameListener;
import de.moonstarlabs.android.minesweeper.game.OpenCellModeState;
import de.moonstarlabs.android.minesweeper.game.ToggleMarkModeState;
import de.moonstarlabs.android.minesweeper.widget.FieldAdapter;
import de.moonstarlabs.android.minesweeper.widget.RectangularFieldView;
import de.moonstarlabs.android.minesweeper.widget.FieldAdapter.OnItemClickListener;
import de.moonstarlabs.android.minesweeper.widget.FieldAdapter.OnItemLongClickListener;

public class MainActivity extends Activity implements OnClickListener, OnItemClickListener, OnItemLongClickListener, GameListener {
	private static final ClickModeState openCellClickModeState = new OpenCellModeState();
	private static final ClickModeState setFlagClickModeState = new ToggleMarkModeState();
	
	private Game game;
	private ClickModeState clickModeState;
	
	private ImageButton newGameButton;
	private ImageButton switchClickModeButton;
	private TextView minesLeftView;
	private Chronometer secondsPastView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		switchClickModeButton = (ImageButton) findViewById(R.id.switchClickMode);
		newGameButton = (ImageButton) findViewById(R.id.newGameButton);
		minesLeftView = (TextView) findViewById(R.id.minesLeftView);
		secondsPastView = (Chronometer) findViewById(R.id.secondsPastView);
		
		initNewGame();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		setOpenCellMode();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		secondsPastView.stop();
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
			
		case R.id.newGameButton:
			initNewGame();
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
		clickModeState.longClickOn(game, position);
	}

	@Override
	public void onItemClick(View item, int position) {
		clickModeState.clickOn(game, position);
	}

	@Override
	public void onGameStatusChanged(Status status) {
		switch (status) {
		case WON:
			// TODO
			secondsPastView.stop();
			break;
		case LOST:
			secondsPastView.stop();
			newGameButton.setImageResource(R.drawable.game_over);
			break;
		}
	}

	@Override
	public void onMinesLeftChanged(int minesLeft) {
		minesLeftView.setText(String.valueOf(minesLeft));
	}
	
	private void initNewGame() {
		newGameButton.setImageResource(R.drawable.new_game);
		game = new Game(DifficultyLevel.EASY);
		game.addListener(this);
		minesLeftView.setText(String.valueOf(game.getMinesLeft()));
		secondsPastView.setBase(game.getStartMillis());
		secondsPastView.start();
		
		FieldAdapter adapter = new FieldAdapter(this, game.getField());
		adapter.setOnItemClickListener(this);
		adapter.setOnItemLongClickListener(this);
		RectangularFieldView fieldView = (RectangularFieldView) findViewById(R.id.fieldView);
		fieldView.setStretchMode(GridView.NO_STRETCH);
		fieldView.setNumColumns(5);
		fieldView.setAdapter(adapter);
	}

}
