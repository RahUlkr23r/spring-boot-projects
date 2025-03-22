// package com.smart.smartcontactmanager.entities;

// import jakarta.persistence.Column;
// import jakarta.persistence.Entity;
// import jakarta.persistence.GeneratedValue;
// import jakarta.persistence.GenerationType;
// import jakarta.persistence.Id;
// import jakarta.persistence.ManyToOne;
// import jakarta.persistence.Table;

// @Entity
// @Table(name="CONTACT")
// public class contact {
//     @Id
//     @GeneratedValue(strategy= GenerationType.AUTO)

//     private int cId;
//     private String secondName;
//     private String work;
//     private String email;
//     private String phone;
//     private String image ;
//     @Column(length=50000)
//     private User user;
// @ManyToOne

//     private String description;
//     public int getcId() {
//         return cId;
//     }
//     public void setcId(int cId) {
//         this.cId = cId;
//     }
//     public String getSecondName() {
//         return secondName;
//     }
//     public void setSecondName(String secondName) {
//         this.secondName = secondName;
//     }
//     public String getWork() {
//         return work;
//     }
//     public void setWork(String work) {
//         this.work = work;
//     }
//     public String getEmail() {
//         return email;
//     }
//     public void setEmail(String email) {
//         this.email = email;
//     }
//     public String getPhone() {
//         return phone;
//     }
//     public void setPhone(String phone) {
//         this.phone = phone;
//     }
//     public String getImage() {
//         return image;
//     }
//     public void setImage(String image) {
//         this.image = image;
//     }
//     public String getDescription() {
//         return description;
//     }
//     public void setDescription(String description) {
//         this.description = description;
//     }
//     public User getUser() {
//         return user;
//     }
//     public void setUser(User user) {
//         this.user = user;
//     }

// }



// package com.smart.smartcontactmanager.entities;

// import jakarta.persistence.*;

// @Entity
// @Table(name = "CONTACT")
// public class contact {

//     @Id
//     @GeneratedValue(strategy = GenerationType.AUTO)
//     private int cId;

//     private String name;
//     private String secondName;
//     private String work;
//     private String email;
//     private String phone;
//     private String image;

//     @Column(length = 50000)
//     private String description;

//     @ManyToOne
//     @JoinColumn(name = "user_id")
//     private User user;

//     // Default Constructor (Required for JPA)
//     public contact() {}

//     // Getters and Setters
//     public int getcId() {
//         return cId;
//     }

//     public void setcId(int cId) {
//         this.cId = cId;
//     }

//     public String getName() {
//         return name;
//     }

//     public void setName(String name) {
//         this.name = name;
//     }

//     public String getSecondName() {
//         return secondName;
//     }

//     public void setSecondName(String secondName) {
//         this.secondName = secondName;
//     }

//     public String getWork() {
//         return work;
//     }

//     public void setWork(String work) {
//         this.work = work;
//     }

//     public String getEmail() {
//         return email;
//     }

//     public void setEmail(String email) {
//         this.email = email;
//     }

//     public String getPhone() {
//         return phone;
//     }

//     public void setPhone(String phone) {
//         this.phone = phone;
//     }

//     public String getImage() {
//         return image;
//     }

//     public void setImage(String image) {
//         this.image = image;
//     }

//     public String getDescription() {
//         return description;
//     }

//     public void setDescription(String description) {
//         this.description = description;
//     }

//     public User getUser() {
//         return user;
//     }

//     public void setUser(User user) {
//         this.user = user;
//     }

//     @Override
//     public String toString() {
//         return "Contact{" +
//                 "cId=" + cId +
//                 ", name='" + name + '\'' +
//                 ", secondName='" + secondName + '\'' +
//                 ", work='" + work + '\'' +
//                 ", email='" + email + '\'' +
//                 ", phone='" + phone + '\'' +
//                 ", image='" + image + '\'' +
//                 ", description='" + description + '\'' +
//                 ", userId=" + (user != null ? user.getId() : null) +
//                 '}';
//     }
// }




package com.smart.smartcontactmanager.entities;

import jakarta.persistence.*;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "CONTACT")
public class contact {  // Class name should start with uppercase
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int cId;

    private String name;
    private String secondName;
    private String work;
    private String email;
    private String phone;

    // Store image path in DB
    private String image;

    @Column(length = 50000)
    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @Transient // Not stored in the database
    private MultipartFile imageFile;

    // Default Constructor
    public contact() {}

    // Getters and Setters
    public int getcId() {
        return cId;
    }

    public void setcId(int cId) {
        this.cId = cId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getWork() {
        return work;
    }

    public void setWork(String work) {
        this.work = work;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public MultipartFile getImageFile() {
        return imageFile;
    }

    public void setImageFile(MultipartFile imageFile) {
        this.imageFile = imageFile;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // @Override
    // public String toString() {
    //     return "contact{" +
    //             "cId=" + cId +
    //             ", name='" + name + '\'' +
    //             ", secondName='" + secondName + '\'' +
    //             ", work='" + work + '\'' +
    //             ", email='" + email + '\'' +
    //             ", phone='" + phone + '\'' +
    //             ", image='" + image + '\'' +
    //             ", description='" + description + '\'' +
    //             ", userId=" + (user != null ? user.getId() : null) +
    //             '}';
    // }
}
