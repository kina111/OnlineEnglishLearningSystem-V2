-- File: src/main/resources/data.sql
-- Dữ liệu mẫu cho hệ thống Online English Learning
-- QUAN TRỌNG: Thứ tự chèn phải được tuân thủ để đảm bảo ràng buộc khóa ngoại

-- 1. Bảng USER_ROLES (Không phụ thuộc)
SET IDENTITY_INSERT user_roles ON;
INSERT INTO user_roles (id, name, description) VALUES
                                                   (1, 'ROLE_ADMIN', N'Quản trị viên hệ thống, có quyền cao nhất.'),
                                                   (2, 'ROLE_EXPERT', N'Chuyên gia, giảng viên tạo và quản lý khóa học.'),
                                                   (3, 'ROLE_MARKETING', N'Nhân viên marketing, quản lý bài viết và chiến dịch.'),
                                                   (4, 'ROLE_USER', N'Người dùng, học viên đăng ký khóa học.');
SET IDENTITY_INSERT user_roles OFF;

-- 2. Bảng USERS (Phụ thuộc vào USER_ROLES)
-- Mật khẩu mặc định cho tất cả là "password123" (đã được hash bằng Bcrypt)
SET IDENTITY_INSERT users ON;
INSERT INTO users (id, email, password, full_name, gender, mobile, address, avatar, dob, enabled, role_id) VALUES
                                                                                                               (1, 'admin@example.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07c209qnMrHlClDW2W', 'Admin Manager', 'OTHER', '0987654321', '123 Admin Street, Hanoi', 'avatars/default_admin.png', '1990-01-01', 1, 1),
                                                                                                               (2, 'expert@example.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07c209qnMrHlClDW2W', 'John Doe', 'MALE', '0912345678', '456 Expert Avenue, HCMC', 'avatars/expert_john.png', '1985-05-15', 1, 2),
                                                                                                               (3, 'marketing@example.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07c209qnMrHlClDW2W', 'Jane Smith', 'FEMALE', '0911223344', '789 Marketing Road, Danang', 'avatars/marketing_jane.png', '1992-09-20', 1, 3),
                                                                                                               (4, 'user@example.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07c209qnMrHlClDW2W', 'Alice Student', 'FEMALE', '0955667788', '101 Student Lane, Can Tho', 'avatars/student_alice.png', '2002-12-10', 1, 4),
                                                                                                               (5, 'expert2@example.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07c209qnMrHlClDW2W', 'Michael Chen', 'MALE', '0922334455', '111 IELTS Street, Hanoi', 'avatars/expert_michael.png', '1988-03-10', 1, 2),
                                                                                                               (6, 'expert3@example.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07c209qnMrHlClDW2W', 'Sophia Nguyen', 'FEMALE', '0933445566', '222 TOEIC Road, HCMC', 'avatars/expert_sophia.png', '1991-11-25', 1, 2);
SET IDENTITY_INSERT users OFF;

-- 3. Bảng COURSE_CATEGORIES (Không phụ thuộc)
SET IDENTITY_INSERT course_categories ON;
INSERT INTO course_categories (id, name, description, active) VALUES
                                                                  (1, N'Tiếng Anh Giao Tiếp', N'Các khóa học tập trung vào kỹ năng nghe và nói trong các tình huống hàng ngày.', 1),
                                                                  (2, N'Luyện thi IELTS', N'Các khóa học chuyên sâu về chiến lược, từ vựng và ngữ pháp cho kỳ thi IELTS.', 1),
                                                                  (3, N'Tiếng Anh Thương Mại', N'Các khóa học dành cho người đi làm, tập trung vào email, thuyết trình và đàm phán.', 1);
SET IDENTITY_INSERT course_categories OFF;

-- 4. Bảng COURSES (Phụ thuộc vào COURSE_CATEGORIES và USERS (author_id))
-- author_id = 2 là của user 'expert@example.com'
SET IDENTITY_INSERT courses ON;
INSERT INTO courses (id, name, short_description, description, prerequisite, thumbnail, price, discount, featured, status, category_id, author_id, created_at, updated_at) VALUES
                                                                                                                                                                               (1, N'IELTS Speaking Masterclass: Band 8.0+', N'Nâng cao kỹ năng nói IELTS từ cơ bản đến nâng cao, tập trung vào sự trôi chảy và từ vựng học thuật.', N'Đây là khóa học toàn diện giúp bạn chinh phục band điểm 8.0+ trong phần thi IELTS Speaking...', N' * Yêu cầu trình độ tiếng Anh tương đương 5.0 IELTS...', 'courses/thumbnails/ielts_speaking.png', 2000000.0, 15.0, 1, 'PUBLISHED', 2, 2, GETDATE(), GETDATE()),
                                                                                                                                                                               (2, N'Giao tiếp tự tin nơi công sở', N'Khóa học giúp bạn tự tin sử dụng tiếng Anh trong môi trường làm việc chuyên nghiệp.', N'Bạn sẽ học cách:\n     * Viết email chuyên nghiệp...', N' * Trình độ tiếng Anh cơ bản...', 'courses/thumbnails/business_english.png', 1500000.0, 10.0, 0, 'PUBLISHED', 3, 2, GETDATE(), GETDATE()),
                                                                                                                                                                               (3, N'Phát âm chuẩn Anh-Mỹ cho người mới bắt đầu', N'Xây dựng nền tảng phát âm vững chắc với 44 âm IPA...', N'Khóa học này sẽ giúp bạn nói tiếng Anh rõ ràng và tự nhiên hơn...', N' * Không yêu cầu đầu vào...', 'courses/thumbnails/pronunciation.png', 1200000.0, 0.0, 1, 'PUBLISHED', 1, 2, GETDATE(), GETDATE()),
                                                                                                                                                                               (4, N'Từ vựng IELTS theo chủ đề', N'Học 1000+ từ vựng học thuật quan trọng cho IELTS.', N'Phân loại từ vựng theo các chủ đề thường gặp trong IELTS như Environment, Technology, Education...', N'Trình độ IELTS 4.5+', 'courses/thumbnails/6195519.jpg', 1800000.0, 20.0, 0, 'PUBLISHED', 2, 2, GETDATE(), GETDATE()),
                                                                                                                                                                               (5, N'Ngữ pháp Tiếng Anh nâng cao', N'Ôn tập và nâng cao các cấu trúc ngữ pháp phức tạp.', N'Đi sâu vào các điểm ngữ pháp khó như Mệnh đề quan hệ, Câu điều kiện, Thể bị động nâng cao...', N'Trình độ Intermediate trở lên', 'courses/thumbnails/6734768.jpg', 1600000.0, 5.0, 1, 'DRAFT', 1, 2, GETDATE(), GETDATE()),
                                                                                                                                                                               (6, N'Thuyết trình Tiếng Anh chuyên nghiệp', N'Kỹ năng chuẩn bị và thực hiện bài thuyết trình ấn tượng.', N'Học cách xây dựng cấu trúc, sử dụng ngôn ngữ cơ thể, và trả lời câu hỏi Q&A.', N'Trình độ Upper-Intermediate', 'courses/thumbnails/6743349.jpg', 1700000.0, 0.0, 0, 'DRAFT', 3, 2, GETDATE(), GETDATE()),
                                                                                                                                                                               (7, N'Luyện nghe IELTS Listening Max Score', N'Chiến thuật làm bài và luyện tập các dạng câu hỏi Listening.', N'Phân tích các dạng bài Map, Multiple Choice, Form Completion... và mẹo tránh bẫy.', N'Trình độ IELTS 5.0+', 'courses/thumbnails/7420258.jpg', 1900000.0, 10.0, 1, 'DRAFT', 2, 5, GETDATE(), GETDATE()),
                                                                                                                                                                               (8, N'Tiếng Anh du lịch cho người Việt', N'Các mẫu câu và từ vựng cần thiết khi đi du lịch nước ngoài.', N'Bao gồm các tình huống tại sân bay, khách sạn, nhà hàng, hỏi đường...', N'Trình độ Basic', 'courses/thumbnails/10019196.jpg', 900000.0, 0.0, 0, 'DRAFT', 1, 5, GETDATE(), GETDATE()),
                                                                                                                                                                               (9, N'Viết luận IELTS Writing Task 2', N'Hướng dẫn viết các dạng bài luận Agree/Disagree, Discussion, Problem/Solution.', N'Phân tích đề, lập dàn ý, sử dụng từ nối và cấu trúc câu hiệu quả.', N'Trình độ IELTS 5.5+', 'courses/thumbnails/ielts_writing2.png', 2100000.0, 15.0, 1, 'DRAFT', 2, 5, GETDATE(), GETDATE()),
                                                                                                                                                                               (10, N'Tiếng Anh phỏng vấn xin việc', N'Chuẩn bị cho các câu hỏi phỏng vấn tiếng Anh phổ biến.', N'Cách giới thiệu bản thân, trả lời về điểm mạnh/yếu, kinh nghiệm làm việc...', N'Trình độ Intermediate', 'courses/thumbnails/job_interview.png', 1300000.0, 5.0, 0, 'DRAFT', 3, 6, GETDATE(), GETDATE()),
                                                                                                                                                                               (11, N'Luyện đọc IELTS Reading Intensive', N'Kỹ năng Skimming, Scanning và làm các dạng bài True/False/NG, Matching Headings.', N'Thực hành với các bài đọc học thuật đa dạng.', N'Trình độ IELTS 5.0+', 'courses/thumbnails/ielts_reading.png', 1800000.0, 10.0, 0, 'DRAFT', 2, 6, GETDATE(), GETDATE()),
                                                                                                                                                                               (12, N'Tiếng Anh giao tiếp hàng ngày (Nâng cao)', N'Mở rộng vốn từ lóng, thành ngữ và cách diễn đạt tự nhiên.', N'Thảo luận các chủ đề phức tạp hơn, luyện phản xạ nghe nói.', N'Trình độ Upper-Intermediate', 'courses/thumbnails/daily_english_adv.png', 1400000.0, 0.0, 1, 'DRAFT', 1, 6, GETDATE(), GETDATE());
