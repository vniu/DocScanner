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

    private ImageButton backButton;
    private ImageButton backIcon;

    public SaveFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_save, container, false);

        backButton = (ImageButton) view.findViewById(R.id.backButton);
        backIcon = (ImageButton) view.findViewById(R.id.backIcon);

        View.OnClickListener back = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getFragmentManager().popBackStack();
            }
        };

        backButton.setOnClickListener(back);
        backIcon.setOnClickListener(back);

        return view;
    }


}
