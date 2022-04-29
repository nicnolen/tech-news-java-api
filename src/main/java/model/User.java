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

    // lists are collections of objects of the same type. @OneToMany creates the relationships between tables automatically
    // Use FetchType.EAGER to gather all necessary information for the list immediately after being created. CAN ONLY BE ONE IN A LIST
    @OneToMany(mappedBy = "userId", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Post> posts;
    // Need to use FetchType.LAZY to gather information for the list as needed
    @OneToMany(mappedBy = "userId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<vote> votes;
    @OneToMany(mappedBy = "userId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Comment> comments;
}
