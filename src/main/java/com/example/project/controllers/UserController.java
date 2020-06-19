package com.example.project.controllers;

import com.example.project.models.User;
import com.example.project.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@CrossOrigin(
        origins = "http://localhost:3000",
        allowCredentials = "true")
public class UserController {
  @Autowired
  UserService service;

  @PostMapping("/api/register")
  public User register(
          @RequestBody User user,
          HttpSession session) {
    User existingUser = service.findUserByUsername(user.getUsername());
    if (existingUser == null) {
      User currentUser = service.createUser(user);
      session.setAttribute("currentUser", currentUser);
      return currentUser;
    }
    return null;
  }
//    User currentUser = service.createUser(user);
//    session.setAttribute("currentUser", currentUser);
//    return currentUser;

  @PostMapping("/api/login")
  public User login(
          @RequestBody User user,
          HttpSession session) {
    User currentUser = service.findUserByCredentials(user.getUsername(), user.getPassword());
    session.setAttribute("currentUser", currentUser);
    return currentUser;
  }

  @GetMapping("/api/profile")
  public User profile(HttpSession session) {
    User currentUser = (User) session.getAttribute("currentUser");
    return currentUser;
  }

  @PutMapping("/api/profile/{uid}")
  public User profile(
          @RequestBody User user,
          HttpSession session, @PathVariable Integer uid) {
    User currentUser = service.updateUser(uid, user);
    session.setAttribute("currentUser", currentUser);
    return currentUser;
  }

  @PostMapping("/api/logout")
  public void logout(HttpSession session) {
    session.invalidate();
  }
}
