package com.practice.companysearch.controller;

import com.practice.companysearch.constants.ApplicationConstants;
import com.practice.companysearch.model.CompanySearchRequest;
import com.practice.companysearch.model.CompanySearchResponse;
import com.practice.companysearch.service.CompanySearchService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class CompanySearchControllerTest {

    @Mock
    private CompanySearchService companySearchService;

    @InjectMocks
    private CompanySearchController companySearchController;

    private MockMvc mockMvc;

    public CompanySearchControllerTest() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(companySearchController).build();
    }

    @Test
    public void testSearchCompany() throws Exception {
        String apiKey = "test-api-key";
        CompanySearchRequest request = new CompanySearchRequest("Test Company", "123456");
        CompanySearchResponse expectedResponse = new CompanySearchResponse(1, null); // Add your expected response here

        when(companySearchService.searchCompany(request, apiKey, false)).thenReturn(expectedResponse);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/company/search")
                .contentType(MediaType.APPLICATION_JSON)
                .header(ApplicationConstants.HEADER_API_KEY, apiKey)
                .param(ApplicationConstants.ACTIVE_ONLY, "false")
                .content("{ \"companyName\": \"Test Company\", \"companyNumber\": \"123456\" }"))
                .andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(200, response.getStatus());

        verify(companySearchService, times(1)).searchCompany(request, apiKey, false);
    }
}
