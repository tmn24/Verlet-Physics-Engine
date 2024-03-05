public class Vertex{//Stores x and y Positions
   double x, y, prevY, prevX, mass;
   public Vertex(double x, double y){
      this.x = x;
      this.y = y;
      prevX = x;
      prevY = y;
   }
   public String toString(){
      return "("+x+","+y+")";
   }
}