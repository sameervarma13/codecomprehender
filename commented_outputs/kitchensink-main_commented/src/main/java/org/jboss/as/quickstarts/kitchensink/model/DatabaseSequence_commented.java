package org.jboss.as.quickstarts.kitchensink.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigInteger;

@Document(collection = "database_sequences")
/** 
 * Represents a MongoDB document that stores a database sequence identifier and its corresponding value. 
 * This class facilitates the management of unique sequence numbers within the application. 
 */

public class DatabaseSequence {
    @Id
    private String id;

    private BigInteger sequence;

/**
 * Retrieves ID value.
 *
 * @return ID value as string
 */

    public String getId() {
        return id;
    }

/**
 * Sets ID for the object.
 *
 * @param id ID to set
 */

    public void setId(String id) {
        this.id = id;
    }

/**
 * Retrieves current sequence value.
 *
 * @return Current sequence value
 */

    public BigInteger getSequence() {
        return sequence;
    }

/**
 * Sets the sequence value.
 *
 * @param sequence New sequence value
 */

    public void setSequence(BigInteger sequence) {
        this.sequence = sequence;
    }
}
