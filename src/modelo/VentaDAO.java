package modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComboBox;

public class VentaDAO {

    Connection conn;
    Conexion cn = new Conexion();
    PreparedStatement ps;
    ResultSet rs;
    int respuesta;

    public int IDVenta() {
        int id = 0;
        String sql = "SELECT MAX(id) FROM ventas";
        try {
            conn = cn.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            if (rs.next()) {
                id = rs.getInt(1);
            }
            System.out.println("ID Venta obtenido: " + id); // Depuración

        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        return id;
    }

    public int registrarVenta(Venta v) {
        String sql = "INSERT INTO ventas (cliente, total, fecha, cantidad) VALUES (?, ?, ?, ?)";
        try {
            conn = cn.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, v.getNombre());
            ps.setDouble(2, v.getTotal());
            ps.setString(3, v.getFecha());
            ps.setInt(4, v.getCantidad());
            ps.execute();
        } catch (SQLException e) {
            System.out.println(e.toString());
        } finally {
            cerrarRecursos();
        }
        return respuesta;
    }
    
    public void consultarProducto(JComboBox<String> producto) {
        producto.removeAllItems(); // Limpiar el JComboBox antes de llenarlo
        String sql = "SELECT nombre FROM productos";
        try (Connection con = cn.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String categoriaNombre = rs.getString("nombre");
                producto.addItem(categoriaNombre);
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
    }

    public int registrarDetalle(Detalle Dv) {
        String sql = "INSERT INTO detallle (cod_pro, cantidad, precio, id_venta) VALUES (?, ?, ?, ?)";
        try {
            conn = cn.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, Dv.getCod_pro());
            ps.setInt(2, Dv.getCantidad());
            ps.setDouble(3, Dv.getPrecio());
            ps.setInt(4, Dv.getId_venta());
            ps.execute();
        } catch (SQLException e) {
            System.out.println(e.toString());
        } finally {
            cerrarRecursos();
        }
        return respuesta;
    }
    
    public boolean actualizarStock(int cantidad, String codigo){
        String sql = "UPDATE productos SET stock = ? WHERE codigo =?";
        try {
            conn= cn.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, cantidad);
            ps.setString(2, codigo);
            ps.execute();
            return true;
        } catch (SQLException e) {
            System.out.println(e.toString());
            return false;
        }
    }
    
    public List listarVentas() {
        List<Venta> ListaVentas = new ArrayList();
        String sql = "SELECT * FROM ventas";
        try {
            conn = cn.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Venta venta = new Venta();
                venta.setId(rs.getInt("id"));
                venta.setNombre(rs.getString("cliente"));
                venta.setTotal(rs.getDouble("total"));
                venta.setFecha(rs.getString("fecha"));
                ListaVentas.add(venta);

            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        return ListaVentas;
    }

    private void cerrarRecursos() {
        try {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            System.out.println("Error al cerrar recursos: " + e.getMessage());
        }
    }
}
