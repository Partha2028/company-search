package com.practice.companysearch.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"totalResults", "items"})
public class CompanySearchResponse {
    private int totalResults;
    private List<CompanyDetails> items;
}
