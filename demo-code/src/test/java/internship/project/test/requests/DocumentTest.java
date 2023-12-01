package internship.project.test.requests;

import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import org.apache.commons.io.FileUtils;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.*;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class DocumentTest {

	private String createPatient(IGenericClient client) {
		String patientJson = ClientUtil.readJsonFromFile("patient2.json");
		Patient patient = (Patient) ClientUtil.clientContext.newJsonParser().parseResource(patientJson);

		MethodOutcome outcome = client
			.create()
			.resource(patient)
			.execute();
		System.out.println("Patient created Successfully: " + outcome.getId());

		return outcome.getId().getResourceType() + "/" + outcome.getId().getIdPart();
	}

	private String createAuthor(IGenericClient client) {

		String authorJson = ClientUtil.readJsonFromFile("practitioner.json");
		Practitioner author = (Practitioner) ClientUtil.clientContext.newJsonParser().parseResource(authorJson);


		MethodOutcome outcome = client
			.create()
			.resource(author)
			.execute();
		System.out.println("Author created Successfully: " + outcome.getId());

		return outcome.getId().getResourceType() + "/" + outcome.getId().getIdPart();
	}

	private DocumentReference.DocumentReferenceContentComponent createContent(IGenericClient client) throws IOException {
		byte[] pdfBytes = FileUtils.readFileToByteArray(new File("src/test/resources/clientTests/document_test.pdf"));
		Binary document = new Binary();
		document.setData(pdfBytes);

		MethodOutcome outcome = client
			.create()
			.resource(document)
			.execute();

		String documentId = outcome.getId().getValue();
		System.out.println("Binary created Successfully: " + outcome.getId());

		Attachment attachment = new Attachment();
		attachment.setContentType("application/pdf");
		attachment.setUrl(outcome.getId().getValue());

		DocumentReference.DocumentReferenceContentComponent contentComponent = new DocumentReference.DocumentReferenceContentComponent();
		contentComponent.setAttachment(attachment);

		return contentComponent;

	}

	@Test
	public void testDocument() throws IOException {
		IGenericClient client = ClientUtil.getClient();


		String documentReferenceJson = ClientUtil.readJsonFromFile("document.json");
		DocumentReference documentReference = (DocumentReference) ClientUtil.clientContext.newJsonParser().parseResource(documentReferenceJson);

//		Reference newRef = new Reference();
//		newRef.setReference(createPatient(client));
//		documentReference.setSubject(newRef);
//		newRef.setReference(createAuthor(client));
//		documentReference.addAuthor(newRef);

		documentReference.setSubject(new Reference(createPatient(client)));
		documentReference.addAuthor(new Reference(createAuthor(client)));
		documentReference.addContent(createContent(client));

		MethodOutcome outcome = client
			.create()
			.resource(documentReference)
			.execute();

//		System.out.println(ClientUtil.clientContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome.getOperationOutcome()));
		System.out.println("Document ref created : " + outcome.getId());
	}
}
