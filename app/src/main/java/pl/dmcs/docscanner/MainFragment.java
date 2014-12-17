package pl.dmcs.docscanner;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MainFragment extends Fragment {

    private static final int CAMERA_REQUEST = 2048;
    private static final int FILE_REQUEST = 4096;

    private ImageButton photoButton;
    private TextView photoText;
    private ImageButton fileButton;
    private TextView fileText;

    private String mCurrentPhotoPath;
    private Context context;

    private OnCurrentPhotoPathSetListener mListener;

    public MainFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = this.getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        setListeners(view);
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnCurrentPhotoPathSetListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnCurrentPhotoPathSetListener");
        }
    }

    private void setListeners(View view) {

        this.photoButton = (ImageButton) view.findViewById(R.id.photoButton);
        this.photoText = (TextView) view.findViewById(R.id.photoText);
        this.fileButton = (ImageButton) view.findViewById(R.id.fileButton);
        this.fileText = (TextView) view.findViewById(R.id.fileText);

        View.OnClickListener photoListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                if (intent.resolveActivity(context.getPackageManager()) != null) {
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException ex) {
                        System.err.println("error creating file");
                    }

                    if (photoFile != null) {
                        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                                Uri.fromFile(photoFile));
                        getActivity().startActivityForResult(intent, CAMERA_REQUEST);
                    }
                }
            }
        };

        View.OnClickListener fileListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                getActivity().startActivityForResult(Intent.createChooser(intent,
                        "File request"), FILE_REQUEST);
            }
        };

        this.photoButton.setOnClickListener(photoListener);
        this.photoText.setOnClickListener(photoListener);
        this.fileButton.setOnClickListener(fileListener);
        this.fileText.setOnClickListener(fileListener);
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        this.mListener.onCurrentPhotoPathSet(mCurrentPhotoPath);
        return image;
    }

    public interface OnCurrentPhotoPathSetListener {
        public void onCurrentPhotoPathSet(String path);
    }

}
