package com.example.SportLogin.config;
import com.example.SportLogin.model.Role;
import com.example.SportLogin.model.User;
import com.example.SportLogin.repository.RoleRepository;
import com.example.SportLogin.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class AdminInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AdminInitializer(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }
//
//    @PostConstruct
//    public void initAdmin() {
//        // Check if admin user already exists
//        if (!userRepository.existsByUsername("admin")) {
//            // Create admin user
//            User adminUser = new User();
//            adminUser.setName("Admin");
//            adminUser.setUsername("admin");
//            adminUser.setEmail("admin@example.com");
//            adminUser.setPassword(passwordEncoder.encode("adminpassword"));
//
//            // Add admin role
//            Role adminRole = roleRepository.findByName("ROLE_ADMIN");
//            adminUser.setRoles(Collections.singleton(adminRole));
//
//            userRepository.save(adminUser);
//        }
//    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // Check if admin user already exists
        if (!userRepository.existsByUsername("admin")) {
            // Create admin user
            User adminUser = new User();
            adminUser.setName("Admin");
            adminUser.setUsername("admin");
            adminUser.setEmail("admin@gmail.com");
            adminUser.setPassword(passwordEncoder.encode("admin@123"));

            // Add admin role
            Role adminRole = roleRepository.findByName("ROLE_ADMIN");

            if(adminRole==null){
                adminRole.setName("ROLE_ADMIN");
                roleRepository.save(adminRole);
            }
            adminUser.setRoles(Collections.singleton(adminRole));

            userRepository.save(adminUser);
        }
    }
}