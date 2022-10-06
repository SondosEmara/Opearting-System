/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cli;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Scanner;

class Parser {

    private String currentPath;
    private String commandName;
    private String[] args;
    private ArrayList argsTemp;
    private boolean ifFileStore;
    private String fileName;

    Parser() {
        ifFileStore = false;
    }

    /*
    becuase i dont know the String[]args Size,so i need to store first arguments in ArrayList argsTemp
    and then store the data to String[]args when call function getArgs*/
    public void setPath(String currentPath) {
        //set the current path .
        System.setProperty("user.dir", currentPath);
        this.currentPath = System.getProperty("user.dir");
    }

    public boolean parse(String input) {

        this.argsTemp = new ArrayList();
        String[] subString = input.split(" ");
        int len = subString.length; //3
        String storeData = "";
        commandName = len != 0 ? subString[0] : "";

        //it<len-1
        for (int it = 0;; it++) {

            switch (commandName) {

                case ("echo") -> {
                    if (len == 1) {
                        return false;
                    } else if (subString[it + 1].equals(">") && it + 2 == len - 1) {
                        argsTemp.add(storeData);
                        ifFileStore = true;
                        fileName = subString[it + 2];
                        return true;
                    } else if (it == len - 2) {
                        argsTemp.add(storeData + subString[it + 1]);
                        return true;
                    }
                    storeData += subString[it + 1] + ' '; //ahmed ali ibrahim
                }

                case ("pwd") -> {
                    if (len == 3 && subString[1].equals(">")) {
                        ifFileStore = true;
                        fileName = subString[it + 2];
                        return true;
                    } else {
                        return len == 1;
                    }
                }

                case ("cd") -> {
                    if (len < 1 || len > 2) {
                        return false;
                    } else if (len == 2) {
                        argsTemp.add(subString[it + 1]);
                    }
                    return true;
                }

                case ("ls") -> {
                    if (len == 2 && "-r".equals(subString[1])) {
                        commandName += ' ' + "-r";
                        return true;
                    } else if (((len == 4 && "-r".equals(subString[1])) && ">".equals(subString[2]))) {
                        commandName += ' ' + "-r";
                        fileName = subString[it + 3];
                        ifFileStore = true;
                        return true;

                    } else if ((len == 3 && ">".equals(subString[1]))) {

                        fileName = subString[it + 2];
                        ifFileStore = true;
                        return true;
                    } else {
                        return len == 1;
                    }
                }

                case ("cp") -> {
                    if (len == 3) {
                        argsTemp.add(subString[it + 1]);
                        if (it == 1) {
                            return true;
                        }
                    } else {
                    }
                }

                case ("cp-r") -> {
                    if (len == 3) {
                        argsTemp.add(subString[it + 1]);
                        if (it == 1) {
                            return true;
                        }
                    } else {
                    }
                }

                case ("rm") -> {
                    if (len == 2) {
                        argsTemp.add(subString[it + 1]);
                        return true;
                    } else {
                        return false;
                    }
                }

                case ("cat") -> {
                    if (len == 2) {
                        argsTemp.add(subString[it + 1]);
                        return true;
                    } else if (len == 3) {
                        argsTemp.add(subString[it + 1]);
                        if (it == 1) {
                            return true;
                        }
                    } else if (len == 4 && ">".equals(subString[2])) {
                        argsTemp.add(subString[it + 1]);
                        fileName = subString[it + 3];
                        ifFileStore = true;
                        return true;
                    } else if (len == 5 && ">".equals(subString[3])) {
                        argsTemp.add(subString[it + 1]);
                        if (it == 1) {
                            fileName = subString[it + 3];
                            ifFileStore = true;
                            return true;
                        }
                    } else {
                        return false;
                    }
                }
                case ("touch"),("rmdir") -> {
                    if (subString.length == 2) {
                        argsTemp.add(subString[1]);
                        return true;
                    } else {
                        return false;
                    }
                }
                case ("mkdir") -> {
                    if (subString.length >= 2) {
                        argsTemp.addAll(Arrays.asList(subString).subList(1, subString.length));
                        return true;
                    } else {
                        return false;
                    }
                }

                default -> {
                    break;
                }

            }

            if (it >= len - 1) {
                break;
            }

        }

        commandName = "";
        return false;
    }

