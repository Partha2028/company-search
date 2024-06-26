package com.practice.companysearch.repository;

import com.practice.companysearch.model.CompanyDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends JpaRepository<CompanyDetails, String> {
}
