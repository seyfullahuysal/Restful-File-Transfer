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
 	*Bu örnek, istemciden gelen dosyalarý diske yüklemek için Java REST web servisinin nasýl oluþturulacaðýný göstermektedir.
  	* Þifreleme türü "multipart / form-data" ile POST isteklerini kabul eder

 */
@Path("/upload")
public class FileUploadService {
	
	/** bu sunucuya gelen dosyalarý kaydetmek istediðimiz yeri yazýyoruz */
	private static final String UPLOAD_FOLDER = "c:/uploadedFiles/";
	
	public FileUploadService() {}
	
	@Context
    private UriInfo context;

    /**
     * geçerli türde bir yanýt döndürür
     * eksik bir parametre varsa istisna fýrlatýlýr eðer dosya baþarýyla kaydedilirse cevap  gönderilir
     */
	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadFile(
        @FormDataParam("file") InputStream uploadedInputStream,
        @FormDataParam("file") FormDataContentDisposition fileDetail) {
		
		// Tüm form parametrelerinin saðlanýp saðlanmadýðýný kontrol ettik
		if (uploadedInputStream == null || fileDetail == null)
			return Response.status(400).entity("Invalid form data").build();
		
		// hedef klasör yolsa olýþtur
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
     * inputstream dosyalarý hedefe kaydettik
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
    

	// hedef klasör yolsa olýþtur
	private void createFolderIfNotExists(String dirName) throws SecurityException {
    	File theDir = new File(dirName);
    	if (!theDir.exists()) {
    		theDir.mkdir();
    	}
    }

}
