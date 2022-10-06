import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

class FCFS {
    int head;
    ArrayList<Integer> requests;
    ArrayList<Integer> sequence = new ArrayList<>();

    FCFS(int head, ArrayList<Integer> requests) {
        this.requests = new ArrayList<>(requests);
        this.head = head;
    }

    public void sequence() {
        sequence.add(head);
        sequence.addAll(requests);
        System.out.println("------------------------------Sequence of head movement to access the requested cylinders of FCFS Algorithm -----------");
        System.out.println(sequence);
    }

    public void totalMovements() {
        int total = 0;
        for (int i = 0; i < sequence.size() - 1; i++) {
            total += Math.abs(sequence.get(i + 1) - sequence.get(i));
        }
        System.out.println("--------------------------------------------Total FCFS head movement----------------------------------------------");
        System.out.println(total);
    }
}

class SSTF {
    int requestsNum, head;
    ArrayList<Integer> requests;
    ArrayList<Integer> sequence = new ArrayList<>();

    SSTF(int head, ArrayList<Integer> requests) {
        this.requests = new ArrayList<>(requests);
        this.requestsNum = requests.size();
        this.head = head;
    }

    public void sequence() {
        sequence.add(head);
        int close = head;
        for (int i = 0; i < requestsNum; i++) {
            int minIndex = 0;
            int difference;
            int min = Integer.MAX_VALUE;
            for (int j = 0; j < requests.size(); j++) {
                difference = Math.abs(requests.get(j) - close);
                if (difference < min) {
                    min = difference;
                    minIndex = j;
                }
            }
            sequence.add(requests.get(minIndex));
            close = requests.get(minIndex);
            requests.remove(requests.get(minIndex));
        }
        System.out.println("------------------------------Sequence of head movement to access the requested cylinders of SSTF Algorithm -----------");
        System.out.println(sequence);
    }

    public void totalMovements() {
        int total = 0;
        for (int i = 0; i < sequence.size() - 1; i++) {
            total += Math.abs(sequence.get(i + 1) - sequence.get(i));
        }
        System.out.println("--------------------------------------------Total SSTF head movement----------------------------------------------");
        System.out.println(total);
    }
}

class Circular {
    int head;
    int totalCscanMovements = 0;
    int totalClookMovements = 0;
    ArrayList<Integer> requests;
    ArrayList<Integer> CLookSequence = new ArrayList<>();
    ArrayList<Integer> CScanSequence = new ArrayList<>();

    Circular(int head, ArrayList<Integer> requests) {
        this.requests = new ArrayList<>(requests);
        this.head = head;
        Sequence();
        TotalMovements();
    }

    void Sequence() {
        requests.add(0);
        requests.add(head);
        requests.add(199);
        Collections.sort(requests);
        int start = requests.indexOf(head);
        for (int i = start; i >= 0; i--) {
            if (i == 0) CScanSequence.add(requests.get(i));
            else {
                CScanSequence.add(requests.get(i));
                CLookSequence.add(requests.get(i));
            }
        }
        int max = requests.indexOf(199);
        for (int i = max; i > start; i--) {
            CScanSequence.add(requests.get(i));
        }
        int maxRequest = requests.size() - 2;
        for (int i = maxRequest; i > start; i--) {
            CLookSequence.add(requests.get(i));
        }
    }

    void TotalMovements() {
        for (int i = 0; i < CLookSequence.size() - 1; i++) {
            totalClookMovements += Math.abs(CLookSequence.get(i + 1) - CLookSequence.get(i));
        }
        for (int i = 0; i < CScanSequence.size() - 1; i++) {
            totalCscanMovements += Math.abs(CScanSequence.get(i + 1) - CScanSequence.get(i));
        }
    }

    void CSCAN() {
        System.out.println("------------------------------Sequence of head movement to access the requested cylinders of C-Scan Algorithm -----------");
        System.out.println(CScanSequence);
        System.out.println("--------------------------------------------Total C-Scan head movement----------------------------------------------");
        System.out.println(totalCscanMovements);
    }

    void CLOOK() {
        System.out.println("------------------------------Sequence of head movement to access the requested cylinders of C-Look Algorithm -----------");
        System.out.println(CLookSequence);
        System.out.println("--------------------------------------------Total C-Look head movement----------------------------------------------");
        System.out.println(totalClookMovements);
    }
}


class Scan_ScanLook {

    private ArrayList<Integer> inputData;
    private ArrayList<Integer> scanSequence;
    private ArrayList<Integer> lookSequence;
    private int initialCylinder;
    private int headMovementScan;
    private int headMovementLOOK;

    Scan_ScanLook(int initialCylinder, ArrayList<Integer> queue) {
        inputData = new ArrayList<>(queue);
        scanSequence = new ArrayList<>();
        lookSequence = new ArrayList<>();
        this.initialCylinder = initialCylinder;
        headMovementLOOK = headMovementScan = 0;
    }


    //by sort data inputdata
    private void sortArray() {
        //sort arary from smaller to big number.
        inputData.add(initialCylinder);
        Collections.sort(inputData);
    }

