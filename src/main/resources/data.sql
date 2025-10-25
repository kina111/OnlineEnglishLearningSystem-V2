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
                                                                                                               (4, 'user@example.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07c209qnMrHlClDW2W', 'Alice Student', 'FEMALE', '0955667788', '101 Student Lane, Can Tho', 'avatars/student_alice.png', '2002-12-10', 1, 4);
SET IDENTITY_INSERT users OFF;

-- 3. Bảng COURSE_CATEGORIES (Không phụ thuộc)
SET IDENTITY_INSERT course_categories ON;
INSERT INTO course_categories (id, name, description, active) VALUES
                                                                  (1, N'Tiếng Anh Giao Tiếp', N'Các khóa học tập trung vào kỹ năng nghe và nói trong các tình huống hàng ngày.', 1),
                                                                  (2, N'Luyện thi IELTS', N'Các khóa học chuyên sâu về chiến lược, từ vựng và ngữ pháp cho kỳ thi IELTS.', 1),
                                                                  (3, N'Tiếng Anh Thương Mại', N'Các khóa học dành cho người đi làm, tập trung vào email, thuyết trình và đàm phán.', 0);
SET IDENTITY_INSERT course_categories OFF;

-- 4. Bảng COURSES (Phụ thuộc vào COURSE_CATEGORIES và USERS (author_id))
-- author_id = 2 là của user 'expert@example.com'
SET IDENTITY_INSERT courses ON;
-- *** SỬA Ở ĐÂY: Thêm created_at, updated_at vào danh sách cột ***
INSERT INTO courses (id, name, short_description, description, prerequisite, thumbnail, price, discount, featured, status, category_id, author_id, created_at, updated_at) VALUES
                                                                                                                                                                               (1, 'IELTS Speaking Masterclass: Band 8.0+', N'Nâng cao kỹ năng nói IELTS từ cơ bản đến nâng cao, tập trung vào sự trôi chảy và từ vựng học thuật.',
                                                                                                                                                                                N'Đây là khóa học toàn diện giúp bạn chinh phục band điểm 8.0+ trong phần thi IELTS Speaking...',
                                                                                                                                                                                N' * Yêu cầu trình độ tiếng Anh tương đương 5.0 IELTS...',
                                                                                                                                                                                'thumbnails/ielts_speaking.png', 2000000.0, 15.0, 1, 0, 2, 2, GETDATE(), GETDATE()), -- *** SỬA Ở ĐÂY: Thêm GETDATE() ***

                                                                                                                                                                               (2, N'Giao tiếp tự tin nơi công sở', N'Khóa học giúp bạn tự tin sử dụng tiếng Anh trong môi trường làm việc chuyên nghiệp.',
                                                                                                                                                                                N'Bạn sẽ học cách:\n     * Viết email chuyên nghiệp...',
                                                                                                                                                                                N' * Trình độ tiếng Anh cơ bản...',
                                                                                                                                                                                'thumbnails/business_english.png', 1500000.0, 10.0, 0, 2, 3, 2, GETDATE(), GETDATE()), -- *** SỬA Ở ĐÂY: Thêm GETDATE() ***

                                                                                                                                                                               (3, N'Phát âm chuẩn Anh-Mỹ cho người mới bắt đầu', N'Xây dựng nền tảng phát âm vững chắc với 44 âm IPA...',
                                                                                                                                                                                N'Khóa học này sẽ giúp bạn nói tiếng Anh rõ ràng và tự nhiên hơn...',
                                                                                                                                                                                N' * Không yêu cầu đầu vào...',
                                                                                                                                                                                'thumbnails/pronunciation.png', 1200000.0, 0.0, 1, 1, 1, 2, GETDATE(), GETDATE()); -- *** SỬA Ở ĐÂY: Thêm GETDATE() ***
SET IDENTITY_INSERT courses OFF;

