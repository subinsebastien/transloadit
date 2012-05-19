package com.exp.transloadit;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.os.AsyncTask;


 abstract class UploadCallback	{
	public abstract void done(JSONObject result, Exception e);
}

public class Transloadit {
	private String authKey;
	private String templateId;
	private String filePath;

	private class UploadTask extends AsyncTask<String, Void, JSONObject>	{

		HttpClient httpclient;
		HttpPost httppost;
		MultipartEntity entity;
		HttpResponse response;
		BufferedReader reader;
		String sResponse;
		StringBuilder builder;
		Exception exception;
		UploadCallback callback;
		
		
		public UploadTask(UploadCallback callback) {
			super();
			this.callback = callback;
		}
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			httpclient =  new DefaultHttpClient();
			httppost = new HttpPost("http://api2.transloadit.com/assemblies");
			entity = new MultipartEntity();
			exception =  null;
		}
		@Override
		protected JSONObject doInBackground(String... inputs) {
			File image = new File(inputs[0]);
			System.out.println((new FileBody(image)).toString());
			httppost.setHeader("enctype", "multipart/form-data");
			try {
				entity.addPart("params", new StringBody("{\"auth\":{\"key\":\"" + authKey + "\"},\"template_id\":\"" + templateId + "\"}"));
				entity.addPart("my_file", new FileBody(image));
				httppost.setEntity(entity);
				response = httpclient.execute(httppost);
				reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
				builder = new StringBuilder();
				while ((sResponse = reader.readLine()) != null) {
					builder = builder.append(sResponse);
				}
				return new JSONObject(builder.toString());
			} catch (Exception e) {
				e.printStackTrace();
				exception = e;
			}
			return null;
		}
		@Override
		protected void onPostExecute(JSONObject result) {
			super.onPostExecute(result);
			callback.done(result, exception);
		}
	}
	
	public void upload(UploadCallback callback)	{
		UploadTask task = new UploadTask(callback);
		if(task.getStatus() != AsyncTask.Status.RUNNING)	{
			task.execute(filePath);
		}
	}
	
	public String getAuthKey() {
		return authKey;
	}

	public void setAuthKey(String authKey) {
		this.authKey = authKey;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
}