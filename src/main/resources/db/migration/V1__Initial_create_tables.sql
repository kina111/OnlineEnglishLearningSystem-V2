-- ============================================================
-- V1__Initial_create_tables.sql
-- Tạo toàn bộ schema từ các Entity JPA
-- Thứ tự: bảng độc lập trước, bảng có FK sau
-- ============================================================

-- 1. user_roles (không phụ thuộc bảng nào)
CREATE TABLE user_roles (
    id BIGINT IDENTITY(1, 1) PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    description NVARCHAR(1000) NOT NULL
);

-- 2. blog_categories (không phụ thuộc bảng nào)
CREATE TABLE blog_categories (
    id BIGINT IDENTITY(1, 1) PRIMARY KEY,
    name NVARCHAR(255) NOT NULL,
    slug VARCHAR(255) NOT NULL UNIQUE
);

-- 3. course_categories (không phụ thuộc bảng nào)
CREATE TABLE course_categories (
    id BIGINT IDENTITY(1, 1) PRIMARY KEY,
    name NVARCHAR(100) NOT NULL UNIQUE,
    description NVARCHAR(MAX) NOT NULL,
    active BIT NOT NULL DEFAULT 0,
    created_at DATETIME2,
    updated_at DATETIME2
);

-- 4. chats (không phụ thuộc bảng nào)
CREATE TABLE chats (
    id BIGINT IDENTITY(1, 1) PRIMARY KEY,
    type VARCHAR(20) NOT NULL, -- PRIVATE | GROUP
    name NVARCHAR(255),
    description NVARCHAR(1000),
    avatar VARCHAR(255),
    created_at DATETIME2 NOT NULL DEFAULT SYSDATETIME()
);

-- 5. users (FK -> user_roles)
CREATE TABLE users (
    id BIGINT IDENTITY(1, 1) PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    full_name NVARCHAR(100) NOT NULL,
    gender VARCHAR(10), -- MALE | FEMALE | OTHER
    mobile VARCHAR(255),
    address NVARCHAR(1000),
    avatar VARCHAR(255),
    dob DATE,
    enabled BIT NOT NULL DEFAULT 0,
    role_id BIGINT,
    created_at DATETIME2,
    updated_at DATETIME2,
    CONSTRAINT FK_users_role FOREIGN KEY (role_id) REFERENCES user_roles (id)
);

-- 6. courses (FK -> course_categories, users)
CREATE TABLE courses (
    id BIGINT IDENTITY(1, 1) PRIMARY KEY,
    name NVARCHAR(100) NOT NULL,
    short_description NVARCHAR(255) NOT NULL,
    description NVARCHAR(MAX) NOT NULL,
    prerequisite NVARCHAR(MAX) NOT NULL,
    thumbnail VARCHAR(255) NOT NULL,
    price FLOAT NOT NULL DEFAULT 0.0,
    discount FLOAT NOT NULL DEFAULT 0.0,
    featured BIT NOT NULL DEFAULT 0,
    status VARCHAR(20), -- PUBLISHED | DRAFT | PENDING
    category_id BIGINT,
    author_id BIGINT,
    created_at DATETIME2,
    updated_at DATETIME2,
    CONSTRAINT FK_courses_category FOREIGN KEY (category_id) REFERENCES course_categories (id),
    CONSTRAINT FK_courses_author FOREIGN KEY (author_id) REFERENCES users (id)
);

-- 7. blogs (FK -> blog_categories, users)
CREATE TABLE blogs (
    id BIGINT IDENTITY(1, 1) PRIMARY KEY,
    title NVARCHAR(100),
    thumbnail_url VARCHAR(255),
    short_description NVARCHAR(255),
    content NVARCHAR(MAX),
    status VARCHAR(20) NOT NULL DEFAULT 'DRAFT', -- PUBLISHED | DRAFT | CANCELLED
    blog_category_id BIGINT,
    author_id BIGINT,
    created_at DATETIME2,
    updated_at DATETIME2,
    CONSTRAINT FK_blogs_category FOREIGN KEY (blog_category_id) REFERENCES blog_categories (id),
    CONSTRAINT FK_blogs_author FOREIGN KEY (author_id) REFERENCES users (id)
);

