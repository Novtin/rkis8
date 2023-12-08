package javaClasses;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import javaClasses.entity.Glasses;

import java.util.Optional;


@Component
public class RestClient {

    private final GlassesRepository glassesRepository;

    private static final String BASE_URL = "http://localhost:8081/rest/glasses";

    @Autowired
    public RestClient(GlassesRepository glassesRepository) {
        this.glassesRepository = glassesRepository;
    }

    public void testGetAllGlasses() {
        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(BASE_URL, String.class);
            printResponse("GET All Glasses", response);
        } catch (HttpClientErrorException exception) {
            printNotFound("GET All Glasses", exception.getMessage());
        }
    }

    public void testGetFilterAllGlasses(double diopters) {
        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(BASE_URL + "?diopters=" + diopters, String.class);
            printResponse("GET Filter Glasses", response);
        } catch (HttpClientErrorException exception) {
            printNotFound("GET Filter Glasses", exception.getMessage());
        }
    }

    public void testGetGlassesById(int id) {
        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(BASE_URL + "/" + id, String.class);
            printResponse("GET Glasses by ID", response);
        } catch (HttpClientErrorException exception) {
            printNotFound("GET Glasses by ID", exception.getMessage());
        }
    }

    public void testAddGlasses() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Glasses newGlasses = new Glasses(1.4, 20.5, "red", "red", "brand");
        HttpEntity<Glasses> requestEntity = new HttpEntity<>(newGlasses, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(BASE_URL, requestEntity, String.class);

        printResponse("POST Add Glasses", response);
    }

    public void testUpdateGlasses(int id) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Glasses glasses;
        Optional<Glasses> updatedGlasses = glassesRepository.findById((long) id);
        if (updatedGlasses.isPresent()) {
            glasses = updatedGlasses.get();
            glasses.setBrand("new1");
        } else {
            glasses = new Glasses();
        }
        HttpEntity<Glasses> requestEntity = new HttpEntity<>(glasses, headers);
        try {
            ResponseEntity<String> response = new RestTemplate().exchange(
                    BASE_URL + "/" + id, HttpMethod.PUT, requestEntity, String.class);
            printResponse("PUT Update Glasses", response);
        } catch (HttpClientErrorException exception) {
            printNotFound("PUT Update Glasses", exception.getMessage());
        }
    }

    public void testDeleteGlasses(int id) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);
        try {
            ResponseEntity<String> response = restTemplate
                    .exchange(BASE_URL + "/" + id, HttpMethod.DELETE, requestEntity, String.class);
            printResponse("DELETE Glasses", response);
        } catch (HttpClientErrorException exception) {
            printNotFound("DELETE Glasses", exception.getMessage());
        }
    }

    public static void printResponse(String testName, ResponseEntity<String> response) {
        System.out.println("Test: " + testName);
        System.out.println("Status Code: " + response.getStatusCode());
        System.out.println("Response Body: " + response.getBody());
        System.out.println();
    }

    public static void printNotFound(String testName, String status) {
        System.out.println("Test: " + testName);
        System.out.println("Status: " + status);
        System.out.println();
    }
}
