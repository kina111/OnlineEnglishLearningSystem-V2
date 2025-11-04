package com.swp391.OnlineEnglishLearningSystem.service.impl;

import com.swp391.OnlineEnglishLearningSystem.config.VNPayConfig;
import com.swp391.OnlineEnglishLearningSystem.model.Course;
import com.swp391.OnlineEnglishLearningSystem.model.Order;
import com.swp391.OnlineEnglishLearningSystem.model.User;
import com.swp391.OnlineEnglishLearningSystem.model.dto.OrderFilter;
import com.swp391.OnlineEnglishLearningSystem.repository.OrderRepository;
import com.swp391.OnlineEnglishLearningSystem.service.OrderService;
import com.swp391.OnlineEnglishLearningSystem.service.specification.OrderSpecs;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Order createNewOrder(User currentUser, Course currentCourse) {
        Order newOrder = new Order();
        newOrder.setOrderCode(VNPayConfig.getRandomNumber(8));
        newOrder.setAmount(currentCourse.getPrice() * (1 - currentCourse.getDiscount()/100));
        newOrder.setOrderInfo("THANH TOAN KHOA HOC");
        newOrder.setUser(currentUser);
        newOrder.setCourse(currentCourse);
        newOrder.setStatus(Order.OrderStatus.PENDING);
        return this.orderRepository.save(newOrder);
    }

    /**
     * Hàm xử lý IPN (Code chuẩn)
     */
    /*@Override
    public String processIPN(Map<String, String> vnpParams) {
        final String RspCode_Success = "00";
        final String RspCode_OrderNotFound = "01";
        final String RspCode_OrderAlreadyConfirmed = "02";
        final String RspCode_InvalidAmount = "04";
        final String RspCode_InvalidSignature = "97";
        final String RspCode_SystemError = "99";
        try
        {
            String vnp_SecureHash = vnpParams.get("vnp_SecureHash");
            vnpParams.remove("vnp_SecureHashType");
            vnpParams.remove("vnp_SecureHash");

            // Check checksum
            String signValue = VNPayConfig.hashAllFields(vnpParams);
            if (!signValue.equals(vnp_SecureHash)){
                return "{\"RspCode\":\"" + RspCode_InvalidSignature + "\",\"Message\":\"Invalid Checksum\"}";
            }
            // 3. Lấy thông tin
            // vnp_TxnRef chính là Order ID của chúng ta
            String vnp_TxnRef = vnpParams.get("vnp_TxnRef");
            String vnp_Amount_str = vnpParams.get("vnp_Amount");
            String vnp_ResponseCode = vnpParams.get("vnp_ResponseCode");
            String vnp_TransactionNo = vnpParams.get("vnp_TransactionNo");

            // 4. Kiểm tra Order (checkOrderId)
            Order order = this.orderRepository.findById(Long.parseLong(vnp_TxnRef)).orElse(null);
            if (order == null) {
                return "{\"RspCode\":\"" + RspCode_OrderNotFound + "\",\"Message\":\"Order not Found\"}";
            }

            // 5. Kiểm tra số tiền (checkAmount)
            // SỬA LỖI 3: Logic so sánh Amount bị sai
            long vnpAmount = Long.parseLong(vnp_Amount_str); // (Vd: 152000000)
            long orderAmount = (long) (order.getAmount() * 100); // (Vd: 1520000.0 * 100 = 152000000)
            if (orderAmount != vnpAmount) {
                return "{\"RspCode\":\"" + RspCode_InvalidAmount + "\",\"Message\":\"Invalid Amount\"}";
            }

            // 6. Kiểm tra trạng thái (checkOrderStatus)
            if (order.getStatus() != Order.OrderStatus.PENDING) {
                return "{\"RspCode\":\"" + RspCode_OrderAlreadyConfirmed + "\",\"Message\":\"Order already confirmed\"}";
            }

            // 7. Cập nhật trạng thái
            if ("00".equals(vnp_ResponseCode)) {
                order.setStatus(Order.OrderStatus.PAID);
            } else {
                order.setStatus(Order.OrderStatus.FAILED);
            }

            // 8. Lưu bằng chứng và CẬP NHẬT
            order.setVnpResponseCode(vnp_ResponseCode);
            order.setVnpTransactionNo(vnp_TransactionNo);
            this.orderRepository.save(order);

            // 9. Trả về 00
            return "{\"RspCode\":\"" + RspCode_Success + "\",\"Message\":\"Confirm Success\"}";
        }

        catch(Exception e)
        {
            return "{\"RspCode\":\"" + RspCode_SystemError + "\",\"Message\":\"System Error\"}";
        }
    }*/

