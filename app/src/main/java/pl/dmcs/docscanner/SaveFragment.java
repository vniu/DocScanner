package pl.dmcs.docscanner;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class SaveFragment extends Fragment {

    private TextView backButton;
    private ImageButton backIcon;
    private TextView saveAsPdfButton;
    private ImageButton saveAsPdfIcon;

    public SaveFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_save, container, false);

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
                SendFragment sendFragment = new SendFragment();
                getActivity().getFragmentManager().beginTransaction().replace(R.id.container, sendFragment).addToBackStack(null).commit();
            }
        };

        backButton.setOnClickListener(back);
        backIcon.setOnClickListener(back);
        saveAsPdfButton.setOnClickListener(saveAsPdf);
        saveAsPdfIcon.setOnClickListener(saveAsPdf);


        return view;
    }


}
