package com.practice.companysearch.service;

import com.practice.companysearch.config.ApiConfig;
import com.practice.companysearch.exception.ApiException;
import com.practice.companysearch.model.CompanyDetails;
import com.practice.companysearch.model.CompanySearchRequest;
import com.practice.companysearch.model.CompanySearchResponse;
import com.practice.companysearch.repository.CompanyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CompanySearchServiceImplTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ApiConfig apiConfig;

    @Mock
    private CompanyRepository companyRepository;

    @InjectMocks
    private CompanySearchServiceImpl companySearchService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSearchCompanyFromDatabase() {
        CompanyDetails companyDetails = new CompanyDetails();
        companyDetails.setCompanyNumber("123456");
        when(companyRepository.findById("123456")).thenReturn(Optional.of(companyDetails));

        CompanySearchRequest request = new CompanySearchRequest("Test Company", "123456");
        String apiKey = "test-api-key";

        CompanySearchResponse response = companySearchService.searchCompany(request, apiKey, false);

        assertNotNull(response);
        assertEquals(1, response.getTotalResults());
        assertEquals("123456", response.getItems().get(0).getCompanyNumber());
    }

    @Test
    public void testSearchCompanyFromApi_SuccessfulResponse() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        ResponseEntity<String> successfulResponseEntity = new ResponseEntity<>("{\"totalResults\": 1, \"items\": [{\"company_number\": \"123456\"}]}", HttpStatus.OK);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(String.class))).thenReturn(successfulResponseEntity);

        when(apiConfig.getSearchUrl()).thenReturn("http://example.com/api/company/search");
        when(apiConfig.getOfficersUrl()).thenReturn("http://example.com/api/company/officers");

        CompanySearchRequest request = new CompanySearchRequest("Test Company", "123456");
        String apiKey = "test-api-key";

        CompanySearchResponse response = companySearchService.searchCompany(request, apiKey, false);

        assertNotNull(response);
        assertEquals(1, response.getTotalResults());
        assertEquals("123456", response.getItems().get(0).getCompanyNumber());
    }

    @Test
    public void testSearchCompanyFromApi_EmptyResponse() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        ResponseEntity<String> emptyResponseEntity = new ResponseEntity<>("{}", HttpStatus.OK);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(String.class))).thenReturn(emptyResponseEntity);

        when(apiConfig.getSearchUrl()).thenReturn("http://example.com/api/company/search");

        CompanySearchRequest request = new CompanySearchRequest("Test Company", "123456");
        String apiKey = "test-api-key";

        CompanySearchResponse response = companySearchService.searchCompany(request, apiKey, false);

        assertNotNull(response);
        assertEquals(0, response.getTotalResults());
        assertTrue(response.getItems().isEmpty());
    }

    @Test
    public void testSearchCompanyFromApi_ClientError() {
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(String.class))).thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Bad Request"));
        when(apiConfig.getSearchUrl()).thenReturn("http://example.com/api/company/search");

        CompanySearchRequest request = new CompanySearchRequest("Test Company", "123456");
        String apiKey = "test-api-key";

        assertThrows(ApiException.class, () -> companySearchService.searchCompany(request, apiKey, false));
    }
}
