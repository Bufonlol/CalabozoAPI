package com.calabozoapi.crud.ventas.modelo;

import com.calabozoapi.crud.entity.Producto;

import javax.persistence.*;
import java.util.List;

@Entity
public class Venta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String fecha;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "venta_id", referencedColumnName = "id")
    private List<Producto> productos;

    public Venta() {
    }

    public Venta(String fecha, List<Producto> productos) {
        this.fecha = fecha;
        this.productos = productos;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }
}
