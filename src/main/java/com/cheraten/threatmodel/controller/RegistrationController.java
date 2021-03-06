package com.cheraten.threatmodel.controller;

import com.cheraten.threatmodel.entity.User;
import com.cheraten.threatmodel.service.ISystemService;
import com.cheraten.threatmodel.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class RegistrationController {
    @Autowired
    private ISystemService isystemService;
    @Autowired
    private UserService userService;

    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("userForm", new User());

        return "registration.jsp";
    }

    @PostMapping("/registration")
    public String addUser(@ModelAttribute("userForm") @Valid User userForm, BindingResult bindingResult, Model model) {
        if (userForm.getUsername().equals("")) {
            model.addAttribute("usernameError", "Введите имя пользователя!");
            return "registration.jsp";
        }
        if (userForm.getPassword().equals("")) {
            model.addAttribute("passwordError", "Введите пароль!");
            return "registration.jsp";
        }
        if (userForm.getPasswordConfirm().equals("")) {
            model.addAttribute("passwordError", "Подтвердите введённый пароль!");
            return "registration.jsp";
        }
        if (bindingResult.hasErrors()) {
            return "registration.jsp";
        }
        if (!userForm.getPassword().equals(userForm.getPasswordConfirm())){
            model.addAttribute("passwordError", "Пароли не совпадают");
            return "registration.jsp";
        }
        if (!userService.saveUser(userForm)){
            model.addAttribute("usernameError", "Пользователь с таким именем уже существует");
            return "registration.jsp";
        }
        isystemService.setFullThreatListByISystems();

        return "modeling.jsp";
    }
}
