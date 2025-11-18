package com.project.ecom_application.dto;

import lombok.Data;

@Data
public class UserRequest {

    private String firstName;

    private String lastName;

    private String email;

    private String phoneNumber;

    private AddressDto address;
}
