package com.practice.companysearch.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.practice.companysearch.constants.ApplicationConstants;
import com.practice.companysearch.model.Address;
import com.practice.companysearch.model.CompanyDetails;
import com.practice.companysearch.model.Officer;

import java.util.ArrayList;
import java.util.List;

public class JsonParserUtil {

    public static CompanyDetails parseCompanyDetails(JsonNode companyNode) {
        CompanyDetails companyDetails = new CompanyDetails();
        companyDetails.setCompanyNumber(getSafeText(companyNode, ApplicationConstants.JSON_COMPANY_NUMBER));
        companyDetails.setCompanyType(getSafeText(companyNode, ApplicationConstants.JSON_COMPANY_TYPE));
        companyDetails.setTitle(getSafeText(companyNode, ApplicationConstants.JSON_TITLE));
        companyDetails.setCompanyStatus(getSafeText(companyNode, ApplicationConstants.JSON_COMPANY_STATUS));
        companyDetails.setDateOfCreation(getSafeText(companyNode, ApplicationConstants.JSON_DATE_OF_CREATION));
        companyDetails.setAddress(parseAddress(companyNode.path(ApplicationConstants.JSON_ADDRESS)));
        return companyDetails;
    }

    public static Address parseAddress(JsonNode addressNode) {
        return new Address(
                getSafeText(addressNode, ApplicationConstants.JSON_LOCALITY),
                getSafeText(addressNode, ApplicationConstants.JSON_POSTAL_CODE),
                getSafeText(addressNode, ApplicationConstants.JSON_PREMISES),
                getSafeText(addressNode, ApplicationConstants.JSON_ADDRESS_LINE_1),
                getSafeText(addressNode, ApplicationConstants.JSON_COUNTRY));
    }

    public static List<Officer> parseOfficers(JsonNode officersNode, CompanyDetails company) {
        List<Officer> officers = new ArrayList<>();
        officersNode.forEach(officerNode -> {
            if (isActiveOfficer(officerNode)) {
                Officer officer = new Officer(
                        null,
                        getSafeText(officerNode, ApplicationConstants.JSON_NAME),
                        getSafeText(officerNode, ApplicationConstants.JSON_OFFICER_ROLE),
                        getSafeText(officerNode, ApplicationConstants.JSON_APPOINTED_ON),
                        parseAddress(officerNode.path(ApplicationConstants.JSON_ADDRESS)),
                        company);
                officers.add(officer);
            }
        });
        return officers;
    }

    private static boolean isActiveOfficer(JsonNode officerNode) {
        return officerNode.path(ApplicationConstants.JSON_RESIGNED_ON).isMissingNode() || officerNode.path(ApplicationConstants.JSON_RESIGNED_ON).asText().isEmpty();
    }

    private static String getSafeText(JsonNode node, String key) {
        JsonNode valueNode = node.path(key);
        return valueNode.isMissingNode() ? "" : valueNode.asText();
    }
}
