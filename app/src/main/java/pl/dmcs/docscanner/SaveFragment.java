package pl.dmcs.docscanner;


import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import pl.dmcs.docscanner.helpers.FileNameDialog;
import pl.dmcs.docscanner.helpers.LanguageDialog;
import pl.dmcs.docscanner.helpers.PDFWriter;


/**
 * A simple {@link Fragment} subclass.
 */
public class SaveFragment extends Fragment {

    private Context context;
    private final String TAG = "SaveFragment";

    private TextView backButton;
    private ImageButton backIcon;
    private TextView saveAsPdfButton;
    private ImageButton saveAsPdfIcon;

    public SaveFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_save, container, false);
        this.context = this.getActivity();

        Bundle bundle = this.getArguments();
        final String text = bundle.getString("text");

        backButton = (TextView) view.findViewById(R.id.backToEditButton);
        backIcon = (ImageButton) view.findViewById(R.id.backToEditIcon);
        saveAsPdfButton = (TextView) view.findViewById(R.id.saveAsPdfButton);
        saveAsPdfIcon = (ImageButton) view.findViewById(R.id.saveAsPdfIcon);

        View.OnClickListener back = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getFragmentManager().popBackStack();
            }
        };
        View.OnClickListener saveAsPdf = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileNameDialog fileNameDialog = new FileNameDialog();
                fileNameDialog.setText(text);
                fileNameDialog.show(getFragmentManager(), "fileName");
            }
        };

        backButton.setOnClickListener(back);
        backIcon.setOnClickListener(back);
        saveAsPdfButton.setOnClickListener(saveAsPdf);
        saveAsPdfIcon.setOnClickListener(saveAsPdf);

        return view;
    }

}
