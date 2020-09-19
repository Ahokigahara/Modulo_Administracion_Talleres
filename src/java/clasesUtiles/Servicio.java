/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clasesUtiles;

/**
 *
 * @author david
 */
public class Servicio {
    
    private int grupo;
    private String nombre;
    private Long telefono;

    public Servicio() {
    }

    public Servicio(int grupo, String nombre, Long telefono) {
        this.grupo = grupo;
        this.nombre = nombre;
        this.telefono = telefono;
    }

    public int getGrupo() {
        return grupo;
    }

    public void setGrupo(int grupo) {
        this.grupo = grupo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Long getTelefono() {
        return telefono;
    }

    public void setTelefono(Long telefono) {
        this.telefono = telefono;
    }

    
    
    
}
