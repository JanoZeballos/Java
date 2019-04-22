/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tp.modelo;

import java.util.Objects;

/**
 *
 * @author Alumno
 */
public class Usuario {

    private String NIP;
    private String clave;
    private Integer saldo;
    private Integer saldoCajero;

    public Usuario(String NIP, String clave) {
        this.NIP = NIP;
        this.clave = clave;
    }

    public Usuario(String NIP, String clave, Integer saldo, Integer saldoCajero) {
        this(NIP, clave);
        this.saldo = saldo;
        this.saldoCajero = saldoCajero;
   }

    public String getNIP() {
        return NIP;
    }

    public void setNIP(String NIP) {
        this.NIP = NIP;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public Integer getSaldo() {
        return saldo;
    }
    
    public void setSaldoCajero(Integer saldoCajero){
        this.saldoCajero = saldoCajero;
    }
    
    public Integer getSaldoCajero(){
        return saldoCajero;
    }

    public void setSaldo(Integer saldo) {
        this.saldo = saldo;
    }

    @Override
    public String toString() {
        return "Usuario{" + "NIP=" + NIP + ", clave=" + clave + ", saldo=" + saldo + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.NIP);
        hash = 79 * hash + Objects.hashCode(this.clave);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Usuario other = (Usuario) obj;
        if (!Objects.equals(this.NIP, other.NIP)) {
            return false;
        }
        if (!Objects.equals(this.clave, other.clave)) {
            return false;
        }
        return true;
    }

    

}
