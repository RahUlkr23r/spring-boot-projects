# 📇 Smart Contact Manager

A **Spring Boot–based web application** to manage personal and professional contacts securely.  
It allows users to **register, log in, and manage contacts** (add, update, delete, view) with authentication and database integration.  

---

## ✨ Features
- 👤 **User Authentication** – Signup/Login with Spring Security & JWT.  
- 📇 **Contact Management (CRUD)** – Add, update, delete, and view contacts.  
- 🔍 **Search & Filter** – Find contacts by name, email, or phone number.  
- 🖼 **Profile Pictures** – Upload and store images for each contact.  
- 🗄 **Database Integration** – Persistent storage of users & contacts.  
- 📊 **Admin Dashboard (optional)** – Manage users and their data.  

---

## 🏗 Tech Stack
- **Backend:** Spring Boot (Java)  
- **Frontend:** Thymeleaf / React *(depending on your implementation)*  
- **Database:** MySQL / PostgreSQL  
- **Authentication:** Spring Security + JWT  
- **Image Storage:** Cloudinary / Local storage  

---

## ⚡ Getting Started

### 1. Clone the Repository
```bash
git clone https://github.com/your-username/smart-contact-manager.git
cd smart-contact-manager
2. Configure Database
Create a database (e.g., smart_contact_db).

Update application.properties with your DB credentials:

properties
Copy code
spring.datasource.url=jdbc:mysql://localhost:3306/smart_contact_db
spring.datasource.username=your-username
spring.datasource.password=your-password
spring.jpa.hibernate.ddl-auto=update
3. Run the Application
bash
Copy code
mvn spring-boot:run
📌 API Endpoints (Sample)
Authentication
POST /auth/signup → Register new user

POST /auth/login → Login & get JWT

Contacts
POST /api/contacts → Add new contact

GET /api/contacts → Get all contacts (for logged-in user)

GET /api/contacts/{id} → Get single contact

PUT /api/contacts/{id} → Update contact

DELETE /api/contacts/{id} → Delete contact

🎯 Future Enhancements
📱 Mobile-friendly responsive UI

✉️ SMS/Email integration for quick communication

📂 Import/Export contacts (CSV/Excel)

👥 Contact grouping & tagging

👨‍💻 Author
Your Name

📧 Email:rahulkumar8684singh@gmail.com

🌐 GitHub: https://github.com/RahUlkr23r
