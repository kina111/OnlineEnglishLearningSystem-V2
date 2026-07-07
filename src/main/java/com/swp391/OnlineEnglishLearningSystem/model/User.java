package com.swp391.OnlineEnglishLearningSystem.model;

import com.swp391.OnlineEnglishLearningSystem.util.ValidEmail;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User extends BaseEntity implements UserDetails {

    public enum Gender {
        MALE("Male"),
        FEMALE("Female"),
        OTHER("Other");

        private final String displayName;

        Gender(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ValidEmail(message = "Email không hợp lệ")
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank(message = "Password không được để trống!")
    @Column(nullable = false)
    private String password;

    @NotBlank(message = "Fullname không được để trống")
    @Column(nullable = false, name = "full_name", columnDefinition = "NVARCHAR(100)")
    private String fullName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private Gender gender;

    private String mobile;

    @Column(nullable = true, columnDefinition = "NVARCHAR(1000)")
    private String address;

    private String avatar;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dob;

    private boolean enabled;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    private UserRole role;

    @OneToMany(mappedBy = "author")
    private List<Course> courses = new ArrayList<>();

    @OneToMany(mappedBy = "author")
    private List<Blog> blogs = new ArrayList<>();

    public User() {
    }

    public User(String email,
                String password,
                String fullName,
                Gender gender,
                String mobile,
                String address,
                String avatar,
                @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dob,
                boolean enabled) {
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.gender = gender;
        this.mobile = mobile;
        this.address = address;
        this.avatar = avatar;
        this.enabled = enabled;
        this.dob = dob;
    }

    public User(Long id) {
        this.id = id;
    }

    // ===== UserDetails interface overrides =====
    // KHÔNG xóa các method này — đây là implementation của Spring Security UserDetails

    @Override
    public String getUsername() {
        // Trả về email làm username — KHÔNG được để Lombok sinh method này
        return email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role.getName());
        return List.of(authority);
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
