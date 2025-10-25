package com.swp391.OnlineEnglishLearningSystem.service.impl;

import com.swp391.OnlineEnglishLearningSystem.model.Course;
import com.swp391.OnlineEnglishLearningSystem.model.User;
import com.swp391.OnlineEnglishLearningSystem.model.Wishlist;
import com.swp391.OnlineEnglishLearningSystem.repository.WishlistRepository;
import com.swp391.OnlineEnglishLearningSystem.service.WishlistService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WishlistServiceImpl implements WishlistService {
    private final WishlistRepository wishlistRepository;

    public WishlistServiceImpl(WishlistRepository wishlistRepository) {
        this.wishlistRepository = wishlistRepository;
    }

    @Override
    public Wishlist createNew(User currentUser, Course currentCourse) {
        return this.wishlistRepository.save(new Wishlist(currentUser, currentCourse));
    }

    @Override
    public List<Wishlist> findByUser(User user) {
        if (user == null) throw new IllegalArgumentException("User not found");
        return this.wishlistRepository.findByUserWithCourse(user);
    }

    @Override
    public Optional<Wishlist> findByUserAndCourse(User currentUser, Course currentCourse) {
        return this.wishlistRepository.findByUserAndCourse(currentUser, currentCourse);
    }

    @Override
    public void delete(Wishlist wishlist) {
        this.wishlistRepository.delete(wishlist);
    }
}
