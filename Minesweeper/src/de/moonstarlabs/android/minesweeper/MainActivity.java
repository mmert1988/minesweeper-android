package de.moonstarlabs.android.minesweeper;

import static de.moonstarlabs.android.minesweeper.Preferences.*;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Chronometer;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.TextView;

import de.moonstarlabs.android.minesweeper.fragment.HighscoresDialogFragment;
import de.moonstarlabs.android.minesweeper.fragment.NewHighscoreDialogFragment;
import de.moonstarlabs.android.minesweeper.game.ClickModeState;
import de.moonstarlabs.android.minesweeper.game.Game;
import de.moonstarlabs.android.minesweeper.game.Game.DifficultyLevel;
import de.moonstarlabs.android.minesweeper.game.Game.Status;
import de.moonstarlabs.android.minesweeper.game.GameListener;
import de.moonstarlabs.android.minesweeper.game.OpenCellModeState;
import de.moonstarlabs.android.minesweeper.game.ToggleMarkModeState;
import de.moonstarlabs.android.minesweeper.model.FieldListener;
import de.moonstarlabs.android.minesweeper.model.RectangularField;
import de.moonstarlabs.android.minesweeper.task.RefreshFieldViewTask;
import de.moonstarlabs.android.minesweeper.widget.RectangularFieldAdapter.OnCellClickListener;
import de.moonstarlabs.android.minesweeper.widget.RectangularFieldAdapter.OnCellLongClickListener;

/**
 * Die Haupt-Activity zur Darstellung des Spieldfeldes.
 */
