package developer.ezandro.main;

import developer.ezandro.exception.FilePersistenceException;
import developer.ezandro.persistence.FilePersistence;
import developer.ezandro.persistence.NIOFilePersistence;

import java.io.IOException;

public class NIOMain {
    private static final String SEPARATOR = "=====";

    public static void main(String[] args) {
        try {
            FilePersistence filePersistence = new NIOFilePersistence("user.csv");

            // Data insertion
            executeOperation("Insert Bianca", () ->
                    filePersistence.write("Bianca;bianca@bianca.com;22/09/1997;"));

            executeOperation("Insert Bernardo", () ->
                    filePersistence.write("Bernardo;bernardo@bernardo.com;28/11/1999;"));

            executeOperation("Insert Ricardo", () ->
                    filePersistence.write("Ricardo;ricardo@ricardo.com;12/01/2000;"));

            // Data operations
            executeOperation("Show all records", filePersistence::findAll);

            executeOperation("Remove /01/20", () ->
                    String.valueOf(filePersistence.remove("/01/20")));

            executeOperation("Remove /11/199", () ->
                    String.valueOf(filePersistence.remove("/11/199")));

            executeOperation("Find Bianca", () ->
                    filePersistence.findBy("Bianca;"));

            executeOperation("Find Bernardo by email", () ->
                    filePersistence.findBy(";bernardo@"));

            executeOperation("Find by '99'", () ->
                    filePersistence.findBy("99"));

            executeOperation("Replace record", () ->
                    filePersistence.replace(".com;22/09/", "Bianca;bianca@bianca.com;22/03/1998;"));

            executeOperation("Final state", filePersistence::findAll);

        } catch (FilePersistenceException |
                IOException e) {
            System.err.println("CRITICAL ERROR: " + e.getMessage());
            if (e.getCause() != null) {
                System.err.println("Root cause: " + e.getCause().getMessage());
            }
        }
    }

    private static void executeOperation(String description, Operation operation)
            throws FilePersistenceException {
        System.out.println(SEPARATOR);
        System.out.println("OPERATION: " + description);
        try {
            String result = operation.execute();
            System.out.println("RESULT: " + (result.isEmpty() ? "<no output>" : result));
        } catch (FilePersistenceException e) {
            System.err.println("OPERATION FAILED: " + description);
            throw e;
        }
    }

    @FunctionalInterface
    private interface Operation {
        String execute() throws FilePersistenceException;
    }
}