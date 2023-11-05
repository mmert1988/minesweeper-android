package de.moonstarlabs.android.minesweeper.fragment;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;

import de.moonstarlabs.android.minesweeper.MainActivity;
import de.moonstarlabs.android.minesweeper.R;

/**
 * Dialog, der nach einem Gewinn angezeigt wird.
 */
public class NewHighscoreDialogFragment extends DialogFragment {
    
    /**
     * Implementierung einer statischen newInstance-Methode als eine Best Practice, sowie von Android's Developer-Seite empfohlen wird.
     * @return new {@link NewHighscoreDialogFragment} intance
     */
    public static NewHighscoreDialogFragment newInstance() {
        NewHighscoreDialogFragment instance = new NewHighscoreDialogFragment();
        instance.setRetainInstance(true);
        return instance;
    }
    
    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        if (getActivity() instanceof MainActivity) {
            final MainActivity activity = (MainActivity)getActivity();
            final EditText nameEdit = new EditText(activity);
            Builder builder = new Builder(getActivity());
            builder.setTitle(R.string.dialog_new_highscore_title);
            builder.setMessage(R.string.dialog_new_highscore_message);
            builder.setView(nameEdit);
            builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(final DialogInterface dialog, final int which) {
                    activity.setHighscore(nameEdit.getText().toString());
                }
            });
            builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(final DialogInterface dialog, final int which) {
                    dialog.cancel();
                }
            });
            
            return builder.create();
        }
        else {
            throw new IllegalAccessError("GameWonDialogFragment can only started from MainActivity");
        }
    }
    
}

