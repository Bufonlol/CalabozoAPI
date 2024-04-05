package com.calabozoapi.crud.security.service;

import com.calabozoapi.crud.security.entity.Usuario;
import com.calabozoapi.crud.security.entity.UsuarioPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UsuarioService usuarioService;

    @Override
    public UserDetails loadUserByUsername(String nombreUsuario) throws UsernameNotFoundException {
        Optional<Usuario> usuarioOptional = usuarioService.getByNombreUsuario(nombreUsuario);
        Usuario usuario = usuarioOptional.orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con nombre de usuario: " + nombreUsuario));
        return UsuarioPrincipal.build(usuario);
    }
}
