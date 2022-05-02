package com.technews.controller;

import com.technews.model.Post;
import com.technews.model.User;
import com.technews.repository.UserRepository;
import com.technews.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Allows Spring to process JSON/XML responses and send JSON/XML objects via the API
@RestController
public class UserController {
    // tells Spring to only instantiate each object as needed by the program
    @Autowired
    UserRepository repository;

    @Autowired
    VoteRepository voteRepository;

    // combines the route ("/api/users") and the type of HTTP method used (GET) to provide the method with a unique endpoint
    @GetMapping("/api/users")
    public List<User> getAllUsers() {
        // get a list of all users and assign it to the userList variable
        List<User> userList = repository.findAll();
        // for every User assigned to the variable u in userList...
        for (User u : userList) {
            // get a list of posts for every User assigned to the variable u in userList
            List<Post> postList = u.getPosts();
            // iterate over each post assigned to the variable p in postList
            for (Post p : postList) {
                // pass in countVotesByPostId() method to count posts by id and use to getId() method to obtain the id of the post
                p.setVoteCount(voteRepository.countVotesByPostId((p.getId())));
            }
        }
        return userList;
    }

    @GetMapping("/api/users/{id}")
    // @pathVariable lets you enter the int id into the request URL as a parameter
    public User getUserById(@PathVariable Integer id) {
        // find a user by id
        User returnUser = repository.getById(id);
        List<Post> postList = returnUser.getPosts();
        for (Post p : postList) {
            p.setVoteCount(voteRepository.countVotesByPostId(p.getId()));
        }
        // return a single user
        return returnUser;
    }

    // allows us to add a user to the database
    @PostMapping("/api/users")
    // @RequestBody annotation maps the body of this request to a transfer object and then deserializes the body onto a Java object for easier use
    public User addUser(@RequestBody User user) {
        // encrypt password
        user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
        // save the user
        repository.save(user);
        // return the new user
        return user;
    }

    // allows us to update a user based on a specific id
    @PutMapping("/api/users/{id}")
    public User updateUser(@PathVariable int id, @RequestBody User user) {
        User tempUser = repository.getById(id);

        if (!tempUser.equals(null)) {
            user.setId(tempUser.getId());
            repository.save(user);
        }
        return user;
    }

    // allows us to delete an associated user
    @DeleteMapping("/api/users/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable int id) {
        repository.deleteById(id);
    }
}

