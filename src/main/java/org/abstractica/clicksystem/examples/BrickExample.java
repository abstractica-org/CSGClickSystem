package org.abstractica.clicksystem.examples;

import org.abstractica.clicksystem.ClickSystem;
import org.abstractica.clicksystem.ClickSystemFactory;
import org.abstractica.javacsg.Geometry3D;
import org.abstractica.javacsg.JavaCSG;
import org.abstractica.javacsg.JavaCSGFactory;

import java.util.ArrayList;
import java.util.List;

public class BrickExample
{
    public static void main(String[] args)
    {
        JavaCSG csg = JavaCSGFactory.createDefault();
        ClickSystem cs = ClickSystemFactory.system_12_8_6_medium(csg);
        double unit = cs.getUnit();
        int xSize = 3;
        int ySize = 2;
        int zSize = 1;
        Geometry3D brick = csg.box3D(unit*xSize, unit*ySize, unit*zSize, false);
        brick = csg.translate3D(0.5*unit*xSize, 0.5*unit*ySize, 0).transform(brick);
        List<Geometry3D> holes = new ArrayList<>();
        Geometry3D hole = cs.getTurnHole(0.5*unit*zSize, true, true, 2, false);
        for(int y = 0; y < ySize; ++y)
        {
            for(int x = 0; x < xSize; ++x)
            {
                Geometry3D holeCopy = csg.translate3D((x+0.5)*unit, (y+0.5)*unit, 0).transform(hole);
                holes.add(holeCopy);
            }
        }
        brick = csg.difference3D(brick, holes);
        csg.view(brick);

    }
}
