package com.swp391.OnlineEnglishLearningSystem.controller;

import com.swp391.OnlineEnglishLearningSystem.model.CourseCategory;
import com.swp391.OnlineEnglishLearningSystem.model.Order;
import com.swp391.OnlineEnglishLearningSystem.model.User;
import com.swp391.OnlineEnglishLearningSystem.model.dto.OrderFilter;
import com.swp391.OnlineEnglishLearningSystem.service.*;
import jakarta.validation.Valid;
import org.springframework.data.domain.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final UploadService uploadService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final CourseCategoryService courseCategoryService;
    private final OrderService orderService;

    public AdminController(UserService userService, UploadService uploadService, RoleService roleService, PasswordEncoder passwordEncoder, EmailService emailService, CourseCategoryService courseCategoryService, OrderService orderService) {
        this.userService = userService;
        this.uploadService = uploadService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.courseCategoryService = courseCategoryService;
        this.orderService = orderService;
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
                String avatarFileName = uploadService.uploadImage(avatarFile, "avatars");
                user.setAvatar(avatarFileName);
            }
            String plainPassword = user.getPassword();
            // Encode password and set default values
            String encodedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encodedPassword);
            user.setEnabled(true);

            // Save user
            userService.save(user);

            // Send email notification
            emailService.sendEmail(user.getEmail(), "Tài khoản đã được tạo bởi Quản trị viên",
                    emailService.buildEmailContent(plainPassword));

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

    //===================== COURSE CATEGORY ===============================
    @GetMapping("/course_categories")
    public String getCourseCategories(Model model,
                           @RequestParam(value = "active", required = false) Boolean active,
                           @RequestParam(value = "search", required = false) String search,
                           @RequestParam(value = "page", defaultValue = "0") int page,
                           @RequestParam(value = "size", defaultValue = "10") int size) {


        Pageable pageable = PageRequest.of(page, size);

        Page<CourseCategory> courseCategoryPage = this.courseCategoryService.getCourseCategoriesWithSpecs(pageable, active, search);

        model.addAttribute("courseCategoryPage", courseCategoryPage);

        int totalPages = courseCategoryPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().toList();
            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "admin/courseCategory/list";
    }

    @GetMapping("/course_categories/create")
    public String createCourseCategoryForm(Model model) {
        model.addAttribute("courseCategory", new CourseCategory());
        return "admin/courseCategory/create";
    }

    @PostMapping("/course_categories/create")
    public String createCourseCategory(@Valid @ModelAttribute("courseCategory") CourseCategory courseCategory,
                                     BindingResult bindingResult,
                                     RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "admin/courseCategory/create";
        }
        try{
            courseCategoryService.ensureNotDuplicateName(courseCategory.getName());
            courseCategoryService.save(courseCategory);
            return "redirect:/admin/course_categories";
        }catch (Exception e) {
            bindingResult.rejectValue("name", "error.courseCategory", e.getMessage());
            return "redirect:/admin/course_categories";
        }
    }

    @GetMapping("/course_categories/update/{id}")
    public String createCourseCategoryForm(@PathVariable("id") Long id, Model model,
                                           RedirectAttributes redirectAttributes) {
        try{
            CourseCategory cc = this.courseCategoryService.getById(id);
            model.addAttribute("courseCategory", cc);
            return "admin/courseCategory/update";
        }catch (Exception e) {
            return "redirect:/admin/course_categories";
        }
    }

    @PostMapping("course_categories/update")
    public String updateCourseCategory(@Valid @ModelAttribute("courseCategory") CourseCategory courseCategory,
                                     BindingResult bindingResult,
                                     RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "admin/courseCategory/update";
        }
        try{
            CourseCategory cc = this.courseCategoryService.getById(courseCategory.getId());
            this.courseCategoryService.ensureNotDuplicateName(courseCategory.getName());

            cc.setName(courseCategory.getName());
            cc.setDescription(courseCategory.getDescription());
            cc.setActive(courseCategory.isActive());
            this.courseCategoryService.save(cc);

            return "redirect:/admin/course_categories";
        }catch (Exception e) {
            bindingResult.rejectValue("name", "error.courseCategory", e.getMessage());
            return "redirect:/admin/course_categories";
        }
    }

    @GetMapping("/orders")
    public String orderManagement(Model model, @RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "5") int size,
                                  @RequestParam(required = false, defaultValue = "") String status,
                                  @RequestParam(required = false, defaultValue = "updatedAt") String sortBy,
                                  @RequestParam(required = false, defaultValue = "DESC") String sortDir,
                                  @RequestParam(required = false, defaultValue = "") String keyword,
                                  @RequestParam(required = false,defaultValue = "line" )String chartType,
                                  @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startUpdate,
                                  @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endUpdate){
//        List<Order> orders = orderService.getAllOrders();
        OrderFilter filter = new OrderFilter();
        filter.setStatus(status);
        filter.setSortBy(sortBy);
        filter.setSortDir(sortDir);
        filter.setSearch(keyword);
        if(startUpdate != null){
            filter.setStartUpdate(startUpdate.atStartOfDay());
        }
        if(endUpdate != null){
            filter.setEndUpdate(endUpdate.atStartOfDay().plusDays(1));
        }

        List<Order> orders = orderService.getOrdersWithSpecs(filter);
        //List to page
        int start = Math.min(page * size, orders.size());
        int end = Math.min((page + 1) * size, orders.size());
        List<Order> pagedOrders = orders.subList(start, end);
        Page<Order> ordersPage = new PageImpl<>(pagedOrders, PageRequest.of(page, size), orders.size());
        //total amount
        double totalAmount = orders.stream()
                .mapToDouble(Order::getAmount)
                .sum();

        // 4️⃣ Tính doanh thu theo tháng (YearMonth)
        double filteredAmount = ordersPage.stream().mapToDouble(Order::getAmount).sum();
        Map<YearMonth, Double> revenueByMonth = orders.stream()
                .collect(Collectors.groupingBy(
                        order -> YearMonth.from(order.getUpdatedAt()), // group theo tháng-năm của updatedAt
                        TreeMap::new, // sắp xếp theo thứ tự thời gian
                        Collectors.summingDouble(Order::getAmount)
                ));

        // 5️⃣ Chuyển sang 2 danh sách để hiển thị trên biểu đồ (JS dễ dùng)
        List<String> monthLabels = revenueByMonth.keySet().stream()
                .map(ym -> ym.toString()) // dạng "2025-01"
                .toList();
        List<Double> monthTotals = new ArrayList<>(revenueByMonth.values());

        model.addAttribute("startUpdate", startUpdate);
        model.addAttribute("endUpdate", endUpdate);
        model.addAttribute("orders", ordersPage);
        model.addAttribute("allStatuses", Order.OrderStatus.values());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", ordersPage.getTotalPages());
        model.addAttribute("pageSize", size);
        model.addAttribute("filter", filter);
        model.addAttribute("chartType", chartType);
        model.addAttribute("totalAmount", totalAmount);
        model.addAttribute("allOrders", orders);
        model.addAttribute("monthLabels", monthLabels);
        model.addAttribute("monthTotals", monthTotals);
        return "admin/orderDashboard";
    }
}