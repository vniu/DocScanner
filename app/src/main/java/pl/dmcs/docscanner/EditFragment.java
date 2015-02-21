package pl.dmcs.docscanner;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import pl.dmcs.docscanner.helpers.PDFWriter;


/**
 * A simple {@link Fragment} subclass.
 */
public class EditFragment extends Fragment {

    private EditText editText;
    private ImageButton okButton;
    private ImageButton backButton;

    private Context context;

    public EditFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = this.getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit, container, false);

        Bundle bundle = this.getArguments();
        String text = bundle.getString("text");

        editText = (EditText) view.findViewById(R.id.text);
        editText.setText(text);

        okButton = (ImageButton) view.findViewById(R.id.okButtonEdit);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveFragment saveFragment = new SaveFragment();
                Bundle args = new Bundle();
                args.putString("text", editText.getText().toString());
                saveFragment.setArguments(args);
                getActivity().getFragmentManager().beginTransaction().replace(R.id.container, saveFragment).addToBackStack(null).commit();
            }
        });

        backButton = (ImageButton) view.findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getFragmentManager().popBackStack();
            }
        });

        return view;
    }


}
