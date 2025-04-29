package org.abstractica.profiles;

import org.abstractica.javacsg.Geometry2D;
import org.abstractica.javacsg.Geometry3D;

import java.util.List;

public interface Profile2D
{
    Geometry2D getGeometry2D();
    List<Geometry3D> getCutouts();
    List<Geometry3D> getAddOns();
}
