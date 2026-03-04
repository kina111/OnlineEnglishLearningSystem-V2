# Online English Learning System V2

[![GitHub branch](https://img.shields.io/badge/Branch-master-brightgreen.svg)](https://github.com/kina111/OnlineEnglishLearningSystem-V2)
[![Java Version](https://img.shields.io/badge/Java-25-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.6-brightgreen.svg)](https://spring.io/projects/spring-boot)

## 🚀 Introduction

**Online English Learning System V2** is a comprehensive, feature-rich online platform designed to facilitate English language learning. The system supports individual learners in their educational journey by providing access to courses, exercises, practice tests, and direct interaction with instructors and peers—all through an intuitive web interface.

## ✨ Key Features

### User Management & Authentication
- **Role-based Access Control:** Support for multiple user roles (Learner, Expert, Marketing Manager, Administrator)
- **Email Verification:** Secure email-based authentication and registration
- **Profile Management:** User profile customization and account settings

### Course Management
- **Course Creation & Organization:** Experts can create and manage comprehensive English courses
- **Lesson Modules:** Structured lesson content with multimedia support
- **Exercise System:** Multiple types of exercises for skill practice
- **Assessment & Quizzes:** Multiple-choice and short-answer question types
- **Progress Tracking:** Learners can monitor their learning progress and achievements

### Content Management
- **Multimedia Support:** Upload and manage various content types
  - PDF documents and study materials
  - Video lectures and tutorials
  - Images and visual resources
  - Audio files
- **Content Organization:** Hierarchical lesson and chapter structure

### Interactive Learning
- **Group Chat System:** Real-time messaging between learners
- **Expert Consultation:** Direct messaging with instructors and marketing advisors
- **Peer Interaction:** Learner-to-learner communication and collaboration
- **Notifications:** Real-time updates on course activities

### Enrollment & Payment
- **Course Registration:** Learners can enroll in available courses
- **Payment Management:** Order processing and payment tracking
- **Enrollment History:** View past and current course enrollments

### Dashboards & Analytics
- **Role-Specific Dashboards:**
  - **Admin Dashboard:** User and course management, revenue analytics
  - **Expert Dashboard:** Course content management, student performance
  - **Marketing Dashboard:** Promotional content, enrollment statistics
  - **Learner Dashboard:** Course progress, achievements, recommendations
- **Performance Reports:** Detailed statistics and learning analytics

### Admin Controls
- **User Management:** Create, update, and manage user accounts
- **Course Administration:** Course creation, approval, and removal
- **Revenue Tracking:** Financial reporting and revenue analytics
- **Marketing Tools:** Slider/banner management for homepage

## 🛠️ Technology Stack

### Backend
- **Framework:** Spring Boot 3.5.6
- **Java Version:** 25
- **ORM:** Spring Data JPA with Hibernate
- **Security:** Spring Security
- **Validation:** Spring Validation Framework
- **Build Tool:** Maven

### Frontend
- **Template Engine:** Thymeleaf
- **Markup:** HTML5
- **Styling:** CSS3
- **Responsive Design:** Bootstrap integration

### Database
- **ORM:** JPA (Java Persistence API)
- **ORM Implementation:** Hibernate

## 📋 System Architecture

```
OnlineEnglishLearningSystem-V2/
├── src/
│   ├── main/
│   │   ├── java/com/swp391/           (Java source code)
│   │   └── resources/
│   │       ├── application.properties  (Configuration)
│   │       ├── data.sql               (Initial database data)
│   │       ├── static/                (CSS, Images)
│   │       └── templates/             (Thymeleaf templates)
│   │           ├── admin/             (Admin interface)
│   │           ├── auth/              (Authentication pages)
│   │           ├── chat/              (Messaging interface)
│   │           ├── course/            (Course management)
│   │           ├── quiz/              (Assessment pages)
│   │           └── user/              (User interface)
│   └── test/
│       └── java/com/swp391/           (Unit tests)
├── uploads/                            (User-generated content)
│   ├── avatars/
│   ├── blogs/
│   ├── courses/
│   ├── lectures/
│   ├── quizzes/
│   └── sliders/
├── pom.xml                             (Maven configuration)
└── README.md                           (This file)
```

## 👥 User Roles & Capabilities

### Guest User
- Browse homepage and available courses
- Register new account via email verification
- Login with credentials

### Learner
- Enroll in available courses
- View and complete lessons
- Submit exercises and assignments
- Participate in quizzes and practice tests
- Track learning progress and statistics
- Communicate with instructors and peers
- Access performance reports

### Expert (Instructor)
- Create and manage courses
- Create lesson modules and content
- Design exercises and quizzes
- Upload multimedia content
- View student progress and performance
- Respond to student inquiries via messaging

### Marketing Manager
- Design promotional content (sliders/banners)
- Provide course consultation to potential learners
- Manage promotional campaigns
- View enrollment statistics

### Administrator
- Manage all user accounts and permissions
- Monitor and manage all courses
- View financial reports and revenue analytics
- Configure system settings
- Access system logs and audit trails

## 🚀 Getting Started

### Prerequisites
- Java 25 or higher
- Maven 3.6+
- MySQL 8.0+ (or preferred relational database)
- Git

### Installation

1. **Clone the repository:**
   ```bash
   git clone https://github.com/kina111/OnlineEnglishLearningSystem-V2.git
   cd OnlineEnglishLearningSystem-V2
   ```

2. **Configure application properties:**
   Edit `src/main/resources/application.properties` with your database configuration:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/online_english_learning
   spring.datasource.username=your_db_user
   spring.datasource.password=your_db_password
   ```

3. **Build the project:**
   ```bash
   mvn clean install
   ```

4. **Run the application:**
   ```bash
   mvn spring-boot:run
   ```

5. **Access the application:**
   Navigate to `http://localhost:8080` in your web browser

## 📝 Database Setup

The application includes an `data.sql` file that initializes sample data. This file is automatically executed on application startup and contains:
- Sample users with different roles
- Initial course content
- Sample quiz questions
- Default system configurations

## 🔐 Security Features

- **Password Encryption:** Spring Security integration with BCrypt
- **Role-Based Access Control (RBAC):** URL-level and method-level security
- **CSRF Protection:** Cross-Site Request Forgery prevention
- **Session Management:** Secure session handling

## 📊 Project Status

- **Version:** 0.0.1-SNAPSHOT
- **Active Branch:** master
- **Development Phase:** In Progress

## 🤝 Contributing

We welcome contributions! If you'd like to contribute to this project:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📧 Contact & Support

For questions, suggestions, or collaboration opportunities:

- **Email:** namthptcvp@gmail.com
- **GitHub:** [@kina111](https://github.com/kina111)
- **Repository:** [OnlineEnglishLearningSystem-V2](https://github.com/kina111/OnlineEnglishLearningSystem-V2)

## 📄 License

This project is provided as-is for educational purposes. See LICENSE file for more details.

## 🙏 Acknowledgments

- Spring Framework team for excellent Java ecosystem
- Thymeleaf community for template engine
- All contributors and testers who help improve this platform

---

**Last Updated:** March 2026