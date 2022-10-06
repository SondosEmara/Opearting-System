/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package systemallocation;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

//FileSystem is manage to the file that will load in first step.(to get the data)
public class FileSystem {

    private BufferedWriter writer;
    private String fileName;
    private Directory tree;

    public FileSystem(String fileName) {
        this.fileName = fileName;
    }

    //setBuffer is to make value of writer.
    void setBuffer() {
        try {
            writer = new BufferedWriter(new FileWriter(fileName));
            this.writer = writer;
        } catch (IOException ex) {
            Logger.getLogger(FileSystem.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //updtae file after each creation new file or folder or delete new file or folder.
    //this function is similar to the print tree in tree.java.
    void updateFile(Directory tree) throws IOException {

        for (int i = 0; i < tree.spaceCount; i++) {
            writer.write(" ");
        }

        writer.write(tree.path);
        writer.newLine();

        for (Directory subDir : tree.subDirectories) {
            updateFile(subDir);
        }

        for (File file : tree.files) {
            for (int i = 0; i < file.spaceCount; i++) {
                writer.write(" ");
            }
              writer.write(file.filePath);
            for(int i=0;i<file.allocatedBlocks.size();i++){
              writer.write(":"+file.allocatedBlocks.get(i));
            }
            writer.newLine();
        }
    }

    void loadToFile(Directory tree, Technique tType) throws IOException {

        this.setBuffer();
        String blocks = "";
        //first put  the blocks sequence in the first line of file.
        if(tType instanceof ContiguousAllocation){
            ContiguousAllocation t1=(ContiguousAllocation)tType;
            for (int i = 0; i < t1.blocks.size(); i++) {
                blocks += Integer.toString(t1.blocks.get(i));
            }
        }else if(tType instanceof IndexedAllocation){
            IndexedAllocation t1=(IndexedAllocation)tType;
            for (int i = 0; i < t1.allBlocks.size(); i++) {
                blocks += Integer.toString(t1.allBlocks.get(i));
            }
        }if(tType instanceof LinkedAllocation){
            LinkedAllocation t1=(LinkedAllocation)tType;
            for (int i = 0; i < t1.allBlocks.size(); i++) {
                blocks += Integer.toString(t1.allBlocks.get(i));
            }
        }
        //write it to file.
        writer.write(blocks);
        writer.newLine();
        //uodateFile functio to store the tree itself in file.
        this.updateFile(tree);
        writer.close();
    }

    //Intial when the program will start we will get data from file to load to the tree code structure.
    Tree getFromFile(Technique techinque1) throws FileNotFoundException {

        //inialize a new tree
        Tree tree = new Tree(new Directory("root", "root"));

        FileInputStream fileStream = new FileInputStream(fileName);
        Scanner sc = new Scanner(fileStream);    //file to be scanned  

        //First line in file is the blocks Seqyence.
        //check to the file contain data (not empty).
        if (sc.hasNextLine()) {
            String sequenceBlocks = (sc.nextLine());
            //store that seqyence in techinque1 (Technique).
            techinque1.convertToArray(sequenceBlocks);
        }

        //make tree structure contains files and folders.
        while (sc.hasNextLine()) {

            String orignialLine=sc.nextLine();
            String[] line = (orignialLine).split(":");
            String str = line[0].replaceAll(" ", "");
            
            //check if the line is corresponding to file or folder
            //becsuse if is file that is mean the file hava startBlock and EndBlock.
            ArrayList <Integer>fileBlocks=new ArrayList<>();
            if (line.length>=3) {
 
                //store in file blocks.
                for(int i=1;i<line.length;i++){
                  fileBlocks.add(Integer.parseInt(line[i]));
                }
               
                tree.insertNewElement(str,fileBlocks);
                techinque1.storeFiles(orignialLine);
            } else if (line.length == 1 && !"root".equals(line[0])) {
                tree.insertNewElement(str,new ArrayList<>());
            }
        }
        sc.close();     //closes the scanner  
        return tree;

    }

}
