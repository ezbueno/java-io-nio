package developer.ezandro.persistence;

import developer.ezandro.exception.FilePersistenceException;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class NIOFilePersistence implements FilePersistence {
    private final String currentDir = System.getProperty("user.dir");
    private static final String STORED_DIR = "/managedFiles/NIO/";
    private final String fileName;

    public NIOFilePersistence(String fileName) throws IOException {
        this.fileName = fileName;
        File file = new File(this.currentDir + STORED_DIR);

        if (!file.exists() && !file.mkdirs()) {
            throw new IOException("Failed to create directory structure at: " + file.getAbsolutePath());
        }

        this.clearFile();
    }

    @Override
    public String write(String data) {
        String fullPath = this.getFullPath();

        try (RandomAccessFile file = new RandomAccessFile(new File(fullPath), "rw")) {
            file.seek(file.length());
            file.writeBytes(data);
            file.writeBytes(System.lineSeparator());
        } catch (IOException e) {
            System.err.println("[ERROR] Failed to write data to file '" + this.fileName +
                    "' at " + STORED_DIR + ". Reason: " + e.getMessage());
        }

        return data;
    }

    @Override
    public boolean remove(String sentence) throws FilePersistenceException {
        Path fullPath = Paths.get(this.getFullPath());
        Path tempPath = Paths.get(fullPath + ".tmp");
        boolean removed = false;

        try (BufferedReader reader = Files.newBufferedReader(fullPath);
             BufferedWriter writer = Files.newBufferedWriter(tempPath)) {

            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                if (currentLine.contains(sentence)) {
                    removed = true;
                    continue;
                }
                writer.write(currentLine + System.lineSeparator());
            }
        } catch (IOException e) {
            throw new FilePersistenceException(
                    "Failed to remove content: " + sentence + " from file: " + fullPath, e
            );
        }

        try {
            if (removed) {
                Files.delete(fullPath);
                Files.move(tempPath, fullPath);
            } else {
                Files.deleteIfExists(tempPath);
            }
        } catch (IOException e) {
            throw new FilePersistenceException("Failed to complete file operation: " + fullPath, e);
        }
        return removed;
    }

    @Override
    public String replace(String oldContent, String newContent) throws FilePersistenceException {
        Path fullPath = Paths.get(this.getFullPath());
        Path tempPath = Paths.get(fullPath + ".tmp");
        boolean replaced = false;

        try (BufferedReader reader = Files.newBufferedReader(fullPath);
             BufferedWriter writer = Files.newBufferedWriter(tempPath)) {

            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                if (currentLine.contains(oldContent)) {
                    currentLine = currentLine.replace(oldContent, newContent);
                    replaced = true;
                }
                writer.write(currentLine + System.lineSeparator());
            }
        } catch (IOException e) {
            throw new FilePersistenceException(
                    "Failed to replace content: " + oldContent + " in file: " + fullPath, e
            );
        }

        try {
            if (replaced) {
                Files.delete(fullPath);
                Files.move(tempPath, fullPath);
                return newContent;
            } else {
                Files.deleteIfExists(tempPath);
                throw new FilePersistenceException(
                        "Content to replace not found: " + oldContent + " in file: " + fullPath,
                        new NoSuchFileException(fullPath.toString())
                );
            }
        } catch (IOException e) {
            throw new FilePersistenceException(
                    "Failed to complete file replacement: " + fullPath, e
            );
        }
    }

    @Override
    public String findAll() throws FilePersistenceException {
        StringBuilder content = new StringBuilder();
        String fullPath = this.getFullPath();

        try (RandomAccessFile file = new RandomAccessFile(new File(fullPath), "r");
             FileChannel channel = file.getChannel()
        ) {
            ByteBuffer buffer = ByteBuffer.allocate(256);
            int bytesReader = channel.read(buffer);

            while (bytesReader != -1) {
                buffer.flip();
                while (buffer.hasRemaining()) {
                    content.append((char) buffer.get());
                }
                buffer.clear();
                bytesReader = channel.read(buffer);
            }
        } catch (FileNotFoundException e) {
            throw new FilePersistenceException("File not found at: " + fullPath, e);
        } catch (IOException e) {
            throw new FilePersistenceException("Failed to read file: " + fullPath +
                    " | Error: " + e.getMessage(), e);
        }

        return content.toString();
    }

    @Override
    public String findBy(String sentence) {
        StringBuilder content = new StringBuilder();
        String fullPath = this.getFullPath();

        try (RandomAccessFile file = new RandomAccessFile(new File(fullPath), "r");
             FileChannel channel = file.getChannel()) {

            ByteBuffer buffer = ByteBuffer.allocate(256);
            boolean found = false;  // Flag to control the search loop

            // Read file content until EOF or until sentence is found
            while (channel.read(buffer) != -1 && !found) {
                buffer.flip();

                // Process buffer content
                while (buffer.hasRemaining() && !found) {
                    // Build line until we find a line separator
                    while (!content.toString().endsWith(System.lineSeparator()) && buffer.hasRemaining()) {
                        content.append((char) buffer.get());
                    }

                    // Check if current line contains the target sentence
                    if (content.toString().contains(sentence)) {
                        found = true;  // Set flag to exit loops
                    } else {
                        content.setLength(0);  // Reset for next line
                    }
                }
                buffer.clear();
            }
        } catch (FileNotFoundException e) {
            System.err.println("[SEARCH ERROR] Data file not found at: " + fullPath +
                    " | Verify file existence and permissions");
        } catch (IOException e) {
            System.err.println("[SEARCH ERROR] Failed to search in file: " + this.fileName +
                    " | Error: " + e.getMessage());
        }

        return content.toString();
    }

    private void clearFile() {
        String fullPath = this.getFullPath();

        File file = new File(fullPath);

        try {
            // Open e close immediately (truncate mode)
            new FileOutputStream(file).close();
            System.out.println("[INFO] File initialized/truncated: " + file.getAbsolutePath());
        } catch (
                FileNotFoundException _) {
            System.err.println("[ERROR] File not accessible: " + file.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("[ERROR] Clear operation failed for '" + file.getName() +
                    "' at " + file.getParent() + ". Details: " + e.getMessage());
        }
    }

    private String getFullPath() {
        return this.currentDir + STORED_DIR + this.fileName;
    }
}