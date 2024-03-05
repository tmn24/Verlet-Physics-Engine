import java.awt.Graphics;
import java.awt.Color;
public class Circle{
   Color c = new Color((int)(Math.random()*255),(int)(Math.random()*255),(int)(Math.random()*255));
   double x, y, radius, mass;
   double prevX, prevY;
   public Circle(double x, double y, double radius){
      this.x = x;
      this.y = y;
      this.radius = radius;
      prevX = x;
      prevY = y;
      mass = Math.PI*radius*radius;
   }
   public void timeStep(double dt){
      double g = 9.81*2;
      double subX = x;
      double subY = y;
      x = x+(x-prevX)-0*dt*dt/2;
      y = y+(y-prevY)-g*dt*dt/2;
      prevX = subX;
      prevY = subY;
   }
   public void display(Graphics g,int cW,int cH){
      int javaX = (int)(cW/2+x);
      int javaY = (int)(cH/2-y);
      g.setColor(c);
      g.fillOval(javaX-(int)radius,javaY-(int)radius,2*(int)radius,2*(int)radius);
   }
}