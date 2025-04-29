package org.abstractica.clicksystem;

import org.abstractica.javacsg.Geometry2D;
import org.abstractica.javacsg.Geometry3D;
import org.abstractica.javacsg.JavaCSG;
import org.abstractica.javacsg.JavaCSGFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PrinterCalibration
{
    private final JavaCSG csg;

    public PrinterCalibration(JavaCSG csg)
    {
        this.csg = csg;
    }

    public Geometry3D getRoundVerticalHoleCalibration(int count, double diameter, double startAdjust, double deltaAdjust)
    {
        double size = Math.max(diameter*2, 24);
        Geometry3D box = csg.box3D(count*size, size+10, 6, false);
        box = csg.translate3D(0.5*count*size, 0.5*(size+10), 0).transform(box);
        double curAdjust = startAdjust;
        List<Geometry3D> positives = new ArrayList<>();
        List<Geometry3D> negatives = new ArrayList<>();
        positives.add(box);
        for(int i = 0; i < count; i++)
        {
            Geometry3D hole = getHole(diameter, curAdjust, 6);
            hole = csg.translate3D(i*size + 0.5*size, 0.5*size, 0).transform(hole);
            negatives.add(hole);
            String s = String.format(Locale.ENGLISH, "%,.2f", curAdjust);
            Geometry3D text = getText(s);
            text = csg.translate3D(i*size+0.5*size, size, 6).transform(text);
            positives.add(text);
            curAdjust += deltaAdjust;
        }
        Geometry3D res = csg.union3D(positives);
        res = csg.difference3D(res, negatives);
        return res;
    }

    public Geometry3D getFixedVerticalHoleCalibration(int count, double diameter, double diameterAdjust, double width, double startAdjust, double deltaAdjust)
    {
        double size = Math.max(diameter*2, 24);
        Geometry3D box = csg.box3D(count*size, size+10, 6, false);
        box = csg.translate3D(0.5*count*size, 0.5*(size+10), 0).transform(box);
        double curAdjust = startAdjust;
        List<Geometry3D> positives = new ArrayList<>();
        List<Geometry3D> negatives = new ArrayList<>();
        positives.add(box);
        for(int i = 0; i < count; i++)
        {
            Geometry3D hole = getFixedHole(diameter, diameterAdjust, width, curAdjust, 6);
            hole = csg.translate3D(i*size + 0.5*size, 0.5*size, 0).transform(hole);
            negatives.add(hole);
            String s = String.format(Locale.ENGLISH, "%,.2f", curAdjust);
            Geometry3D text = getText(s);
            text = csg.translate3D(i*size+0.5*size, size, 6).transform(text);
            positives.add(text);
            curAdjust += deltaAdjust;
        }
        Geometry3D res = csg.union3D(positives);
        res = csg.difference3D(res, negatives);
        return res;
    }

    public Geometry3D getRoundHorizontalHoleCalibration(int count, double diameter, double startAdjust, double deltaAdjust)
    {
        double size = Math.max(diameter*2, 24);
        Geometry3D box = csg.box3D(count*size, 10, size, false);
        box = csg.translate3D(0.5*count*size, 5, 0).transform(box);
        double curAdjust = startAdjust;
        List<Geometry3D> positives = new ArrayList<>();
        List<Geometry3D> negatives = new ArrayList<>();
        positives.add(box);
        for(int i = 0; i < count; i++)
        {
            Geometry3D hole = getHole(diameter, curAdjust, 10);
            hole = csg.rotate3DX(csg.degrees(-90)).transform(hole);
            hole = csg.translate3D(i*size + 0.5*size, 0, 0.5*size).transform(hole);
            negatives.add(hole);
            String s = String.format(Locale.ENGLISH, "%,.2f", curAdjust);
            Geometry3D text = getText(s);
            text = csg.translate3D(i*size+0.5*size, 0, size).transform(text);
            positives.add(text);
            curAdjust += deltaAdjust;
        }
        Geometry3D res = csg.union3D(positives);
        res = csg.difference3D(res, negatives);
        return res;
    }

    public Geometry3D getFixedHorizontalHoleCalibration(int count, double diameter, double diameterAdjust, double width, double startAdjust, double deltaAdjust)
    {
        double size = Math.max(diameter*2, 24);
        Geometry3D box = csg.box3D(count*size, 10, size, false);
        box = csg.translate3D(0.5*count*size, 5, 0).transform(box);
        double curAdjust = startAdjust;
        List<Geometry3D> positives = new ArrayList<>();
        List<Geometry3D> negatives = new ArrayList<>();
        positives.add(box);
        for(int i = 0; i < count; i++)
        {
            Geometry3D hole = getFixedHole(diameter, diameterAdjust, width, curAdjust, 10);
            hole = csg.rotate3DX(csg.degrees(-90)).transform(hole);
            hole = csg.translate3D(i*size + 0.5*size, 0, 0.5*size).transform(hole);
            negatives.add(hole);
            String s = String.format(Locale.ENGLISH, "%,.2f", curAdjust);
            Geometry3D text = getText(s);
            text = csg.translate3D(i*size+0.5*size, 0, size).transform(text);
            positives.add(text);
            curAdjust += deltaAdjust;
        }
        Geometry3D res = csg.union3D(positives);
        res = csg.difference3D(res, negatives);
        return res;
    }

    public Geometry3D getAxleCalibration(double diameter, double diameterAdjust, double width, double length)
    {
        double size = Math.max(diameter, 24);
        Geometry3D box = csg.box3D(size, 10, width, false);
        box = csg.translate3DY(5).transform(box);
        Geometry3D axle = getAxle(diameter, diameterAdjust, length, width);
        Geometry3D text = getText(String.format(Locale.ENGLISH, "%,.2f", diameterAdjust));
        text = csg.translate3DZ(width).transform(text);
        Geometry3D res = csg.union3D(box, axle, text);
        return res;
    }

    public Geometry3D getAxleCalibrations(int count, double diameter, double startAdjust, double deltaAdjust, double width, double length)
    {
        List<Geometry3D> axleCalibrations = new ArrayList<>();
        double size = Math.max(diameter, 24);
        for(int i = 0; i < count; i++)
        {
            Geometry3D axle = getAxleCalibration(diameter, startAdjust, width, length);
            axle = csg.translate3DX(i*(size + 3)).transform(axle);
            axleCalibrations.add(axle);
            startAdjust += deltaAdjust;
        }
        return csg.union3D(axleCalibrations);
    }
    private Geometry3D getText(String str)
    {
        Geometry2D text2D = csg.text2D(str, 4, 64);
        text2D = csg.translate2D(-0.5*str.length()*4, 3).transform(text2D);
        Geometry3D text3D = csg.linearExtrude(1, false, text2D);
        return text3D;
    }

    private Geometry3D getHole(double diameter, double adjust, double height)
    {
        double d = diameter + adjust;
        Geometry3D hole = csg.cylinder3D(d, height, 64, false);
        Geometry3D cone = csg.cone3D(d, d+2, 1, 128, false);
        cone = csg.translate3DZ(height-1).transform(cone);
        hole = csg.union3D(hole, cone);
        return hole;
    }

    private Geometry3D getFixedHole(double diameter, double diameterAdjust, double width, double widthAdjust, double height)
    {
        Geometry3D hole = getHole(diameter, diameterAdjust, height);
        Geometry3D restrict = csg.box3D(width+widthAdjust, diameter+diameterAdjust+2, height, false);
        hole = csg.intersection3D(hole, restrict);
        return hole;
    }


    private Geometry3D getAxle(double diameter, double adjust, double length, double width)
    {
        double d = diameter + adjust;
        double d2 = d-1.2;
        Geometry3D axle = csg.cylinder3D(d, length, 128, false);
        Geometry3D cone = csg.cone3D(d, d2, 0.6, 128, false);
        cone = csg.translate3DZ(length).transform(cone);
        axle = csg.union3D(axle, cone);
        Geometry3D restrict = csg.box3D(d, width, length+1, false);
        axle = csg.intersection3D(axle, restrict);
        axle = csg.rotate3DX(csg.degrees(90)).transform(axle);
        axle = csg.translate3DZ(0.5*width).transform(axle);
        return axle;
    }

    public static void main(String[] args)
    {
        JavaCSG csg = JavaCSGFactory.createDefault();
        PrinterCalibration printerCalibration = new PrinterCalibration(csg);
        Geometry3D test = printerCalibration.getRoundHorizontalHoleCalibration(3, 8, 0.2, 0.05);
        //Geometry3D test = printerCalibration.getFixedHorizontalHoleCalibration(5, 8, 0.1, 6, 0.0, 0.05);
        //Geometry3D test = printerCalibration.getRoundVerticalHoleCalibration(3, 8, 0.2, 0.05);
        //Geometry3D test = printerCalibration.getFixedVerticalHoleCalibration(5, 8, 0.1, 6, 0.0, 0.05);
        //Geometry3D test = printerCalibration.getAxleCalibration(8, -0.1, 8, 6);
        //Geometry3D test = printerCalibration.getAxleCalibrations(5, 8, -0.1, 0.05, 6, 10);
        csg.view(test);
    }
}
