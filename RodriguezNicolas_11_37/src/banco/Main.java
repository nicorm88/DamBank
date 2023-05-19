package banco;

import java.sql.Connection;
import java.sql.Date;
import java.util.Calendar;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 *
 * @author Nicolas 1ºDAM
 * 
 */
public class Main {

	public static void main(String[] args) {
		BDMysql gestionBancaria = new BDMysql();
		Connection connection=gestionBancaria.connect();

		boolean done=appGestion(gestionBancaria,connection);
		if(done==true) {
			System.out.println("Cerrando conexion con mysql...");
			System.out.println("OK!!");
		}else {
			System.out.println("Error en el sistema perdona las molestias");
		}

	}
	public static int gestionCuentas(BDMysql gestionBancaria,Connection connection){
		Scanner input=new Scanner(System.in);
		boolean flag=true;
		int menu=0;
		System.out.println("ELIJA OPERACION(0 PARA SALIR):"
				+ "\n1-Ingresar"
				+ "\n2-Retirar"
				+ "\n3-Realizar transferencia"
				+ "\n4-Dar de alta una cuenta o modificar tipo cuenta"
				+ "\n5-Dar de baja una cuenta"
				+ "\n6-Mostrar cuentas de un cliente");
		while(flag) {
			try {
				flag=false;
				menu=input.nextInt();
			}catch(InputMismatchException e) {
				System.out.println("Introdujo mal el numero");
				flag=true;
			}
		}
		return menu;
	}
	public static int ingresarDinero(BDMysql gestionBancaria,Connection connection) {
		Scanner input=new Scanner(System.in);
		double importe = 0;
		int numCuenta = 0;
		String concepto;
		System.out.println("Dime el numero de cuenta al que quieres realizar el ingreso");
		boolean flag=true;
		while(flag) {
			try {
				flag=false;
				numCuenta=input.nextInt();
				input.nextLine();
			}catch(InputMismatchException e) {
				System.out.println("Introdujo mal el numero");
				input.nextLine();
				flag=true;
			}
		}
		flag=true;
		System.out.println("Dime el importe a ingresar");
		while(flag) {
			try {
				flag=false;
				importe=input.nextDouble();
				input.nextLine();
			}catch(InputMismatchException e) {
				System.out.println("Introdujo mal el numero");
				input.nextLine();
				flag=true;
			}
		}
		System.out.println("Dime le concepto del ingreso");
		concepto=input.nextLine();

		int resultado=gestionBancaria.realizarIngreso(connection, numCuenta, importe, concepto);
		if(resultado==0) {
			return 0;
		}else {
			if(resultado==1) {
				return 1;
			}else {
				return 2;
			}
		}
	}
	public static int retirarDinero(BDMysql gestionBancaria,Connection connection) {
		Scanner input=new Scanner(System.in);
		double importe = 0;
		int numCuenta = 0;
		String concepto;
		System.out.println("Dime el numero de cuenta al que quieres realizar el ingreso");
		boolean flag=true;
		while(flag) {
			try {
				flag=false;
				numCuenta=input.nextInt();
			}catch(InputMismatchException e) {
				System.out.println("Introdujo mal el numero");
				flag=true;
			}
		}
		flag=true;
		System.out.println("Dime el importe a retirar");
		while(flag) {
			try {
				flag=false;
				importe=input.nextDouble();
			}catch(InputMismatchException e) {
				System.out.println("Introdujo mal el numero");
				flag=true;
			}
		}
		input.nextLine();
		System.out.println("Dime le concepto del ingreso");
		concepto=input.nextLine();

		int resultado=gestionBancaria.realizarSalida(connection, numCuenta, importe, concepto);
		if(resultado==0) {
			return 0;
		}else {
			if(resultado==1) {
				return 1;
			}else {
				if(resultado==2) {
					return 2;
				}else {
					return 3;
				}
			}
		}
	}
	public static int hacerTransferencia(BDMysql gestionBancaria,Connection connection) {
		Scanner input=new Scanner(System.in);
		double importe;
		int numCuentaOrigen = 0,numCuentaDestino = 0;
		String concepto;
		System.out.println("Dime el numero de cuenta con el que vas a hacer la trasferencia");
		boolean flag=true;
		while(flag) {
			try {
				flag=false;
				numCuentaOrigen=input.nextInt();
			}catch(InputMismatchException e) {
				System.out.println("Introdujo mal el numero");
				flag=true;
			}
		}
		flag=true;
		System.out.println("Dime el numero de cuenta al que vas a hacer la trasferencia");
		while(flag) {
			try {
				flag=false;
				numCuentaDestino=input.nextInt();
			}catch(InputMismatchException e) {
				System.out.println("Introdujo mal el numero");
				flag=true;
			}
		}
		System.out.println("Dime el importe a ingresar");
		importe=input.nextDouble();
		input.nextLine();
		System.out.println("Dime le concepto del ingreso");
		concepto=input.nextLine();

		int resultado=gestionBancaria.realizarTransferencia(connection, numCuentaOrigen, numCuentaDestino, importe, concepto);
		if(resultado==0) {
			return 0;
		}else {
			if(resultado==1) {
				return 1;
			}else {
				if(resultado==2) {
					return 2;
				}else {
					return 3;
				}
			}
		}
	}
	public static int gestionClientes(){
		Scanner input=new Scanner(System.in);
		boolean flag=true;
		int menu=0;
		System.out.println("ELIJA OPERACION(0 PARA SALIR):"
				+ "\n1-Modificar cliente"
				+ "\n2-Alta cliente"
				+ "\n3-Dar de baja cliente");
		while(flag) {
			try {
				flag=false;
				menu=input.nextInt();
			}catch(InputMismatchException e) {
				System.out.println("Introdujo mal el numero");
				flag=true;
			}
		}
		return menu;
	}
	public static boolean existeCliente(BDMysql gestionBancaria,Connection connection,String dni) {
		if(gestionBancaria.existeCliente(connection, dni)==false) {
			return false;
		}else {
			return true;
		}
	}
	public static boolean altaCliente(BDMysql gestionBancaria,Connection connection) {
		Scanner input=new Scanner(System.in);
		String dni,nombre,telefono,direccion;
		System.out.println("Digame su dni:");
		dni=input.nextLine();
		System.out.println("Digame su nombre:");
		nombre=input.nextLine();
		System.out.println("Digame su telefono:");
		telefono=input.nextLine();
		System.out.println("Digame su direccion:");
		direccion=input.nextLine();
		if(gestionBancaria.crearCliente(connection, dni, nombre, telefono, direccion)==false) {
			return false;
		}else {
			return true;
		}
	}
	public static int menuPrincipal() {
		Scanner input=new Scanner(System.in);
		boolean flag=true;
		int menu=0;
		System.out.println("ELIJA OPERACION(0 PARA SALIR):"
				+ "\n1-Gestion de clientes"
				+ "\n2-Gestion de cuentas"
				+ "\n3-Gestion de movimientos"
				+ "\n4-Mostrar todos los clientes");
		while(flag) {
			try {
				flag=false;
				menu=input.nextInt();
			}catch(InputMismatchException e) {
				System.out.println("Introdujo mal el numero");
				flag=true;
			}
		}
		return menu;
	}
	public static boolean modificarCliente(BDMysql gestionBancaria,Connection connection) {
		Scanner input=new Scanner(System.in);
		String dni,nombre,telefono,direccion;
		System.out.println("Digame su dni:");
		dni=input.nextLine();
		System.out.println("Digame su nombre:");
		nombre=input.nextLine();
		System.out.println("Digame su telefono:");
		telefono=input.nextLine();
		System.out.println("Digame su direccion:");
		direccion=input.nextLine();
		if(gestionBancaria.modificarCliente(connection, dni, nombre, telefono, direccion)==false) {
			return false;
		}else {
			return true;
		}
	}
	public static boolean bajaCliente(BDMysql gestionBancaria,Connection connection) {
		Scanner input=new Scanner(System.in);
		String dni,nombre,telefono,direccion;
		System.out.println("Digame su dni:");
		dni=input.nextLine();
		if(gestionBancaria.eliminarCliente(connection, dni)==false) {
			return false;
		}else {
			return true;
		}
	}
	public static String pideDni(BDMysql gestionBancaria,Connection connection) {
		Scanner input=new Scanner (System.in);
		String dni;
		int menu=0;
		System.out.println("Dime el dni del cliente:");
		dni=input.nextLine();
		return dni;
	}
	public static boolean appGestion(BDMysql gestionBancaria, Connection connection) {
		int numMenuPrincipal,numMenuCliente,numMenuCuentas;
		String dni;
		do {
			numMenuPrincipal=menuPrincipal();
			switch(numMenuPrincipal) {
			case 0:{

				break;
			}
			case 1:{
				do {
					numMenuCliente=gestionClientes();
					switch(numMenuCliente) {
					case 0:{
						break;
					}
					case 1:{
						if(modificarCliente(gestionBancaria, connection)) {
							System.out.println("\nSe ha modificado correctamente\n");
						}else {
							System.out.println("\nEse cliente no existe\n");
						}
						break;
					}case 2:{
						if(altaCliente(gestionBancaria, connection)) {
							System.out.println("\nSe ha dado de alta correctamente el cliente\n");
						}else {
							System.out.println("\nEse dni ya está en uso");
						}
						break;
					}case 3:{
						if(bajaCliente(gestionBancaria, connection)) {
							System.out.println("\nSe ha eliminado correctamente\n");
						}else {
							System.out.println("\nEste cliente aun tiene alguna cuenta activa, tiene que darse de baja en todas sus cuentas\n");
						}
						break;
					}default:{
						System.err.println("\n\nNUMERO MAL INTRODUCIDO\n\n");
						break;
					}
					}
				}while(numMenuCliente!=0);
				break;
			}case 2:{
				do {
					numMenuCuentas=gestionCuentas(gestionBancaria, connection);
					switch(numMenuCuentas) {
					case 0:{
						break;
					}
					case 1:{
						System.out.println();
						int resultado=ingresarDinero(gestionBancaria, connection);
						if(resultado==0) {
							System.out.println("Se ha realizado el ingreso correctamente");
						}else {
							if(resultado==1) {
								System.out.println("El numero de cuenta no existe");
							}else {
								System.out.println("La cuenta está de baja");
							}
						}
						System.out.println();
						break;
					}case 2:{
						System.out.println();
						int resultado=retirarDinero(gestionBancaria, connection);
						if(resultado==0) {
							System.out.println("Se ha realizado el ingreso correctamente");
						}else {
							if(resultado==1) {
								System.out.println("No hay suficiente saldo");
							}else {
								if(resultado==2) {
									System.out.println("El numero de cuenta no existe");
								}else {
									System.out.println("La cuenta está de baja");
								}
							}
						}
						System.out.println();
						break;
					}case 3:{
						System.out.println();
						int resultado=hacerTransferencia(gestionBancaria, connection);
						if(resultado==0) {
							System.out.println("Se ha realizado el ingreso correctamente");
						}else {
							if(resultado==1) {
								System.out.println("No hay suficiente dinero en la cuenta origen");
							}else {
								if(resultado==2) {
									System.out.println("La cuenta origen está de baja ");
								}else {
									System.out.println("La cuenta destino está de baja");
								}
							}
						}
						System.out.println();
						break;
					}case 4:{
						int resultado= darAltaCuenta(gestionBancaria,connection);
						System.out.println();
						if(resultado==0) {
							System.out.println("Se ha dado de alta correctamente");
						}else {
							if(resultado==1) {
								System.out.println("No existe esa cuenta");
							}else {
								if(resultado==2) {
									System.out.println("Se ha dado de alta correctamente ");
								}else {
									System.out.println("Ese dni no consta como cliente");
								}
							}
						}
						System.out.println();
						break;
					}case 5:{
						boolean resultado= darBajaCliente(gestionBancaria,connection);
						System.out.println();
						if(resultado==true) {
							System.out.println("Se ha dado de baja correctamente");
						}else {
							System.out.println("Esa cuenta no existe");
						}
						System.out.println();
						break;
					}case 6:{
						System.out.println();
						String infoCuentas=gestionBancaria.mostrarCuentasCliente(connection,pideDni(gestionBancaria, connection));
						if(infoCuentas==null) {

						}else {
							System.out.println("Las cuentas del Cliente son:\n"+infoCuentas+"\n");
						}
						System.out.println();
						break;
					}default:{
						System.err.println("\n\nNUMERO MAL INTRODUCIDO\n\n");
						break;
					}
					}
				}while(numMenuCuentas!=0);
				break;
			}case 3:{
				System.out.println();
				System.out.println(mostrarMovimientosCuenta(gestionBancaria,connection));
				System.out.println();
				break;
			}case 4:{
				System.out.println();
				System.out.println(gestionBancaria.mostrarClientes(connection));
				break;
			}default:{
				System.err.println("\n\nNUMERO MAL INTRODUCIDO\n\n");
				break;
			}
			}
		}while(numMenuPrincipal!=0);
		gestionBancaria.cerrarConexion(connection);
		return true;
	}
	public static int darAltaCuenta(BDMysql gestionBancaria, Connection connection) {
		Scanner input=new Scanner(System.in);
		int tipoAlta = 0;
		System.out.println("\n(1)Para modificar el tipo de una cuenta a activa-(2)Para dar de alta una nueva cuenta");
		boolean flag=true;
		while(flag) {
			try {
				flag=false;
				tipoAlta=input.nextInt();
			}catch(InputMismatchException e) {
				System.out.println("Introdujo mal el numero");
				flag=true;
			}
		}
		flag=true;
		input.nextLine();
		if(tipoAlta==1) {
			System.out.println("Dime el numero de cuenta:");
			int numCuenta = 0;
			while(flag) {
				try {
					flag=false;
					numCuenta=input.nextInt();
				}catch(InputMismatchException e) {
					System.out.println("Introdujo mal el numero");
					flag=true;
				}
			}
			if(gestionBancaria.altaCuenta(connection, numCuenta)==true) {
				return 0;
			}else {
				return 1;
			} 
		}else {
			if(tipoAlta==2) {
				String dni=pideDni(gestionBancaria, connection);
				if(gestionBancaria.crearCuenta(connection, dni)==true) {
					return 2;
				}else {
					return 3;
				}
			}else {
				System.out.println("Eligio mal la opcion");
				return -1;
			}
		}
	}
	public static boolean darBajaCliente(BDMysql gestionBancaria, Connection connection) {
		Scanner input=new Scanner(System.in);
		System.out.println("Dime el numero de cuenta:");
		boolean flag=true;
		int numCuenta = 0;
		while(flag) {
			try {
				flag=false;
				numCuenta=input.nextInt();
			}catch(InputMismatchException e) {
				System.out.println("Introdujo mal el numero");
				flag=true;
			}
		}
		if(gestionBancaria.bajaCuenta(connection, numCuenta)==true) {
			return true;
		}else {
			return false;
		} 
	}
	public static String mostrarMovimientosCuenta(BDMysql gestionBancaria, Connection connection) {
		Scanner input=new Scanner(System.in);
		System.out.println("Dime numero de cuenta:");
		boolean flag=true;
		int numCuenta = 0;
		while(flag) {
			try {
				flag=false;
				numCuenta=input.nextInt();
			}catch(InputMismatchException e) {
				System.out.println("Introdujo mal el numero");
				flag=true;
			}
		}

		System.out.println("Ingrese la fecha de inicio: ");
		Date fechaInicio = pedirFecha();

		System.out.println("Ingrese la fecha de fin: ");
		Date fechaFin = pedirFecha();
		System.out.println();
		System.out.println("Movimientos entre: " + fechaInicio+" y "+fechaFin);
		
		return gestionBancaria.mostrarMovimientoCliente(connection,numCuenta,fechaInicio,fechaFin );
	}

