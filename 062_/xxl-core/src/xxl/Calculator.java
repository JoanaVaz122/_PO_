package xxl;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Scanner;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import xxl.exceptions.ImportFileException;
import xxl.exceptions.MissingFileAssociationException;
import xxl.exceptions.UnavailableFileException;
import xxl.exceptions.UnrecognizedEntryException;

/**
 * Class representing a spreadsheet application.
 */
public class Calculator {

    /** The current spreadsheet. */
    private Spreadsheet _spreadsheet = null;
    private User _activeUser = new User();


    /**
     * Saves the serialized application's state into the file associated to the current network.
     *
     * @throws FileNotFoundException if for some reason the file cannot be created or opened. 
     * @throws MissingFileAssociationException if the current network does not have a file.
     * @throws IOException if there is some error while serializing the state of the network to disk.
     */
    public void save() throws FileNotFoundException, MissingFileAssociationException, IOException {
        if (_spreadsheet.getFilename() == null || _spreadsheet.getFilename().equals(""))
            throw new MissingFileAssociationException();
        try (ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(_spreadsheet.getFilename())))) {
            oos.writeObject(_spreadsheet);
            _spreadsheet.setChanged(false);
        }
    }


    /**
     * Saves the serialized application's state into the specified file. The current network is
     * associated to this file.
     *
     * @param filename the name of the file.
     * @throws FileNotFoundException if for some reason the file cannot be created or opened.
     * @throws MissingFileAssociationException if the current network does not have a file.
     * @throws IOException if there is some error while serializing the state of the network to disk.
     */
    public void saveAs(String filename) throws FileNotFoundException, MissingFileAssociationException, IOException {
        
        _spreadsheet.setFilename(filename);
        save();
    }


    /**
     * @param filename name of the file containing the serialized application's state
     *        to load.
     * @throws UnavailableFileException if the specified file does not exist or there is
     *         an error while processing this file.
     */
    public void load(String filename) throws UnavailableFileException {
        
        try (ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(filename)))) {
            _spreadsheet = (Spreadsheet) ois.readObject();
            _spreadsheet.setChanged(false);
            _spreadsheet.setFilename(filename); // = filename;
        }catch(ClassNotFoundException | IOException e){
            throw new UnavailableFileException(filename);
        }
    }


    /**
     * Read text input file and create domain entities..
     *
     * @param filename name of the text input file
     * @throws ImportFileException
     */
    
    public void importFile(String filename) throws ImportFileException {
        try (Scanner fileReader = new Scanner(new File(filename))){
            
            int lines = (Integer.valueOf(fileReader.nextLine().split("=")[1]));
            int columns = (Integer.valueOf(fileReader.nextLine().split("=")[1]));
            _spreadsheet = new Spreadsheet(lines, columns);

            while(fileReader.hasNextLine()){
                String[] entry = fileReader.nextLine().split("[|]");
                if(entry.length == 2){
                    _spreadsheet.insertContents(entry[0], entry[1]);
                } 
            }

            fileReader.close();
        } catch (IOException | UnrecognizedEntryException e ) { 
            throw new ImportFileException(filename, e);

        }
    }


    public boolean changed() {
        return _spreadsheet.hasChanged();
    }


    public void createSpreadsheet(int lines, int columns){
        _spreadsheet = new Spreadsheet(lines, columns);
        _spreadsheet.addUser(_activeUser);
    }


    public Spreadsheet getSpreadsheet() {
        return _spreadsheet;
    }
}