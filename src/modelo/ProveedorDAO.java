package modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProveedorDAO {

    Connection con;
    Conexion cn = new Conexion();
    PreparedStatement ps;
    ResultSet rs;

    public boolean registrarProveedor(Proveedor pr) {
        String sql = "INSERT INTO proveedor(identificacion, nombre, direccion, telefono, correo, descripcion) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            con = cn.getConnection();
            if (con == null) {
                System.out.println("Error: Conexión a la base de datos es nula");
                return false;
            }
            ps = con.prepareStatement(sql);
            ps.setInt(1, pr.getIdentificacion());
            ps.setString(2, pr.getNombre());
            ps.setString(3, pr.getDireccion());
            ps.setString(4, pr.getTelefono());
            ps.setString(5, pr.getCorreo());
            ps.setString(6, pr.getDescripcion());

            System.out.println("Ejecutando consulta: " + ps);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Proveedor registrado con éxito");
                return true;
            } else {
                System.out.println("No se insertó ninguna fila");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Error SQL: " + e.getMessage());
            return false;
        } finally {
            cerrarRecursos();
        }
    }

    public List listarProveedore() {
        List<Proveedor> ListaProveedor = new ArrayList();
        String sql = "SELECT * FROM proveedor";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Proveedor pr = new Proveedor();
                pr.setId(rs.getInt("id"));
                pr.setIdentificacion(rs.getInt("identificacion"));
                pr.setNombre(rs.getString("nombre"));
                pr.setDireccion(rs.getString("direccion"));
                pr.setTelefono(rs.getString("telefono"));
                pr.setCorreo(rs.getString("correo"));
                pr.setDescripcion(rs.getString("descripcion"));
                ListaProveedor.add(pr);
            }

        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        return ListaProveedor;
    }

    public boolean eliminarProveedor(int id) {
        String sql = "DELETE FROM proveedor WHERE id = ?";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ps.execute();
            return true;
        } catch (SQLException e) {
            System.out.println(e.toString());
            return false;
        } finally {
            cerrarRecursos();
        }
    }

    public boolean modificarProveedor(Proveedor pr) {
        String sql = "UPDATE proveedor SET identificacion=?, nombre=?, telefono=?, correo=?, direccion=?, descripcion=? WHERE id=?";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, pr.getIdentificacion());
            ps.setString(2, pr.getNombre());
            ps.setString(3, pr.getTelefono());
            ps.setString(4, pr.getDireccion());
            ps.setString(5, pr.getCorreo());
            ps.setString(6, pr.getDescripcion());
            ps.setInt(7, pr.getId());
            ps.execute();
            return true;
        } catch (SQLException e) {
            System.out.println(e.toString());
            return false;
        } finally {
            cerrarRecursos();
        }
    }

    public List<Proveedor> buscarProveedor(String criterio) {
        List<Proveedor> proveedores = new ArrayList<>();
        String sql = "SELECT * FROM proveedor WHERE identificacion LIKE ?";

        try (Connection con = cn.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "%" + criterio + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Proveedor pr = new Proveedor();
                pr.setId(rs.getInt("id"));
                pr.setIdentificacion(rs.getInt("identificacion"));
                pr.setNombre(rs.getString("nombre"));
                pr.setDireccion(rs.getString("direccion"));
                pr.setTelefono(rs.getString("telefono"));
                pr.setCorreo(rs.getString("correo"));
                pr.setDescripcion(rs.getString("descripcion"));
                proveedores.add(pr);
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        return proveedores;
    }

    private void cerrarRecursos() {
        try {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
            if (con != null) {
                con.close();
            }
        } catch (SQLException e) {
            System.out.println("Error al cerrar recursos: " + e.getMessage());
        }
    }
}