-- 8. sliders (FK -> users)
CREATE TABLE sliders (
    id BIGINT IDENTITY(1, 1) PRIMARY KEY,
    user_id BIGINT NOT NULL,
    title NVARCHAR(100) NOT NULL,
    description NVARCHAR(MAX),
    order_number INT NOT NULL DEFAULT 1,
    status VARCHAR(10) NOT NULL, -- ACTIVE | INACTIVE
    image_url VARCHAR(255),
    link_url VARCHAR(255),
    created_at DATETIME2 DEFAULT SYSDATETIME(),
    updated_at DATETIME2 DEFAULT SYSDATETIME(),
    view_count BIGINT DEFAULT 0,
    CONSTRAINT FK_sliders_user FOREIGN KEY (user_id) REFERENCES users (id)
);

-- 9. chat_members (FK -> chats, users)
CREATE TABLE chat_members (
    id BIGINT IDENTITY(1, 1) PRIMARY KEY,
    chat_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    role VARCHAR(10) NOT NULL DEFAULT 'MEMBER', -- ADMIN | MEMBER
    joined_at DATETIME2 NOT NULL DEFAULT SYSDATETIME(),
    CONSTRAINT FK_chat_members_chat FOREIGN KEY (chat_id) REFERENCES chats (id),
    CONSTRAINT FK_chat_members_user FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT UQ_chat_members UNIQUE (chat_id, user_id)
);

-- 10. messages (FK -> chats, users)
CREATE TABLE messages (
    id BIGINT IDENTITY(1, 1) PRIMARY KEY,
    chat_id BIGINT NOT NULL,
    sender_id BIGINT NOT NULL,
    content NVARCHAR(2000) NOT NULL,
    created_at DATETIME2 NOT NULL DEFAULT SYSDATETIME(),
    fileurl VARCHAR(255),
    CONSTRAINT FK_messages_chat FOREIGN KEY (chat_id) REFERENCES chats (id),
    CONSTRAINT FK_messages_sender FOREIGN KEY (sender_id) REFERENCES users (id)
);

-- 11. confirmation_tokens (FK -> users)
CREATE TABLE confirmation_tokens (
    id BIGINT IDENTITY(1, 1) PRIMARY KEY,
    token VARCHAR(255) NOT NULL,
    created_at DATETIME2 NOT NULL,
    expired_at DATETIME2 NOT NULL,
    confirmed_at DATETIME2,
    user_id BIGINT,
    CONSTRAINT FK_tokens_user FOREIGN KEY (user_id) REFERENCES users (id)
);

-- 12. chapters (FK -> courses)
CREATE TABLE chapters (
    id BIGINT IDENTITY(1, 1) PRIMARY KEY,
    name NVARCHAR(100) NOT NULL UNIQUE,
    short_description NVARCHAR(255) NOT NULL,
    order_number INT NOT NULL,
    course_id BIGINT,
    CONSTRAINT FK_chapters_course FOREIGN KEY (course_id) REFERENCES courses (id)
);

-- 13. lessons (FK -> chapters)
CREATE TABLE lessons (
    id BIGINT IDENTITY(1, 1) PRIMARY KEY,
    title NVARCHAR(200) NOT NULL,
    order_number INT NOT NULL,
    lesson_type VARCHAR(20) NOT NULL, -- LECTURE | QUIZ
    estimated_time INT,
    html_content NVARCHAR(MAX),
    video_url VARCHAR(255),
    duration BIGINT DEFAULT 0,
    pass_rate INT,
    time_limit_in_minutes INT,
    number_of_questions INT,
    chapter_id BIGINT,
    created_at DATETIME2,
    updated_at DATETIME2,
    CONSTRAINT FK_lessons_chapter FOREIGN KEY (chapter_id) REFERENCES chapters (id)
);