    //by default make the direction is towards 0
    //look and scan algorithm--make two algorithms.
    private void applyAlgorithms() {

        //scan algorithm &&Lock Algorithm.
        //case 1 towards to 0.
        int startIndex = inputData.indexOf(initialCylinder), it, value;
        for (it = startIndex; it >= 0; it--) {
            value = inputData.get(it);
            scanSequence.add(value);
            lookSequence.add(value);
            if (it == 0) {
                headMovementScan += (value - 0);
                scanSequence.add(0);
            } else if (it != 0) {
                headMovementScan += (value - inputData.get(it - 1));
                headMovementLOOK += (value - inputData.get(it - 1));
            }
        }

        //case 2 when reverse after go to the 0.
        //case Scan
        value = inputData.get(startIndex + 1);
        scanSequence.add(value);
        headMovementScan += value;

        //case Look
        lookSequence.add(value);
        headMovementLOOK += value - inputData.get(0);

        for (it = startIndex + 2; it < inputData.size(); it++) {
            value = inputData.get(it);
            headMovementScan += (value - inputData.get(it - 1));
            headMovementLOOK += (value - inputData.get(it - 1));
            scanSequence.add(value);
            lookSequence.add(value);
        }
    }

    void doMechanism() {
        this.sortArray();
        this.applyAlgorithms();
    }

    void displayScanResult() {
        System.out.println("------------------------------Sequence of head movement to access the requested cylinders of Scan Algorithm -----------");
        System.out.println(scanSequence);
        System.out.println("--------------------------------------------Total head movement----------------------------------------------");
        System.out.println(headMovementScan);
    }

    void displayLookResult() {
        System.out.println("------------------------------Sequence of head movement to access the requested cylinders of Look Algorithm -----------");
        System.out.println(lookSequence);
        System.out.println("--------------------------------------------Total head movement----------------------------------------------");
        System.out.println(headMovementLOOK);
    }

    int getheadMovementLOOK() {
        return headMovementLOOK;
    }

    int getheadMovementScan() {
        return headMovementScan;
    }
}


/* The newly optimized algorithm works similarly to CLOOK and SCAN algorithms
instead it has a fixed initial head position at 0 and instead of serving requests in one direction
and then serving requests in the opposite direction, we can save the time of switching directions
by sorting requests in ascending order and serving requests in ascending order by moving in one direction
-from 0 to the last request- and by that saving the time of moving in two directions.
* */
class NewlyOptimizedAlgorithm{
    int head;
    ArrayList<Integer> requests;
    ArrayList <Integer> sequence = new ArrayList<>();

    NewlyOptimizedAlgorithm(ArrayList<Integer> requests){
        this.head = 0;
        this.requests = requests;
    }

    public void sequence(){
        int start =0;
        sequence.add(start);
        Collections.sort(requests);
        for(int i=0; i<requests.size(); i++){
            sequence.add(requests.get(i));
        }
        System.out.println("------------------------------Sequence of head movement to access the requested cylinders of Newly Optimized Algorithm -----------");
        System.out.println(sequence);

    }

    public void TotalMovements(){
        int total =0;
        for(int i=0;i<sequence.size() - 1; i++){
            total +=Math.abs(sequence.get(i+1)- sequence.get(i));
        }
        System.out.println("--------------------------------------------Total Newly Optimized Algorithm head movement----------------------------------------------");
        System.out.println(total);
    }
}
public class DiskScheduler {
    
      public static ArrayList<Integer> readFromFile(String fileName) {
        ArrayList<Integer> inputData = new ArrayList<>();
        try {
            int charInput;
            int integerValue = 0;
            BufferedReader bufferReader;
            File targetFile = new File(fileName);
            FileReader fileRead = new FileReader(targetFile);
            bufferReader = new BufferedReader(fileRead);
            do {
                charInput = bufferReader.read();
                if (charInput == 44 || charInput == -1) {
                    inputData.add(integerValue);
                    integerValue = 0;
                } else {
                    integerValue = integerValue * 10 + (charInput - 48);
                }
            } while (charInput != -1);
            bufferReader.close();
        } catch (IOException error) {
            System.out.println(error.getMessage());
        }
        return inputData;

    }

    public static void main(String[] args) {
        ArrayList<Integer> queue = DiskScheduler.readFromFile("data.txt");
        System.out.println("Enter Initial head start cylinder: ");
        Scanner input = new Scanner(System.in);
        int initialHead = input.nextInt();
        Scan_ScanLook ex = new Scan_ScanLook(initialHead, queue);
        ex.doMechanism();
        ex.displayScanResult();
        ex.displayLookResult();

        SSTF sstf = new SSTF(initialHead, queue);
        sstf.sequence();
        sstf.totalMovements();

        Circular circular = new Circular(initialHead, queue);
        circular.CSCAN();
        circular.CLOOK();

        FCFS fcfs = new FCFS(initialHead, queue);
        fcfs.sequence();
        fcfs.totalMovements();

        NewlyOptimizedAlgorithm NOP = new NewlyOptimizedAlgorithm(queue);
        NOP.sequence();
        NOP.TotalMovements();
        /*
        Ex1:
        data: 98, 183, 37, 122, 14, 124, 65, 67
        initialHead=53.

        Ex2:
        data:38,180,130,10,50,15,190,90,150
        intialHead=120
        */

    }
}
