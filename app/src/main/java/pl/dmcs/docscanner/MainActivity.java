package pl.dmcs.docscanner;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.io.IOException;

import pl.dmcs.docscanner.helpers.FileNameDialog;
import pl.dmcs.docscanner.helpers.LanguageDialog;
import pl.dmcs.docscanner.helpers.PDFWriter;

public class MainActivity extends Activity
        implements MainFragment.OnCurrentPhotoPathSetListener,
                   PhotoSelectFragment.OnCroppedImageSetListener,
                   LanguageDialog.LanguageSelectListener,
                   FileNameDialog.FilenameTypeListener {

	private static final int CAMERA_REQUEST = 2048;
    private static final int FILE_REQUEST = 4096;

    private static final String DATA_PATH = Environment.getExternalStorageDirectory().getPath()+"/tesseract";

    private static final String TAG = "MainActivity";

	public String mCurrentPhotoPath;
    public Bitmap croppedImage;

    private String tesseractLanguage;
    private String fileName;

    private ProgressDialog progressDialog;
    private Context context;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

        this.context = this;

        MainFragment mainFragment = new MainFragment();
        getFragmentManager().beginTransaction().add(R.id.container, mainFragment).commit();
		
	}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        PhotoSelectFragment photoSelectFragment = new PhotoSelectFragment();
        Bundle args = new Bundle();

        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {

            args.putString(PhotoSelectFragment.PASS_PATH, mCurrentPhotoPath);

        } else if (requestCode == FILE_REQUEST && resultCode == RESULT_OK) {

            Uri selectedImageUri = data.getData();
            args.putString(PhotoSelectFragment.PASS_URI, selectedImageUri.toString());

        }

        photoSelectFragment.setArguments(args);
        getFragmentManager().beginTransaction().replace(R.id.container, photoSelectFragment).addToBackStack(null).commit();
    }

    @Override
    public void onCurrentPhotoPathSet(String path) {
        this.mCurrentPhotoPath = path;
    }

    @Override
    public void onLanguageSelect(int option) {
        if (option == 0) tesseractLanguage = "eng";
        else if (option == 1) tesseractLanguage = "pol";
        new TesseractTask().execute(this.croppedImage);
    }

    @Override
    public void onCroppedImageSetListener(Bitmap image) {
        this.croppedImage = image;

        ChoiceFragment choiceFragment = new ChoiceFragment();
        getFragmentManager().beginTransaction().replace(R.id.container, choiceFragment).addToBackStack(null).commit();
    }

    @Override
    public void onFilenameType(String name, String text) {
        Log.v(TAG, "onFilenameType");

        this.fileName = name;
        new PDFWriterTask().execute(text);
    }

    private class TesseractTask extends AsyncTask<Bitmap, Void, String> {

        @Override
        protected void onPreExecute() {
            if (tesseractLanguage == "pol") {
                progressDialog = ProgressDialog.show(context, "", "Przetwarzanie..", false);
            } else if (tesseractLanguage == "eng") {
                progressDialog = ProgressDialog.show(context, "", "Reading...", false);
            }
        }

        @Override
        protected String doInBackground(Bitmap... params) {
            return processTesseract(params[0]);
        }

        @Override
        protected void onPostExecute(String resultText) {
            progressDialog.dismiss();

            EditFragment editFragment = new EditFragment();
            Bundle args = new Bundle();
            args.putString("text", resultText);
            editFragment.setArguments(args);
            getFragmentManager().beginTransaction().replace(R.id.container, editFragment).addToBackStack(null).commit();
        }

        private String processTesseract(Bitmap bitmap) {
            TessBaseAPI baseApi = new TessBaseAPI();
            baseApi.init(DATA_PATH, tesseractLanguage);
            baseApi.setImage(bitmap);

            String recognizedText = baseApi.getUTF8Text();
            baseApi.end();

            recognizedText = recognizedText.replace("-\n", " ").replace("\n", " ");
            Log.v(TAG, recognizedText);
            return recognizedText;
        }
    }

    private class PDFWriterTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(context, "", "Generating PDF...", false);
        }

        @Override
        protected String doInBackground(String... params) {
            File file;
            try {
                file = PDFWriter.createFile(params[0], fileName + ".pdf");

                return file.toURI().toString();
            } catch (IOException e) {
                Log.v(TAG, "Cannot write text to file");
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String filePath) {
            progressDialog.dismiss();

            SendFragment sendFragment = new SendFragment();
            Bundle args = new Bundle();
            args.putString("filePath", filePath);
            sendFragment.setArguments(args);

            getFragmentManager().beginTransaction().replace(R.id.container, sendFragment).addToBackStack(null).commit();
        }
    }

}
