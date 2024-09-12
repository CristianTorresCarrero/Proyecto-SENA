package vista;

import Reportes.Excel;
import Reportes.Grafico;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JDesktopPane;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import java.awt.Desktop;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import modelo.*;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

/**
 *
 * @author USUARIO
 */
public class Sistema extends javax.swing.JFrame {

    public static JDesktopPane jDesktopPane_menu;

    Date fechaVenta = new Date();
    String fechaActual = new SimpleDateFormat("dd-MM-yyyy").format(fechaVenta);

    Cliente cl = new Cliente();
    ClienteDAO cliente = new ClienteDAO();

    Proveedor pr = new Proveedor();
    ProveedorDAO proveedor = new ProveedorDAO();

    Productos pro = new Productos();
    ProductosDAO prodao = new ProductosDAO();

    Categorias cat = new Categorias();
    CategoriasDAO catdao = new CategoriasDAO();

    Venta v = new Venta();
    VentaDAO VDAO = new VentaDAO();
    Detalle Dv = new Detalle();

    DatosEmpresa dato = new DatosEmpresa();

    Usuarios usu = new Usuarios();
    UsuariosDAO usuDAO = new UsuariosDAO();

    Eventos event = new Eventos();

    private Usuarios usuarioActual;

    DefaultTableModel modelo = new DefaultTableModel();
    DefaultTableModel tap = new DefaultTableModel();

    int item;
    double TotalPagar = 0.00;

    public Sistema(Usuarios usuario) {
        initComponents();
        setSize(1230, 720);
        setResizable(false);
        setTitle("Panel de Administracion");
        setLocationRelativeTo(null);

        txt_IdentificacionProveedor.setEnabled(true);
        txt_IdentificacionProveedor.setEditable(true);
        txt_ClienteVenta.setEditable(true);
        txt_IDProveedor.setVisible(false);
        txt_VentaId.setVisible(false);
        txt_IdEmpresa.setVisible(false);
        txt_IdUsuario.setVisible(false);
        txt_VentaNombreProducto.setVisible(false);
        this.usuarioActual = null;
        VDAO.consultarProducto(cmb_NombreProductoVenta);

        actualizarInformacionUsuario();

        prodao.consultarCategoria(cmb_CategoriaProducto);
        AutoCompleteDecorator.decorate(cmb_CategoriaProducto);

        this.setLayout(null);
        jDesktopPane_menu = new JDesktopPane();

        int ancho = java.awt.Toolkit.getDefaultToolkit().getScreenSize().width;
        int alto = java.awt.Toolkit.getDefaultToolkit().getScreenSize().height;
        this.jDesktopPane_menu.setBounds(0, 0, ancho, (alto - 110));
        this.add(jDesktopPane_menu);
        this.usuarioActual = usuario;
        actualizarInformacionUsuario();
        if (usuario.getRol().equals("Empleado")) {
            btn_Proveedor.setEnabled(false);
            btn_DatosEmpresa.setEnabled(false);
            btn_AdministrarPerfiles.setEnabled(false);
            btn_Clientes.setEnabled(false);
            btn_Reportes.setEnabled(false);
        }
        txt_IDProveedor.setVisible(false);
        txt_VentaId.setVisible(false);
        txt_IdEmpresa.setVisible(false);
        txt_IdUsuario.setVisible(false);
    }

    public Sistema() {
        initComponents();
    }

    private void actualizarInformacionUsuario() {
        if (usuarioActual != null) {
            label_nombrePerfil.setText(usuarioActual.getNombre());
            label_Rol.setText("Rol: " + usuarioActual.getRol());
        } else {
            label_nombrePerfil.setText("Nombre: Desconocido");
            label_Rol.setText("Rol: Desconocido");
        }
    }

    public void ListarCliente() {
        List<Cliente> listarCl = cliente.listarCliente();
        modelo = (DefaultTableModel) TablaClientes.getModel();
        Object[] ob = new Object[6];
        for (int i = 0; i < listarCl.size(); i++) {
            ob[0] = listarCl.get(i).getId();
            ob[1] = listarCl.get(i).getIdentificacion();
            ob[2] = listarCl.get(i).getNombre();
            ob[3] = listarCl.get(i).getTelefono();
            ob[4] = listarCl.get(i).getDireccion();
            ob[5] = listarCl.get(i).getCorreo();
            modelo.addRow(ob);
        }
        TablaClientes.setModel(modelo);
    }

    public void ListarProveedor() {
        List<Proveedor> listarPr = proveedor.listarProveedore();
        modelo = (DefaultTableModel) TablaProveedor.getModel();
        Object[] ob = new Object[7];
        for (int i = 0; i < listarPr.size(); i++) {
            ob[0] = listarPr.get(i).getId();
            ob[1] = listarPr.get(i).getIdentificacion();
            ob[2] = listarPr.get(i).getNombre();
            ob[3] = listarPr.get(i).getTelefono();
            ob[4] = listarPr.get(i).getCorreo();
            ob[5] = listarPr.get(i).getDireccion();
            ob[6] = listarPr.get(i).getDescripcion();
            modelo.addRow(ob);
        }
        TablaProveedor.setModel(modelo);
    }

    public void ListarCategoria() {
        List<Categorias> listarCa = catdao.listarCategorias();
        modelo = (DefaultTableModel) TablaCategoria.getModel();
        Object[] ob = new Object[2];
        for (int i = 0; i < listarCa.size(); i++) {
            ob[0] = listarCa.get(i).getId();
            ob[1] = listarCa.get(i).getNombre();
            modelo.addRow(ob);
        }
        TablaCategoria.setModel(modelo);
    }

    public void ListarProductos() {
        List<Productos> listarPro = prodao.listarProductos();
        modelo = (DefaultTableModel) Tabla_Producto.getModel();
        Object[] ob = new Object[7];
        for (int i = 0; i < listarPro.size(); i++) {
            ob[0] = listarPro.get(i).getId();
            ob[1] = listarPro.get(i).getCodigo();
            ob[2] = listarPro.get(i).getNombre();
            ob[3] = listarPro.get(i).getStock();
            ob[4] = listarPro.get(i).getCategoria();
            ob[5] = listarPro.get(i).getDescripcion();
            ob[6] = listarPro.get(i).getPrecio();
            modelo.addRow(ob);
        }
        Tabla_Producto.setModel(modelo);
    }

    public void ListarEmpresa() {
        dato = prodao.BuscarDatos();
        txt_IdEmpresa.setText("" + dato.getId());
        txt_NombreEmpresa.setText("" + dato.getNombre());
        txt_NitEmpresa.setText("" + dato.getNit());
        txt_TelefonoEmpresa.setText("" + dato.getTelefono());
        txt_DireccionEmpresa.setText("" + dato.getDireccion());
        txt_CorreoEmpresa.setText("" + dato.getCorreo());
    }

    public void ListarVentas() {
        List<Venta> listarVen = VDAO.listarVentas();
        modelo = (DefaultTableModel) TablaReportes.getModel();
        Object[] ob = new Object[4];
        for (int i = 0; i < listarVen.size(); i++) {
            ob[0] = listarVen.get(i).getId();
            ob[1] = listarVen.get(i).getNombre();
            ob[2] = listarVen.get(i).getTotal();
            ob[3] = listarVen.get(i).getFecha();
            modelo.addRow(ob);
        }
        TablaReportes.setModel(modelo);
    }

    public void ListarUsuarios() {
        List<Usuarios> listarUsu = usuDAO.listarUsuarios();
        modelo = (DefaultTableModel) TablaUsuarios.getModel();
        Object[] ob = new Object[9];
        for (int i = 0; i < listarUsu.size(); i++) {
            ob[0] = listarUsu.get(i).getId();
            ob[1] = listarUsu.get(i).getIdentificacion();
            ob[2] = listarUsu.get(i).getNombre();
            ob[3] = listarUsu.get(i).getNombre_usuario();
            ob[4] = listarUsu.get(i).getDireccion();
            ob[5] = listarUsu.get(i).getTelefono();
            ob[6] = listarUsu.get(i).getCorreo();
            ob[7] = listarUsu.get(i).getRol();
            ob[8] = listarUsu.get(i).getPass();
            modelo.addRow(ob);

        }
        TablaUsuarios.setModel(modelo);
    }

    public void limpiarTabla() {
        for (int i = 0; i < modelo.getRowCount(); i++) {
            modelo.removeRow(i);
            i = i - 1;
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanelCerrarSesion = new javax.swing.JPanel();
        btn_Salir = new javax.swing.JButton();
        btn_NuevaVenta = new javax.swing.JButton();
        btn_Productos = new javax.swing.JButton();
        btn_Clientes = new javax.swing.JButton();
        btn_Proveedor = new javax.swing.JButton();
        btn_Categorias = new javax.swing.JButton();
        btn_DatosEmpresa = new javax.swing.JButton();
        btn_Reportes = new javax.swing.JButton();
        btn_AdministrarPerfiles = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        btn_Foto = new javax.swing.JButton();
        label_nombrePerfil = new javax.swing.JLabel();
        label_Rol = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        TablaVenta = new javax.swing.JTable();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        txt_VentaStockProducto = new javax.swing.JTextField();
        txt_VentaDescripcionProducto = new javax.swing.JTextField();
        txt_VentaCodigoProducto = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        txt_VentaPrecioProducto = new javax.swing.JTextField();
        txt_ClienteVenta = new javax.swing.JTextField();
        txt_VentaCantidadProducto = new javax.swing.JTextField();
        btn_AgregarCompraProducto = new javax.swing.JButton();
        btn_CompraProducto = new javax.swing.JButton();
        btn_EliminarCompraProducto = new javax.swing.JButton();
        btn_CancelarCompraProducto = new javax.swing.JButton();
        jLabel15 = new javax.swing.JLabel();
        txt_VentaNombreProducto = new javax.swing.JTextField();
        LabelTotal = new javax.swing.JLabel();
        jLabel80 = new javax.swing.JLabel();
        txt_NombreClienteVenta = new javax.swing.JTextField();
        txt_DirreccionClienteVenta = new javax.swing.JTextField();
        txt_TelefonoClienteVenta = new javax.swing.JTextField();
        jLabel81 = new javax.swing.JLabel();
        jLabel71 = new javax.swing.JLabel();
        txt_VentaId = new javax.swing.JTextField();
        cmb_NombreProductoVenta = new javax.swing.JComboBox<>();
        jLabel53 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        txt_BuscarProducto = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        Tabla_Producto = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txt_IdProducto = new javax.swing.JTextField();
        txt_NombreProducto = new javax.swing.JTextField();
        txt_ProductoCodigo = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txt_DescripcionProducto = new javax.swing.JTextField();
        btn_RegistrarProducto = new javax.swing.JButton();
        btn_ActualizarProducto = new javax.swing.JButton();
        btn_EliminarProducto = new javax.swing.JButton();
        btn_CancelarProducto = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        txt_Cantidades = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        txt_PrecioVenta = new javax.swing.JTextField();
        cmb_CategoriaProducto = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        jLabel52 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel32 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        TablaClientes = new javax.swing.JTable();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        txt_BuscarCliente = new javax.swing.JTextField();
        txt_IDCliente = new javax.swing.JTextField();
        txt_NombreCliente = new javax.swing.JTextField();
        txt_DireccionCliente = new javax.swing.JTextField();
        txt_TelefonoCliente = new javax.swing.JTextField();
        txt_CorreoCliente = new javax.swing.JTextField();
        btn_RegistrarCliente = new javax.swing.JButton();
        btn_ModificarCliente = new javax.swing.JButton();
        btn_EliminarCliente = new javax.swing.JButton();
        btn_CancelarCliente = new javax.swing.JButton();
        jLabel79 = new javax.swing.JLabel();
        txt_IdentificacionCliente = new javax.swing.JTextField();
        jLabel54 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jPanel16 = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        TablaProveedor = new javax.swing.JTable();
        btn_RegistrarProveedor = new javax.swing.JButton();
        btn_ModificarProveedor = new javax.swing.JButton();
        btn_EliminarProveedor = new javax.swing.JButton();
        btn_CancelarProveedor = new javax.swing.JButton();
        jLabel26 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel61 = new javax.swing.JLabel();
        jLabel62 = new javax.swing.JLabel();
        jLabel63 = new javax.swing.JLabel();
        jLabel64 = new javax.swing.JLabel();
        jLabel65 = new javax.swing.JLabel();
        jLabel66 = new javax.swing.JLabel();
        jLabel67 = new javax.swing.JLabel();
        jLabel68 = new javax.swing.JLabel();
        jLabel69 = new javax.swing.JLabel();
        jLabel70 = new javax.swing.JLabel();
        jLabel72 = new javax.swing.JLabel();
        txt_IdentificacionProveedor = new javax.swing.JTextField();
        jLabel73 = new javax.swing.JLabel();
        txt_NombreProveedor = new javax.swing.JTextField();
        jLabel74 = new javax.swing.JLabel();
        txt_BuscarProveedor = new javax.swing.JTextField();
        jLabel75 = new javax.swing.JLabel();
        txt_TelefonoProveedor = new javax.swing.JTextField();
        jLabel76 = new javax.swing.JLabel();
        txt_CorreoProveedor = new javax.swing.JTextField();
        jLabel77 = new javax.swing.JLabel();
        txt_DireccionProveedor = new javax.swing.JTextField();
        jLabel78 = new javax.swing.JLabel();
        txt_DescripcionProveedor = new javax.swing.JTextField();
        txt_IDProveedor = new javax.swing.JTextField();
        jLabel56 = new javax.swing.JLabel();
        panelTapar = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        jLabel24 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        txt_BuscarCategoria = new javax.swing.JTextField();
        jScrollPane4 = new javax.swing.JScrollPane();
        TablaCategoria = new javax.swing.JTable();
        jLabel25 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        txt_IdCategoria = new javax.swing.JTextField();
        txt_NombreCategoria = new javax.swing.JTextField();
        btn_RegistrarCategoria = new javax.swing.JButton();
        btn_ModificarCategoria = new javax.swing.JButton();
        btn_EliminarCategoria = new javax.swing.JButton();
        btn_CancelarCategoria = new javax.swing.JButton();
        jLabel60 = new javax.swing.JLabel();
        jLabel55 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jLabel35 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        TablaReportes = new javax.swing.JTable();
        btn_ReportePdf = new javax.swing.JButton();
        txt_IdReportes = new javax.swing.JTextField();
        jLabel42 = new javax.swing.JLabel();
        btn_ReporteTorta = new javax.swing.JButton();
        jLabel85 = new javax.swing.JLabel();
        jLabel84 = new javax.swing.JLabel();
        MiDate = new com.toedter.calendar.JDateChooser();
        jLabel57 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jLabel36 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        txt_NitEmpresa = new javax.swing.JTextField();
        txt_NombreEmpresa = new javax.swing.JTextField();
        txt_DireccionEmpresa = new javax.swing.JTextField();
        txt_TelefonoEmpresa = new javax.swing.JTextField();
        txt_CorreoEmpresa = new javax.swing.JTextField();
        btn_ModificarEmpresa = new javax.swing.JButton();
        jLabel41 = new javax.swing.JLabel();
        txt_IdEmpresa = new javax.swing.JTextField();
        jLabel58 = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        jLabel51 = new javax.swing.JLabel();
        txt_BuscarUsuario = new javax.swing.JTextField();
        jScrollPane6 = new javax.swing.JScrollPane();
        TablaUsuarios = new javax.swing.JTable();
        jLabel43 = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        jLabel46 = new javax.swing.JLabel();
        jLabel47 = new javax.swing.JLabel();
        jLabel48 = new javax.swing.JLabel();
        jLabel49 = new javax.swing.JLabel();
        jLabel50 = new javax.swing.JLabel();
        txt_IdentificacionUsuario = new javax.swing.JTextField();
        txt_nombreCompletoUsuario = new javax.swing.JTextField();
        txt_nombreUsuario = new javax.swing.JTextField();
        txt_direccionUsuario = new javax.swing.JTextField();
        cmb_rol = new javax.swing.JComboBox<>();
        txt_telefonoUsuario = new javax.swing.JTextField();
        txt_correoUsuario = new javax.swing.JTextField();
        btn_RegistrarUsuario = new javax.swing.JButton();
        btn_modificarUsuario = new javax.swing.JButton();
        btn_eliminarUsuario = new javax.swing.JButton();
        btn_cancelarUsuario = new javax.swing.JButton();
        txt_contraseñaUsuario = new javax.swing.JPasswordField();
        txt_IdUsuario = new javax.swing.JTextField();
        jLabel59 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(0, 0, 153));
        setIconImage(getIconImage());
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(129, 1, 137));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanelCerrarSesion.setBackground(new java.awt.Color(129, 1, 137));

