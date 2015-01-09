package pl.dmcs.docscanner;


import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import org.apache.http.protocol.HTTP;

import java.io.File;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class SendFragment extends Fragment {

    private static String TAG = "SendFragment";

    private String filePath;

    private ImageButton bluetoothButton;
    private TextView bluetoothText;
    private ImageButton mailButton;
    private TextView mailText;
    private TextView restartText;

    public SendFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_send, container, false);

        Bundle bundle = this.getArguments();
        this.filePath = bundle.getString("filePath");
        Log.v(TAG, filePath);

//        this.bluetoothButton = (ImageButton) view.findViewById(R.id.bluetoothshareIcon);
//        this.bluetoothText = (TextView) view.findViewById(R.id.bluetoothshareButton);
        this.mailButton = (ImageButton) view.findViewById(R.id.mailshareIcon);
        this.mailText = (TextView) view.findViewById(R.id.mailshareButton);
        this.restartText = (TextView) view.findViewById(R.id.restartText);

        this.setListeners();

        return view;
    }

    private void setListeners() {
        File file = new File(filePath);
        final Uri uri = Uri.fromFile(file);

        View.OnClickListener mailListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                emailIntent.setType("message/rfc822");
                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,"Test Subject");
                emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "From My App");
                emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
                startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            }
        };

        View.OnClickListener bluetoothListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
//                if (btAdapter != null) {
//                    Intent intent = new Intent();
//                    intent.setAction(Intent.ACTION_SEND);
//                    intent.setType("application/pdf");
//                    intent.putExtra(Intent.EXTRA_STREAM, uri );
//
//                    PackageManager packageManager = getActivity().getPackageManager();
//                    List<ResolveInfo> appsList = packageManager.queryIntentActivities(intent, 0);
//                    if(appsList.size() > 0) {
//                        String packageName = null;
//                        String className = null;
//                        boolean found = false;
//
//                        for(ResolveInfo info: appsList){
//                            packageName = info.activityInfo.packageName;
//                            if( packageName.equals("com.android.bluetooth")){
//                                className = info.activityInfo.name;
//                            }
//                        }
//                        intent.setClassName(packageName, className);
//                        startActivity(intent);
//                    }
//                }
            }
        };

        View.OnClickListener restartListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainFragment mainFragment = new MainFragment();
                getFragmentManager().beginTransaction().replace(R.id.container, mainFragment).addToBackStack(null).commit();
            }
        };

        this.mailText.setOnClickListener(mailListener);
        this.mailButton.setOnClickListener(mailListener);
//        this.bluetoothText.setOnClickListener(bluetoothListener);
//        this.bluetoothButton.setOnClickListener(bluetoothListener);
        this.restartText.setOnClickListener(restartListener);

    }
}