SET IDENTITY_INSERT courses OFF;

-- 5. Bảng CHAPTERS (Phụ thuộc vào COURSES)
-- Chương cho khóa học "IELTS Speaking Masterclass" (course_id = 1)
SET IDENTITY_INSERT chapters ON;
INSERT INTO chapters (id, name, short_description, order_number, course_id) VALUES
-- Course 1 (IELTS Speaking)
(1, N'Giới thiệu về IELTS Speaking', N'Tổng quan về cấu trúc bài thi và các tiêu chí chấm điểm.', 1, 1),
(2, N'Chiến lược trả lời Part 1', N'Cách trả lời các câu hỏi về bản thân một cách tự nhiên và ấn tượng.', 2, 1),
(3, N'Xây dựng bài nói Part 2', N'Phương pháp A.R.E.A để phát triển ý tưởng cho bài nói dài 2 phút.', 3, 1),
-- Course 2 (Giao tiếp công sở)
(4, N'Kỹ năng viết Email', N'Các mẫu email thông dụng và cách sử dụng ngôn từ chuyên nghiệp.', 1, 2),
(5, N'Tham gia họp trực tuyến', N'Các cụm từ hữu ích khi phát biểu ý kiến, đồng ý và phản đối.', 2, 2),
-- Course 4 (Từ vựng IELTS)
(6, N'Chủ đề Environment', N'Từ vựng về môi trường, biến đổi khí hậu.', 1, 4),
(7, N'Chủ đề Technology', N'Từ vựng về công nghệ, internet, AI.', 2, 4),
-- Course 5 (Ngữ pháp)
(8, N'Mệnh đề quan hệ (Relative Clauses)', N'Cách dùng who, whom, which, that, whose.', 1, 5),
-- Course 6 (Thuyết trình)
(9, N'Cấu trúc bài thuyết trình', N'Mở bài, thân bài, kết luận hiệu quả.', 1, 6),
-- Course 7 (Listening)
(10, N'Dạng bài Multiple Choice', N'Chiến thuật chọn đáp án đúng.', 1, 7),
-- Course 8 (Du lịch)
(11, N'Tại sân bay', N'Check-in, hải quan, tìm cổng bay.', 1, 8),
-- Course 9 (Writing Task 2)
(12, N'Dạng bài Agree/Disagree', N'Cách trình bày quan điểm cá nhân.', 1, 9),
-- Course 10 (Phỏng vấn)
(13, N'Giới thiệu bản thân (Tell me about yourself)', N'Cách tóm tắt kinh nghiệm ấn tượng.', 1, 10),
-- Course 11 (Reading)
(14, N'Kỹ năng Skimming & Scanning', N'Đọc lướt và đọc quét tìm thông tin.', 1, 11),
-- Course 12 (Giao tiếp nâng cao)
(15, N'Thành ngữ (Idioms) thông dụng', N'Học các thành ngữ phổ biến trong giao tiếp.', 1, 12),

-- *** BỔ SUNG CHƯƠNG CHO KHÓA 3 (PHÁT ÂM) ***
(16, N'Nguyên âm đơn (Monophthongs)', N'Học 12 nguyên âm đơn trong bảng IPA.', 1, 3),
(17, N'Nguyên âm đôi (Diphthongs)', N'Học 8 nguyên âm đôi trong bảng IPA.', 2, 3);
SET IDENTITY_INSERT chapters OFF;

-- 6. Bảng LESSONS (Phụ thuộc vào CHAPTERS)
-- *** CẬP NHẬT ORDER_NUMBER CHO QUIZ (ID 1-15) TỪ 1 THÀNH 2 ***
-- Chạy câu UPDATE này trước khi chèn để đảm bảo thứ tự
UPDATE lessons SET order_number = 2 WHERE id BETWEEN 1 AND 15;

SET IDENTITY_INSERT lessons ON;
-- Chèn các QUIZ (ID 1-15) với order_number = 2 (đã update ở trên)
INSERT INTO lessons (
    id, title, order_number, lesson_type, estimated_time, pass_rate, time_limit_in_minutes,
    html_content, video_url, chapter_id, number_of_questions, duration
) VALUES
      (1, N'Quiz 1: Tổng quan IELTS Speaking', 2, 'QUIZ', 15, 60, 1, N'<p>Bài kiểm tra tổng quan...</p>', NULL, 1, 15, NULL),
      (2, N'Quiz 2: Câu hỏi Part 1', 2, 'QUIZ', 20, 70, 15, N'<p>Kiểm tra khả năng phản xạ...</p>', NULL, 2, 20, NULL),
      (3, N'Quiz 3: Topic Development', 2, 'QUIZ', 25, 75, 20, N'<p>Đánh giá khả năng phát triển...</p>', NULL, 3, 25, NULL),
      (4, N'Quiz 4: Email chuyên nghiệp', 2, 'QUIZ', 10, 60, 8, N'<p>Bài kiểm tra chọn câu đúng...</p>', NULL, 4, 30, NULL),
      (5, N'Quiz 5: Giao tiếp trong cuộc họp', 2, 'QUIZ', 15, 65, 10, N'<p>Bài trắc nghiệm tình huống...</p>', NULL, 5, 10, NULL),
      (6, N'Quiz: Environment Vocabulary', 2, 'QUIZ', 10, 70, 8, N'<p>Kiểm tra từ vựng...</p>', NULL, 6, 20, NULL),
      (7, N'Quiz: Technology Terms', 2, 'QUIZ', 10, 70, 8, N'<p>Kiểm tra từ vựng...</p>', NULL, 7, 20, NULL),
      (8, N'Quiz: Relative Clauses Practice', 2, 'QUIZ', 15, 65, 12, N'<p>Bài tập điền từ...</p>', NULL, 8, 15, NULL),
      (9, N'Quiz: Presentation Structure', 2, 'QUIZ', 10, 60, 7, N'<p>Trắc nghiệm về cấu trúc...</p>', NULL, 9, 10, NULL),
      (10, N'Quiz: Listening Multiple Choice', 2, 'QUIZ', 20, 75, 15, N'<p>Luyện nghe dạng...</p>', NULL, 10, 18, NULL),
      (11, N'Quiz: Airport Situations', 2, 'QUIZ', 8, 50, 5, N'<p>Chọn mẫu câu phù hợp...</p>', NULL, 11, 12, NULL),
      (12, N'Quiz: Agree/Disagree Essays', 2, 'QUIZ', 25, 70, 20, N'<p>Kiểm tra cấu trúc...</p>', NULL, 12, 10, NULL),
      (13, N'Quiz: Common Interview Questions', 2, 'QUIZ', 15, 60, 10, N'<p>Trắc nghiệm cách trả lời...</p>', NULL, 13, 15, NULL),
      (14, N'Quiz: Reading Skills', 2, 'QUIZ', 20, 65, 15, N'<p>Bài tập Skimming...</p>', NULL, 14, 20, NULL),
      (15, N'Quiz: Idioms Matching', 2, 'QUIZ', 10, 70, 8, N'<p>Nối thành ngữ...</p>', NULL, 15, 25, NULL),

-- *** BỔ SUNG CÁC BÀI HỌC DẠNG LECTURE (ID 16-30) CHO CHAPTERS 1-15 ***
-- *** SỬA LỖI: Cập nhật LECTURE: pass_rate, time_limit, video_url -> NULL. Thêm duration ***
      (16, N'Video: Tổng quan về bài thi Speaking', 1, 'LECTURE', 20, NULL, NULL, N'<p>Nội dung bài giảng video...</p>', 'lectures/videos/6006989891083.mp4', 1, NULL, 20),
      (17, N'Video: Kỹ thuật trả lời Part 1', 1, 'LECTURE', 25, NULL, NULL, N'<p>Nội dung bài giảng video...</p>', 'lectures/videos/6006989894583.mp4', 2, NULL, 25),
      (18, N'Video: Phát triển ý tưởng Part 2', 1, 'LECTURE', 30, NULL, NULL, N'<p>Nội dung bài giảng video...</p>', 'lectures/videos/6006989913221.mp4', 3, NULL, 30),
      (19, N'Video: Cách viết Email trang trọng', 1, 'LECTURE', 15, NULL, NULL, N'<p>Nội dung bài giảng video...</p>', 'lectures/videos/6006989913221.mp4', 4, NULL, 15),
      (20, N'Video: Thuật ngữ trong cuộc họp', 1, 'LECTURE', 20, NULL, NULL, N'<p>Nội dung bài giảng video...</p>', NULL, 5, NULL, 20),
      (21, N'Video: Từ vựng chủ đề Môi trường', 1, 'LECTURE', 15, NULL, NULL, N'<p>Nội dung bài giảng video...</p>', NULL, 6, NULL, 15),
      (22, N'Video: Từ vựng chủ đề Công nghệ', 1, 'LECTURE', 15, NULL, NULL, N'<p>Nội dung bài giảng video...</p>', NULL, 7, NULL, 15),
      (23, N'Video: Bài giảng Mệnh đề quan hệ', 1, 'LECTURE', 25, NULL, NULL, N'<p>Nội dung bài giảng video...</p>', NULL, 8, NULL, 25),
      (24, N'Video: Lên cấu trúc bài thuyết trình', 1, 'LECTURE', 20, NULL, NULL, N'<p>Nội dung bài giảng video...</p>', NULL, 9, NULL, 20),
      (25, N'Video: Mẹo làm bài Multiple Choice', 1, 'LECTURE', 30, NULL, NULL, N'<p>Nội dung bài giảng video...</p>', NULL, 10, NULL, 30),
      (26, N'Video: Giao tiếp tại sân bay', 1, 'LECTURE', 10, NULL, NULL, N'<p>Nội dung bài giảng video...</p>', NULL, 11, NULL, 10),
      (27, N'Video: Phân tích đề Agree/Disagree', 1, 'LECTURE', 35, NULL, NULL, N'<p>Nội dung bài giảng video...</p>', NULL, 12, NULL, 35),
      (28, N'Video: Trả lời "Tell me about yourself"', 1, 'LECTURE', 15, NULL, NULL, N'<p>Nội dung bài giảng video...</p>', NULL, 13, NULL, 15),
      (29, N'Video: Hướng dẫn Skimming và Scanning', 1, 'LECTURE', 20, NULL, NULL, N'<p>Nội dung bài giảng video...</p>', NULL, 14, NULL, 20),
      (30, N'Video: Các thành ngữ phổ biến', 1, 'LECTURE', 15, NULL, NULL, N'<p>Nội dung bài giảng video...</p>', NULL, 15, NULL, 15),

