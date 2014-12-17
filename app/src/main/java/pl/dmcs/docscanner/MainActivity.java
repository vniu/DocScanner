package pl.dmcs.docscanner;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

import pl.dmcs.docscanner.util.SystemUiHider;

public class MainActivity extends Activity implements MainFragment.OnCurrentPhotoPathSetListener {

	private static final int CAMERA_REQUEST = 2048;
    private static final int FILE_REQUEST = 4096;

	public String mCurrentPhotoPath;
    private Context context;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

        MainFragment mainFragment = new MainFragment();
        getFragmentManager().beginTransaction().add(R.id.container, mainFragment).commit();
		
	}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        PhotoSelectFragment photoSelectFragment = new PhotoSelectFragment();
        Bundle args = new Bundle();

        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            args.putString(PhotoSelectFragment.PASS_PATH, mCurrentPhotoPath);
            Log.v("Main Activity", mCurrentPhotoPath);
        } else if (requestCode == FILE_REQUEST && resultCode == RESULT_OK) {
            Uri selectedImageUri = data.getData();
            args.putString(PhotoSelectFragment.PASS_PATH, selectedImageUri.toString());
            Log.v("Main Activity", selectedImageUri.toString());
        }

        photoSelectFragment.setArguments(args);
        getFragmentManager().beginTransaction().replace(R.id.container, photoSelectFragment).addToBackStack(null).commit();
    }

    @Override
    public void onCurrentPhotoPathSet(String path) {
        this.mCurrentPhotoPath = path;
    }
}
