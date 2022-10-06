package systemallocation;

import java.util.ArrayList;
interface Technique {

    void makeIntailBlocks();
    void storeFiles(String filePath);
    boolean ifExistBlocks(int newFileSize);
    void DisplayDiskStatus();
    void updateAfterDelete(String path);
    void convertToArray(String sequenceBlocks);
}

class IndexedAllocation implements Technique {
    private int totalBlocksCount;
    int blockSize;
    // the index block is the index of the start and has an arrayList of the file allocated files -blocksToBeAllocated-
    int indexBlock;
    ArrayList<Integer> allBlocks = new ArrayList<>();
    ArrayList<String> allocatedFiles = new ArrayList<>();
    ArrayList<Integer> blocksToBeAllocated;

    int allocatedBlocks, freeBlocks, totalSize;
    IndexedAllocation(int totalSize, int blockSize){
        this.blockSize = blockSize;
        this.totalBlocksCount = totalSize;
        this.totalSize = this.totalBlocksCount * this.blockSize;
        this.allocatedBlocks =0;
        this.freeBlocks = totalSize;
        makeIntailBlocks();
    }

    @Override
    public void makeIntailBlocks() {
        for(int i=0; i< totalBlocksCount; i++){
            allBlocks.add(0);
        }
    }

    @Override
    public void storeFiles(String filePath) {
        allocatedFiles.add(filePath);
    }

    @Override
    public boolean ifExistBlocks(int newFileSize) {
        blocksToBeAllocated = new ArrayList<>();
        for(int i = 0; i < allBlocks.size(); i++) {
            if(allBlocks.get(i) == 0){
                if(blocksToBeAllocated.size() == (newFileSize + 1))
                    break;
                blocksToBeAllocated.add(i);
            }
        }
        if(blocksToBeAllocated.size() == (newFileSize + 1) ) {
            // The index block is the first block and has an arrayList containing each block allocated to the file.
            indexBlock = blocksToBeAllocated.get(0);
            updateBlocks(blocksToBeAllocated);
            return true;
        }
        return false;
    }

    @Override
    public void DisplayDiskStatus() {
        int flag =1;
        System.out.println("-----------------------The Empty Space (Reminder Space)----------------------");
        System.out.println(freeBlocks);

        System.out.println("-----------------------The Allocated Space-----------------------------------");
        System.out.println(allocatedBlocks);

        System.out.println("-----------------------The Empty Blocks in Disk------------------------------");

        for (int i = 0; i < totalBlocksCount; i++) {
            if (allBlocks.get(i) == 0) {
                System.out.println("Block ID : " + i);
                flag = 0;
            }
        }

        if (flag == 1) {
            System.out.println("There is no empty blocks in disk.");
        }
        flag = 1;
        System.out.println("-----------------------The Allocated Blocks in Disk-------------------------");
        for (int i = 0; i < totalBlocksCount; i++) {
            if (allBlocks.get(i) == 1) {
                System.out.println("Block ID : " + i);
                flag = 0;
            }
        }

        System.out.println("-----------------------The Allocated Files Name in Disk-------------------------");
        for (int i = 0; i < allocatedFiles.size(); i++) {
            String file = allocatedFiles.get(i).replaceAll(" ", "");
            System.out.println(file);
        }

        if (flag == 1) {
            System.out.println("There is no allocated blocks in disk.");
            System.out.println("There is no allocated files in disk.");
        }

    }

    public void updateBlocks(ArrayList<Integer> blocks) {
        for(int block : blocks){
            allBlocks.set(block, 1);
        }
        allocatedBlocks += blocks.size() ;
        freeBlocks -= blocks.size() ;
    }

    @Override
    public void updateAfterDelete(String path) {
        ArrayList<String> storeMatch = new ArrayList<>();
        for(int i=0; i<allocatedFiles.size(); i++){
            String element = allocatedFiles.get(i);

            if(element.contains(path)){
                String[] line = (element).split(":");
                ArrayList<Integer> blocksToBeDeleted = new ArrayList<>();
                int count =0;
                for(String s : line){
                    if(count == 0){
                        count ++;
                    }else{
                        blocksToBeDeleted.add(Integer.parseInt(s));
                    }
                }
                returnTo0(blocksToBeDeleted);
                storeMatch.add(element);
                break;
            }
        }
        allocatedFiles.removeAll(storeMatch);
    }

    @Override
    public void convertToArray(String sequenceBlocks) {
        for (int i = 0; i < sequenceBlocks.length(); i++) {
            if (sequenceBlocks.charAt(i) == '1') {
                allBlocks.set(i, 1);
                allocatedBlocks += blockSize;
                freeBlocks -= blockSize;
            } else {
                allBlocks.set(i, 0);
            }

        }
    }

    void returnTo0(ArrayList<Integer> blocks) {
        for (int block : blocks) {
            allBlocks.set(block, 0);
        }
        allocatedBlocks -= (blocks.size()) * blockSize ;
        freeBlocks += (blocks.size()) * blockSize;
    }
}









