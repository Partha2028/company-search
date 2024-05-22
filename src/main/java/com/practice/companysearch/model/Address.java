package com.practice.companysearch.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.practice.companysearch.constants.ApplicationConstants;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"premises", "locality", "address_line_1", "country", "postal_code"})
public class Address {
    @JsonProperty(ApplicationConstants.JSON_LOCALITY)
    private String locality;

    @JsonProperty(ApplicationConstants.JSON_POSTAL_CODE)
    private String postalCode;

    @JsonProperty(ApplicationConstants.JSON_PREMISES)
    private String premises;

    @JsonProperty(ApplicationConstants.JSON_ADDRESS_LINE_1)
    private String addressLine1;

    @JsonProperty(ApplicationConstants.JSON_COUNTRY)
    private String country;
}
