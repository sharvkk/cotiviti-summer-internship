package internship.project.test.requests;


import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

public class PatientResourceTest {

	@Test
	public void createPatient() {
		IGenericClient client = ClientUtil.getClient();

		String patientJson = ClientUtil.readJsonFromFile("patient.json");
		Patient patient = (Patient) ClientUtil.clientContext.newJsonParser().parseResource(patientJson);

		MethodOutcome outcome = client
			.create()
			.resource(patient)
			.execute();

		System.out.println(outcome.getId());
	}


	@Test
	public void getPatient() {
		IGenericClient client = ClientUtil.getClient();

		Bundle result = client.search()
			.forResource(Patient.class)
			.where(Patient.GENDER.exactly().code("male"))
			.returnBundle(Bundle.class)
			.execute();

		System.out.println("Found " + result.getEntry().size() + " patients");

		List<Bundle.BundleEntryComponent> entries = result.getEntry();

		for (Bundle.BundleEntryComponent entry : entries) {
			if (entry.getResource() instanceof Patient) {
				Patient patient = (Patient) entry.getResource();

				String familyName = patient.getNameFirstRep().getFamily();
				String givenName = patient.getNameFirstRep().getGivenAsSingleString();
				String gender = patient.getGender().getDisplay();
				Date birthDate = patient.getBirthDate();

				System.out.println("Family Name: " + familyName);
				System.out.println("Given Name: " + givenName);
				System.out.println("Gender: " + gender);
				System.out.println("Birth Date: " + birthDate);
			}
		}
	}

	@Test
	public void deletePatient(){
		IGenericClient client = ClientUtil.getClient();

		MethodOutcome outcome = client.delete()
			.resourceById(new IdType("Patient", "152"))
			.execute();

		System.out.println(ClientUtil.clientContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome.getOperationOutcome()));

	}

	@Test
	public void deleteConditional(){
		IGenericClient client = ClientUtil.getClient();

		Bundle resultBundle = client.search()
			.forResource(Patient.class)
			.where(Patient.ACTIVE.exactly().code("true"))
			.returnBundle(Bundle.class)
			.execute();

		for (Bundle.BundleEntryComponent entry : resultBundle.getEntry()) {
			IdType resourceId = entry.getResource().getIdElement();
			MethodOutcome outcome = client.delete().resourceById(resourceId).execute();
			System.out.println(ClientUtil.clientContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome.getOperationOutcome()));

		}

	}

}
