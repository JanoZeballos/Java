/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tp.modelo;

/**
 *
 * @author Alumno
 */
public class ATM {
    
    Integer saldo;
    
    public ATM (){
    }

    public void setSaldo(int saldo) {
        this.saldo = saldo;
    }

    @Override
    public String toString() {
        return "ATM{" + "saldo=" + saldo + '}';
    }
    
}
