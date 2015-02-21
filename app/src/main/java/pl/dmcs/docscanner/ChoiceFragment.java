package pl.dmcs.docscanner;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import pl.dmcs.docscanner.helpers.LanguageDialog;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChoiceFragment extends Fragment {

    private Button pdfFromImageButton;
    private Button pdfFromTextButton;

    public ChoiceFragment() { }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_choice, container, false);

        this.pdfFromImageButton = (Button) view.findViewById(R.id.fromImageButton);
        this.pdfFromTextButton = (Button) view.findViewById(R.id.fromTextButton);

        this.setListeners();

        return view;
    }

    private void setListeners() {

        this.pdfFromTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LanguageDialog languageDialog = new LanguageDialog();
                languageDialog.show(getFragmentManager(), "lang");
            }
        });

        this.pdfFromImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }


}
