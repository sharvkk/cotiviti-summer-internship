# Interoperability demo

I have used a starter project developed by HAPI as a FHIR server for the demonstration of functionalities in a FHIR REST API.

The entire library which is used in this starter project can be seen here: https://github.com/hapifhir/hapi-fhir

## Structure

The server

- HAPI's libraries are provided in the main sources in this project which uses **Spring Boot**.
- We can configure the *application.yml* file as per requirements. (I have chosen to connect to a local postgres DB).

The Client

- I have used the *src/test/java/"internship.project.test.requests"* package to write my code for FHIR client as per [documentation of HAPI](https://hapifhir.io/hapi-fhir/docs/).
- I have written a couple of test cases which you can find in the *src/test/resources/"clientTests"*. These are a few json documents and a test pdf file which I have used for my demo.


## Test Cases

The test cases covered in demo are (all the tests are performed from taking a semi-structured json data):

- Basic CRUD for a test Resource - Patient (patient.json)
- Transaction test - Using the 'Bundle' resource, tested multiple operations and conditional create. (patient2.json, observaion.json)
- Document test - Tested handling of a pdf document associated with a patient. (documentReference.json, practitioner.json, patient3.json)

