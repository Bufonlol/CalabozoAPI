package com.calabozoapi.crud.security.repository;

import com.calabozoapi.crud.security.entity.Rol;
import com.calabozoapi.crud.security.enums.RolNombre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RolRepository extends JpaRepository<Rol, Integer> {
    Optional<Rol> findByRolNombre(RolNombre rolNombre);
    @Query("SELECT r FROM Rol r WHERE r.rolNombre = ?1 GROUP BY r.rolNombre HAVING COUNT(r) > 1")
    List<Rol> findDuplicatedRoles(RolNombre rolNombre);
}
