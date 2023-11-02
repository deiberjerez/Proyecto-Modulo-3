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

public class Adoptante extends Usuario {
    int id;
    String preferencesAdoption;


    public Adoptante() {


        try {
            Tool.createWorkbook();
            String[] titulos = {"id", "Nombre", "direccion", "Numero de contacto", "Preferencias de adopción"};
            Tool.createSheet("Adoptante", titulos);


        } catch (Exception e) {
            System.out.println("Algo salio super hiper mega mal!!!");
        }
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) throws Exception {
        if (id <= 0) {
            Tool.updateLogger(Level.INFO, "la id no puede ser menor o igual a 0");
        }
        this.id = id;
    }


    public String getPreferencesAdoption() {
        return preferencesAdoption;
    }

    public void setPreferencesAdoption(String preferencesAdoption) throws Exception {
        if (preferencesAdoption != null && !preferencesAdoption.isEmpty()) {
            this.preferencesAdoption = preferencesAdoption;
        } else {
            Tool.updateLogger(Level.INFO, "El campo no puede quedar vacío");
        }
    }


    //sigue el create

    public void CreateAdoptante() {
        try {
            Scanner sc = new Scanner(System.in);


            System.out.println("Ingrese el nombre del adoptante: ");
            String name = sc.nextLine();
            setName(name);

            System.out.println("¿Cuál es la dirección del usuario? ");
            String address = sc.nextLine();
            setAddress(address);

            System.out.println("Ingrese el numero de contacto: ");
            String contactNumber = sc.nextLine();
            setContactNumber(contactNumber);

            System.out.println("Indique sus preferencias de adopción: ");
            String preferencesAdoption = sc.nextLine();
            setPreferencesAdoption(preferencesAdoption);

            if (hoja != null) {
                int lastRowNum = hoja.getLastRowNum();
                Row newRow = hoja.createRow(lastRowNum + 1); // Create a new row after the last one
                id = lastRowNum + 1;

                Object[] newAdoptante = {id, name, address, contactNumber, preferencesAdoption};
                for (int i = 0; i < newAdoptante.length; i++) {
                    Cell cell = newRow.createCell(i);
                    cell.setCellValue(newAdoptante[i].toString());
                }

                try (FileOutputStream outputStream = new FileOutputStream("database.xlsx")) {
                    libro.write(outputStream);
                    System.out.println("Información del adoptante agregada al archivo de Excel.");
                }
            }
        } catch (NumberFormatException e) {
            System.err.println("Error: El número de contacto debe ser un valor numérico.");
        } catch (IOException e) {
            System.err.println("Error al escribir en el archivo Excel: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void ReadAdoptante() throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Ingresa el Id del adoptante que deseas consultar ");
        int id = scanner.nextInt();
        Tool.read("Adoptante",id);
    }

    public void UpdateAdoptante() throws IOException {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Ingrese el ID del adoptante a actualizar: ");
        int id = Integer.parseInt(scanner.nextLine());

        // Obtener el adoptante actual
        Row row = hoja.getRow(id);

        if (row != null) {
            // Crear un array de nombres de columnas
            String[] columnNames = {"ID", "Nombre", "Direccion", "Numero de contacto", "Preferencias de adopción"};

            // Crear un array de objetos para almacenar los valores actualizados
            Object[] newValues = new Object[columnNames.length];

            // Establecer el ID como el primer valor
            newValues[0] = id;

            // Solicitar los valores actualizados en el orden correcto
            for (int i = 1; i < columnNames.length; i++) {
                System.out.println("Ingrese el valor actualizado para " + columnNames[i] + ": ");
                newValues[i] = scanner.nextLine();
            }

            // Actualizar el adoptante
            Tool.update("Adoptante", id, newValues);
        } else {
            System.out.println("El adoptante con el ID especificado no existe.");
        }


    }

    public void deleteAdoptante() throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Ingrese el id del adoptante que desea eliminar  ");
        int id = scanner.nextInt();

        Tool.delete("Adoptante", id);
        System.out.println("El adoptante ah sido eliminado ");
    }

}


