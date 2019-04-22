/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tp.servicio;

/**
 *
 * @author Alumno
 */
public class ATMException extends Exception {

    private String mensaje;
    public ATMException(String elMensaje) {
        mensaje = elMensaje;
    }

    public String getMensaje() {
        return mensaje;
    }
    
}
