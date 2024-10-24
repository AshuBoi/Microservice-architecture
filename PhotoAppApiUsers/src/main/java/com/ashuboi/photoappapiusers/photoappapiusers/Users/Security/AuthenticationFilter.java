package com.ashuboi.photoappapiusers.photoappapiusers.Users.Security;


import com.ashuboi.photoappapiusers.photoappapiusers.Shared.UserDto;
import com.ashuboi.photoappapiusers.photoappapiusers.Users.Service.UserService;
import com.ashuboi.photoappapiusers.photoappapiusers.Users.ui.Model.LoginRequestModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.Objects;

// this comes from org.springframework.security.web package
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private UserService userService;
    private Environment env;
    private BCryptPasswordEncoder bCryptPasswordEncoder;



    public AuthenticationFilter(AuthenticationManager authenticationManager, Environment env, UserService userService) {
        super(authenticationManager);
        this.env = env;
        this.userService = userService;
        this.bCryptPasswordEncoder = new BCryptPasswordEncoder();
    }



    // When client application sends a request to perform a user login, Spring framework will invoke this auth method
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try{
            LoginRequestModel creds = new ObjectMapper().readValue(request.getInputStream(), LoginRequestModel.class);

            //we are invoking authenticate method on authenticationManager object that will be returned when we call
            // getAuthenticationManager method and spring will be able to authenticate the user
            // they will check if the username password exits in database
            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            creds.getEmail(),
                            creds.getPassword(),
                            new ArrayList<>()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    // if user Auth is successful then Spring Framework will invoke this method for us and we can run any business logic we want to run in it
    // we dont need to call this method ourselves, Spring Framework will call this message itself if user auth is successful
    // here we take user details like, for example, user name or userID to generate jwt token access token and add this token to HttpSession header and return it
    // we need to have public userId because we use this id as a subject claim in jwt token
    // to find JWT token we use Object of Authentication , that gives us a user object that represents currently authenticated user who has just successfully logged in
    //
    // once user performs login and their username and password are correct, spring framework will invoke this method called successful login
    // and this method generates JWT access token and it will add it to HTTP response together with userID
    // the client application that performed login and HTTP request should get back in response headers
    // .
    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) throws IOException, ServletException {
        String userName = ((User) auth.getPrincipal()).getUsername();
        UserDto userDetails = userService.getUserByEmail(userName);

        String token = Jwts.builder()
                .subject(userDetails.getUserId())
                .expiration(new Date(System.currentTimeMillis() + Long.parseLong(env.getProperty("token.expiration_time"))))
                .signWith(SignatureAlgorithm.HS512,
                        Keys.secretKeyFor(SignatureAlgorithm.HS512)) // Use Keys.secretKeyFor
                .compact();

        res.addHeader("token", token);
        res.addHeader("userId", userDetails.getUserId());
    }


}
