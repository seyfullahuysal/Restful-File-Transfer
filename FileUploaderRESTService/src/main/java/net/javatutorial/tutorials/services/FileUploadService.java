package net.javatutorial.tutorials.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

/**
 	*Bu �rnek, istemciden gelen dosyalar� diske y�klemek i�in Java REST web servisinin nas�l olu�turulaca��n� g�stermektedir.
� 	* �ifreleme t�r� "multipart / form-data" ile POST isteklerini kabul eder

 */
@Path("/upload")
public class FileUploadService {
	
	/** bu sunucuya gelen dosyalar� kaydetmek istedi�imiz yeri yaz�yoruz */
	private static final String UPLOAD_FOLDER = "c:/uploadedFiles/";
	
	public FileUploadService() {}
	
	@Context
    private UriInfo context;

    /**
     * ge�erli t�rde bir yan�t d�nd�r�r
     * eksik bir parametre varsa istisna f�rlat�l�r e�er dosya ba�ar�yla kaydedilirse cevap  g�nderilir
     */
	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadFile(
        @FormDataParam("file") InputStream uploadedInputStream,
        @FormDataParam("file") FormDataContentDisposition fileDetail) {
		
		// T�m form parametrelerinin sa�lan�p sa�lanmad���n� kontrol ettik
		if (uploadedInputStream == null || fileDetail == null)
			return Response.status(400).entity("Invalid form data").build();
		
		// hedef klas�r yolsa ol��tur
		try {
			createFolderIfNotExists(UPLOAD_FOLDER);
		} catch (SecurityException se) {
			return Response.status(500).entity("Can not create destination folder on server").build();
		}

        String uploadedFileLocation = UPLOAD_FOLDER + fileDetail.getFileName();
        try {
			saveToFile(uploadedInputStream, uploadedFileLocation);
		} catch (IOException e) {
			return Response.status(500).entity("Can not save file").build();
		}

        return Response.status(200).entity("File saved to " + uploadedFileLocation).build();
    }

    /**
     * inputstream dosyalar� hedefe kaydettik
     */
	private void saveToFile(InputStream inStream, String target) throws IOException {
		OutputStream out = null;
		int read = 0;
		byte[] bytes = new byte[1024];

		out = new FileOutputStream(new File(target));
		while ((read = inStream.read(bytes)) != -1) {
			out.write(bytes, 0, read);
		}
		out.flush();
		out.close();
	}
    

	// hedef klas�r yolsa ol��tur
	private void createFolderIfNotExists(String dirName) throws SecurityException {
    	File theDir = new File(dirName);
    	if (!theDir.exists()) {
    		theDir.mkdir();
    	}
    }

}
