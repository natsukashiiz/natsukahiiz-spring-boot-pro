package com.natsukashiiz.iiserverapi.entity;

import lombok.Data;
import lombok.NonNull;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import javax.validation.constraints.Email;

@Document(collection = "customers")
@Data
public class Customer {
    @Id
    private String id;
    @NonNull
    private String firstName;
    @NonNull
    private String lastName;
    @Email
    private String email;
    @NonNull
    private Integer age;
}
