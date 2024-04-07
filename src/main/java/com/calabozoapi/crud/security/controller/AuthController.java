package com.calabozoapi.crud.security.controller;

import com.calabozoapi.crud.dto.Mensaje;
import com.calabozoapi.crud.security.dto.JwtDto;
import com.calabozoapi.crud.security.dto.LoginUsuario;
import com.calabozoapi.crud.security.dto.NuevoUsuario;
import com.calabozoapi.crud.security.entity.Rol;
import com.calabozoapi.crud.security.entity.Usuario;
import com.calabozoapi.crud.security.enums.RolNombre;
import com.calabozoapi.crud.security.jwt.JwtProvider;
import com.calabozoapi.crud.security.repository.UsuarioRepository;
import com.calabozoapi.crud.security.service.RolService;
import com.calabozoapi.crud.security.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {

    @Autowired
    PasswordEncoder passwordEncoder;



    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    RolService rolService;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    JwtProvider jwtProvider;

    @PostMapping("/nuevo")
    public ResponseEntity<?> nuevo(@Valid @RequestBody NuevoUsuario nuevoUsuario, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return new ResponseEntity<>(new Mensaje("campos mal puestos o email inválido"), HttpStatus.BAD_REQUEST);

        if (usuarioService.existsByNombreUsuario(nuevoUsuario.getNombreUsuario()))
            return new ResponseEntity<>(new Mensaje("ese nombre ya existe"), HttpStatus.BAD_REQUEST);

        if (usuarioService.existsByEmail(nuevoUsuario.getEmail()))
            return new ResponseEntity<>(new Mensaje("ese email ya existe"), HttpStatus.BAD_REQUEST);

        Usuario usuario = new Usuario(
                nuevoUsuario.getNombre(),
                nuevoUsuario.getNombreUsuario(),
                nuevoUsuario.getEmail(),
                passwordEncoder.encode(nuevoUsuario.getPassword())
        );

        Set<Rol> roles = new HashSet<>();

        // Agregar el rol por defecto si no se especifica ninguno
        if (nuevoUsuario.getRoles().isEmpty()) {
            Optional<Rol> defaultRoleOptional = rolService.getByRolNombre(RolNombre.ROLE_USER);
            defaultRoleOptional.ifPresent(roles::add);
        } else {
            for (String roleName : nuevoUsuario.getRoles()) {
                Optional<Rol> roleOptional = rolService.getByRolNombre(RolNombre.valueOf(roleName.toUpperCase()));
                roleOptional.ifPresent(roles::add);
            }
        }

        usuario.setRoles(roles);
        usuarioService.save(usuario);

        return new ResponseEntity<>(new Mensaje("usuario guardado"), HttpStatus.CREATED);
    }

    @GetMapping("/usuarios/{id}")
    public ResponseEntity<Usuario> obtenerUsuarioPorId(@PathVariable Long id) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(id);
        if (usuarioOptional.isPresent()) {
            return new ResponseEntity<>(usuarioOptional.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }





    @PutMapping("/usuarios/{id}")
    public ResponseEntity<?> actualizarUsuario(@PathVariable("id") Long id, @Valid @RequestBody NuevoUsuario nuevoUsuario) {
        if (!usuarioService.existsById(id)) {
            return new ResponseEntity<>(new Mensaje("No se encontró el usuario con el ID proporcionado"), HttpStatus.NOT_FOUND);
        }

        Usuario usuario = usuarioService.getById(id).get();

        usuario.setNombre(nuevoUsuario.getNombre());
        usuario.setNombreUsuario(nuevoUsuario.getNombreUsuario());
        usuario.setEmail(nuevoUsuario.getEmail());
        usuario.setPassword(passwordEncoder.encode(nuevoUsuario.getPassword()));

        Set<Rol> roles = new HashSet<>();

        if (nuevoUsuario.getRoles() == null || nuevoUsuario.getRoles().isEmpty()) {
            // Si no se proporcionan roles, puedes asignar un rol predeterminado
            Optional<Rol> defaultRoleOptional = rolService.getByRolNombre(RolNombre.ROLE_USER);
            defaultRoleOptional.ifPresent(roles::add);
        } else {
            // Procesar los roles proporcionados
            for (String roleName : nuevoUsuario.getRoles()) {
                Optional<Rol> roleOptional = rolService.getByRolNombre(RolNombre.valueOf(roleName.toUpperCase()));
                roleOptional.ifPresent(roles::add);
            }
        }

        usuario.setRoles(roles);
        usuarioService.save(usuario);

        return new ResponseEntity<>(new Mensaje("Usuario actualizado correctamente"), HttpStatus.OK);
    }





    @GetMapping("/usuarios")
    public ResponseEntity<List<Usuario>> listarUsuarios() {
        List<Usuario> usuarios = usuarioService.list();
        return new ResponseEntity<>(usuarios, HttpStatus.OK);
    }



    @PostMapping("/login")
    public ResponseEntity<JwtDto> login(@Valid @RequestBody LoginUsuario loginUsuario, BindingResult bindingResult){
        if(bindingResult.hasErrors())
            return new ResponseEntity(new Mensaje("campos mal puestos"), HttpStatus.BAD_REQUEST);
        Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginUsuario.getNombreUsuario(), loginUsuario.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateToken(authentication);
        UserDetails userDetails = (UserDetails)authentication.getPrincipal();
        JwtDto jwtDto = new JwtDto(jwt, userDetails.getUsername(), userDetails.getAuthorities());
        return new ResponseEntity(jwtDto, HttpStatus.OK);
    }
}