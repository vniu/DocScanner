package pl.dmcs.docscanner;


import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
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


public class PhotoSelectFragment extends Fragment {

    private static final String DATA_PATH = Environment.getExternalStorageDirectory().getPath()+"/tesseract";
    private static final String TAG = "PhotoSelectActivity";
    public static final String PASS_PATH = "path";
    public static final String PASS_URI = "uri";

    private Context context;

    private ImageButton okButton;
    private Bitmap croppedImage;

    private ProgressDialog progressDialog;

    public PhotoSelectFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = this.getActivity();
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
                new TesseractTask().execute(croppedImage);
            }
        });

        return view;
    }

    private String testTesseract(Bitmap bitmap) {
        TessBaseAPI baseApi = new TessBaseAPI();
        baseApi.init(DATA_PATH, "eng");
        baseApi.setImage(bitmap);
        String recognizedText = baseApi.getUTF8Text();
        baseApi.end();
        Log.v(TAG, recognizedText);
        return recognizedText;
    }

    private class TesseractTask extends AsyncTask<Bitmap, Void, String> {

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(context, "", "Przetwarzanie", false);
        }

        @Override
        protected String doInBackground(Bitmap... params) {
            String result = null;
            for (int i = 0; i < params.length; i++) {
                result = testTesseract(params[i]);
            }
            return result;
        }

        @Override
        protected void onPostExecute(String resultText) {
            progressDialog.dismiss();

            EditFragment editFragment = new EditFragment();
            Bundle args = new Bundle();
            args.putString("text", resultText);
            editFragment.setArguments(args);
            getActivity().getFragmentManager().beginTransaction().replace(R.id.container, editFragment).addToBackStack(null).commit();
        }
    }
}
