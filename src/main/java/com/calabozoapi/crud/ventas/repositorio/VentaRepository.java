package com.calabozoapi.crud.ventas.repositorio;

import com.calabozoapi.crud.ventas.modelo.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Integer> {
}
