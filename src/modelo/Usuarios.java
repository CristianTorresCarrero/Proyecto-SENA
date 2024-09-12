
package modelo;

public class Usuarios {
    private int id;
    private int identificacion;
    private String nombre;
    private String nombre_usuario;
    private String rol;
    private String direccion;
    private String telefono;
    private String correo;
    private String pass;

    public Usuarios() {
    }

    public Usuarios(int id, int identificacion, String nombre, String nombre_usuario, String rol, String direccion, String telefono, String correo, String pass) {
        this.id = id;
        this.identificacion = identificacion;
        this.nombre = nombre;
        this.nombre_usuario = nombre_usuario;
        this.rol = rol;
        this.direccion = direccion;
        this.telefono = telefono;
        this.correo = correo;
        this.pass = pass;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(int identificacion) {
        this.identificacion = identificacion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre_usuario() {
        return nombre_usuario;
    }

    public void setNombre_usuario(String nombre_usuario) {
        this.nombre_usuario = nombre_usuario;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
    
    
    
}
