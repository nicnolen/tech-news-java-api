package model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.persistence.Entity;
import javax.persistence.Table;

// Properties to be ignored when serializing this object to JSON
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
// Marks this as a persistable object, so that the User class can map to a table
@Entity
// Name of the table the class maps to
@Table(name = "user")
public class User {
}
