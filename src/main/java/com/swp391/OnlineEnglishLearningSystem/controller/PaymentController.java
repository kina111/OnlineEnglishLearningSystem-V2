package com.swp391.OnlineEnglishLearningSystem.controller;

import com.swp391.OnlineEnglishLearningSystem.config.VNPayConfig;
import com.swp391.OnlineEnglishLearningSystem.model.Course;
import com.swp391.OnlineEnglishLearningSystem.model.Enrollment;
import com.swp391.OnlineEnglishLearningSystem.model.Order;
import com.swp391.OnlineEnglishLearningSystem.model.User; // Import User
import com.swp391.OnlineEnglishLearningSystem.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.PathVariable;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Controller
public class PaymentController {

    private final VNPayService vnPayService;
    private final OrderService orderService; // Service để lưu Order
    private final UserService userService; // Để lấy thông tin user
    private final CourseService courseService;
    private final EnrollmentService enrollmentService;

    public PaymentController(VNPayService vnPayService, OrderService orderService, UserService userService, CourseService courseService, EnrollmentService enrollmentService) {
        this.vnPayService = vnPayService;
        this.orderService = orderService;
        this.userService = userService;
        this.courseService = courseService;
        this.enrollmentService = enrollmentService;
    }

    @GetMapping("/payment/checkout/{courseId}")
    public String showCheckoutPage(@PathVariable("courseId") Long courseId,
                                   HttpSession session, Model model) {

        // 1. Kiểm tra đăng nhập
        Long userId = (Long) session.getAttribute("currentUserId");
        if (userId == null) {
            return "redirect:/login";
        }

        // 2. Lấy thông tin khóa học
        Course course = courseService.findById(courseId);
        if (course == null) {
            return "redirect:/courses?error=NotFound"; // Về trang chủ nếu ko thấy khóa học
        }

        model.addAttribute("course", course);

        // 3. Tính toán lại giá tiền để hiển thị (an toàn)
        double finalPrice = course.getPrice() * (1 - course.getDiscount() / 100.0);
        model.addAttribute("finalPrice", finalPrice);

        // 4. Trả về file HTML checkout mới
        return "user/checkout";
    }

    // Endpoint này được gọi khi người dùng nhấn nút "Thanh toán"
    @PostMapping("/payment/create-vnpay-order")
    public String createVNPayOrder(@RequestParam("courseId") Long courseId,
                                   HttpServletRequest request,
                                   HttpSession session) throws UnsupportedEncodingException {

        // 1. Lấy thông tin User (ví dụ từ session)
        Long userId = (Long) session.getAttribute("currentUserId");
        if (userId == null) {
            return "redirect:/login"; // Chưa đăng nhập
        }
        User currentUser = this.userService.getUserById(userId);
        Course currentCourse = this.courseService.findById(courseId);
        // 2. Tạo đối tượng Order và lưu vào DB với status PENDING
        Order newOrder = this.orderService.createNewOrder(currentUser, currentCourse);

        String paymentUrl = vnPayService.createVNPayPaymentUrl(newOrder, request);

        // 3. Chuyển hướng người dùng sang VNPay
        return "redirect:" + paymentUrl;
    }

    //Code chuẩn: khi deploy, sẽ xác minh và cập nhật db ở đây chứ không phải /payment/vnpay-ipn
    /*
    @GetMapping("/payment/vnpay-ipn")
    @ResponseBody
    public String handleIPN(HttpServletRequest request) {
        // 1. Lấy tất cả tham số VNPAY gửi về
        Map<String, String> vnpParams = new HashMap<>();
        Enumeration<String> paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String fieldName = paramNames.nextElement();
            String fieldValue = request.getParameter(fieldName);
            vnpParams.put(fieldName, fieldValue);
        }

        // 2. Gọi Service để xử lý
        String response = this.vnPayService.processIPN(vnpParams);

        // 3. nếu response thành công, update vào enrollment.

        // 3. Trả về chuỗi JSON "00" hoặc "97", "01",... cho VNPAY
        return response;
    }*/

    @GetMapping("/payment/vnpay-return")
    public String handleVNPayReturn(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        //checkSum
        Map fields = new HashMap();
        for (Enumeration params = request.getParameterNames(); params.hasMoreElements();) {
            String fieldName = (String) params.nextElement();
            String fieldValue = request.getParameter(fieldName);
            if ((fieldValue != null) && (!fieldValue.isEmpty())) {
                fields.put(fieldName, fieldValue);
            }
        }

        String vnp_SecureHash = request.getParameter("vnp_SecureHash");
        fields.remove("vnp_SecureHashType");
        fields.remove("vnp_SecureHash");

        String hashData = VNPayConfig.buildHashData(fields);

        String signValue = VNPayConfig.hmacSHA512(VNPayConfig.secretKey, hashData);

        if (signValue.equals(vnp_SecureHash)) {
            if ("00".equals(request.getParameter("vnp_ResponseCode"))) {
                Order updatedOrder = this.orderService.update(fields);
                Enrollment newEnrollment = this.enrollmentService.createNew(updatedOrder);

                redirectAttributes.addAttribute("status", "success");
            } else {
                redirectAttributes.addAttribute("status", "failed");
            }
        } else {
            redirectAttributes.addAttribute("status", "invalid signature");
        }
        return "redirect:/payment/result";
    }

    @GetMapping("/payment/result")
    public String showPaymentResult(@RequestParam Map<String, String> params, Model model) {
        // Lấy các tham số từ URL (do redirectAttributes thêm vào)
        model.addAttribute("params", params);

        // (Bạn cần tạo file HTML cho trang này)
        return "user/paymentResult";
    }
}