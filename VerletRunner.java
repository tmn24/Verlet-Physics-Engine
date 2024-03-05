import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Graphics;
import java.time.Instant;
import java.time.Duration;
import java.awt.Color;
public class VerletRunner extends JPanel{
   JFrame frame;
   //Create N circles
   Circle[] circle = new Circle[0];
   RigidBody[] bodies = new RigidBody[6];
   int xRegions = 10;
   int yRegions = 10;
   Region[][] regions = new Region[xRegions][yRegions];
   VerletRunner(){
      //Create Circle
      for(int i = 0; i < circle.length; i++){
         double r = Math.random()*1+5;
         circle[i] = new Circle((Math.random())*200-r,(Math.random()-0.5)*400-r,r);
      }
      //Create RigidBodies
      for(int i = 0; i < bodies.length; i++){
         double L = 50;
         int noVertices = (int)(3+Math.random()*5);
         Vertex[] vertices = new Vertex[noVertices];
         double a = 0;
         for(int j = 0; j < noVertices; j++){
            vertices[j] = new Vertex(L*(Math.random()+0.5)*Math.cos(a),L*(Math.random()+1)*Math.sin(a));
            a += 2*Math.PI/noVertices;
         }
         bodies[i] = new RigidBody(400*(Math.random()-0.5),400*(Math.random()-0.5),vertices);
      }
      //Create Regions
      for(int i = 0; i < xRegions; i++){
         for(int j = 0; j < yRegions; j++){
            regions[i][j] = new Region(i,j,xRegions,yRegions);
         }
      }  
      
      //System.out.println(bodies[0].sides);
      //System.out.println(bodies[1].sides);
      //Create JFrame
      frame = new JFrame("Verlet Physics");
      frame.add(this);
      frame.setPreferredSize(new Dimension(1000,800));
      frame.pack();
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setVisible(true);
   }
   public void paint(Graphics g){
      super.paint(g);
      //Start-end timer to regulate framerate
      Instant start = Instant.now();
      //Get important Values
      int cW = frame.getWidth();
      int cH = frame.getHeight();
      int radius = cH/2-40;
      //Custom Code
      //Custom Code end
      //ResetRegions
      for(int i = 0; i < xRegions; i++){
         for(int j = 0; j < yRegions; j++){
            regions[i][j].resetRegion(circle,cW,cH);
         }
      }  
      
      //Substep loop
      int substeps = 8;
      double dt = 0.1/substeps;
      for(int i = 0; i < substeps; i++){ 
         circleCollision2(g);
         rigidBodyCollision(g);
         
         //Circle Collision with Rectangle Wall
         //circleRectangleWallCollision(g,cW,cH);
         
         //Circle Collision with Round Wall
         circleRoundWallCollision(g,cW,cH);
         
         //Rigid Body vs Circle Collision
         rigidBodyCircleCollision(g);
         
         //Rigid Body Wall Collision
         for(int j = 0; j < bodies.length; j++){
            for(int k = 0; k < bodies[j].sides; k++){
               double x1 = bodies[j].vertices[k].x;
               double y1 = bodies[j].vertices[k].y;
               if(x1*x1+y1*y1 > radius*radius){
                  double dist = Math.sqrt(x1*x1+y1*y1);
                  double scale = radius/dist;
                  bodies[j].vertices[k].x *= scale; 
                  bodies[j].vertices[k].y *= scale;   
               }
            }
         }
         //TimeStep
         for(int j = 0; j < circle.length; j++){
            circle[j].timeStep(dt);
         }
         for(int j = 0; j < bodies.length; j++){
            bodies[j].timeStep(dt);
         }  
      }
      
      //Display Circles
      for(int i = 0; i < circle.length; i++){
         circle[i].display(g,cW,cH);
      }
      //Display Rigid Bodies
      for(int i = 0; i < bodies.length; i++){
         bodies[i].display(g,cW,cH);
      }
      Instant end = Instant.now();
      //Wait until 1/60seconds have passed in total. 60Fps
      dt = (double)Duration.between(start,end).toMillis();
      int framerate = 200;
      if(dt < (double)1000/framerate){
         try{
            Thread.sleep((long)((double)1000/framerate-dt));
         } catch(Exception e){}
      }
      repaint();
   }
   public static void main(String[] args){
      VerletRunner vr = new VerletRunner();
   }
   public void rigidBodyCircleCollision(Graphics g){
      for(int i = 0; i < bodies.length; i++){
         for(int j = 0; j < bodies[i].sides; j++){
            for(int k = 0; k < circle.length; k++){
               
            }
         }
      }
   }
   public void circleRectangleWallCollision(Graphics g, int cW, int cH){
      cW -= 40;
      cH -= 100;
      g.drawRect(20,50,cW,cH);
      double xBound1 = 0-cW/2;
      double yBound1 = cH/2-cH;
      double xBound2 = cW-cW/2;
      double yBound2 = cH/2-0;
      for(int i = 0; i < circle.length; i++){
         //Necessary Coords
         double x = circle[i].x;
         double y = circle[i].y;
         double r = circle[i].radius;
         //Check Left Side
         if(x-r < xBound1){
            circle[i].x = xBound1+r;
         }
         //Check Right Side
         if(x+r > xBound2){
            circle[i].x = xBound2-r;
         }
         //Check Top
         if(y+r > yBound2){
            circle[i].y = yBound2-r;
         }
         //Check Bottom
         if(y-r < yBound1){
            circle[i].y = yBound1+r;
         }
      }
   }
   public void circleRoundWallCollision(Graphics g,int cW, int cH){
      int radius = cH/2-40;
      g.drawOval(cW/2-radius,cH/2-radius,2*radius,2*radius);
      //Circle Wall Collision
         for(int j = 0; j < circle.length; j++){
            if(circle[j].x*circle[j].x+circle[j].y*circle[j].y > (radius-circle[j].radius)*(radius-circle[j].radius)){
               double dist = Math.sqrt(circle[j].x*circle[j].x+circle[j].y*circle[j].y);
               double scale = (radius-circle[j].radius)/dist;
               circle[j].x *= scale; 
               circle[j].y *= scale;   
            }
         }
   }
   public void circleCollision2(Graphics g){
      //Circle vs Circle Collision
      Circle[] c; 
      for(int i1 = 0; i1 < regions.length-1; i1++){
         for(int i2 = 0; i2 < regions[0].length-1; i2++){
            int L1=regions[i1][i2].circles.length;
            int L2=regions[i1+1][i2].circles.length;
            int L3=regions[i1][i2+1].circles.length;
            int L4=regions[i1+1][i2+1].circles.length;
         
            c = new Circle[L1+L2+L3+L4];
            //System.out.println(c.length);
         //Create c
            int index = 0;
            for(int i = 0; i < L1; i++){
               c[index++] = regions[i1][i2].circles[i];
            }
            for(int i = 0; i < L2; i++){
               c[index++] = regions[i1+1][i2].circles[i];
            }
            for(int i = 0; i < L3; i++){
               c[index++] = regions[i1][i2+1].circles[i];
            }
            for(int i = 0; i < L4; i++){
               c[index++] = regions[i1+1][i2+1].circles[i];
            }
            for(int j = 0; j < c.length; j++){
               for(int k = j+1; k < c.length; k++){
                  double x1 = c[j].x, y1 = c[j].y, r1 = c[j].radius, m1 = c[j].mass;
                  double x2 = c[k].x, y2 = c[k].y, r2 = c[k].radius, m2 = c[k].mass;
                  if((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2) < (r1+r2)*(r1+r2)){
                  //Midpoint of center of circle
                     double midX = (x1*m1+x2*m2)/(m1+m2), midY = (y1*m1+y2*m2)/(m1+m2);
                     double dist = Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2));
                  //Change Circle 1 Position
                     c[j].x = midX+(x1-midX)/(dist/2)*(r1+r2)/2;
                     c[j].y = midY+(y1-midY)/(dist/2)*(r1+r2)/2;
                  //Change Circle 2 Position
                     c[k].x = midX+(x2-midX)/(dist/2)*(r1+r2)/2;
                     c[k].y = midY+(y2-midY)/(dist/2)*(r1+r2)/2;
                  }
               }
            }
         }
      }
   }
   public void circleCollision(Graphics g){
      //Circle vs Circle Collision
      for(int j = 0; j < circle.length; j++){
         for(int k = j+1; k < circle.length; k++){
            double x1 = circle[j].x, y1 = circle[j].y, r1 = circle[j].radius, m1 = circle[j].mass;
            double x2 = circle[k].x, y2 = circle[k].y, r2 = circle[k].radius, m2 = circle[k].mass;
            if((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2) < (r1+r2)*(r1+r2)){
                  //Midpoint of center of circle
               double midX = (x1*m1+x2*m2)/(m1+m2), midY = (y1*m1+y2*m2)/(m1+m2);
               double dist = Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2));
                  //Change Circle 1 Position
               circle[j].x = midX+(x1-midX)/(dist/2)*(r1+r2)/2;
               circle[j].y = midY+(y1-midY)/(dist/2)*(r1+r2)/2;
                  //Change Circle 2 Position
               circle[k].x = midX+(x2-midX)/(dist/2)*(r1+r2)/2;
               circle[k].y = midY+(y2-midY)/(dist/2)*(r1+r2)/2;
            }
         }
      }
   }

   public void rigidBodyCollision(Graphics g){
      //Rigid Body vs Rigid Body Collision: Vertex in Body
      for(int j = 0; j < bodies.length; j++){//For each body
         for(int k = 0; k < bodies.length; k++){//starting k at 0 and not j+1 because this collision calculation is orientation dependent
            double X1 = bodies[j].x, Y1 = bodies[j].y, R1 = bodies[j].maxMidLength;
            double X2 = bodies[k].x, Y2 = bodies[k].y, R2 = bodies[k].maxMidLength;
            if((X1-X2)*(X1-X2)+(Y1-Y2)*(Y1-Y2) < (R1+R2)*(R1+R2)){
               if(j != k){
                  boolean vertexCollision = false;
                  boolean lineCollision = false;
                  int vertexIndex = 0;
                  for(int l = 0; l < bodies[j].sides; l++){//Each Vertex on body 1  
                     int intersections = 0;
                     for(int m = 0; m < bodies[k].sides; m++){//Raycast onto each side of body 2
                        //Raycast Magic
                        double X = bodies[j].vertices[l].x;
                        double Y = bodies[j].vertices[l].y;
                        double x1 = bodies[k].vertices[m].x;
                        double y1 = bodies[k].vertices[m].y;
                        double x2 = bodies[k].vertices[(m+1)%bodies[k].sides].x;
                        double y2 = bodies[k].vertices[(m+1)%bodies[k].sides].y;
                        double t1 = (Y-y1)/(y2-y1);
                        double t1x = x1+t1*(x2-x1);
                        double t1y = y1+t1*(y2-y1);
                        //Raycast Magic end
                        if(t1x >= X && 0 <= t1 && t1 <= 1){//Counting how many intersections. If odd, collision is true
                           intersections++;
                        }
                     }
                     if(intersections % 2 == 1){
                        vertexCollision = true;
                        vertexIndex = l;
                        l = bodies[j].sides;
                     }
                  }
                  if(vertexCollision){
                     int sideIndex = 0;
                     double minDist = 100000;
                     double X = bodies[j].vertices[vertexIndex].x;
                     double Y = bodies[j].vertices[vertexIndex].y;
                     //Check for closest side
                     for(int m = 0; m < bodies[k].sides; m++){
                        double x1 = bodies[k].vertices[m].x;
                        double y1 = bodies[k].vertices[m].y;
                        double x2 = bodies[k].vertices[(m+1)%bodies[k].sides].x;
                        double y2 = bodies[k].vertices[(m+1)%bodies[k].sides].y;
                        double dx1 = X-x1;
                        double dy1 = Y-y1;
                        double dx2 = x2-x1;
                        double dy2 = y2-y1;
                        double t1 = (dx1*dx2+dy1*dy2)/(dx2*dx2+dy2*dy2);//Vector Projection
                        double t1x = x1+t1*(x2-x1);
                        double t1y = y1+t1*(y2-y1);
                        double dist = Math.sqrt((X-t1x)*(X-t1x)+(Y-t1y)*(Y-t1y));
                        if(minDist > dist){
                           minDist = dist;
                           sideIndex = m;
                        }
                     }
                     int l = vertexIndex;
                     int m = sideIndex;
                        
                        //Coords
                     double x1 = bodies[k].vertices[m].x;
                     double y1 = bodies[k].vertices[m].y;
                     double x2 = bodies[k].vertices[(m+1)%bodies[k].sides].x;
                     double y2 = bodies[k].vertices[(m+1)%bodies[k].sides].y;
                        //Find closes Point
                     double dx1 = X-x1;
                     double dy1 = Y-y1;
                     double dx2 = x2-x1;
                     double dy2 = y2-y1;
                     double t1 = (dx1*dx2+dy1*dy2)/(dx2*dx2+dy2*dy2);//Vector Projection
                       
                     double t1x = x1+t1*(x2-x1);
                     double t1y = y1+t1*(y2-y1);   
                        //Masses
                     double M = bodies[j].vertices[l].mass;
                     double m1 = (1-t1)*bodies[k].vertices[m].mass;
                     double m2 = t1*bodies[k].vertices[(m+1)%bodies[k].sides].mass;
                        
                        //Midpoint
                     double midX = (t1x*(m1+m2)+X*M)/(m1+m2+M);
                     double midY = (t1y*(m1+m2)+Y*M)/(m1+m2+M);
                     //Delta from collision point to midPoint
                     double deltax = midX-t1x;
                     double deltay = midY-t1y;
                     //Change points
                     bodies[j].vertices[l].x = midX;
                     bodies[j].vertices[l].y = midY;
                     bodies[k].vertices[m].x = x1+deltax*(1-t1);
                     bodies[k].vertices[m].y = y1+deltay*(1-t1);
                     bodies[k].vertices[(m+1)%bodies[k].sides].x = x2+deltax*t1;
                     bodies[k].vertices[(m+1)%bodies[k].sides].y = y2+deltay*t1; 
                  }
               }
            }
         }
      }
         /*for(int j = 0; j < bodies.length; j++){
            for(int k = j+1; k < bodies.length; k++){
               for(int l = 0; l < bodies[j].sides; l++){//Each Vertex on body 1  
                  int intersections = 0;
                  for(int m = 0; m < bodies[k].sides; m++){//Raycast onto each side of body 2
                        //Raycast Magic
                           //Line 1
                     double x11 = bodies[j].vertices[l].x;
                     double y11 = bodies[j].vertices[l].y;
                     double m11 = bodies[j].vertices[l].mass;
                     double x12 = bodies[j].vertices[(l+1)%bodies[j].sides].x;
                     double y12 = bodies[j].vertices[(l+1)%bodies[j].sides].y;
                     double m12 = bodies[j].vertices[(l+1)%bodies[j].sides].mass;
                           //Line 2
                     double x21 = bodies[k].vertices[m].x;
                     double y21 = bodies[k].vertices[m].y;
                     double m21 = bodies[k].vertices[m].mass;
                     double x22 = bodies[k].vertices[(m+1)%bodies[k].sides].x;
                     double y22 = bodies[k].vertices[(m+1)%bodies[k].sides].y;
                     double m22 = bodies[k].vertices[(m+1)%bodies[k].sides].mass;
                     
                     double t2 = ((y12-y11)*(x21-x11)-(x12-x11)*(y21-y11))/((y22-y21)*(x12-x11)-(y12-y11)*(x22-x21));
                     double t1 = 0;
                     if(x12-x11 != 0){
                        t1 = ((x22-x21)*t2+x21-x11)/(x12-x11);
                     } else {
                        t1 = ((y22-y21)*t2+y21-y11)/(y12-y11);
                     }
                     double t1x = x11+t1*(x12-x11);
                     double t1y = y11+t1*(y12-y11);
                        //Raycast Magic end
                     if(t2 >= 0 && t2 <= 1&& 0 <= t1 && t1 <= 1){//Check for Intersection
                        double dx1 = 0;
                        double dy1 = 0;
                        double dx2 = 0;
                        double dy2 = 0;
                        if(t1 < 0.5){
                           dx2 = x11-t1x;
                           dy2 = y11-t1y;
                        } else {
                           dx2 = x12-t1x;
                           dy2 = y12-t1y;
                        }
                        if(t2 < 0.5){
                           dx1 = x21-t1x;
                           dy1 = y21-t1y;
                        } else {
                           dx1 = x22-t1x;
                           dy1 = y22-t1y;
                        }
                        bodies[j].vertices[l].x+=dx1;
                        bodies[j].vertices[l].y+=dy1;
                        bodies[j].vertices[(l+1)%bodies[j].sides].x+=dx1;
                        bodies[j].vertices[(l+1)%bodies[j].sides].y+=dy1;
                        
                        bodies[k].vertices[m].x+=dx2;
                        bodies[k].vertices[m].y+=dy2;
                        bodies[k].vertices[(m+1)%bodies[k].sides].x+=dx2;
                        bodies[k].vertices[(m+1)%bodies[k].sides].y+=dy2;
                     }
                  }
               }
            }
         }*/
         
   }
}