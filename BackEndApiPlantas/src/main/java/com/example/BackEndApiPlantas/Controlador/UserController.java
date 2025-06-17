package com.example.BackEndApiPlantas.Controlador;

import com.example.BackEndApiPlantas.model.UserPrincipal;
import com.example.BackEndApiPlantas.model.Users;
import com.example.BackEndApiPlantas.service.UserService;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth/")
public class UserController {

    @Autowired
    private UserService service;

    @PostMapping("register")
    public Users register(@RequestBody Users user) {
        return service.register(user);

    }

    @PostMapping("login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Users user) {
        String token = service.verify(user);
        if (!token.equals("fail")) {
            return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON) // 👈 Asegura el Content-Type
                .body(Map.of("token", token)); // 👈 Envuelve el token en un JSON
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
    
    @PostMapping("change-role")
    public ResponseEntity<String> changeUserRole(
            @RequestParam String username,
            @RequestParam Users.Role newRole,
            @AuthenticationPrincipal UserPrincipal currentUser) {
        
        // Verifica si el usuario actual es ADMIN
        if (!currentUser.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Solo ADMIN puede cambiar roles");
        }

        try {
            Users user = service.findByUsername(username);
            user.setRole(newRole);
            service.updateUser(user);
            return ResponseEntity.ok("Rol actualizado a " + newRole.name());
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
}
