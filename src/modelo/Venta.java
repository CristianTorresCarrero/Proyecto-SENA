
package modelo;

public class Venta {
    private int id;
    private String nombre;
    private String vendedor;
    private double total;
    private String fecha;
    private int cantidad;

    public Venta() {
    }

    public Venta(int id, String nombre, String vendedor, double total, String fecha, int cantidad) {
        this.id = id;
        this.nombre = nombre;
        this.vendedor = vendedor;
        this.total = total;
        this.fecha = fecha;
        this.cantidad = cantidad;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getVendedor() {
        return vendedor;
    }

    public void setVendedor(String vendedor) {
        this.vendedor = vendedor;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
    
}
