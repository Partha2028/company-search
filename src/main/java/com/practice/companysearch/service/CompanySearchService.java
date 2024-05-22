package com.practice.companysearch.service;

import com.practice.companysearch.model.CompanySearchRequest;
import com.practice.companysearch.model.CompanySearchResponse;

public interface CompanySearchService {
    CompanySearchResponse searchCompany(CompanySearchRequest request, String apiKey, boolean activeOnly);
}
