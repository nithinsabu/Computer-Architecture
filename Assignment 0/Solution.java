import java.lang.Math;
//  class Clock {
//     int time;

//     public Clock(){
//         this.time=0;
//     }

//     public void increment() {
//         this.time+=10;
//     }
// }
class Sensor {
    public boolean isOn;
    private double probability;
    public Sensor(double probability){
        this.probability=probability;
        this.isOn=Math.random()<probability;
    }
    public void restart(){
        this.isOn=Math.random()<this.probability;
    }
}
 class Border {

    public int width;
    public int length;
    public Sensor[][] grid;
    public Border(int w, double probability){
        this.width = w;
        this.length = 1000;
        this.grid= new Sensor[this.width][this.length];
        for (int i=0;i<this.width; i++){
            for (int j=0;j<this.length;j++){
                this.grid[i][j]=new Sensor(probability);
            }
        }
    }
    public void update(){
        for (int i=0;i<this.width; i++){
            for (int j=0;j<this.length;j++){
                this.grid[i][j].restart();
            }
        }
    }
}
class Infiltrator{
    public int x,y;
    public boolean reachedDefcountry;
    public Infiltrator(){
        this.x=0;
        this.y=0;
        reachedDefcountry=false;
    }
    public void move(Sensor border[][]){
        if (this.y>=border.length-1){
            this.y++;
            this.reachedDefcountry=true;
            return;
        }
        if (!border[y+1][x].isOn){
            this.y++;
            return;
        }
        if (x-1>=0 && !border[y+1][x-1].isOn){
            this.x=x-1;
            this.y++;
            return;
        }
        if (x+1<border[0].length && !border[y+1][x+1].isOn){
            this.x=x+1;
            this.y++;
            return;
        
        }
    }
    
}

class Main{
    public static void main(String arg[]){
        final int max_time = 1000000;
        final int increment = 10; 
        double probability = 0.5;
        int width=4;
        int curr_time=0;

        Border B= new Border(width,probability);
        Infiltrator I = new Infiltrator();
        while (!I.reachedDefcountry && curr_time< max_time){
            I.move(B.grid);
            B.update();
            curr_time+=increment;
        }
        System.out.println(curr_time);

    }
}