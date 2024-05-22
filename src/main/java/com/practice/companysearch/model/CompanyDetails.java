package com.practice.companysearch.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.practice.companysearch.constants.ApplicationConstants;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"company_number", "company_type", "title", "company_status", "date_of_creation", "address", "officers"})
public class CompanyDetails {
    @Id
    @JsonProperty(ApplicationConstants.JSON_COMPANY_NUMBER)
    private String companyNumber;

    @JsonProperty(ApplicationConstants.JSON_COMPANY_TYPE)
    private String companyType;

    @JsonProperty(ApplicationConstants.JSON_TITLE)
    private String title;

    @JsonProperty(ApplicationConstants.JSON_COMPANY_STATUS)
    private String companyStatus;

    @JsonProperty(ApplicationConstants.JSON_DATE_OF_CREATION)
    private String dateOfCreation;

    @Embedded
    @JsonProperty(ApplicationConstants.JSON_ADDRESS)
    private Address address;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonProperty(ApplicationConstants.JSON_OFFICERS)
    private List<Officer> officers;
}