    public String getCommandName() {
        return commandName;
    }

    public String[] getArgs() {
        args = new String[argsTemp.size()];
        for (int i = 0; i < args.length; i++) {
            args[i] = (String) argsTemp.get(i);
        }
        return args;
    }

    public String getPath() {
        return (this.currentPath);
    }


    /*to only test*/
    public void print() {
        for (int i = 0; i < argsTemp.size(); i++) {
            System.out.println(argsTemp.get(i));
        }
    }

    void changefileState(boolean newState) {
        ifFileStore = newState;
    }

    boolean getFileState() {
        return ifFileStore;
    }

    String getFileName() {
        return fileName;
    }

}


class Terminal {

    private final Parser parser;

    public Terminal(Parser parser) {
        this.parser = parser;
    }

    public void Write_in_File(String args) throws IOException {
        File fileInput = new File(parser.getPath() + "\\" + parser.getFileName());
        if (!fileInput.exists()) {
            fileInput.createNewFile();
        }
        try ( FileWriter file_write = new FileWriter(fileInput)) {
            file_write.write(String.valueOf(args));
        } finally {
            parser.changefileState(false);
        }
    }

    //echo Command Implentaion
    public void echo(String[] args) throws IOException {
        if (parser.getFileState()) {
            Write_in_File("Result:: " + args[0]);
        } else {
            System.out.println("Result:: " + args[0]);
        }
    }

    //pwd Implentaion
    public void pwd() throws IOException {
        if (parser.getFileState()) {
            Write_in_File("Current Path Is:: " + parser.getPath());
        } else {
            System.out.println("Current Path Is:: " + parser.getPath());
        }
        //the same if print System.getProperty("user.dir");
    }

    /*
    cd Implentaion
    in cd Path will change dir to another dir  not file path.
     */
    public void cd(String[] args) throws Exception {

        String currentPath;
        if (args.length == 0) {
            //cd takes no arguments and changes the current path to the path of your home directory
            currentPath = System.getProperty("user.home");
            parser.setPath(currentPath);
            System.out.println("Current path Is:: " + currentPath);

        } //cd takes 1 argument which is â€œ..â€ (e.g. cd ..) and changes the current directory to the previous directory
        else if ("..".equals(args[0])) {

            currentPath = parser.getPath();
            File path = new File(currentPath);
            currentPath = path.getParent();
            parser.setPath(currentPath);
            System.out.println("Current path is::" + currentPath);

        } //cd takes 1 argument which is either the full path or the relative (short) path and changes the current path to that path.
        //if the path is absoluote.
        //cd +(dir path) not a file path.
        else if (new File(args[0]).exists() && !new File(args[0]).isFile()) {
            /*Because if the path is file then we refer to the dirctory path not the file path like in cmd*/
            currentPath = new File(args[0]).getCanonicalPath();
            parser.setPath(currentPath);
            System.out.println("Current path::" + currentPath);
        } //relative path and any other cases
        else {
            currentPath = ((args[0].charAt(0)) != '/') ? parser.getPath() + "\\" + args[0] : parser.getPath() + args[0];

            if (!new File(currentPath).exists() || new File(currentPath).isFile()) {
                throw new Exception("Erorr of args input.");
            }
            parser.setPath(new File(currentPath).getCanonicalPath());
            System.out.println("Current path is " + new File(currentPath).getCanonicalPath());
        }
    }

    public String[] liststheContents() {
        File director = new File(parser.getPath());
        String[] dirContent = director.list();
        assert dirContent != null;
        Arrays.sort(dirContent);
        return dirContent;
    }

