import java.util.ArrayList;
public class Region{
   int X, Y, xRegions, yRegions;
   Circle[] circles;
   int javaXBound1;
   int javaYBound1;
   int javaXBound2;
   int javaYBound2;
   double xBound1;
   double yBound1;
   double xBound2;
   double yBound2;
   Region(int i, int j, int xRegions, int yRegions){
      X = i;
      Y = j;
      this.xRegions = xRegions;
      this.yRegions = yRegions;
   }  
   void resetRegion(Circle[] c, int cW, int cH){
      javaXBound1 = cW/xRegions*X;
      javaYBound1 = cH/yRegions*Y;
      javaXBound2 = cW/xRegions*(X+1);
      javaYBound2 = cH/yRegions*(Y+1);
      xBound1 = javaXBound1-cW/2;
      yBound1 = cH/2-javaYBound1;
      xBound2 = javaXBound2-cW/2;
      yBound2 = cH/2-javaYBound2;
      int noCircles = 0;
      //System.out.println(X+","+Y+","+xBound1+","+xBound2+","+yBound1+","+yBound2);
      ArrayList<Integer> circleIndices = new ArrayList<Integer>(10);
      for(int i = 0; i < c.length; i++){
         //System.out.println(c[i].x+","+c[i].y);
         if(c[i].x > xBound1 && c[i].x < xBound2 && c[i].y < yBound1 && c[i].y > yBound2){
            noCircles++;  
            circleIndices.add(i);
            
         }
      }
      circles = new Circle[noCircles];
      for(int i = 0; i < noCircles; i++){
         circles[i] = c[circleIndices.get(i)];
      }
      //System.out.println(noCircles);
   }
   void reallocateCircles(Circle[] c, int cW, int cH){
      int noCircles = 0;
      //System.out.println(X+","+Y+","+xBound1);
      ArrayList<Integer> circleIndices = new ArrayList<Integer>(10);
      for(int i = 0; i < c.length; i++){
         if(c[i].x > xBound1 && c[i].x < xBound2 && c[i].y < yBound1 && c[i].y > yBound2){
            noCircles++;  
            circleIndices.add(i);
         }
      }
      circles = new Circle[noCircles];
      for(int i = 0; i < noCircles; i++){
         circles[i] = c[circleIndices.get(i)];
      }
      //System.out.println(noCircles);
   }
}