package com.practice.companysearch.controller;

import com.practice.companysearch.constants.ApplicationConstants;
import com.practice.companysearch.model.CompanySearchRequest;
import com.practice.companysearch.model.CompanySearchResponse;
import com.practice.companysearch.service.CompanySearchService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/company")
public class CompanySearchController {

    private final CompanySearchService companySearchService;

    public CompanySearchController(CompanySearchService companySearchService) {
        this.companySearchService = companySearchService;
    }

    @PostMapping("/search")
    public CompanySearchResponse searchCompany(
            @RequestBody CompanySearchRequest request,
            @RequestHeader(ApplicationConstants.HEADER_API_KEY) String apiKey,
            @RequestParam(value = ApplicationConstants.ACTIVE_ONLY, defaultValue = "false") boolean activeOnly) {
        return companySearchService.searchCompany(request, apiKey, activeOnly);
    }
}
