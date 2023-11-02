package Tools;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import static Tools.Tool.hoja;

public class Proceso {

    int id;
    String hora;
    String fecha;
    String status;
    int idEmpleado;
    int idAdoptante;
    int idAnimal;

    public Proceso() {
        try {
            Tool.createWorkbook();
            Tool.libro = Tool.libro;
            hoja = Tool.libro.getSheet("Proceso Adopcion");
            if (hoja == null) {
                String[] titulos = {"id", "hora", "fecha", "status", "idEmpleado", "idAdoptante", "idAnimal"};
                hoja = Tool.libro.createSheet("Proceso Adopcion");
                Row fila1 = hoja.createRow(0);
                for (int i = 0; i < titulos.length; i++) {
                    Cell celda1 = fila1.createCell(i);
                    celda1.setCellValue(titulos[i]);
                }
            }
        } catch (Exception e) {
            System.out.println("Algo salió mal!!!");
        }
    }

    public void create() {
        Scanner scanner = new Scanner(System.in);

        // Prompt for employee, applicant, and animal IDs
        System.out.print("Ingrese el ID del empleado: ");
        int idEmpleado = scanner.nextInt();
        System.out.print("Ingrese el ID del Adoptante: ");
        int idAdoptante = scanner.nextInt();
        System.out.print("Ingrese el ID del animal: ");
        int idAnimal = scanner.nextInt();

        createAdoptionProcess(idEmpleado, idAdoptante, idAnimal);
    }

    private boolean doesAdoptanteExist(int idAdoptante) {
        for (Row row : hoja) {
            Cell idCell = row.getCell(0); // Assuming the ID is in the first column
            if (idCell != null) {
                try {
                    int cellValue = (int) Double.parseDouble(idCell.getStringCellValue()); // Convert the cell value to an integer
                    if (cellValue == idAdoptante) {
                        return true;
                    }
                } catch (NumberFormatException e) {
                    // The cell value couldn't be converted to an integer, so continue searching
                }
            }
        }
        return false; // Adoptante ID not found
    }

    private boolean doesEmpleadoExist(int idEmpleado) {
        for (Row row : hoja) {
            Cell idCell = row.getCell(0); // Assuming the ID is in the first column
            if (idCell != null) {
                try {
                    int cellValue = (int) Double.parseDouble(idCell.getStringCellValue()); // Convert the cell value to an integer
                    if (cellValue == idEmpleado) {
                        return true;
                    }
                } catch (NumberFormatException e) {
                    // The cell value couldn't be converted to an integer, so continue searching
                }
            }
        }
        return false; // Empleado ID not found
    }

    private boolean doesAnimalExist(int idAnimal) {
        for (Row row : hoja) {
            Cell idCell = row.getCell(0); // Assuming the ID is in the first column
            if (idCell != null) {
                try {
                    int cellValue = (int) Double.parseDouble(idCell.getStringCellValue()); // Convert the cell value to an integer
                    if (cellValue == idAnimal) {
                        return true;
                    }
                } catch (NumberFormatException e) {
                    // The cell value couldn't be converted to an integer, so continue searching
                }
            }
        }
        return false; // Animal ID not found
    }





