package tankgame;

import java.awt.*;

import static java.lang.StrictMath.abs;

public class Circle {
    double x;
    double y;
    double radius;

    public Circle(double x,double y,double radius){
        this.x = x;
        this.y = y;
        this.radius = radius;
    }



    boolean intersects( Rectangle rect)
    {
        double circleDistanceX =  abs(this.x - rect.x);
        double circleDistanceY =  abs(this.y - rect.y);

        if (circleDistanceX > (rect.width/2. + this.radius)) { return false; }
        if (circleDistanceY > (rect.height/2. + this.radius)) { return false; }

        if (circleDistanceX <= (rect.width/2.)) { return true; }
        if (circleDistanceY <= (rect.height/2.)) { return true; }

        double cornerDistance_sq = (circleDistanceX - rect.width / 2.)*(circleDistanceX - rect.width / 2.) +
                (circleDistanceY - rect.height / 2.) *(circleDistanceY - rect.height / 2.);

        return (cornerDistance_sq <= (this.radius* this.radius));
    }
}