/*
    @Override
    public Order processReturn(Map<String, String> vnpParams) {
        // 1. Kiểm tra chữ ký (Checksum)
        String vnp_SecureHash = vnpParams.get("vnp_SecureHash");
        vnpParams.remove("vnp_SecureHashType");
        vnpParams.remove("vnp_SecureHash");

        String signValue = VNPayConfig.hashAllFields(vnpParams);
        if (!signValue.equals(vnp_SecureHash)) throw new RuntimeException("Invalid checksum");

        // 2. Lấy thông tin
        String vnp_TxnRef = vnpParams.get("vnp_TxnRef");
        String vnp_Amount_str = vnpParams.get("vnp_Amount");
        String vnp_ResponseCode = vnpParams.get("vnp_ResponseCode");
        String vnp_TransactionNo = vnpParams.get("vnp_TransactionNo");

        // 3. Kiểm tra Order
        Order order = this.findByVnp_TxnRef(vnp_TxnRef);
        if (order == null) throw new RuntimeException("Order not found");

        // 4. Kiểm tra số tiền
        long vnpAmount = Long.parseLong(vnp_Amount_str);
        long orderAmount = (long) (order.getAmount() * 100);
        if (orderAmount != vnpAmount) throw new RuntimeException("Invalid amount");

        // 5. Kiểm tra trạng thái (Chỉ cập nhật nếu đang PENDING)
        if (order.getStatus() == Order.OrderStatus.PENDING) {
            if ("00".equals(vnp_ResponseCode)) {
                order.setStatus(Order.OrderStatus.PAID);
            } else {
                order.setStatus(Order.OrderStatus.FAILED);
            }
            // 6. Lưu bằng chứng và CẬP NHẬT DB
            order.setVnpResponseCode(vnp_ResponseCode);
            order.setVnpTransactionNo(vnp_TransactionNo);
            orderRepository.save(order);

        }
        // Nếu không PENDING (đã xử lý bởi IPN) thì cứ trả về order
        return order;
    }
*/

    @Override
    public Order update(Map fields) {
        Order order = this.orderRepository.findByOrderCode(fields.get("vnp_TxnRef").toString());
        order.setStatus(Order.OrderStatus.PAID);
        order.setVnpResponseCode("00");
        order.setVnpTransactionNo(fields.get("vnp_TransactionNo").toString());
        return this.orderRepository.save(order);
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Page<Order> getOrdersWithSpecs(OrderFilter filter,int page, int size) {
//        String range = filter.;
        String status = filter.getStatus();
        String sortBy = filter.getSortBy();
        String direction = filter.getSortDir();
        if (direction == null) direction = "DESC";
        direction = direction.equalsIgnoreCase("desc") ? "DESC" : "ASC";
        LocalDateTime updatedFrom = filter.getStartUpdate();

        Specification<Order> spec = null;
        if (updatedFrom != null) {
            spec = OrderSpecs.fromUpdateDate(updatedFrom);
        }
        if(status != null && !status.isEmpty())
        spec = OrderSpecs.hasStatus(status);

        Sort sort = Sort.by(Sort.Direction.fromString(direction),  sortBy != null ? sortBy : "updatedAt");

        Pageable pageable = PageRequest.of(page, size, sort);
        return orderRepository.findAll(spec, pageable);
    }
}