-- *** BỔ SUNG BÀI HỌC CHO KHÓA 3 (CHAPTERS 16, 17) ***
      (31, N'Video: 12 Nguyên âm đơn', 1, 'LECTURE', 20, NULL, NULL, N'<p>Bài giảng về /i:/, /ɪ/, /e/...</p>', NULL, 16, NULL, 20),
      (32, N'Quiz: Luyện tập Nguyên âm đơn', 2, 'QUIZ', 15, 70, 10, N'<p>Trắc nghiệm nhận diện âm...</p>', NULL, 16, 20, NULL),
      (33, N'Video: 8 Nguyên âm đôi', 1, 'LECTURE', 20, NULL, NULL, N'<p>Bài giảng về /aɪ/, /eɪ/, /ɔɪ/...</p>', NULL, 17, NULL, 20),
      (34, N'Quiz: Luyện tập Nguyên âm đôi', 2, 'QUIZ', 15, 70, 10, N'<p>Trắc nghiệm nhận diện âm...</p>', NULL, 17, 20, NULL);
SET IDENTITY_INSERT lessons OFF;

-- 7. Bảng ENROLLMENTS (Phụ thuộc vào USERS và COURSES)
SET IDENTITY_INSERT enrollments ON;
INSERT INTO enrollments (id, user_id, course_id, enrolled_at, status) VALUES
-- User 4 (Alice) đăng ký khóa 1 (IELTS Speaking) và khóa 3 (Phát âm)
(1, 4, 1, DATEADD(day, -10, GETDATE()), 'ENROLLED'), -- Cập nhật progress (2/6 lessons ~ 33%)
(2, 4, 3, DATEADD(day, -5, GETDATE()), 'ENROLLED');  -- Cập nhật progress (3/4 lessons = 75%)
SET IDENTITY_INSERT enrollments OFF;

-- 8. Bảng USER_LESSON_PROGRESS (Phụ thuộc vào USERS, LESSONS, ENROLLMENTS)
-- *** SỬA ĐỔI: TẠO ĐẦY ĐỦ LESSONS CHO ENROLLMENTS (ID 1 VÀ 2) ***
SET IDENTITY_INSERT user_lesson_progress ON;
INSERT INTO user_lesson_progress (id, user_id, lesson_id, enrollment_id, is_completed, completion_date) VALUES
-- User 4 (Alice) - Enrollment 1 (Course 1: IELTS Speaking - Gồm 6 lessons: 16, 1, 17, 2, 18, 3)
-- Giả sử đã học xong Chapter 1 (Lesson 16, 1). Progress 2/6 = 33%
(1, 4, 16, 1, 0, DATEADD(day, -8, GETDATE())), -- Chapter 1 - Lecture 1 (Completed)
(2, 4, 1, 1, 0, DATEADD(day, -7, GETDATE())),  -- Chapter 1 - Quiz 1 (Completed)
(3, 4, 17, 1, 0, NULL),                        -- Chapter 2 - Lecture 2 (Not completed)
(4, 4, 2, 1, 0, NULL),                         -- Chapter 2 - Quiz 2 (Not completed)
(5, 4, 18, 1, 0, NULL),                        -- Chapter 3 - Lecture 3 (Not completed)
(6, 4, 3, 1, 0, NULL),                         -- Chapter 3 - Quiz 3 (Not completed)

-- User 4 (Alice) - Enrollment 2 (Course 3: Phát âm - Gồm 4 lessons: 31, 32, 33, 34)
-- Giả sử đã học 3/4 lessons. Progress 3/4 = 75%
(7, 4, 31, 2, 0, DATEADD(day, -4, GETDATE())), -- Chapter 16 - Lecture 1 (Completed)
(8, 4, 32, 2, 0, DATEADD(day, -3, GETDATE())), -- Chapter 16 - Quiz 1 (Completed)
(9, 4, 33, 2, 0, DATEADD(day, -2, GETDATE())), -- Chapter 17 - Lecture 2 (Completed)
(10, 4, 34, 2, 0, NULL);                       -- Chapter 17 - Quiz 2 (Not completed)
SET IDENTITY_INSERT user_lesson_progress OFF;

-- 9. Bảng WISHLIST (Phụ thuộc vào USERS và COURSES)
SET IDENTITY_INSERT wishlist ON;
INSERT INTO wishlist (id, user_id, course_id, added_date) VALUES
-- User 4 (Alice) quan tâm khóa 2 (Giao tiếp công sở) và khóa 7 (Listening)
(1, 4, 2, DATEADD(day, -2, GETDATE())),
(2, 4, 7, DATEADD(day, -1, GETDATE()));
SET IDENTITY_INSERT wishlist OFF;

----
SET IDENTITY_INSERT questions ON
INSERT INTO questions (id, content, question_type, media_type, media_url, lesson_id, created_at, updated_at)
VALUES
    (1, N'IELTS Speaking test có bao nhiêu phần?', 'MULTIPLE_CHOICE', 'IMAGE', 'quizzes/media/Phan-nao-IELTS-Speaking-quan-trong-nhat-1-1_1762747036995.png', 1, GETDATE(), GETDATE()),
    (2, N'Phần nào trong IELTS Speaking yêu cầu thí sinh nói về một chủ đề trong khoảng 1–2 phút?', 'MULTIPLE_CHOICE', 'NONE', NULL, 1, GETDATE(), GETDATE()),
    (3, N'Thời gian trung bình của bài thi IELTS Speaking là bao lâu?', 'MULTIPLE_CHOICE', 'NONE', NULL, 1, GETDATE(), GETDATE()),
    (4, N'Tiêu chí nào KHÔNG nằm trong 4 tiêu chí chấm điểm IELTS Speaking?', 'MULTIPLE_CHOICE', 'NONE', NULL, 1, GETDATE(), GETDATE()),
    (5, N'Mục đích chính của phần Speaking Part 1 là gì?', 'MULTIPLE_CHOICE', 'NONE', NULL, 1, GETDATE(), GETDATE()),
    (6, N'Khi giám khảo hỏi bạn “Do you work or study?”, câu trả lời nào sau đây là phù hợp nhất?', 'MULTIPLE_CHOICE', 'NONE', NULL, 1, GETDATE(), GETDATE()),
    (7, N'Bạn sẽ nói trong bao lâu ở phần IELTS Speaking Part 2?', 'SHORT_ANSWER', 'NONE', NULL, 1, GETDATE(), GETDATE()),
-- -- Q2
    (8, N'Bạn thường làm gì vào thời gian rảnh?', 'SHORT_ANSWER', 'NONE', NULL, 2, GETDATE(), GETDATE()),
    (9, N'IELTS Speaking Part 1 kéo dài khoảng bao lâu?', 'MULTIPLE_CHOICE', 'NONE', NULL, 2, GETDATE(), GETDATE()),
    (10, N'Giám khảo sẽ hỏi bao nhiêu chủ đề trong Part 1?', 'MULTIPLE_CHOICE', 'NONE', NULL, 2, GETDATE(), GETDATE()),
    (11, N'Mục tiêu của Part 1 là gì?', 'MULTIPLE_CHOICE', 'NONE', NULL, 2, GETDATE(), GETDATE()),
-- Q3
    (12, N'Trong Part 2, bạn cần nói trong bao lâu?', 'SHORT_ANSWER', 'NONE', NULL, 3, GETDATE(), GETDATE()),
    (13, N'Điều gì giúp bạn phát triển ý tốt hơn?', 'MULTIPLE_CHOICE', 'NONE', NULL, 3, GETDATE(), GETDATE()),
    (14, N'Trong Part 3, bạn nên trả lời thế nào?', 'MULTIPLE_CHOICE', 'NONE', NULL, 3, GETDATE(), GETDATE()),
-- Q4
    (15, N'Cách mở đầu email lịch sự là gì?', 'MULTIPLE_CHOICE', 'NONE', NULL, 4, GETDATE(), GETDATE()),
    (16, N'“I look forward to hearing from you” nghĩa là gì?', 'MULTIPLE_CHOICE', 'NONE', NULL, 4, GETDATE(), GETDATE());
SET IDENTITY_INSERT questions OFF;


--Answer option
SET IDENTITY_INSERT answer_options ON
INSERT INTO answer_options (id, content, correct, explanation, question_id, created_at, updated_at)
VALUES
-- Q1
(1, N'3 phần', 1, N'IELTS Speaking có 3 phần: Part 1 - Introduction & Interview, Part 2 - Long Turn, Part 3 - Discussion.', 1, GETDATE(), GETDATE()),
(2, N'2 phần', 0, N'Có tất cả 3 phần trong IELTS Speaking.', 1, GETDATE(), GETDATE()),
(3, N'4 phần', 0, N'Chỉ có 3 phần chính, không phải 4.', 1, GETDATE(), GETDATE()),
(4, N'5 phần', 0, N'Không đúng, chỉ có 3 phần.', 1, GETDATE(), GETDATE()),

