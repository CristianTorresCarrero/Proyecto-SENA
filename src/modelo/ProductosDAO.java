package modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComboBox;

public class ProductosDAO {

    Connection con;
    Conexion cn = new Conexion();
    PreparedStatement ps;
    ResultSet rs;

    public boolean registrarProducto(Productos pro) {
        String sql = "INSERT INTO productos (codigo, nombre, categoria, descripcion, stock, precio) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, pro.getCodigo());
            ps.setString(2, pro.getNombre());
            ps.setString(3, pro.getCategoria());
            ps.setString(4, pro.getDescripcion());
            ps.setInt(5, pro.getStock());
            ps.setDouble(6, pro.getPrecio());
            ps.execute();
            return true;

        } catch (SQLException e) {
            System.out.println(e.toString());
            return false;
        }
    }

    public void consultarCategoria(JComboBox<String> categoria) {
        categoria.removeAllItems(); // Limpiar el JComboBox antes de llenarlo
        String sql = "SELECT nombre FROM categorias";
        try (Connection con = cn.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String categoriaNombre = rs.getString("nombre");
                categoria.addItem(categoriaNombre);
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
    }

    public List listarProductos() {
        List<Productos> ListaProductos = new ArrayList();
        String sql = "SELECT * FROM productos";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Productos pro = new Productos();
                pro.setId(rs.getInt("id"));
                pro.setCodigo(rs.getString("codigo"));
                pro.setNombre(rs.getString("nombre"));
                pro.setCategoria(rs.getString("categoria"));
                pro.setDescripcion(rs.getString("descripcion"));
                pro.setStock(rs.getInt("stock"));
                pro.setPrecio(rs.getDouble("precio"));
                ListaProductos.add(pro);

            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        return ListaProductos;
    }

    public boolean eliminarProducto(int id) {
        String sql = "DELETE FROM productos WHERE id = ?";
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

    public boolean modificarProducto(Productos pro) {
        String sql = "UPDATE productos SET codigo=?, nombre=?, categoria=?, descripcion=?, stock=?, precio=? WHERE id=?";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, pro.getCodigo());
            ps.setString(2, pro.getNombre());
            ps.setString(3, pro.getCategoria());
            ps.setString(4, pro.getDescripcion());
            ps.setInt(5, pro.getStock());
            ps.setDouble(6, pro.getPrecio());
            ps.setInt(7, pro.getId());
            ps.execute();
            return true;
        } catch (SQLException e) {
            System.out.println(e.toString());
            return false;
        } finally {
            cerrarRecursos();
        }
    }

    public List<Productos> buscarProducto(String criterio) {
        List<Productos> productos = new ArrayList<>();
        String sql = "SELECT * FROM productos WHERE nombre LIKE ?";

        try (Connection con = cn.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "%" + criterio + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Productos pro = new Productos();
                pro.setId(rs.getInt("id"));
                pro.setCodigo(rs.getString("codigo"));
                pro.setNombre(rs.getString("nombre"));
                pro.setStock(rs.getInt("stock"));
                pro.setCategoria(rs.getString("categoria"));
                pro.setDescripcion(rs.getString("descripcion"));
                pro.setPrecio(rs.getDouble("precio"));
                productos.add(pro);
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        return productos;
    }

    public Productos BuscarProVenta(String cod) {
        Productos producto = null; // Cambiado para manejar el caso de no encontrado
        String sql = "SELECT * FROM productos WHERE codigo = ?";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, cod);
            rs = ps.executeQuery();
            if (rs.next()) {
                producto = new Productos(); // Inicializa el objeto solo si se encuentra un producto
                producto.setCodigo(rs.getString("codigo"));
                producto.setNombre(rs.getString("nombre"));
                producto.setDescripcion(rs.getString("descripcion"));
                producto.setPrecio(rs.getDouble("precio"));
                producto.setStock(rs.getInt("stock"));

            } else {
                // Mensaje de depuración si no se encuentra el producto
                System.out.println("Producto no encontrado con el código: " + cod);
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar producto: " + e.toString());
        }
        return producto;
    }

    public Productos BuscarNombreProVenta(String nom) {
        Productos producto = null; // Cambiado para manejar el caso de no encontrado
        String sql = "SELECT * FROM productos WHERE nombre = ?";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, nom);
            rs = ps.executeQuery();
            if (rs.next()) {
                producto = new Productos(); // Inicializa el objeto solo si se encuentra un producto
                producto.setCodigo(rs.getString("codigo"));
                producto.setNombre(rs.getString("nombre")); // Añadido para establecer el nombre
                producto.setDescripcion(rs.getString("descripcion"));
                producto.setPrecio(rs.getDouble("precio"));
                producto.setStock(rs.getInt("stock"));

            } else {
                // Mensaje de depuración si no se encuentra el producto
                System.out.println("Producto no encontrado con el nombre: " + nom);
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar producto: " + e.toString());
        }
        return producto;
    }

    public DatosEmpresa BuscarDatos() {
        DatosEmpresa dato = new DatosEmpresa();
        String sql = "SELECT * FROM config";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                dato.setId(rs.getInt("id"));
                dato.setNit(rs.getString("nit"));
                dato.setNombre(rs.getString("nombre"));
                dato.setTelefono(rs.getString("telefono"));
                dato.setDireccion(rs.getString("direccion"));
                dato.setCorreo(rs.getString("correo"));

            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        return dato;
    }

    public boolean modificarDatos(DatosEmpresa dato) {
        String sql = "UPDATE config SET nombre=?, nit=?, telefono=?, direccion=?, correo=? WHERE id=?";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, dato.getNombre());
            ps.setString(2, dato.getNit());
            ps.setString(3, dato.getTelefono());
            ps.setString(4, dato.getDireccion());
            ps.setString(5, dato.getCorreo());
            ps.setInt(6, dato.getId());
            ps.execute();
            return true;
        } catch (SQLException e) {
            System.out.println(e.toString());
            return false;
        } finally {
            cerrarRecursos();
        }
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
