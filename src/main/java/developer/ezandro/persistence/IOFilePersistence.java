package developer.ezandro.persistence;

import developer.ezandro.exception.FilePersistenceException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class IOFilePersistence implements FilePersistence {
    private final String currentDir = System.getProperty("user.dir");
    private static final String STORED_DIR = "/managedFiles/IO/";
    private final String fileName;

    public IOFilePersistence(String fileName) throws IOException {
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
        
        try (FileWriter fileWriter = new FileWriter(fullPath, true);
               BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
               PrintWriter printWriter = new PrintWriter(bufferedWriter)
        ) {
            printWriter.println(data);
        } catch (IOException e) {
            System.err.println("[ERROR] Failed to write data to file '" + this.fileName +
                    "' at " + STORED_DIR + ". Reason: " + e.getMessage());
        }

        return data;
    }

    @Override
    public boolean remove(String sentence) throws FilePersistenceException {
        List<String> contentList = this.getContentList();

        if (contentList.stream().noneMatch(c -> c.contains(sentence))) {
            return false;
        }

        this.clearFile();

        contentList.stream()
                .filter(c -> !c.contains(sentence))
                .forEach(this::write);

        return true;
    }

    @Override
    public String replace(String oldContent, String newContent) throws FilePersistenceException {
        List<String> contentList = this.getContentList();

        if (contentList.stream().noneMatch(c -> c.contains(oldContent))) {
            return "";
        }

        this.clearFile();

        contentList.stream()
                .map(c -> c.contains(oldContent) ? newContent : c)
                .forEach(this::write);

        return newContent;
    }

    @Override
    public String findAll() throws FilePersistenceException {
        StringBuilder content = new StringBuilder();
        String fullPath = this.getFullPath();

        try (BufferedReader br = new BufferedReader(new FileReader(fullPath))) {
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line).append(System.lineSeparator());
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
        String found = "";
        String fullPath = this.getFullPath();

        try (BufferedReader br = new BufferedReader(new FileReader(fullPath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains(sentence)) {
                    found = line;
                    break;
                }
            }
        } catch (FileNotFoundException _) {
            System.err.println("[SEARCH ERROR] Data file not found at: " + fullPath +
                    " | Verify file existence and permissions");
        } catch (IOException e) {
            System.err.println("[SEARCH ERROR] Failed to search in file: " + this.fileName +
                    " | Error: " + e.getMessage());
        }

        return found;
    }

    private void clearFile() {
        String fullPath = this.getFullPath();

        File file = new File(fullPath);

        try {
            // Open e close immediately (truncate mode)
            new FileOutputStream(file).close();
            System.out.println("[INFO] File initialized/truncated: " + file.getAbsolutePath());
        } catch (FileNotFoundException _) {
            System.err.println("[ERROR] File not accessible: " + file.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("[ERROR] Clear operation failed for '" + file.getName() +
                    "' at " + file.getParent() + ". Details: " + e.getMessage());
        }
    }

    private List<String> getContentList() throws FilePersistenceException {
        String content = this.findAll();
        return new ArrayList<>(Stream.of(content.split(System.lineSeparator())).toList());
    }

    private String getFullPath() {
        return this.currentDir + STORED_DIR + this.fileName;
    }
}