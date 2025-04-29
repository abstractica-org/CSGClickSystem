package org.abstractica.clicksystem.test;

import org.abstractica.clicksystem.ClickSystem;
import org.abstractica.clicksystem.ClickSystemFactory;
import org.abstractica.javacsg.JavaCSG;
import org.abstractica.javacsg.JavaCSGFactory;

public class TestHoles
{
    public static void main(String[] args)
    {
        JavaCSG csg = JavaCSGFactory.createDefault();
        ClickSystem cs = ClickSystemFactory.system_12_8_6_hard(csg);
        csg.view(cs.getTurnHole(6, true, true, 3, true));
    }
}
