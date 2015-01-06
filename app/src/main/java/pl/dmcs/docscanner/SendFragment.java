package pl.dmcs.docscanner;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import java.io.File;


/**
 * A simple {@link Fragment} subclass.
 */
public class SendFragment extends Fragment {

    private static String TAG = "SendFragment";

    private String filePath;

    private ImageButton smsButton;
    private ImageButton fbButton;
    private ImageButton mailButton;

    public SendFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_send, container, false);

        Bundle bundle = this.getArguments();
        this.filePath = bundle.getString("filePath");
        Log.v(TAG, filePath);

        this.smsButton = (ImageButton) view.findViewById(R.id.smsshare);
        this.fbButton = (ImageButton) view.findViewById(R.id.fbshare);
        this.mailButton = (ImageButton) view.findViewById(R.id.mailshare);

        this.setListeners();

        return view;
    }

    private void setListeners() {
//        this.smsButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                Log.v(TAG, filePath);
//                Intent sendIntent = new Intent(Intent.ACTION_SEND);
//                sendIntent.setType("text/x-vcard");
//                sendIntent.putExtra(Intent.EXTRA_STREAM, filePath);
//                startActivity(sendIntent);
//            }
//        });

        this.mailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = new File(filePath);
                Uri uri = Uri.fromFile(file);

                Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                emailIntent.setType("application/pdf");
                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,"Test Subject");
                emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "From My App");
                emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
                startActivity(Intent.createChooser(emailIntent, "Send mail..."));
//                Intent i = new Intent(Intent.ACTION_SENDTO);
//                i.setType("application/pdf");
//                i.putExtra(Intent.EXTRA_STREAM, uri);
                startActivity(Intent.createChooser(emailIntent,"Email:"));
            }
        });
    }


}
