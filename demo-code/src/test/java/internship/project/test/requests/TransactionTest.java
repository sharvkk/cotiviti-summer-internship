package internship.project.test.requests;

import ca.uhn.fhir.rest.client.api.IGenericClient;
import org.hl7.fhir.r4.model.*;
import org.junit.jupiter.api.Test;

public class TransactionTest {

	@Test
	public void testTransaction() {
		String patientJson = ClientUtil.readJsonFromFile("patient3.json");
		Patient patient = (Patient) ClientUtil.clientContext.newJsonParser().parseResource(patientJson);

		patient.setId(IdType.newRandomUuid());

		String observationJson = ClientUtil.readJsonFromFile("observation.json");
		Observation observation = (Observation) ClientUtil.clientContext.newJsonParser().parseResource(observationJson);

		observation.setSubject(new Reference(patient.getIdElement().getValue()));

		Bundle bundle = new Bundle();
		bundle.setType(Bundle.BundleType.TRANSACTION);

		bundle.addEntry()
			.setFullUrl(patient.getIdElement().getValue())
			.setResource(patient)
			.getRequest()
			.setIfNoneExist("Patient?identifier=http://example.org/clients|9")
			.setMethod(Bundle.HTTPVerb.POST);

		bundle.addEntry()
			.setResource(observation)
			.getRequest()
			.setUrl("Observation")
			.setMethod(Bundle.HTTPVerb.POST);

		System.out.println(ClientUtil.clientContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(bundle));

		IGenericClient client = ClientUtil.getClient();
		Bundle resp = client.transaction().withBundle(bundle).execute();

		System.out.println(ClientUtil.clientContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(resp));

	}
}
