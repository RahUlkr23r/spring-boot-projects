

package com.smart.smartcontactmanager.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


import com.smart.smartcontactmanager.dao.UserRepository;
import com.smart.smartcontactmanager.dao.contactRepository;
import com.smart.smartcontactmanager.entities.User;
import com.smart.smartcontactmanager.entities.contact;
import com.smart.smartcontactmanager.helper.Message;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/user")
public class UserController {

@Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private contactRepository contactRepository;

    // Add common user data to all pages
    @ModelAttribute
    public void addCommonData(Model model, Principal principal) {
        if (principal != null) { 
            String userName = principal.getName();
            System.out.println("USERNAME: " + userName);

            User user = userRepository.getUserByUserName(userName);

            if (user != null) {
                model.addAttribute("user", user);
                System.out.println("USER: " + user);
            } else {
                System.out.println("User not found!");
            }
        }
    }

    // Dashboard
    @GetMapping("/index")
    public String dashboard(Model model) {
        model.addAttribute("title", "Dashboard");
        return "normal/user_dashboard";
    }

    // Open Add Contact Form
    @GetMapping("/add-contact")
    public String openAddContactForm(Model model) {
        model.addAttribute("title", "Add Contact");
        model.addAttribute("contact", new contact());
        return "normal/add_contact_form";
    }

    @PostMapping("/process-contact")
    public String processContact(@Valid @ModelAttribute contact contact, 
                                 @RequestParam(value = "imageFile", required = false) MultipartFile file,
                                 Principal principal,
                                 HttpSession session) {
        try {
            // Handle image upload
            if (file != null && !file.isEmpty()) { 
                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                String uploadDir = "src/main/resources/static/images/"; 
                Path uploadPath = Paths.get(uploadDir);
    
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
    
                Path filePath = uploadPath.resolve(fileName);
                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
    
                contact.setImage(fileName);
            } else {
                contact.setImage("default.png");
            }
    
            // Get logged-in user
            String name = principal.getName();
            User user = this.userRepository.getUserByUserName(name);
    
            if (user == null) {
                throw new RuntimeException("User not found!");
            }
    
            // Associate contact with user
            contact.setUser(user);
            user.getContacts().add(contact);
    
            // Save contact
            contact savedContact = contactRepository.save(contact);
            System.out.println("Saved Contact: " + savedContact);
    
            // Success message
            session.setAttribute("message", new Message("Contact is added", "success"));
    
        } catch (IOException e) {
            e.printStackTrace();
            session.setAttribute("message", new Message("Oops!! Try again", "danger"));
        }
    
        return "normal/add_contact_form";
    }
    
    // Show Contacts with Pagination
    @GetMapping("/show-contacts/{page}")
    public String showContact(@PathVariable("page") Integer page, Model model, Principal principal) {
        model.addAttribute("title", "Show User Contacts");

        String userName = principal.getName();
        User user = this.userRepository.getUserByUserName(userName);

        if (user != null) {
            Pageable pageable = PageRequest.of(page, 8);
            Page<contact> contacts = this.contactRepository.findContactByUser(user.getId(), pageable);
            model.addAttribute("contacts", contacts);
            model.addAttribute("currentPage", page);
            model.addAttribute("totalpages", contacts.getTotalPages());
        } else {
            model.addAttribute("contacts", List.of()); // Empty list to prevent null errors
        }

        return "normal/show_contacts";
    }
//showing the particular contact detail
@RequestMapping("/contact/{cId}")
public String  showContactDetail(@PathVariable("cId")Integer cId,Model model)
{
System.out.println("CID"+cId);

  Optional<contact> contactOptional= this.contactRepository.findById(cId);
contact contact=contactOptional.get();
model.addAttribute("contact", contact);

    return "normal/particular_contact";

    
}// delete contact handler

@GetMapping("/delete/{cId}")
public String deleteContact(@PathVariable("cId") Integer cId, Model model, HttpSession session, Principal principal) {
    Optional<contact> contactOptional = this.contactRepository.findById(cId);

    if (contactOptional.isPresent()) {
        contact contact = contactOptional.get();

        // Get the currently logged-in user
        String userName = principal.getName();
        User user = this.userRepository.getUserByUserName(userName);

        if (user != null && user.getContacts().contains(contact)) {
            // Unlink contact from user
            user.getContacts().remove(contact);
            contact.setUser(null);

            // Delete the image if it's not the default image
            String imageFileName = contact.getImage();
            if (imageFileName != null && !imageFileName.equals("default.png")) {
                String uploadDir = "src/main/resources/static/images/"; // Image directory
                Path imagePath = Paths.get(uploadDir, imageFileName);

                try {
                    Files.deleteIfExists(imagePath); // Delete image file
                } catch (IOException e) {
                    e.printStackTrace();
                    session.setAttribute("message", new Message("Error deleting image file!", "danger"));
                }
            }

            // Delete the contact
            this.contactRepository.delete(contact);

            // Success message
            session.setAttribute("message", new Message("Contact deleted successfully", "success"));
        } else {
            session.setAttribute("message", new Message("Unauthorized action!", "danger"));
        }
    } else {
        session.setAttribute("message", new Message("Contact not found!", "danger"));
    }

    return "redirect:/user/show-contacts/0";
}