	private static Date pedirFecha() {
		Scanner input = new Scanner(System.in);
		boolean flag = true;
		int dia = 0, mes = 0, anio = 0;
		do {
			System.out.print("Día: ");
			while (flag) {
				try {
					flag = false;
					dia = input.nextInt();
				} catch (InputMismatchException e) {
					System.out.println("Introdujo mal el número");
					flag = true;
					input.next(); // Limpiar el búfer del escáner
				}
			}
			flag = true;
			System.out.print("Mes: ");
			while (flag) {
				try {
					flag = false;
					mes = input.nextInt();
				} catch (InputMismatchException e) {
					System.out.println("Introdujo mal el número");
					flag = true;
					input.next(); // Limpiar el búfer del escáner
				}
			}
			flag = true;
			System.out.print("Año: ");
			while (flag) {
				try {
					flag = false;
					anio = input.nextInt();
				} catch (InputMismatchException e) {
					System.out.println("Introdujo mal el número");
					flag = true;
					input.next(); // Limpiar el búfer del escáner
				}
			}
			flag = true;
			if (!fechaValida(dia, mes, anio)) {
				System.out.println("\nIntrodujo mal la fecha\n");
			}
		} while (!fechaValida(dia, mes, anio));

		Calendar calendar = Calendar.getInstance();
		calendar.set(anio, mes - 1, dia);

		// Crear un objeto java.sql.Date a partir del Calendar
		java.sql.Date fechaSQL = new java.sql.Date(calendar.getTimeInMillis());

		return fechaSQL;
	}


	private static boolean fechaValida(int dia, int mes, int anio) {
		if (mes < 1 || mes > 12 || dia < 1) {
			System.out.println("Fecha inválida. Intente nuevamente.");
			return false;
		}

		int[] diasPorMes = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
		if (mes == 2 && esAnioBisiesto(anio)) {
			diasPorMes[1] = 29;
		}

		if (dia > diasPorMes[mes - 1]) {
			System.out.println("Fecha inválida. Intente nuevamente.");
			return false;
		}

		return true;
	}

	private static boolean esAnioBisiesto(int anio) {
		return (anio % 4 == 0 && anio % 100 != 0) || (anio % 400 == 0);
	}
}

