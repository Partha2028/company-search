package com.practice.companysearch.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice.companysearch.config.ApiConfig;
import com.practice.companysearch.constants.ApplicationConstants;
import com.practice.companysearch.exception.ApiException;
import com.practice.companysearch.model.CompanyDetails;
import com.practice.companysearch.model.CompanySearchRequest;
import com.practice.companysearch.model.CompanySearchResponse;
import com.practice.companysearch.model.Officer;
import com.practice.companysearch.repository.CompanyRepository;
import com.practice.companysearch.util.JsonParserUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CompanySearchServiceImpl implements CompanySearchService {

    private static final Logger logger = LoggerFactory.getLogger(CompanySearchServiceImpl.class);
    private final RestTemplate restTemplate;
    private final ApiConfig apiConfig;
    private final CompanyRepository companyRepository;

    @Autowired
    public CompanySearchServiceImpl(RestTemplate restTemplate, ApiConfig apiConfig, CompanyRepository companyRepository) {
        this.restTemplate = restTemplate;
        this.apiConfig = apiConfig;
        this.companyRepository = companyRepository;
    }

    @Override
    public CompanySearchResponse searchCompany(CompanySearchRequest request, String apiKey, boolean activeOnly) {
        Optional<CompanyDetails> optionalCompany = companyRepository.findById(request.getCompanyNumber());
        if (optionalCompany.isPresent()) {
            return convertToCompanySearchResponse(optionalCompany.get());
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(ApplicationConstants.HEADER_API_KEY, apiKey);

        String url = UriComponentsBuilder.fromHttpUrl(apiConfig.getSearchUrl())
                .queryParam(ApplicationConstants.QUERY_PARAM, Optional.ofNullable(request.getCompanyNumber()).orElse(request.getCompanyName()))
                .toUriString();

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), String.class);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return parseResponse(response.getBody(), apiKey, activeOnly);
            } else {
                logger.warn("No content found or error during the API call.");
                return new CompanySearchResponse(0, new ArrayList<>());
            }
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            logger.error("API request failed with status code: " + e.getStatusCode(), e);
            throw new ApiException("API request failed: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Unexpected error during API request", e);
            throw new ApiException("Unexpected error during API request", e);
        }
    }

    private CompanySearchResponse parseResponse(String responseBody, String apiKey, boolean activeOnly) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode rootNode = objectMapper.readTree(responseBody);
            List<CompanyDetails> companies = new ArrayList<>();
            rootNode.path(ApplicationConstants.ITEMS).forEach(item -> {
                if (!activeOnly || ApplicationConstants.COMPANY_STATUS_ACTIVE.equalsIgnoreCase(item.path(ApplicationConstants.JSON_COMPANY_STATUS).asText())) {
                    CompanyDetails companyDetails = JsonParserUtil.parseCompanyDetails(item);
                    if (companyDetails != null) {
                        List<Officer> officers = fetchOfficers(companyDetails.getCompanyNumber(), apiKey);
                        companyDetails.setOfficers(officers);
                        companyRepository.save(companyDetails);
                        companies.add(companyDetails);
                    }
                }
            });
            return new CompanySearchResponse(companies.size(), companies);
        } catch (JsonProcessingException e) {
            logger.error("Error processing JSON response", e);
            throw new ApiException("Error processing JSON response", e);
        }
    }

    List<Officer> fetchOfficers(String companyNumber, String apiKey) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(ApplicationConstants.HEADER_API_KEY, apiKey);

        String officerUrl = apiConfig.getOfficersUrl();
        if (officerUrl == null) {
            throw new IllegalArgumentException("Officer URL is null");
        }

        officerUrl = UriComponentsBuilder.fromHttpUrl(officerUrl)
                .queryParam(ApplicationConstants.COMPANY_NUMBER, companyNumber)
                .toUriString();

        try {
            ResponseEntity<String> officerResponse = restTemplate.exchange(officerUrl, HttpMethod.GET, new HttpEntity<>(headers), String.class);
            if (officerResponse.getStatusCode().is2xxSuccessful() && officerResponse.getBody() != null) {
                JsonNode officersNode = new ObjectMapper().readTree(officerResponse.getBody()).path(ApplicationConstants.ITEMS);
                return JsonParserUtil.parseOfficers(officersNode, new CompanyDetails(companyNumber, null, null, null, null, null, null));
            } else {
                logger.warn("No officer data found or error during the API call.");
                return new ArrayList<>();
            }
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            logger.error("API request for officers failed with status code: " + e.getStatusCode(), e);
            throw new ApiException("API request for officers failed: " + e.getMessage(), e);
        } catch (JsonProcessingException e) {
            logger.error("Error parsing officer details", e);
            throw new ApiException("Error parsing officer data", e);
        }
    }

    CompanySearchResponse convertToCompanySearchResponse(CompanyDetails company) {
        List<CompanyDetails> companyDetailsList = new ArrayList<>();
        companyDetailsList.add(company);
        return new CompanySearchResponse(1, companyDetailsList);
    }
}
