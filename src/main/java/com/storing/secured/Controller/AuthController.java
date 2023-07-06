package com.storing.secured.Controller;

import com.storing.secured.Entity.Attachment;
import com.storing.secured.Entity.User;
import com.storing.secured.Model.Attachment.AttachmentResponse;
import com.storing.secured.Model.Jwt.JwtRequest;
import com.storing.secured.Model.Jwt.JwtResponse;
import com.storing.secured.Security.JwtHelper;
import com.storing.secured.Service.AttachmentService;
import com.storing.secured.Service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
public class AuthController {

    @Autowired
    private AttachmentService attachmentService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthenticationManager manager;


    @Autowired
    private JwtHelper helper;

    private Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private UserService userService;


    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest request) {

        this.doAuthenticate(request.getEmail(), request.getPassword());


        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        String token = this.helper.generateToken(userDetails);

        JwtResponse response = JwtResponse.builder()
                .jwtToken(token)
                .userId(userDetails.getUsername()).build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @CrossOrigin
    private void doAuthenticate(String email, String password) {

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email, password);
        try {
            manager.authenticate(authentication);


        } catch (BadCredentialsException e) {
            throw new BadCredentialsException(" Invalid Username or Password  !!");
        }

    }

    @ExceptionHandler(BadCredentialsException.class)
    public String exceptionHandler() {
        return "Credentials Invalid !!";
    }

    @PostMapping("/create-user")
    public User createUser(@RequestBody  User user){

        return (User) userService.createUser(user);
    }
    @PostMapping("/upload")
    public AttachmentResponse uploacFile(@RequestParam("file") MultipartFile file) throws Exception {
        Attachment attachment=null;
        attachment =attachmentService.saveAttachment(file);
        String downloadUrl= ServletUriComponentsBuilder.fromCurrentContextPath().replacePath("/home/download/").path(attachment.getId()).toUriString();
        return new AttachmentResponse(
                attachment.getFileName(),downloadUrl, file.getContentType(),attachment.getComments()
        );
    }

}