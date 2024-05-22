# Company Search Application

## Overview

This is a Spring Boot application that implements a company search functionality using the TruProxyAPI. It exposes an endpoint to search for companies and their officers by name or registration number.

## Getting Started

To get started with the project, follow these steps:

1. Clone the repository to your local machine.
2. Open the project in your preferred IDE.
3. Build and run the application.

## Endpoints

### Search Company

- **URL:** `/api/company/search`
- **Method:** `POST`
- **Request Body:**
  ```json
  {
      "companyName": "BBC LIMITED",
      "companyNumber": "06500244"
  }

- **Request Headers:** `x-api-key: Your API key`
- **Query Parameters:** `activeOnly (optional, default: false): Filter to return only active companies`


- **Response:**
  ```json
    {
    "total_results": 1,
    "items": 
        [
            {
            "company_number": "06500244",
            "company_type": "ltd",
            "title": "BBC LIMITED",
            "company_status": "active",
            "date_of_creation": "2008-02-11",
            "address": {
                "locality": "Retford",
                "postal_code": "DN22 0AD",
                "premises": "Boswell Cottage Main Street",
                "address_line_1": "North Leverton",
                "country": "England"
            },
            "officers": 
                [
                    {
                    "name": "BOXALL, Sarah Victoria",
                    "officer_role": "secretary",
                    "appointed_on": "2008-02-11",
                    "address": {
                        "premises": "5",
                        "locality": "London",
                        "address_line_1": "Cranford Close",
                        "country": "England",
                        "postal_code": "SW20 0DP"
                        }
                    }
                ]
            }
        ]
    }