-- 5. Bảng CHAPTERS (Phụ thuộc vào COURSES)
-- Không cần sửa vì Chapter không kế thừa BaseEntity (giả định)
SET IDENTITY_INSERT chapters ON;
INSERT INTO chapters (id, name, short_description, order_number, course_id) VALUES
                                                                                (1, N'Giới thiệu về IELTS Speaking', N'Tổng quan về cấu trúc bài thi và các tiêu chí chấm điểm.', 1, 1),
                                                                                (2, N'Chiến lược trả lời Part 1', N'Cách trả lời các câu hỏi về bản thân một cách tự nhiên và ấn tượng.', 2, 1),
                                                                                (3, N'Xây dựng bài nói Part 2', N'Phương pháp A.R.E.A để phát triển ý tưởng cho bài nói dài 2 phút.', 3, 1),
                                                                                (4, N'Kỹ năng viết Email', N'Các mẫu email thông dụng và cách sử dụng ngôn từ chuyên nghiệp.', 1, 2),
                                                                                (5, N'Tham gia họp trực tuyến', N'Các cụm từ hữu ích khi phát biểu ý kiến, đồng ý và phản đối.', 2, 2);
SET IDENTITY_INSERT chapters OFF;

-- 6. Bảng LESSONS (Phụ thuộc vào CHAPTERS)
-- Không cần sửa vì Lesson không kế thừa BaseEntity (giả định)
SET IDENTITY_INSERT lessons ON;
INSERT INTO lessons (
    id, title, order_number, lesson_type, estimated_time, pass_rate, time_limit_in_minutes,
    html_content, video_url, chapter_id, number_of_questions
) VALUES
      (1, N'Quiz 1: Tổng quan IELTS Speaking', 1, 'QUIZ', 15, 60, 10, N'<p>Bài kiểm tra tổng quan...</p>', NULL, 1, 15),
      (2, N'Quiz 2: Câu hỏi Part 1', 2, 'QUIZ', 20, 70, 15, N'<p>Kiểm tra khả năng phản xạ...</p>', NULL, 2, 20),
      (3, N'Quiz 3: Topic Development', 3, 'QUIZ', 25, 75, 20, N'<p>Đánh giá khả năng phát triển...</p>', NULL, 3, 25),
      (4, N'Quiz 4: Email chuyên nghiệp', 1, 'QUIZ', 10, 60, 8, N'<p>Bài kiểm tra chọn câu đúng...</p>', NULL, 4, 30),
      (5, N'Quiz 5: Giao tiếp trong cuộc họp', 2, 'QUIZ', 15, 65, 10, N'<p>Bài trắc nghiệm tình huống...</p>', NULL, 5, 10);
SET IDENTITY_INSERT lessons OFF;

-- *** BỔ SUNG DỮ LIỆU ***

-- Bổ sung Users (Experts)
SET IDENTITY_INSERT users ON;
INSERT INTO users (id, email, password, full_name, gender, mobile, address, avatar, dob, enabled, role_id) VALUES
                                                                                                               (5, 'expert2@example.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07c209qnMrHlClDW2W', 'Michael Chen', 'MALE', '0922334455', '111 IELTS Street, Hanoi', 'avatars/expert_michael.png', '1988-03-10', 1, 2),
                                                                                                               (6, 'expert3@example.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07c209qnMrHlClDW2W', 'Sophia Nguyen', 'FEMALE', '0933445566', '222 TOEIC Road, HCMC', 'avatars/expert_sophia.png', '1991-11-25', 1, 2);
SET IDENTITY_INSERT users OFF;

