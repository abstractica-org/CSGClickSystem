package org.abstractica.clicksystem.impl;

import org.abstractica.clicksystem.ClickSystem;
import org.abstractica.clicksystem.impl.dimensions.ClickerDimensions;
import org.abstractica.javacsg.Geometry3D;
import org.abstractica.javacsg.JavaCSG;
import org.abstractica.profiles.Profile2D;
import org.abstractica.profiles.Profile2DStitcher;
import org.abstractica.profiles.impl.Profile2DStitcherGeometry2DImpl;

import java.util.ArrayList;
import java.util.List;

public class ClickSystemImpl implements ClickSystem
{
    private final JavaCSG csg;
    private final ClickerDimensions clickerDimensions;

    public ClickSystemImpl(ClickerDimensions clickerDimensions)
    {
        this.csg = clickerDimensions.getJavaCSG();
        this.clickerDimensions = clickerDimensions;
    }

    @Override
    public JavaCSG getJavaCSG()
    {
        return csg;
    }

    @Override
    public double getUnit()
    {
        return clickerDimensions.unit;
    }

    @Override
    public Geometry3D getClicker(double length, double extraHeadLength)
    {
        double clickerLength = length - clickerDimensions.leadInLength() - clickerDimensions.axleToHeadTransitionHeight();
        Profile2DStitcher stitcher = new Profile2DStitcherGeometry2DImpl(csg);
        clickerDimensions.addStartClickerProfile(stitcher, clickerLength);
        clickerDimensions.addClickerAxleToClickerHead(stitcher);
        clickerDimensions.addClickerHead(stitcher, clickerDimensions.leadInLength() + extraHeadLength);
        return makeClicker(stitcher.getResult(), length + extraHeadLength, clickerDimensions.headDiameter);
    }

    @Override
    public Geometry3D getDoubleClicker(double sideALength, double sideBLength, double extraMiddleHeadLength)
    {
        double clickerLengthA = sideALength - clickerDimensions.leadInLength() - clickerDimensions.axleToHeadTransitionHeight();
        double clickerLengthB = sideBLength - clickerDimensions.leadInLength() - clickerDimensions.axleToHeadTransitionHeight();
        Profile2DStitcher stitcher = new Profile2DStitcherGeometry2DImpl(csg);
        clickerDimensions.addStartClickerProfile(stitcher, clickerLengthA);
        clickerDimensions.addClickerAxleToClickerHead(stitcher);
        clickerDimensions.addClickerHead(stitcher, 2*clickerDimensions.leadInLength() + extraMiddleHeadLength);
        clickerDimensions.addClickerHeadToClickerAxle(stitcher);
        clickerDimensions.addEndClickerProfile(stitcher, clickerLengthB);
        return makeClicker(stitcher.getResult(), sideALength + sideBLength + extraMiddleHeadLength, clickerDimensions.headDiameter);
    }

    @Override
    public Geometry3D getTurnHole(double length, boolean leadInTop, boolean leadInBottom, int repeats, boolean fixOverhang)
    {
        if(repeats < 1)
        {
            throw new IllegalArgumentException("Repeats must be at least 1");
        }
        Profile2D profile2D = makeHoleProfile(length, leadInTop, leadInBottom);
        Geometry3D res = makeTurnHole(profile2D);
        if(repeats > 1)
        {
            res = repeat(res, length, repeats);
        }
        if(fixOverhang)
        {
            Geometry3D overhangFix = getTurnOverhangFix(leadInTop);
            overhangFix = csg.translate3DZ(repeats*length).transform(overhangFix);
            res = csg.union3D(res, overhangFix);
        }
        return res;
    }

    @Override
    public Geometry3D getFixedHole(double length, boolean leadInTop, boolean leadInBottom, int repeats, boolean fixOverhang)
    {
        if(repeats < 1)
        {
            throw new IllegalArgumentException("Repeats must be at least 1");
        }
        Profile2D profile2D = makeHoleProfile(length, leadInTop, leadInBottom);
        Geometry3D res = makeFixedHole(profile2D, length, 2*clickerDimensions.headDiameter);
        if(repeats > 1)
        {
            res = repeat(res, length, repeats);
        }
        if(fixOverhang)
        {
            Geometry3D overhangFix = getFixedOverhangFix(leadInTop);
            overhangFix = csg.translate3DZ(repeats*length).transform(overhangFix);
            res = csg.union3D(res, overhangFix);
        }
        return res;
    }

    @Override
    public Geometry3D getTurnHeadThroughHole(double length, boolean fixOverhang)
    {
        Profile2DStitcher stitcher = new Profile2DStitcherGeometry2DImpl(csg);
        clickerDimensions.addHoleHead(stitcher, length);
        Geometry3D res = makeTurnHole(stitcher.getResult());
        if(fixOverhang)
        {
            Geometry3D overhangFix = getTurnOverhangFix(true);
            overhangFix = csg.translate3DZ(length).transform(overhangFix);
            res = csg.union3D(res, overhangFix);
        }
        return res;
    }

    @Override
    public Geometry3D getFixedHeadThroughHole(double length, boolean fixOverhang)
    {
        Profile2DStitcher stitcher = new Profile2DStitcherGeometry2DImpl(csg);
        clickerDimensions.addHoleHead(stitcher, length);
        Geometry3D res = makeFixedHole(stitcher.getResult(), length, 2*clickerDimensions.headDiameter);
        if(fixOverhang)
        {
            Geometry3D overhangFix = getFixedOverhangFix(true);
            overhangFix = csg.translate3DZ(length).transform(overhangFix);
            res = csg.union3D(res, overhangFix);
        }
        return res;
    }

