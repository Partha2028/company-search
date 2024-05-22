package com.practice.companysearch.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanySearchRequest {
    private String companyName;
    private String companyNumber;
}