class LinkedAllocation implements Technique {
    private int totalBlocksCount;
    int blockSize;
    // the index block is the index of the start and has an arrayList of the file allocated files -blocksToBeAllocated-
    int indexBlock;
    ArrayList<Integer> allBlocks = new ArrayList<>();
    ArrayList<String> allocatedFiles = new ArrayList<>();
    ArrayList<File> allocatedFileObjects = new ArrayList<>();
    ArrayList<Integer> blocksToBeAllocated;

    int allocatedBlocks, freeBlocks, totalSize;
    LinkedAllocation(int totalSize, int blockSize){
        this.blockSize = blockSize;
        this.totalBlocksCount = totalSize;
        this.totalSize = this.totalBlocksCount * this.blockSize;
        this.allocatedBlocks =0;
        this.freeBlocks = totalSize;
        makeIntailBlocks();
    }

    @Override
    public void makeIntailBlocks() {
        for(int i=0; i< totalBlocksCount; i++){
            allBlocks.add(0);
        }
    }

    @Override
    public void storeFiles(String filePath) {
        allocatedFiles.add(filePath);
    }

    @Override
    public boolean ifExistBlocks(int newFileSize) {
        int start = -1, end = 0;
        blocksToBeAllocated = new ArrayList<Integer>();
        for(int i = 0; i < allBlocks.size(); i++) {
            if(allBlocks.get(i) == 0) {
                if(start < 0) start = i;
                if(blocksToBeAllocated.size() == (newFileSize)) {
                    end = i - 1;
                    break;
                }
                blocksToBeAllocated.add(i);
            }
        }



        if(blocksToBeAllocated.size() == (newFileSize) ) {
            blocksToBeAllocated.add(0, end);
            blocksToBeAllocated.add(0, start);
            updateBlocks(blocksToBeAllocated);
            return true;
        }

        return false;
    }

    @Override
    public void DisplayDiskStatus() {
        int flag = 1;
        System.out.println("-----------------------The Empty Space (Reminder Space)----------------------");
        System.out.println(freeBlocks);

        System.out.println("-----------------------The Allocated Space-----------------------------------");
        System.out.println(allocatedBlocks);

        System.out.println("-----------------------The Empty Blocks in Disk------------------------------");

        for (int i = 0; i < totalBlocksCount; i++) {
            if (allBlocks.get(i) == 0) {
                System.out.println("Block ID : " + i);
                flag = 0;
            }
        }

        if (flag == 1) {
            System.out.println("There is no empty blocks in disk.");
        }
        flag = 1;
        System.out.println("-----------------------The Allocated Blocks in Disk-------------------------");
        for (int i = 0; i < totalBlocksCount; i++) {
            if (allBlocks.get(i) == 1) {
                System.out.println("Block ID : " + i);
                flag = 0;
            }
        }

        System.out.println("-----------------------The Allocated Files Name in Disk-------------------------");
        for (int i = 0; i < allocatedFiles.size(); i++) {
            String file = allocatedFiles.get(i).replaceAll(" ", "");
            System.out.println(file);
        }

        if (flag == 1) {
            System.out.println("There is no allocated blocks in disk.");
            System.out.println("There is no allocated files in disk.");
        }

    }

    public void updateBlocks(ArrayList<Integer> blocks) {
        for(int block : blocks){
            allBlocks.set(block, 1);
        }
        allocatedBlocks += blocks.size() - 2 ;
        freeBlocks = (freeBlocks - blocks.size()) + 2;

    }

    @Override
    public void updateAfterDelete(String path) {
        ArrayList<String> storeMatch = new ArrayList<>();
        for(int i=0; i<allocatedFiles.size(); i++){
            String element = allocatedFiles.get(i);

            if(element.contains(path)){
                String[] line = (element).split(":");
                ArrayList<Integer> blocksToBeDeleted = new ArrayList<>();
                int count =0;
                for(String s : line){
                    if(count == 0){
                        count ++;
                    }else{
                        blocksToBeDeleted.add(Integer.parseInt(s));
                    }
                }
                returnTo0(blocksToBeDeleted);
                storeMatch.add(element);
                break;
            }
        }
        allocatedFiles.removeAll(storeMatch);
    }

    @Override
    public void convertToArray(String sequenceBlocks) {
        for (int i = 0; i < sequenceBlocks.length(); i++) {
            if (sequenceBlocks.charAt(i) == '1') {
                allBlocks.set(i, 1);
                allocatedBlocks += blockSize;
                freeBlocks -= blockSize;
            } else {
                allBlocks.set(i, 0);
            }

        }
    }

    void returnTo0(ArrayList<Integer> blocks) {
        for (int block : blocks) {
            allBlocks.set(block, 0);
        }
        allocatedBlocks -= ((blocks.size()) * blockSize) - 2 ;
        freeBlocks += ((blocks.size()) * blockSize) - 2;
    }
}






class ContiguousAllocation   implements Technique {

