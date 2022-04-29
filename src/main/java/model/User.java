package model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

// Properties to be ignored when serializing this object to JSON
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
// Marks this as a persistable object, so that the User class can map to a table
@Entity
// Name of the table the class maps to
@Table(name = "user")
public class User {
    // id will be used as the unique identifier
    @Id
    // id will be a generated value and should be generated automatically
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String username;
    // value must be unique
    @Column(unique = true)
    private String email;
    private String password;
    // data will not be persisted in the database
    @Transient
    boolean loggedIn;

    // lists are collections of objects of the same type
    private List<Post> posts;
    private List<vote> votes;
    private List<Comment> comments;
}
