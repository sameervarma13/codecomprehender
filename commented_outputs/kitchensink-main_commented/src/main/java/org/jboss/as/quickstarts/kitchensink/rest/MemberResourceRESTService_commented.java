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
package org.jboss.as.quickstarts.kitchensink.rest;

import jakarta.validation.ValidationException;
import org.jboss.as.quickstarts.kitchensink.data.MemberRepository;
import org.jboss.as.quickstarts.kitchensink.model.Member;
import org.jboss.as.quickstarts.kitchensink.service.MemberRegistration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigInteger;
import java.util.List;
import java.util.logging.Logger;

/**
 * JAX-RS Example
 * <p/>
 * This class produces a RESTful service to read/write the contents of the members table.
 */
@RestController
public class MemberResourceRESTService {
    private final Logger log;
    private final MemberRepository repository;
    private final MemberRegistration registration;


    @Autowired
    public MemberResourceRESTService(Logger log, MemberRepository repository, MemberRegistration registration) {
        this.log = log;
        this.repository = repository;
        this.registration = registration;
    }

    @GetMapping({"/api/members"})
    @ResponseBody
    public List<Member> listAllMembers() {
        return repository.findAll();
    }

    @GetMapping("/api/members/{id:[0-9]+}")
    @ResponseBody
    public Member lookupMemberById(@PathVariable("id") long id) {
        Member member = repository.findById(BigInteger.valueOf(id));
        if (member == null) {
            ResponseStatusException e = new ResponseStatusException(HttpStatus.NOT_FOUND, "Member not found");
            log.throwing(MemberResourceRESTService.class.getName(), "deleteMemberById", e);
            throw e;
        }
        return member;
    }

    @DeleteMapping("/api/members/{id:[0-9]+}")
    public void deleteMemberById(@PathVariable("id") long id) {
        Member member = repository.findById(BigInteger.valueOf(id));
        if (member == null) {
            ResponseStatusException e = new ResponseStatusException(HttpStatus.NOT_FOUND, "Member not found");
            log.throwing(MemberResourceRESTService.class.getName(), "deleteMemberById", e);
            throw e;
        }
        repository.deleteMemberById(BigInteger.valueOf(id));
    }

    /**
     * Creates a new member from the values provided. Performs validation, and will return a JAX-RS response with either 200 ok,
     * or with a map of fields, and related errors.
     */
    @PostMapping("/api/members")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public Member createMember(@RequestBody Member member) {
        try {
            validateMember(member);
            registration.register(member);
        } catch (ValidationException e) {
            ResponseStatusException error = new ResponseStatusException(HttpStatus.CONFLICT, "Email is already in use by another member");
            log.throwing(this.getClass().getName(), "createMember", error);
            throw error;
        } catch (Exception e) {
            ResponseStatusException error = new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
            log.throwing(this.getClass().getName(), "createMember", error);
            throw error;
        }
        return member;
    }

    /**
     * <p>
     * Validates the given Member variable and throws validation exception if the error is caused because an existing member with the same email is registered
     * </p>
     *
     * @param member Member to be validated
     * @throws ValidationException If member with the same email already exists
     */
    private void validateMember(Member member) throws ValidationException {
        // Check the uniqueness of the email address
        String email = member.getEmail();
        if (emailAlreadyExists(email)) {
            ValidationException e = new ValidationException("Member already exists using email: " + email);
            log.throwing(this.getClass().getName(), "validateMember", e);
            throw e;
        }
    }

    /**
     * Checks if a member with the same email address is already registered. Returns a more friendly error response
     *
     * @param email The email to check
     * @return True if the email already exists, and false otherwise
     */
    public boolean emailAlreadyExists(String email) {
        Member member = null;
        try {
            member = repository.findByEmail(email);
        } catch (Exception e) {
            // ignore
        }
        return member != null;
    }
}
