package Users;

import Tools.Tool;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;

import static Tools.Tool.hoja;
import static Tools.Tool.libro;

public class Empleado extends Usuario {
    //atributos
    int id;
    String rol;
    String hiringDate;

    //constructor
    public Empleado() {


        try {
            Tool.createWorkbook();
            String[] titulos = {"id", "Nombre", "direccion", "Numero de contacto", "rol", "Fecha de contratacion"};
            Tool.createSheet("Empleado", titulos);


        } catch (Exception e) {
            System.out.println("Algo salio super mal!!!");
        }
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) throws Exception {
        if (rol.equals("administrador")) {
            Tool.updateLogger(Level.INFO, "no puedes usar el rol de administrador");
        }
        this.rol = rol;
    }


    public int getId() {
        return id;
    }


    public void setId(int id) throws Exception {
        if (id <= 0) {
            Tool.updateLogger(Level.INFO, "la id no puede ser menor o igual a 0");
        }
        this.id = id;
    }

    public String getHiringDate() {
        return hiringDate;
    }

    public void setHiringDate(String hiringDate) throws Exception {
        String regex = "^\\d{4}-\\d{2}-\\d{2}$";
        if (hiringDate.matches(regex)) {
            this.hiringDate = hiringDate;
        } else {
            Tool.updateLogger(Level.INFO, "La fecha de contratación debe tener el formato yyyy-MM-dd");
        }
    }



    public void CreateEmpleado() {
        Scanner sc = new Scanner(System.in);
        try {


                System.out.println("Ingrese el nombre del empleado: ");
                String name = sc.next();
                setName(name);

                System.out.println("¿Cuál es la dirección del empleado? ");
                String address = sc.next();
                setAddress(address);

                System.out.println("Ingrese el número de contacto: ");
                String contactNumber = sc.next();
                setContactNumber(contactNumber);

                System.out.println("Ingrese el rol: ");
                String rol = sc.next();
                setRol(rol);

                System.out.println("Ingrese la fecha de contratación: ");
                String hiringDate = sc.next();
                setHiringDate(hiringDate);

                if (hoja != null) {
                    int lastRowNum = hoja.getLastRowNum();
                    Row newRow = hoja.createRow(lastRowNum + 1); // Create a new row after the last one
                    id = lastRowNum + 1;

                    Object[] newEmpleado = {id, name, address, contactNumber, rol, hiringDate};
                    for (int i = 0; i < newEmpleado.length; i++) {
                        Cell cell = newRow.createCell(i);
                        cell.setCellValue(newEmpleado[i].toString());
                    }

                    try (FileOutputStream outputStream = new FileOutputStream("database.xlsx")) {
                        libro.write(outputStream);

                    }
                }

        } catch (Exception e) {
            System.err.println("Se ha producido una excepción: " + e.getMessage());
        }
    }

    public void ReadEmpleado() throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Ingresa el Id del empleado que deseas consultar ");
        int id = scanner.nextInt();
        Tool.read("Empleado",id);
    }

    public void UpdateEmpleado() throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Ingrese el ID del empleado a actualizar: ");
        int id = Integer.parseInt(scanner.nextLine());

        // Obtener el empleado actual
        Row row = hoja.getRow(id);

        if (row != null) {
            // Crear un array de nombres de columnas, excluyendo "ID"
            String[] columnNames = {"ID", "Nombre", "Direccion", "Numero de contacto", "Rol", "Fecha de contratacion"};

            // Crear un array de objetos para almacenar los valores actualizados
            Object[] newValues = new Object[columnNames.length];

            // Solicitar los valores actualizados en el orden correcto, excluyendo "ID"
            for (int i = 1; i < columnNames.length; i++) {
                System.out.println("Ingrese el valor actualizado para " + columnNames[i] + ": ");
                newValues[i] = scanner.nextLine();
            }

            // Establecer el ID como el primer valor
            newValues[0] = id;

            // Actualizar el empleado
            Tool.update("Empleado", id, newValues);
        } else {
            System.out.println("El empleado con el ID especificado no existe.");
        }



    }

    public void deleteEmpleado() throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Ingrese el id del animal que desea eliminar  ");
        int id = scanner.nextInt();

        Tool.delete("Empleado", id);
        System.out.println("El empleado  ah sido eliminado ");
    }




}

