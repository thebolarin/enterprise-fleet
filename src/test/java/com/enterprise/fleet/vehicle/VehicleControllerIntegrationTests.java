package com.enterprise.fleet.vehicle;

import com.enterprise.fleet.auth.dto.AuthenticationRequestDTO;
import com.enterprise.fleet.auth.dto.RegisterRequestDTO;
import com.enterprise.fleet.location.Location;
import com.enterprise.fleet.location.LocationRepository;
import com.enterprise.fleet.user.types.Role;
import com.enterprise.fleet.user.entity.User;
import com.enterprise.fleet.user.repository.UserRepository;
import com.enterprise.fleet.user.types.UserStatusType;
import com.enterprise.fleet.vehicle.types.FuelType;
import com.enterprise.fleet.vehicle.types.VehicleType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class VehicleControllerIntegrationTests {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    private final Faker faker = new Faker();

    private String authToken;

    @BeforeEach
    void setup() {
        authToken = authenticateAndGetToken();
    }

    @Test
    void when_UserNotAuthorized_expect_CustomAuthenticationError() throws Exception {
        locationRepository.deleteAll();
        vehicleRepository.deleteAll();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth("user-token");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                "/api/v1/vehicle", HttpMethod.GET, entity, String.class);

        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode responseJson = objectMapper.readTree(responseEntity.getBody());
        assertFalse(responseJson.get("status").asBoolean());
        assertTrue(responseJson.has("data"));
        assertTrue(responseJson.get("data").isNull());
    }

    @Test
    void when_NoVehicles_expect_EmptyListOfResponseData() throws Exception {
        locationRepository.deleteAll();
        vehicleRepository.deleteAll();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken); // Set the bearer token in the headers
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                "/api/v1/vehicle", HttpMethod.GET, entity, String.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, responseEntity.getHeaders().getContentType());

        // Parse the actual response body as JSON
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode responseJson = objectMapper.readTree(responseEntity.getBody());

        // Check the "data" field for an empty array
        assertTrue(responseJson.has("data"));
        assertTrue(responseJson.get("data").isArray());
        assertEquals(0, responseJson.get("data").size());
    }

    @Test
    void when_Vehicles_expect_AllVehiclesReturnsListFromVehicleService() throws Exception
    {
        locationRepository.deleteAll();
        vehicleRepository.deleteAll();

        Location location =  Location.builder()
                .address(faker.address().streetAddress())
                .city(faker.address().cityName())
                .postcode(faker.address().zipCode())
                .capacity(faker.number().numberBetween(1,10))
            .build();

        Location locationResult = locationRepository.save(location);

        Vehicle vehicle =  Vehicle.builder()
                .type(VehicleType.CONVERTIBLE)
                .model("RX 780")
                .fuelType(FuelType.DIESEL)
                .manufacturer("toyota")
                .licensePlate("21345SGB")
                .year(2020)
                .location(locationResult)
                .build();

        vehicleRepository.save(vehicle);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken); // Set the bearer token in the headers
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                "/api/v1/vehicle", HttpMethod.GET, entity, String.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, responseEntity.getHeaders().getContentType());
        assertEquals(1, extractDataFromJsonResponse(responseEntity.getBody()).size());
    }


    public String authenticateAndGetToken() {
        String password = faker.internet().password();
        User user = buildUser(password);
        userRepository.save(user);
        return getAuthToken(user, password);
    }

    private User buildUser(String password) {
        return User.builder()
                .firstName(faker.name().firstName())
                .lastName(faker.name().lastName())
                .status(UserStatusType.ACTIVE)
                .email(faker.internet().emailAddress())
                .password(passwordEncoder.encode(password))
                .role(Role.ADMIN)
                .build();
    }

    private String getAuthToken(User user, String password) {
        AuthenticationRequestDTO authenticationRequestDTO = new AuthenticationRequestDTO();
        authenticationRequestDTO.setEmail(user.getEmail());
        authenticationRequestDTO.setPassword(password);

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(
                "/api/v1/auth/admin/login",
                authenticationRequestDTO,
                String.class
        );

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            JsonNode data =  extractDataFromJsonResponse(responseEntity.getBody());
            return data.get("accessToken").asText();
        } else {
            throw new RuntimeException("Authentication failed. Unable to obtain token.");
        }
    }

    private JsonNode extractDataFromJsonResponse(String jsonResponse) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(jsonResponse);

            if (jsonNode.has("data")) {
                return jsonNode.get("data");
            } else {
                throw new RuntimeException("Unable to extract data from JSON response. Check response structure.");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error extracting token from JSON response.", e);
        }
    }
}
