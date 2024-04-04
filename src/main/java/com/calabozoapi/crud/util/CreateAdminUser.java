package com.calabozoapi.crud.util;

import com.calabozoapi.crud.security.entity.Rol;
import com.calabozoapi.crud.security.entity.Usuario;
import com.calabozoapi.crud.security.enums.RolNombre;
import com.calabozoapi.crud.security.service.RolService;
import com.calabozoapi.crud.security.service.UsuarioService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class CreateAdminUser implements CommandLineRunner {

    private final UsuarioService usuarioService;
    private final RolService rolService;
    private final PasswordEncoder passwordEncoder;

    public CreateAdminUser(UsuarioService usuarioService, RolService rolService, PasswordEncoder passwordEncoder) {
        this.usuarioService = usuarioService;
        this.rolService = rolService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // Obtener el rol administrador existente
        Rol rolAdmin = rolService.getByRolNombre(RolNombre.ROLE_ADMIN)
                .orElseGet(() -> {
                    // Si no existe, crearlo y guardarlo en la base de datos
                    Rol newRolAdmin = new Rol(RolNombre.ROLE_ADMIN);
                    rolService.save(newRolAdmin);
                    return newRolAdmin;
                });

        // Crear un usuario administrador
        Usuario adminUser = new Usuario();
        adminUser.setNombre("Admin");
        adminUser.setNombreUsuario("admin");
        adminUser.setEmail("admin@example.com");
        adminUser.setPassword(passwordEncoder.encode("adminpassword"));

        Set<Rol> roles = new HashSet<>();
        roles.add(rolAdmin); // Asignar el rol de administrador
        adminUser.setRoles(roles);

        usuarioService.save(adminUser);
    }
}
