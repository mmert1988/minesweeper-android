package de.moonstarlabs.android.minesweeper

import android.os.Bundle
import android.os.SystemClock
import android.preference.PreferenceManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Chronometer
import android.widget.HorizontalScrollView
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import de.moonstarlabs.android.minesweeper.fragment.HighscoresDialogFragment
import de.moonstarlabs.android.minesweeper.fragment.NewHighscoreDialogFragment
import de.moonstarlabs.android.minesweeper.game.ClickModeState
import de.moonstarlabs.android.minesweeper.game.Game
import de.moonstarlabs.android.minesweeper.game.Game.DifficultyLevel
import de.moonstarlabs.android.minesweeper.game.GameListener
import de.moonstarlabs.android.minesweeper.game.OpenCellModeState
import de.moonstarlabs.android.minesweeper.game.ToggleMarkModeState
import de.moonstarlabs.android.minesweeper.model.FieldListener
import de.moonstarlabs.android.minesweeper.model.RectangularField
import de.moonstarlabs.android.minesweeper.task.RefreshFieldViewTask
import de.moonstarlabs.android.minesweeper.widget.RectangularFieldAdapter.OnCellClickListener
import de.moonstarlabs.android.minesweeper.widget.RectangularFieldAdapter.OnCellLongClickListener

/**
 * Die Haupt-Activity zur Darstellung des Spieldfeldes.
 */
