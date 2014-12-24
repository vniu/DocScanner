package pl.dmcs.docscanner;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.edmodo.cropper.CropImageView;
import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.IOException;

import pl.dmcs.docscanner.helpers.LanguageDialog;


public class PhotoSelectFragment extends DialogFragment {

    public static final String PASS_PATH = "path";
    public static final String PASS_URI = "uri";

    private Context context;

    private ImageButton okButton;
    private Bitmap croppedImage;

    public OnCroppedImageSetListener mListener;

    public PhotoSelectFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = this.getActivity();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnCroppedImageSetListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnCroppedImageSetListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_photo_select, container, false);

        final CropImageView cropImageView = (CropImageView) view.findViewById(R.id.CropImageView);
        okButton = (ImageButton) view.findViewById(R.id.okButton);

        Bitmap bitmap = null;
        ContentResolver contentResolver = this.getActivity().getContentResolver();
        Bundle bundle = this.getArguments();

        if (bundle.getString(PASS_PATH) != "" && bundle.getString(PASS_PATH) != null) {
            String photoSrc = bundle.getString(PASS_PATH);
            bitmap = BitmapFactory.decodeFile(photoSrc.substring(5));
        } else if (bundle.getString(PASS_URI) != "" && bundle.getString(PASS_URI) != null) {
            Uri uri = Uri.parse(bundle.getString(PASS_URI));
            try {
                bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        cropImageView.setImageBitmap(bitmap);
        cropImageView.setGuidelines(2);

        okButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                croppedImage = cropImageView.getCroppedImage();
                mListener.onCroppedImageSetListener(croppedImage);
                LanguageDialog languageDialog = new LanguageDialog();
                languageDialog.show(getFragmentManager(), "lang");
            }
        });

        return view;
    }

    public interface OnCroppedImageSetListener {
        public void onCroppedImageSetListener(Bitmap image);
    }

}
