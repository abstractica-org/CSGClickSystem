package org.abstractica.clicksystem.test;

import org.abstractica.clicksystem.ClickSystem;
import org.abstractica.clicksystem.ClickSystemFactory;
import org.abstractica.javacsg.Geometry3D;
import org.abstractica.javacsg.JavaCSG;
import org.abstractica.javacsg.JavaCSGFactory;

import java.util.ArrayList;
import java.util.List;

public class TestClickerSystem
{
    private final JavaCSG csg;
    private final ClickSystem cs;

    public TestClickerSystem(JavaCSG csg)
    {
        this.csg = csg;
        this.cs = ClickSystemFactory.system_12_8_6_hard(csg);
    }

    public Geometry3D getDoubleClicker(int lengthA, int lengthB)
    {
        return cs.getDoubleClicker(lengthA, lengthB,0);
    }

    public Geometry3D getClicker(int length)
    {
        return cs.getClicker(length, 0);
    }

    public Geometry3D getClickerSelection()
    {
        List<Geometry3D> clickers = new ArrayList<>();
        clickers.add(getDoubleClicker(6, 6));
        clickers.add(getDoubleClicker(6, 12));
        clickers.add(getClicker(6));
        clickers.add(getClicker(12));
        clickers = arrangeLine(clickers, 10);
        return csg.union3D(clickers);
    }

    public Geometry3D getClickerHoles()
    {
        Geometry3D box1 = csg.box3D(40, 20, 12, false);
        box1 = csg.translate3DX(-20).transform(box1);
        Geometry3D box2 = csg.box3D(40, 20, 6, false);
        box2 = csg.translate3DX(20).transform(box2);
        Geometry3D res = csg.union3D(box1, box2);
        Geometry3D c1 = cs.getFixedHole(12,true, true, 1, false);
        Geometry3D c2 = cs.getTurnHole(12,true, true, 1, false);
        Geometry3D c3 = cs.getFixedHole(6,true, true, 1, false);
        Geometry3D c4 = cs.getTurnHole(6,true, true, 1, false);
        c1 = csg.translate3DX(-30).transform(c1);
        c2 = csg.translate3DX(-10).transform(c2);
        c3 = csg.translate3DX(10).transform(c3);
        c4 = csg.translate3DX(30).transform(c4);
        res = csg.difference3D(res, c1, c2, c3, c4);
        return res;
    }

    public Geometry3D getRemoveTool()
    {
        return cs.getRemoveTool();
    }

    public Geometry3D getInsertTool()
    {
        return cs.getInsertTool();
    }

    private List<Geometry3D> arrangeLine(List<Geometry3D> geos, double spacing)
    {
        List<Geometry3D> res = new ArrayList<>(geos.size());
        double curX = (geos.size()-1)*-0.5*spacing;
        for(Geometry3D geo : geos)
        {
            res.add(csg.translate3DX(curX).transform(geo));
            curX += spacing;
        }
        return res;
    }

    public static void main(String[] args)
    {
        JavaCSG csg = JavaCSGFactory.createDefault();
        TestClickerSystem tcs = new TestClickerSystem(csg);
        Geometry3D res = tcs.getClickerSelection();
        //Geometry3D res = tcs.getClicker(6);
        //csg.view(tcs.getRemoveTool());
        //csg.view(tcs.getInsertTool());
        csg.view(tcs.getClickerHoles());
        //csg.view(res);
    }
}
