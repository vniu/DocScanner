package pl.dmcs.docscanner.helpers;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;

import pl.dmcs.docscanner.R;

/**
 * Created by zales_000 on 2014-12-22.
 */
public class LanguageDialog extends DialogFragment {

    private LanguageSelectListener mListener;

    public LanguageDialog() {};

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (LanguageSelectListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement LanguageSelectListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.choose_lang)
                .setItems(R.array.lang_array, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mListener.onLanguageSelect(which);
                    }
                });
        return builder.create();
    }

    public interface LanguageSelectListener {
        public void onLanguageSelect(int option);
    }


}