-- 14. questions (FK -> lessons)
CREATE TABLE questions (
    id BIGINT IDENTITY(1, 1) PRIMARY KEY,
    content NVARCHAR(255) NOT NULL,
    question_type VARCHAR(30) NOT NULL, -- MULTIPLE_CHOICE | SHORT_ANSWER
    media_type VARCHAR(10) NOT NULL, -- NONE | IMAGE | AUDIO | VIDEO
    media_url NVARCHAR(MAX),
    lesson_id BIGINT NOT NULL,
    created_at DATETIME2,
    updated_at DATETIME2,
    CONSTRAINT FK_questions_lesson FOREIGN KEY (lesson_id) REFERENCES lessons (id)
);

-- 15. answer_options (FK -> questions)
CREATE TABLE answer_options (
    id BIGINT IDENTITY(1, 1) PRIMARY KEY,
    content NVARCHAR(500) NOT NULL,
    correct BIT NOT NULL,
    explanation NVARCHAR(MAX),
    question_id BIGINT NOT NULL,
    created_at DATETIME2,
    updated_at DATETIME2,
    CONSTRAINT FK_answer_options_question FOREIGN KEY (question_id) REFERENCES questions (id)
);

-- 16. short_answer_options (FK -> questions)
CREATE TABLE short_answer_options (
    id BIGINT IDENTITY(1, 1) PRIMARY KEY,
    solution_text NVARCHAR(255) NOT NULL,
    question_id BIGINT,
    CONSTRAINT FK_short_answer_question FOREIGN KEY (question_id) REFERENCES questions (id)
);

-- 17. enrollments (FK -> users, courses)
CREATE TABLE enrollments (
    id BIGINT IDENTITY(1, 1) PRIMARY KEY,
    user_id BIGINT NOT NULL,
    course_id BIGINT NOT NULL,
    enrolled_at DATETIME2 NOT NULL DEFAULT SYSDATETIME(),
    completed_at DATETIME2,
    last_access_at DATETIME2,
    status VARCHAR(20) NOT NULL, -- ENROLLED | COMPLETED | CANCELLED
    CONSTRAINT FK_enrollments_user FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT FK_enrollments_course FOREIGN KEY (course_id) REFERENCES courses (id),
    CONSTRAINT UQ_enrollments UNIQUE (user_id, course_id)
);

-- 18. feedbacks (FK -> enrollments, 1-1)
CREATE TABLE feedbacks (
    id BIGINT IDENTITY(1, 1) PRIMARY KEY,
    rating INT NOT NULL,
    review NVARCHAR(MAX),
    status VARCHAR(20) NOT NULL DEFAULT 'APPROVED', -- PENDING | APPROVED | REJECTED
    helpful_count INT NOT NULL DEFAULT 0,
    not_helpful_count INT NOT NULL DEFAULT 0,
    enrollment_id BIGINT NOT NULL UNIQUE,
    created_at DATETIME2,
    updated_at DATETIME2,
    CONSTRAINT FK_feedbacks_enrollment FOREIGN KEY (enrollment_id) REFERENCES enrollments (id)
);

-- 19. orders (FK -> users, courses)
CREATE TABLE orders (
    id BIGINT IDENTITY(1, 1) PRIMARY KEY,
    order_code VARCHAR(255) NOT NULL UNIQUE,
    amount FLOAT NOT NULL,
    order_info NVARCHAR(MAX) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING', -- PENDING | PAID | FAILED | CANCELLED
    vnp_response_code VARCHAR(2),
    vnp_transaction_no VARCHAR(20),
    user_id BIGINT NOT NULL,
    course_id BIGINT NOT NULL,
    created_at DATETIME2,
    updated_at DATETIME2,
    CONSTRAINT FK_orders_user FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT FK_orders_course FOREIGN KEY (course_id) REFERENCES courses (id)
);

