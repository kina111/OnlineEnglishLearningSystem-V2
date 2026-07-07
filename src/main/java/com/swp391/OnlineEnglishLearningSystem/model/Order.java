package com.swp391.OnlineEnglishLearningSystem.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "orders")
public class Order extends BaseEntity {
    public enum OrderStatus {
        PENDING("Đang chờ"),
        PAID("Đã thanh toán"),
        FAILED("Thất bại"),
        CANCELLED("Đã hủy");

        private final String displayName;

        OrderStatus(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String orderCode;

    @Column(nullable = false)
    private double amount;

    @Column(nullable = false, columnDefinition = "NVARCHAR(MAX)")
    private String orderInfo; //dùng cho vnp_OrderInfo

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status = OrderStatus.PENDING;

    @Column(length = 2)
    private String vnpResponseCode; //mã phản hồi từ VNPay (vnp_ResponseCode) sau khi xử lí IPN/Return

    @Column(length = 20)
    private String vnpTransactionNo; //mã giao dịch của vnpay (vnp_TransactionNo)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    public Order() {
        super();
    }
}
