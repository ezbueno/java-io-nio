package developer.ezandro.persistence;

import developer.ezandro.exception.FilePersistenceException;

public interface FilePersistence {
    String write(final String data);
    boolean remove(final String sentence) throws FilePersistenceException;
    String replace(final String oldContent, final String newContent) throws FilePersistenceException;
    String findAll() throws FilePersistenceException;
    String findBy(final String sentence);
}