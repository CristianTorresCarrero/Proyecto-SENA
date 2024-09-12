package modelo;

import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsuariosDAO {

    Connection con;
    Conexion cn = new Conexion();
    PreparedStatement ps;
    ResultSet rs;

    public boolean registrarUsuarios(Usuarios reg) {
        String sql = "INSERT INTO usuarios (identificacion, nombre, usuario, rol, direccion, "
                + "telefono, correo, pass) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, reg.getIdentificacion());
            ps.setString(2, reg.getNombre());
            ps.setString(3, reg.getNombre_usuario());
            ps.setString(4, reg.getRol());
            ps.setString(5, reg.getDireccion());
            ps.setString(6, reg.getTelefono());
            ps.setString(7, reg.getCorreo());
            ps.setString(8, reg.getPass());
            ps.execute();
            return true;
        } catch (SQLException e) {
            System.out.println(e.toString());
            return false;
        }
    }

    public List listarUsuarios() {
        List<Usuarios> ListaUsuarios = new ArrayList();
        String sql = "SELECT * FROM usuarios";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Usuarios usu = new Usuarios();
                usu.setId(rs.getInt("id"));
                usu.setIdentificacion(rs.getInt("identificacion"));
                usu.setNombre(rs.getString("nombre"));
                usu.setNombre_usuario(rs.getString("usuario"));
                usu.setRol(rs.getString("rol"));
                usu.setDireccion(rs.getString("direccion"));
                usu.setTelefono(rs.getString("telefono"));
                usu.setCorreo(rs.getString("correo"));
                usu.setPass(rs.getString("pass"));
                ListaUsuarios.add(usu);
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        return ListaUsuarios;
    }

    public boolean eliminarUusario(int id) {
        String sql = "DELETE FROM usuarios WHERE id = ?";
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

    public boolean modificarUsuario(Usuarios usu) {
        String sql = "UPDATE usuarios SET identificacion = ?, nombre=?, usuario=?, rol=?, direccion=?, telefono=?, correo=?, pass=? WHERE id=?";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, usu.getIdentificacion());
            ps.setString(2, usu.getNombre());
            ps.setString(3, usu.getNombre_usuario());
            ps.setString(4, usu.getRol());
            ps.setString(5, usu.getDireccion());
            ps.setString(6, usu.getTelefono());
            ps.setString(7, usu.getCorreo());
            ps.setString(8, usu.getPass());
            ps.setInt(9, usu.getId());
            int resultado = ps.executeUpdate();
            return resultado > 0; // Verifica si se actualizaron filas
        } catch (SQLException e) {
            System.out.println("Error al modificar usuario: " + e.getMessage());
            return false;
        } finally {
            cerrarRecursos();
        }
    }

    public List<Usuarios> buscarUsuario(String criterio) {
        List<Usuarios> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM usuarios WHERE identificacion LIKE ?";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, "%" + criterio + "%");
            rs = ps.executeQuery();
            while (rs.next()) {
                Usuarios us = new Usuarios();
                us.setId(rs.getInt("id"));
                us.setIdentificacion(rs.getInt("identificacion"));
                us.setNombre(rs.getString("nombre"));
                us.setNombre_usuario(rs.getString("usuario"));
                us.setRol(rs.getString("rol"));
                us.setDireccion(rs.getString("direccion"));
                us.setTelefono(rs.getString("telefono"));
                us.setCorreo(rs.getString("correo"));
                us.setPass(rs.getString("pass"));
                usuarios.add(us);
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        return usuarios;
    }

    public Usuarios login(String correo, String pass) {
        String sql = "SELECT * FROM usuarios WHERE usuario = ? AND pass = ?";
        Usuarios usuario = null;
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, correo);
            ps.setString(2, pass);
            rs = ps.executeQuery();
            if (rs.next()) {
                usuario = new Usuarios();
                usuario.setId(rs.getInt("id"));
                usuario.setIdentificacion(rs.getInt("identificacion"));
                usuario.setNombre(rs.getString("nombre"));
                usuario.setNombre_usuario(rs.getString("usuario"));
                usuario.setRol(rs.getString("rol"));
                usuario.setDireccion(rs.getString("direccion"));
                usuario.setTelefono(rs.getString("telefono"));
                usuario.setCorreo(rs.getString("correo"));
                usuario.setPass(rs.getString("pass"));
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        } finally {
            cerrarRecursos();
        }
        return usuario;
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
