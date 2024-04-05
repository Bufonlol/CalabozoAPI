package com.calabozoapi.crud.util;

import com.calabozoapi.crud.security.entity.Rol;
import com.calabozoapi.crud.security.enums.RolNombre;
import com.calabozoapi.crud.security.service.RolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CreateRoles implements CommandLineRunner {

    @Autowired
    RolService rolService;

    @Override
    public void run(String... args) throws Exception {
        Optional<Rol> rolAdminOptional = rolService.getByRolNombre(RolNombre.ROLE_ADMIN);
        if (!rolAdminOptional.isPresent()) {
            Rol rolAdmin = new Rol(RolNombre.ROLE_ADMIN);
            rolService.save(rolAdmin);
        }

        Optional<Rol> rolUserOptional = rolService.getByRolNombre(RolNombre.ROLE_USER);
        if (!rolUserOptional.isPresent()) {
            Rol rolUser = new Rol(RolNombre.ROLE_USER);
            rolService.save(rolUser);
        }
    }
}

