package com.calabozoapi.crud.security.service;

import com.calabozoapi.crud.security.entity.Rol;
import com.calabozoapi.crud.security.enums.RolNombre;
import com.calabozoapi.crud.security.repository.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RolService {

    @Autowired
    RolRepository rolRepository;

    public Optional<Rol> getByRolNombre(RolNombre rolNombre) {
        return rolRepository.findByRolNombre(rolNombre);
    }

    public void save(Rol rol) {
        rolRepository.save(rol);
    }

    public void eliminarRolesDuplicados() {
        List<Rol> roles = rolRepository.findAll();
        for (Rol rol : roles) {
            Optional<Rol> rolOptional = rolRepository.findByRolNombre(rol.getRolNombre());
            if (rolOptional.isPresent()) {
                List<Rol> rolesDuplicados = rolRepository.findDuplicatedRoles(rol.getRolNombre());
                if (rolesDuplicados.size() > 1) {
                    // Mantener el primer rol y eliminar los duplicados
                    for (int i = 1; i < rolesDuplicados.size(); i++) {
                        rolRepository.delete(rolesDuplicados.get(i));
                    }
                }
            }
        }
    }
}