-- Bổ sung Courses (Thêm 9 khóa học)
SET IDENTITY_INSERT courses ON;
-- *** SỬA Ở ĐÂY: Thêm created_at, updated_at vào danh sách cột ***
INSERT INTO courses (id, name, short_description, description, prerequisite, thumbnail, price, discount, featured, status, category_id, author_id, created_at, updated_at) VALUES
                                                                                                                                                                               -- Expert 1 (ID=2)
                                                                                                                                                                               (4, N'Từ vựng IELTS theo chủ đề', N'Học 1000+ từ vựng...', N'Phân loại từ vựng...', N'Trình độ IELTS 4.5+', 'courses/thumbnails/6195519.jpg', 1800000.0, 20.0, 0, 0, 2, 2, GETDATE(), GETDATE()), -- *** SỬA Ở ĐÂY ***
                                                                                                                                                                               (5, N'Ngữ pháp Tiếng Anh nâng cao', N'Ôn tập và nâng cao...', N'Đi sâu vào các điểm ngữ pháp...', N'Trình độ Intermediate trở lên', 'courses/thumbnails/6734768.jpg', 1600000.0, 5.0, 1, 1, 1, 2, GETDATE(), GETDATE()), -- *** SỬA Ở ĐÂY ***
                                                                                                                                                                               (6, N'Thuyết trình Tiếng Anh chuyên nghiệp', N'Kỹ năng chuẩn bị...', N'Học cách xây dựng cấu trúc...', N'Trình độ Upper-Intermediate', 'courses/thumbnails/6743349.jpg', 1700000.0, 0.0, 0, 2, 3, 2, GETDATE(), GETDATE()), -- *** SỬA Ở ĐÂY ***

                                                                                                                                                                               -- Expert 2 (ID=5)
                                                                                                                                                                               (7, N'Luyện nghe IELTS Listening Max Score', N'Chiến thuật làm bài...', N'Phân tích các dạng bài...', N'Trình độ IELTS 5.0+', 'courses/thumbnails/7420258.jpg', 1900000.0, 10.0, 1, 0, 2, 5, GETDATE(), GETDATE()), -- *** SỬA Ở ĐÂY ***
                                                                                                                                                                               (8, N'Tiếng Anh du lịch cho người Việt', N'Các mẫu câu và từ vựng...', N'Bao gồm các tình huống...', N'Trình độ Basic', 'courses/thumbnails/10019196.jpg', 900000.0, 0.0, 0, 1, 1, 5, GETDATE(), GETDATE()), -- *** SỬA Ở ĐÂY ***
                                                                                                                                                                               (9, N'Viết luận IELTS Writing Task 2', N'Hướng dẫn viết...', N'Phân tích đề...', N'Trình độ IELTS 5.5+', 'courses/thumbnails/ielts_writing2.png', 2100000.0, 15.0, 1, 2, 2, 5, GETDATE(), GETDATE()), -- *** SỬA Ở ĐÂY ***

                                                                                                                                                                               -- Expert 3 (ID=6)
                                                                                                                                                                               (10, N'Tiếng Anh phỏng vấn xin việc', N'Chuẩn bị cho các câu hỏi...', N'Cách giới thiệu bản thân...', N'Trình độ Intermediate', 'courses/thumbnails/job_interview.png', 1300000.0, 5.0, 0, 0, 3, 6, GETDATE(), GETDATE()), -- *** SỬA Ở ĐÂY ***
                                                                                                                                                                               (11, N'Luyện đọc IELTS Reading Intensive', N'Kỹ năng Skimming...', N'Thực hành với các bài đọc...', N'Trình độ IELTS 5.0+', 'courses/thumbnails/ielts_reading.png', 1800000.0, 10.0, 0, 1, 2, 6, GETDATE(), GETDATE()), -- *** SỬA Ở ĐÂY ***
                                                                                                                                                                               (12, N'Tiếng Anh giao tiếp hàng ngày (Nâng cao)', N'Mở rộng vốn từ lóng...', N'Thảo luận các chủ đề...', N'Trình độ Upper-Intermediate', 'courses/thumbnails/daily_english_adv.png', 1400000.0, 0.0, 1, 0, 1, 6, GETDATE(), GETDATE()); -- *** SỬA Ở ĐÂY ***
SET IDENTITY_INSERT courses OFF;

