package banco;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.sql.Date;

public class BDMysql {
    /**
     * Metodo para obtener la conexion a la base de datos
     * @return devuelve la conexion a la bbdd si se conecta y null si ya no existe
     */
    public Connection connect() {
        // Establecer la conexión a la base de datos
        String url = "jdbc:mysql://localhost:3306/banco?serverTimezone=UTC";
        String user = "prog";
        String password = "prog";

        try {
        	Connection connection = DriverManager.getConnection(url, user, password);
        	
            return connection;
        } catch (SQLException e) {
        	return null;
        }
    }
    /**
     * Metodo para crear nuevo cliente
     * @param connection
     * @param dni
     * @param nombre
     * @param telefono
     * @param direccion
     * @return devuelve true si se ha creado y false si el dni ya existe en la base de datos
     */
    public boolean crearCliente(Connection connection,String dni, String nombre, String telefono, String direccion) {
        String sql = "INSERT INTO CLIENTES (dni, nombre, telefono, direccion) VALUES (?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, dni);
            statement.setString(2, nombre);
            statement.setString(3, telefono);
            statement.setString(4, direccion);
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
    /**
     * Metodo  para eliminar un cliente, primero comprueba si tiene alguna cuenta activa
     * @param connection
     * @param dni
     * @return true si se ha podido eliminar y false si tiene alguna cuenta activa
     */
    public boolean eliminarCliente(Connection connection,String dni) {
    	String sql = "SELECT COUNT(*) AS count FROM CUENTAS WHERE dni_cliente = ? and situacion = ?";
    	boolean cuentaActiva=false;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, dni);
            statement.setString(2, "activa");
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt("count");
                if(count==0) {
                	cuentaActiva=true;
                }
            }else {
            	cuentaActiva=false;
            }
        }catch (SQLException e) {
     		return false;
     	}
    	if(cuentaActiva==false) {
    		return false;
    	}else {
	    	String sql2 = "DELETE FROM " + "CLIENTES" + " WHERE dni = ?"; 	
	        try (PreparedStatement statement2 = connection.prepareStatement(sql2)) {
	        	statement2.setString(1, dni);
	            statement2.executeUpdate();
	            return true;
	        } catch (SQLException e) {
	            return false;
	        }
    	}
    }
    /**
     * Metodo que modifica los datos del cliente
     * @param connection
     * @param dni
     * @param nombre
     * @param telefono
     * @param direccion
     * @return devuelve true si se ha modificado y false si el cliente no existe
     */
    public boolean modificarCliente(Connection connection,String dni, String nombre, String telefono, String direccion) {
        String sql = "UPDATE CLIENTES SET nombre = ?, telefono = ?, direccion = ? WHERE dni = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, nombre);
            statement.setString(2, telefono);
            statement.setString(3, direccion);
            statement.setString(4, dni);
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
    /**
     * Metodo para comprobar que existe el cliente antes de crear uno nuevo
     * @param connection
     * @param dni
     * @return devuelve true si ya existe uno con su dni y false si no existe ninguno
     */
    public boolean existeCliente(Connection connection, String dni) {
        String sql = "SELECT COUNT(*) AS count FROM CLIENTES WHERE dni = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, dni);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int count = resultSet.getInt("count");
                return count > 0;
            }else {
            	return false;
            }
        } catch (SQLException e) {
            return false;
        }
    }
    /**
     * Metodo para crear cuenta bancaria
     * @param connection
     * @param dniCliente
     * @param situacion
     * @param saldo
     * @return	devuelve true si se ha creado y false si el dni del cliente no existe en la base de datos
     */
    public boolean crearCuenta(Connection connection,String dniCliente) {
        String sql = "INSERT INTO CUENTAS (dni_cliente, situacion, saldo) VALUES (?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, dniCliente);
            statement.setString(2, "activa");
            statement.setDouble(3, 0);
            statement.executeUpdate();

            return true;
        } catch (SQLException e) {
            return false;
        }
    }
    /**
     * Metodo que da de baja una cuenta
     * @param connection
     * @param numeroCuenta
     * @return true si se ha dado de baja y false si no existe esa cuenta
     */
    public boolean bajaCuenta(Connection connection,int numeroCuenta) {
    	String sql = "UPDATE CUENTAS SET situacion = ? WHERE numero_cuenta = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, "baja");
            statement.setInt(2, numeroCuenta);
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
    /**
     * Metodo que da de alta una cuenta
     * @param connection
     * @param numeroCuenta
     * @return true si se ha dado de alta y false si no existe esa cuenta
     */
    public boolean altaCuenta(Connection connection,int numeroCuenta) {
    	String sql = "UPDATE CUENTAS SET situacion = ? WHERE numero_cuenta = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, "activa");
            statement.setInt(2, numeroCuenta);
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
    /**
     * Metodo para ingresar dinero en la cuenta del banco
     * @param connection
     * @param numeroCuenta
     * @param importe
     * @param concepto
     * @return	devuelve 0 si se ha realizado correctamente, 1 si el numero de cuenta no existe y 2 si la cuenta está de baja
     */
    public int realizarIngreso(Connection connection,int numeroCuenta, double importe,String concepto) {
    	if(isActiva(connection,numeroCuenta)==false) {
    		return 2;
    	}else {
	        String sql = "UPDATE CUENTAS SET saldo = saldo + ? WHERE numero_cuenta = ?";
	
	        try (PreparedStatement statement = connection.prepareStatement(sql)) {
	            statement.setDouble(1, importe);
	            statement.setInt(2, numeroCuenta);
	            int rowsAffected = statement.executeUpdate();
	
	            if (rowsAffected > 0) {
	                if(concepto.equals("Transferencia")) {
                		registrarMovimiento(connection, numeroCuenta, importe, "transferencia recibida", numeroCuenta, concepto);
                	}else {
                		registrarMovimiento(connection,numeroCuenta, importe, "ingreso", numeroCuenta, concepto);
                	}
	                return 0;
	            } else {
	                return 1;
	            }
	        } catch (SQLException e) {
	            return 1;
	        }
        }
    }
    
    /**
     * Metodo para realizar una retirada de dinero
     * @param connection
     * @param numeroCuenta
     * @param importe
     * @return 0 si se ha realizado correctamente la salida, 1 si no hay suficiente saldo, 2 si no se ha encontrado al usuario , 3 si hay una excepcion sql y 4 si la cuenta está de baja
     */
    public int realizarSalida(Connection connection, int numeroCuenta, double importe, String concepto) {
    	if(isActiva(connection,numeroCuenta)==false) {
    		return 4;
    	}else {
	        String consultaSaldo = "SELECT saldo FROM CUENTAS WHERE numero_cuenta = ?";
	        String sql = "UPDATE CUENTAS SET saldo = saldo - ? WHERE numero_cuenta = ?";
	
	        try (PreparedStatement consultaSaldoStatement = connection.prepareStatement(consultaSaldo); 
	             PreparedStatement actualizacionSaldoStatement = connection.prepareStatement(sql)) {
	            consultaSaldoStatement.setInt(1, numeroCuenta);
	            ResultSet saldoResultSet = consultaSaldoStatement.executeQuery();
	
	            if (saldoResultSet.next()) {
	                double saldoActual = saldoResultSet.getDouble("saldo");
	
	                if (saldoActual >= importe) {
	                    actualizacionSaldoStatement.setDouble(1, importe);
	                    actualizacionSaldoStatement.setInt(2, numeroCuenta);
	                    int filasAfectadas = actualizacionSaldoStatement.executeUpdate();
	
	                    if (filasAfectadas > 0) {
	                    	if(concepto.equals("Transferencia")) {
	                    		registrarMovimiento(connection, numeroCuenta, importe, "transferencia enviada", numeroCuenta, concepto);
	                    	}else {
	                    		registrarMovimiento(connection, numeroCuenta, importe, "salida", numeroCuenta, concepto);
	                    	}
	                        return 0;
	                    }else {
	                    	return 1;
	                    }
	                } else {
	                    return 1;
	                }
	            } else {
	            	return 2;
	            }
	        } catch (SQLException e) {
	        	return 3;
	        }
    	}

    }
    /**
     * Metodo para realizar una transferencia
     * @param connection
     * @param numeroCuentaOrigen
     * @param numeroCuentaDestino
     * @param importe
     * @param concepto
     * @return devuelve 0 si se ha realizado corectamente la transferencia y 1 si no hay suficiente dinero en la cuenta origen, 2 si la cuenta origen está de baja y 3 si la cuenta destino está de baja
     */
    public int realizarTransferencia(Connection connection,int numeroCuentaOrigen, int numeroCuentaDestino, double importe, String concepto) {
    	if(isActiva(connection,numeroCuentaOrigen)==false) {
    		return 4;
    	}else {
    		if(isActiva(connection,numeroCuentaDestino)==false) {
        		return 4;
        	}else {
        		if(realizarSalida(connection,numeroCuentaOrigen,importe,"Transferencia")!=0) {
                	return 1;
                }else {
        	        realizarIngreso(connection,numeroCuentaDestino, importe, "Transferencia"+numeroCuentaOrigen);
        	        return 0;
                }
        	}
    	}
    }
    /**
     * Metodo con el cual se registran los movimientos
     * @param connection
     * @param numeroCuenta
     * @param importe
     * @param tipo
     * @param numeroCuentaTransferencia
     * @param concepto
     * @return devuelve true si se ha registrado y false si el numero de cuenta no existe
     */
    private boolean registrarMovimiento(Connection connection,int numeroCuenta, double importe, String tipo, int numeroCuentaTransferencia, String concepto) {
        String sql = "INSERT INTO MOVIMIENTOS (numero_cuenta, importe, fecha_hora, tipo, numero_cuenta_transferencia, concepto) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, numeroCuenta);
            statement.setDouble(2, importe);
            statement.setString(3, obtenerFechaHoraActual());
            statement.setString(4, tipo);
            statement.setInt(5, numeroCuentaTransferencia);
            statement.setString(6, concepto);
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
   
        }
    }
    /**
     * Metodo para obtener el saldo de una cuenta
     * @param connection
     * @param numeroCuenta
     * @return devuelve el saldo
     */
    public double obtenerSaldo(Connection connection,int numeroCuenta) {
        String sql = "SELECT saldo FROM CUENTAS WHERE numero_cuenta = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, numeroCuenta);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getDouble("saldo");
            } else {
            	return 0.0;
            }
        } catch (SQLException e) {
        	return 0.0;
        }
    }
    /**
     * Metodo para cerrar la conexion con la base de datos
     * @param connection
     * @return
     */
    public boolean cerrarConexion(Connection connection) {
        try {
            connection.close();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
    /**
     * Metodo para obtener la fecha actual
     * @return devuelve la fecha
     */
    private String obtenerFechaHoraActual() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return now.format(formatter);
    }
    
    /**
     * Metodo que dice si la cuenta está activa o no
     * @param connection
     * @param numeroCuenta
     * @return devulev true si la cuenta está activa y false si está de baja
     */
    public boolean isActiva(Connection connection,int numeroCuenta) {
    	String sql = "SELECT COUNT(*) AS count FROM CUENTAS WHERE numero_cuenta = ? and situacion = ?";
    	try(PreparedStatement statement = connection.prepareStatement(sql)){
    		statement.setInt(1,numeroCuenta);
    		statement.setString(2,"activa");
    		ResultSet resultSet = statement.executeQuery();
    		if (resultSet.next()) {
                int count = resultSet.getInt("count");
                return count > 0;
            }else {
            	return false;
            }
    	}catch (SQLException e) {
    		return false;
    	}
    }
    /**
     * Metodo que muestra todos los clientes de la base de datos
     * @param connection
     * @return devuelve un string con la lista
     */
    public String mostrarClientes(Connection connection) {
    	String sql = "SELECT * FROM CLIENTES";
    	String clientes="";
    	try(PreparedStatement statement = connection.prepareStatement(sql)){
    		ResultSet resultSet = statement.executeQuery();
    		while (resultSet.next()) {
                String dni = resultSet.getString("dni");
                String nombre = resultSet.getString("nombre");
                String telefono = resultSet.getString("telefono");
                String direccion = resultSet.getString("direccion");
                clientes+="Cliente [dni = "+dni+", nombre = "+nombre+",telefono = "+telefono+", direccion = "+direccion+"]\n";
            }
    		return clientes;
    	}catch (SQLException e) {
    		return null;
    	}
    }
    /**
     * Metodo que muestra todas las cuentas de un cliente en concreto
     * @param connection
     * @param dni
     * @return devuelve el String con la lista
     */
    public String mostrarCuentasCliente(Connection connection,String dni) {
    	String sql = "SELECT * FROM CUENTAS WHERE dni_cliente= ?";
    	String cuentas="";
    	try(PreparedStatement statement = connection.prepareStatement(sql)){
    		statement.setString(1, dni);
    		ResultSet resultSet = statement.executeQuery();
    		while (resultSet.next()) {
                int numero_cuenta = resultSet.getInt("numero_cuenta");
                String dni_cliente = resultSet.getString("dni_cliente");
                String situacion = resultSet.getString("situacion");
                double saldo = resultSet.getDouble("saldo");
                cuentas+="Cuenta [numero_cuenta = "+numero_cuenta+", dni_cliente = "+dni_cliente+",situacion = "+situacion+", saldo = "+saldo+"]\n";
            }
    		return cuentas;
    	}catch (SQLException e) {
    		return null;
    	}
    }
    /**
     * Metodo que muestra todos los movimientos de un cliente en concreto
     * @param connection
     * @param dni
     * @return devulve la lista de esos movimientos
     */
    public String mostrarMovimientoCliente(Connection connection,int numCuenta,Date fechaInicio,Date fechaFin) {
    	String sql = "SELECT * FROM MOVIMIENTOS WHERE numero_cuenta = ? and fecha_hora >= ? and fecha_hora <= ?";
    	String movimientos="";
    	try(PreparedStatement statement = connection.prepareStatement(sql)){
    		statement.setInt(1, numCuenta);
    		statement.setDate(2, fechaInicio);
    		statement.setDate(3, fechaFin);
    		ResultSet resultSet = statement.executeQuery();
    		while (resultSet.next()) {
                int numero_cuenta = resultSet.getInt("numero_cuenta");
                double importe = resultSet.getDouble("importe");
                Date fecha = resultSet.getDate("fecha_hora");
                String tipo = resultSet.getString("tipo");
                int numero_cuenta_transferencia = resultSet.getInt("numero_cuenta_transferencia");
                String concepto = resultSet.getString("concepto");
                if(numero_cuenta==numero_cuenta_transferencia) {
                    movimientos+="Movimiento [numero_cuenta = "+numero_cuenta+", importe = "+importe+", fecha = "+fecha+", tipo = "+tipo+", concepto = "+concepto+"]\n";
                }else {
                	movimientos+="Movimiento [numero_cuenta = "+numero_cuenta+", importe = "+importe+", fecha = "+fecha+", tipo = "+tipo+", numero_cuenta_transferencia = "+numero_cuenta_transferencia+", concepto = "+concepto+"]\n";
                }
            }
    		return movimientos;
    	}catch (SQLException e) {
    		return null;
    	}
    }
}