-- Q2
(5, N'Speaking Part 2', 1, N'Ở Part 2, bạn sẽ nói liên tục 1–2 phút về một chủ đề được cho sẵn (Cue Card).', 2, GETDATE(), GETDATE()),
(6, N'Speaking Part 1', 0, N'Part 1 chỉ gồm các câu hỏi ngắn về bản thân.', 2, GETDATE(), GETDATE()),
(7, N'Speaking Part 3', 0, N'Part 3 là phần thảo luận với giám khảo.', 2, GETDATE(), GETDATE()),
(8, N'Writing Task 1', 0, N'Writing Task 1 thuộc kỹ năng viết, không phải Speaking.', 2, GETDATE(), GETDATE()),

-- Q3
(9, N'11–14 phút', 1, N'Thời gian IELTS Speaking thường kéo dài 11–14 phút.', 3, GETDATE(), GETDATE()),
(10, N'5 phút', 0, N'Quá ngắn, bài thi thường hơn 10 phút.', 3, GETDATE(), GETDATE()),
(11, N'20 phút', 0, N'Không đúng, quá dài so với thực tế.', 3, GETDATE(), GETDATE()),
(12, N'30 phút', 0, N'Không chính xác, IELTS Speaking chỉ khoảng 14 phút.', 3, GETDATE(), GETDATE()),

-- Q4
(13, N'Pronunciation', 0, N'Phát âm là 1 trong 4 tiêu chí chính.', 4, GETDATE(), GETDATE()),
(14, N'Fluency and Coherence', 0, N'Độ trôi chảy và mạch lạc là 1 tiêu chí chính.', 4, GETDATE(), GETDATE()),
(15, N'Grammar Range and Accuracy', 0, N'Ngữ pháp là 1 tiêu chí chính.', 4, GETDATE(), GETDATE()),
(16, N'Spelling Accuracy', 1, N'Đánh vần không nằm trong tiêu chí chấm điểm Speaking.', 4, GETDATE(), GETDATE()),

-- Q5
(17, N'Giới thiệu bản thân và nói về những chủ đề quen thuộc', 1, N'Part 1 nhằm giúp thí sinh “khởi động” và nói về bản thân, công việc, sở thích,...', 5, GETDATE(), GETDATE()),
(18, N'Thảo luận chủ đề học thuật', 0, N'Đây là nội dung của Part 3.', 5, GETDATE(), GETDATE()),
(19, N'Phân tích một biểu đồ', 0, N'Đó là kỹ năng Writing Task 1.', 5, GETDATE(), GETDATE()),
(20, N'Đọc to một đoạn văn', 0, N'IELTS Speaking không có phần đọc to.', 5, GETDATE(), GETDATE()),

-- Q6
(21, N'I’m currently a student at university.', 1, N'Câu trả lời tự nhiên, đúng ngữ pháp, và cung cấp thông tin cụ thể.', 6, GETDATE(), GETDATE()),
(22, N'Yes, I do.', 0, N'Quá ngắn, không phù hợp với yêu cầu mở rộng câu trả lời.', 6, GETDATE(), GETDATE()),
(23, N'Study.', 0, N'Câu này không có cấu trúc đầy đủ.', 6, GETDATE(), GETDATE()),
(24, N'Work.', 0, N'Cần một câu hoàn chỉnh, không chỉ một từ.', 6, GETDATE(), GETDATE()),

--     Quiz2
--     Q1
(28, N'4–5 phút', 1, N'Part 1 thường kéo dài 4–5 phút.', 9, GETDATE(), GETDATE()),
(29, N'2–3 phút', 0, NULL, 9, GETDATE(), GETDATE()),
(30, N'6–8 phút', 0, NULL, 9, GETDATE(), GETDATE()),

(31, N'2–3 chủ đề', 1, N'Part 1 gồm 2–3 chủ đề quen thuộc.', 10, GETDATE(), GETDATE()),
(32, N'1 chủ đề duy nhất', 0, NULL, 10, GETDATE(), GETDATE()),

(33, N'Giúp thí sinh thoải mái, kiểm tra khả năng giao tiếp cơ bản', 1, N'Đúng – Part 1 là phần “warm-up”.', 11, GETDATE(), GETDATE()),
(34, N'Kiểm tra phát âm chuyên sâu', 0, NULL, 11, GETDATE(), GETDATE()),

    -- Quiz 3
(35, N'Dùng ví dụ, so sánh và trải nghiệm cá nhân', 1, N'Các ví dụ giúp mở rộng nội dung.', 13, GETDATE(), GETDATE()),
(36, N'Chỉ trả lời ngắn gọn 1 câu', 0, NULL, 13, GETDATE(), GETDATE()),

(37, N'Trả lời chi tiết, nêu lý do và ví dụ', 1, N'Part 3 yêu cầu câu trả lời sâu.', 14, GETDATE(), GETDATE()),
(38, N'Trả lời bằng Yes/No', 0, NULL, 14, GETDATE(), GETDATE()),
-- Quiz 4
(39, N'Dear Mr. Smith,', 1, N'Cách chào chuyên nghiệp phổ biến.', 15, GETDATE(), GETDATE()),
(40, N'Hey dude,', 0, NULL, 15, GETDATE(), GETDATE()),
-- QUiz 5
(41, N'Tôi mong nhận phản hồi từ bạn.', 1, N'Dịch chính xác nghĩa câu này.', 16, GETDATE(), GETDATE()),
(42, N'Tôi muốn nghe bạn nói.', 0, NULL, 16, GETDATE(), GETDATE());


SET IDENTITY_INSERT answer_options OFF;

SET IDENTITY_INSERT short_answer_options ON
INSERT INTO short_answer_options (id,question_id,solution_text) values
    (1,7,'2'),
    (2, 8, N'free time activities'),
    (3, 12, N'2 minutes');
SET IDENTITY_INSERT short_answer_options OFF;


-- 10. Bảng NOTES (Phụ thuộc vào USER_LESSON_PROGRESS)
-- Chèn 20 ghi chú mẫu, phân bổ cho 6 user_lesson_progress đầu tiên (ID 1-6)
SET IDENTITY_INSERT notes ON;
INSERT INTO notes (id, time_at_lesson, content, user_lesson_id) VALUES
-- 4 Ghi chú cho UserLessonID 1 (Lesson 16: Video: 12 Nguyên âm đơn)
(1, '01:30', N'Phân biệt /i:/ (sheep) và /ɪ/ (ship) rất quan trọng.', 1),
(2, '05:45', N'Âm /e/ trong "bed" khác với /æ/ trong "bad". Phải mở miệng rộng hơn cho âm /æ/.', 1),
(3, '10:12', N'Lưu ý: âm /ɜ:/ (bird) và /ə/ (about) đều là âm "ơ" nhưng độ dài khác nhau.', 1),
(4, '15:05', N'Ghi chú lại cách đặt lưỡi cho âm /u:/ (blue) và /ʊ/ (book).', 1),

-- 4 Ghi chú cho UserLessonID 2 (Lesson 1: Quiz: Tổng quan IELTS Speaking)
(5, '00:15', N'Quiz này kiểm tra 4 tiêu chí: Fluency, Lexical Resource, Grammatical Range, Pronunciation.', 2),
(6, '00:30', N'Thời gian thi thật: Part 1 (4-5 phút), Part 2 (3-4 phút), Part 3 (4-5 phút).', 2),
(7, '00:45', N'Mình bị sai câu về Lexical Resource, cần dùng từ vựng ít phổ biến hơn (less common).', 2),
(8, '01:00', N'Đã hoàn thành quiz! Cần ôn lại các tiêu chí chấm điểm.', 2),

-- 3 Ghi chú cho UserLessonID 3 (Lesson 17: Video: 8 Nguyên âm đôi)
(9, '02:20', N'Âm /aɪ/ như trong "my", "sky", "time". Bắt đầu từ /a/ và lướt tới /ɪ/.', 3),
(10, '08:00', N'Luyện tập cặp âm /eɪ/ (say) và /ɔɪ/ (boy).', 3),
(11, '12:50', N'Ghi lại các ví dụ cho âm /aʊ/ (now, how) và /əʊ/ (go, no).', 3),

-- 3 Ghi chú cho UserLessonID 4 (Lesson 2: Quiz: Câu hỏi Part 1)
(12, '03:15', N'Tuyệt đối không nên trả lời quá ngắn (chỉ Yes/No). Phải mở rộng câu trả lời.', 4),
(13, '07:30', N'Các chủ đề phổ biến: Work/Studies, Hometown, Hobbies.', 4),
(14, '10:00', N'Ghi chú: Dùng "used to" để nói về thói quen trong quá khứ.', 4),

-- 3 Ghi chú cho UserLessonID 5 (Lesson 18: Video: Phát triển ý tưởng Part 2)
(15, '04:10', N'Phương pháp A.R.E.A: Answer, Reason, Example, Alternative.', 5),
(16, '15:25', N'Sử dụng A.R.E.A giúp bài nói dài và logic hơn, không bị bí ý.', 5),
(17, '25:00', N'Note lại các từ nối (linking words) hay dùng: moreover, however, therefore...', 5),

-- 3 Ghi chú cho UserLessonID 6 (Lesson 3: Quiz: Topic Development)
(18, '05:00', N'Mình bị bí ý tưởng ở chủ đề "Describe a person". Cần chuẩn bị từ vựng về tính cách.', 6),
(19, '10:20', N'Khi kể chuyện (Part 2), phải dùng thì quá khứ (Past Simple, Past Continuous).', 6),
(20, '15:00', N'Hoàn thành quiz, điểm chưa cao lắm, cần luyện thêm Part 2.', 6);
SET IDENTITY_INSERT notes OFF;


