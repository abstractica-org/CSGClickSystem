package org.abstractica.clicksystem.examples;

import org.abstractica.clicksystem.ClickSystem;
import org.abstractica.clicksystem.ClickSystemFactory;
import org.abstractica.javacsg.JavaCSG;
import org.abstractica.javacsg.JavaCSGFactory;

public class ToolExample
{
    public static void main(String[] args)
    {
        JavaCSG csg = JavaCSGFactory.createDefault();
        ClickSystem cs = ClickSystemFactory.system_12_8_6_medium(csg);
        csg.view(cs.getRemoveTool());
    }
}
