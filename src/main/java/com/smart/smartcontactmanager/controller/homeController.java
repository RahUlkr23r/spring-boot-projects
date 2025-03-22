package com.smart.smartcontactmanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.smart.smartcontactmanager.dao.UserRepository;
import com.smart.smartcontactmanager.entities.User;
import com.smart.smartcontactmanager.helper.Message;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class homeController { // Class name should follow PascalCase convention

     @Autowired
    private PasswordEncoder passwordEncoder; // Ensure it's of type PasswordEncoder

    public void someMethod() {
        String encodedPassword = passwordEncoder.encode("myPassword");
        System.out.println("Encoded Password: " + encodedPassword);
    }
    @Autowired
    private UserRepository userRepository;

    @RequestMapping("/")
    public String home(Model model) {
        model.addAttribute("title", "Home - Smart Contact Manager");
        return "index"; // Maps to home.html in templates folder
    }

    @RequestMapping("/about")
    public String about(Model model) {
        model.addAttribute("title", "About - Smart Contact Manager");
        return "about"; // Maps to about.html in templates folder
    }

    @RequestMapping("/register")
    public String register(Model model) {
        model.addAttribute("title", "Register - Smart Contact Manager");
        model.addAttribute("user", new User());
        return "/register"; // Maps to register.html in the templates folder
    }


    //for login 
    @GetMapping("/signin") // Match security configuration
    public String login(Model model) {
        model.addAttribute("title", "login - Smart Contact Manager");
        return "/signin"; // Ensure "login.html" exists in templates
    }



    @GetMapping("/logout") // Match security configuration
    public String loginout() {
        return "/signin"; // Ensure "login.html" exists in templates
    }

    @Transactional
    @RequestMapping(value = "/do_register", method = RequestMethod.POST)
    public String registerUser(
            @Valid @ModelAttribute("user") User user,
            BindingResult result1,
            @RequestParam(value = "agreement", defaultValue = "false") boolean agreement,
            Model model,
            HttpSession session) {   
    
        try {    
            // Check if the user agreed to the terms
            if (!agreement) {
                throw new Exception("You have not agreed to the terms and conditions.");
            }
    
            // Check for validation errors
            if (result1.hasErrors()) {
                model.addAttribute("user", user);
                return "register";
            }
    
            // Check if email already exists in the database
            User existingUser = userRepository.getUserByUserName(user.getEmail());
            if (existingUser != null) {
                session.setAttribute("message", new Message("Email already exists! Please use a different email.", "alert-danger"));
                return "register"; // Return to the registration form
            }
    
            // Set default user attributes
            user.setRole("ROLE_USER");
            user.setEnabled(true);

            user.setImageUrl("user.png");

            user.setPassword(passwordEncoder.encode(user.getPassword())); // Encrypt password
    
            // Save user and clear the form
            userRepository.save(user);
            model.addAttribute("user", new User());
    
            // Set success message in the session
            session.setAttribute("message", new Message("Successfully Registered", "alert-success"));
    
            return "redirect:/signin"; // Redirect to avoid form resubmission
    
        } catch (Exception e) {
            // Handle exceptions
            e.printStackTrace();
            model.addAttribute("user", user);
            session.setAttribute("message", new Message("Something went wrong: " + e.getMessage(), "alert-danger"));
            return "register";
        }
    }
    

    // Method to remove the message from the session
    public void removeMessageFromSession() {
        try {
            HttpSession session = ((ServletRequestAttributes) RequestContextHolder
                    .getRequestAttributes()).getRequest().getSession(false);

            if (session != null) {
                session.removeAttribute("message");
            }
        } catch (Exception e) {
            e.printStackTrace(); // Log the exception for debugging
        }
    }
}












// Annotation	Purpose	Example
// @Controller	Marks the class as a Spring MVC controller.	@Controller public class HomeController {}
// @Autowired	Injects dependencies automatically.	@Autowired private UserRepository userRepository;
// @RequestMapping	Maps requests to methods (GET/POST).	@RequestMapping("/register")
// @GetMapping	Maps GET requests.	@GetMapping("/signin")
// @Transactional	Ensures database operations execute in a single transaction.	@Transactional public void registerUser() {}
// @Valid	Triggers validation on an object.	@Valid @ModelAttribute("user") User user
// @ModelAttribute	Maps request parameters to an object.	@ModelAttribute("user") User user
// @RequestParam	Extracts a single request parameter.	@RequestParam(value="email") String email
// @RequestContextHolder	Provides access to the current request.	RequestContextHolder.getRequestAttributes()

