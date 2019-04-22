package tp.servicio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import tp.modelo.Usuario;

public class UsuarioServicio {

    private final String INSERT_STATEMENT = "INSERT INTO usuarios"
            + "(NIP_USUARIO,CLAVE_USUARIO,SALDO_USUARIO,SALDO_CAJERO) VALUES "
            + "(?,?,?,?)";

    private final String UPDATE_STATEMENT = "UPDATE usuarios "
            + " SET SALDO_USUARIO = ? "
            + " WHERE NIP_USUARIO = ?";
    
    private final String UPDATE_STATEMENT_CAJERO = "UPDATE usuarios "
            + " SET SALDO_CAJERO = ? ";

    private final String UPDATE_SALDO_STATEMENT = "UPDATE usuarios "
            + " SET SALDO_USUARIO = ? "
            + " WHERE NIP_USUARIO = ?";

    private final String SELECT_STATEMENT = "SELECT NIP_USUARIO, CLAVE_USUARIO, SALDO_USUARIO, SALDO_CAJERO FROM usuarios";

    private final String SELECT_BY_NIP_STATEMENT = "SELECT NIP_USUARIO, CLAVE_USUARIO, SALDO_USUARIO, SALDO_CAJERO "
            + " FROM usuarios"
            + " WHERE NIP_USUARIO = ? ";
    
    private final String SELECT_BY_SALDOCAJERO_STATEMENT = "SELECT SALDO_CAJERO"
            + " FROM usuarios"
            + " WHERE NIP_USUARIO = ? ";


