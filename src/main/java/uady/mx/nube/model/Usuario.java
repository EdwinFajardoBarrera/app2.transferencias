package uady.mx.nube.model;

import javax.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "Usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "secret")
    @JsonIgnore
    private String secret;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    @JsonIgnore
    private String password;

    @OneToOne
    @JoinColumn(name = "id_cuenta", referencedColumnName = "id")
    private Cuenta cuenta;

    public Usuario() {
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return this.password;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getSecret() {
        return this.secret;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return this.email;
    }

    public Cuenta getCuenta() {
        return cuenta;
    }

    public void setCuenta(Cuenta idCuenta) {
        this.cuenta = idCuenta;
    }

    @Override
    public String toString() {
        return "{" + " nombre='" + getNombre() + "'" + " email='" + getEmail() + "'" + " idCuenta='" + getCuenta()
                + "}";
    }
}