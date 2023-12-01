package internship.project.test.requests;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import org.apache.commons.io.FileUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.File;
import java.util.Base64;

public class ClientUtil {

	private static final String baseUrl = "http://localhost:8080/fhir";
	public static FhirContext clientContext = FhirContext.forR4();
	private static boolean clientInitialized;
	private static IGenericClient client;

	private static void initializeClient(){
		client = clientContext.newRestfulGenericClient(baseUrl);
		clientInitialized = true;
	}

	public static IGenericClient getClient(){
		if(!clientInitialized){
			initializeClient();
		}
		return client;
	}

	public static String readJsonFromFile(String fileName) {
		try {
			// Read the content of the file
			Path path = Paths.get("src/test/resources/clientTests/" + fileName);
			return new String(Files.readAllBytes(path));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String getPdfBytes(String pdfFilePath) throws IOException {
		Path path = Paths.get("src/test/resources/clientTests/" + pdfFilePath);
		byte[] pdfBytes = FileUtils.readFileToByteArray(new File(pdfFilePath));

		// Encode the PDF content to Base64
		String base64String = Base64.getEncoder().encodeToString(pdfBytes);

		return base64String;
	}

}
