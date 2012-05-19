package com.exp.transloadit;

import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;

public class TransloaditActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		Button b = (Button) findViewById(R.id.button1);
		final ProgressBar p = (ProgressBar) findViewById(R.id.progressBar);
		p.setVisibility(View.INVISIBLE);
		
		b.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Transloadit t = new Transloadit();
				t.setAuthKey("c4b82fb7b5644bd7bb53bb2089fd215d");
				t.setTemplateId("3ba65aac42854932a3a3f72c44683fd1");
				t.setFilePath("/sdcard/comp_geek.jpg");
				
				p.setVisibility(View.VISIBLE);
				
				t.upload(new UploadCallback() {
					@Override
					public void done(JSONObject result, Exception e) {
						p.setVisibility(View.INVISIBLE);
						if(result != null && e == null)	{
							System.out.println("[][][][] -- " + result.toString());
						}
					}
				});
			}
		});
	}
}





















/*
 * 
 * File image = new File("/sdcard/comp_geek.jpg");

				System.out.println((new FileBody(image)).toString());

				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(
						"http://api2.transloadit.com/assemblies");
				httppost.setHeader("enctype", "multipart/form-data");

				try {
					MultipartEntity entity = new MultipartEntity();

					entity.addPart("params", new StringBody("{\"auth\":{\"key\":\"c4b82fb7b5644bd7bb53bb2089fd215d\"},\"template_id\":\"3ba65aac42854932a3a3f72c44683fd1\"}"));
					entity.addPart("my_file", new FileBody(image));
					httppost.setEntity(entity);
					HttpResponse res = httpclient.execute(httppost);

					BufferedReader reader = new BufferedReader(new InputStreamReader(res.getEntity().getContent(), "UTF-8"));
					String sResponse;
					StringBuilder s = new StringBuilder();
					while ((sResponse = reader.readLine()) != null) {
						s = s.append(sResponse);
					}
					System.out.println("Response: " + s);

				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
 * 
 */