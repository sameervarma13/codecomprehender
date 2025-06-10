/*
 * JBoss, Home of Professional Open Source
 * Copyright 2015, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.as.quickstarts.kitchensink.model;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.math.BigInteger;

@Document(collection = "members")
/** 
 * Represents a model for a kitchen sink application, encapsulating user data 
 * with validation constraints for fields such as email and numeric values. 
 */

public class Member implements Serializable {

    @Transient
    public static final String SEQUENCE_NAME = "members_sequence";

    @Id
    private BigInteger id;

    @NotEmpty
    @Email
    @Indexed(unique = true)
    private String email;

    @NotEmpty
    @Size(min = 1, max = 25)
    @Pattern(regexp = "[^0-9]*", message = "Must not contain numbers")
    private String name;

    @NotNull
    @Size(min = 10, max = 12)
    @Digits(fraction = 0, integer = 12)
    private String phoneNumber;

/**
 * Retrieves ID value.
 *
 * @return ID value
 */

    public BigInteger getId() {
        return id;
    }

/**
 * Sets ID for the object.
 *
 * @param id New ID value
 */

    public void setId(BigInteger id) {
        this.id = id;
    }

/**
 * Retrieves email address.
 *
 * @return Email address
 */

    public String getEmail() {
        return email;
    }

/**
 * Sets user's email.
 *
 * @param email User's email
 */

    public void setEmail(String email) {
        this.email = email;
    }

/**
 * Retrieves user's name.
 *
 * @return User's name
 */

    public String getName() {
        return name;
    }

/**
 * Sets user's name.
 *
 * @param name User's name
 */

    public void setName(String name) {
        this.name = name;
    }

/**
 * Retrieves phone number.
 *
 * @return Phone number
 */

    public String getPhoneNumber() {
        return phoneNumber;
    }

/**
 * Sets phone number.
 *
 * @param phoneNumber New phone number
 */

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