    public void createAdoptionProcess(int idEmpleado, int idAdoptante, int idAnimal) {
        if (Tool.hoja != null) {
            // Check if the employee, adopter, and animal IDs exist using your existing methods
            boolean empleadoExists = doesEmpleadoExist(idEmpleado);
            boolean adoptanteExists = doesAdoptanteExist(idAdoptante);
            boolean animalExists = doesAnimalExist(idAnimal);

            if (empleadoExists && adoptanteExists && animalExists) {
                int lastRowNum = Tool.hoja.getLastRowNum();
                Row newRow = Tool.hoja.createRow(lastRowNum + 1);

                this.id = lastRowNum + 1;
                this.idEmpleado = idEmpleado;
                this.idAdoptante = idAdoptante;
                this.idAnimal = idAnimal;
                this.hora = "";
                this.fecha = "";
                this.status = "pendiente";

                Object[] newProcess = {id, hora, fecha, status, idEmpleado, idAdoptante, idAnimal};
                for (int i = 0; i < newProcess.length; i++) {
                    Cell cell = newRow.createCell(i);
                    cell.setCellValue(newProcess[i].toString());
                }

                try (FileOutputStream outputStream = new FileOutputStream("database.xlsx")) {
                    Tool.libro.write(outputStream);
                    System.out.println("Proceso de adopción creado y guardado en el archivo de Excel.");

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                if (!empleadoExists) {
                    System.out.println("El empleado con ID " + idEmpleado + " no existe.");
                }
                if (!adoptanteExists) {
                    System.out.println("El adoptante con ID " + idAdoptante + " no existe.");
                }
                if (!animalExists) {
                    System.out.println("El animal con ID " + idAnimal + " no existe.");
                }
            }
        }
    }















    public void viewAdoptionProcesses() throws Exception {
        if (hoja != null) {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Ingrese el ID del proceso de adopción que desea ver: ");
            String idProceso = scanner.next(); // Read the ID as a string

            for (Row row : hoja) {
                Cell idCell = row.getCell(0);
                if (idCell != null && idCell.getStringCellValue().equals(idProceso)) {
                    for (Cell cell : row) {
                        System.out.print(cell.getStringCellValue() + "\t");
                    }
                    System.out.println();
                    return;
                }
            }
            System.out.println("No se encontró ningún proceso de adopción con el ID " + idProceso);
        } else {
            System.out.println("La hoja no existe.");
        }
    }

    public void updateAdoptionProcess() {
        if (hoja != null) {
            Scanner scanner = new Scanner(System.in);

            // Prompt for the adoption process ID
            System.out.print("Ingrese el ID del proceso de adopción que desea actualizar: ");
            int idProceso = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            // Prompt for the new status (1 for "aceptado" and 2 for "rechazado")
            System.out.print("Ingrese el nuevo estado (1 para 'aceptado' o 2 para 'rechazado'): ");
            int newStatus = scanner.nextInt();

            // Check if the new status is valid
            if (newStatus != 1 && newStatus != 2) {
                System.out.println("El estado debe ser 1 para 'aceptado' o 2 para 'rechazado'.");
                return;
            }

            String idProcesoStr = Integer.toString(idProceso); // Convert ID to a string

            for (Row row : hoja) {
                Cell idCell = row.getCell(0);

                // Skip the header row
                if (idCell != null && idCell.getCellType() == CellType.STRING) {
                    String rowIdStr = idCell.getStringCellValue();
                    if (rowIdStr.equals(idProcesoStr)) {
                        // Check if the adoption process exists before updating it
                        row.getCell(3).setCellValue(newStatus == 1 ? "aceptado" : "rechazado");

                        // Register the date and time of the update
                        Date now = new Date();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
                        row.getCell(1).setCellValue(dateFormat.format(now));
                        row.getCell(2).setCellValue(timeFormat.format(now));

                        try (FileOutputStream outputStream = new FileOutputStream("database.xlsx")) {
                            Tool.libro.write(outputStream);
                            System.out.println("Proceso de adopción actualizado en el archivo de Excel.");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return;
                    }
                }
            }
            System.out.println("No se encontró ningún proceso de adopción con el ID " + idProceso);
        } else {
            System.out.println("La hoja no existe.");
        }
    }

    public void deleteAdoptionProcess() throws Exception {
        if (hoja != null) {
            Scanner scanner = new Scanner(System.in);

            // Preguntar el ID que se desea eliminar
            System.out.print("Ingrese el ID del proceso de adopción que desea eliminar: ");
            int idProceso = scanner.nextInt();
            scanner.nextLine(); // Consumir el carácter de nueva línea

            String idProcesoStr = Integer.toString(idProceso); // Convertir el ID a una cadena

            for (Row row : hoja) {
                Cell idCell = row.getCell(0);

                // Saltar la fila de encabezado
                if (idCell != null && idCell.getCellType() == CellType.STRING) {
                    String rowIdStr = idCell.getStringCellValue();
                    if (rowIdStr.equals(idProcesoStr)) {
                        // Eliminar el proceso de adopción eliminando la fila
                        hoja.removeRow(row);
                        // Guardar los cambios en el archivo de Excel
                        try (FileOutputStream outputStream = new FileOutputStream("database.xlsx")) {
                            Tool.libro.write(outputStream);
                            System.out.println("Proceso de adopción eliminado del archivo de Excel.");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return;
                    }
                }
            }
            System.out.println("No se encontró ningún proceso de adopción con el ID " + idProceso);
        } else {
            System.out.println("La hoja no existe.");
        }
    }
}