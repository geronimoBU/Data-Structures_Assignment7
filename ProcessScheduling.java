/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hw7;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.*;
/**
 *
 * @author alexgeronimo
 */
public class ProcessScheduling {
    
    public static void main(String[] args) throws FileNotFoundException{
        
        // Set up output strem. (To .txt file) 
        PrintStream console = new PrintStream(new FileOutputStream("process_scheduling_out.txt"));
        System.setOut(console);
        
        List<Process> processArray = new ArrayList<>();
        
        //Read each line of .txt to Process objects. Store all objects in array
        try (Scanner input = checkFile("process_scheduling_input_1.txt")){
            
            while(input.hasNextLine()) {
                String line = input.nextLine();
                String[] items = new String[4];
                items = line.split("\\s");
                
                int pID = Integer.parseInt(items[0]);
                int priority = Integer.parseInt(items[1]);
                int duration = Integer.parseInt(items[2]);
                int arrival_time = Integer.parseInt(items[3]);
                
                Process p = new Process(pID, priority, duration, arrival_time);
                processArray.add(p);
                
            }
        }
        //Main program parameters
        int currentTime = 0;
        boolean running = false; 
        HeapAdaptablePriorityQueue<Integer, Process> Q = new HeapAdaptablePriorityQueue<>();
        Process p = processArray.iterator().next();
        int maxWait = 10;
        int total_wait_time = 0;
        List<Integer> wtArray = new ArrayList<>();
        Process running_p = null;
        ArrayList<Entry<Integer,Process>> currentQ = new ArrayList<>();
        
        //Main while loop until Process object array is empty
        while(!processArray.isEmpty()){
            
            if (p.getArrivalTime() <= currentTime){
                Q.insert(p.getPriority(), p);
                processArray.remove(p);
                if(!processArray.isEmpty()){
                    p = processArray.get(0);
                }  
            }

            if (!Q.isEmpty() && running == false){
                running_p = Q.min().getValue();
                Q.removeMin();
                running = true;
                printRemoved(running_p, currentTime, running_p.getWaitTime());
                wtArray.add(running_p.getWaitTime());
            }

            currentTime++;
            currentQ = Q.heap;
            
            for(int i=0; i<currentQ.size(); i++){
                currentQ.get(i).getValue().plusWaitTime();
                total_wait_time++;
            }
            
            if (running_p != null && running_p.getArrivalTime() + running_p.getDuration() + running_p.getWaitTime() == currentTime){
                console.print("Process " + running_p.getID() + " finished at time " + currentTime+"\n");
                running = false;
       
                Entry<Integer, Process> e;
                for(int i=0; i<currentQ.size(); i++){
                    e = currentQ.get(i);
                    if (e.getValue().getWaitTime() >= maxWait){
                        if(e.getValue().getPriority() > 1){
                            Q.replaceKey(e, e.getKey()-1);
                            e.getValue().minusPriority();
                            console.print("Priority  of process " + e.getValue().getID() + " decremented"+"\n");
                        }  
                    }
                }
                console.print("");
            }        
        }
        
        // 2nd while loop until priority queue is empty
        while (!Q.isEmpty()){
            if (running == false){
                running_p = Q.min().getValue();
                Q.removeMin();
                running = true;
                printRemoved(running_p, currentTime, running_p.getWaitTime());
                wtArray.add(running_p.getWaitTime());
            }
            
            currentTime++;
            currentQ = Q.heap;
            
            for(int i=0; i<currentQ.size(); i++){
                currentQ.get(i).getValue().plusWaitTime();
                total_wait_time++;
            }
            
            if (running_p != null && running_p.getArrivalTime() + running_p.getDuration() + running_p.getWaitTime() == currentTime){
                console.print("Process " + running_p.getID() + " finished at time " + currentTime+"\n");
                running = false;
                Entry<Integer, Process> e;
                for(int i=0; i<currentQ.size(); i++){
                    e = currentQ.get(i);
                    if (e.getValue().getWaitTime() >= maxWait){
                        if(e.getValue().getPriority() > 1){
                            Q.replaceKey(e, e.getKey()-1);
                            e.getValue().minusPriority();   
                            console.print("Priority  of process " + e.getValue().getID() + " decremented"+"\n");
                        }  
                    }
                }
                console.print("");
            }
        }
        
        //write info
        console.print("");
        console.print("Process " + running_p.getID() + " finished at time " + currentTime+"\n");
        console.print("Total wait time " + total_wait_time+"\n");
        console.print("Average wait time = " + intArray_mean(wtArray)+"\n");
    }
   //********************* END MAIN *****************************************************************
    
    /**
     * Check if file exists 
     * @param String to file
     * @return Scanner object 
     */
    public static Scanner checkFile(String file){
        Scanner input;
        try {
            input = new Scanner(new File(file));
            return input;
        } 
        catch (FileNotFoundException ex){
            ex.printStackTrace();
            return null;
        }
        
    }
    
    /**
     * Reads first line of .txt - returns integer value 
     * @param in_file
     * @return int first row
     */
    public static int getLines(Scanner in_file){
        int num_lines = in_file.nextInt();
        return num_lines;
    }
    
    /**
     * Print info from removed process
     * @param p - Process object
     * @param ct - current time integer
     * @param wt - wait time integer
     */
    public static void printRemoved(Process p, int ct, int wt){
        System.out.println("Process removed from queue is: id = " + p.getID() + ", at time " + ct
        + ", wait time = " + wt);
        System.out.println("    Process ID = " + p.getID());
        System.out.println("    Priority = " + p.getPriority());
        System.out.println("    Arrival = " + p.getArrivalTime());
        System.out.println("    Duration = " + p.getDuration());
    }
    
    /**
     * Calculate average of integer array
     * @param a List of integers
     * @return average as double
     */
    public static double intArray_mean(List<Integer> a){
        int sum = 0;
        for(int i=0; i<a.size(); i++){
            sum += a.get(i);
        }
        double ave = sum / a.size();
        return ave;
    }
    
}
