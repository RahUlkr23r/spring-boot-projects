// package com.smart.smartcontactmanager.controller;

// import java.util.Random;

// import org.springframework.stereotype.Controller;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestParam;

// import com.smart.smartcontactmanager.helper.Message;

// import jakarta.servlet.http.HttpSession;

// @Controller
// public class forgetController {
//     Random random =new Random(1000);
//     @RequestMapping("/forget")
//     public String openingEmailForm(){

//         return "forget_form";
//     }


//     @PostMapping("/process-forget")
//     public String sendOtp(@RequestParam("email") String email, HttpSession session){


// int otp = random.nextInt(999999);
// System.out.println(otp);


// session.setAttribute("message", new Message("Otp sent ", "success"));
//         return "enter-otp";
//     }



    
//     @PostMapping("/process-forget")
//     public String processOtp(@RequestParam("email") String email, HttpSession session){


// int otp = random.nextInt(999999);
// System.out.println(otp);


// session.setAttribute("message", new Message("Otp sent ", "success"));
//         return "forget";
//     }
   

// }
