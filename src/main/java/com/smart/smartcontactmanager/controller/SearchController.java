package com.smart.smartcontactmanager.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.RestController;

import com.smart.smartcontactmanager.dao.UserRepository;
import com.smart.smartcontactmanager.dao.contactRepository;
import com.smart.smartcontactmanager.entities.contact;
import com.smart.smartcontactmanager.entities.User;
@CrossOrigin(origins = "*")
@RestController

public class SearchController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private contactRepository contactRepository;

    // Search Handler
    @GetMapping("/search/{query}")
    public ResponseEntity<?> search(@PathVariable("query") String query, Principal principal) {
        System.out.println("Search Query: " + query);

        // Check if user is logged in
        if (principal == null) {
            return ResponseEntity.status(401).body("Unauthorized: User not logged in");
        }

        // Get the currently logged-in user
        User user = this.userRepository.getUserByUserName(principal.getName());
        
        if (user == null) {
            return ResponseEntity.status(404).body("User not found");
        }

        // Search contacts by name for this user
        List<contact> contacts = this.contactRepository.findByNameContainingIgnoreCaseAndUser(query, user);

        return ResponseEntity.ok(contacts);
    }
}
