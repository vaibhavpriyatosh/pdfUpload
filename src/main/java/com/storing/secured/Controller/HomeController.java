package com.storing.secured.Controller;

import com.storing.secured.Entity.User;
import com.storing.secured.Security.JwtHelper;
import com.storing.secured.Service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/home")
@CrossOrigin("*")
public class HomeController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    HttpServletRequest request;

    //http://localhost:8081/home/user
    @GetMapping("/")
    public String landing(){
        System.out.println("On landing");
        String requestHeader = request.getHeader("Authorization");
        String token = requestHeader.substring(7);
        String temp= (jwtHelper.getUsernameFromToken(token));

        System.out.println(userService.getUserByEmail(temp).getUserId());

        return temp;
    }

    @GetMapping("/users")
    public List<User> getUsers(){
        return userService.getUsers();
    }

}
