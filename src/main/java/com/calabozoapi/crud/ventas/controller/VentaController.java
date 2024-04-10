package com.calabozoapi.crud.ventas.controller;

import com.calabozoapi.crud.ventas.modelo.Venta;
import com.calabozoapi.crud.ventas.repositorio.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ventas")
public class VentaController {

    @Autowired
    private VentaRepository ventaRepository;

    // Endpoint para crear una nueva venta
    @PostMapping("/")
    public Venta createVenta(@RequestBody Venta venta) {
        return ventaRepository.save(venta);
    }

    // Endpoint para obtener todas las ventas
    @GetMapping("/")
    public List<Venta> getAllVentas() {
        return ventaRepository.findAll();
    }

    // Endpoint para obtener una venta por su ID
    @GetMapping("/{id}")
    public Venta getVentaById(@PathVariable int id) {
        return ventaRepository.findById(id).orElse(null);
    }

    // Endpoint para actualizar una venta existente
    @PutMapping("/{id}")
    public Venta updateVenta(@PathVariable int id, @RequestBody Venta ventaDetails) {
        Venta venta = ventaRepository.findById(id).orElse(null);
        if (venta != null) {
            venta.setFecha(ventaDetails.getFecha());
            venta.setProductos(ventaDetails.getProductos());
            return ventaRepository.save(venta);
        }
        return null;
    }

    // Endpoint para eliminar una venta por su ID
    @DeleteMapping("/{id}")
    public void deleteVenta(@PathVariable int id) {
        ventaRepository.deleteById(id);
    }
}