-- Bổ sung Chapters cho các khóa học mới
-- Không cần sửa
SET IDENTITY_INSERT chapters ON;
INSERT INTO chapters (id, name, short_description, order_number, course_id) VALUES
                                                                                (6, N'Chủ đề Environment', N'Từ vựng về môi trường, biến đổi khí hậu.', 1, 4),
                                                                                (7, N'Chủ đề Technology', N'Từ vựng về công nghệ, internet, AI.', 2, 4),
                                                                                (8, N'Mệnh đề quan hệ (Relative Clauses)', N'Cách dùng who, whom, which, that, whose.', 1, 5),
                                                                                (9, N'Cấu trúc bài thuyết trình', N'Mở bài, thân bài, kết luận hiệu quả.', 1, 6),
                                                                                (10, N'Dạng bài Multiple Choice', N'Chiến thuật chọn đáp án đúng.', 1, 7),
                                                                                (11, N'Tại sân bay', N'Check-in, hải quan, tìm cổng bay.', 1, 8),
                                                                                (12, N'Dạng bài Agree/Disagree', N'Cách trình bày quan điểm cá nhân.', 1, 9),
                                                                                (13, N'Giới thiệu bản thân (Tell me about yourself)', N'Cách tóm tắt kinh nghiệm ấn tượng.', 1, 10),
                                                                                (14, N'Kỹ năng Skimming & Scanning', N'Đọc lướt và đọc quét tìm thông tin.', 1, 11),
                                                                                (15, N'Thành ngữ (Idioms) thông dụng', N'Học các thành ngữ phổ biến trong giao tiếp.', 1, 12);
SET IDENTITY_INSERT chapters OFF;

-- Bổ sung Lessons (Quizzes) cho các chapter mới
-- Không cần sửa
SET IDENTITY_INSERT lessons ON;
INSERT INTO lessons (
    id, title, order_number, lesson_type, estimated_time, pass_rate, time_limit_in_minutes,
    html_content, video_url, chapter_id, number_of_questions
) VALUES
      (6, N'Quiz: Environment Vocabulary', 1, 'QUIZ', 10, 70, 8, N'<p>Kiểm tra từ vựng...</p>', NULL, 6, 20),
      (7, N'Quiz: Technology Terms', 1, 'QUIZ', 10, 70, 8, N'<p>Kiểm tra từ vựng...</p>', NULL, 7, 20),
      (8, N'Quiz: Relative Clauses Practice', 1, 'QUIZ', 15, 65, 12, N'<p>Bài tập điền từ...</p>', NULL, 8, 15),
      (9, N'Quiz: Presentation Structure', 1, 'QUIZ', 10, 60, 7, N'<p>Trắc nghiệm về cấu trúc...</p>', NULL, 9, 10),
      (10, N'Quiz: Listening Multiple Choice', 1, 'QUIZ', 20, 75, 15, N'<p>Luyện nghe dạng...</p>', NULL, 10, 18),
      (11, N'Quiz: Airport Situations', 1, 'QUIZ', 8, 50, 5, N'<p>Chọn mẫu câu phù hợp...</p>', NULL, 11, 12),
      (12, N'Quiz: Agree/Disagree Essays', 1, 'QUIZ', 25, 70, 20, N'<p>Kiểm tra cấu trúc...</p>', NULL, 12, 10),
      (13, N'Quiz: Common Interview Questions', 1, 'QUIZ', 15, 60, 10, N'<p>Trắc nghiệm cách trả lời...</p>', NULL, 13, 15),
      (14, N'Quiz: Reading Skills', 1, 'QUIZ', 20, 65, 15, N'<p>Bài tập Skimming...</p>', NULL, 14, 20),
      (15, N'Quiz: Idioms Matching', 1, 'QUIZ', 10, 70, 8, N'<p>Nối thành ngữ...</p>', NULL, 15, 25);
SET IDENTITY_INSERT lessons OFF;

-- *** KẾT THÚC PHẦN BỔ SUNG ***