    public void insertar(Usuario usuario) {

        Connection conexion = null;
        PreparedStatement preparedStmtInsercion = null;

        try {

            conexion = AdministradorDeConexiones.getConnection();
            preparedStmtInsercion = conexion.prepareStatement(INSERT_STATEMENT);
            preparedStmtInsercion.setString(1, usuario.getNIP());
            preparedStmtInsercion.setString(2, usuario.getClave());
            preparedStmtInsercion.setInt(3, usuario.getSaldo());
            preparedStmtInsercion.setInt(4, 10000);
            preparedStmtInsercion.execute();

        } catch (ReflectiveOperationException | SQLException ex) {
            Logger.getLogger(UsuarioServicio.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (preparedStmtInsercion != null) {
                try {
                    preparedStmtInsercion.close();
                    conexion.close();
                } catch (SQLException ex) {
                    Logger.getLogger(UsuarioServicio.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public void insertarlista(List<Usuario> usuarios) {

        Connection conexion = null;

        try {
            conexion = AdministradorDeConexiones.getConnection();
            conexion.setAutoCommit(false);

            for (Usuario usuario : usuarios) {
                insertar(usuario);
            }
            conexion.commit();
        } catch (ReflectiveOperationException | SQLException e) {
            try {
                conexion.rollback();
            } catch (SQLException ee) {
            }
        } finally {
            try {
                if (conexion != null) {
                    conexion.close();
                }
            } catch (SQLException e) {
            }
        }
    }

    public List<Usuario> listarTodos() {

        Connection conexion = null;
        Statement stmtConsulta = null;
        List<Usuario> usuarios = new ArrayList<>();

        try {
            conexion = AdministradorDeConexiones.getConnection();
            stmtConsulta = conexion.createStatement();
            ResultSet rs = stmtConsulta.executeQuery(SELECT_STATEMENT);
            while (rs.next()) {
                usuarios.add(new Usuario(
                        rs.getString("NIP_USUARIO"),
                        rs.getString("CLAVE_USUARIO"),
                        rs.getInt("SALDO_USUARIO"),
                        rs.getInt("SALDO_CAJERO")));
            }
        } catch (SQLException | ReflectiveOperationException ex) {
            Logger.getLogger(UsuarioServicio.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (conexion != null) {
                    conexion.close();
                    stmtConsulta.close();
                }
            } catch (SQLException e) {
            }
        }
        return usuarios;
    }

    public Usuario getUsuario(String NIP) throws ATMException {

        Connection conexion = null;
        PreparedStatement preparedStmtbuscar = null;
        Usuario usuario = null;

        try {
            conexion = AdministradorDeConexiones.getConnection();
            preparedStmtbuscar = conexion.prepareStatement(
                    SELECT_BY_NIP_STATEMENT);
            preparedStmtbuscar.setString(1, NIP);
            ResultSet rs = preparedStmtbuscar.executeQuery();

            if (rs.next()) {
                usuario = new Usuario(rs.getString("NIP_USUARIO"),
                        rs.getString("CLAVE_USUARIO"),
                        rs.getInt("SALDO_USUARIO"),
                        rs.getInt("SALDO_CAJERO"));
            } else {
                throw new ATMException("No existe usuario para el usuario: " + NIP);
            }
        } catch (SQLException | ReflectiveOperationException ex) {
            Logger.getLogger(UsuarioServicio.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (conexion != null) {
                    preparedStmtbuscar.close();
                    conexion.close();
                }
            } catch (SQLException e) {
            }
        }
        return usuario;
    }

    public Usuario retirarefectivo(Usuario usuario, int retirar) throws ATMException{

        //Chequear que el cajero tenga esa plata
        //Chequear que el USUARIO tenga esa plata
        //Restarle la plata al cajero y al usuario
        //Guardar el valor de plata final en la BDD
        Connection conexion = null;
        PreparedStatement preparedStmtActualizar = null;
        PreparedStatement preparedStmtActualizarsaldo = null;

        Integer saldoNuevo = null;
        if (usuario.getSaldo() > retirar) {
            saldoNuevo = usuario.getSaldo() - retirar;
            usuario.setSaldo(saldoNuevo);
        }else{
            throw new ATMException("No posee ese monto para retirar");
        }
        
        Integer saldoCajero = null;
        if(usuario.getSaldoCajero() > retirar){
            saldoCajero = usuario.getSaldoCajero() - retirar;
            usuario.setSaldoCajero(saldoCajero);
        }else{
            throw new ATMException("El cajero no posee el dinero suficiente para retirar");
        }
        if (saldoNuevo != null) {
            try {
                conexion = AdministradorDeConexiones.getConnection();
                preparedStmtActualizar = conexion.prepareStatement(UPDATE_STATEMENT);
                preparedStmtActualizar.setInt(1, saldoNuevo.intValue()); //SALDO ACTUAL
                preparedStmtActualizar.setString(2, usuario.getNIP()); //USUARIO CONECTADO
                preparedStmtActualizar.execute();
                
                preparedStmtActualizarsaldo = conexion.prepareStatement(UPDATE_STATEMENT_CAJERO);
                preparedStmtActualizarsaldo.setInt(1, saldoCajero.intValue());
                preparedStmtActualizarsaldo.execute();

            } catch (ReflectiveOperationException | SQLException ex) {
                Logger.getLogger(UsuarioServicio.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    if (conexion != null) {
                        preparedStmtActualizar.close();
                        preparedStmtActualizarsaldo.close();
                        conexion.close();
                    }
                } catch (SQLException e) {
                }
            }
        }
        return usuario;
    }

    public Usuario depositarefectivo(Usuario usuario, int depositar) {

        //Sumarle la plata al cajero y al usuario
        //Guardar el valor de plata final en la BDD
        Connection conexion = null;
        PreparedStatement preparedStmtActualizar = null;
        PreparedStatement preparedStmtActualizarsaldo = null;

        Integer saldoNuevo = null;
        saldoNuevo = usuario.getSaldo() + depositar;
        usuario.setSaldo(saldoNuevo);
        
        Integer saldoCajero = null;
        saldoCajero = usuario.getSaldoCajero() + depositar;
        usuario.setSaldoCajero(saldoCajero);

        try {

            conexion = AdministradorDeConexiones.getConnection();
            preparedStmtActualizar = conexion.prepareStatement(
                    UPDATE_SALDO_STATEMENT);
            preparedStmtActualizar.setInt(1, saldoNuevo.intValue()); //SALDO ACTUAL
            preparedStmtActualizar.setString(2, usuario.getNIP()); //USUARIO CONECTADO
            preparedStmtActualizar.execute();
            
            preparedStmtActualizarsaldo = conexion.prepareStatement(UPDATE_STATEMENT_CAJERO);
            preparedStmtActualizarsaldo.setInt(1, saldoCajero.intValue());
            preparedStmtActualizarsaldo.execute();

        } catch (ReflectiveOperationException | SQLException ex) {
            Logger.getLogger(UsuarioServicio.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (conexion != null) {
                    preparedStmtActualizar.close();
                    preparedStmtActualizarsaldo.close();
                    conexion.close();
                }
            } catch (SQLException e) {
            }
        }
        return usuario;
    }

    public Usuario versaldo(Usuario usuario) {

        Connection conexion = null;
        PreparedStatement stmtConsulta = null;

        try {

            conexion = AdministradorDeConexiones.getConnection();
            stmtConsulta = conexion.prepareStatement(SELECT_BY_NIP_STATEMENT);
            stmtConsulta.setString(1, usuario.getNIP());
            stmtConsulta.executeQuery();

        } catch (ReflectiveOperationException | SQLException ex) {
            Logger.getLogger(UsuarioServicio.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (conexion != null) {
                    conexion.close();
                    stmtConsulta.close();
                }
            } catch (SQLException e) {
            }
        }
        return usuario;
    }
    
        public int versaldocajero(Usuario usuario) throws ATMException{

        int saldoCajero = 0;
        Connection conexion = null;
        PreparedStatement stmtConsulta = null;

        try {

            conexion = AdministradorDeConexiones.getConnection();
            stmtConsulta = conexion.prepareStatement(SELECT_BY_SALDOCAJERO_STATEMENT);
            stmtConsulta.setString(1, usuario.getNIP());
            ResultSet rs = stmtConsulta.executeQuery();
            
            if (rs.next()) {
                saldoCajero = rs.getInt("SALDO_CAJERO");
            } else {
                throw new ATMException("El cajero se quedo sin saldo");
            }

        } catch (ReflectiveOperationException | SQLException ex) {
            Logger.getLogger(UsuarioServicio.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (conexion != null) {
                    conexion.close();
                    stmtConsulta.close();
                }
            } catch (SQLException e) {
            }
        }
        return saldoCajero;
    }

    boolean validarUsuario(Usuario usuario, String clave) {
        return usuario.getClave().equals(clave);
    }

}