    //ls implenation
    public void ls() throws IOException {
        if (parser.getFileState()) {
            String dirContent = "";
            for (String content : liststheContents()) {
                if (content.equals(liststheContents()[liststheContents().length - 1])) {
                    dirContent = dirContent + content;
                } else {
                    dirContent = dirContent + content + "\n";
                }
            }
            Write_in_File(dirContent);
            parser.changefileState(false);
        } else {
            String[] dirContent = liststheContents();
            for (String content : dirContent) {
                System.out.println(content);
            }
        }

    }

    //lsReverse implentation
    public void lsReverse() throws IOException {
        String[] dirContents = liststheContents();
        if (parser.getFileState()) {
            String dirContent = "";
            for (int counter = dirContents.length - 1; counter >= 0; counter--) {
                if (counter == 0) {
                    dirContent = dirContent + dirContents[counter];
                } else {
                    dirContent = dirContent + dirContents[counter] + "\n";
                }
            }
            Write_in_File(dirContent);
            parser.changefileState(false);
        } else {
            for (int counter = dirContents.length - 1; counter >= 0; counter--) {
                System.out.println(dirContents[counter]);
            }
        }

    }

    public static void copyFile(File a, File b) throws Exception {
        InputStream input = null;
        OutputStream output = null;
        try {
            input = new FileInputStream(a);
            output = new FileOutputStream(b);
            byte[] buffer = new byte[1024];

            int size;
            while ((size = input.read(buffer)) > 0) {
                output.write(buffer, 0, size);
            }
        } catch (Exception e) {
            input.close();
            output.close();
        }
    }

    public static void copyFolder(File source, File destination) throws Exception {
        if (source.isDirectory()) {
            if (!destination.exists()) {
                destination.mkdirs();
            }
            String All_Files[] = source.list();

            for (String file : All_Files) {
                File source_File = new File(source, file);
                File destination_File = new File(destination, file);
                copyFolder(source_File, destination_File);
            }
        } else {
            copyFile(source, destination);
        }
    }

    public void copy(String[] args, String copy_type) throws Exception {
        File f1 = new File(parser.getPath() + "\\" + args[0]);
        File f2 = new File(parser.getPath() + "\\" + args[1]);
        if (copy_type.equals("File")) {
            copyFile(f1, f2);
        } else if (copy_type.equals("Folder")) {
            copyFolder(f1, f2);
        }
    }

    public void Remove_File(String[] args) {
        try {
            File f1 = new File(parser.getPath() + "\\" + args[0]);
            if (f1.exists()) {
                Files.delete(f1.toPath());
            } else {
                System.out.println("File Not Found");
            }
        } catch (Exception r) {
            System.out.println("Remove Failed");
        }
    }

    public void Concatenates(String[] args) throws IOException {
        int filenum = 0;
        StringBuilder file1 = new StringBuilder();
        StringBuilder file2 = new StringBuilder();

        File f1 = new File(parser.getPath() + "\\" + args[0]);
        File f2 = (args.length == 2) ? new File(parser.getPath() + "\\" + args[1]) : null;

        FileInputStream file_read = new FileInputStream(f1);
        Scanner read = new Scanner(file_read);
        while (filenum < args.length) {
            while (read.hasNextLine()) {
                if (filenum == 0) {
                    file1.append(read.nextLine()).append("\n");
                } else {
                    file2.append(read.nextLine()).append("\n");
                }
            }
            if (args.length == 2) {
                file_read = new FileInputStream(f2);
                read = new Scanner(file_read);
            }
            filenum++;
        }
        file2.append(file1);

        if (parser.getFileState()) {
            Write_in_File(file2.toString());
            parser.changefileState(false);
        } else {
            System.out.print(file2);
        }
    }