    private int blocksCount;
    int blockSize;
    ArrayList<Integer> blocks = new ArrayList<>();
    //store the all files that be allocate using contiguios technique.
    ArrayList<String> filesAllocated = new ArrayList<>();

    int totalSpace;
    int allocatedSpace;
    int reminderSpace;
    int tempstartBlock, tempendBlock;

    ContiguousAllocation (int blocksCount, int blockSize) {
        this.blockSize = blockSize;
        this.blocksCount = blocksCount;
        totalSpace = blocksCount * this.blockSize;
        allocatedSpace = 0;
        reminderSpace = totalSpace;
        makeIntailBlocks();
    }

    //to put all the blocks is 0 in the arary.
    @Override
    public void makeIntailBlocks() {
        for (int i = 0; i < blocksCount; i++) {
            blocks.add(0);
        }
    }

    //Best Fit
    //if existblocks to check the new file request size can exist blocks to store it or not?
    @Override
    public void storeFiles(String filePath) {
        // System.out.println(filePath);
        filesAllocated.add(filePath);
    }

    @Override
    public boolean ifExistBlocks(int newFileSize) {
        int requestBlocks = newFileSize / blockSize;
        int j, i;
        int finalstartBlock = -1;
        int finalendBlock = -1;

        for (i = 0; i < blocksCount; i++) {
            for (j = i; j < blocksCount && blocks.get(j) == 0; j++) {
            }

            //get min sequence and in the time is greater than or equal to requestBlocks.
            //to make best fit.
            if ((j - i) >= requestBlocks && j >= finalendBlock && i >= finalstartBlock) {
                finalendBlock = j - 1;
                finalstartBlock = i;
            }
        }
        //to make update of the blocks sequence array.
        //and update the allocatespace and reminderspace.
        if (finalendBlock != -1) {
            tempendBlock = finalendBlock;
            tempstartBlock = finalstartBlock;
            updateBlocks(finalstartBlock, finalendBlock);
            return true;
        } else {
            return false;
        }

    }

    public void updateBlocks(int startBlock, int endBlock) {
        for (int i = startBlock; i <= endBlock; i++) {
            blocks.set(i, 1);
        }
        endBlock += 1;
        allocatedSpace += (endBlock - startBlock) * blockSize;
        reminderSpace -= (endBlock - startBlock) * blockSize;
    }

    //when delete -->delete all files related to the deletefolderFile
    //path is the path that you need to delete it.
    @Override
    public void updateAfterDelete(String path) {
        ArrayList<String>storeMatch=new ArrayList<>();
        for (int i = 0; i < filesAllocated.size(); i++) {
            String target = filesAllocated.get(i);

            if (target.contains(path)) {

                String[] line = (target).split(":");
                returnTo0(Integer.parseInt(line[1]), Integer.parseInt(line[2]));
                storeMatch.add(target);
            }

        }
        filesAllocated.removeAll(storeMatch);

    }

    //print
    @Override
    public void DisplayDiskStatus() {
        int flag = 1;
        System.out.println("-----------------------ThE Empty Space (Reminder Space)----------------------");
        System.out.println(reminderSpace);

        System.out.println("-----------------------ThE Allocated Space-----------------------------------");
        System.out.println(allocatedSpace);

        System.out.println("-----------------------ThE Empty Blocks in Disk------------------------------");

        for (int i = 0; i < blocksCount; i++) {
            if (blocks.get(i) == 0) {
                System.out.println("Block ID : " + i);
                flag = 0;
            }
        }
        if (flag == 1) {
            System.out.println("NO exist Empty Blocks in Disk");
        }
        flag = 1;
        System.out.println("-----------------------ThE Allocated   Blocks in Disk-------------------------");
        for (int i = 0; i < blocksCount; i++) {
            if (blocks.get(i) == 1) {
                System.out.println("Block ID : " + i);
                flag = 0;
            }
        }
        System.out.println("-----------------------ThE Allocated   Files Name in Disk-------------------------");
        for (int i = 0; i < filesAllocated.size(); i++) {
            String file = filesAllocated.get(i).replaceAll(" ", "");
            System.out.println(file);
        }

        if (flag == 1) {
            System.out.println("NO exist Allocated Blocks in Disk");
            System.out.println("NO exist Files Allocated in Disk");
        }

    }

    //that function is pass to it the sequence blocks but in string. 
    @Override
    public void convertToArray(String sequenceBlocks) {
        for (int i = 0; i < sequenceBlocks.length(); i++) {

            if (sequenceBlocks.charAt(i) == '1') {
                blocks.set(i, 1);
                allocatedSpace += blockSize;
                reminderSpace -= blockSize;

            } else {
                blocks.set(i, 0);
            }

        }
    }

    void returnTo0(int startBlock, int endBlock) {
        for (int i = startBlock; i <= endBlock; i++) {
            blocks.set(i, 0);
        }
        endBlock += 1;
        allocatedSpace -= (endBlock - startBlock) * blockSize;
        reminderSpace += (endBlock - startBlock) * blockSize;

    }

}