        btn_Salir.setBackground(new java.awt.Color(129, 1, 137));
        btn_Salir.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btn_Salir.setForeground(new java.awt.Color(255, 255, 255));
        btn_Salir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/logout_11618657.png"))); // NOI18N
        btn_Salir.setText("     Cerrar Sesión");
        btn_Salir.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btn_Salir.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_Salir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_SalirActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelCerrarSesionLayout = new javax.swing.GroupLayout(jPanelCerrarSesion);
        jPanelCerrarSesion.setLayout(jPanelCerrarSesionLayout);
        jPanelCerrarSesionLayout.setHorizontalGroup(
            jPanelCerrarSesionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelCerrarSesionLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btn_Salir, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanelCerrarSesionLayout.setVerticalGroup(
            jPanelCerrarSesionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelCerrarSesionLayout.createSequentialGroup()
                .addComponent(btn_Salir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel1.add(jPanelCerrarSesion, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 490, 200, 60));

        btn_NuevaVenta.setBackground(new java.awt.Color(129, 1, 137));
        btn_NuevaVenta.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btn_NuevaVenta.setForeground(new java.awt.Color(255, 255, 255));
        btn_NuevaVenta.setText("      Ventas");
        btn_NuevaVenta.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btn_NuevaVenta.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_NuevaVenta.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btn_NuevaVenta.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_NuevaVentaMouseClicked(evt);
            }
        });
        btn_NuevaVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_NuevaVentaActionPerformed(evt);
            }
        });
        jPanel1.add(btn_NuevaVenta, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 200, 50));

        btn_Productos.setBackground(new java.awt.Color(129, 1, 137));
        btn_Productos.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btn_Productos.setForeground(new java.awt.Color(255, 255, 255));
        btn_Productos.setText("      Productos");
        btn_Productos.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btn_Productos.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_Productos.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btn_Productos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_ProductosActionPerformed(evt);
            }
        });
        jPanel1.add(btn_Productos, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 60, 200, 50));

        btn_Clientes.setBackground(new java.awt.Color(129, 1, 137));
        btn_Clientes.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btn_Clientes.setForeground(new java.awt.Color(255, 255, 255));
        btn_Clientes.setText("      Cliente");
        btn_Clientes.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btn_Clientes.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_Clientes.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btn_Clientes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_ClientesActionPerformed(evt);
            }
        });
        jPanel1.add(btn_Clientes, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 120, 200, 50));

        btn_Proveedor.setBackground(new java.awt.Color(129, 1, 137));
        btn_Proveedor.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btn_Proveedor.setForeground(new java.awt.Color(255, 255, 255));
        btn_Proveedor.setText("      Proveedor");
        btn_Proveedor.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btn_Proveedor.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_Proveedor.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btn_Proveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_ProveedorActionPerformed(evt);
            }
        });
        jPanel1.add(btn_Proveedor, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 180, 200, 50));

        btn_Categorias.setBackground(new java.awt.Color(129, 1, 137));
        btn_Categorias.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btn_Categorias.setForeground(new java.awt.Color(255, 255, 255));
        btn_Categorias.setText("      Categorias");
        btn_Categorias.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btn_Categorias.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_Categorias.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btn_Categorias.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_CategoriasActionPerformed(evt);
            }
        });
        jPanel1.add(btn_Categorias, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 240, 200, 50));

        btn_DatosEmpresa.setBackground(new java.awt.Color(129, 1, 137));
        btn_DatosEmpresa.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btn_DatosEmpresa.setForeground(new java.awt.Color(255, 255, 255));
        btn_DatosEmpresa.setText("      Datos Empresa");
        btn_DatosEmpresa.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btn_DatosEmpresa.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_DatosEmpresa.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btn_DatosEmpresa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_DatosEmpresaActionPerformed(evt);
            }
        });
        jPanel1.add(btn_DatosEmpresa, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 360, 200, 50));

        btn_Reportes.setBackground(new java.awt.Color(129, 1, 137));
        btn_Reportes.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btn_Reportes.setForeground(new java.awt.Color(255, 255, 255));
        btn_Reportes.setText("      Reportes");
        btn_Reportes.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btn_Reportes.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_Reportes.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btn_Reportes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_ReportesActionPerformed(evt);
            }
        });
        jPanel1.add(btn_Reportes, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 300, 200, 50));

        btn_AdministrarPerfiles.setBackground(new java.awt.Color(129, 1, 137));
        btn_AdministrarPerfiles.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btn_AdministrarPerfiles.setForeground(new java.awt.Color(255, 255, 255));
        btn_AdministrarPerfiles.setText("      Administrar Perfiles");
        btn_AdministrarPerfiles.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btn_AdministrarPerfiles.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_AdministrarPerfiles.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btn_AdministrarPerfiles.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_AdministrarPerfilesActionPerformed(evt);
            }
        });
        jPanel1.add(btn_AdministrarPerfiles, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 420, 200, 50));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 100, 200, 580));

        jPanel2.setBackground(new java.awt.Color(129, 1, 137));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setBackground(new java.awt.Color(129, 1, 137));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Ellipse 1_1.png"))); // NOI18N
        jPanel2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 200, 100));

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 200, 100));

        jPanel3.setBackground(new java.awt.Color(129, 1, 137));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 48)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(204, 204, 204));
        jLabel2.setText("HELADERIA BUHOMAR");
        jPanel3.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 640, 80));

        btn_Foto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/man_11473142.png"))); // NOI18N
        btn_Foto.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btn_Foto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_FotoActionPerformed(evt);
            }
        });
        jPanel3.add(btn_Foto, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 20, 80, 70));

        label_nombrePerfil.setBackground(new java.awt.Color(255, 255, 255));
        label_nombrePerfil.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        label_nombrePerfil.setForeground(new java.awt.Color(255, 255, 255));
        jPanel3.add(label_nombrePerfil, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 30, 230, 20));

        label_Rol.setBackground(new java.awt.Color(255, 255, 255));
        label_Rol.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        label_Rol.setForeground(new java.awt.Color(255, 255, 255));
        jPanel3.add(label_Rol, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 60, 230, 20));

        getContentPane().add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 0, 1010, 100));

        jTabbedPane1.setBackground(new java.awt.Color(153, 102, 255));
        jTabbedPane1.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED, new java.awt.Color(0, 0, 153), new java.awt.Color(0, 153, 204)));

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        TablaVenta.setBackground(new java.awt.Color(204, 204, 255));
        TablaVenta.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Codigo", "Nombre", "Descripcion", "Cantidad", "Precio Unitario", "SubTotal"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        TablaVenta.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TablaVentaMouseClicked(evt);
            }
        });
        TablaVenta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TablaVentaKeyPressed(evt);
            }
        });
        jScrollPane2.setViewportView(TablaVenta);

        jPanel5.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 220, 920, 200));

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("Stock Disponible:");
        jPanel5.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 80, -1, -1));

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Nombre Producto:");
        jPanel5.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 130, -1, -1));

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("Codigo:");
        jPanel5.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 30, -1, -1));

        txt_VentaStockProducto.setEditable(false);
        txt_VentaStockProducto.setBackground(new java.awt.Color(204, 204, 255));
        txt_VentaStockProducto.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txt_VentaStockProducto.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txt_VentaStockProducto.setEnabled(false);
        txt_VentaStockProducto.setSelectedTextColor(new java.awt.Color(0, 0, 0));
        txt_VentaStockProducto.setSelectionColor(new java.awt.Color(0, 0, 0));
        txt_VentaStockProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_VentaStockProductoActionPerformed(evt);
            }
        });
        jPanel5.add(txt_VentaStockProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 80, 165, 30));

        txt_VentaDescripcionProducto.setEditable(false);
        txt_VentaDescripcionProducto.setBackground(new java.awt.Color(204, 204, 255));
        txt_VentaDescripcionProducto.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txt_VentaDescripcionProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_VentaDescripcionProductoActionPerformed(evt);
            }
        });
        jPanel5.add(txt_VentaDescripcionProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 130, 165, 30));

        txt_VentaCodigoProducto.setBackground(new java.awt.Color(204, 204, 255));
        txt_VentaCodigoProducto.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txt_VentaCodigoProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_VentaCodigoProductoActionPerformed(evt);
            }
        });
        txt_VentaCodigoProducto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_VentaCodigoProductoKeyPressed(evt);
            }
        });
        jPanel5.add(txt_VentaCodigoProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 30, 165, 30));

        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setText("Precio Venta:");
        jPanel5.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 80, -1, -1));

        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setText("Telefono Cliente:");
        jPanel5.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 500, -1, -1));

        jLabel18.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(255, 255, 255));
        jLabel18.setText("Cantidad:");
        jPanel5.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 30, -1, -1));

        jLabel19.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 255, 255));
        jLabel19.setText("Total a Pagar:");
        jPanel5.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 440, -1, 20));

        txt_VentaPrecioProducto.setBackground(new java.awt.Color(204, 204, 255));
        txt_VentaPrecioProducto.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txt_VentaPrecioProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_VentaPrecioProductoActionPerformed(evt);
            }
        });
        jPanel5.add(txt_VentaPrecioProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 80, 165, 30));

        txt_ClienteVenta.setBackground(new java.awt.Color(204, 204, 255));
        txt_ClienteVenta.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txt_ClienteVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_ClienteVentaActionPerformed(evt);
            }
        });
        txt_ClienteVenta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_ClienteVentaKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_ClienteVentaKeyTyped(evt);
            }
        });
        jPanel5.add(txt_ClienteVenta, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 440, 165, 30));

        txt_VentaCantidadProducto.setBackground(new java.awt.Color(204, 204, 255));
        txt_VentaCantidadProducto.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txt_VentaCantidadProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_VentaCantidadProductoActionPerformed(evt);
            }
        });
        txt_VentaCantidadProducto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_VentaCantidadProductoKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_VentaCantidadProductoKeyTyped(evt);
            }
        });
        jPanel5.add(txt_VentaCantidadProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 30, 165, 30));

        btn_AgregarCompraProducto.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btn_AgregarCompraProducto.setText("Agregar");
        btn_AgregarCompraProducto.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_AgregarCompraProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_AgregarCompraProductoActionPerformed(evt);
            }
        });
        jPanel5.add(btn_AgregarCompraProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 10, 120, 30));

        btn_CompraProducto.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btn_CompraProducto.setText("Vender");
        btn_CompraProducto.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_CompraProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_CompraProductoActionPerformed(evt);
            }
        });
        jPanel5.add(btn_CompraProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 60, 120, 30));

        btn_EliminarCompraProducto.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btn_EliminarCompraProducto.setText("Eliminar");
        btn_EliminarCompraProducto.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_EliminarCompraProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_EliminarCompraProductoActionPerformed(evt);
            }
        });
        jPanel5.add(btn_EliminarCompraProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 110, 120, 30));

        btn_CancelarCompraProducto.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btn_CancelarCompraProducto.setText("Cancelar");
        btn_CancelarCompraProducto.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_CancelarCompraProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_CancelarCompraProductoActionPerformed(evt);
            }
        });
        jPanel5.add(btn_CancelarCompraProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 160, 120, 30));

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("Descripción:");
        jPanel5.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 130, -1, 20));

        txt_VentaNombreProducto.setEditable(false);
        txt_VentaNombreProducto.setBackground(new java.awt.Color(204, 204, 255));
        txt_VentaNombreProducto.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jPanel5.add(txt_VentaNombreProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 170, 165, 30));

        LabelTotal.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        LabelTotal.setForeground(new java.awt.Color(255, 255, 255));
        LabelTotal.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        jPanel5.add(LabelTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 440, 160, 30));

        jLabel80.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel80.setForeground(new java.awt.Color(255, 255, 255));
        jLabel80.setText("Dirección Cliente:");
        jPanel5.add(jLabel80, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 500, -1, -1));

        txt_NombreClienteVenta.setEditable(false);
        txt_NombreClienteVenta.setBackground(new java.awt.Color(204, 204, 255));
        txt_NombreClienteVenta.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txt_NombreClienteVenta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_NombreClienteVentaKeyPressed(evt);
            }
        });
        jPanel5.add(txt_NombreClienteVenta, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 440, 165, 30));

        txt_DirreccionClienteVenta.setEditable(false);
        txt_DirreccionClienteVenta.setBackground(new java.awt.Color(204, 204, 255));
        txt_DirreccionClienteVenta.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txt_DirreccionClienteVenta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_DirreccionClienteVentaKeyPressed(evt);
            }
        });
        jPanel5.add(txt_DirreccionClienteVenta, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 500, 165, 30));

        txt_TelefonoClienteVenta.setEditable(false);
        txt_TelefonoClienteVenta.setBackground(new java.awt.Color(204, 204, 255));
        txt_TelefonoClienteVenta.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txt_TelefonoClienteVenta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_TelefonoClienteVentaKeyPressed(evt);
            }
        });
        jPanel5.add(txt_TelefonoClienteVenta, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 500, 165, 30));

        jLabel81.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel81.setForeground(new java.awt.Color(255, 255, 255));
        jLabel81.setText("Identificación Cliente:");
        jPanel5.add(jLabel81, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 440, -1, -1));

        jLabel71.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel71.setForeground(new java.awt.Color(255, 255, 255));
        jLabel71.setText("Nombre Cliente:");
        jPanel5.add(jLabel71, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 440, -1, -1));

        txt_VentaId.setBackground(new java.awt.Color(204, 204, 255));
        txt_VentaId.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txt_VentaId.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_VentaIdKeyPressed(evt);
            }
        });
        jPanel5.add(txt_VentaId, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 170, 50, 30));

        cmb_NombreProductoVenta.setBackground(new java.awt.Color(204, 204, 255));
        cmb_NombreProductoVenta.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seleccione un Producto" }));
        cmb_NombreProductoVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmb_NombreProductoVentaActionPerformed(evt);
            }
        });
        jPanel5.add(cmb_NombreProductoVenta, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 130, 160, 30));

        jLabel53.setBackground(new java.awt.Color(204, 204, 255));
        jLabel53.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/medida.jpg"))); // NOI18N
        jLabel53.setText("jLabel52");
        jPanel5.add(jLabel53, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1010, 550));

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 870, Short.MAX_VALUE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );

        jPanel5.add(jPanel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, -30, 870, 30));

        jTabbedPane1.addTab("Ventas", jPanel5);

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Buscar:");
        jPanel4.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 330, -1, -1));

        txt_BuscarProducto.setBackground(new java.awt.Color(204, 204, 255));
        txt_BuscarProducto.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txt_BuscarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_BuscarProductoActionPerformed(evt);
            }
        });
        txt_BuscarProducto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_BuscarProductoKeyReleased(evt);
            }
        });
        jPanel4.add(txt_BuscarProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 320, 330, 30));

        Tabla_Producto.setBackground(new java.awt.Color(204, 204, 255));
        Tabla_Producto.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        Tabla_Producto.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Codigo", "Nombre", "Cantidad Disponible", "Categoria", "Descripcion", "Precio De Venta"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        Tabla_Producto.setFocusable(false);
        Tabla_Producto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Tabla_ProductoMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(Tabla_Producto);
        if (Tabla_Producto.getColumnModel().getColumnCount() > 0) {
            Tabla_Producto.getColumnModel().getColumn(0).setResizable(false);
            Tabla_Producto.getColumnModel().getColumn(1).setResizable(false);
            Tabla_Producto.getColumnModel().getColumn(2).setResizable(false);
            Tabla_Producto.getColumnModel().getColumn(3).setResizable(false);
            Tabla_Producto.getColumnModel().getColumn(4).setResizable(false);
            Tabla_Producto.getColumnModel().getColumn(5).setResizable(false);
            Tabla_Producto.getColumnModel().getColumn(6).setResizable(false);
        }

        jPanel4.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 370, 930, 160));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("ID:");
        jPanel4.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 40, -1, -1));

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Nombre:");
        jPanel4.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(45, 104, -1, -1));

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Codigo:");
        jPanel4.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(45, 164, -1, -1));

        txt_IdProducto.setEditable(false);
        txt_IdProducto.setBackground(new java.awt.Color(204, 204, 255));
        txt_IdProducto.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txt_IdProducto.setEnabled(false);
        jPanel4.add(txt_IdProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 40, 165, 30));

        txt_NombreProducto.setBackground(new java.awt.Color(204, 204, 255));
        txt_NombreProducto.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jPanel4.add(txt_NombreProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(118, 98, 165, 30));

        txt_ProductoCodigo.setBackground(new java.awt.Color(204, 204, 255));
        txt_ProductoCodigo.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jPanel4.add(txt_ProductoCodigo, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 160, 165, 30));

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Descripcion:");
        jPanel4.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 43, -1, -1));

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Categoria:");
        jPanel4.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 104, -1, -1));

        txt_DescripcionProducto.setBackground(new java.awt.Color(204, 204, 255));
        txt_DescripcionProducto.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jPanel4.add(txt_DescripcionProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(555, 37, 165, 30));

        btn_RegistrarProducto.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btn_RegistrarProducto.setText("Registrar");
        btn_RegistrarProducto.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_RegistrarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_RegistrarProductoActionPerformed(evt);
            }
        });
        jPanel4.add(btn_RegistrarProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(806, 37, 136, 30));

        btn_ActualizarProducto.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btn_ActualizarProducto.setText("Modificar");
        btn_ActualizarProducto.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_ActualizarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_ActualizarProductoActionPerformed(evt);
            }
        });
        jPanel4.add(btn_ActualizarProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(806, 98, 136, 30));

        btn_EliminarProducto.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btn_EliminarProducto.setText("Eliminar");
        btn_EliminarProducto.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_EliminarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_EliminarProductoActionPerformed(evt);
            }
        });
        jPanel4.add(btn_EliminarProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(806, 158, 136, 30));

        btn_CancelarProducto.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btn_CancelarProducto.setText("Cancelar");
        btn_CancelarProducto.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_CancelarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_CancelarProductoActionPerformed(evt);
            }
        });
        jPanel4.add(btn_CancelarProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(806, 216, 136, 30));

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Cantidades Disponibles:");
        jPanel4.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(45, 222, -1, -1));

        txt_Cantidades.setBackground(new java.awt.Color(204, 204, 255));
        txt_Cantidades.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jPanel4.add(txt_Cantidades, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 216, 165, 30));

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Precio de Venta:");
        jPanel4.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 170, -1, -1));

        txt_PrecioVenta.setBackground(new java.awt.Color(204, 204, 255));
        txt_PrecioVenta.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jPanel4.add(txt_PrecioVenta, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 170, 165, 30));

        cmb_CategoriaProducto.setBackground(new java.awt.Color(204, 204, 255));
        cmb_CategoriaProducto.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seleccione una Categoria" }));
        cmb_CategoriaProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmb_CategoriaProductoActionPerformed(evt);
            }
        });
        jPanel4.add(cmb_CategoriaProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 100, 160, 30));

        jButton1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton1.setText("Reporte");
        jButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel4.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 270, 136, 30));

        jLabel52.setBackground(new java.awt.Color(204, 204, 204));
        jLabel52.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/medida.jpg"))); // NOI18N
        jLabel52.setText("jLabel52");
        jPanel4.add(jLabel52, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1010, 550));

        jTabbedPane1.addTab("Productos", jPanel4);

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel32.setBackground(new java.awt.Color(255, 255, 255));
        jLabel32.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel32.setForeground(new java.awt.Color(255, 255, 255));
        jLabel32.setText("Buscar:");
        jPanel6.add(jLabel32, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 320, -1, -1));

        TablaClientes.setBackground(new java.awt.Color(204, 204, 255));
        TablaClientes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID Cliente", "Identificacion", "Nombre", "Telefono", "Direccion", "Correo"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        TablaClientes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TablaClientesMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(TablaClientes);

        jPanel6.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 360, 920, 170));

        jLabel20.setBackground(new java.awt.Color(255, 255, 255));
        jLabel20.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(255, 255, 255));
        jLabel20.setText("Identificacion:");
        jPanel6.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 100, -1, -1));

        jLabel21.setBackground(new java.awt.Color(255, 255, 255));
        jLabel21.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(255, 255, 255));
        jLabel21.setText("ID:");
        jPanel6.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 40, -1, -1));

        jLabel22.setBackground(new java.awt.Color(255, 255, 255));
        jLabel22.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(255, 255, 255));
        jLabel22.setText("Direccion:");
        jPanel6.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 100, -1, -1));

        jLabel23.setBackground(new java.awt.Color(255, 255, 255));
        jLabel23.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(255, 255, 255));
        jLabel23.setText("Telefono:");
        jPanel6.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 40, -1, -1));

        jLabel31.setBackground(new java.awt.Color(255, 255, 255));
        jLabel31.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel31.setForeground(new java.awt.Color(255, 255, 255));
        jLabel31.setText("Correo:");
        jPanel6.add(jLabel31, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 160, -1, -1));

        txt_BuscarCliente.setBackground(new java.awt.Color(204, 204, 255));
        txt_BuscarCliente.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txt_BuscarCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_BuscarClienteActionPerformed(evt);
            }
        });
        txt_BuscarCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_BuscarClienteKeyReleased(evt);
            }
        });
        jPanel6.add(txt_BuscarCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 310, 300, 30));

        txt_IDCliente.setEditable(false);
        txt_IDCliente.setBackground(new java.awt.Color(204, 204, 255));
        txt_IDCliente.setEnabled(false);
        txt_IDCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_IDClienteActionPerformed(evt);
            }
        });
        jPanel6.add(txt_IDCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 40, 165, 30));

        txt_NombreCliente.setBackground(new java.awt.Color(204, 204, 255));
        jPanel6.add(txt_NombreCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 160, 165, 30));

        txt_DireccionCliente.setBackground(new java.awt.Color(204, 204, 255));
        jPanel6.add(txt_DireccionCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 100, 165, 30));

        txt_TelefonoCliente.setBackground(new java.awt.Color(204, 204, 255));
        jPanel6.add(txt_TelefonoCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 40, 165, 30));

        txt_CorreoCliente.setBackground(new java.awt.Color(204, 204, 255));
        jPanel6.add(txt_CorreoCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 160, 165, 30));

        btn_RegistrarCliente.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btn_RegistrarCliente.setText("Registrar");
        btn_RegistrarCliente.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_RegistrarCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_RegistrarClienteActionPerformed(evt);
            }
        });
        jPanel6.add(btn_RegistrarCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 40, 115, 30));

        btn_ModificarCliente.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btn_ModificarCliente.setText("Modificar");
        btn_ModificarCliente.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_ModificarCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_ModificarClienteActionPerformed(evt);
            }
        });
        jPanel6.add(btn_ModificarCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 110, 115, 30));

        btn_EliminarCliente.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btn_EliminarCliente.setText("Eliminar");
        btn_EliminarCliente.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_EliminarCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_EliminarClienteActionPerformed(evt);
            }
        });
        jPanel6.add(btn_EliminarCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 170, 115, 30));

        btn_CancelarCliente.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btn_CancelarCliente.setText("Cancelar");
        btn_CancelarCliente.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_CancelarCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_CancelarClienteActionPerformed(evt);
            }
        });
        jPanel6.add(btn_CancelarCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 230, 115, 30));

        jLabel79.setBackground(new java.awt.Color(255, 255, 255));
        jLabel79.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel79.setForeground(new java.awt.Color(255, 255, 255));
        jLabel79.setText("Nombre Completo:");
        jPanel6.add(jLabel79, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 160, -1, -1));

        txt_IdentificacionCliente.setBackground(new java.awt.Color(204, 204, 255));
        jPanel6.add(txt_IdentificacionCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 100, 165, 30));

        jLabel54.setBackground(new java.awt.Color(255, 255, 255));
        jLabel54.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/medida.jpg"))); // NOI18N
        jLabel54.setText("jLabel52");
        jPanel6.add(jLabel54, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1010, 550));

        jTabbedPane1.addTab("Cliente", jPanel6);

        jPanel16.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        TablaProveedor.setBackground(new java.awt.Color(204, 204, 255));
        TablaProveedor.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Identificacion", "Nombre", "Telefono", "Email", "Direccion", "Descripcion"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                true, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        TablaProveedor.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TablaProveedorMouseClicked(evt);
            }
        });
        jScrollPane7.setViewportView(TablaProveedor);
        if (TablaProveedor.getColumnModel().getColumnCount() > 0) {
            TablaProveedor.getColumnModel().getColumn(0).setPreferredWidth(20);
        }

        jPanel16.add(jScrollPane7, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 310, 810, 230));

        btn_RegistrarProveedor.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btn_RegistrarProveedor.setText("Registrar");
        btn_RegistrarProveedor.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_RegistrarProveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_RegistrarProveedorActionPerformed(evt);
            }
        });
        jPanel16.add(btn_RegistrarProveedor, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 50, 136, 30));

        btn_ModificarProveedor.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btn_ModificarProveedor.setText("Modificar");
        btn_ModificarProveedor.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_ModificarProveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_ModificarProveedorActionPerformed(evt);
            }
        });
        jPanel16.add(btn_ModificarProveedor, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 110, 136, 30));

        btn_EliminarProveedor.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btn_EliminarProveedor.setText("Eliminar");
        btn_EliminarProveedor.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_EliminarProveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_EliminarProveedorActionPerformed(evt);
            }
        });
        jPanel16.add(btn_EliminarProveedor, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 170, 136, 30));

        btn_CancelarProveedor.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btn_CancelarProveedor.setText("Cancelar");
        btn_CancelarProveedor.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_CancelarProveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_CancelarProveedorActionPerformed(evt);
            }
        });
        jPanel16.add(btn_CancelarProveedor, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 230, 136, 30));

        jLabel26.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Ellipse 1_1.png"))); // NOI18N
        jPanel16.add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(900, 430, -1, 100));

        jLabel30.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/bavaria.png"))); // NOI18N
        jPanel16.add(jLabel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 180, -1, 140));

        jLabel27.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/colombina.png"))); // NOI18N
        jPanel16.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 300, -1, 150));

        jLabel28.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/descarga.png"))); // NOI18N
        jPanel16.add(jLabel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 440, -1, 110));

        jLabel29.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/mimos.png"))); // NOI18N
        jPanel16.add(jLabel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 80, -1, 140));

        jLabel61.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/crem-helado.png"))); // NOI18N
        jPanel16.add(jLabel61, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 100));

        jLabel62.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/crem-helado.png"))); // NOI18N
        jPanel16.add(jLabel62, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 100));

        jLabel63.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/coca.png"))); // NOI18N
        jPanel16.add(jLabel63, new org.netbeans.lib.awtextra.AbsoluteConstraints(910, 0, -1, 70));

        jLabel64.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/postobon.png"))); // NOI18N
        jPanel16.add(jLabel64, new org.netbeans.lib.awtextra.AbsoluteConstraints(910, 70, -1, 70));

        jLabel65.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/doritos.jpg"))); // NOI18N
        jPanel16.add(jLabel65, new org.netbeans.lib.awtextra.AbsoluteConstraints(910, 140, -1, 90));

        jLabel66.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/margarita.jpg"))); // NOI18N
        jPanel16.add(jLabel66, new org.netbeans.lib.awtextra.AbsoluteConstraints(910, 230, -1, 100));

        jLabel67.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/yupi.png"))); // NOI18N
        jPanel16.add(jLabel67, new org.netbeans.lib.awtextra.AbsoluteConstraints(910, 330, -1, 100));

        jLabel68.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/yupi.png"))); // NOI18N
        jPanel16.add(jLabel68, new org.netbeans.lib.awtextra.AbsoluteConstraints(910, 330, -1, 100));

        jLabel69.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/yupi.png"))); // NOI18N
        jPanel16.add(jLabel69, new org.netbeans.lib.awtextra.AbsoluteConstraints(910, 330, -1, 100));

        jLabel70.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/yupi.png"))); // NOI18N
        jPanel16.add(jLabel70, new org.netbeans.lib.awtextra.AbsoluteConstraints(910, 330, -1, 100));

        jLabel72.setBackground(new java.awt.Color(255, 255, 255));
        jLabel72.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel72.setForeground(new java.awt.Color(255, 255, 255));
        jLabel72.setText("Identificacion:");
        jPanel16.add(jLabel72, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 60, -1, -1));

        txt_IdentificacionProveedor.setEditable(false);
        txt_IdentificacionProveedor.setBackground(new java.awt.Color(204, 204, 255));
        jPanel16.add(txt_IdentificacionProveedor, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 50, 165, 30));

        jLabel73.setBackground(new java.awt.Color(255, 255, 255));
        jLabel73.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel73.setForeground(new java.awt.Color(255, 255, 255));
        jLabel73.setText("Nombre Completo:");
        jPanel16.add(jLabel73, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 120, -1, -1));

        txt_NombreProveedor.setBackground(new java.awt.Color(204, 204, 255));
        jPanel16.add(txt_NombreProveedor, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 110, 165, 30));

        jLabel74.setBackground(new java.awt.Color(255, 255, 255));
        jLabel74.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel74.setForeground(new java.awt.Color(255, 255, 255));
        jLabel74.setText("Direccion:");
        jPanel16.add(jLabel74, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 180, -1, -1));

        txt_BuscarProveedor.setBackground(new java.awt.Color(204, 204, 255));
        txt_BuscarProveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_BuscarProveedorActionPerformed(evt);
            }
        });
        txt_BuscarProveedor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_BuscarProveedorKeyReleased(evt);
            }
        });
        jPanel16.add(txt_BuscarProveedor, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 250, 320, 30));

        jLabel75.setBackground(new java.awt.Color(255, 255, 255));
        jLabel75.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel75.setForeground(new java.awt.Color(255, 255, 255));
        jLabel75.setText("Telefono:");
        jPanel16.add(jLabel75, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 50, -1, -1));

        txt_TelefonoProveedor.setBackground(new java.awt.Color(204, 204, 255));
        jPanel16.add(txt_TelefonoProveedor, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 50, 165, 30));

        jLabel76.setBackground(new java.awt.Color(255, 255, 255));
        jLabel76.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel76.setForeground(new java.awt.Color(255, 255, 255));
        jLabel76.setText("Correo:");
        jPanel16.add(jLabel76, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 120, -1, -1));

        txt_CorreoProveedor.setBackground(new java.awt.Color(204, 204, 255));
        jPanel16.add(txt_CorreoProveedor, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 120, 165, 30));

        jLabel77.setBackground(new java.awt.Color(255, 255, 255));
        jLabel77.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel77.setForeground(new java.awt.Color(255, 255, 255));
        jLabel77.setText("Buscar:");
        jPanel16.add(jLabel77, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 250, -1, -1));

        txt_DireccionProveedor.setBackground(new java.awt.Color(204, 204, 255));
        jPanel16.add(txt_DireccionProveedor, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 170, 165, 30));

        jLabel78.setBackground(new java.awt.Color(255, 255, 255));
        jLabel78.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel78.setForeground(new java.awt.Color(255, 255, 255));
        jLabel78.setText("Descripcion:");
        jPanel16.add(jLabel78, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 180, -1, -1));

        txt_DescripcionProveedor.setBackground(new java.awt.Color(204, 204, 255));
        jPanel16.add(txt_DescripcionProveedor, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 180, 190, 30));

        txt_IDProveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_IDProveedorActionPerformed(evt);
            }
        });
        jPanel16.add(txt_IDProveedor, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 10, -1, -1));

        jLabel56.setBackground(new java.awt.Color(204, 204, 255));
        jLabel56.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/medida.jpg"))); // NOI18N
        jLabel56.setText("zz");
        jPanel16.add(jLabel56, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1010, 550));

        javax.swing.GroupLayout panelTaparLayout = new javax.swing.GroupLayout(panelTapar);
        panelTapar.setLayout(panelTaparLayout);
        panelTaparLayout.setHorizontalGroup(
            panelTaparLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        panelTaparLayout.setVerticalGroup(
            panelTaparLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 110, Short.MAX_VALUE)
        );

        jPanel16.add(panelTapar, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, -40, -1, 110));

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Proveedores", jPanel7);

        jPanel15.setBackground(new java.awt.Color(255, 255, 255));
        jPanel15.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel15.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(44, 55, -1, -1));

        jLabel34.setBackground(new java.awt.Color(255, 255, 255));
        jLabel34.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel34.setForeground(new java.awt.Color(255, 255, 255));
        jLabel34.setText("Buscar:");
        jPanel15.add(jLabel34, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 250, -1, -1));

        txt_BuscarCategoria.setBackground(new java.awt.Color(204, 204, 255));
        txt_BuscarCategoria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_BuscarCategoriaActionPerformed(evt);
            }
        });
        txt_BuscarCategoria.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_BuscarCategoriaKeyReleased(evt);
            }
        });
        jPanel15.add(txt_BuscarCategoria, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 240, 290, 30));

        TablaCategoria.setBackground(new java.awt.Color(204, 204, 255));
        TablaCategoria.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Nombre"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        TablaCategoria.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TablaCategoriaMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(TablaCategoria);

        jPanel15.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 290, 500, 240));

        jLabel25.setBackground(new java.awt.Color(255, 255, 255));
        jLabel25.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(255, 255, 255));
        jLabel25.setText("ID:");
        jPanel15.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 50, -1, -1));

        jLabel33.setBackground(new java.awt.Color(255, 255, 255));
        jLabel33.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel33.setForeground(new java.awt.Color(255, 255, 255));
        jLabel33.setText("Nombre:");
        jPanel15.add(jLabel33, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 130, -1, -1));

        txt_IdCategoria.setEditable(false);
        txt_IdCategoria.setBackground(new java.awt.Color(204, 204, 255));
        txt_IdCategoria.setEnabled(false);
        txt_IdCategoria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_IdCategoriaActionPerformed(evt);
            }
        });
        jPanel15.add(txt_IdCategoria, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 40, 165, 30));

        txt_NombreCategoria.setBackground(new java.awt.Color(204, 204, 255));
        jPanel15.add(txt_NombreCategoria, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 120, 165, 30));

        btn_RegistrarCategoria.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btn_RegistrarCategoria.setText("Registrar");
        btn_RegistrarCategoria.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_RegistrarCategoria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_RegistrarCategoriaActionPerformed(evt);
            }
        });
        jPanel15.add(btn_RegistrarCategoria, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 40, 105, 30));

        btn_ModificarCategoria.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btn_ModificarCategoria.setText("Modificar");
        btn_ModificarCategoria.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_ModificarCategoria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_ModificarCategoriaActionPerformed(evt);
            }
        });
        jPanel15.add(btn_ModificarCategoria, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 110, 105, 30));

        btn_EliminarCategoria.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btn_EliminarCategoria.setText("Eliminar");
        btn_EliminarCategoria.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_EliminarCategoria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_EliminarCategoriaActionPerformed(evt);
            }
        });
        jPanel15.add(btn_EliminarCategoria, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 180, 105, 30));

        btn_CancelarCategoria.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btn_CancelarCategoria.setText("Cancelar");
        btn_CancelarCategoria.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_CancelarCategoria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_CancelarCategoriaActionPerformed(evt);
            }
        });
        jPanel15.add(btn_CancelarCategoria, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 250, 105, 30));

        jLabel60.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/gokuhelado.jpg"))); // NOI18N
        jPanel15.add(jLabel60, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 90, -1, 370));

        jLabel55.setBackground(new java.awt.Color(255, 255, 255));
        jLabel55.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/medida.jpg"))); // NOI18N
        jLabel55.setText("jLabel52");
        jPanel15.add(jLabel55, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1010, 550));

        jTabbedPane1.addTab("Categorias", jPanel15);

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));
        jPanel9.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel35.setBackground(new java.awt.Color(0, 0, 0));
        jLabel35.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel35.setForeground(new java.awt.Color(255, 255, 255));
        jLabel35.setText("VENTAS REALIZADAS");
        jPanel9.add(jLabel35, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 30, -1, -1));

        TablaReportes.setBackground(new java.awt.Color(204, 204, 255));
        TablaReportes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Cliente", "Total", "Fecha"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        TablaReportes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TablaReportesMouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(TablaReportes);

        jPanel9.add(jScrollPane5, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 180, 920, 360));

        btn_ReportePdf.setBackground(new java.awt.Color(102, 0, 102));
        btn_ReportePdf.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/pdf_9560582.png"))); // NOI18N
        btn_ReportePdf.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btn_ReportePdf.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_ReportePdf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_ReportePdfActionPerformed(evt);
            }
        });
        jPanel9.add(btn_ReportePdf, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 110, 40, 50));

        txt_IdReportes.setBackground(new java.awt.Color(204, 204, 255));
        jPanel9.add(txt_IdReportes, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 130, 50, 30));

        jLabel42.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel42.setForeground(new java.awt.Color(255, 255, 255));
        jLabel42.setText("Mostrar Reporte:");
        jPanel9.add(jLabel42, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 130, -1, -1));

        btn_ReporteTorta.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/barra.jpg"))); // NOI18N
        btn_ReporteTorta.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_ReporteTorta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_ReporteTortaActionPerformed(evt);
            }
        });
        jPanel9.add(btn_ReporteTorta, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 110, 60, 50));

        jLabel85.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel85.setForeground(new java.awt.Color(255, 255, 255));
        jLabel85.setText("Seleccione una Fecha:");
        jPanel9.add(jLabel85, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 110, -1, -1));

        jLabel84.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel84.setForeground(new java.awt.Color(255, 255, 255));
        jLabel84.setText("Mostrar Factura: ");
        jPanel9.add(jLabel84, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 130, -1, -1));

        MiDate.setBackground(new java.awt.Color(204, 204, 255));
        jPanel9.add(MiDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 130, 200, 30));

        jLabel57.setBackground(new java.awt.Color(255, 255, 51));
        jLabel57.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/medida.jpg"))); // NOI18N
        jLabel57.setText("jLabel52");
        jPanel9.add(jLabel57, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1010, 560));

        jTabbedPane1.addTab("Reportes", jPanel9);

        jPanel10.setBackground(new java.awt.Color(255, 255, 255));
        jPanel10.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel36.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel36.setForeground(new java.awt.Color(255, 255, 255));
        jLabel36.setText("NIT de la Empresa:");
        jPanel10.add(jLabel36, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 140, -1, -1));

        jLabel37.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel37.setForeground(new java.awt.Color(255, 255, 255));
        jLabel37.setText("Nombre Empresa:");
        jPanel10.add(jLabel37, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 210, -1, -1));

        jLabel38.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel38.setForeground(new java.awt.Color(255, 255, 255));
        jLabel38.setText("Direccion:");
        jPanel10.add(jLabel38, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 180, -1, -1));

        jLabel39.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel39.setForeground(new java.awt.Color(255, 255, 255));
        jLabel39.setText("Telefono:");
        jPanel10.add(jLabel39, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 250, -1, -1));

        jLabel40.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel40.setForeground(new java.awt.Color(255, 255, 255));
        jLabel40.setText("Correo:");
        jPanel10.add(jLabel40, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 280, -1, -1));

        txt_NitEmpresa.setBackground(new java.awt.Color(204, 204, 255));
        txt_NitEmpresa.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txt_NitEmpresa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_NitEmpresaActionPerformed(evt);
            }
        });
        jPanel10.add(txt_NitEmpresa, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 140, 240, 30));

        txt_NombreEmpresa.setBackground(new java.awt.Color(204, 204, 255));
        txt_NombreEmpresa.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jPanel10.add(txt_NombreEmpresa, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 210, 240, 30));

        txt_DireccionEmpresa.setBackground(new java.awt.Color(204, 204, 255));
        txt_DireccionEmpresa.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jPanel10.add(txt_DireccionEmpresa, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 180, 220, 30));

        txt_TelefonoEmpresa.setBackground(new java.awt.Color(204, 204, 255));
        txt_TelefonoEmpresa.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jPanel10.add(txt_TelefonoEmpresa, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 250, 220, 30));

        txt_CorreoEmpresa.setBackground(new java.awt.Color(204, 204, 255));
        txt_CorreoEmpresa.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jPanel10.add(txt_CorreoEmpresa, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 280, 240, 30));

        btn_ModificarEmpresa.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        btn_ModificarEmpresa.setText("Actualizar");
        btn_ModificarEmpresa.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_ModificarEmpresa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_ModificarEmpresaActionPerformed(evt);
            }
        });
        jPanel10.add(btn_ModificarEmpresa, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 400, 140, 30));

        jLabel41.setBackground(new java.awt.Color(0, 0, 0));
        jLabel41.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel41.setForeground(new java.awt.Color(255, 255, 255));
        jLabel41.setText("Datos de la Empresa Buhomar");
        jPanel10.add(jLabel41, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 40, -1, -1));
        jPanel10.add(txt_IdEmpresa, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 90, 20, -1));

        jLabel58.setBackground(new java.awt.Color(204, 204, 255));
        jLabel58.setForeground(new java.awt.Color(255, 255, 255));
        jLabel58.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/medida.jpg"))); // NOI18N
        jLabel58.setText("jLabel52");
        jPanel10.add(jLabel58, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1010, 550));

        jTabbedPane1.addTab("Datos Empresa", jPanel10);

        jPanel11.setBackground(new java.awt.Color(255, 255, 255));
        jPanel11.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel51.setBackground(new java.awt.Color(204, 204, 255));
        jLabel51.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel51.setForeground(new java.awt.Color(255, 255, 255));
        jLabel51.setText("Buscar:");
        jPanel11.add(jLabel51, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 260, 60, 40));

        txt_BuscarUsuario.setBackground(new java.awt.Color(204, 204, 255));
        txt_BuscarUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_BuscarUsuarioActionPerformed(evt);
            }
        });
        txt_BuscarUsuario.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_BuscarUsuarioKeyReleased(evt);
            }
        });
        jPanel11.add(txt_BuscarUsuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 270, 350, 30));

        TablaUsuarios.setBackground(new java.awt.Color(204, 204, 255));
        TablaUsuarios.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID Usuario", "Identificacion", "Nombre", "Nombre de Usuario", "Direccion", "Telefono", "Correo", "Rol", "Contraseña"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        TablaUsuarios.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TablaUsuariosMouseClicked(evt);
            }
        });
        jScrollPane6.setViewportView(TablaUsuarios);

        jPanel11.add(jScrollPane6, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 320, 960, 210));

        jLabel43.setBackground(new java.awt.Color(204, 204, 255));
        jLabel43.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel43.setForeground(new java.awt.Color(255, 255, 255));
        jLabel43.setText("Identificacion:");
        jPanel11.add(jLabel43, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 40, 110, -1));

        jLabel44.setBackground(new java.awt.Color(204, 204, 255));
        jLabel44.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel44.setForeground(new java.awt.Color(255, 255, 255));
        jLabel44.setText("Nombre:");
        jPanel11.add(jLabel44, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 90, -1, -1));

        jLabel45.setBackground(new java.awt.Color(204, 204, 255));
        jLabel45.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel45.setForeground(new java.awt.Color(255, 255, 255));
        jLabel45.setText("Nombre Usuario:");
        jPanel11.add(jLabel45, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 140, 120, -1));

        jLabel46.setBackground(new java.awt.Color(204, 204, 255));
        jLabel46.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel46.setForeground(new java.awt.Color(255, 255, 255));
        jLabel46.setText("Rol:");
        jPanel11.add(jLabel46, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 190, -1, -1));

        jLabel47.setBackground(new java.awt.Color(204, 204, 255));
        jLabel47.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel47.setForeground(new java.awt.Color(255, 255, 255));
        jLabel47.setText("Direccion:");
        jPanel11.add(jLabel47, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 40, -1, -1));

        jLabel48.setBackground(new java.awt.Color(204, 204, 255));
        jLabel48.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel48.setForeground(new java.awt.Color(255, 255, 255));
        jLabel48.setText("Telefono:");
        jPanel11.add(jLabel48, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 90, -1, -1));

        jLabel49.setBackground(new java.awt.Color(204, 204, 255));
        jLabel49.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel49.setForeground(new java.awt.Color(255, 255, 255));
        jLabel49.setText("Correo:");
        jPanel11.add(jLabel49, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 140, -1, -1));

        jLabel50.setBackground(new java.awt.Color(204, 204, 255));
        jLabel50.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel50.setForeground(new java.awt.Color(255, 255, 255));
        jLabel50.setText("Contraseña:");
        jPanel11.add(jLabel50, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 190, -1, -1));

        txt_IdentificacionUsuario.setBackground(new java.awt.Color(204, 204, 255));
        jPanel11.add(txt_IdentificacionUsuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 40, 165, 30));

        txt_nombreCompletoUsuario.setBackground(new java.awt.Color(204, 204, 255));
        jPanel11.add(txt_nombreCompletoUsuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 90, 165, 30));

        txt_nombreUsuario.setBackground(new java.awt.Color(204, 204, 255));
        jPanel11.add(txt_nombreUsuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 140, 165, 30));

        txt_direccionUsuario.setBackground(new java.awt.Color(204, 204, 255));
        jPanel11.add(txt_direccionUsuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 40, 165, 30));

        cmb_rol.setBackground(new java.awt.Color(204, 204, 255));
        cmb_rol.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Administrador", "Empleado" }));
        cmb_rol.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmb_rolActionPerformed(evt);
            }
        });
        jPanel11.add(cmb_rol, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 190, 165, 30));

        txt_telefonoUsuario.setBackground(new java.awt.Color(204, 204, 255));
        jPanel11.add(txt_telefonoUsuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 90, 165, 30));

        txt_correoUsuario.setBackground(new java.awt.Color(204, 204, 255));
        jPanel11.add(txt_correoUsuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 140, 165, 30));

        btn_RegistrarUsuario.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btn_RegistrarUsuario.setText("Registrar");
        btn_RegistrarUsuario.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_RegistrarUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_RegistrarUsuarioActionPerformed(evt);
            }
        });
        jPanel11.add(btn_RegistrarUsuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 40, 105, 30));

        btn_modificarUsuario.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btn_modificarUsuario.setText("Modificar");
        btn_modificarUsuario.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_modificarUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_modificarUsuarioActionPerformed(evt);
            }
        });
        jPanel11.add(btn_modificarUsuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 90, 105, 30));

        btn_eliminarUsuario.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btn_eliminarUsuario.setText("Eliminar");
        btn_eliminarUsuario.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_eliminarUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_eliminarUsuarioActionPerformed(evt);
            }
        });
        jPanel11.add(btn_eliminarUsuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 140, 105, 30));

        btn_cancelarUsuario.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btn_cancelarUsuario.setText("Cancelar");
        btn_cancelarUsuario.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_cancelarUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cancelarUsuarioActionPerformed(evt);
            }
        });
        jPanel11.add(btn_cancelarUsuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 190, 105, 30));

        txt_contraseñaUsuario.setBackground(new java.awt.Color(204, 204, 255));
        jPanel11.add(txt_contraseñaUsuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 190, 165, 30));

        txt_IdUsuario.setEditable(false);
        txt_IdUsuario.setEnabled(false);
        txt_IdUsuario.setRequestFocusEnabled(false);
        jPanel11.add(txt_IdUsuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 10, 40, -1));

        jLabel59.setBackground(new java.awt.Color(204, 204, 255));
        jLabel59.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/medida.jpg"))); // NOI18N
        jLabel59.setText("jLabel52");
        jPanel11.add(jLabel59, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1010, 550));

        jTabbedPane1.addTab("Administrar Empleados", jPanel11);

        getContentPane().add(jTabbedPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 100, 1010, 590));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_SalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_SalirActionPerformed
        dispose();
        InicioSesion login = new InicioSesion();
        login.setVisible(true);
    }//GEN-LAST:event_btn_SalirActionPerformed

    private void btn_FotoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_FotoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_FotoActionPerformed

    private void cmb_rolActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmb_rolActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmb_rolActionPerformed

    private void txt_NitEmpresaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_NitEmpresaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_NitEmpresaActionPerformed

    private void txt_IdCategoriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_IdCategoriaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_IdCategoriaActionPerformed

    private void txt_VentaPrecioProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_VentaPrecioProductoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_VentaPrecioProductoActionPerformed

    private void btn_RegistrarClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_RegistrarClienteActionPerformed
        if (!"".equals(txt_IdentificacionCliente.getText()) && !"".equals(txt_NombreCliente.getText()) && !"".equals(txt_DireccionCliente.getText()) && !"".equals(txt_TelefonoCliente.getText()) && !"".equals(txt_CorreoCliente.getText())) {
            try {
                int identificacion = Integer.parseInt(txt_IdentificacionCliente.getText());
                String nombre = txt_NombreCliente.getText();
                String direccion = txt_DireccionCliente.getText();
                String telefono = txt_TelefonoCliente.getText();
                String correo = txt_CorreoCliente.getText();

                cl.setIdentificacion(identificacion);
                cl.setNombre(nombre);
                cl.setTelefono(telefono);
                cl.setDireccion(direccion);
                cl.setCorreo(correo);
                cliente.registrarCliente(cl);
                limpiarTabla();
                limpiarCliente();
                ListarCliente();
                JOptionPane.showMessageDialog(null, "¡Cliente Registrado con Éxito!");
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "¡Error! Por favor ingrese un número válido para la identificación o teléfono.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "¡Los Campos están vacíos!");
        }
    }//GEN-LAST:event_btn_RegistrarClienteActionPerformed

    private void btn_ClientesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_ClientesActionPerformed
        limpiarTabla();
        ListarCliente();
        jTabbedPane1.setSelectedIndex(2);
    }//GEN-LAST:event_btn_ClientesActionPerformed

    private void TablaClientesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TablaClientesMouseClicked
        int fila = TablaClientes.rowAtPoint(evt.getPoint());
        txt_IDCliente.setText(TablaClientes.getValueAt(fila, 0).toString());
        txt_IdentificacionCliente.setText(TablaClientes.getValueAt(fila, 1).toString());
        txt_NombreCliente.setText(TablaClientes.getValueAt(fila, 2).toString());
        txt_TelefonoCliente.setText(TablaClientes.getValueAt(fila, 3).toString());
        txt_DireccionCliente.setText(TablaClientes.getValueAt(fila, 4).toString());
        txt_CorreoCliente.setText(TablaClientes.getValueAt(fila, 5).toString());

    }//GEN-LAST:event_TablaClientesMouseClicked

    private void btn_EliminarClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_EliminarClienteActionPerformed
        if (!"".equals(txt_IDCliente.getText())) {
            int pregunta = JOptionPane.showConfirmDialog(null, "¿Estas Seguro de Eliminar este Cliente?");
            if (pregunta == 0) {
                int id = Integer.parseInt(txt_IDCliente.getText());
                cliente.eliminarCliente(id);
                limpiarTabla();
                limpiarCliente();
                ListarCliente();
            }
        }
    }//GEN-LAST:event_btn_EliminarClienteActionPerformed

    private void txt_IDClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_IDClienteActionPerformed

    }//GEN-LAST:event_txt_IDClienteActionPerformed

    private void btn_CancelarClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_CancelarClienteActionPerformed
        limpiarCliente();
    }//GEN-LAST:event_btn_CancelarClienteActionPerformed

    private void btn_ModificarClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_ModificarClienteActionPerformed
        if ("".equals(txt_IDCliente.getText().trim())) {
            JOptionPane.showMessageDialog(null, "Seleccione una Fila");
        } else {
            // Usar trim() para eliminar espacios en blanco al principio y al final de las cadenas
            if (!"".equals(txt_IdentificacionCliente.getText().trim())
                    && !"".equals(txt_NombreCliente.getText().trim())
                    && !"".equals(txt_TelefonoCliente.getText().trim())
                    && !"".equals(txt_DireccionCliente.getText().trim())
                    && !"".equals(txt_CorreoCliente.getText().trim())) {

                cl.setIdentificacion(Integer.parseInt(txt_IdentificacionCliente.getText().trim()));
                cl.setNombre(txt_NombreCliente.getText().trim());
                cl.setTelefono(txt_TelefonoCliente.getText().trim());
                cl.setDireccion(txt_DireccionCliente.getText().trim());
                cl.setCorreo(txt_CorreoCliente.getText().trim());
                cl.setId(Integer.parseInt(txt_IDCliente.getText().trim()));
                cliente.modificarCliente(cl);
                limpiarTabla();
                limpiarCliente();
                ListarCliente();
                JOptionPane.showMessageDialog(null, "Cliente Modificado");
            } else {
                JOptionPane.showMessageDialog(null, "Los Campos Estan Vacios");
            }
        }
    }//GEN-LAST:event_btn_ModificarClienteActionPerformed

    private void btn_RegistrarProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_RegistrarProveedorActionPerformed
        // Usar trim() para eliminar espacios en blanco
        if (!"".equals(txt_IdentificacionProveedor.getText().trim())
                && !"".equals(txt_NombreProveedor.getText().trim())
                && !"".equals(txt_DireccionProveedor.getText().trim())
                && !"".equals(txt_TelefonoProveedor.getText().trim())
                && !"".equals(txt_CorreoProveedor.getText().trim())
                && !"".equals(txt_DescripcionProveedor.getText().trim())) {

            try {
                pr.setIdentificacion(Integer.parseInt(txt_IdentificacionProveedor.getText().trim()));
                pr.setNombre(txt_NombreProveedor.getText().trim());
                pr.setDireccion(txt_DireccionProveedor.getText().trim());
                pr.setTelefono(txt_TelefonoProveedor.getText().trim());
                pr.setCorreo(txt_CorreoProveedor.getText().trim());
                pr.setDescripcion(txt_DescripcionProveedor.getText().trim());
                proveedor.registrarProveedor(pr);
                limpiarTabla();
                ListarProveedor();
                limpiarProveedor();
                JOptionPane.showMessageDialog(null, "¡Proveedor Registrado con Éxito!");
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Error: Verifique que los campos numéricos sean válidos.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Los Campos Están Vacíos");
        }
    }//GEN-LAST:event_btn_RegistrarProveedorActionPerformed

    private void btn_ProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_ProveedorActionPerformed
        limpiarTabla();
        ListarProveedor();
        jTabbedPane1.setSelectedIndex(3);
    }//GEN-LAST:event_btn_ProveedorActionPerformed

    private void TablaProveedorMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TablaProveedorMouseClicked
        int fila = TablaProveedor.rowAtPoint(evt.getPoint());
        txt_IDProveedor.setText(TablaProveedor.getValueAt(fila, 0).toString());
        txt_IdentificacionProveedor.setText(TablaProveedor.getValueAt(fila, 1).toString());
        txt_NombreProveedor.setText(TablaProveedor.getValueAt(fila, 2).toString());
        txt_TelefonoProveedor.setText(TablaProveedor.getValueAt(fila, 3).toString());
        txt_CorreoProveedor.setText(TablaProveedor.getValueAt(fila, 4).toString());
        txt_DireccionProveedor.setText(TablaProveedor.getValueAt(fila, 5).toString());
        txt_DescripcionProveedor.setText(TablaProveedor.getValueAt(fila, 6).toString());

    }//GEN-LAST:event_TablaProveedorMouseClicked

    private void txt_IDProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_IDProveedorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_IDProveedorActionPerformed

    private void btn_EliminarProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_EliminarProveedorActionPerformed
        if (!"".equals(txt_IDProveedor.getText())) {
            int pregunta = JOptionPane.showConfirmDialog(null, "¿Deseas Eliminar este Proveedor?");
            if (pregunta == 0) {
                int id = Integer.parseInt(txt_IDProveedor.getText());
                proveedor.eliminarProveedor(id);
                limpiarTabla();
                ListarProveedor();
                limpiarProveedor();
            }
        } else {
            JOptionPane.showMessageDialog(null, "Seleccione una Fila");
        }
    }//GEN-LAST:event_btn_EliminarProveedorActionPerformed

    private void btn_ModificarProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_ModificarProveedorActionPerformed
        if ("".equals(txt_IDProveedor.getText().trim())) {
            JOptionPane.showMessageDialog(null, "Seleccione una Fila");
        } else {
            if (!"".equals(txt_IdentificacionProveedor.getText().trim())
                    && !"".equals(txt_NombreProveedor.getText().trim())
                    && !"".equals(txt_DireccionProveedor.getText().trim())
                    && !"".equals(txt_TelefonoProveedor.getText().trim())
                    && !"".equals(txt_CorreoProveedor.getText().trim())
                    && !"".equals(txt_DescripcionProveedor.getText().trim())) {

                pr.setIdentificacion(Integer.parseInt(txt_IdentificacionProveedor.getText().trim()));
                pr.setNombre(txt_NombreProveedor.getText().trim());
                pr.setDireccion(txt_DireccionProveedor.getText().trim());
                pr.setTelefono(txt_TelefonoProveedor.getText().trim());
                pr.setCorreo(txt_CorreoProveedor.getText().trim());
                pr.setDescripcion(txt_DescripcionProveedor.getText().trim());
                pr.setId(Integer.parseInt(txt_IDProveedor.getText().trim()));
                proveedor.modificarProveedor(pr);
                limpiarTabla();
                ListarProveedor();
                limpiarProveedor();
                JOptionPane.showMessageDialog(null, "Proveedor Modificado");
            } else {
                JOptionPane.showMessageDialog(null, "Por favor, complete todos los campos.");
            }
        }
    }//GEN-LAST:event_btn_ModificarProveedorActionPerformed

    private void btn_CancelarProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_CancelarProveedorActionPerformed
        limpiarProveedor();
    }//GEN-LAST:event_btn_CancelarProveedorActionPerformed

    private void txt_BuscarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_BuscarProductoActionPerformed
        String criterio = txt_BuscarProducto.getText().trim();

        if (!criterio.isEmpty()) {
            List<Productos> productos = prodao.buscarProducto(criterio);
            actualizarTablaProductos(productos);
        } else {
            // Si el campo de búsqueda está vacío, puedes optar por mostrar todos los proveedores o limpiar la tabla
            List<Productos> productos = prodao.listarProductos();
            actualizarTablaProductos(productos);
        }
    }//GEN-LAST:event_txt_BuscarProductoActionPerformed

    private void txt_BuscarProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_BuscarProveedorActionPerformed
        String criterio = txt_BuscarProveedor.getText().trim();

        if (!criterio.isEmpty()) {
            List<Proveedor> proveedores = proveedor.buscarProveedor(criterio);
            actualizarTablaProveedores(proveedores);
        } else {
            // Si el campo de búsqueda está vacío, puedes optar por mostrar todos los proveedores o limpiar la tabla
            List<Proveedor> proveedores = proveedor.listarProveedore();
            actualizarTablaProveedores(proveedores);
        }
    }//GEN-LAST:event_txt_BuscarProveedorActionPerformed

    private void txt_BuscarProveedorKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_BuscarProveedorKeyReleased
        txt_BuscarProveedorActionPerformed(null);
    }//GEN-LAST:event_txt_BuscarProveedorKeyReleased

    private void txt_BuscarClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_BuscarClienteActionPerformed
        String criterio = txt_BuscarCliente.getText().trim();

        if (!criterio.isEmpty()) {
            List<Cliente> clientes = cliente.buscarCliente(criterio);
            actualizarTablaClientes(clientes);
        } else {
            // Si el campo de búsqueda está vacío, puedes optar por mostrar todos los proveedores o limpiar la tabla
            List<Cliente> clientes = cliente.listarCliente();
            actualizarTablaClientes(clientes);
        }
    }//GEN-LAST:event_txt_BuscarClienteActionPerformed

    private void txt_BuscarClienteKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_BuscarClienteKeyReleased
        txt_BuscarClienteActionPerformed(null);
    }//GEN-LAST:event_txt_BuscarClienteKeyReleased

    private void btn_RegistrarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_RegistrarProductoActionPerformed

        if (!"".equals(txt_ProductoCodigo.getText().trim())
                && !"".equals(txt_NombreProducto.getText().trim())
                && !"".equals(txt_DescripcionProducto.getText().trim())
                && cmb_CategoriaProducto.getSelectedItem() != null
                && !"".equals(txt_Cantidades.getText().trim())
                && !"".equals(txt_PrecioVenta.getText().trim())) {

            pro.setCodigo(txt_ProductoCodigo.getText());
            pro.setNombre(txt_NombreProducto.getText());
            pro.setDescripcion(txt_DescripcionProducto.getText());
            pro.setCategoria(cmb_CategoriaProducto.getSelectedItem().toString());
            pro.setStock(Integer.parseInt(txt_Cantidades.getText()));
            pro.setPrecio(Double.parseDouble(txt_PrecioVenta.getText()));
            prodao.registrarProducto(pro);
            limpiarTabla();
            ListarProductos();
            limpiarProducto();
            JOptionPane.showMessageDialog(null, "Producto Registrado con Exito");

        } else {
            JOptionPane.showMessageDialog(null, "Los Campos están Vacíos");
        }


    }//GEN-LAST:event_btn_RegistrarProductoActionPerformed

    private void btn_RegistrarCategoriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_RegistrarCategoriaActionPerformed
        if (!"".equals(txt_NombreCategoria.getText().trim())) {
            cat.setNombre(txt_NombreCategoria.getText());
            catdao.registrarCategoria(cat);
            limpiarTabla();
            ListarCategoria();
            limpiarCategoria();
            JOptionPane.showMessageDialog(null, "Categoría Registrada con Exito");

        } else {
            JOptionPane.showMessageDialog(null, "Los Campos estan Vacíos");
        }
    }//GEN-LAST:event_btn_RegistrarCategoriaActionPerformed

    private void btn_ProductosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_ProductosActionPerformed
        cmb_CategoriaProducto.removeAllItems(); // Limpiar el JComboBox antes de actualizar
        prodao.consultarCategoria(cmb_CategoriaProducto);
        limpiarTabla();
        ListarProductos();
        jTabbedPane1.setSelectedIndex(1);
    }//GEN-LAST:event_btn_ProductosActionPerformed


    private void btn_CategoriasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_CategoriasActionPerformed
        limpiarTabla();
        ListarCategoria();
        jTabbedPane1.setSelectedIndex(4);
    }//GEN-LAST:event_btn_CategoriasActionPerformed

    private void Tabla_ProductoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Tabla_ProductoMouseClicked
        int fila = Tabla_Producto.rowAtPoint(evt.getPoint());
        txt_IdProducto.setText(Tabla_Producto.getValueAt(fila, 0).toString());
        txt_ProductoCodigo.setText(Tabla_Producto.getValueAt(fila, 1).toString());
        txt_NombreProducto.setText(Tabla_Producto.getValueAt(fila, 2).toString());
        txt_Cantidades.setText(Tabla_Producto.getValueAt(fila, 3).toString());
        cmb_CategoriaProducto.setSelectedItem(Tabla_Producto.getValueAt(fila, 4).toString());
        txt_DescripcionProducto.setText(Tabla_Producto.getValueAt(fila, 5).toString());
        txt_PrecioVenta.setText(Tabla_Producto.getValueAt(fila, 6).toString());
    }//GEN-LAST:event_Tabla_ProductoMouseClicked

    private void btn_EliminarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_EliminarProductoActionPerformed
        if (!"".equals(txt_IdProducto.getText())) {
            int pregunta = JOptionPane.showConfirmDialog(null, "¿Deseas Eliminar este Producto?");
            if (pregunta == 0) {
                int id = Integer.parseInt(txt_IdProducto.getText());
                prodao.eliminarProducto(id);
                limpiarTabla();
                ListarProductos();
                limpiarProducto();
            }
        } else {
            JOptionPane.showMessageDialog(null, "Seleccione una Fila");
        }
    }//GEN-LAST:event_btn_EliminarProductoActionPerformed

    private void btn_EliminarCategoriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_EliminarCategoriaActionPerformed
        if (!"".equals(txt_IdCategoria.getText())) {
            int pregunta = JOptionPane.showConfirmDialog(null, "¿Deseas Eliminar esta Categoria?");
            if (pregunta == 0) {
                int id = Integer.parseInt(txt_IdCategoria.getText());
                catdao.eliminarCategoria(id);
                limpiarTabla();
                ListarCategoria();
                limpiarCategoria();
            }
        } else {
            JOptionPane.showMessageDialog(null, "Seleccione una Fila");
        }
    }//GEN-LAST:event_btn_EliminarCategoriaActionPerformed

    private void TablaCategoriaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TablaCategoriaMouseClicked
        int fila = TablaCategoria.rowAtPoint(evt.getPoint());
        txt_IdCategoria.setText(TablaCategoria.getValueAt(fila, 0).toString());
        txt_NombreCategoria.setText(TablaCategoria.getValueAt(fila, 1).toString());
    }//GEN-LAST:event_TablaCategoriaMouseClicked

    private void btn_ActualizarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_ActualizarProductoActionPerformed
        if ("".equals(txt_IdProducto.getText().trim())) {
            JOptionPane.showMessageDialog(null, "Seleccione una Fila");
        } else {
            if (!"".equals(txt_ProductoCodigo.getText().trim())
                    && !"".equals(txt_NombreProducto.getText().trim())
                    && !"".equals(txt_DescripcionProducto.getText().trim())
                    && !"".equals(cmb_CategoriaProducto.getSelectedItem())
                    && !"".equals(txt_Cantidades.getText().trim())
                    && !"".equals(txt_PrecioVenta.getText().trim())) {

                pro.setCodigo(txt_ProductoCodigo.getText().trim());
                pro.setNombre(txt_NombreProducto.getText().trim());
                pro.setDescripcion(txt_DescripcionProducto.getText().trim());
                pro.setCategoria((String) cmb_CategoriaProducto.getSelectedItem().toString());
                pro.setStock(Integer.parseInt(txt_Cantidades.getText()));
                pro.setPrecio(Double.parseDouble(txt_PrecioVenta.getText()));
                pro.setId(Integer.parseInt(txt_IdProducto.getText().trim()));
                prodao.modificarProducto(pro);
                limpiarTabla();
                ListarProductos();
                limpiarProducto();
                JOptionPane.showMessageDialog(null, "Producto Modificado");
            } else {
                JOptionPane.showMessageDialog(null, "Por favor, complete todos los campos.");
            }
        }
    }//GEN-LAST:event_btn_ActualizarProductoActionPerformed

    private void btn_ModificarCategoriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_ModificarCategoriaActionPerformed
        if ("".equals(txt_IdCategoria.getText().trim())) {
            JOptionPane.showMessageDialog(null, "Seleccione una Fila");
        } else {
            if (!"".equals(txt_NombreCategoria.getText().trim())) {
                cat.setNombre(txt_NombreCategoria.getText().trim());
                cat.setId(Integer.parseInt(txt_IdCategoria.getText().trim()));
                catdao.modificarCategoria(cat);
                limpiarTabla();
                ListarCategoria();
                limpiarCategoria();
                JOptionPane.showMessageDialog(null, "Categoria Modificada");
            } else {
                JOptionPane.showMessageDialog(null, "Por favor, complete todos los campos.");
            }
        }
    }//GEN-LAST:event_btn_ModificarCategoriaActionPerformed

    private void txt_BuscarProductoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_BuscarProductoKeyReleased
        txt_BuscarProductoActionPerformed(null);
    }//GEN-LAST:event_txt_BuscarProductoKeyReleased

    private void btn_CancelarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_CancelarProductoActionPerformed
        limpiarProducto();
    }//GEN-LAST:event_btn_CancelarProductoActionPerformed

    private void txt_BuscarCategoriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_BuscarCategoriaActionPerformed
        String criterio = txt_BuscarCategoria.getText().trim();

        if (!criterio.isEmpty()) {
            List<Categorias> categorias = catdao.buscarCategorias(criterio);
            actualizarTablaCategorias(categorias);
        } else {
            // Si el campo de búsqueda está vacío, puedes optar por mostrar todos los proveedores o limpiar la tabla
            List<Categorias> categorias = catdao.listarCategorias();
            actualizarTablaCategorias(categorias);
        }
    }//GEN-LAST:event_txt_BuscarCategoriaActionPerformed

    private void txt_BuscarCategoriaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_BuscarCategoriaKeyReleased
        txt_BuscarCategoriaActionPerformed(null);
    }//GEN-LAST:event_txt_BuscarCategoriaKeyReleased

    private void btn_CancelarCategoriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_CancelarCategoriaActionPerformed
        limpiarCategoria();
    }//GEN-LAST:event_btn_CancelarCategoriaActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        Excel.reporte();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void txt_VentaCodigoProductoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_VentaCodigoProductoKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            String cod = txt_VentaCodigoProducto.getText();
            if (!cod.isEmpty()) {
                pro = prodao.BuscarProVenta(cod);
                if (pro != null) { // Verifica que el producto no sea null
                    cmb_NombreProductoVenta.setSelectedItem(pro.getNombre());
                    txt_VentaNombreProducto.setText(pro.getNombre()); // Asegúrate de que se llene correctamente
                    txt_VentaPrecioProducto.setText(String.valueOf(pro.getPrecio()));
                    txt_VentaStockProducto.setText(String.valueOf(pro.getStock()));
                    txt_VentaDescripcionProducto.setText(pro.getDescripcion());
                    txt_VentaCantidadProducto.requestFocus();
                } else {
                    LimpiarVenta();
                    JOptionPane.showMessageDialog(null, "Producto no encontrado");
                    txt_VentaCodigoProducto.requestFocus();
                }
            } else {
                JOptionPane.showMessageDialog(null, "Ingrese el Código del Producto");
                txt_VentaCodigoProducto.requestFocus();
            }
        }
    }//GEN-LAST:event_txt_VentaCodigoProductoKeyPressed

    private void txt_VentaDescripcionProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_VentaDescripcionProductoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_VentaDescripcionProductoActionPerformed

    private void txt_CompraNombreProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_CompraNombreProductoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_CompraNombreProductoActionPerformed

    private void txt_VentaCantidadProductoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_VentaCantidadProductoKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (!"".equals(txt_VentaCantidadProducto.getText())) {
                String cod = txt_VentaCodigoProducto.getText();
                String nombre = txt_VentaNombreProducto.getText();
                String descripcion = txt_VentaDescripcionProducto.getText();
                int cantidad = Integer.parseInt(txt_VentaCantidadProducto.getText());
                double precio = Double.parseDouble(txt_VentaPrecioProducto.getText());
                double total = cantidad * precio;
                int stock = Integer.parseInt(txt_VentaStockProducto.getText());

                if (stock >= cantidad) {
                    boolean productoExistente = false;
                    tap = (DefaultTableModel) TablaVenta.getModel();

                    // Comparar por código de producto en lugar de solo por nombre
                    for (int i = 0; i < TablaVenta.getRowCount(); i++) {
                        String codigoEnTabla = TablaVenta.getValueAt(i, 0).toString(); // Código en la primera columna
                        if (codigoEnTabla.equals(cod)) {
                            productoExistente = true;
                            JOptionPane.showMessageDialog(null, "El Producto ya Está Registrado");
                            break;
                        }
                    }

                    if (!productoExistente) {
                        item = item + 1;
                        ArrayList<Object> lista = new ArrayList<>();
                        lista.add(item);
                        lista.add(cod);
                        lista.add(nombre);
                        lista.add(descripcion);
                        lista.add(cantidad);
                        lista.add(precio);
                        lista.add(total);

                        Object[] O = new Object[6];
                        O[0] = lista.get(1);
                        O[1] = lista.get(2);
                        O[2] = lista.get(3);
                        O[3] = lista.get(4);
                        O[4] = lista.get(5);
                        O[5] = lista.get(6);

                        tap.addRow(O);
                        TablaVenta.setModel(tap);
                        TotalPagar();
                        LimpiarVenta();
                        txt_VentaCodigoProducto.requestFocus();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Stock NO Disponible");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Ingrese Cantidad");
            }
        }
    }//GEN-LAST:event_txt_VentaCantidadProductoKeyPressed

    private void btn_EliminarCompraProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_EliminarCompraProductoActionPerformed
        modelo = (DefaultTableModel) TablaVenta.getModel();
        modelo.removeRow(TablaVenta.getSelectedRow());
        TotalPagar();
        txt_VentaCodigoProducto.requestFocus();
    }//GEN-LAST:event_btn_EliminarCompraProductoActionPerformed

    private void txt_ClienteVentaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_ClienteVentaKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (!"".equals(txt_ClienteVenta.getText())) {
                try {
                    int identificacion = Integer.parseInt(txt_ClienteVenta.getText());
                    cl = cliente.BuscarClienteVenta(identificacion);
                    if (cl.getNombre() != null) {
                        txt_NombreClienteVenta.setText("" + cl.getNombre());
                        txt_DirreccionClienteVenta.setText("" + cl.getDireccion());
                        txt_TelefonoClienteVenta.setText("" + cl.getTelefono());
                    } else {
                        txt_ClienteVenta.setText("");
                        JOptionPane.showMessageDialog(null, "El Cliente no Existe");
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Por favor, ingresa un número válido.");
                    txt_ClienteVenta.setText("");
                }
            }
        }
    }//GEN-LAST:event_txt_ClienteVentaKeyPressed

    private void txt_NombreClienteVentaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_NombreClienteVentaKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_NombreClienteVentaKeyPressed

    private void txt_DirreccionClienteVentaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_DirreccionClienteVentaKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_DirreccionClienteVentaKeyPressed

    private void txt_TelefonoClienteVentaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_TelefonoClienteVentaKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_TelefonoClienteVentaKeyPressed

    private void btn_CompraProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_CompraProductoActionPerformed

        if (TablaVenta.getRowCount() > 0) {
            if (!"".equals(txt_NombreClienteVenta.getText())) {
                RegistrarVenta();
                RegistrarDetalle();
                ActualizarStock();
                pdf();
                LimpiarTablaVenta();
                LimpiarClienteVenta();
            } else {
                JOptionPane.showMessageDialog(null, "Debes Ingresar un Cliente");
            }
        } else {
            JOptionPane.showMessageDialog(null, "No hay Productos en la Venta");
        }

    }//GEN-LAST:event_btn_CompraProductoActionPerformed

    private void txt_VentaIdKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_VentaIdKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_VentaIdKeyPressed

    private void btn_NuevaVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_NuevaVentaActionPerformed
        // Limpiar el JComboBox antes de actualizar
        cmb_NombreProductoVenta.removeAllItems();

        // Consultar productos y actualizar el JComboBox
        VDAO.consultarProducto(cmb_NombreProductoVenta);

        // Restablecer los campos de texto a sus valores predeterminados
        txt_VentaCodigoProducto.setText("0");
        txt_VentaNombreProducto.setText("");
        txt_VentaPrecioProducto.setText("0.0");
        txt_VentaStockProducto.setText("0");
        txt_VentaDescripcionProducto.setText("");
        txt_VentaCantidadProducto.setText("0");

        jTabbedPane1.setSelectedIndex(0);

        // Mover el foco al primer campo
        txt_VentaCodigoProducto.requestFocus();
    }//GEN-LAST:event_btn_NuevaVentaActionPerformed

    private void txt_VentaCantidadProductoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_VentaCantidadProductoKeyTyped
        event.numberKeyPress(evt);
    }//GEN-LAST:event_txt_VentaCantidadProductoKeyTyped

    private void txt_ClienteVentaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_ClienteVentaKeyTyped
        event.numberKeyPress(evt);
    }//GEN-LAST:event_txt_ClienteVentaKeyTyped

    private void btn_ModificarEmpresaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_ModificarEmpresaActionPerformed

        if (!"".equals(txt_NitEmpresa.getText().trim())
                && !"".equals(txt_NombreEmpresa.getText().trim())
                && !"".equals(txt_DireccionEmpresa.getText().trim())
                && !"".equals(txt_TelefonoEmpresa.getText().trim())
                && !"".equals(txt_CorreoEmpresa.getText().trim())) {

            dato.setNit(txt_NitEmpresa.getText().trim());
            dato.setNombre(txt_NombreEmpresa.getText().trim());
            dato.setDireccion(txt_DireccionEmpresa.getText().trim());
            dato.setTelefono(txt_TelefonoEmpresa.getText().trim());
            dato.setCorreo(txt_CorreoEmpresa.getText().trim());
            dato.setId(Integer.parseInt(txt_IdEmpresa.getText().trim()));
            prodao.modificarDatos(dato);
            JOptionPane.showMessageDialog(null, "Datos de la Empresa Modificado");
            ListarEmpresa();
        } else {
            JOptionPane.showMessageDialog(null, "Por favor, complete todos los campos.");
        }
    }//GEN-LAST:event_btn_ModificarEmpresaActionPerformed

    private void btn_DatosEmpresaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_DatosEmpresaActionPerformed
        jTabbedPane1.setSelectedIndex(6);
        ListarEmpresa();
    }//GEN-LAST:event_btn_DatosEmpresaActionPerformed

    private void btn_ReportesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_ReportesActionPerformed
        jTabbedPane1.setSelectedIndex(5);
        limpiarTabla();
        ListarVentas();
    }//GEN-LAST:event_btn_ReportesActionPerformed

    private void btn_AdministrarPerfilesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_AdministrarPerfilesActionPerformed
        limpiarTabla();
        ListarUsuarios();
        jTabbedPane1.setSelectedIndex(7);
    }//GEN-LAST:event_btn_AdministrarPerfilesActionPerformed

    private void TablaReportesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TablaReportesMouseClicked
        int fila = TablaReportes.rowAtPoint(evt.getPoint());
        txt_IdReportes.setText(TablaReportes.getValueAt(fila, 0).toString());
    }//GEN-LAST:event_TablaReportesMouseClicked

    private void btn_ReportePdfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_ReportePdfActionPerformed
        try {
            int id = Integer.parseInt(txt_IdReportes.getText());
            File file = new File("D:/Usuario/Downloads/Ventas Buhomar/venta" + id + ".pdf");
            Desktop.getDesktop().open(file);
        } catch (IOException ex) {
            Logger.getLogger(Sistema.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btn_ReportePdfActionPerformed

    private void btn_ReporteTortaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_ReporteTortaActionPerformed

        Date fecha = MiDate.getDate();

        if (fecha == null) {
            JOptionPane.showMessageDialog(this, "Por favor, selecciona una fecha.", "Error", JOptionPane.ERROR_MESSAGE);
            return; // Salir del método si la fecha es nula
        }

        String fechaReporte = new SimpleDateFormat("dd-MM-yyyy").format(MiDate.getDate());
        try {
            Grafico.Graficar(fechaReporte);
        } catch (IOException ex) {
        }

    }//GEN-LAST:event_btn_ReporteTortaActionPerformed

    private void btn_RegistrarUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_RegistrarUsuarioActionPerformed
        if (!"".equals(txt_IdentificacionUsuario.getText()) && !"".equals(txt_nombreCompletoUsuario.getText())
                && !"".equals(txt_nombreUsuario.getText()) && !"".equals(cmb_rol.getSelectedItem())
                && !"".equals(txt_direccionUsuario.getText()) && !"".equals(txt_telefonoUsuario.getText())
                && !"".equals(txt_correoUsuario.getText()) && !"".equals(txt_contraseñaUsuario.getPassword().length > 0)) {

            usu.setIdentificacion(Integer.parseInt(txt_IdentificacionUsuario.getText()));
            usu.setNombre(txt_nombreCompletoUsuario.getText());
            usu.setNombre_usuario(txt_nombreUsuario.getText());
            usu.setRol((String) cmb_rol.getSelectedItem());
            usu.setDireccion(txt_direccionUsuario.getText());
            usu.setTelefono(txt_telefonoUsuario.getText());
            usu.setCorreo(txt_correoUsuario.getText());
            // Convierte la contraseña a String (considera cifrar/hashear antes de almacenar)
            char[] passwordArray = txt_contraseñaUsuario.getPassword();
            String password = new String(passwordArray);
            Arrays.fill(passwordArray, ' '); // Limpiar el array después de su uso

            usu.setPass(password);

            usuDAO.registrarUsuarios(usu);

            limpiarTabla();
            LimpiarUsuario();
            ListarUsuarios();
            JOptionPane.showMessageDialog(null, "Usuario Registrado con Exito");

        } else {
            JOptionPane.showMessageDialog(null, "Los Campos Estan Vacíos");
        }
    }//GEN-LAST:event_btn_RegistrarUsuarioActionPerformed

    private void TablaUsuariosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TablaUsuariosMouseClicked
        int fila = TablaUsuarios.rowAtPoint(evt.getPoint());
        txt_IdUsuario.setText(TablaUsuarios.getValueAt(fila, 0).toString());
        txt_IdentificacionUsuario.setText(TablaUsuarios.getValueAt(fila, 1).toString());
        txt_nombreCompletoUsuario.setText(TablaUsuarios.getValueAt(fila, 2).toString());
        txt_nombreUsuario.setText(TablaUsuarios.getValueAt(fila, 3).toString());
        txt_direccionUsuario.setText(TablaUsuarios.getValueAt(fila, 4).toString());
        txt_telefonoUsuario.setText(TablaUsuarios.getValueAt(fila, 5).toString());
        txt_correoUsuario.setText(TablaUsuarios.getValueAt(fila, 6).toString());
        cmb_rol.setSelectedItem(TablaUsuarios.getValueAt(fila, 7).toString());
        txt_contraseñaUsuario.setText(TablaUsuarios.getValueAt(fila, 8).toString());
    }//GEN-LAST:event_TablaUsuariosMouseClicked

    private void btn_eliminarUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_eliminarUsuarioActionPerformed
        if (!"".equals(txt_IdUsuario.getText())) {
            int pregunta = JOptionPane.showConfirmDialog(null, "¿Estas Seguro de Eliminar Este Cliente?");
            if (pregunta == 0) {
                int id = Integer.parseInt(txt_IdUsuario.getText());
                usuDAO.eliminarUusario(id);
                limpiarTabla();
                LimpiarUsuario();
                ListarUsuarios();
            }
        }
    }//GEN-LAST:event_btn_eliminarUsuarioActionPerformed

    private void btn_modificarUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_modificarUsuarioActionPerformed
        if ("".equals(txt_IdUsuario.getText())) {
            JOptionPane.showMessageDialog(null, "Seleccione una fila");
        } else {
            if (!"".equals(txt_IdentificacionUsuario.getText())
                    && !"".equals(txt_nombreCompletoUsuario.getText())
                    && !"".equals(txt_nombreUsuario.getText())
                    && cmb_rol.getSelectedItem() != null
                    && !"".equals(txt_direccionUsuario.getText())
                    && !"".equals(txt_telefonoUsuario.getText())
                    && !"".equals(txt_correoUsuario.getText())
                    && txt_contraseñaUsuario.getPassword().length > 0) {

                usu.setId(Integer.parseInt(txt_IdUsuario.getText()));
                usu.setIdentificacion(Integer.parseInt(txt_IdentificacionUsuario.getText()));
                usu.setNombre(txt_nombreCompletoUsuario.getText());
                usu.setNombre_usuario(txt_nombreUsuario.getText());
                usu.setRol((String) cmb_rol.getSelectedItem());
                usu.setDireccion(txt_direccionUsuario.getText());
                usu.setTelefono(txt_telefonoUsuario.getText());
                usu.setCorreo(txt_correoUsuario.getText());
                usu.setPass(new String(txt_contraseñaUsuario.getPassword())); // Convierte char[] a String

                if (usuDAO.modificarUsuario(usu)) {
                    limpiarTabla();
                    LimpiarUsuario();
                    ListarUsuarios();
                    JOptionPane.showMessageDialog(null, "Usuario Modificado con Éxito");
                } else {
                    JOptionPane.showMessageDialog(null, "Error al modificar usuario");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Los Campos Están Vacíos");
            }
        }
    }//GEN-LAST:event_btn_modificarUsuarioActionPerformed

    private void btn_cancelarUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cancelarUsuarioActionPerformed
        LimpiarUsuario();
    }//GEN-LAST:event_btn_cancelarUsuarioActionPerformed

    private void txt_ClienteVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_ClienteVentaActionPerformed
        txt_ClienteVenta.setEnabled(true);
    }//GEN-LAST:event_txt_ClienteVentaActionPerformed

    private void txt_BuscarUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_BuscarUsuarioActionPerformed
        String criterio = txt_BuscarUsuario.getText().trim();

        if (!criterio.isEmpty()) {
            List<Usuarios> usuario = usuDAO.buscarUsuario(criterio);
            actualizarTablaUsuarios(usuario);
        } else {
            // Si el campo de búsqueda está vacío, puedes optar por mostrar todos los proveedores o limpiar la tabla
            List<Usuarios> usuario = usuDAO.listarUsuarios();
            actualizarTablaUsuarios(usuario);
        }
    }//GEN-LAST:event_txt_BuscarUsuarioActionPerformed

    private void txt_BuscarUsuarioKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_BuscarUsuarioKeyReleased
        txt_BuscarUsuarioActionPerformed(null);
    }//GEN-LAST:event_txt_BuscarUsuarioKeyReleased

    private void txt_VentaCodigoProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_VentaCodigoProductoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_VentaCodigoProductoActionPerformed

    private void cmb_CategoriaProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmb_CategoriaProductoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmb_CategoriaProductoActionPerformed

    private void btn_AgregarCompraProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_AgregarCompraProductoActionPerformed
        // Verificar que la cantidad no esté vacía
        if (!"".equals(txt_VentaCantidadProducto.getText())) {
            // Obtener los datos de los campos de texto
            String cod = txt_VentaCodigoProducto.getText();
            String nombre = txt_VentaNombreProducto.getText();
            String descripcion = txt_VentaDescripcionProducto.getText();
            int cantidad = Integer.parseInt(txt_VentaCantidadProducto.getText());
            double precio = Double.parseDouble(txt_VentaPrecioProducto.getText());
            double total = cantidad * precio;
            int stock = Integer.parseInt(txt_VentaStockProducto.getText());

            // Verificar el stock disponible
            if (stock >= cantidad) {
                item = item + 1;
                tap = (DefaultTableModel) TablaVenta.getModel();

                // Verificar si el producto ya está registrado en la tabla usando el código del producto
                for (int i = 0; i < TablaVenta.getRowCount(); i++) {
                    if (TablaVenta.getValueAt(i, 0).equals(cod)) {  // Verifica por código en lugar de nombre
                        JOptionPane.showMessageDialog(null, "El Producto ya Está Registrado");
                        return;
                    }
                }

                ArrayList<Object> lista = new ArrayList<>();
                lista.add(item);
                lista.add(cod);
                lista.add(nombre);
                lista.add(descripcion);
                lista.add(cantidad);
                lista.add(precio);
                lista.add(total);

                Object[] O = new Object[6];
                O[0] = lista.get(1); // Código del producto
                O[1] = lista.get(2); // Nombre del producto
                O[2] = lista.get(3); // Descripción del producto
                O[3] = lista.get(4); // Cantidad
                O[4] = lista.get(5); // Precio
                O[5] = lista.get(6); // Total

                tap.addRow(O);
                TablaVenta.setModel(tap);

                // Actualizar el total y limpiar la venta
                TotalPagar();
                LimpiarVenta();

                // Mover el foco al campo de código del producto
                txt_VentaCodigoProducto.requestFocus();
            } else {
                // Mostrar mensaje si el stock no es suficiente
                JOptionPane.showMessageDialog(null, "Stock NO Disponible");
            }
        } else {
            // Mostrar mensaje si no se ingresa una cantidad
            JOptionPane.showMessageDialog(null, "Ingrese Cantidad");
        }
    }//GEN-LAST:event_btn_AgregarCompraProductoActionPerformed

    private void cmb_NombreProductoVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmb_NombreProductoVentaActionPerformed
        // Obtener el nombre del producto seleccionado en el ComboBox
        String nombreProducto = (String) cmb_NombreProductoVenta.getSelectedItem();

        if (nombreProducto != null && !"".equals(nombreProducto)) {
            // Buscar el producto en la base de datos usando el nombre
            pro = prodao.BuscarNombreProVenta(nombreProducto);

            // Verificar si se encontró el producto
            if (pro != null) {
                // Llenar los campos con los datos del producto
                txt_VentaNombreProducto.setText("" + pro.getNombre());
                txt_VentaCodigoProducto.setText("" + pro.getCodigo());
                txt_VentaPrecioProducto.setText("" + pro.getPrecio());
                txt_VentaStockProducto.setText("" + pro.getStock());
                txt_VentaDescripcionProducto.setText("" + pro.getDescripcion());
                txt_VentaCantidadProducto.requestFocus(); // Foco en el campo de cantidad
            } else {
                // Si no se encuentra el producto, limpiar los campos
                LimpiarVenta();
            }
        } else {
            // Limpiar campos si el nombre del producto es nulo o vacío
            LimpiarVenta();
        }
    }//GEN-LAST:event_cmb_NombreProductoVentaActionPerformed

    private void txt_VentaCantidadProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_VentaCantidadProductoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_VentaCantidadProductoActionPerformed

    private void btn_CancelarCompraProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_CancelarCompraProductoActionPerformed
        LimpiarVenta();
    }//GEN-LAST:event_btn_CancelarCompraProductoActionPerformed

    private void TablaVentaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TablaVentaKeyPressed

    }//GEN-LAST:event_TablaVentaKeyPressed

    private void TablaVentaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TablaVentaMouseClicked
        int filaSeleccionada = TablaVenta.getSelectedRow(); // Obtén la fila seleccionada
        if (filaSeleccionada >= 0) {

            txt_VentaCodigoProducto.setText(TablaVenta.getValueAt(filaSeleccionada, 0).toString());
            txt_VentaNombreProducto.setText(TablaVenta.getValueAt(filaSeleccionada, 1).toString());
            txt_VentaDescripcionProducto.setText(TablaVenta.getValueAt(filaSeleccionada, 2).toString());
            txt_VentaCantidadProducto.setText(TablaVenta.getValueAt(filaSeleccionada, 3).toString());
            txt_VentaPrecioProducto.setText(TablaVenta.getValueAt(filaSeleccionada, 4).toString());
        } else {
            JOptionPane.showMessageDialog(null, "Selecciona una fila de la tabla");
        }
    }//GEN-LAST:event_TablaVentaMouseClicked

    private void btn_NuevaVentaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_NuevaVentaMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_NuevaVentaMouseClicked

    private void txt_VentaStockProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_VentaStockProductoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_VentaStockProductoActionPerformed

    private void actualizarTablaProveedores(List<Proveedor> proveedores) {
        DefaultTableModel model = (DefaultTableModel) TablaProveedor.getModel();
        model.setRowCount(0); // Limpiar la tabla

        for (Proveedor pr : proveedores) {
            Object[] row = new Object[7];
            row[0] = pr.getId();
            row[1] = pr.getIdentificacion();
            row[2] = pr.getNombre();
            row[3] = pr.getDireccion();
            row[4] = pr.getTelefono();
            row[5] = pr.getCorreo();
            row[6] = pr.getDescripcion();
            model.addRow(row);
        }
    }

    private void actualizarTablaClientes(List<Cliente> clientes) {
        DefaultTableModel model = (DefaultTableModel) TablaClientes.getModel();
        model.setRowCount(0); // Limpiar la tabla

        for (Cliente cl : clientes) {
            Object[] row = new Object[6];
            row[0] = cl.getId();
            row[1] = cl.getIdentificacion();
            row[2] = cl.getNombre();
            row[3] = cl.getDireccion();
            row[4] = cl.getTelefono();
            row[5] = cl.getCorreo();
            model.addRow(row);
        }
    }

    public void actualizarTablaUsuarios(List<Usuarios> usuario) {
        DefaultTableModel modelo = (DefaultTableModel) TablaUsuarios.getModel();
        modelo.setRowCount(0); // Limpia la tabla

        for (Usuarios us : usuario) {
            Object[] row = new Object[9];
            row[0] = us.getId();
            row[1] = us.getIdentificacion();
            row[2] = us.getNombre();
            row[3] = us.getNombre_usuario();
            row[4] = us.getDireccion();
            row[5] = us.getTelefono();
            row[6] = us.getCorreo();
            row[7] = us.getRol();
            row[8] = us.getPass();

            modelo.addRow(row);
        }
    }

    private void actualizarTablaProductos(List<Productos> productos) {
        DefaultTableModel model = (DefaultTableModel) Tabla_Producto.getModel();
        model.setRowCount(0);

        for (Productos pro : productos) {
            Object[] row = new Object[7];
            row[0] = pro.getId();
            row[1] = pro.getCodigo();
            row[2] = pro.getNombre();
            row[3] = pro.getStock();
            row[4] = pro.getCategoria();
            row[5] = pro.getDescripcion();
            row[6] = pro.getPrecio();
            model.addRow(row);
        }
    }

    private void actualizarTablaCategorias(List<Categorias> categorias) {
        DefaultTableModel model = (DefaultTableModel) TablaCategoria.getModel();
        model.setRowCount(0);

        for (Categorias cat : categorias) {
            Object[] row = new Object[2];
            row[0] = cat.getId();
            row[1] = cat.getNombre();
            model.addRow(row);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Sistema.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Sistema.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Sistema.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Sistema.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Sistema().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel LabelTotal;
    private com.toedter.calendar.JDateChooser MiDate;
    public javax.swing.JTable TablaCategoria;
    public javax.swing.JTable TablaClientes;
    public javax.swing.JTable TablaProveedor;
    public javax.swing.JTable TablaReportes;
    public javax.swing.JTable TablaUsuarios;
    public javax.swing.JTable TablaVenta;
    public javax.swing.JTable Tabla_Producto;
    public javax.swing.JButton btn_ActualizarProducto;
    private javax.swing.JButton btn_AdministrarPerfiles;
    public javax.swing.JButton btn_AgregarCompraProducto;
    public javax.swing.JButton btn_CancelarCategoria;
    public javax.swing.JButton btn_CancelarCliente;
    public javax.swing.JButton btn_CancelarCompraProducto;
    public javax.swing.JButton btn_CancelarProducto;
    public javax.swing.JButton btn_CancelarProveedor;
    private javax.swing.JButton btn_Categorias;
    private javax.swing.JButton btn_Clientes;
    public javax.swing.JButton btn_CompraProducto;
    private javax.swing.JButton btn_DatosEmpresa;
    public javax.swing.JButton btn_EliminarCategoria;
    public javax.swing.JButton btn_EliminarCliente;
    public javax.swing.JButton btn_EliminarCompraProducto;
    public javax.swing.JButton btn_EliminarProducto;
    public javax.swing.JButton btn_EliminarProveedor;
    public javax.swing.JButton btn_Foto;
    public javax.swing.JButton btn_ModificarCategoria;
    public javax.swing.JButton btn_ModificarCliente;
    public javax.swing.JButton btn_ModificarEmpresa;
    public javax.swing.JButton btn_ModificarProveedor;
    private javax.swing.JButton btn_NuevaVenta;
    private javax.swing.JButton btn_Productos;
    private javax.swing.JButton btn_Proveedor;
    public javax.swing.JButton btn_RegistrarCategoria;
    public javax.swing.JButton btn_RegistrarCliente;
    public javax.swing.JButton btn_RegistrarProducto;
    public javax.swing.JButton btn_RegistrarProveedor;
    public javax.swing.JButton btn_RegistrarUsuario;
    private javax.swing.JButton btn_ReportePdf;
    private javax.swing.JButton btn_ReporteTorta;
    private javax.swing.JButton btn_Reportes;
    private javax.swing.JButton btn_Salir;
    public javax.swing.JButton btn_cancelarUsuario;
    public javax.swing.JButton btn_eliminarUsuario;
    public javax.swing.JButton btn_modificarUsuario;
    private javax.swing.JComboBox<String> cmb_CategoriaProducto;
    private javax.swing.JComboBox<String> cmb_NombreProductoVenta;
    public javax.swing.JComboBox<String> cmb_rol;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    public javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel65;
    private javax.swing.JLabel jLabel66;
    private javax.swing.JLabel jLabel67;
    private javax.swing.JLabel jLabel68;
    private javax.swing.JLabel jLabel69;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel70;
    private javax.swing.JLabel jLabel71;
    private javax.swing.JLabel jLabel72;
    private javax.swing.JLabel jLabel73;
    private javax.swing.JLabel jLabel74;
    private javax.swing.JLabel jLabel75;
    private javax.swing.JLabel jLabel76;
    private javax.swing.JLabel jLabel77;
    private javax.swing.JLabel jLabel78;
    private javax.swing.JLabel jLabel79;
    private javax.swing.JLabel jLabel80;
    private javax.swing.JLabel jLabel81;
    private javax.swing.JLabel jLabel84;
    private javax.swing.JLabel jLabel85;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    public javax.swing.JPanel jPanelCerrarSesion;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    public javax.swing.JTabbedPane jTabbedPane1;
    public javax.swing.JLabel label_Rol;
    public javax.swing.JLabel label_nombrePerfil;
    private javax.swing.JPanel panelTapar;
    public javax.swing.JTextField txt_BuscarCategoria;
    public javax.swing.JTextField txt_BuscarCliente;
    public javax.swing.JTextField txt_BuscarProducto;
    public javax.swing.JTextField txt_BuscarProveedor;
    public javax.swing.JTextField txt_BuscarUsuario;
    public javax.swing.JTextField txt_Cantidades;
    public javax.swing.JTextField txt_ClienteVenta;
    public javax.swing.JTextField txt_CorreoCliente;
    public javax.swing.JTextField txt_CorreoEmpresa;
    public javax.swing.JTextField txt_CorreoProveedor;
    public javax.swing.JTextField txt_DescripcionProducto;
    public javax.swing.JTextField txt_DescripcionProveedor;
    public javax.swing.JTextField txt_DireccionCliente;
    public javax.swing.JTextField txt_DireccionEmpresa;
    public javax.swing.JTextField txt_DireccionProveedor;
    public javax.swing.JTextField txt_DirreccionClienteVenta;
    public javax.swing.JTextField txt_IDCliente;
    private javax.swing.JTextField txt_IDProveedor;
    public javax.swing.JTextField txt_IdCategoria;
    private javax.swing.JTextField txt_IdEmpresa;
    public javax.swing.JTextField txt_IdProducto;
    private javax.swing.JTextField txt_IdReportes;
    private javax.swing.JTextField txt_IdUsuario;
    public javax.swing.JTextField txt_IdentificacionCliente;
    public javax.swing.JTextField txt_IdentificacionProveedor;
    public javax.swing.JTextField txt_IdentificacionUsuario;
    public javax.swing.JTextField txt_NitEmpresa;
    public javax.swing.JTextField txt_NombreCategoria;
    public javax.swing.JTextField txt_NombreCliente;
    public javax.swing.JTextField txt_NombreClienteVenta;
    public javax.swing.JTextField txt_NombreEmpresa;
    public javax.swing.JTextField txt_NombreProducto;
    public javax.swing.JTextField txt_NombreProveedor;
    public javax.swing.JTextField txt_PrecioVenta;
    public javax.swing.JTextField txt_ProductoCodigo;
    public javax.swing.JTextField txt_TelefonoCliente;
    public javax.swing.JTextField txt_TelefonoClienteVenta;
    public javax.swing.JTextField txt_TelefonoEmpresa;
    public javax.swing.JTextField txt_TelefonoProveedor;
    public javax.swing.JTextField txt_VentaCantidadProducto;
    public javax.swing.JTextField txt_VentaCodigoProducto;
    public javax.swing.JTextField txt_VentaDescripcionProducto;
    public javax.swing.JTextField txt_VentaId;
    public javax.swing.JTextField txt_VentaNombreProducto;
    public javax.swing.JTextField txt_VentaPrecioProducto;
    public javax.swing.JTextField txt_VentaStockProducto;
    public javax.swing.JPasswordField txt_contraseñaUsuario;
    public javax.swing.JTextField txt_correoUsuario;
    public javax.swing.JTextField txt_direccionUsuario;
    public javax.swing.JTextField txt_nombreCompletoUsuario;
    public javax.swing.JTextField txt_nombreUsuario;
    public javax.swing.JTextField txt_telefonoUsuario;
    // End of variables declaration//GEN-END:variables

    private void limpiarCliente() {
        txt_IDCliente.setText("");
        txt_IdentificacionCliente.setText("");
        txt_NombreCliente.setText("");
        txt_TelefonoCliente.setText("");
        txt_DireccionCliente.setText("");
        txt_CorreoCliente.setText("");
    }

    private void limpiarProveedor() {
        txt_IDProveedor.setText("");
        txt_IdentificacionProveedor.setText("");
        txt_NombreProveedor.setText("");
        txt_TelefonoProveedor.setText("");
        txt_CorreoProveedor.setText("");
        txt_DireccionProveedor.setText("");
        txt_DescripcionProveedor.setText("");
    }

    private void limpiarProducto() {
        txt_NombreProducto.setText("");
        txt_ProductoCodigo.setText("");
        txt_Cantidades.setText("");
        txt_DescripcionProducto.setText("");
        cmb_CategoriaProducto.setSelectedItem("");
        txt_PrecioVenta.setText("");
    }

    private void limpiarCategoria() {
        txt_NombreCategoria.setText("");
    }

    private void TotalPagar() {
        TotalPagar = 0.00;
        int numFila = TablaVenta.getRowCount();
        for (int i = 0; i < numFila; i++) {
            double calcular = Double.parseDouble(String.valueOf(TablaVenta.getModel().getValueAt(i, 5)));
            TotalPagar = TotalPagar + calcular;
        }
        // Formatear el valor total con separadores de miles y decimales
        NumberFormat formato = NumberFormat.getCurrencyInstance(new Locale("es", "CO"));
        LabelTotal.setText(formato.format(TotalPagar));
    }

    private void LimpiarVenta() {
        txt_VentaCodigoProducto.setText("");
        txt_VentaNombreProducto.setText("");
        txt_VentaDescripcionProducto.setText("");
        txt_VentaCantidadProducto.setText("");
        txt_VentaPrecioProducto.setText("");
        txt_VentaStockProducto.setText("");
        txt_VentaId.setText("");
    }

    private void RegistrarVenta() {
        String nombre = txt_NombreClienteVenta.getText();
        String vendedor = label_nombrePerfil.getText();
        double monto = TotalPagar;
        v.setNombre(nombre);
        v.setVendedor(vendedor);
        v.setTotal(monto);
        v.setFecha(fechaActual);
        VDAO.registrarVenta(v);
    }

    private void RegistrarDetalle() {
        int id = VDAO.IDVenta();

        for (int i = 0; i < TablaVenta.getRowCount(); i++) {
            String cod = TablaVenta.getValueAt(i, 0).toString();
            int cant = Integer.parseInt(TablaVenta.getValueAt(i, 3).toString());
            double precio = Double.parseDouble(TablaVenta.getValueAt(i, 4).toString());

            Dv.setCod_pro(cod);
            Dv.setCantidad(cant);
            Dv.setPrecio(precio);
            Dv.setId_venta(id); // Asegúrate de que este valor es correcto

            VDAO.registrarDetalle(Dv);
        }
    }

    private void ActualizarStock() {
        for (int i = 0; i < TablaVenta.getRowCount(); i++) {
            String codigo = TablaVenta.getValueAt(i, 0).toString();
            int cantidad = Integer.parseInt(TablaVenta.getValueAt(i, 3).toString());
            prodao.BuscarProVenta(codigo);
            int StockActual = pro.getStock() - cantidad;
            VDAO.actualizarStock(StockActual, codigo);
        }
    }

    private void LimpiarTablaVenta() {
        tap = (DefaultTableModel) TablaVenta.getModel();
        int fila = TablaVenta.getRowCount();
        for (int i = 0; i < fila; i++) {
            tap.removeRow(0);

        }
    }

    private void LimpiarClienteVenta() {
        txt_ClienteVenta.setText("");
        txt_NombreClienteVenta.setText("");
        txt_TelefonoClienteVenta.setText("");
        txt_DirreccionClienteVenta.setText("");
    }

    private void LimpiarUsuario() {
        txt_IdentificacionUsuario.setText("");
        txt_nombreCompletoUsuario.setText("");
        txt_nombreUsuario.setText("");
        txt_direccionUsuario.setText("");
        txt_telefonoUsuario.setText("");
        txt_correoUsuario.setText("");
        txt_contraseñaUsuario.setText("");
    }

    private void pdf() {
        try {
            int id = VDAO.IDVenta();
            String paquete = "D:/Usuario/Downloads/Ventas Buhomar";
            File directorio = new File(paquete);

            //Verificar si la carpeta "Ventas Buhomar", existe
            if (!directorio.exists()) {
                directorio.mkdirs();//Crea la carpeta

            }
            File file = new File(directorio, "venta" + id + ".pdf");
            FileOutputStream archivo = new FileOutputStream(file);
            Document doc = new Document();
            PdfWriter.getInstance(doc, archivo);
            doc.open();
            Image img = Image.getInstance("src/img/Ellipse 1_1.png");

            Paragraph fecha = new Paragraph();
            Font negrita = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD, new BaseColor(75, 0, 130));
            fecha.add(Chunk.NEWLINE);
            Date date = new Date();
            fecha.add("Factura: " + codigoVenta() + id + "\n" + "Fecha: " + new SimpleDateFormat("dd-MM-yyyy").format(date) + "\n\n");

            PdfPTable Encabezado = new PdfPTable(4);
            Encabezado.setWidthPercentage(100);
            Encabezado.getDefaultCell().setBorder(0);
            float[] ColumnaEncabezado = new float[]{20f, 30f, 70f, 40f};
            Encabezado.setWidths(ColumnaEncabezado);
            Encabezado.setHorizontalAlignment(Element.ALIGN_LEFT);

            Encabezado.addCell(img);

            String nit = txt_NitEmpresa.getText();
            String nom = txt_NombreEmpresa.getText();
            String tel = txt_TelefonoEmpresa.getText();
            String dir = txt_DireccionEmpresa.getText();
            String cor = txt_CorreoEmpresa.getText();

            Encabezado.addCell("");
            Encabezado.addCell("NIT: " + nit + "\nNombre: " + nom + "\nTelefono: " + tel + "\nDirección: " + dir + "\nCorreo: " + cor);
            Encabezado.addCell(fecha);
            doc.add(Encabezado);

            Paragraph cliente = new Paragraph();
            cliente.add(Chunk.NEWLINE);
            cliente.add("Datos del Cliente: " + "\n\n");
            doc.add(cliente);

            PdfPTable tablacli = new PdfPTable(4);
            tablacli.setWidthPercentage(100);
            tablacli.getDefaultCell().setBorder(0);
            float[] Columnacli = new float[]{30f, 60f, 30f, 40f};
            tablacli.setWidths(Columnacli);
            tablacli.setHorizontalAlignment(Element.ALIGN_LEFT);
            PdfPCell cl1 = new PdfPCell(new Phrase("Identificacion", negrita));
            PdfPCell cl2 = new PdfPCell(new Phrase("Nombre", negrita));
            PdfPCell cl3 = new PdfPCell(new Phrase("Telefono", negrita));
            PdfPCell cl4 = new PdfPCell(new Phrase("Direccion", negrita));
            cl1.setBorder(0);
            cl2.setBorder(0);
            cl3.setBorder(0);
            cl4.setBorder(0);
            tablacli.addCell(cl1);
            tablacli.addCell(cl2);
            tablacli.addCell(cl3);
            tablacli.addCell(cl4);
            tablacli.addCell(txt_ClienteVenta.getText());
            tablacli.addCell(txt_NombreClienteVenta.getText());
            tablacli.addCell(txt_TelefonoClienteVenta.getText());
            tablacli.addCell(txt_DirreccionClienteVenta.getText());

            doc.add(tablacli);

            Paragraph espacio = new Paragraph("\n"); // Esto añade un salto de línea
            doc.add(espacio);

            cliente.add("Productos Vendidos: " + "\n\n");
            //Productos 
            PdfPTable tablapro = new PdfPTable(4);
            tablapro.setWidthPercentage(100);
            tablapro.getDefaultCell().setBorder(0);
            float[] Columnapro = new float[]{10f, 50f, 15f, 20f};
            tablapro.setWidths(Columnapro);
            tablapro.setHorizontalAlignment(Element.ALIGN_LEFT);
            PdfPCell pro1 = new PdfPCell(new Phrase("Cantidad", negrita));
            PdfPCell pro2 = new PdfPCell(new Phrase("Descripcion", negrita));
            PdfPCell pro3 = new PdfPCell(new Phrase("Precio Unitario", negrita));
            PdfPCell pro4 = new PdfPCell(new Phrase("Precio Total", negrita));
            pro1.setBorder(0);
            pro2.setBorder(0);
            pro3.setBorder(0);
            pro4.setBorder(0);
            pro1.setBackgroundColor(new BaseColor(230, 230, 250));
            pro2.setBackgroundColor(new BaseColor(230, 230, 250));
            pro3.setBackgroundColor(new BaseColor(230, 230, 250));
            pro4.setBackgroundColor(new BaseColor(230, 230, 250));
            tablapro.addCell(pro1);
            tablapro.addCell(pro2);
            tablapro.addCell(pro3);
            tablapro.addCell(pro4);
            for (int i = 0; i < TablaVenta.getRowCount(); i++) {
                String producto = TablaVenta.getValueAt(i, 1).toString();
                String cantidad = TablaVenta.getValueAt(i, 3).toString();
                String precio = TablaVenta.getValueAt(i, 4).toString();
                String total = TablaVenta.getValueAt(i, 5).toString();
                tablapro.addCell(cantidad);
                tablapro.addCell(producto);
                tablapro.addCell(precio);
                tablapro.addCell(total);
            }
            doc.add(tablapro);

            Paragraph info = new Paragraph();
            info.add(Chunk.NEWLINE);
            info.add("Total a Pagar: " + TotalPagar);
            info.setAlignment(Element.ALIGN_RIGHT);
            doc.add(info);

            Paragraph firma = new Paragraph();
            firma.add(Chunk.NEWLINE);
            firma.add("Cancelación y Firma\n\n");
            firma.add("________________________________");
            firma.setAlignment(Element.ALIGN_CENTER);
            doc.add(firma);

            Paragraph mensaje = new Paragraph();
            mensaje.add(Chunk.NEWLINE);
            mensaje.add("¡Gracias por su Compra!\n\n");
            mensaje.setAlignment(Element.ALIGN_CENTER);
            doc.add(mensaje);

            doc.close();
            archivo.close();
            Desktop.getDesktop().open(file);
        } catch (DocumentException | IOException e) {
            System.out.println(e.toString());
        }
    }

    private String codigoVenta() {
        String codigo;

        // Obtener la hora actual
        LocalTime ahora = LocalTime.now();

        // Definir el formato de hora deseado
        DateTimeFormatter formatterHora = DateTimeFormatter.ofPattern("HHmm");

        // Formatear la hora
        String horaFormateada = ahora.format(formatterHora);

        // Obtener la fecha actual
        LocalDate fechaActual = LocalDate.now();

        // Definir el formato de fecha deseado (incluyendo el día)
        DateTimeFormatter formatterFecha = DateTimeFormatter.ofPattern("yyyyMMdd");

        // Formatear la fecha
        String fechaFormateada = fechaActual.format(formatterFecha);

        // Generar el código de venta
        codigo = "BH" + fechaFormateada + horaFormateada;

        return codigo;
    }
}
