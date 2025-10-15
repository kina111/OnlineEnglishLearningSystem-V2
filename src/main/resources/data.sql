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
INSERT INTO courses (id, name, short_description, description, prerequisite, thumbnail, price, discount, featured, status, total_lesson, category_id, author_id) VALUES
    (1, 'IELTS Speaking Masterclass: Band 8.0+', N'Nâng cao kỹ năng nói IELTS từ cơ bản đến nâng cao, tập trung vào sự trôi chảy và từ vựng học thuật.',
     N'Đây là khóa học toàn diện giúp bạn chinh phục band điểm 8.0+ trong phần thi IELTS Speaking.
     * Phân tích chi tiết các dạng câu hỏi Part 1, 2, 3.
     * Cung cấp bộ từ vựng "ăn điểm" theo chủ đề.
     * Luyện tập với các đề thi thật.',
     N' * Yêu cầu trình độ tiếng Anh tương đương 5.0 IELTS.
     * Cần có microphone để thực hành ghi âm.',
     'thumbnails/ielts_speaking.png', 2000000.0, 15.0, 1, 0, 0, 2, 2),

    (2, N'Giao tiếp tự tin nơi công sở', N'Khóa học giúp bạn tự tin sử dụng tiếng Anh trong môi trường làm việc chuyên nghiệp.',
     N'Bạn sẽ học cách:
     * Viết email chuyên nghiệp.
     * Thuyết trình và trình bày ý tưởng.
     * Tham gia các cuộc họp và thảo luận.',
     N' * Trình độ tiếng Anh cơ bản.
     * Mong muốn cải thiện kỹ năng giao tiếp.',
     'thumbnails/business_english.png', 1500000.0, 10.0, 0, 2, 0, 3, 2),

    (3, N'Phát âm chuẩn Anh-Mỹ cho người mới bắt đầu', N'Xây dựng nền tảng phát âm vững chắc với 44 âm IPA và các quy tắc nối âm, ngữ điệu.',
     N'Khóa học này sẽ giúp bạn nói tiếng Anh rõ ràng và tự nhiên hơn. Chúng tôi sẽ đi sâu vào từng âm trong bảng phiên âm quốc tế IPA, giúp bạn sửa các lỗi phát âm phổ biến.',
     N' * Không yêu cầu đầu vào.
     * Phù hợp cho mọi đối tượng muốn cải thiện phát âm.',
     'thumbnails/pronunciation.png', 1200000.0, 0.0, 1, 1, 0, 1, 2);
SET IDENTITY_INSERT courses OFF;

-- 5. Bảng CHAPTERS (Phụ thuộc vào COURSES)
-- Chương cho khóa học "IELTS Speaking Masterclass" (course_id = 1)
SET IDENTITY_INSERT chapters ON;
INSERT INTO chapters (id, name, short_description, order_number, course_id) VALUES
    (1, N'Giới thiệu về IELTS Speaking', N'Tổng quan về cấu trúc bài thi và các tiêu chí chấm điểm.', 1, 1),
    (2, N'Chiến lược trả lời Part 1', N'Cách trả lời các câu hỏi về bản thân một cách tự nhiên và ấn tượng.', 2, 1),
    (3, N'Xây dựng bài nói Part 2', N'Phương pháp A.R.E.A để phát triển ý tưởng cho bài nói dài 2 phút.', 3, 1),
    -- Chương cho khóa học "Giao tiếp tự tin nơi công sở" (course_id = 2)
    (4, N'Kỹ năng viết Email', N'Các mẫu email thông dụng và cách sử dụng ngôn từ chuyên nghiệp.', 1, 2),
    (5, N'Tham gia họp trực tuyến', N'Các cụm từ hữu ích khi phát biểu ý kiến, đồng ý và phản đối.', 2, 2);
SET IDENTITY_INSERT chapters OFF;

-- 6. Bảng LESSONS (Phụ thuộc vào CHAPTERS)
SET IDENTITY_INSERT lessons ON;
INSERT INTO lessons (
    id, title, order_number, lesson_type, estimated_time, pass_rate, time_limit_in_minutes,
    html_content, video_url, chapter_id
) VALUES
      -- Quiz cho chương "Giới thiệu về IELTS Speaking" (chapter_id = 1)
      (1, N'Quiz 1: Tổng quan IELTS Speaking', 1, 'QUIZ', 15, 60, 10,
       N'<p>Bài kiểm tra tổng quan kiến thức cơ bản về cấu trúc và tiêu chí chấm điểm phần thi Speaking.</p>',
       NULL, 1),

      -- Quiz cho chương "Chiến lược trả lời Part 1" (chapter_id = 2)
      (2, N'Quiz 2: Câu hỏi Part 1', 2, 'QUIZ', 20, 70, 15,
       N'<p>Kiểm tra khả năng phản xạ và vốn từ vựng trong phần Part 1 của bài thi.</p>',
       NULL, 2),

      -- Quiz cho chương "Xây dựng bài nói Part 2" (chapter_id = 3)
      (3, N'Quiz 3: Topic Development', 3, 'QUIZ', 25, 75, 20,
       N'<p>Đánh giá khả năng phát triển ý tưởng và sử dụng cấu trúc nâng cao trong Part 2.</p>',
       NULL, 3),

      -- Quiz cho chương "Kỹ năng viết Email" (chapter_id = 4)
      (4, N'Quiz 4: Email chuyên nghiệp', 1, 'QUIZ', 10, 60, 8,
       N'<p>Bài kiểm tra chọn câu đúng về ngữ pháp và từ vựng trong Email công sở.</p>',
       NULL, 4),

      -- Quiz cho chương "Tham gia họp trực tuyến" (chapter_id = 5)
      (5, N'Quiz 5: Giao tiếp trong cuộc họp', 2, 'QUIZ', 15, 65, 10,
       N'<p>Bài trắc nghiệm tình huống khi tham gia thảo luận và phát biểu trong họp trực tuyến.</p>',
       NULL, 5);
SET IDENTITY_INSERT lessons OFF;
