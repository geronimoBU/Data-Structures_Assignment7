
package hw7;

/**
 *
 * @author alexgeronimo
 */
public class Process {
    
    private int p_id, priority, duration, arrival_time;
    private int wait_time;
    public Process(int p_ID, int priority, int duration, int arrival_time){
        this.p_id = p_ID;
        this.priority = priority;
        this.duration = duration;
        this.arrival_time = arrival_time;
        this.wait_time = 0;
    }
    
    // Getter methods
    public int getID(){return p_id;}
    public int getPriority(){return priority;}
    public int getDuration(){return duration;}
    public int getArrivalTime(){return arrival_time;}
    public int getWaitTime(){return wait_time;}
    
    //Setter methods
    public void minusPriority(){this.priority--;}
    public void plusWaitTime(){wait_time++;}
    public void setArrivalTime(int t){this.arrival_time = t;}
    
    //Get info method
    public void getInfo(){
        System.out.println("ID = " + p_id + ", prority = " + priority + ", duration = " + duration + ", arrival time = " + arrival_time);
    }
    
}