    public void rmdir(String[] args) throws Exception {
        File file = new File(parser.getPath());
        if (args[0].equals("*")) {
            for (File fil : file.listFiles()) {
                if (fil.isDirectory()) {
                    fil.delete();
                }
            }
        } else if (new File(args[0]).isDirectory() && new File(args[0]).isAbsolute() && new File(args[0]).exists() && !file.isFile()) {
            //fullpath
            file = new File(args[0]);
            file.delete();
        } else if (!(new File(args[0])).isAbsolute()) {
            String currentPath = ((args[0].charAt(0)) != '/') ? parser.getPath() + "\\" + args[0] : parser.getPath() + args[0];
            //relative path
            if (!new File(currentPath).exists() || new File(currentPath).isFile()) {
                throw new Exception("Erorr of args input.");
            } else {
                file = new File(new File(currentPath).getCanonicalPath());
                file.delete();
            }
        }
    }

    public void touch(String[] args) throws IOException {

        //relative path
        if (!(new File(args[0])).isAbsolute()) {
            //relative path
            String currentPath = ((args[0].charAt(0)) != '/') ? parser.getPath() + "\\" + args[0] : parser.getPath() + args[0];
            File file = new File(new File(currentPath).getCanonicalPath());
            file.createNewFile();
        } //absolute path
        else if ((new File(args[0])).isAbsolute()) {
            File file = new File(args[0]);
            file.createNewFile();
        }

    }

    public void mkdir(String[] args) throws Exception {
        String currentPath;
        for (int i = 0; i < args.length; i++) {
            File f;
            if (!args[i].contains("\\")) {
                f = new File(parser.getPath() + "\\" + args[i]);
                if (!f.isDirectory()) {
                    f.mkdir();
                } else {
                    System.out.println("The directory is founded!");
                }
            } else if (!new File(args[i]).exists() && !new File(args[i]).isFile()) {
                currentPath = new File(args[i]).getCanonicalPath();
                f = new File(currentPath);
                f.mkdir();
            } else {
                currentPath = ((args[i].charAt(0)) != '/') ? parser.getPath() + "\\" + args[i] : parser.getPath() + args[i];

                if (!new File(currentPath).exists() || new File(currentPath).isFile()) {
                    throw new Exception("Erorr of args input.");
                }
                f = new File(parser.getPath());
                f.mkdir();
            }

        }
    }

//Implement each command in a method, for example:
    public void chooseCommandAction() throws Exception {
        switch (parser.getCommandName()) {
            case ("echo") ->
                echo(parser.getArgs());
            case ("pwd") ->
                pwd();
            case ("cd") ->
                cd(parser.getArgs());
            case ("ls") ->
                ls();
            case ("ls -r") ->
                lsReverse();
            case ("cp") ->
                copy(parser.getArgs(), "File");
            case ("cp-r") ->
                copy(parser.getArgs(), "Folder");
            case ("rm") ->
                Remove_File(parser.getArgs());
            case ("cat") ->
                Concatenates(parser.getArgs());
            case ("rmdir") ->
                rmdir(parser.getArgs());

            case ("touch") ->
                touch(parser.getArgs());

            case ("mkdir") ->
                mkdir(parser.getArgs());

        }
    }

    public Parser getParser() {
        return this.parser;
    }

    private void printPaths(File file) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}

public class CLI {

    public static void main(String[] args) throws Exception {

        //Assume the current path is "C:\\Users\\c\\Desktop\\Test\\OS1"
        String path = "C:\\Users\\c\\Desktop\\Test\\OS1";
        String input;
        Scanner inputData;
        Parser parser = new Parser();
        parser.setPath(path);

        while (true) {

            inputData = new Scanner(System.in);

            System.out.print("Enter the command :: ");
            input = inputData.nextLine();

            if (input.equalsIgnoreCase("exit")) {
                break;
            } else if (!parser.parse(input)) {
                System.out.println("Please Enter Correct Command");
            } else {
                Terminal terminal = new Terminal(parser);

                try {
                    terminal.chooseCommandAction();
                } catch (Exception error) {
                    System.out.println(error.getMessage());
                }
                //to update  the parser object from any changes.
                parser = terminal.getParser();
            }

        }
    }
}