-- 1. Bảng USERS (Bổ sung 19 học viên mới)
-- Mật khẩu mặc định: "password123" ($2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07c209qnMrHlClDW2W)
SET IDENTITY_INSERT users ON;
INSERT INTO users (id, email, password, full_name, gender, mobile, address, avatar, dob, enabled, role_id) VALUES
                                                                                                               (7, 'user2@example.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07c209qnMrHlClDW2W', 'Binh Le', 'MALE', '0900000001', '1 Nguyen Trai, Hanoi', 'avatars/default_user.png', '1995-02-10', 1, 4),
                                                                                                               (8, 'user3@example.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07c209qnMrHlClDW2W', 'Chi Phan', 'FEMALE', '0900000002', '2 Ba Trieu, HCMC', 'avatars/default_user.png', '1998-07-20', 1, 4),
                                                                                                               (9, 'user4@example.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07c209qnMrHlClDW2W', 'Dung Tran', 'MALE', '0900000003', '3 Le Loi, Danang', 'avatars/default_user.png', '2000-01-01', 1, 4),
                                                                                                               (10, 'user5@example.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07c209qnMrHlClDW2W', 'Giang Hoang', 'FEMALE', '0900000004', '4 Quang Trung, Hai Phong', 'avatars/default_user.png', '1997-11-30', 1, 4),
                                                                                                               (11, 'user6@example.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07c209qnMrHlClDW2W', 'Hieu Nguyen', 'MALE', '0900000005', '5 Tran Phu, Can Tho', 'avatars/default_user.png', '1996-04-15', 1, 4),
                                                                                                               (12, 'user7@example.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07c209qnMrHlClDW2W', 'Khanh Vu', 'FEMALE', '0900000006', '6 Ly Thuong Kiet, Hanoi', 'avatars/default_user.png', '2001-08-25', 1, 4),
                                                                                                               (13, 'user8@example.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07c209qnMrHlClDW2W', 'Linh Pham', 'FEMALE', '0900000007', '7 Vo Thi Sau, HCMC', 'avatars/default_user.png', '1999-03-05', 1, 4),
                                                                                                               (14, 'user9@example.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07c209qnMrHlClDW2W', 'Minh Dang', 'MALE', '0900000008', '8 Bach Dang, Danang', 'avatars/default_user.png', '1994-09-12', 1, 4),
                                                                                                               (15, 'user10@example.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07c209qnMrHlClDW2W', 'Nam Bui', 'MALE', '0900000009', '9 Nguyen Du, Hanoi', 'avatars/default_user.png', '1993-06-18', 1, 4),
                                                                                                               (16, 'user11@example.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07c209qnMrHlClDW2W', 'Nga Do', 'FEMALE', '0900000010', '10 Hai Ba Trung, HCMC', 'avatars/default_user.png', '1998-12-01', 1, 4),
                                                                                                               (17, 'user12@example.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07c209qnMrHlClDW2W', 'Oanh Dinh', 'FEMALE', '0900000011', '11 Phan Chu Trinh, Hanoi', 'avatars/default_user.png', '1997-02-28', 1, 4),
                                                                                                               (18, 'user13@example.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07c209qnMrHlClDW2W', 'Phuong Le', 'MALE', '0900000012', '12 Dien Bien Phu, HCMC', 'avatars/default_user.png', '1999-10-10', 1, 4),
                                                                                                               (19, 'user14@example.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07c209qnMrHlClDW2W', 'Quang Truong', 'MALE', '0900000013', '13 Nguyen Hue, Danang', 'avatars/default_user.png', '2002-05-05', 1, 4),
                                                                                                               (20, 'user15@example.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07c209qnMrHlClDW2W', 'Son Nguyen', 'MALE', '0900000014', '14 Ly Tu Trong, Hanoi', 'avatars/default_user.png', '1996-08-08', 1, 4),
                                                                                                               (21, 'user16@example.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07c209qnMrHlClDW2W', 'Tam Tran', 'FEMALE', '0900000015', '15 Le Thanh Ton, HCMC', 'avatars/default_user.png', '1995-11-11', 1, 4),
                                                                                                               (22, 'user17@example.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07c209qnMrHlClDW2W', 'Thu Vu', 'FEMALE', '0900000016', '16 Ton Duc Thang, Hanoi', 'avatars/default_user.png', '2000-03-03', 1, 4),
                                                                                                               (23, 'user18@example.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07c209qnMrHlClDW2W', 'Tuan Anh', 'MALE', '0900000017', '17 Pasteur, HCMC', 'avatars/default_user.png', '1998-06-06', 1, 4),
                                                                                                               (24, 'user19@example.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07c209qnMrHlClDW2W', 'Uyen Nguyen', 'FEMALE', '0900000018', '18 Ham Nghi, Danang', 'avatars/default_user.png', '1997-09-09', 1, 4),
                                                                                                               (25, 'user20@example.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07c209qnMrHlClDW2W', 'Viet Pham', 'MALE', '0900000019', '19 Pham Ngu Lao, HCMC', 'avatars/default_user.png', '1999-12-12', 1, 4);
SET IDENTITY_INSERT users OFF;

-- 2. Bảng ENROLLMENTS (Bổ sung 19 enrollment cho Khóa học 1)
SET IDENTITY_INSERT enrollments ON;
INSERT INTO enrollments (id, user_id, course_id, enrolled_at, status) VALUES
                                                                          (3, 7, 1, DATEADD(day, -9, GETDATE()), 'ENROLLED'),  -- user2 (ID 7)
                                                                          (4, 8, 1, DATEADD(day, -8, GETDATE()), 'ENROLLED'),  -- user3 (ID 8)
                                                                          (5, 9, 1, DATEADD(day, -8, GETDATE()), 'ENROLLED'),  -- user4 (ID 9)
                                                                          (6, 10, 1, DATEADD(day, -7, GETDATE()), 'ENROLLED'), -- user5 (ID 10)
                                                                          (7, 11, 1, DATEADD(day, -7, GETDATE()), 'ENROLLED'), -- user6 (ID 11)
                                                                          (8, 12, 1, DATEADD(day, -6, GETDATE()), 'ENROLLED'), -- user7 (ID 12)
                                                                          (9, 13, 1, DATEADD(day, -6, GETDATE()), 'ENROLLED'), -- user8 (ID 13)
                                                                          (10, 14, 1, DATEADD(day, -5, GETDATE()), 'ENROLLED'), -- user9 (ID 14)
                                                                          (11, 15, 1, DATEADD(day, -5, GETDATE()), 'ENROLLED'), -- user10 (ID 15)
                                                                          (12, 16, 1, DATEADD(day, -4, GETDATE()), 'ENROLLED'), -- user11 (ID 16)
                                                                          (13, 17, 1, DATEADD(day, -4, GETDATE()), 'ENROLLED'), -- user12 (ID 17)
                                                                          (14, 18, 1, DATEADD(day, -3, GETDATE()), 'ENROLLED'), -- user13 (ID 18)
                                                                          (15, 19, 1, DATEADD(day, -3, GETDATE()), 'ENROLLED'), -- user14 (ID 19)
                                                                          (16, 20, 1, DATEADD(day, -2, GETDATE()), 'ENROLLED'), -- user15 (ID 20)
                                                                          (17, 21, 1, DATEADD(day, -2, GETDATE()), 'ENROLLED'), -- user16 (ID 21)
                                                                          (18, 22, 1, DATEADD(day, -1, GETDATE()), 'ENROLLED'), -- user17 (ID 22)
                                                                          (19, 23, 1, DATEADD(day, -1, GETDATE()), 'ENROLLED'), -- user18 (ID 23)
                                                                          (20, 24, 1, DATEADD(day, -1, GETDATE()), 'ENROLLED'), -- user19 (ID 24)
                                                                          (21, 25, 1, DATEADD(day, -1, GETDATE()), 'ENROLLED'); -- user20 (ID 25)
SET IDENTITY_INSERT enrollments OFF;

-- 3. Bảng FEEDBACKS (Bổ sung 20 feedback cho Khóa học 1)
-- Mỗi feedback tương ứng với MỘT enrollment_id khác nhau (1, 3, 4, ..., 21)
-- để tuân thủ ràng buộc UNIQUE.
SET IDENTITY_INSERT feedbacks ON;
INSERT INTO feedbacks (id, rating, review, status, helpful_count, not_helpful_count, enrollment_id) VALUES
-- Feedback (ID 21) cho Enrollment 1 (của User 4, 'Alice Student')
(21, 5, N'Khóa học IELTS Speaking này rất tuyệt vời! Giảng viên John Doe dạy rất chi tiết, dễ hiểu. Mình đã tự tin hơn rất nhiều.', 'APPROVED', 10, 0, 1),

