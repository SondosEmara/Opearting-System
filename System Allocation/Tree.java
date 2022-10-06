package systemallocation;

import java.util.ArrayList;
import java.util.Arrays;

class Tree {

    //root node
    Directory root;

    public Tree(Directory root) {
        this.root = root;
    }

    public String[] commonSteps(String path) {

        //not need the root word in path 
        //file path now is ex:/folder/x.txt
        path = path.substring(4);//folder
        String[] result = path.split("/");
        return result;
    }

    //in contiguous allocation the startBlockFile is the index of startBlock block.
    //in contiguous allocation the endBlockFile is the index of endBlock block.
    public void insertNewElement(String newPath, ArrayList<Integer> blocks) {
       
        String[] path = commonSteps(newPath);
        root.insert(root.path, path, blocks);
    }

    public void search(String newPath, int flags[]) {
        String[] path = commonSteps(newPath);
        root.ifExist(root.path, path, flags);
    }

    public void remove(String newPath) {
        String[] path = commonSteps(newPath);
        root.remove(root.path, path);

    }
}

//the class Directory like tree node.
class Directory {

    //Data Variables
    ArrayList<File> files;
    //the subdir is the node childern.
    ArrayList<Directory> subDirectories;
    int spaceCount;
    String path;
    String directoryName;

    //Functions
    public Directory(String directoryName, String path) {
        files = new ArrayList<File>();
        subDirectories = new ArrayList<Directory>();
        this.directoryName = directoryName;
        this.path = path;
        spaceCount = 0;
    }

    String[] removeNull(String[] newPath) {
        //Array.copyOfRange is to copy all elemets in list to another list except the first Element.
        //because after make split the first element is empty.
        while (newPath[0] == null || newPath[0].equals("")) {
            newPath = Arrays.copyOfRange(newPath, 1, newPath.length);
        }
        return newPath;
    }

    int getFileIndex(String targetPath) {
        int index = -1;
        //check with all files 
        for (int i = 0; i < files.size(); i++) {
            if (targetPath.equals(files.get(i).fileName)) {
                index = i;
                break;
            }
        }
        return index;

    }

    int getChildIndex(String directoryName) {
        int index = -1;
        //iterate to the subDir
        //if exist the same dirName -->then it is exist.
        for (int i = 0; i < subDirectories.size(); i++) {
            if (directoryName.equals(subDirectories.get(i).directoryName)) {
                index = i;
                break;
            }
        }
        return index;
    }

    //in the beginng the currentPath is root
    //ex:newPath is ["folder","g.txt"]-->newpath /folder/g.txt
    void insert(String currentPath, String[] newPath, ArrayList<Integer> blocks) {

        newPath = removeNull(newPath);

        //checkif newPath is lenght to 1 and is file format then make new file.
        //not make call the insert again because the path is always endBlock with txt file or folder.
        if (newPath.length == 1 && (newPath[0]).lastIndexOf('.') > 0) {

            File file = new File();
            file.filePath = (currentPath + "/" + newPath[0]);
            file.allocatedBlocks = blocks;
            file.fileName = newPath[0];
            file.spaceCount = spaceCount + 2;
            files.add(file);

        } //Directory.
        //ex: newPath is /folder/x.txt--> then
        //the make diretory this name is newPath[0] -->folder1
        //make the directory path is currentPath+/+newPath[0]-->root/folder1
        //and call the insert again with new child is path is-->root/folder1.
        else {
            //make new cildern
            Directory newChild = new Directory(newPath[0], (currentPath + "/" + newPath[0]));
            newChild.spaceCount = spaceCount + 2;

            //check if the newchild is already exist in childerns in tree.
            int index = getChildIndex(newPath[0]);

            //not exist then we will add the newchild in the subDirectories
            if (index == -1) {
                subDirectories.add(newChild);
            } //if exist not add newchild in subDirectories and continue call the insert with different paramter.
            else {

                newChild = subDirectories.get(index);
            }
            //check to stop call insert function .
            if (newPath.length != 1) {
                newChild.insert(newChild.path, Arrays.copyOfRange(newPath, 1, newPath.length), blocks);
            }
        }
    }

    void ifExist(String currentPath, String[] targetPath, int flags[]) {

        targetPath = removeNull(targetPath);

        //check if it is file --> mean we reach to the end of the targetpath
        if (targetPath.length == 1 && (targetPath[0]).lastIndexOf('.') > 0) {

            int index = getFileIndex(targetPath[0]);
            if (index != -1) {
                flags[0] = 0;
            }
        } //that is directory checks.
        else {
            //make new cildern
            Directory newChild = new Directory(targetPath[0], (currentPath + "/" + targetPath[0]));

            //check if the newchild is already exist in childerns in tree.
            int index = getChildIndex(targetPath[0]);

            //this mean the target path is already exist
            if (index != -1 && targetPath.length == 1) {
                flags[0] = 0;
            } //this mean the target path is not exist
            else if (index == -1 && targetPath.length > 1) {
                flags[1] = 0;
            } //if exist continue untill we reach the end of the targetpath.
            else if (index != -1) {
                Directory nextChild = subDirectories.get(index);
                //condtion to stop recurision
                if (targetPath.length != 1) {
                    nextChild.ifExist(newChild.path, Arrays.copyOfRange(targetPath, 1, targetPath.length), flags);
                }
            }
        }
    }

    public void printTree() {

        for (int i = 0; i < spaceCount; i++) {
            System.out.print(" ");
        }

        System.out.println(path);
        //iterate to all sub directories 
        for (Directory subDir : subDirectories) {
            subDir.printTree();
        }

        //because can not make call the printtree when print file because file is arraylist not directory.
        for (File file : files) {
            for (int i = 0; i < file.spaceCount; i++) {
                System.out.print(" ");
            }
            System.out.print(file.filePath);
            for (int i = 0; i < file.allocatedBlocks.size(); i++) {
                System.out.print(" " + file.allocatedBlocks.get(i));
            }
            System.out.println();
        }
    }

    //currnt path is intial is "root".
    void remove(String currentPath, String[] targetPath) {

        targetPath = removeNull(targetPath);
        //checkif newPath is lenght to 1 and is file format then make new file.
        //not make call the remove again because the path is always endBlock with txt file or folder.
        if (targetPath.length == 1 && (targetPath[0]).lastIndexOf('.') > 0) {
            //Get the index of the file to remove it from files.

            int index = getFileIndex(targetPath[0]);
            File target = files.get(index);
            files.remove(target);
        } else {
            //make new cildern
            Directory newChild = new Directory(targetPath[0], (currentPath + "/" + targetPath[0]));

            //check if the newchild is already exist in childerns in tree.
            int index = getChildIndex(targetPath[0]);

            Directory nextChild = subDirectories.get(index);
            //if reach to end of the path.
            if (targetPath.length == 1) {
                for (int i = 0; i < nextChild.subDirectories.size(); i++) {
                    nextChild.subDirectories.remove(nextChild.subDirectories.get(i));
                }
                subDirectories.remove(subDirectories.get(index));
            } //if exist not add newchild in subDirectories and continue call the insert with different paramter.
            else {
                nextChild.remove(newChild.path, Arrays.copyOfRange(targetPath, 1, targetPath.length));

            }
        }

    }

}

class File {

    String filePath;
    int spaceCount;
    ArrayList<Integer> allocatedBlocks = new ArrayList<>();
    String fileName;
}
