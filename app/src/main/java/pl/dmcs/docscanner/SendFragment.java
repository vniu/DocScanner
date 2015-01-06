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

import org.apache.http.protocol.HTTP;

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
        File file = new File(filePath);
        final Uri uri = Uri.fromFile(file);

        this.smsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("smsto:"));
                intent.putExtra("sms_body", "Check out pdf created by awesome OCR Application - Docscanner!");
                intent.putExtra(Intent.EXTRA_STREAM, uri);
                startActivity(Intent.createChooser(intent, "Send mms..."));
            }
        });

        this.mailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                emailIntent.setType("message/rfc822");
                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,"Test Subject");
                emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "From My App");
                emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
                startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            }
        });
    }
}
