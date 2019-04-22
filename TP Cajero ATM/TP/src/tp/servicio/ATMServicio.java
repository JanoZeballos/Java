/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tp.servicio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import tp.modelo.ATM;
import tp.modelo.Usuario;

/**
 *
 * @author Alumno
 */
public class ATMServicio {

    private UsuarioServicio usuarioServicio;
    private Usuario usuario;
    private ATM maquina;

    public ATMServicio() {
        this.maquina = new ATM();
        this.usuarioServicio = new UsuarioServicio();
    }

    public Usuario obtenerUsuario(String NIP, String clave) throws ATMException {
        usuario = usuarioServicio.getUsuario(NIP);
        if (usuarioServicio.validarUsuario(usuario, clave)) {
            return usuario;
        } else {
            throw new ATMException("Usuario o password no válidos");
        }

    }

    public void retirar(Integer monto) throws ATMException {
        if(monto > usuario.getSaldoCajero()){
            throw new ATMException("El cajero no posee ese monto para retirar");
        }
        if(monto < usuario.getSaldo()){
            usuarioServicio.retirarefectivo(usuario, monto);
        }else{
            throw new ATMException("No posee ese monto para retirar");
        }
    }

    public void depositar(Integer monto) {
        usuarioServicio.depositarefectivo(usuario, monto);

    }

    public String verSaldo() {
        return usuario.getSaldo().toString();
    }
    
    public String verSaldoCajero(){
        return usuario.getSaldoCajero().toString();
    }

}
