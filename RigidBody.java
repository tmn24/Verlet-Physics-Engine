import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.Color;
import java.util.Arrays;
public class RigidBody{//Rigid Bodies that are polygons
   Color c = new Color((int)(Math.random()*255),(int)(Math.random()*255),(int)(Math.random()*255));
   Vertex[] vertices;
   Vertex[] checkVertices;
   double[] sideLengths;
   double[] midLengths;
   double maxMidLength;
   int sides;
   double avgDist;//Used to estimate mass
   double mass;
   int extraV = 0;
   double x, y, prevX, prevY;//Position of Body (Center of Mass)
   RigidBody(double x,double y,Vertex[] vertices){
      //this.vertices = vertices;
      extraV = 2;//Intermediate Vertices
      //checkVertices = new Vertex[vertices.length*3];
      checkVertices = new Vertex[vertices.length*extraV];
      for(int i = 0; i < vertices.length; i++){
         double x1 = vertices[i].x;
         double y1 = vertices[i].y;
         double x2 = vertices[(i+1)%vertices.length].x;
         double y2 = vertices[(i+1)%vertices.length].y;
         for(int j = 0; j < extraV; j++){
            checkVertices[extraV*i+j] = new Vertex((x2-x1)/extraV*j+x1,(y2-y1)/extraV*j+y1);
         }
      }
      //Create intermediate vertices
      /*for(int i = 0; i < vertices.length; i++){
         double x1 = vertices[i].x;
         double y1 = vertices[i].y;
         double x2 = vertices[(i+1)%vertices.length].x;
         double y2 = vertices[(i+1)%vertices.length].y;
         checkVertices[3*i] = new Vertex(x1,y1);
         checkVertices[3*i+1] = new Vertex((x2-x1)/3+x1,(y2-y1)/3+y1);
         checkVertices[3*i+2] = new Vertex(-(x2-x1)/3+x2,-(y2-y1)/3+y2);
      }*/
      vertices = checkVertices;
      this.vertices = vertices;
      sides = vertices.length;
      //Determine side lengths of each side of the body
      sideLengths = new double[sides];
      for(int i = 0; i < sides; i++){
         double x1 = vertices[i].x;
         double y1 = vertices[i].y;
         double x2 = vertices[(i+1)%sides].x;
         double y2 = vertices[(i+1)%sides].y;
         sideLengths[i] = Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2));
      }
      this.x = x;
      this.y = y;
      this.prevX = x;
      this.prevY = y;
      //Calculate Center of Mass
      double cx = 0, cy = 0;
      for(int i = 0; i < sides; i++){
         cx += vertices[i].x;
         cy += vertices[i].x;
      }
      cx /= sides;
      cy /= sides;
      //Move vertices to correct Position
      double deltaX = (x-cx);
      double deltaY = (y-cy);
      for(int i = 0; i < sides; i++){
         vertices[i].x += deltaX;
         vertices[i].y += deltaY;
         vertices[i].prevX = vertices[i].x;
         vertices[i].prevY = vertices[i].y;
      }
      //Determine midLengths
      maxMidLength = 0;
      midLengths = new double[sides];
      for(int i = 0; i < sides; i++){
         double x1 = vertices[i].x;
         double y1 = vertices[i].y;
         double x2 = x;
         double y2 = y;
         midLengths[i] = Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2));
         if(maxMidLength < midLengths[i]){
            maxMidLength = midLengths[i];
         }
      }
      //Determine avgDist
      for(int i = 0; i < sides; i++){
         double x1 = vertices[i].x;
         double y1 = vertices[i].y;
         double x2 = x;
         double y2 = y;
         avgDist += Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2));
      } 
      avgDist /= sides;
      mass = Math.PI*avgDist*avgDist;
      //Give vertices their mass;
      for(int i = 0; i < sides; i++){
         vertices[i].mass = mass/sides;  
      }
   }
   public void timeStep(double dt){
      //Reset Points so that they are zat the correct distance
      int substeps = 20;
      for(int i = 0; i < substeps; i++){
         //Change so distance from mid point to point is correct
         for(int j = 0; j < sides; j++){
            double x1 = vertices[j].x;
            double y1 = vertices[j].y;
            double x2 = x;
            double y2 = y;
            //Determine current distance between points
            double dist = Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2));
            //MidPoint
            double mX = (x1+x2)/2;
            double mY = (y1+y2)/2;
            //Determine delta x and y
            double deltax1 = (x1-mX);
            double deltay1 = (y1-mY);
            double deltax2 = (x2-mX);
            double deltay2 = (y2-mY);
            //Change Point locations by scaling from midpoint
            double scale = midLengths[j]/dist;
            
            vertices[j].x = mX+deltax1*scale;//x1
            vertices[j].y = mY+deltay1*scale;//y1
            x = mX+deltax2*scale;//x2
            y = mY+deltay2*scale;//y2
         }
         //Change side lengths so they are correct
         for(int j = 0; j < sides; j++){
            double x1 = vertices[j%sides].x;
            double y1 = vertices[j%sides].y;
            double x2 = vertices[(j+1)%sides].x;
            double y2 = vertices[(j+1)%sides].y;
            //Determine current distance between points
            double dist = Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2));
            //MidPoint
            double mX = (x1+x2)/2;
            double mY = (y1+y2)/2;
            //Determine delta x and y
            double deltax1 = (x1-mX);
            double deltay1 = (y1-mY);
            double deltax2 = (x2-mX);
            double deltay2 = (y2-mY);
            //Change Point locations by scaling from midpoint
            double scale = sideLengths[j]/dist;
            
            vertices[j%sides].x = mX+deltax1*scale;//x1
            vertices[j%sides].y = mY+deltay1*scale;//y1
            vertices[(j+1)%sides].x = mX+deltax2*scale;//x2
            vertices[(j+1)%sides].y = mY+deltay2*scale;//y2
         }
      }
      //Move points according to verlet
      double g = 9.81*10;
      //vertices
      for(int i = 0; i < sides; i++){
         double subX = vertices[i].x;
         double subY = vertices[i].y;
         vertices[i].x = vertices[i].x+(vertices[i].x-vertices[i].prevX)-0*dt*dt/2;
         vertices[i].y = vertices[i].y+(vertices[i].y-vertices[i].prevY)-g*dt*dt/2;
         vertices[i].prevX = subX;
         vertices[i].prevY = subY;
      }
      //Center of mass
      double subX = x;
      double subY = y;
      x = x+(x-prevX)-0*dt*dt/2;
      y = y+(y-prevY)-g*dt*dt/2;
      prevX = subX;
      prevY = subY;
   }
   public void display(Graphics g,int cW, int cH){
      Polygon p = new Polygon();
      for(int i = 0; i < vertices.length; i+=extraV){
         p.addPoint((int)(cW/2+vertices[i].x),(int)(cH/2-vertices[i].y));
      }
      g.setColor(c);
      g.fillPolygon(p);
   }
}