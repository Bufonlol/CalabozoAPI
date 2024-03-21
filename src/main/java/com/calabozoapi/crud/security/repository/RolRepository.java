package com.calabozoapi.crud.security.repository;

import com.calabozoapi.crud.security.entity.Rol;
import com.calabozoapi.crud.security.enums.RolNombre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RolRepository extends JpaRepository<Rol, Integer> {
    Optional<Rol> findByRolNombre(RolNombre rolNombre);

}
