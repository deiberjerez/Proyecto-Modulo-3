package Users;

import Tools.Tool;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

import static Tools.Tool.hoja;
import static Tools.Tool.libro;

public class Administrador extends Usuario {
    Animal animal = new Animal();
    Empleado empleado = new Empleado();
    int id;

    public Administrador() {

        try {
            Tool.createWorkbook();
            String[] titulos = {"id", "Nombre", "direccion", "Numero de contacto"};
            Tool.createSheet("Administrador", titulos);


        } catch (Exception e) {
            System.out.println("Algo salio super hiper mega mal!!!");
        }
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public void CreateAdministrador() throws Exception {
        try {
            Scanner sc = new Scanner(System.in);


            System.out.println("Ingrese el nombre del Administrador: ");
            String name = sc.nextLine();
            setName(name);

            System.out.println("¿Cuál es la dirección del usuario? ");
            String address = sc.nextLine();
            setAddress(address);

            System.out.println("Ingrese el número de contacto: ");
            String contactNumber = sc.nextLine();
            setContactNumber(contactNumber);


            if (hoja != null) {
                int lastRowNum = hoja.getLastRowNum();
                Row newRow = hoja.createRow(lastRowNum + 1); // Create a new row after the last one
                id = lastRowNum + 1;

                Object[] newEmpleado = {id, name, address, contactNumber};
                for (int i = 0; i < newEmpleado.length; i++) {
                    Cell cell = newRow.createCell(i);
                    cell.setCellValue(newEmpleado[i].toString());
                }

                try (FileOutputStream outputStream = new FileOutputStream("database.xlsx")) {
                    libro.write(outputStream);

                }
            }
        } catch (IOException e) {

            throw new Exception("Error al escribir en el archivo Excel: " + e.getMessage(), e);
        }
    }

    public void ReadAdministrador() throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Ingresa el Id del administrador que deseas consultar ");
        int id = scanner.nextInt();
        Tool.read("Administrador",id);
    }


    public void updateAdministrador() throws IOException {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Ingrese el ID del administrador a actualizar: ");
        int id = Integer.parseInt(scanner.nextLine());


        Row row = hoja.getRow(id);

        if (row != null) {

            String[] columnNames = {"id", "Nombre", "direccion", "Numero de contacto"};

            Object[] newValues = new Object[columnNames.length];

            for (int i = 1; i < columnNames.length; i++) {
                System.out.println("Ingrese el valor actualizado para " + columnNames[i] + ": ");
                newValues[i] = scanner.nextLine();
            }

            newValues[0] = id;
            Tool.update("Administrador", id, newValues);
        } else {
            System.out.println("El animal con el ID especificado no existe.");
        }
    }

    public void deleteAdministrador() throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Ingrese el id del animal que desea eliminar  ");
        int id = scanner.nextInt();

        Tool.delete("Administrador", id);
        System.out.println("El animal ah sido eliminado ");
    }


    public void createAnimal() throws Exception {
        Animal animal = new Animal();
        animal.Create();
    }

    public void CreateEmpleado() throws Exception {
        Empleado empleado = new Empleado();
        empleado.CreateEmpleado();

    }


}