-- 20. user_lesson_progress (FK -> users, lessons, enrollments)
CREATE TABLE user_lesson_progress (
    id BIGINT IDENTITY(1, 1) PRIMARY KEY,
    user_id BIGINT NOT NULL,
    lesson_id BIGINT NOT NULL,
    enrollment_id BIGINT NOT NULL,
    is_completed BIT NOT NULL DEFAULT 0,
    completion_date DATETIME2,
    CONSTRAINT FK_ulp_user FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT FK_ulp_lesson FOREIGN KEY (lesson_id) REFERENCES lessons (id),
    CONSTRAINT FK_ulp_enrollment FOREIGN KEY (enrollment_id) REFERENCES enrollments (id),
    CONSTRAINT UQ_ulp UNIQUE (
        user_id,
        lesson_id,
        enrollment_id
    )
);

-- 21. notes (FK -> user_lesson_progress)
CREATE TABLE notes (
    id BIGINT IDENTITY(1, 1) PRIMARY KEY,
    time_at_lesson VARCHAR(255) NOT NULL,
    content NVARCHAR(255) NOT NULL,
    user_lesson_id BIGINT,
    created_at DATETIME2,
    updated_at DATETIME2,
    CONSTRAINT FK_notes_user_lesson FOREIGN KEY (user_lesson_id) REFERENCES user_lesson_progress (id)
);

-- 22. wishlist (FK -> users, courses)
CREATE TABLE wishlist (
    id BIGINT IDENTITY(1, 1) PRIMARY KEY,
    user_id BIGINT NOT NULL,
    course_id BIGINT NOT NULL,
    added_date DATETIME2 NOT NULL DEFAULT SYSDATETIME(),
    CONSTRAINT FK_wishlist_user FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT FK_wishlist_course FOREIGN KEY (course_id) REFERENCES courses (id),
    CONSTRAINT UQ_wishlist UNIQUE (user_id, course_id)
);

-- 23. quiz_attempt (FK -> users, lessons) — dùng SEQUENCE vì @GeneratedValue không chỉ định strategy
CREATE SEQUENCE quiz_attempt_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE quiz_attempt (
    id BIGINT PRIMARY KEY,
    user_id BIGINT,
    lesson_id BIGINT,
    start_time DATETIME2,
    end_time DATETIME2,
    completed_time DATETIME2,
    score FLOAT,
    passed BIT DEFAULT 0,
    CONSTRAINT FK_quiz_attempt_user FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT FK_quiz_attempt_lesson FOREIGN KEY (lesson_id) REFERENCES lessons (id)
);

-- 24. quiz_attempt_question (FK -> quiz_attempt, questions) — dùng SEQUENCE
CREATE
SEQUENCE quiz_attempt_question_seq START
WITH
    1 INCREMENT BY 50;

CREATE TABLE quiz_attempt_question (
    id BIGINT PRIMARY KEY,
    quiz_attempt_id BIGINT,
    question_id BIGINT,
    bookmarked BIT NOT NULL DEFAULT 0,
    question_order INT NOT NULL DEFAULT 0,
    is_correct BIT DEFAULT 0,
    CONSTRAINT FK_qaq_quiz_attempt FOREIGN KEY (quiz_attempt_id) REFERENCES quiz_attempt (id),
    CONSTRAINT FK_qaq_question FOREIGN KEY (question_id) REFERENCES questions (id)
);

-- 25. quiz_attempt_selected_option (FK -> quiz_attempt_question, answer_options) — dùng SEQUENCE
CREATE
SEQUENCE quiz_attempt_selected_option_seq START
WITH
    1 INCREMENT BY 50;

CREATE TABLE quiz_attempt_selected_option (
    id BIGINT PRIMARY KEY,
    attempt_question_id BIGINT,
    option_id BIGINT,
    selected_value VARCHAR(255),
    CONSTRAINT FK_qaso_attempt_question FOREIGN KEY (attempt_question_id) REFERENCES quiz_attempt_question (id),
    CONSTRAINT FK_qaso_option FOREIGN KEY (option_id) REFERENCES answer_options (id)
);