public class MainActivity extends FragmentActivity implements OnClickListener, OnCellClickListener,
OnCellLongClickListener, GameListener, FieldListener {
    private static final ClickModeState OPEN_CELL_CLICK_MODE_STATE = new OpenCellModeState();
    private static final ClickModeState SET_FLAG_CLICK_MODE_STATE = new ToggleMarkModeState();
    private static final String EXTRA_GAME = "game";
    
    private Game game;
    private ClickModeState clickModeState = OPEN_CELL_CLICK_MODE_STATE;
    
    private ImageButton newGameButton;
    private ImageButton switchClickModeButton;
    private TextView minesLeftView;
    private Chronometer secondsPastView;
    private Game.DifficultyLevel level = DifficultyLevel.EASY;
    
    private HorizontalScrollView hScroll;
    
    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setViews();
        
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        level = DifficultyLevel.valueOf(prefs.getString(PREF_KEY_LEVEL, DifficultyLevel.EASY.toString()));
        
        if (savedInstanceState != null) {
            game = savedInstanceState.getParcelable(EXTRA_GAME);
        }
        else {
            game = new Game(level);
        }
        
        initViews(game);
        refreshFieldView(game);
        updateViewsOnStatusChange(game.getStatus());
    }
    
    private void setViews() {
        setContentView(R.layout.activity_main);
        
        switchClickModeButton = (ImageButton)findViewById(R.id.switchClickMode);
        newGameButton = (ImageButton)findViewById(R.id.newGameButton);
        minesLeftView = (TextView)findViewById(R.id.minesLeftView);
        secondsPastView = (Chronometer)findViewById(R.id.secondsPastView);
        
        hScroll = (HorizontalScrollView)findViewById(R.id.horizontalScroll);
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        setOpenCellMode();
        game.resume();
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        game.stop();
    }
    
    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(EXTRA_GAME, game);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor prefEdit = prefs.edit();
        prefEdit.putString(PREF_KEY_LEVEL, level.toString());
        prefEdit.commit();
    }
    
    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_settings:
                return true;
            case R.id.menu_difficulty_easy:
                level = DifficultyLevel.EASY;
                initNewGame(level);
                return true;
            case R.id.menu_difficulty_medium:
                level = DifficultyLevel.MEDIUM;
                initNewGame(level);
                return true;
            case R.id.menu_difficulty_hard:
                level = DifficultyLevel.HARD;
                initNewGame(level);
                return true;
            case R.id.menu_view_highscores:
                showHighscores();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.switchClickMode:
                clickModeState.switchClickMode(this);
                break;
                
            case R.id.newGameButton:
                initNewGame(level);
                break;
            default:
                break;
        }
    }
    
    /**
     * setzt Klick-Modus zum Öffnen.
     */
    public void setOpenCellMode() {
        clickModeState = OPEN_CELL_CLICK_MODE_STATE;
        switchClickModeButton.setImageResource(R.drawable.mine);
    }
    
    /**
     * setzt Klick-Modus zum setzen von Fahnen.
     */
    public void setToggleMarkMode() {
        clickModeState = SET_FLAG_CLICK_MODE_STATE;
        switchClickModeButton.setImageResource(R.drawable.flag);
    }
    
    @Override
    public void onItemLongClick(final View item, final int position) {
        clickModeState.longClickOn(game, position);
    }
    
    @Override
    public void onItemClick(final View item, final int position) {
        clickModeState.clickOn(game, position);
    }
    
    @Override
    public void onGameStatusChanged(final Status status) {
        if (status == Status.WON) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            long wonTime = game.getStopMillis() - game.getStartMillis();
            long highscoreTime = Long.MAX_VALUE;
            switch(level) {
                case EASY:
                    highscoreTime = prefs.getLong(PREF_EASY_HIGHSORE_MILLIS, Long.MAX_VALUE);
                    break;
                case MEDIUM:
                    highscoreTime = prefs.getLong(PREF_MEDIUM_HIGHSORE_MILLIS, Long.MAX_VALUE);
                    break;
                case HARD:
                    highscoreTime = prefs.getLong(PREF_HARD_HIGHSORE_MILLIS, Long.MAX_VALUE);
                    break;
                default:
                    break;
            }
            if (wonTime < highscoreTime) {
                NewHighscoreDialogFragment.newInstance().show(getSupportFragmentManager(), "game won dialog");
            }
        }
        updateViewsOnStatusChange(status);
    }
    
    @Override
    public void onMinesLeftChanged(final int minesLeft) {
        minesLeftView.setText(String.valueOf(minesLeft));
    }
    
    @Override
    public void onFieldChanged() {
        refreshFieldView(game);
    }
    
    private void initNewGame(final DifficultyLevel l) {
    	setOpenCellMode();
        game = new Game(l);
        initViews(game);
        refreshFieldView(game);
        updateViewsOnStatusChange(game.getStatus());
    }
    
    private void initViews(final Game g) {
        g.addListener(this);
        g.getField().addListener(this);
        minesLeftView.setText(String.valueOf(g.getMinesLeft()));
    }
    
    private void refreshFieldView(final Game g) {
        hScroll.post(new RefreshFieldViewTask(this, (RectangularField)g.getField(), hScroll));
    }
    
    private void updateViewsOnStatusChange(final Game.Status status) {
        switch (status) {
            case NEW:
                secondsPastView.setBase(SystemClock.elapsedRealtime());
                secondsPastView.stop();
                newGameButton.setImageResource(R.drawable.new_game);
                break;
                
            case RUNNING:
                secondsPastView.setBase(game.getStartMillis());
                secondsPastView.start();
                newGameButton.setImageResource(R.drawable.new_game);
                break;
                
            case STOPPED:
                secondsPastView.stop();
                break;
                
            case LOST:
                secondsPastView.stop();
                newGameButton.setImageResource(R.drawable.game_over);
                break;
                
            case WON:
                newGameButton.setImageResource(R.drawable.game_won);
                secondsPastView.stop();
                break;
            default:
                break;
        }
    }
    
    private void showHighscores() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String[] names = new String[] {
                prefs.getString(PREF_EASY_HIGHSCORE_NAME, ""),
                prefs.getString(PREF_MEDIUM_HIGHSCORE_NAME, ""),
                prefs.getString(PREF_HARD_HIGHSCORE_NAME, "")
        };
        long[] millis = new long[] {
                prefs.getLong(PREF_EASY_HIGHSORE_MILLIS, Long.MAX_VALUE),
                prefs.getLong(PREF_MEDIUM_HIGHSORE_MILLIS, Long.MAX_VALUE),
                prefs.getLong(PREF_HARD_HIGHSORE_MILLIS, Long.MAX_VALUE)
        };
        
        HighscoresDialogFragment.newInstance(names, millis).show(getSupportFragmentManager(), "highscores dialog");
    }
    
    /**
     * Startet Dialog zum Registrieren eines Gewinns.
     * @param name Name des Spielers
     */
    public void setHighscore(final String name) {
        long wonTime = game.getStopMillis() - game.getStartMillis();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        switch(level) {
            case EASY:
                prefs.edit().putLong(PREF_EASY_HIGHSORE_MILLIS, wonTime).commit();
                prefs.edit().putString(PREF_EASY_HIGHSCORE_NAME, name).commit();
                break;
            case MEDIUM:
                prefs.edit().putLong(PREF_MEDIUM_HIGHSORE_MILLIS, wonTime).commit();
                prefs.edit().putString(PREF_MEDIUM_HIGHSCORE_NAME, name).commit();
                break;
            case HARD:
                prefs.edit().putLong(PREF_HARD_HIGHSORE_MILLIS, wonTime).commit();
                prefs.edit().putString(PREF_HARD_HIGHSCORE_NAME, name).commit();
                break;
            default:
                break;
        }
        showHighscores();
    }
    
}