    @Override
    public Geometry3D getClickerLock()
    {
        return clickerDimensions.lockDimensions.getLock(clickerDimensions);
    }

    @Override
    public Geometry3D getInsertTool()
    {
        Geometry3D insertTool = clickerDimensions.getClickerInsertTool(60);
        insertTool = csg.rotate3DX(csg.degrees(-90)).transform(insertTool);
        return insertTool;
    }

    @Override
    public Geometry3D getRemoveTool()
    {
       Geometry3D removeTool = clickerDimensions.getClickerRemoveTool(60);
       removeTool = csg.rotate3DX(csg.degrees(-90)).transform(removeTool);
       return removeTool;
    }

    @Override
    public Geometry3D getLockTool()
    {
        return clickerDimensions.lockDimensions.getLockTool(clickerDimensions, 6,16,40);
    }

    private Geometry3D makeClicker(Profile2D profile2D, double length, double radius)
    {
        Geometry3D res = csg.rotateExtrude(csg.rotations(1), 128, profile2D.getGeometry2D());
        Geometry3D restrict = csg.box3D(2*(radius+1), clickerDimensions.clickerWidth + clickerDimensions.clickerWidthAdjust, length, false);
        res = csg.intersection3D(res, restrict);
        res = csg.rotate3DX(csg.degrees(-90)).transform(res);
        if(!profile2D.getAddOns().isEmpty())
        {
            List<Geometry3D> unionList = new ArrayList<>();
            unionList.add(res);
            unionList.addAll(profile2D.getAddOns());
            res = csg.union3D(unionList);
        }
        if(!profile2D.getCutouts().isEmpty())
        {
            res = csg.difference3D(res, profile2D.getCutouts());
        }
        return res;
    }

    public Profile2D makeHoleProfile(double length, boolean leadInTop, boolean leadInBottom)
    {
        Profile2DStitcher stitcher = new Profile2DStitcherGeometry2DImpl(csg);
        double topLength = leadInTop ? clickerDimensions.leadInLength() + clickerDimensions.axleToHeadTransitionHeight() : 0;
        double bottomLength = leadInBottom ? clickerDimensions.leadInLength() + clickerDimensions.axleToHeadTransitionHeight() : 0;
        double axleLength = length - topLength - bottomLength;
        if(leadInBottom)
        {
            clickerDimensions.addHoleHead(stitcher, clickerDimensions.leadInLength());
            clickerDimensions.addHoleHeadToHoleAxle(stitcher);
        }
        clickerDimensions.addHoleAxle(stitcher, axleLength);
        if(leadInTop)
        {
            clickerDimensions.addHoleAxleToHoleHead(stitcher);
            clickerDimensions.addHoleHead(stitcher, clickerDimensions.leadInLength());
        }
        return stitcher.getResult();
    }

    private Geometry3D makeTurnHole(Profile2D profile2D)
    {
        Geometry3D hole = csg.rotateExtrude(csg.rotations(1), 128, profile2D.getGeometry2D());
        return finishHole(profile2D, hole);
    }

    private Geometry3D makeFixedHole(Profile2D profile2D, double length, double radius)
    {
        Geometry3D hole = csg.rotateExtrude(csg.rotations(1), 128, profile2D.getGeometry2D());
        Geometry3D restrict = csg.box3D(clickerDimensions.clickerWidth + clickerDimensions.holeWidthAdjust,2*(radius+1), length, false);
        hole = csg.intersection3D(hole, restrict);
        return finishHole(profile2D, hole);
    }

    private Geometry3D finishHole(Profile2D profile2D, Geometry3D hole)
    {
        if(profile2D.getAddOns().isEmpty() && profile2D.getCutouts().isEmpty())
        {
            return hole;
        }
        hole = csg.rotate3DX(csg.degrees(-90)).transform(hole);
        if(!profile2D.getAddOns().isEmpty())
        {
            hole = csg.union3D(hole, profile2D.getAddOns());
        }
        if(!profile2D.getCutouts().isEmpty())
        {
            hole = csg.difference3D(hole, profile2D.getCutouts());
        }
        hole = csg.rotate3DX(csg.degrees(90)).transform(hole);
        return hole;
    }

    private Geometry3D repeat(Geometry3D geometry, double length, int repeats)
    {
        List<Geometry3D> unionList = new ArrayList<>();
        unionList.add(geometry);
        for(int i = 1; i < repeats; i++)
        {
            unionList.add(csg.translate3DZ(i*length).transform(geometry));
        }
        return csg.union3D(unionList);
    }

    private Geometry3D getFixedOverhangFix(boolean leadIn)
    {
        Profile2DStitcher stitcher = new Profile2DStitcherGeometry2DImpl(csg);
        if(leadIn)
        {
            clickerDimensions.fixOverhangFromHead(stitcher);
        }
        else
        {
            clickerDimensions.fixOverhangFromAxle(stitcher);
        }
        Profile2D overhangProfile2D = stitcher.getResult();
        return makeFixedHole(overhangProfile2D, 2*clickerDimensions.headDiameter, 2*clickerDimensions.headDiameter);
    }

    private Geometry3D getTurnOverhangFix(boolean leadIn)
    {
        Profile2DStitcher stitcher = new Profile2DStitcherGeometry2DImpl(csg);
        if(leadIn)
        {
            clickerDimensions.fixOverhangFromHead(stitcher);
        }
        else
        {
            clickerDimensions.fixOverhangFromAxle(stitcher);
        }
        Profile2D overhangProfile2D = stitcher.getResult();
        return makeTurnHole((overhangProfile2D));
    }
}
