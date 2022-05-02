package com.technews.controller;

import com.technews.model.Post;
import com.technews.model.User;
import com.technews.repository.UserRepository;
import com.technews.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

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
        List<User> userList = repository.findAll();
        for (User u : userList) {
            List<Post> postList = u.getPosts();
            for (Post p : postList) {
                p.setVoteCount(voteRepository.countVotesByPostId((p.getId())));
            }
        }
        return userList;
    }

    @GetMapping("/api/users/{id}")
    // @pathVariable lets you enter the int id into the request URL as a parameter
    public User getUserById(@PathVariable Integer id) {
        User returnUser = repository.getById(id);
        List<Post> postList = returnUser.getPosts();
        for (Post p : postList) {
            p.setVoteCount(voteRepository.countVotesByPostId(p.getId()));
        }
        return returnUser;
    }
}
