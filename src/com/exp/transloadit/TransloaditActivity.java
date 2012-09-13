package com.exp.transloadit;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore.Images.ImageColumns;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;

import com.androidquery.AQuery;

public class TransloaditActivity extends Activity {
	
	AQuery aQuery;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		Button b = (Button) findViewById(R.id.button1);
		Button b2 = (Button) findViewById(R.id.button2);

		aQuery = new AQuery(TransloaditActivity.this);
		
		final ProgressBar p = (ProgressBar) findViewById(R.id.progressBar);

		p.setVisibility(View.INVISIBLE);

		b2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
				photoPickerIntent.setType("image/*");
				startActivityForResult(photoPickerIntent, 0);
			}
		});

		b.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				TransloaditUpload t = new TransloaditUpload();
				t.setAuthKey("c4b82fb7b5644bd7bb53bb2089fd215d");
				t.setTemplateId("bd723f3eaa5540798e43166c58504d6d");

				p.setVisibility(View.VISIBLE);

				t.upload(aQuery.id(R.id.textView1).getText().toString(), new UploadCallback() {
					@Override
					public void done(String assemblyId, Exception e) {
						p.setVisibility(View.INVISIBLE);
						if (assemblyId != null && e == null) {
							
							File x = new File(aQuery.id(R.id.textView1).getText().toString());
							
							aQuery.id(R.id.imageView).image(
									"http://fanclubsocial.s3.amazonaws.com/"
											+ assemblyId
											+ "/100x100_cropped_thumbnail_"
											+ x.getName());
						}
					}
				});
			}
		});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 0) {
			if (data != null) {
				
				Cursor cursor = getContentResolver().query(data.getData(), null, null, null, null);
				cursor.moveToFirst();
				int idx = cursor.getColumnIndex(ImageColumns.DATA);
				String fileSrc = cursor.getString(idx);
				aQuery.id(R.id.textView1).text(fileSrc);
				
				Bitmap bitmapPreview = BitmapFactory.decodeFile(fileSrc);
				aQuery.id(R.id.imageView).image(bitmapPreview);
				
			} else {
				aQuery.id(R.id.textView1).text("Image selection canceled!");
			}
		}
	}

}