-- 19 Feedbacks (ID 22-40) cho 19 Enrollments mới (ID 3-21)
(22, 4, N'Nội dung khóa học hay, bám sát thực tế. Tuy nhiên, phần video ở Part 2 (Lesson 18) hơi dài một chút.', 'APPROVED', 5, 1, 3),
(23, 5, N'Rất hài lòng. Phương pháp A.R.E.A (Lesson 18) cực kỳ hữu ích để phát triển ý tưởng. Highly recommended!', 'APPROVED', 22, 0, 4),
(24, 3, N'Khóa học ổn, nhưng mình mong muốn có nhiều bài quiz hơn. Hiện tại chỉ có 3 quiz (Lesson 1, 2, 3) cho 3 chương.', 'APPROVED', 2, 0, 5),
(25, 5, N'Giọng giảng viên rất chuẩn, phần video (Lesson 16, 17) chất lượng cao. Mình học được nhiều từ vựng hay.', 'APPROVED', 15, 0, 6),
(26, 4, N'Nội dung phong phú, đặc biệt là phần chiến lược trả lời Part 1 (Lesson 17).', 'APPROVED', 3, 0, 7),
(27, 5, N'Đây là khóa học IELTS Speaking tốt nhất mình từng tham gia. Mình đã học xong 2/6 bài và thấy tiến bộ rõ rệt.', 'APPROVED', 8, 0, 8),
(28, 4, N'Giá 2,000,000 VNĐ là hợp lý so với chất lượng. Mình mua được đợt giảm 15% nữa.', 'APPROVED', 1, 0, 9),
(29, 5, N'Cảm ơn thầy John Doe. Sau khóa học mình đã đạt được 7.5 Speaking.', 'APPROVED', 30, 2, 10),
(30, 2, N'Video bài giảng (Lesson 16) thỉnh thoảng bị mờ, không rõ nét. Mong team kỹ thuật kiểm tra lại.', 'REJECTED', 0, 5, 11),
(31, 5, N'Các bài quiz (Lesson 1, 2, 3) có độ khó vừa phải, giúp ôn tập kiến thức đã học trong video.', 'APPROVED', 7, 0, 12),
(32, 4, N'Mình (user11@example.com) rất thích cách giảng viên phân tích các tiêu chí chấm điểm ở Chương 1.', 'APPROVED', 6, 0, 13),
(33, 5, N'Tuyệt vời! Các chiến thuật cho Part 2 và 3 rất thực tế.', 'APPROVED', 9, 0, 14),
(34, 4, N'Hệ thống ghi chú (Notes) khi xem video rất tiện. Mình đã note lại rất nhiều.', 'APPROVED', 4, 0, 15),
(35, 3, N'Phần quiz (Lesson 1) có 15 câu, mình làm trong 15 phút. Hơi ít thời gian.', 'APPROVED', 0, 0, 16),
(36, 5, N'Khóa học này xứng đáng 5 sao. Nội dung đầy đủ cả 3 part.', 'APPROVED', 11, 0, 17),
(37, 5, N'Đã áp dụng thành công các tips trong khóa học và thấy hiệu quả. Cảm ơn OELS.', 'APPROVED', 12, 1, 18),
(38, 4, N'Mong chờ thêm các khóa học khác của giảng viên John Doe.', 'APPROVED', 2, 0, 19),
(39, 5, N'Phần nội dung (html_content) của các bài quiz cũng được viết tốt, giải thích rõ ràng.', 'APPROVED', 3, 0, 20),
(40, 4, N'Khóa học có 3 chương, 6 bài học (3 video, 3 quiz) là hơi ít. Mong có thêm nội dung về Part 3.', 'APPROVED', 5, 1, 21);
SET IDENTITY_INSERT feedbacks OFF;

INSERT INTO orders (
    order_code,
    amount,
    order_info,
    status,
    vnp_response_code,
    vnp_transaction_no,
    user_id,
    course_id,
    created_at,
    updated_at
) VALUES
      ('ORD001', 1990000, N'Khóa học Java cơ bản', 'PAID', '00', '123456789', 1, 1, '02/11/2025', GETDATE()),
      ('ORD002', 2490000, N'Khóa học Spring Boot nâng cao', 'PAID', '00', '123456790', 2, 2, '03/11/2025', GETDATE()),
      ('ORD003', 990000, N'Khóa học SQL cho người mới bắt đầu', 'PENDING', NULL, NULL, 3, 3, GETDATE(), GETDATE()),
      ('ORD004', 2990000, N'Khóa học ReactJS chuyên sâu', 'FAILED', '99', '123456791', 4, 4, GETDATE(), GETDATE()),
      ('ORD005', 1590000, N'Khóa học Python thực hành', 'PAID', '00', '123456792', 5, 5, GETDATE(), GETDATE()),
      ('ORD006', 3490000, N'Khóa học Machine Learning cơ bản', 'CANCELLED', NULL, NULL, 6, 6, GETDATE(), GETDATE());

SET IDENTITY_INSERT blog_categories ON;
INSERT INTO blog_categories (id, name, slug) VALUES
                                                 (1, N'Mẹo thi IELTS', 'meo-thi-ielts'),
                                                 (2, N'Ngữ pháp Tiếng Anh', 'ngu-phap-tieng-anh'),
                                                 (3, N'Từ vựng theo chủ đề', 'tu-vung-theo-chu-de'),
                                                 (4, N'Tiếng Anh Thương Mại', 'tieng-anh-thuong-mai');
SET IDENTITY_INSERT blog_categories OFF;

