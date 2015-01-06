package pl.dmcs.docscanner.helpers;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import pl.dmcs.docscanner.R;

/**
 * Created by zales_000 on 2014-12-22.
 */
public class FileNameDialog extends DialogFragment {

    private Activity host;
    private String text;
    private FilenameTypeListener mListener;

    public FileNameDialog() {};

    public void setText(String text) { this.text = text; }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.host = activity;
        try {
            mListener = (FilenameTypeListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement LanguageSelectListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = this.host.getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.file_name_dialog, null);
        final EditText fileNameField = (EditText) view.findViewById(R.id.fileNameField);
        builder.setTitle(R.string.type_file_name)
               .setView(view)
               .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                        mListener.onFilenameType(fileNameField.getText().toString(), text);
                   }
               })
               .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       FileNameDialog.this.getDialog().dismiss();
                   }
               });
        return builder.create();
    }

    public interface FilenameTypeListener {
        public void onFilenameType(String name, String text);
    }


}
