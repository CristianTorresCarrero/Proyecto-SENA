package Reportes;

import java.awt.Color;
import java.awt.TexturePaint;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.imageio.ImageIO;
import modelo.Conexion;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

public class Grafico {
    public static void Graficar(String fecha) throws IOException {
        Connection conn;
        Conexion cn = new Conexion();
        PreparedStatement ps;
        ResultSet rs;
        try {
            // Consulta para obtener el nombre del producto y la cantidad vendida
            String sql = "SELECT p.nombre AS nombre_producto, SUM(dv.cantidad) AS cantidad_vendida " +
                         "FROM detallle dv " +
                         "JOIN ventas v ON dv.id_venta = v.id " +
                         "JOIN productos p ON dv.cod_pro = p.codigo " +
                         "WHERE v.fecha = ? " +
                         "GROUP BY p.nombre";
            conn = cn.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, fecha);
            rs = ps.executeQuery();
            
            // Crear el dataset para el diagrama de barras
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            while(rs.next()){
                String nombreProducto = rs.getString("nombre_producto");
                double cantidadVendida = rs.getDouble("cantidad_vendida");
                dataset.addValue(cantidadVendida, "Cantidad Vendida", nombreProducto);
            }
            
            // Crear el gráfico de barras
            JFreeChart chart = ChartFactory.createBarChart(
                "Reporte de Ventas por Producto", // Título del gráfico
                "Productos", // Etiqueta de categoría (eje X)
                "Cantidad Vendida", // Etiqueta de valor (eje Y)
                dataset // Datos del gráfico
            );
            
            // Configurar el formato del eje Y para que muestre números enteros
            CategoryPlot plot = chart.getCategoryPlot();
            NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
            rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

            // Configurar el color de las barras
            BarRenderer renderer = new BarRenderer();
            Color customColor = new Color(129, 1, 137); // Color personalizado
            renderer.setSeriesPaint(0, customColor);
            plot.setRenderer(renderer);
            
            // Configurar el fondo de la gráfica y del área del plot
            Color backgroundColor = new Color(230, 230, 250); // Morado oscuro para el fondo
            chart.setBackgroundPaint(backgroundColor); // Fondo del gráfico
            plot.setBackgroundPaint(backgroundColor); // Fondo del área del plot
            
            // Mostrar el gráfico en un frame
            ChartFrame frame = new ChartFrame("Reporte de Ventas", chart);
            frame.setSize(1000, 600);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
    }
}
