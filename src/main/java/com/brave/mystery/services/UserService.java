package com.brave.mystery.services;

import com.brave.mystery.model.User;
import com.brave.mystery.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by dcohen on 3/4/15.
 */
@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public User getByEmail(String email) {
        List<User> users = userRepository.findByEmailIgnoreCase(email);
        if (users != null && users.size() > 0) {
        	return users.get(0);
        } else {
        	return null;
        }
    }
}
