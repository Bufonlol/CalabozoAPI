package com.calabozoapi.crud.ventas;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ventas")
public class VentaController {
    @Autowired
    private VentaRepository ventaRepository;

    @GetMapping("/todas")
    public List<Venta> todasLasVentas() {
        return ventaRepository.findAll();
    }

    @PostMapping("/nueva")
    public Venta nuevaVenta(@RequestBody Venta venta) {
        return ventaRepository.save(venta);
    }
}