class MainActivity : FragmentActivity(), View.OnClickListener, OnCellClickListener,
    OnCellLongClickListener, GameListener, FieldListener {
    private var game: Game? = null
    private var clickModeState = OPEN_CELL_CLICK_MODE_STATE
    private var newGameButton: ImageButton? = null
    private var switchClickModeButton: ImageButton? = null
    private var minesLeftView: TextView? = null
    private var secondsPastView: Chronometer? = null
    private var level = DifficultyLevel.EASY
    private var hScroll: HorizontalScrollView? = null
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setViews()
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        level = DifficultyLevel.valueOf(
            prefs.getString(
                Preferences.PREF_KEY_LEVEL,
                DifficultyLevel.EASY.toString()
            )!!
        )
        game = if (savedInstanceState != null) {
            savedInstanceState.getParcelable(EXTRA_GAME)
        } else {
            Game(level)
        }
        initViews(game)
        refreshFieldView(game)
        updateViewsOnStatusChange(game!!.status)
    }

    private fun setViews() {
        setContentView(R.layout.activity_main)
        switchClickModeButton = findViewById<View>(R.id.switchClickMode) as ImageButton
        newGameButton = findViewById<View>(R.id.newGameButton) as ImageButton
        minesLeftView = findViewById<View>(R.id.minesLeftView) as TextView
        secondsPastView = findViewById<View>(R.id.secondsPastView) as Chronometer
        hScroll = findViewById<View>(R.id.horizontalScroll) as HorizontalScrollView
    }

    override fun onResume() {
        super.onResume()
        setOpenCellMode()
        game!!.resume()
    }

    override fun onPause() {
        super.onPause()
        game!!.stop()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(EXTRA_GAME, game)
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val prefEdit = prefs.edit()
        prefEdit.putString(Preferences.PREF_KEY_LEVEL, level.toString())
        prefEdit.apply()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.activity_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_settings -> true
            R.id.menu_difficulty_easy -> {
                level = DifficultyLevel.EASY
                initNewGame(level)
                true
            }

            R.id.menu_difficulty_medium -> {
                level = DifficultyLevel.MEDIUM
                initNewGame(level)
                true
            }

            R.id.menu_difficulty_hard -> {
                level = DifficultyLevel.HARD
                initNewGame(level)
                true
            }

            R.id.menu_view_highscores -> {
                showHighscores()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.switchClickMode -> clickModeState.switchClickMode(this)
            R.id.newGameButton -> initNewGame(level)
            else -> {}
        }
    }

    /**
     * setzt Klick-Modus zum Öffnen.
     */
    fun setOpenCellMode() {
        clickModeState = OPEN_CELL_CLICK_MODE_STATE
        switchClickModeButton!!.setImageResource(R.drawable.mine)
    }

    /**
     * setzt Klick-Modus zum setzen von Fahnen.
     */
    fun setToggleMarkMode() {
        clickModeState = SET_FLAG_CLICK_MODE_STATE
        switchClickModeButton!!.setImageResource(R.drawable.flag)
    }

    override fun onItemLongClick(item: View, position: Int) {
        clickModeState.longClickOn(game, position)
    }

    override fun onItemClick(item: View, position: Int) {
        clickModeState.clickOn(game, position)
    }

    override fun onGameStatusChanged(status: Game.Status) {
        if (status == Game.Status.WON) {
            val prefs = PreferenceManager.getDefaultSharedPreferences(this)
            val wonTime = game!!.stopMillis - game!!.startMillis
            var highscoreTime = Long.MAX_VALUE
            when (level) {
                DifficultyLevel.EASY -> highscoreTime =
                    prefs.getLong(Preferences.PREF_EASY_HIGHSORE_MILLIS, Long.MAX_VALUE)

                DifficultyLevel.MEDIUM -> highscoreTime =
                    prefs.getLong(Preferences.PREF_MEDIUM_HIGHSORE_MILLIS, Long.MAX_VALUE)

                DifficultyLevel.HARD -> highscoreTime =
                    prefs.getLong(Preferences.PREF_HARD_HIGHSORE_MILLIS, Long.MAX_VALUE)

                else -> {}
            }
            if (wonTime < highscoreTime) {
                NewHighscoreDialogFragment.newInstance()
                    .show(supportFragmentManager, "game won dialog")
            }
        }
        updateViewsOnStatusChange(status)
    }

    override fun onMinesLeftChanged(minesLeft: Int) {
        minesLeftView!!.text = minesLeft.toString()
    }

    override fun onFieldChanged() {
        refreshFieldView(game)
    }

    private fun initNewGame(l: DifficultyLevel) {
        setOpenCellMode()
        game = Game(l)
        initViews(game)
        refreshFieldView(game)
        updateViewsOnStatusChange(game!!.status)
    }

    private fun initViews(g: Game?) {
        g!!.addListener(this)
        g.field.addListener(this)
        minesLeftView!!.text = g.minesLeft.toString()
    }

    private fun refreshFieldView(g: Game?) {
        hScroll!!.post(RefreshFieldViewTask(this, g!!.field as RectangularField, hScroll))
    }

    private fun updateViewsOnStatusChange(status: Game.Status) {
        when (status) {
            Game.Status.NEW -> {
                secondsPastView!!.base = SystemClock.elapsedRealtime()
                secondsPastView!!.stop()
                newGameButton!!.setImageResource(R.drawable.new_game)
            }

            Game.Status.RUNNING -> {
                secondsPastView!!.base = game!!.startMillis
                secondsPastView!!.start()
                newGameButton!!.setImageResource(R.drawable.new_game)
            }

            Game.Status.STOPPED -> secondsPastView!!.stop()
            Game.Status.LOST -> {
                secondsPastView!!.stop()
                newGameButton!!.setImageResource(R.drawable.game_over)
            }

            Game.Status.WON -> {
                newGameButton!!.setImageResource(R.drawable.game_won)
                secondsPastView!!.stop()
            }

            else -> {}
        }
    }

    private fun showHighscores() {
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val names = arrayOf(
            prefs.getString(Preferences.PREF_EASY_HIGHSCORE_NAME, ""),
            prefs.getString(Preferences.PREF_MEDIUM_HIGHSCORE_NAME, ""),
            prefs.getString(Preferences.PREF_HARD_HIGHSCORE_NAME, "")
        )
        val millis = longArrayOf(
            prefs.getLong(Preferences.PREF_EASY_HIGHSORE_MILLIS, Long.MAX_VALUE),
            prefs.getLong(Preferences.PREF_MEDIUM_HIGHSORE_MILLIS, Long.MAX_VALUE),
            prefs.getLong(Preferences.PREF_HARD_HIGHSORE_MILLIS, Long.MAX_VALUE)
        )
        HighscoresDialogFragment.newInstance(names, millis)
            .show(supportFragmentManager, "highscores dialog")
    }

    /**
     * Startet Dialog zum Registrieren eines Gewinns.
     * @param name Name des Spielers
     */
    fun setHighscore(name: String?) {
        val wonTime = game!!.stopMillis - game!!.startMillis
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        when (level) {
            DifficultyLevel.EASY -> {
                prefs.edit().putLong(Preferences.PREF_EASY_HIGHSORE_MILLIS, wonTime).apply()
                prefs.edit().putString(Preferences.PREF_EASY_HIGHSCORE_NAME, name).apply()
            }

            DifficultyLevel.MEDIUM -> {
                prefs.edit().putLong(Preferences.PREF_MEDIUM_HIGHSORE_MILLIS, wonTime).apply()
                prefs.edit().putString(Preferences.PREF_MEDIUM_HIGHSCORE_NAME, name).apply()
            }

            DifficultyLevel.HARD -> {
                prefs.edit().putLong(Preferences.PREF_HARD_HIGHSORE_MILLIS, wonTime).apply()
                prefs.edit().putString(Preferences.PREF_HARD_HIGHSCORE_NAME, name).apply()
            }

            else -> {}
        }
        showHighscores()
    }

    companion object {
        private val OPEN_CELL_CLICK_MODE_STATE: ClickModeState = OpenCellModeState()
        private val SET_FLAG_CLICK_MODE_STATE: ClickModeState = ToggleMarkModeState()
        private const val EXTRA_GAME = "game"
    }
}