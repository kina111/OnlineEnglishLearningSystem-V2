package com.swp391.OnlineEnglishLearningSystem.service.impl;

import com.swp391.OnlineEnglishLearningSystem.model.UserRole;
import com.swp391.OnlineEnglishLearningSystem.repository.RoleRepository;
import com.swp391.OnlineEnglishLearningSystem.service.RoleService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }
    @Override
    public List<UserRole> findAll() {
        return this.roleRepository.findAll();
    }
}
