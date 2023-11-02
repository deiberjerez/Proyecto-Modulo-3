package Users;

import Tools.Tool;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import static Tools.Tool.hoja;
import static Tools.Tool.libro;


public class Animal {
    protected static int idCounter = 0;
    protected int id;
    protected String name;
    protected int age;
    protected String specie;
    protected String race;
    protected String healthStatus;
    protected String description;

    public Animal() {

        try {
            Tool.createWorkbook();
            String[] titulos = {"id", "Nombre", "Edad", "Especie", "Raza", "Estado de salud", "Descripcion"};
            Tool.createSheet("Animal", titulos);


        } catch (Exception e) {
            System.out.println("Algo salio super mal!!!");
        }
    }


    public void setName(String name) throws Exception {
        if (name == null || name.isEmpty()) {
            Tool.updateLogger(Level.INFO, "el nombre no puede estar vacio");

        }
        this.name = name;
    }

    public void setAge(int age) throws Exception {
        String regex = "^\\d+$";
        if (!String.valueOf(age).matches(regex)) {
            Tool.updateLogger(Level.INFO, "La edad debe ser un número entero positivo");
        }
        this.age = age;
    }

    public void setSpecie(String specie) throws Exception {
        Logger logger = Logger.getLogger("mylogs");
        if (specie.length() > 10) {
            Tool.updateLogger(Level.INFO, "la especie no puede superar 10 caracteres");
        }
        this.specie = specie;
    }

    public void setRace(String race) {


        this.race = race;
    }

    public String getHealthStatus() {
        return healthStatus;
    }

    public void setHealthStatus(String healthStatus) {
        this.healthStatus = healthStatus;
    }

    public void setDescription(String description) throws Exception {
        String regex = "^[a-zA-Z0-9 ,.!?]+$";
        if (!description.matches(regex)) {
            Tool.updateLogger(Level.INFO, "no se pueden ingresar caracteres especiales");
        }
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void Create() throws Exception {

        Scanner sc = new Scanner(System.in);



        System.out.println("Ingrese el nombre del animal: ");
        String name = sc.nextLine();
        setName(name);

        System.out.println("¿Cuál es la edad del animal? ");
        int age = Integer.parseInt(sc.nextLine());
        setAge(age);

        System.out.println("Ingrese la especie del animal: ");
        String specie = sc.nextLine();
        setSpecie(specie);

        System.out.println("Ingrese la raza del animal: ");
        String race = sc.nextLine();
        setRace(race);

        System.out.println("Ingrese el estado de salud del animal: ");
        String healthStatus = sc.nextLine();
        setHealthStatus(healthStatus);

        System.out.println("Ingrese la descripción del animal: ");
        String description = sc.nextLine();
        setDescription(description);

        if (hoja != null) {
            int lastRowNum = hoja.getLastRowNum();
            Row newRow = hoja.createRow(lastRowNum + 1); //crear una nueva fila despues de la ultima
            id = lastRowNum + 1;


            Object[] newAnimal = {id, name, age, specie, race, healthStatus, description};
            for (int i = 0; i < newAnimal.length; i++) {
                Cell cell = newRow.createCell(i);
                cell.setCellValue(newAnimal[i].toString());

            }


            try (FileOutputStream outputStream = new FileOutputStream("database.xlsx")) {
                libro.write(outputStream);
                System.out.println("Información del animal agregada al archivo de Excel.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    public void ReadAnimal() throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.print("ingrese 1 para consultar animal ");
        int id = scanner.nextInt();

        // Read the animal by ID
        Tool.read("Animal", id);
    }


    public void updateAnimal() throws IOException {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Ingrese el ID del animal a actualizar: ");
        int id = Integer.parseInt(scanner.nextLine());

        // Obtener el animal actual
        Row row = hoja.getRow(id);

        if (row != null) {
            // Crear un array de nombres de columnas
            String[] columnNames = {"ID", "Nombre", "Edad", "Especie", "Raza", "Estado de salud", "Descripción"};

            // Crear un array de objetos para almacenar los valores actualizados
            Object[] newValues = new Object[columnNames.length];

            // Solicitar los valores actualizados en el orden correcto
            for (int i = 1; i < columnNames.length; i++) {
                System.out.println("Ingrese el valor actualizado para " + columnNames[i] + ": ");
                newValues[i] = scanner.nextLine();
            }

            // Establecer el ID como el primer valor
            newValues[0] = id;

            // Actualizar el animal
            Tool.update("Animal", id, newValues);
        } else {
            System.out.println("El animal con el ID especificado no existe.");
        }
    }


    public void deleteAnimal() throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Ingrese el id del animal que desea eliminar  ");
        int id = scanner.nextInt();

        Tool.delete("Animal", id);
        System.out.println("El animal ah sido eliminado ");
    }



}








