package Tools;

import Users.Administrador;
import Users.Adoptante;
import Users.Animal;
import Users.Empleado;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Tool {
    public static Workbook libro;
    public static Sheet hoja;

    Animal animal = new Animal();
    Empleado empleado = new Empleado();
    Administrador administrador = new Administrador();
    Adoptante adoptante = new Adoptante();
    Proceso proceso = new Proceso();

    public static void updateLogger(Level level, String mensaje) throws IOException {

        // Obtener el registrador
        Logger logger = Logger.getLogger("myLog.log");

        // Crear un manejador de archivos
        FileHandler fileHandler = null;
        try {
            fileHandler = new FileHandler("data/mylog.log", true); // El segundo argumento "true" indica que se agregará el contenido al final del archivo

            // Crear un formateador de texto
            SimpleFormatter formatter = new SimpleFormatter();

            // Asociar el formateador al manejador de archivos
            fileHandler.setFormatter(formatter);

            // Agregar el manejador de archivos al registrador
            logger.addHandler(fileHandler);

            // Registrar el mensaje
            logger.log(level, mensaje);
        } catch (SecurityException | IOException e) {
            e.printStackTrace(); // Manejo básico de excepciones para depuración
        } finally {
            if (fileHandler != null) {
                fileHandler.close();
            }
        }
    }


    public static boolean excelFileExists(String fileName) {
        File file = new File(fileName);
        return file.exists();
    }


    public static void createWorkbook() {
        try {
            FileInputStream archivoEntrada = new FileInputStream("database.xlsx");
            libro = WorkbookFactory.create(archivoEntrada);
            archivoEntrada.close();
        } catch (IOException e) {
            libro = new XSSFWorkbook();

            try {
                FileOutputStream archivoSalida = new FileOutputStream("database.xlsx");
                libro.write(archivoSalida);
                archivoSalida.close();

            } catch (IOException er) {
                System.out.println("el archivo no puedo ser creado ");

            }


        }

    }


    public static void createSheet(String name, String[] titulos) {
        if (libro != null) {
            hoja = libro.getSheet(name);
            if (hoja == null) {
                hoja = libro.createSheet(name);
            }

            Row fila1 = hoja.createRow(0);


            for (int i = 0; i < titulos.length; i++) {
                Cell celda1 = fila1.createCell(i);
                celda1.setCellValue(titulos[i]);
            }

            try (FileOutputStream outputStream = new FileOutputStream("database.xlsx")) {
                libro.write(outputStream);
                System.out.println("Información se ha agregado al archivo de Excel.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean existsInSheet(String sheetName, int id) {
        if (hoja != null) {
            for (Row row : hoja) {
                Cell idCell = row.getCell(0);  // Suponiendo que el ID está en la primera columna (columna 0).

                // Verifica si la celda contiene un número y si coincide con el ID que estás buscando.
                if (idCell != null && idCell.getCellType() == CellType.NUMERIC && (int) idCell.getNumericCellValue() == id) {
                    return true;  // El ID existe en la hoja.
                }
            }
        }
        return false;  // El ID no se encontró en la hoja.
    }



    public static String getNameFromSheet(String sheetName, int id) {
        if (hoja != null) {
            for (Row row : hoja) {
                Cell idCell = row.getCell(0);  // Suponiendo que el ID está en la primera columna (columna 0).
                Cell nameCell = row.getCell(1);  // Suponiendo que el nombre está en la segunda columna (columna 1).

                // Verifica si la celda de ID contiene un número y si coincide con el ID que estás buscando.
                if (idCell != null && idCell.getCellType() == CellType.NUMERIC && (int) idCell.getNumericCellValue() == id) {
                    if (nameCell != null && nameCell.getCellType() == CellType.STRING) {
                        return nameCell.getStringCellValue();  // Devuelve el nombre correspondiente al ID.
                    }
                }
            }
        }
        return null;  // No se encontró el nombre en la hoja para el ID especificado.
    }

    public static void read(String sheetName, int id) throws IOException {
        if (hoja != null) {
            Row headerRow = hoja.getRow(0);  // Get the row with column names

            if (sheetName.equalsIgnoreCase("Animal")) {
                for (id = 1; id <= hoja.getLastRowNum(); id++) {
                    Row fila = hoja.getRow(id);

                    if (fila != null) {
                        // Iterate through the cells and display column names with values
                        for (int i = 0; i < fila.getLastCellNum(); i++) {
                            Cell celda = fila.getCell(i);
                            Cell headerCell = headerRow.getCell(i);  // Get the corresponding column name cell
                            System.out.println(headerCell.getStringCellValue() + ": " + celda.getStringCellValue());
                        }
                        System.out.println();
                    }
                }
            } else {
                // Read a single animal by ID
                Row fila = hoja.getRow(id);

                if (fila != null) {
                    // Iterate through the cells and display column names with values
                    for (int i = 0; i < fila.getLastCellNum(); i++) {
                        Cell celda = fila.getCell(i);
                        Cell headerCell = headerRow.getCell(i);  // Get the corresponding column name cell
                        System.out.println(headerCell.getStringCellValue() + ": " + celda.getStringCellValue());
                    }
                } else {
                    System.out.println("ID no encontrado.");
                }
            }
        }
    }





    public static void update(String sheetName, int id, Object[] newValues) throws IOException {
        // Verificar si la hoja existe
        if (libro == null) {
            System.out.println("La hoja no existe.");
            return;
        }

        // Obtener la fila que contiene el animal con el ID especificado
        Row row = hoja.getRow(id);

        // Verificar si la fila existe
        if (row == null) {
            System.out.println("El animal con el ID especificado no existe.");
            return;
        }

        // Actualizar el contenido de las celdas de la fila
        for (int i = 0; i < newValues.length; i++) {
            Cell celda = row.getCell(i);

            // Convertir el valor a String
            String valor = String.valueOf(newValues[i]);

            // Establecer el valor de la celda
            celda.setCellValue(valor);
        }

        // Guardar la hoja de Excel
        try (FileOutputStream outputStream = new FileOutputStream("database.xlsx")) {
            libro.write(outputStream);
            System.out.println("Información actualizada en el archivo de Excel.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void delete(String sheetName, int id) throws IOException {
        if (hoja != null) {
            Row rowToDelete = hoja.getRow(id);

            if (rowToDelete != null) {
                hoja.removeRow(rowToDelete);
            } else {
                System.out.println("el id no Fue encontrado");
            }
        }

        // Save the changes to the Excel file
        try (FileOutputStream outputStream = new FileOutputStream("database.xlsx")) {
            libro.write(outputStream);
        }
    }


    public void menu() throws Exception {
        Scanner scanner = new Scanner(System.in);

        // Get the user's role
        System.out.println("Ingrese su rol (1 - administrador, 2 - empleado, 3 - adoptante): ");
        int role = scanner.nextInt();

        // Display the corresponding menu based on the user's role
        switch (role) {
            case 1:
                menuAdministrador();
                break;
            case 2:
                menuEmpleado();
                break;
            case 3:
                menuAdoptante();
                break;
            default:
                System.out.println("Rol no válido.");
                break;
        }
    }


    private void menuAdministrador() throws Exception {
        Scanner scanner = new Scanner(System.in);

        // Print the menu for Administrador
        System.out.println("** Menú de Administrador **");
        System.out.println("1. Crear animal");
        System.out.println("2. Actualizar animal");
        System.out.println("3. Leer animal");
        System.out.println("4. Eliminar animal");
        System.out.println("5. Crear empleado");
        System.out.println("6. Actualizar empleado");
        System.out.println("7. Leer empleado");
        System.out.println("8. Eliminar empleado");
        System.out.println("9. Crear administrador");
        System.out.println("10. Actualizar administrador");
        System.out.println("11. Leer administrador");
        System.out.println("12. Eliminar administrador");
        System.out.println("13. Salir (Cerrar sesión)");

        // Prompt for user input
        System.out.println("Ingrese una opción: ");
        int opcion = scanner.nextInt();

        // Process the selected option for Administrador
        switch (opcion) {
            case 1:
                // Crear animal
                Administrador administrador1 = new Administrador();
                administrador.createAnimal();
                break;
            case 2:
                // Actualizar animal
                Animal animal1 = new Animal();
                animal.updateAnimal();
                break;
            case 3:
                // Leer animal
                Animal animal2 = new Animal();
                animal.ReadAnimal();
                break;
            case 4:
                // Eliminar animal
                Animal animal3 = new Animal();
                animal.deleteAnimal();
                break;
            case 5:
                // Crear empleado
                Administrador administrador2 = new Administrador();
                administrador.CreateEmpleado();
                break;
            case 6:
                // Actualizar empleado
                Empleado empleado1 = new Empleado();
                empleado.UpdateEmpleado();
                break;
            case 7:
                // Leer empleado
                Empleado empleado2 = new Empleado();
                empleado.ReadEmpleado();
                break;
            case 8:
                // Eliminar empleado
                Empleado empleado3 = new Empleado();
                empleado.deleteEmpleado();
                break;
            case 9:
                // Crear administrador
                Administrador administrador3 = new Administrador();
                administrador.CreateAdministrador();
                break;
            case 10:
                // Actualizar administrador
                Administrador administrador4 = new Administrador();
                this.administrador.updateAdministrador();
                break;
            case 11:
                // Leer administrador
                Administrador administrador5 = new Administrador();
                this.administrador.ReadAdministrador();
                break;
            case 12:
                // Eliminar administrador
                Administrador administrador6 = new Administrador();
                this.administrador.deleteAdministrador();
                break;
            case 13:
                // Salir (Cerrar sesión)
                System.out.println("Cerrando sesión de Administrador...");
                break;
            default:
                // Opción no válida
                System.out.println("Opción no válida.");
                break;
        }
    }

    private void menuEmpleado() throws Exception {
        Scanner scanner = new Scanner(System.in);

        // Print the menu for Empleado
        System.out.println("** Menú de Empleado **");
        System.out.println("1. Actualizar proceso de adopción");
        System.out.println("2. Leer proceso de adopción");
        System.out.println("3. Eliminar proceso de adopción");
        System.out.println("4. Leer animal");
        System.out.println("5.  Actualizar animal");
        System.out.println("6. Salir (Cerrar sesión)");

        // Prompt for user input
        System.out.println("Ingrese una opción: ");
        int opcion = scanner.nextInt();

        // Process the selected option for Empleado
        switch (opcion) {
            case 1:

                proceso.updateAdoptionProcess();
                break;
            case 2:
                Proceso proceso2 = new Proceso();
                proceso.viewAdoptionProcesses();
                break;
            case 3:
                // Eliminar proceso de adopción
                Proceso proceso3 = new Proceso();
                proceso.deleteAdoptionProcess();
                break;
            case 4:
                Animal animal2 = new Animal();
                animal.ReadAnimal();
                break;
            case 5:
                Animal animal1 = new Animal();
                animal.updateAnimal();
                break;

            case 6:
                System.out.println("Cerrando sesión de empleado");
                break;
            default:
                // Opción no válida
                System.out.println("Opción no válida.");
                break;
        }
    }

    private void menuAdoptante() throws Exception {
        Scanner scanner = new Scanner(System.in);

        // Print the menu for Adoptante
        System.out.println("** Menú de Adoptante **");
        System.out.println("1. Solicitar proceso de adopción");
        System.out.println("2. Leer animal");
        System.out.println("3. Crear Adoptante");
        System.out.println("4. Leer Adoptante");
        System.out.println("5. Actualizar adoptante");
        System.out.println("6. Eliminar adoptante");
        System.out.println("7.Salir (Cerrar sesión)");


        // Prompt for user input
        System.out.println("Ingrese una opción: ");
        int opcion = scanner.nextInt();

        // Process the selected option for Adoptante
        switch (opcion) {
            case 1:

                proceso.create();
                break;
            case 2:
                Animal animal2 = new Animal();
                animal.ReadAnimal();
                break;
            case 3:
                Adoptante adoptante1 = new Adoptante();
                adoptante1.CreateAdoptante();
                break;
            case 4:
                Adoptante adoptante2 = new Adoptante();
                adoptante2.ReadAdoptante();
                break;
            case 5:
                Adoptante adoptante3 = new Adoptante();
                adoptante3.UpdateAdoptante();
                break;
            case 6:
                Adoptante adoptante4 = new Adoptante();
                adoptante4.deleteAdoptante();
                break;
            case 7:
                System.out.println("Cerrando sesion adoptante");
                break;


            default:
                System.out.println("opcion invalida");
                break;
        }


    }


}

