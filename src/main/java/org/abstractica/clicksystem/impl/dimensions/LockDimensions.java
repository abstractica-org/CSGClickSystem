package org.abstractica.clicksystem.impl.dimensions;

import org.abstractica.javacsg.Geometry3D;
import org.abstractica.javacsg.JavaCSG;

public class LockDimensions
{
    private final JavaCSG csg;
    public final double gripSize;
    public final double gripDepth;
    public final double topClearance;
    public final double pressPlateDiameterAdjust;
    public final double bottomClearance;
    public final double squeezeAdjust;
    public final double toolPressLengthAdjust;
    public final double toolPressDiameterAdjust;
    public final double toolTipDiameter;
    public final double toolTipDiameterAdjust;
    public final double toolTipDepth;
    public final double toolTipDepthAdjust;
    public final double toolInsertDiameterAdjust;

    public LockDimensions(JavaCSG csg,
                          double gripSize, double gripDepth,
                          double topClearance, double pressPlateDiameterAdjust, double bottomClearance,
                          double squeezeAdjust,
                          double toolPressLengthAdjust,
                          double toolPressDiameterAdjust,
                          double toolTipDiameter,
                          double toolTipDiameterAdjust,
                          double toolTipDepth,
                          double toolTipDepthAdjust, double toolInsertDiameterAdjust)
    {
        this.csg = csg;
        this.gripSize = gripSize;
        this.gripDepth = gripDepth;
        this.topClearance = topClearance;
        this.pressPlateDiameterAdjust = pressPlateDiameterAdjust;
        this.bottomClearance = bottomClearance;
        this.squeezeAdjust = squeezeAdjust;
        this.toolPressLengthAdjust = toolPressLengthAdjust;
        this.toolPressDiameterAdjust = toolPressDiameterAdjust;
        this.toolTipDiameter = toolTipDiameter;
        this.toolTipDiameterAdjust = toolTipDiameterAdjust;
        this.toolTipDepth = toolTipDepth;
        this.toolTipDepthAdjust = toolTipDepthAdjust;
        this.toolInsertDiameterAdjust = toolInsertDiameterAdjust;
    }

    public Geometry3D getLockTool(ClickerDimensions cd, double insertLength, double handleDiameter, double handleLength)
    {
        double curHeight = handleLength;
        Geometry3D handle = csg.cylinder3D(handleDiameter, curHeight, 128, false);
        double insertDiameter = cd.axleDiameter + cd.axleDiameterAdjust + toolInsertDiameterAdjust;
        curHeight += insertLength;
        Geometry3D insertPart = csg.cylinder3D(insertDiameter, curHeight, 128, false);
        double pressDiameter = cd.innerDiameterSmall + cd.innerDiameterAdjust + pressPlateDiameterAdjust + toolPressDiameterAdjust;
        curHeight += topClearance + toolPressLengthAdjust;
        Geometry3D press = csg.cylinder3D(pressDiameter, curHeight, 128, false);
        double toolTipDiameter = this.toolTipDiameter + this.toolTipDiameterAdjust;
        double toolTipLength = this.toolTipDepth + this.toolTipDepthAdjust;
        double leadLength = 2.0/3 * toolTipLength;
        curHeight += leadLength;
        Geometry3D toolTipLead = csg.cylinder3D(toolTipDiameter, curHeight, 128, false);
        Geometry3D toolTipTip = csg.cone3D(toolTipDiameter, 0.8*toolTipDiameter, 1.0/3 * toolTipLength, 128, false);
        toolTipTip = csg.translate3DZ(curHeight).transform(toolTipTip);
        Geometry3D toolTip = csg.union3D(toolTipLead, toolTipTip);
        Geometry3D tool = csg.union3D(handle, insertPart, press, toolTip);
        return tool;
    }

    public Geometry3D getLockCutout(ClickerDimensions cd)
    {
        double topDiameter = cd.innerDiameterLarge + 2 * gripSize;
        double bottomDiameter = cd.innerDiameterSmall * 0.8;
        double coneHeight = cd.slitHeight - cd.barbDimensions.barbFreeSpace - bottomClearance - topClearance - gripDepth;
        Geometry3D lockCutout = csg.cone3D(bottomDiameter, topDiameter, coneHeight, 64, false);
        lockCutout = csg.rotate3DX(csg.degrees(180)).transform(lockCutout);
        lockCutout = csg.translate3DZ(coneHeight + cd.barbDimensions.barbFreeSpace + gripDepth).transform(lockCutout);
        return lockCutout;
    }

    public Geometry3D getLock(ClickerDimensions cd)
    {
        double topDiameter = cd.innerDiameterSmall + 2 * gripSize + squeezeAdjust;
        double bottomDiameter = topDiameter * 0.8 + squeezeAdjust;
        double coneHeight = cd.slitHeight - cd.barbDimensions.barbFreeSpace - bottomClearance - topClearance - gripDepth;
        Geometry3D lock = csg.cone3D(bottomDiameter, topDiameter, coneHeight, 64, false);
        double pressPlateDiameter = cd.innerDiameterSmall + cd.innerDiameterAdjust + toolPressDiameterAdjust + squeezeAdjust;
        double pressPlateHeight = cd.slitHeight - cd.barbDimensions.barbFreeSpace - bottomClearance - topClearance - coneHeight;
        Geometry3D pressPlate = csg.cylinder3D(pressPlateDiameter, pressPlateHeight, 64, false);
        pressPlate = csg.translate3DZ(coneHeight).transform(pressPlate);
        lock = csg.union3D(lock, pressPlate);
        double tipDiameter = toolTipDiameter;
        double tipDepth = toolTipDepth;
        Geometry3D toolHole = csg.cylinder3D(tipDiameter, tipDepth, 64, false);
        toolHole = csg.translate3DZ(-tipDepth + coneHeight + pressPlateHeight).transform(toolHole);
        lock = csg.difference3D(lock, toolHole);
        return lock;
    }

}
