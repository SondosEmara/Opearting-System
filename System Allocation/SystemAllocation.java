package systemallocation;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class SystemAllocation {

    static String filePath, fileSize;
    static Tree tree;

    static void createNewFile(Technique techinque1) throws FileNotFoundException, IOException {

        /*//check first if the path is exist or not 
        //by delete the end of the path and check with new path
        /*int index = filePath.lastIndexOf("/");
        String newPath = filePath.substring(0, index);*/
        //to know what is the reason to not add new folder.
        int flags[] = {1, 1};

        tree.search(filePath, flags);

        if (flags[1] == 0) {
            System.out.println("The path does not exist");
        } else if (flags[0] == 0) {
            System.out.println("The file already exists");
        } else if (techinque1 instanceof ContiguousAllocation) {
            
            if (techinque1.ifExistBlocks(Integer.parseInt(fileSize))) {
                ContiguousAllocation t1 = (ContiguousAllocation) techinque1;
                int startBlock = t1.tempstartBlock;
                int endBlock = t1.tempendBlock;
                
                ArrayList<Integer> fileBlocks = new ArrayList<>();
                fileBlocks.add(startBlock);
                fileBlocks.add(endBlock);
                
                
                tree.insertNewElement(filePath, fileBlocks);
                filePath = filePath + ":" + startBlock + ":" + endBlock;
                techinque1.storeFiles(filePath);
            } else {
                System.out.println("There is no enough blocks to store this file in disk");
            }
        } else if(techinque1 instanceof IndexedAllocation){
            if(techinque1.ifExistBlocks(Integer.parseInt(fileSize))){
                IndexedAllocation t1 = (IndexedAllocation) techinque1;
                tree.insertNewElement(filePath, t1.blocksToBeAllocated);

                for(int i = 0; i < t1.blocksToBeAllocated.size(); i++)
                    filePath += ":" + t1.blocksToBeAllocated.get(i);

                techinque1.storeFiles(filePath);
            }
            else
                System.out.println("There is no enough blocks to store this file in disk");
        }

        else if(techinque1 instanceof LinkedAllocation) {
            if(techinque1.ifExistBlocks(Integer.parseInt(fileSize))) {
                LinkedAllocation t1 = (LinkedAllocation) techinque1;
                tree.insertNewElement(filePath, t1.blocksToBeAllocated);

                for(int i = 0; i < t1.blocksToBeAllocated.size(); i++)
                    filePath += ":" + t1.blocksToBeAllocated.get(i);

                techinque1.storeFiles(filePath);
            }
            else
                System.out.println("There is no enough blocks to store this file in disk");
        }

    }

    static void createFolder() throws FileNotFoundException {

        //to know what is the reason to not add new folder.
        int flags[] = {1, 1};

        tree.search(filePath, flags);
        if (flags[0] == 0) {
            System.out.println("The Folder is already Exist.");
        } else if (flags[1] == 0) {
            System.out.println("The path does not exist");
        } else {
            tree.insertNewElement(filePath, new ArrayList<>());
        }

    }

    static void delete(Technique t1) {
        int flag[] = {1, 1};
        tree.search(filePath, flag);
        if (flag[0] == 0) {
            tree.remove(filePath);
            t1.updateAfterDelete(filePath);
        } else {
            System.out.println("The path does not exist");
        }

    }

    public static void main(String[] args) throws FileNotFoundException, IOException {

        Scanner input = new Scanner(System.in);
        int totalSize=400;
        //intial get data from file
        String fileName = "C:\\Users\\c\\Desktop\\data.txt";
        FileSystem file = new FileSystem(fileName);



        System.out.println("Please choose allocation technique 1, 2 or 3:\n " +
                "1- Contiguous Allocation\n" +
                " 2- Indexed Allocation\n" +
                " 3- Linked Allocation\n"
        );
        int choice = input.nextInt();
        if(choice == 1){
            //1 --> is block size.
            Technique techinque1 = new ContiguousAllocation(totalSize, 1);
            //get data from file and store it in tree structure.
            tree = file.getFromFile(techinque1);
            int flag = 1;
            while (flag != 0) {
                String command;
                input = new Scanner(System.in);
                System.out.println("\n-------------------------------------------------\n");
                System.out.println("\nEnter the command");
                command = input.nextLine();
                String[] splitArray = command.split(" ");
                switch (splitArray[0]) {
                    case "CreateFile":
                        //check first if the file is exists or not (to avoid duplicate files).
                        filePath = splitArray[1];
                        fileSize=splitArray[2];
                        createNewFile(techinque1);
                        System.out.println("The Tree Structure after executing the command");
                        tree.root.printTree();
                        break;
                    case "CreateFolder":
                        filePath = splitArray[1];
                        createFolder();
                        System.out.println("The Tree Structure after executing the command");
                        tree.root.printTree();
                        break;
                    case "DeleteFile":
                        filePath = splitArray[1];
                        delete(techinque1);
                        System.out.println("The Tree Structure after executing the command");
                        tree.root.printTree();
                        break;
                    case "DeleteFolder":
                        filePath = splitArray[1];
                        delete(techinque1);
                        System.out.println("The Tree Structure after executing the command");
                        tree.root.printTree();
                        break;
                    case "DisplayDiskStatus":
                        System.out.println("-------------the disk status--------------");
                        techinque1.DisplayDiskStatus();
                        break;
                    case "DisplayDiskStructure":
                        System.out.println("-------------The Tree Structure--------------");
                        tree.root.printTree();
                        break;
                    case "Exit":
                        flag=0;
                        file.loadToFile(tree.root, techinque1);
                        break;
                    default:
                        System.out.println("Enter correct Command Line");
                        break;
                }
            }
        }
        else if(choice == 2){
            //1 --> is block size.
            Technique techinque1 = new IndexedAllocation(totalSize, 1);
            //get data from file and store it in tree structure.
            tree = file.getFromFile(techinque1);
            boolean flag = true;
            while (flag != false) {
                String command;
                input = new Scanner(System.in);
                System.out.println("\n-------------------------------------------------\n");
                System.out.println("\nEnter the command");
                command = input.nextLine();
                String[] splitArray = command.split(" ");
                switch (splitArray[0]) {
                    case "CreateFile":
                        //check first if the file is exists or not (to avoid duplicate files).
                        filePath = splitArray[1];
                        fileSize=splitArray[2];
                        createNewFile(techinque1);
                        System.out.println("The Tree Structure after executing the command");
                        tree.root.printTree();
                        break;
                    case "CreateFolder":
                        filePath = splitArray[1];
                        createFolder();
                        System.out.println("The Tree Structure after executing the command");
                        tree.root.printTree();
                        break;
                    case "DeleteFile":
                        filePath = splitArray[1];
                        delete(techinque1);
                        System.out.println("The Tree Structure after executing the command");
                        tree.root.printTree();
                        break;
                    case "DeleteFolder":
                        filePath = splitArray[1];
                        delete(techinque1);
                        System.out.println("The Tree Structure after executing the command");
                        tree.root.printTree();
                        break;
                    case "DisplayDiskStatus":
                        System.out.println("-------------the disk status--------------");
                        techinque1.DisplayDiskStatus();
                        break;
                    case "DisplayDiskStructure":
                        System.out.println("-------------The Tree Structure--------------");
                        tree.root.printTree();
                        break;
                    case "Exit":
                        flag=false;
                        file.loadToFile(tree.root, techinque1);
                        break;
                    default:
                        System.out.println("Enter correct Command Line");
                        break;
                }
            }
        }

        else if(choice == 3){
            //1 --> is block size.
            Technique techinque1 = new LinkedAllocation(totalSize, 1);
            //get data from file and store it in tree structure.
            tree = file.getFromFile(techinque1);
            boolean flag = true;
            while (flag != false) {
                String command;
                input = new Scanner(System.in);
                System.out.println("\n-------------------------------------------------\n");
                System.out.println("\nEnter the command");
                command = input.nextLine();
                String[] splitArray = command.split(" ");
                switch (splitArray[0]) {
                    case "CreateFile":
                        //check first if the file is exists or not (to avoid duplicate files).
                        filePath = splitArray[1];
                        fileSize=splitArray[2];
                        createNewFile(techinque1);
                        System.out.println("The Tree Structure after executing the command");
                        tree.root.printTree();
                        break;
                    case "CreateFolder":
                        filePath = splitArray[1];
                        createFolder();
                        System.out.println("The Tree Structure after executing the command");
                        tree.root.printTree();
                        break;
                    case "DeleteFile":
                        filePath = splitArray[1];
                        delete(techinque1);
                        System.out.println("The Tree Structure after executing the command");
                        tree.root.printTree();
                        break;
                    case "DeleteFolder":
                        filePath = splitArray[1];
                        delete(techinque1);
                        System.out.println("The Tree Structure after executing the command");
                        tree.root.printTree();
                        break;
                    case "DisplayDiskStatus":
                        System.out.println("-------------the disk status--------------");
                        techinque1.DisplayDiskStatus();
                        break;
                    case "DisplayDiskStructure":
                        System.out.println("-------------The Tree Structure--------------");
                        tree.root.printTree();
                        break;
                    case "Exit":
                        flag=false;
                        file.loadToFile(tree.root, techinque1);
                        break;
                    default:
                        System.out.println("Enter correct Command Line");
                        break;
                }
            }
        }
    }

}
