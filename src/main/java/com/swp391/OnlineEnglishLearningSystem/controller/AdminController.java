package com.swp391.OnlineEnglishLearningSystem.controller;

import com.swp391.OnlineEnglishLearningSystem.model.User;
import com.swp391.OnlineEnglishLearningSystem.service.EmailService;
import com.swp391.OnlineEnglishLearningSystem.service.RoleService;
import com.swp391.OnlineEnglishLearningSystem.service.UploadService;
import com.swp391.OnlineEnglishLearningSystem.service.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final UploadService uploadService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public AdminController(UserService userService, UploadService uploadService, RoleService roleService, PasswordEncoder passwordEncoder, EmailService emailService) {
        this.userService = userService;
        this.uploadService = uploadService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    //===================== DASHBOARD ========================
    @GetMapping("")
    public String admin(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "admin/dashboard";
    }

    @GetMapping("/users")
    public String getUsers(Model model,
                           @RequestParam(value = "role", required = false) String role,
                           @RequestParam(value = "gender", required = false) String gender,
                           @RequestParam(value = "enabled", required = false) Boolean enabled,
                           @RequestParam(value = "search", required = false) String search,
                           @RequestParam(value = "page", defaultValue = "0") int page,
                           @RequestParam(value = "size", defaultValue = "10") int size,
                           @RequestParam(value = "sort", defaultValue = "id") String sort,
                           @RequestParam(value = "direction", defaultValue = "asc") String direction) {

        // Tạo Sort object
        Sort sortObj = createSort(sort, direction);
        Pageable pageable = PageRequest.of(page, size, sortObj);

        Page<User> userPage = this.userService.getUsersWithSpecs(pageable, gender, role, enabled, search);

        model.addAttribute("userPage", userPage);
        model.addAttribute("currentSort", sort);
        model.addAttribute("currentDirection", direction);

        int totalPages = userPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().toList();
            model.addAttribute("pageNumbers", pageNumbers);
        }

        model.addAttribute("roles", roleService.findAll());
        model.addAttribute("genders", User.Gender.values());
        return "admin/userList";
    }

    private Sort createSort(String sort, String direction) {
        Sort.Direction sortDirection = direction.equalsIgnoreCase("desc") ?
                Sort.Direction.DESC : Sort.Direction.ASC;

        switch (sort) {
            case "fullName":
                return Sort.by(sortDirection, "fullName");
            case "gender":
                return Sort.by(sortDirection, "gender");
            case "email":
                return Sort.by(sortDirection, "email");
            case "mobile":
                return Sort.by(sortDirection, "mobile");
            case "role":
                return Sort.by(sortDirection, "role.name");
            default: // id
                return Sort.by(sortDirection, "id");
        }
    }

    //===================== CREATE USER ========================
    @GetMapping("/users/create")
    public String createUserForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("genders", User.Gender.values());
        model.addAttribute("roles", roleService.findAll());
        return "admin/createUser";
    }

    @PostMapping("/users/create")
    public String createUser(@Valid @ModelAttribute("user") User user,
                             BindingResult bindingResult,
                             @RequestParam("avatarFile") MultipartFile avatarFile,
                             RedirectAttributes redirectAttributes,
                             Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("genders", User.Gender.values());
            model.addAttribute("roles", roleService.findAll());
            return "admin/createUser";
        }

        try {
            userService.ensureEmailNotExists(user.getEmail());

            if (avatarFile != null && !avatarFile.isEmpty()) {
                String avatarFileName = uploadService.uploadImage(avatarFile);
                user.setAvatar(avatarFileName);
            }

            // Encode password and set default values
            String encodedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encodedPassword);
            user.setEnabled(true);

            // Save user
            userService.save(user);

            // Send email notification
            emailService.sendEmail(user.getEmail(), "Tài khoản đã được tạo bởi Quản trị viên",
                    emailService.buildEmailContent(user.getPassword()));

            redirectAttributes.addFlashAttribute("successMessage", "Tạo người dùng thành công!");
            return "redirect:/admin/users";

        } catch (IllegalArgumentException e) {
            bindingResult.rejectValue("email", "error.user", e.getMessage());
            model.addAttribute("genders", User.Gender.values());
            model.addAttribute("roles", roleService.findAll());
            return "admin/createUser";
        } catch (Exception e) {
            bindingResult.rejectValue("email", "error.user", "Đã xảy ra lỗi khi tạo người dùng: " + e.getMessage());
            model.addAttribute("genders", User.Gender.values());
            model.addAttribute("roles", roleService.findAll());
            return "admin/createUser";
        }
    }

    //===================== UPDATE USER ========================
    @GetMapping("/users/update/{id}")
    public String getViewAndUpdateForm(@PathVariable("id") Long id, Model model){
        try{
            User user = this.userService.getUserById(id);
            model.addAttribute("user", user);
            model.addAttribute("roles", roleService.findAll());
            return "admin/updateUser";
        }catch(Exception e){
            return "redirect:/admin/users";
        }
    }

    @PostMapping("/users/update")
    public String updateUser(@Valid @ModelAttribute("user") User user,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes){
        if(bindingResult.hasErrors()){
            return "admin/updateUser";
        }
        try{
            User currentUser = this.userService.getUserById(user.getId());
            currentUser.setRole(user.getRole());
            currentUser.setEnabled(user.isEnabled());
            this.userService.save(currentUser);

            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật người dùng thành công!");
            return "redirect:/admin/users";
        }catch(Exception e){
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi khi cập nhật người dùng: " + e.getMessage());
            return "redirect:/admin/users";
        }
    }

    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id, RedirectAttributes redirectAttributes){
        try{
            this.userService.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Xóa người dùng thành công!");
            return "redirect:/admin/users";
        }catch(Exception e){
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi khi xóa người dùng: " + e.getMessage());
            return "redirect:/admin/users";
        }
    }
}