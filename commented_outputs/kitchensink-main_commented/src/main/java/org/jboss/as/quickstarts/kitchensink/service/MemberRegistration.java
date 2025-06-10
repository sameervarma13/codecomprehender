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
package org.jboss.as.quickstarts.kitchensink.service;

import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoClient;
import org.jboss.as.quickstarts.kitchensink.data.MemberRepository;
import org.jboss.as.quickstarts.kitchensink.model.DatabaseSequence;
import org.jboss.as.quickstarts.kitchensink.model.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Objects;
import java.util.logging.Logger;

@Service
public class MemberRegistration {
    private final Logger log;

    private final MongoOperations mongoOperations;

    private final MemberRepository memberRepository;

    @Autowired
    public MemberRegistration(final MongoOperations mongoOperations, final MemberRepository memberRepository, MongoClient mongo) {
        log = Logger.getLogger(getClass().getName());
        this.mongoOperations = mongoOperations;
        this.memberRepository = memberRepository;
    }

    public void register(Member member) throws Exception {
        member.setId(generateSequence(Member.SEQUENCE_NAME));
        try {
            memberRepository.insert(member);
        } catch (MongoWriteException e) {
            throw new Exception(e.getLocalizedMessage());
        }

    }

    private BigInteger generateSequence(String sequenceName) {
        DatabaseSequence counter = mongoOperations.findAndModify(
                Query.query(Criteria.where("_id").is(sequenceName)),
                new Update().inc("sequence", 1),
                FindAndModifyOptions.options().returnNew(true).upsert(true),
                DatabaseSequence.class
        );
        return !Objects.isNull(counter) ? counter.getSequence() : BigInteger.ONE;
    }
}
