package modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoriasDAO {

    Connection con;
    Conexion cn = new Conexion();
    PreparedStatement ps;
    ResultSet rs;

    public boolean registrarCategoria(Categorias cat) {
        String sql = "INSERT INTO categorias(nombre) VALUES (?)";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, cat.getNombre());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println(e.toString());
            return false;
        }
    }

    public List<Categorias> listarCategorias() {
        List<Categorias> ListaCategoria = new ArrayList<>();
        String sql = "SELECT * FROM categorias";
        try (Connection con = cn.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Categorias ca = new Categorias();
                ca.setId(rs.getInt("id"));
                ca.setNombre(rs.getString("nombre"));
                ListaCategoria.add(ca);
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        return ListaCategoria;
    }

    public boolean eliminarCategoria(int id) {
        String sql = "DELETE FROM categorias WHERE id = ?";
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
    
    public boolean modificarCategoria(Categorias cat) {
        String sql = "UPDATE categorias SET nombre=? WHERE id=?";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, cat.getNombre());
            ps.setInt(2, cat.getId());
            ps.execute();
            return true;
        } catch (SQLException e) {
            System.out.println(e.toString());
            return false;
        } finally {
            cerrarRecursos();
        }
    }
    
    public List<Categorias> buscarCategorias(String criterio) {
        List<Categorias> categorias = new ArrayList<>();
        String sql = "SELECT * FROM categorias WHERE nombre LIKE ?";

        try (Connection con = cn.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "%" + criterio + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Categorias cat = new Categorias();
                cat.setId(rs.getInt("id"));
                cat.setNombre(rs.getString("nombre"));
                categorias.add(cat);
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        return categorias;
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
