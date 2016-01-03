package com.brave.mystery.controller;

import com.brave.mystery.model.User;
import com.brave.mystery.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageController extends BaseController {
    @Autowired
    UserService userService;

    @RequestMapping("/")
    public String index(Model model) {
        model.addAttribute("loggedIn", isLoggedIn());
        if (isLoggedIn()) {
            return "index";
        } else {
            return "login";
        }
    }

    @RequestMapping("/items")
    public String items(Model model) {
        model.addAttribute("loggedIn", isLoggedIn());
        if (isLoggedIn()) {
            return "items";
        } else {
            return "login";
        }
    }

    @RequestMapping("/order")
    public String order(Model model) {
        model.addAttribute("loggedIn", isLoggedIn());
        if (isLoggedIn()) {
            User user = getCurrentUser();
            if (user.getRole() == User.UserRole.MANAGER) {
                return "manager-orders";
            } else {
                return "order";
            }
        } else {
            return "login";
        }
    }

    @RequestMapping("/manageOrders")
    public String manageOrders(Model model) {
        model.addAttribute("loggedIn", isLoggedIn());
        if (isLoggedIn()) {
            return "manager-orders";
        } else {
            return "login";
        }
    }

    @RequestMapping("/new")
    public String newCharacter(Model model) {
        model.addAttribute("loggedIn", isLoggedIn());
        if (isLoggedIn()) {
            return "edit";
        } else {
            return "login";
        }
    }

    @RequestMapping("/login")
    public String login(Model model) {
        model.addAttribute("loggedIn", isLoggedIn());
        return "login";
    }
}
