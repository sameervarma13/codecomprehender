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
package org.jboss.as.quickstarts.kitchensink.data;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.event.Reception;
import org.jboss.as.quickstarts.kitchensink.model.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
/**
 * The DataManager class is responsible for managing data operations within the application, 
 * including data retrieval and storage. It serves as a central point for data-related 
 * functionalities, ensuring efficient access and manipulation of data resources.
 */

public class MemberListProducer {
    private final MemberRepository memberRepository;
    private List<Member> members;

    @Autowired
    public MemberListProducer(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

/**
 * Retrieves list of members.
 *
 * @return List of members
 */

    public List<Member> getMembers() {
        return members;
    }

/**
 * Handles changes to the member list.
 *
 * @param member Updated member information
 */

    public void onMemberListChanged(@Observes(notifyObserver = Reception.IF_EXISTS) final Member member) {
        retrieveAllMembersOrderedByName();
    }

    @PostConstruct
/**
 * Retrieves all members ordered by name.
 *
 * @return List of members ordered by name
 */

    public void retrieveAllMembersOrderedByName() {
        members = memberRepository.findAllByOrderByNameAsc();
    }
}
