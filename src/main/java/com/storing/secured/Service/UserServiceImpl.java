package com.storing.secured.Service;

import com.storing.secured.Entity.User;
import com.storing.secured.Repositories.UserRepository;
import com.storing.secured.Security.JwtHelper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    HttpServletRequest request;

    @Autowired
    private JwtHelper jwtHelper;

    private User user;
    public void setUser (){
        String requestHeader = request.getHeader("Authorization");
        String token = requestHeader.substring(7);
        String email= (jwtHelper.getUsernameFromToken(token));
        System.out.println("email"+email);
        user= this.getUserByEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // load user from database

        UserDetails userDetails=userRepository.findByEmail((username)).orElseThrow(()-> new RuntimeException("User Not Found!"));

        return userDetails;
    }

    @Override
    public List<User> getUsers(){
        return userRepository.findAll();
    }


    @Override
    public User getUserByEmail(String email) {
        Optional<User> user= userRepository.findByEmail(email);
        return user.get();
    }

    @Override
    public User createUser(User user){


        user.setUserId(UUID.randomUUID().toString());
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }



}