    // Opening update form
    @GetMapping("/edit/{cId}")
    public String updateContactForm(@PathVariable("cId") Integer cId, Model model, HttpSession session) {
        Optional<contact> contactOptional = contactRepository.findById(cId);

        if (contactOptional.isPresent()) {
            model.addAttribute("contact", contactOptional.get());
            model.addAttribute("title", "Update Contact");
            return "normal/update_form"; // View page for updating contact
        } else {
            session.setAttribute("message", new Message("Contact not found!", "danger"));
            return "redirect:/user/show-contacts/0";
        }
    }
    @PostMapping("/process-update")
    public String processUpdate(
            @ModelAttribute contact contact,
            @RequestParam(value = "imageFile", required = false) MultipartFile file,
            HttpSession session,
            Principal principal,
            Model model
            ) {

                System.out.println("contact name:"+contact.getName());
        try {
            // Get the logged-in user
            String email = principal.getName();
            User loggedInUser = userRepository.getUserByUserName(email);
    
            if (loggedInUser == null) {
                session.setAttribute("message", new Message("User not found!", "danger"));
                return "redirect:/user/show-contacts/0";
            }
    
            // Find the user by ID and user
            Optional<contact> contactOptional = contactRepository.findByIdAndUserId(contact.getcId(), loggedInUser.getId());
    
            if (contactOptional.isPresent()) {
                contact existingContact = contactOptional.get();
    
                // Update contact details
                existingContact.setName(contact.getName());
                existingContact.setPhone(contact.getPhone());
                existingContact.setEmail(contact.getEmail());
                existingContact.setWork(contact.getWork());
                existingContact.setDescription(contact.getDescription());
    
                // Handle new image upload
                if (file != null && !file.isEmpty()) {
                    String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                    String uploadDir = "src/main/resources/static/images/";
                    Path uploadPath = Paths.get(uploadDir);
    
                    // Create upload directory if it doesn't exist
                    if (!Files.exists(uploadPath)) {
                        Files.createDirectories(uploadPath);
                    }
    
                    // Delete old image if it's not the default image
                    if (existingContact.getImage() != null && !existingContact.getImage().equals("default.png")) {
                        Path oldImagePath = uploadPath.resolve(existingContact.getImage());
                        Files.deleteIfExists(oldImagePath);
                    }
    
                    // Save the new image
                    Path filePath = uploadPath.resolve(fileName);
                    Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                    existingContact.setImage(fileName);
                }
    
               // Save updated contact to the database
                contactRepository.save(existingContact);
                session.setAttribute("message", new Message("Contact updated successfully!", "success"));
            } else {
                session.setAttribute("message", new Message("Contact not found!", "danger"));
            }
         } catch (IOException e) {
             e.printStackTrace();
             session.setAttribute("message", new Message("Error updating contact!", "danger"));
        }
    
        //  Redirect to the contacts list page
        return "redirect:/user/show-contacts/0";

    }
    //profillllllee
    @GetMapping("/profile")
    public String yourProfile(Model model){

        return "normal/profile";
 
    }

  // Opening profile update
@GetMapping("/edits/{id}")
public String updateProfile(@PathVariable("id") Integer userId, Model model, HttpSession session) {
    Optional<User> userOptional = userRepository.findById(userId); // Use correct repository name

    if (userOptional.isPresent()) {
        model.addAttribute("user", userOptional.get());
        model.addAttribute("title", "Update Profile");
        return "normal/update_profile"; // View page for updating profile
    } else {
        session.setAttribute("message", new Message("User not found!", "danger"));
        return "redirect:/user/index";
    }
}
@PostMapping("/profile-update")
public String profileUpdate(
        @ModelAttribute User user,
        @RequestParam(value = "imageFile", required = false) MultipartFile file,
        HttpSession session,
        Principal principal
) {
    try {
        // Get the logged-in user
        String email = principal.getName();
        User loggedInUser = userRepository.getUserByUserName(email);

        if (loggedInUser == null) {
            session.setAttribute("message", new Message("User not found!", "danger"));
            return "redirect:/user/profile";
        }

        // Update user details
        loggedInUser.setName(user.getName());
        loggedInUser.setAbout(user.getAbout());

        // Handle image upload
        if (file != null && !file.isEmpty()) {
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            String uploadDir = "src/main/resources/static/images/"; // Store images in 'uploads/' directory
            Path uploadPath = Paths.get(uploadDir);

            // Create directory if not exists
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Delete old image if not default
            if (loggedInUser.getImageUrl() != null && !loggedInUser.getImageUrl().equals("user.png")) {
                Path oldImagePath = uploadPath.resolve(loggedInUser.getImageUrl());
                Files.deleteIfExists(oldImagePath);
            }

            // Save new image
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            loggedInUser.setImageUrl(fileName);
        }

        // Save updated user
        userRepository.save(loggedInUser);
        session.setAttribute("message", new Message("Profile updated successfully!", "success"));

    } catch (IOException e) {
        e.printStackTrace();
        session.setAttribute("message", new Message("Error updating profile!", "danger"));
    }

    return "redirect:/user/profile";
}

//opening setting 
@GetMapping("/setting")
public String openSetting(){
    return "normal/setting";
}


// changing the password 
@PostMapping("/changePassword")
 public String changePassword(@RequestParam("old-password") String oldPassword,
                                 @RequestParam("new-password") String newPassword,
                              
                                  Principal principal,
                                  HttpSession session
                                 ) {
        System.out.println("Old Password: " + oldPassword);
        System.out.println("New Password: " + newPassword);
        String userName =principal.getName();
        User currentUser=this.userRepository.getUserByUserName(userName);
        System.out.println(currentUser.getPassword());
if(this.bCryptPasswordEncoder.matches(oldPassword, currentUser.getPassword()))
{
//chage the passwortd
currentUser.setPassword(this.bCryptPasswordEncoder.encode(newPassword));
this.userRepository.save(currentUser);
session.setAttribute("message", new Message("Your password has been successfully changed!", "success"));
}else{

    session.setAttribute("message", new Message("access denied", "danger"));

}













 ;

        return "/normal/setting";
    }

}











