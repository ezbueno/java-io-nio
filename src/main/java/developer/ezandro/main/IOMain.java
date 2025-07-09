package developer.ezandro.main;

import developer.ezandro.exception.FilePersistenceException;
import developer.ezandro.persistence.FilePersistence;
import developer.ezandro.persistence.IOFilePersistence;

import java.io.IOException;

public class IOMain {
    private static final String SEPARATOR = "=====";

    public static void main(String[] args) {
        try {
            FilePersistence filePersistence = new IOFilePersistence("user.csv");

            // Data insertion
            executeOperation("Insert Lucas", () ->
                    filePersistence.write("Lucas;lucas@lucas.com;15/01/1990;"));

            executeOperation("Insert Maria", () ->
                    filePersistence.write("Maria;maria@maria.com;23/10/2000;"));

            executeOperation("Insert João", () ->
                    filePersistence.write("João;joao@joao.com;01/12/1995;"));

            // Data operations
            executeOperation("Show all records", filePersistence::findAll);

            executeOperation("Remove /12/19", () ->
                    String.valueOf(filePersistence.remove("/12/19")));

            executeOperation("Remove /06/202", () ->
                    String.valueOf(filePersistence.remove("/06/202")));

            executeOperation("Find Lucas", () ->
                    filePersistence.findBy("Lucas;"));

            executeOperation("Find Maria by email", () ->
                    filePersistence.findBy(";maria@"));

            executeOperation("Find by '95'", () ->
                    filePersistence.findBy("95"));

            executeOperation("Replace record", () ->
                    filePersistence.replace(".com;15/01/", "Carlos;carlos@carlos.com;22/03/1991;"));

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