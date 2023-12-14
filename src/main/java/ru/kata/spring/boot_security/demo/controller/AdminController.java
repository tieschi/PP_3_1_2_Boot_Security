package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("")
    public String userList(Model model) {
        model.addAttribute("allUsers", userService.getAllUsers());
        return "allUsers";
    }

    @GetMapping("/addUser")
    public String addNewUser(Model model) {

        User user = new User();
        model.addAttribute("user", user);

        return "info";
    }

    @RequestMapping("/update")
    public String updateUser(@RequestParam("id") int id, Model model) {

        User user = userService.getUser(id);
        model.addAttribute("user", user);

        return "info";
    }

    @PostMapping("/saveUser")
    public String saveUser(@Valid User user, BindingResult bindingResult,
                           @RequestParam(required=false) String roleUser,
                           @RequestParam(required=false) String roleAdmin) {
        Set<Role> roles = new HashSet<>();
        if (bindingResult.hasErrors()) {
            return "info";
        } else {
            if (roleUser != null && roleUser.equals("ROLE_USER")) {
                roles.add(roleService.findRoleByName("ROLE_USER"));
            }
            if (roleAdmin != null && roleAdmin.equals("ROLE_ADMIN")) {
                roles.add(roleService.findRoleByName("ROLE_ADMIN"));
            }
            user.setRoles(roles);
            userService.addUser(user);
            return "redirect:/admin";
        }
    }
    @RequestMapping("/delete")
    public String deleteUser(@RequestParam("id") int id) {

        userService.deleteUser(id);

        return "redirect:/admin";
    }
}
