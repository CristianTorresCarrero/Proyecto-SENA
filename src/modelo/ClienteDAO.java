package modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class ClienteDAO {

    private Conexion cn;
    private Connection con;
    private PreparedStatement ps;
    private ResultSet rs;

    public ClienteDAO() {
        cn = new Conexion();
        con = cn.getConnection(); // Establecer la conexión al crear el DAO
    }

    public boolean registrarCliente(Cliente cl) {
        String sql = "INSERT INTO clientes (identificacion, nombre, direccion, telefono, correo) VALUES (?, ?, ?, ?, ?)";
        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, cl.getIdentificacion());
            ps.setString(2, cl.getNombre());
            ps.setString(3, cl.getDireccion());
            ps.setString(4, cl.getTelefono());
            ps.setString(5, cl.getCorreo());

            int resultado = ps.executeUpdate(); // Usar executeUpdate para INSERT, UPDATE, DELETE

            if (resultado > 0) {
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "No se pudo registrar el cliente.");
                return false;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al intentar registrar el cliente: " + e.getMessage());
            return false;
        } finally {
            cerrarRecursos();
        }
    }

    public List<Cliente> listarCliente() {
        List<Cliente> listaCliente = new ArrayList<>();
        String sql = "SELECT * FROM clientes";
        try {
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery(); // Asignar el resultado de la consulta a rs
            while (rs.next()) {
                Cliente cl = new Cliente();
                cl.setId(rs.getInt("id"));
                cl.setIdentificacion(rs.getInt("identificacion"));
                cl.setNombre(rs.getString("nombre"));
                cl.setTelefono(rs.getString("telefono"));
                cl.setDireccion(rs.getString("direccion"));
                cl.setCorreo(rs.getString("correo"));
                listaCliente.add(cl);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar clientes: " + e.toString());
        } finally {
            cerrarRecursos();
        }
        return listaCliente;
    }

    public boolean eliminarCliente(int id) {
        String sql = "DELETE FROM clientes WHERE id = ?";
        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al eliminar cliente " + e.getMessage());
            return false;
        } finally {
            cerrarRecursos();
        }
    }
    
    public boolean modificarCliente(Cliente cl){
        String sql = "UPDATE clientes SET identificacion=?, nombre=?, telefono=?, direccion=?, correo=? WHERE id=? ";
        try {
            ps =con.prepareStatement(sql);
            ps.setInt(1, cl.getIdentificacion());
            ps.setString(2, cl.getNombre());
            ps.setString(3, cl.getTelefono());
            ps.setString(4, cl.getDireccion());
            ps.setString(5, cl.getCorreo());
            ps.setInt(6, cl.getId());
            ps.execute();
            return true;
        } catch (SQLException e) {
            System.out.println(e.toString());
            return false;
        }finally{
            cerrarRecursos();
        }
    }
    
    public List<Cliente> buscarCliente(String criterio) {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM clientes WHERE identificacion LIKE ?";

        try (Connection con = cn.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "%" + criterio + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Cliente cl = new Cliente();
                cl.setId(rs.getInt("id"));
                cl.setIdentificacion(rs.getInt("identificacion"));
                cl.setNombre(rs.getString("nombre"));
                cl.setDireccion(rs.getString("direccion"));
                cl.setTelefono(rs.getString("telefono"));
                cl.setCorreo(rs.getString("correo"));
                clientes.add(cl);
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        return clientes;
    }
    
    public Cliente BuscarClienteVenta(int identificacion){
        Cliente cl = new Cliente();
        String sql = "SELECT * FROM clientes WHERE identificacion = ?";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, identificacion);
            rs = ps.executeQuery();
            if(rs.next()){
                cl.setNombre(rs.getString("nombre"));
                cl.setDireccion(rs.getString("direccion"));
                cl.setTelefono(rs.getString("telefono"));
                cl.setCorreo(rs.getString("correo"));
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        return cl;
    }
    
    private void cerrarRecursos() {
        try {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
            // No cerrar la conexión con "con" aquí para evitar problemas de conexión cerrada
        } catch (SQLException e) {
            System.out.println("Error al cerrar recursos: " + e.getMessage());
        }
    }
    
}