SET IDENTITY_INSERT blogs ON;
INSERT INTO blogs (id, title, thumbnail_url, short_description, content, blog_category_id, author_id, status, created_at, updated_at) VALUES
-- Danh mục 1: Mẹo thi IELTS (ID=1) - Tác giả 2, 5 (Experts)
(1, N'5 Chiến lược làm bài IELTS Reading hiệu quả', 'blogs/ielts-reading-strategies.png', N'Nắm vững 5 chiến lược Skimming, Scanning và làm chủ các dạng câu hỏi True/False/NG.', N'Phần Reading trong IELTS luôn khiến nhiều thí sinh “đau đầu” vì lượng thông tin quá lớn, thời gian hạn chế và cách ra đề đánh đố. Tuy nhiên, nếu nắm được chiến lược phù hợp, bạn hoàn toàn có thể làm bài nhanh hơn, hiểu chính xác hơn và đạt điểm cao hơn. Dưới đây là 5 chiến lược làm bài IELTS Reading hiệu quả mà bạn nên áp dụng ngay từ hôm nay.

1. Hiểu rõ cấu trúc bài thi trước khi bắt đầu. Bài Reading có 3 đoạn văn với tổng cộng 40 câu hỏi trong 60 phút. Mỗi dạng câu hỏi sẽ kiểm tra một kỹ năng khác nhau: Matching Headings kiểm tra khả năng nắm ý chính, True/False/Not Given kiểm tra kỹ năng hiểu chi tiết, Summary Completion kiểm tra khả năng tìm từ khóa và hiểu ngữ cảnh. Vì vậy, hãy dành 1-2 phút đầu để xem qua cấu trúc, xác định dạng câu hỏi và lên kế hoạch làm bài cho từng phần thay vì lao ngay vào đọc.

2. Làm chủ kỹ năng skimming và scanning. Đây là hai kỹ năng “sống còn” trong IELTS Reading. Skimming giúp bạn đọc nhanh để nắm ý chính của bài, nên chỉ cần dành khoảng 2 phút đọc lướt qua tiêu đề, câu chủ đề và từ khóa. Scanning giúp bạn lướt nhanh để tìm thông tin cụ thể như ngày tháng, tên riêng hay con số. Hãy nhớ rằng từ trong câu hỏi và trong bài thường không giống nhau hoàn toàn, vì thế bạn cần luyện khả năng nhận diện từ đồng nghĩa hoặc cách diễn đạt khác (paraphrase).

3. Đọc câu hỏi trước khi đọc bài. Một sai lầm phổ biến là đọc toàn bài trước rồi mới xem câu hỏi, khiến bạn mất thời gian và phải đọc lại nhiều lần. Hãy đọc câu hỏi trước để biết mình đang tìm gì. Ví dụ, nếu câu hỏi hỏi “What is the main reason why whales migrate?”, bạn chỉ cần tập trung vào phần nói về cá voi và việc di cư, không cần đọc lan man. Khi đọc câu hỏi, hãy gạch chân từ khóa để định hướng khi tìm thông tin trong đoạn văn.

4. Quản lý thời gian thông minh. Bạn chỉ có 60 phút cho 40 câu hỏi, tức khoảng 1,5 phút cho mỗi câu. Nếu gặp câu khó, đừng dừng lại quá 2 phút mà hãy đánh dấu lại để quay lại sau. Thông thường, bạn nên làm Passage 1 trong 15 phút (dễ nhất), Passage 2 trong 20 phút (trung bình) và Passage 3 trong 25 phút (khó nhất). Đừng quên dành 2-3 phút cuối cùng để kiểm tra lại các câu trống hoặc đoán nếu cần.

5. Cẩn thận với “Not Given” và bẫy từ vựng. Dạng True/False/Not Given là nơi dễ mất điểm nhất vì thông tin trong bài thường được diễn đạt theo cách khác. Nguyên tắc là: True nếu thông tin trùng khớp hoàn toàn, False nếu trái ngược, và Not Given nếu bài không nhắc đến. Nếu bạn không tìm thấy thông tin khẳng định hay phủ định rõ ràng, rất có thể đó là Not Given. Ngoài ra, đừng để bị đánh lừa bởi những từ quen thuộc – nhiều khi bài dùng từ tương tự nhưng nghĩa khác.

Tóm lại, muốn làm tốt IELTS Reading, bạn cần hiểu cấu trúc bài, luyện kỹ năng đọc nhanh (skimming và scanning), đọc câu hỏi trước, quản lý thời gian chặt chẽ và nhận diện bẫy từ vựng. Quan trọng nhất là hãy luyện tập thường xuyên với đề thật trong bộ Cambridge IELTS và luôn phân tích kỹ những lỗi sai sau mỗi lần làm bài. Kiên trì thực hiện những chiến lược này, bạn sẽ thấy điểm Reading của mình tăng lên rõ rệt.', 1, 2, 'PUBLISHED', GETDATE(), GETDATE()),
(2, N'Cách đạt Band 8.0+ IELTS Speaking Part 2', 'blogs/ielts-speaking-part2.png', N'Phân tích cấu trúc, cách dùng từ vựng "ăn điểm" và các chủ đề thường gặp trong Part 2.', N'Nội dung chi tiết về phương pháp A.R.E.A...', 1, 5, 'PUBLISHED', GETDATE(), GETDATE()),
(3, N'Tổng hợp lỗi ngữ pháp thường gặp trong IELTS Writing', 'blogs/ielts-writing-grammar.png', N'Tránh mất điểm đáng tiếc với 10 lỗi ngữ pháp phổ biến nhất trong Writing Task 1 và Task 2.', N'Nội dung chi tiết về lỗi mạo từ, thì...', 1, 2, 'PUBLISHED', GETDATE(), GETDATE()),
(4, N'Làm thế nào để luyện nghe IELTS thụ động?', 'blogs/ielts-passive-listening.png', N'Biến thời gian chết thành thời gian học với các nguồn podcast và video hiệu quả.', N'Nội dung chi tiết về các kênh podcast...', 1, 5, 'PUBLISHED', GETDATE(), GETDATE()),
(5, N'Phân tích đề Writing Task 2 (Dạng Problem/Solution)', 'blogs/ielts-writing-task2-problem.png', N'Hướng dẫn lập dàn ý chi tiết cho dạng bài Problem/Solution.', N'Nội dung chi tiết về cách viết mở bài...', 1, 2, 'DRAFT', GETDATE(), GETDATE()),

-- Danh mục 2: Ngữ pháp Tiếng Anh (ID=2) - Tác giả 6 (Expert)
(6, N'Phân biệt Thì Hiện tại Hoàn thành và Quá khứ Đơn', 'blogs/present-perfect-vs-past-simple.png', N'Khi nào dùng "I have finished" và khi nào dùng "I finished"? Tìm hiểu sự khác biệt.', N'Nội dung chi tiết về các dấu hiệu nhận biết...', 2, 6, 'PUBLISHED', GETDATE(), GETDATE()),
(7, N'Tất cả về Mệnh đề Quan hệ (Relative Clauses)', 'blogs/relative-clauses.png', N'Cách sử dụng who, whom, which, that, và whose một cách chính xác.', N'Nội dung chi tiết về mệnh đề quan hệ xác định...', 2, 6, 'PUBLISHED', GETDATE(), GETDATE()),
(8, N'Câu điều kiện loại 1, 2, 3 và câu điều kiện hỗn hợp', 'blogs/conditional-sentences.png', N'Tổng hợp công thức và cách dùng của 4 loại câu điều kiện.', N'Nội dung chi tiết về If-clauses...', 2, 6, 'PUBLISHED', GETDATE(), GETDATE()),
(9, N'Cách sử dụng mạo từ A, AN, THE', 'blogs/articles-a-an-the.png', N'Làm chủ mạo từ, một trong những phần khó nhất với người Việt.', N'Nội dung chi tiết về các quy tắc dùng THE...', 2, 6, 'PUBLISHED', GETDATE(), GETDATE()),
(10, N'Bài tập Câu bị động (Passive Voice)', 'blogs/passive-voice-exercise.png', N'Tổng hợp các dạng bài tập câu bị động từ cơ bản đến nâng cao.', N'Nội dung chi tiết về các bài tập...', 2, 6, 'DRAFT', GETDATE(), GETDATE()),

-- Danh mục 3: Từ vựng theo chủ đề (ID=3) - Tác giả 3 (Marketing)
(11, N'20 Từ vựng chủ đề "Công nghệ" (Technology) bạn nên biết', 'blogs/vocab-technology.png', N'Học các từ vựng thiết yếu về AI, machine learning, và cybersecurity.', N'Nội dung chi tiết về danh sách từ vựng...', 3, 3, 'PUBLISHED', GETDATE(), GETDATE()),
(12, N'Từ vựng chủ đề "Môi trường" (Environment) cho IELTS', 'blogs/vocab-environment.png', N'Các collocations hay về climate change, global warming, và sustainable energy.', N'Nội dung chi tiết về danh sách từ vựng...', 3, 3, 'PUBLISHED', GETDATE(), GETDATE()),
(13, N'15 Idioms (Thành ngữ) phổ biến trong giao tiếp', 'blogs/common-idioms.png', N'Học các thành ngữ như "break a leg", "piece of cake" để nói tiếng Anh tự nhiên hơn.', N'Nội dung chi tiết về 15 thành ngữ...', 3, 3, 'PUBLISHED', GETDATE(), GETDATE()),
(14, N'Từ vựng chủ đề "Sức khỏe" (Health and Fitness)', 'blogs/vocab-health.png', N'Các từ vựng về nutrition, mental health, và physical exercise.', N'Nội dung chi tiết về danh sách từ vựng...', 3, 3, 'PUBLISHED', GETDATE(), GETDATE()),
(15, N'Học từ vựng qua phim ảnh: Phương pháp hiệu quả', 'blogs/vocab-movies.png', N'Hướng dẫn chi tiết cách học từ vựng qua Netflix mà không cần Vietsub.', N'Nội dung chi tiết về các bước học...', 3, 3, 'PUBLISHED', GETDATE(), GETDATE()),

-- Danh mục 4: Tiếng Anh Thương Mại (ID=4) - Tác giả 2, 6 (Experts)
(16, N'5 Mẫu câu tiếng Anh "bất bại" khi đàm phán', 'blogs/business-negotiation.png', N'Học cách đưa ra đề nghị, từ chối lịch sự và đạt được thỏa thuận.', N'Nội dung chi tiết về các mẫu câu...', 4, 2, 'PUBLISHED', GETDATE(), GETDATE()),
(17, N'Cách viết Email xin việc bằng Tiếng Anh (Kèm mẫu)', 'blogs/job-application-email.png', N'Hướng dẫn viết cover letter và email xin việc gây ấn tượng với nhà tuyển dụng.', N'Nội dung chi tiết về các mẫu email...', 4, 6, 'PUBLISHED', GETDATE(), GETDATE()),
(18, N'Làm thế nào để thuyết trình bằng Tiếng Anh tự tin?', 'blogs/business-presentation.png', N'Các bước chuẩn bị, cấu trúc bài thuyết trình và cách xử lý câu hỏi Q&A.', N'Nội dung chi tiết về kỹ năng thuyết trình...', 4, 2, 'PUBLISHED', GETDATE(), GETDATE()),
(19, N'Thuật ngữ Tiếng Anh trong cuộc họp (Meeting Vocabulary)', 'blogs/meeting-vocabulary.png', N'Các cụm từ dùng để bắt đầu cuộc họp, đưa ra ý kiến, ngắt lời và chốt vấn đề.', N'Nội dung chi... (agenda, minutes, consensus)...', 4, 6, 'PUBLISHED', GETDATE(), GETDATE()),
(20, N'Giao tiếp qua điện thoại (Telephoning) nơi công sở', 'blogs/telephoning-skills.png', N'Các mẫu câu nghe, gọi, và để lại lời nhắn một cách chuyên nghiệp.', N'Nội dung chi tiết về telephoning...', 4, 6, 'DRAFT', GETDATE(), GETDATE());
SET IDENTITY_INSERT blogs OFF;


-- ===================================================================
-- BỔ SUNG DỮ LIỆU MẪU (PHẦN 3)
-- ===================================================================
-- Thêm dữ liệu đa dạng hơn cho các khóa học khác (không chỉ khóa 1)
-- và bổ sung nội dung cho các quiz còn trống.

-- 11. Bảng ENROLLMENTS (Bổ sung cho các khóa học khác)
-- Ghi danh cho các user 7, 8, 9, 10 vào các khóa 2, 4, 7, 9
SET IDENTITY_INSERT enrollments ON;
INSERT INTO enrollments (id, user_id, course_id, enrolled_at, status) VALUES
-- Binh Le (user 7) đăng ký Khóa 2 (Giao tiếp công sở)
(22, 7, 2, DATEADD(day, -20, GETDATE()), 'ENROLLED'),
-- Chi Phan (user 8) đăng ký Khóa 4 (Từ vựng IELTS)
(23, 8, 4, DATEADD(day, -15, GETDATE()), 'ENROLLED'),
-- Dung Tran (user 9) đăng ký Khóa 7 (Luyện nghe IELTS)
(24, 9, 7, DATEADD(day, -10, GETDATE()), 'ENROLLED'),
-- Giang Hoang (user 10) đăng ký Khóa 9 (Viết luận IELTS)
(25, 10, 9, DATEADD(day, -5, GETDATE()), 'ENROLLED');
SET IDENTITY_INSERT enrollments OFF;

-- 12. Bảng FEEDBACKS (Bổ sung cho các khóa học 2, 4, 7, 9)
-- Thêm feedback cho các enrollment 22, 23, 24, 25
SET IDENTITY_INSERT feedbacks ON;
INSERT INTO feedbacks (id, rating, review, status, helpful_count, not_helpful_count, enrollment_id) VALUES
-- Feedback cho Enrollment 22 (Khóa 2)
(41, 5, N'Khóa học Giao tiếp công sở rất thực tế. Các mẫu email (Chương 4) rất hữu ích cho công việc của mình.', 'APPROVED', 8, 0, 22),
-- Feedback cho Enrollment 23 (Khóa 4)
(42, 4, N'Nội dung từ vựng IELTS (Khóa 4) tốt, nhưng file video của Chương 7 (Technology) thỉnh thoảng bị giật.', 'APPROVED', 3, 0, 23),
-- Feedback cho Enrollment 24 (Khóa 7)
(43, 5, N'Thầy Michael Chen (author_id 5) dạy khóa Listening (Khóa 7) rất hay, mẹo làm bài Multiple Choice (Chương 10) rất đỉnh.', 'APPROVED', 12, 0, 24),
-- Feedback cho Enrollment 25 (Khóa 9)
(44, 4, N'Mình đã hiểu rõ hơn về cách viết bài Agree/Disagree (Chương 12) sau khóa Writing (Khóa 9). Cảm ơn OELS.', 'APPROVED', 7, 0, 25);
SET IDENTITY_INSERT feedbacks OFF;

-- 13. Bảng QUESTIONS (Bổ sung cho Quiz: Environment Vocabulary, lesson_id = 6)
-- Thêm câu hỏi cho quiz của Khóa 4
SET IDENTITY_INSERT questions ON;
INSERT INTO questions (id, content, question_type, media_type, media_url, lesson_id, created_at, updated_at)
VALUES
    (17, N'Từ nào sau đây đồng nghĩa với "global warming"?', 'MULTIPLE_CHOICE', 'NONE', NULL, 6, GETDATE(), GETDATE()),
    (18, N'"Deforestation" có nghĩa là gì?', 'MULTIPLE_CHOICE', 'NONE', NULL, 6, GETDATE(), GETDATE()),
    (19, N'Năng lượng "sustainable" là năng lượng...', 'MULTIPLE_CHOICE', 'NONE', NULL, 6, GETDATE(), GETDATE()),
    (20, N'Chất thải "biodegradable" là chất thải...', 'MULTIPLE_CHOICE', 'NONE', NULL, 6, GETDATE(), GETDATE());
SET IDENTITY_INSERT questions OFF;

-- 14. Bảng ANSWER_OPTIONS (Cho các câu hỏi 17, 18, 19, 20)
SET IDENTITY_INSERT answer_options ON;
INSERT INTO answer_options (id, content, correct, explanation, question_id, created_at, updated_at)
VALUES
-- Q17
(43, N'Climate change', 1, N'"Global warming" (sự nóng lên toàn cầu) thường được dùng thay thế cho "climate change" (biến đổi khí hậu).', 17, GETDATE(), GETDATE()),
(44, N'Pollution', 0, N'Pollution (ô nhiễm) là một nguyên nhân, không phải từ đồng nghĩa.', 17, GETDATE(), GETDATE()),
(45, N'Biodiversity', 0, N'Biodiversity là đa dạng sinh học.', 17, GETDATE(), GETDATE()),
(46, N'Soil erosion', 0, N'Soil erosion là xói mòn đất.', 17, GETDATE(), GETDATE()),
-- Q18
(47, N'Nạn phá rừng', 1, N'Deforestation là hành động chặt phá rừng trên diện rộng.', 18, GETDATE(), GETDATE()),
(48, N'Trồng cây gây rừng', 0, N'Trồng cây là "afforestation" hoặc "reforestation".', 18, GETDATE(), GETDATE()),
(49, N'Ô nhiễm nguồn nước', 0, N'Đây là "water pollution".', 18, GETDATE(), GETDATE()),
(50, N'Hiệu ứng nhà kính', 0, N'Đây là "greenhouse effect".', 18, GETDATE(), GETDATE()),
-- Q19
(51, N'Bền vững, có thể tái tạo (như gió, mặt trời)', 1, N'Sustainable energy là năng lượng bền vững, không cạn kiệt.', 19, GETDATE(), GETDATE()),
(52, N'Hóa thạch (như than, dầu)', 0, N'Đây là "fossil fuels".', 19, GETDATE(), GETDATE()),
(53, N'Hạt nhân', 0, N'Đây là "nuclear energy".', 19, GETDATE(), GETDATE()),
(54, N'Gây ô nhiễm', 0, N'Năng lượng bền vững thường sạch và ít ô nhiễm.', 19, GETDATE(), GETDATE()),
-- Q20
(55, N'Có thể phân hủy sinh học', 1, N'Biodegradable có nghĩa là có thể bị phân hủy tự nhiên bởi vi sinh vật.', 20, GETDATE(), GETDATE()),
(56, N'Độc hại', 0, N'Độc hại là "toxic" hoặc "hazardous".', 20, GETDATE(), GETDATE()),
(57, N'Tái chế được', 0, N'Tái chế được là "recyclable".', 20, GETDATE(), GETDATE()),
(58, N'Không tan trong nước', 0, N'Đây là "insoluble".', 20, GETDATE(), GETDATE());
SET IDENTITY_INSERT answer_options OFF;

-- 15. Bảng WISHLIST (Bổ sung cho user 7 và 8)
SET IDENTITY_INSERT wishlist ON;
INSERT INTO wishlist (id, user_id, course_id, added_date) VALUES
-- User 7 (Binh Le) quan tâm Khóa 11 (Reading) và Khóa 12 (Giao tiếp nâng cao)
(3, 7, 11, DATEADD(day, -3, GETDATE())),
(4, 7, 12, DATEADD(day, -2, GETDATE())),
-- User 8 (Chi Phan) quan tâm Khóa 5 (Ngữ pháp)
(5, 8, 5, DATEADD(day, -1, GETDATE()));
SET IDENTITY_INSERT wishlist OFF;

-- 16. Bảng USER_LESSON_PROGRESS (Bổ sung cho Enrollment 23)
-- User 8 (Chi Phan) - Enrollment 23 (Course 4: Từ vựng IELTS - Gồm 4 lessons: 21, 6, 22, 7)
-- Giả sử đã học 1/4 lessons.
SET IDENTITY_INSERT user_lesson_progress ON;
INSERT INTO user_lesson_progress (id, user_id, lesson_id, enrollment_id, is_completed, completion_date) VALUES
                                                                                                            (11, 8, 21, 23, 1, DATEADD(day, -10, GETDATE())), -- Chapter 6 - Lecture (Completed)
                                                                                                            (12, 8, 6, 23, 0, NULL),                          -- Chapter 6 - Quiz (Not completed)
                                                                                                            (13, 8, 22, 23, 0, NULL),                          -- Chapter 7 - Lecture (Not completed)
                                                                                                            (14, 8, 7, 23, 0, NULL);                         -- Chapter 7 - Quiz (Not completed)
SET IDENTITY_INSERT user_lesson_progress OFF;

-- 17. Bảng NOTES (Bổ sung cho UserLessonID 11)
-- Ghi chú cho User 8 khi học Lesson 21 (Video: Từ vựng chủ đề Môi trường)
SET IDENTITY_INSERT notes ON;
INSERT INTO notes (id, time_at_lesson, content, user_lesson_id) VALUES
-- Ghi chú cho UserLessonID 11 (Lesson 21: Video: Từ vựng chủ đề Môi trường)
(21, '02:15', N'Note lại từ "biodegradable" - có thể phân hủy sinh học.', 11),
(22, '07:30', N'Phân biệt "climate change" (biến đổi khí hậu) và "global warming" (nóng lên toàn cầu).', 11),
(23, '11:00', N'Học cụm "carbon footprint" - dấu chân carbon.', 11);
SET IDENTITY_INSERT notes OFF;

-- 18. Bảng ORDERS (Bổ sung cho các enrollment 22, 23, 24, 25)
-- (Giả sử các đơn hàng này đã được thanh toán và liên kết với enrollment)
INSERT INTO orders (
    order_code,
    amount,
    order_info,
    status,
    vnp_response_code,
    vnp_transaction_no,
    user_id,
    course_id,
    created_at,
    updated_at
) VALUES
      ('ORD007', 1350000.0, N'Thanh toán khóa học Giao tiếp tự tin nơi công sở', 'PAID', '00', '123456793', 7, 2, DATEADD(day, -20, GETDATE()), DATEADD(day, -20, GETDATE())),
      ('ORD008', 1440000.0, N'Thanh toán khóa học Từ vựng IELTS theo chủ đề', 'PAID', '00', '123456794', 8, 4, DATEADD(day, -15, GETDATE()), DATEADD(day, -15, GETDATE())),
      ('ORD009', 1710000.0, N'Thanh toán khóa học Luyện nghe IELTS Listening Max Score', 'PAID', '00', '123456795', 9, 7, DATEADD(day, -10, GETDATE()), DATEADD(day, -10, GETDATE())),
      ('ORD010', 1785000.0, N'Thanh toán khóa học Viết luận IELTS Writing Task 2', 'PAID', '00', '123456796', 10, 9, DATEADD(day, -5, GETDATE()), DATEADD(day, -5, GETDATE()));

-- 5.1. Bảng SLIDERS
-- (Phụ thuộc vào user_id từ bảng USERS)
-- user_id = 1 (Admin)
-- user_id = 3 (Marketing)
-- link_url thường trỏ tới /courses/{id} hoặc /blogs/{id}
-- ===================================================================

SET IDENTITY_INSERT sliders ON;
INSERT INTO sliders (
    id, user_id, title, description, order_number,
    status, image_url, link_url,
    created_at, updated_at, view_count
) VALUES
      (
          1, 3, -- Jane Smith (Marketing)
          N'IELTS Speaking Masterclass 8.0+',
          N'Giảm giá 15% cho 100 lượt đăng ký đầu tiên. Chinh phục band điểm 8.0+ cùng chuyên gia John Doe.',
          1, 'SHOW', '/uploads/sliders/6578012.jpg', 'https://www.youtube.com/results?search_query=business+flow+diagram',
          GETDATE(), GETDATE(), 1520
      ),
      (
          2, 1, -- Admin Manager
          N'Luyện nghe IELTS Max Score',
          N'Khóa học mới ra mắt! Chiến thuật làm bài và luyện tập các dạng câu hỏi Listening.',
          2, 'SHOW', '/uploads/sliders/6692555.jpg', 'https://www.youtube.com/results?search_query=business+flow+diagram',
          GETDATE(), GETDATE(), 890
      ),
      (
          3, 3, -- Jane Smith (Marketing)
          N'Phát âm chuẩn Anh-Mỹ',
          N'Xây dựng nền tảng phát âm vững chắc với 44 âm IPA. Dành cho người mới bắt đầu.',
          3, 'SHOW', '/uploads/sliders/6718245.jpg', 'https://www.youtube.com/results?search_query=business+flow+diagram',
          GETDATE(), GETDATE(), 1105
      ),
      (
          4, 1, -- Admin Manager
          N'Thông báo: Nâng cấp hệ thống OELS 2.0',
          N'Hệ thống OELS sẽ được nâng cấp lên phiên bản 2.0 với nhiều tính năng hấp dẫn.',
          4, 'SHOW', '/uploads/sliders/slider_announcement.png', 'https://www.youtube.com/results?search_query=business+flow+diagram',
          GETDATE(), GETDATE(), 530
      ),
      (
          5, 3, -- Jane Smith (Marketing)
          N'Giảm giá hè 2024 (ĐÃ HẾT HẠN)',
          N'Chương trình giảm giá mùa hè đã kết thúc.',
          5, 'HIDE', '/uploads/sliders/slider_summer_promo_old.png', '#',
          DATEADD(day, -60, GETDATE()), DATEADD(day, -30, GETDATE()), 2500
      );
SET IDENTITY_INSERT sliders OFF;