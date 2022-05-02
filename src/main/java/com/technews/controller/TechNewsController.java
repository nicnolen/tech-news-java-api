package com.technews.controller;

import com.technews.model.User;
import com.technews.repository.CommentRepository;
import com.technews.repository.PostRepository;
import com.technews.repository.UserRepository;
import com.technews.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class TechNewsController {
    @Autowired
    PostRepository postRepository;

    @Autowired
    VoteRepository voteRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CommentRepository commentRepository;

    // POST Login
    @PostMapping("/users/login")
    public String login(@ModelAttribute User user, Model model, HttpServletRequest request) throws Exception {
        // check if all the fields were properly filled in
        if ((user.getPassword().equals(null) || user.getPassword().isEmpty()) || (user.getEmail().equals(null) || user.getPassword().isEmpty())) {
            model.addAttribute("notice", "Email address and password must be populated in order to login!");
            return "login";
        }
        // populate the sessionUser variable by identifying the email of the user
        User sessionUser = userRepository.findUserByEmail(user.getEmail());
        try {
            // If sessionUser is invalid, running .equals() will throw an error
            if (sessionUser.equals(null)) {
            }
            // We will catch an error and notify client that email address is not recognized
        } catch (NullPointerException e) {
            model.addAttribute("notice", "Email address is not recognized!");
            return "login";
        }
        // Validate Password
        String sessionUserPassword = sessionUser.getPassword();
        boolean isPasswordValid = BCrypt.checkpw(user.getPassword(), sessionUserPassword);
        if(isPasswordValid == false) {
            model.addAttribute("notice", "Password is not valid!");
            return "login";
        }
        sessionUser.setLoggedIn(true);
        request.getSession().setAttribute("SESSION_USER", sessionUser);
        return "redirect:/dashboard";
    }

    // POST AddUser endpoint
    @PostMapping("/users")
    public String signup(@ModelAttribute User user, Model model, HttpServletRequest request) throws Exception {
        if ((user.getUsername().equals(null) || user.getUsername().isEmpty()) || (user.getPassword().equals(null) || user.getPassword().isEmpty()) || (user.getEmail().equals(null) || user.getPassword().isEmpty())) {
            model.addAttribute("notice", "In order to signup username, email address and password must be populated!");
            return "login";
        }
        try {
            // Encrypt password
            user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
            userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            model.addAttribute("notice", "Email address is not available! Please choose a different unique email address.");
            return "login";
        }
        User sessionUser = userRepository.findUserByEmail(user.getEmail());
        try {
            if (sessionUser.equals(null)) {
            }
        } catch (NullPointerException e) {
            model.addAttribute("notice", "User is not recognized!");
            return "login";
        }
        sessionUser.setLoggedIn(true);
        request.getSession().setAttribute("SESSION_USER", sessionUser);
        return "redirect:/dashboard";
    }
}
