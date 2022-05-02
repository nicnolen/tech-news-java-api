package com.technews.controller;

import com.technews.model.Comment;
import com.technews.model.Post;
import com.technews.model.User;
import com.technews.repository.CommentRepository;
import com.technews.repository.PostRepository;
import com.technews.repository.UserRepository;
import com.technews.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

// indicate that these controllers will control flow for the front-end user experience
@Controller
public class HomePageController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    VoteRepository voteRepository;

    @Autowired
    CommentRepository commentRepository;

    // login route
    @GetMapping("/login")
    // Model model is remapping Model to the model variable
    public String login(Model model, HttpServletRequest request) {
        if (request.getSession(false) != null) {
            return "redirect:/";
        }
        // addAttribute() sends information to Thymeleaf
        model.addAttribute("user", new User());
        return "login";
    }

    // Logout route
    @GetMapping("/users/logout")
    public String logout(HttpServletRequest request) {
        if (request.getSession(false) != null) {
            request.getSession().invalidate();
        }
        return "redirect:/login";
    }

    // Homepage route
    @GetMapping("/")
    public String homepageSetup(Model model, HttpServletRequest request) {
        User sessionUser = new User();
        if (request.getSession(false) != null) {
            sessionUser = (User) request.getSession().getAttribute("SESSION_USER");
            model.addAttribute("loggedIn", sessionUser.isLoggedIn());
        } else {
            model.addAttribute("loggedIn", false);
        }
        List<Post> postList = postRepository.findAll();
        for (Post p : postList) {
            p.setVoteCount(voteRepository.countVotesByPostId(p.getId()));
            User user = userRepository.getById(p.getUserId());
            p.setUserName(user.getUsername());
        }
        model.addAttribute("postList", postList);
        model.addAttribute("loggedIn", sessionUser.isLoggedIn());
        // "point" and "points" attributes refer to upvotes.
        model.addAttribute("point", "point");
        model.addAttribute("points", "points");
        return "homepage";
    }

    // Dashboard route
    @GetMapping("/dashboard")
    public String dashboardPageSetup(Model model, HttpServletRequest request) throws Exception {
        if (request.getSession(false) != null) {
            setupDashboardPage(model, request);
            return "dashboard";
        } else {
            model.addAttribute("user", new User());
            return "login";
        }
    }
    // endpoint for if a user tries to make a post but forgets to include a post title or link
    @GetMapping("/dashboardEmptyTitleAndLink")
    public String dashboardEmptyTitleAndLinkHandler(Model model, HttpServletRequest request) throws Exception {
        setupDashboardPage(model, request);
        model.addAttribute("notice", "To create a post the Title and Link must be populated!");
        return "dashboard";
    }
    @GetMapping("/singlePostEmptyComment/{id}")
    public String singlePostEmptyCommentHandler(@PathVariable int id, Model model, HttpServletRequest request) {
        setupSinglePostPage(id, model, request);
        model.addAttribute("notice", "To add a comment you must enter the comment in the comment text area!");
        return "single-post";
    }
    @GetMapping("/post/{id}")
    public String singlePostPageSetup(@PathVariable int id, Model model, HttpServletRequest request) {
        setupSinglePostPage(id, model, request);
        return "single-post";
    }
    @GetMapping("/editPostEmptyComment/{id}")
    public String editPostEmptyCommentHandler(@PathVariable int id, Model model, HttpServletRequest request) {
        if (request.getSession(false) != null) {
            setupEditPostPage(id, model, request);
            model.addAttribute("notice", "To add a comment you must enter the comment in the comment text area!");
            return "edit-post";
        } else {
            model.addAttribute("user", new User());
            return "login";
        }
    }
    @GetMapping("/dashboard/edit/{id}")
    public String editPostPageSetup(@PathVariable int id, Model model, HttpServletRequest request) {
        if (request.getSession(false) != null) {
            setupEditPostPage(id, model, request);
            return "edit-post";
        } else {
            model.addAttribute("user", new User());
            return "login";
        }
    }
    // CRUD routes for the dashboard page methods
    public Model setupDashboardPage(Model model, HttpServletRequest request) throws Exception {
        // assign the value of the current user to sessionUser variable
        User sessionUser = (User) request.getSession().getAttribute("SESSION_USER");
        // get the user id and store it in the userId variable
        Integer userId = sessionUser.getId();
        List<Post> postList = postRepository.findAllPostsByUserId(userId);
        // gather all posts made by the specific userId
        for (Post p : postList) {
            p.setVoteCount(voteRepository.countVotesByPostId(p.getId()));
            User user = userRepository.getById(p.getUserId());
            p.setUserName(user.getUsername());
        }
        // pass information to the Thymeleaf pages when the template is called
        // sending current user (sessionUser) to the Thymeleaf dashboard template as a variable called user
        model.addAttribute("user", sessionUser);
        // sending the postList (postList) to the Thymeleaf dashboard template as a variable called postList
        model.addAttribute("postList", postList);
        // sending the logged in user (sessionUser.isLoggedIn) to the Thymeleaf dashboard template as a variable called loggedIn
        model.addAttribute("loggedIn", sessionUser.isLoggedIn());
        // sending the new post to the Thymeleaf dashboard template as a variable called post
        model.addAttribute("post", new Post());
        return model;
    }
    // CRUD route for the single post page
    public Model setupSinglePostPage(int id, Model model, HttpServletRequest request) {
        if (request.getSession(false) != null) {
            User sessionUser = (User) request.getSession().getAttribute("SESSION_USER");
            model.addAttribute("sessionUser", sessionUser);
            model.addAttribute("loggedIn", sessionUser.isLoggedIn());
        }
        Post post = postRepository.getById(id);
        post.setVoteCount(voteRepository.countVotesByPostId(post.getId()));
        User postUser = userRepository.getById(post.getUserId());
        post.setUserName(postUser.getUsername());
        List<Comment> commentList = commentRepository.findAllCommentsByPostId(post.getId());
        model.addAttribute("post", post);
        model.addAttribute("commentList", commentList);
        model.addAttribute("comment", new Comment());
        return model;
    }
    // CRUD route for the edit post page
    public Model setupEditPostPage(int id, Model model, HttpServletRequest request) {
        if (request.getSession(false) != null) {
            User sessionUser = (User) request.getSession().getAttribute("SESSION_USER");
            Post returnPost = postRepository.getById(id);
            User tempUser = userRepository.getById(returnPost.getUserId());
            returnPost.setUserName(tempUser.getUsername());
            returnPost.setVoteCount(voteRepository.countVotesByPostId(returnPost.getId()));
            List<Comment> commentList = commentRepository.findAllCommentsByPostId(returnPost.getId());
            model.addAttribute("post", returnPost);
            model.addAttribute("loggedIn", sessionUser.isLoggedIn());
            model.addAttribute("commentList", commentList);
            model.addAttribute("comment", new Comment());
        }
        return model;
    }
}
