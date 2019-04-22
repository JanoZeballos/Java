package tp;

import tp.servicio.UsuarioServicio;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import tp.modelo.Usuario;
import tp.servicio.ATMServicio;
import tp.vista.VentanaInicio1;

public class Aplicacion {

    private static UsuarioServicio usuarioServicio;
    private static ATMServicio maquina;
    
    public static void main(String[] args) {
        usuarioServicio = new UsuarioServicio();
        maquina = new ATMServicio();
        
        /*
        //crearUsuarios();
        for(Usuario usuario : usuarioServicio.listarTodos()){
            System.out.println(usuario.toString());
        }
        */
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                VentanaInicio1 ventana = new VentanaInicio1();
                ventana.setLocationRelativeTo(null);
                ventana.setDefaultCloseOperation(ventana.EXIT_ON_CLOSE);
                ventana.setMaquina(maquina);
                ventana.setVisible(true); 
            }
        });
    }

    /*
    private static void crearUsuarios() {
        List<Usuario> usuarios = new ArrayList<>();
        usuarios.add(new Usuario("Jano", "asd123", 1324));
        usuarios.add(new Usuario("Manuel", "asd124", 1234));
        usuarios.add(new Usuario("Lautaro", "asd125", 1432));
        usuarioServicio.insertarlista(usuarios);
    }
    */
    
}
