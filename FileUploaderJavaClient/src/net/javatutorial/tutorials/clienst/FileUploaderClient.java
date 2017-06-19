package net.javatutorial.tutorials.clienst;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.util.EntityUtils;

/*
 * Bu örnek bir dosyayý multipart/form-data þifreleme yöntemini yöntemi ile 
 * Post isteklerini kullanark sunucuya göndermektedir
 */
public class FileUploaderClient {
	
	public static void main(String[] args) {
		
		//Göndermek istediðimiz dosyanýn yolu siz bunun yerini istediðiniz dosyayý gönderebilirsiniz ben resim gönderdim
		File inFile = new File("C:\\Users\\SU\\Desktop\\deneme.jpg");
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(inFile);
			DefaultHttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());
			
			// Server adresi
			HttpPost httppost = new HttpPost("http://localhost:8080/FileUploaderRESTService/rest/upload");
			MultipartEntity entity = new MultipartEntity();

			// dosya adýný ve dosyamýzý multipart paketimize olarak ekliyoruz
			entity.addPart("file", new InputStreamBody(fis, inFile.getName()));
			httppost.setEntity(entity);

			//isteði baþlatýyoruz 
			HttpResponse response = httpclient.execute(httppost);
			
			int statusCode = response.getStatusLine().getStatusCode();
			HttpEntity responseEntity = response.getEntity();
			String responseString = EntityUtils.toString(responseEntity, "UTF-8");
			
			System.out.println("[" + statusCode + "] " + responseString);
			
		} catch (ClientProtocolException e) {
			System.err.println("Unable to make connection");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Unable to read file");
			e.printStackTrace();
		} finally {
			try {
				if (fis != null) fis.close();
			} catch (IOException e) {}
		}
	}
	
}
