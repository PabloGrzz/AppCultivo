package com.example.BackEndApiPlantas.service;

import com.example.BackEndApiPlantas.model.Users;
import com.example.BackEndApiPlantas.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private JWTService jwtService;

    @Autowired
    AuthenticationManager authManager;

    @Autowired
    private UserRepo repo;


    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public Users register(Users user) { 
        user.setPassword(encoder.encode(user.getPassword()));
        repo.save(user);
        return user;
    }

    public String verify(Users user) {
        Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        if (authentication.isAuthenticated()) {
            // Buscar el usuario para obtener su rol
            Users userFromDB = findByUsername(user.getUsername());
            // Generar token incluyendo el rol
            return jwtService.generateToken(user.getUsername(), userFromDB.getRole().name());
        } else {
            return "fail";
        }
    }
    
    public Users findByUsername(String username) {
        Users user = repo.findByUsername(username); 
        if (user == null) {
            throw new UsernameNotFoundException("Usuario no encontrado");
        }
        return user;
    }

    public void updateUser(Users user) {
        repo.save(user);
    }
    
}
