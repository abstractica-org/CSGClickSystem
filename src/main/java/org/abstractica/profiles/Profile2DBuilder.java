package org.abstractica.profiles;

import org.abstractica.javacsg.Geometry3D;

public interface Profile2DBuilder
{
    void add(double x, double y);
    default void move(double dX, double dY)
    {
        add(curX() + dX, curY() + dY);
    }
    double curX();
    double curY();
    void cutout(Geometry3D cutout);
    void addOn(Geometry3D addOn);
    Profile2D getProfile();
}
