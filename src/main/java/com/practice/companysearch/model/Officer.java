package com.practice.companysearch.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.practice.companysearch.constants.ApplicationConstants;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"name", "officer_role", "appointed_on", "address"})
public class Officer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @JsonProperty(ApplicationConstants.JSON_NAME)
    private String name;

    @JsonProperty(ApplicationConstants.JSON_OFFICER_ROLE)
    private String officerRole;

    @JsonProperty(ApplicationConstants.JSON_APPOINTED_ON)
    private String appointedOn;

    @Embedded
    @JsonProperty(ApplicationConstants.JSON_ADDRESS)
    private Address address;

    @ManyToOne
    @JoinColumn(name = "company_number", nullable = false)
    @JsonIgnore
    private CompanyDetails company;
}
