package org.abstractica.clicksystem;

import org.abstractica.javacsg.Geometry3D;
import org.abstractica.javacsg.JavaCSG;

public interface ClickSystem
{
    JavaCSG getJavaCSG();
    double getUnit();

    Geometry3D getClicker(double length, double extraHeadLength);
    Geometry3D getDoubleClicker(double sideALength, double sideBLength, double extraMiddleHeadLength);
    Geometry3D getClickerLock();

    Geometry3D getTurnHole(double length, boolean leadInTop, boolean leadInBottom, int repeats, boolean fixOverhang);
    Geometry3D getFixedHole(double length, boolean leadInTop, boolean leadInBottom, int repeats,  boolean fixOverhang);
    Geometry3D getTurnHeadThroughHole(double length, boolean fixOverhang);
    Geometry3D getFixedHeadThroughHole(double length,  boolean fixOverhang);

    //Tools
    Geometry3D getInsertTool();
    Geometry3D getRemoveTool();
    Geometry3D getLockTool();
}
