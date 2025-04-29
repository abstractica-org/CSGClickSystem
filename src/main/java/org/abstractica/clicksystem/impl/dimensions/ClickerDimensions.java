package org.abstractica.clicksystem.impl.dimensions;

import org.abstractica.javacsg.*;
import org.abstractica.profiles.Profile2DBuilder;
import org.abstractica.profiles.Profile2DStitcher;

import java.util.ArrayList;
import java.util.List;

public class ClickerDimensions
{
    private final JavaCSG csg;
    public final BarbDimensions barbDimensions;
    public final LockDimensions lockDimensions;
    public final double unit;
    public final double clickerWidth;
    public final double clickerWidthAdjust;
    public final double holeWidthAdjust;
    public final double axleDiameter;
    public final double axleDiameterAdjust;
    public final double axleHoleDiameterAdjust;
    public final double headDiameter;
    public final double headDiameterAdjust;
    public final double headHoleDiameterAdjust;
    public final double innerDiameterSmall;
    public final double innerDiameterLarge;
    public final double innerDiameterAdjust;
    public final double slitHeight;
    public final double slitWidthSmall;
    public final double slitWidthLarge;


    public ClickerDimensions(JavaCSG csg,
                             BarbDimensions barbDimensions,
                             LockDimensions lockDimensions,
                             double unit,
                             double clickerWidth, double clickerWidthAdjust, double holeWidthAdjust,
                             double axleDiameter, double axleDiameterAdjust, double axleHoleDiameterAdjust,
                             double headDiameterAdjust, double headHoleDiameterAdjust,
                             double innerDiameterSmall, double innerDiameterLarge, double innerDiameterAdjust,
                             double slitHeight, double slitWidthSmall, double slitWidthLarge)
    {
        this.csg = csg;
        this.barbDimensions = barbDimensions;
        this.lockDimensions = lockDimensions;
        this.unit = unit;
        this.clickerWidth = clickerWidth;
        this.clickerWidthAdjust = clickerWidthAdjust;
        this.holeWidthAdjust = holeWidthAdjust;
        this.axleDiameter = axleDiameter;
        this.axleDiameterAdjust = axleDiameterAdjust;
        this.axleHoleDiameterAdjust = axleHoleDiameterAdjust;
        this.headDiameter = axleDiameter + 2*barbDimensions.barbSize;
        this.headDiameterAdjust = headDiameterAdjust;
        this.headHoleDiameterAdjust = headHoleDiameterAdjust;
        this.innerDiameterSmall = innerDiameterSmall;
        this.innerDiameterLarge = innerDiameterLarge;
        this.innerDiameterAdjust = innerDiameterAdjust;
        this.slitHeight = slitHeight;
        this.slitWidthSmall = slitWidthSmall;
        this.slitWidthLarge = slitWidthLarge;
    }

    public JavaCSG getJavaCSG()
    {
        return csg;
    }

    public void addStartClickerProfile(Profile2DStitcher stitcher, double length)
    {
        Profile2DBuilder b = stitcher.createProfileBuilder();
        generateClickerProfile(b, length);
        stitcher.add(csg.translate2DY(length), b.getProfile());
    }

    public void addEndClickerProfile(Profile2DStitcher stitcher, double length)
    {
        Transform2D mirror = csg.mirror2D(0, 1);
        Transform2D translate = csg.translate2DY(-length);
        Transform2D pointTransform = csg.compose2D(mirror, translate);
        Profile2DBuilder b = stitcher.createProfileBuilder(pointTransform);
        generateClickerProfile(b, length);
        stitcher.add(csg.translate2DY(length), b.getProfile());
    }

    public double axleToHeadTransitionHeight()
    {
        return 0.5 * (headDiameter - axleDiameter);
    }

    public double leadInLength()
    {
        return barbDimensions.barbLeadIn;
    }

    public void fixOverhangFromHead(Profile2DStitcher stitcher)
    {
        double height = 0.5*headDiameter;
        Profile2DBuilder b = stitcher.createProfileBuilder();
        b.add(0,0);
        b.add(0.5*(headDiameter+headDiameterAdjust), 0);
        b.add(0, height);
        stitcher.add(csg.translate2DY(height), b.getProfile());
    }

    public void fixOverhangFromAxle(Profile2DStitcher stitcher)
    {
        double height = 0.5*axleDiameter;
        Profile2DBuilder b = stitcher.createProfileBuilder();
        b.add(0,0);
        b.add(0.5*(axleDiameter+axleDiameterAdjust), 0);
        b.add(0, height);
        stitcher.add(csg.translate2DY(height), b.getProfile());
    }

    public void addClickerAxleToClickerHead(Profile2DStitcher stitcher)
    {
        double height = axleToHeadTransitionHeight();
        Profile2DBuilder b = stitcher.createProfileBuilder();
        b.add(0,0);
        b.add(0.5*(axleDiameter+axleDiameterAdjust), 0);
        b.add(0.5*(headDiameter+headDiameterAdjust), height);
        b.add(0, height);
        stitcher.add(csg.translate2DY(height), b.getProfile());
    }

    public void addClickerHeadToClickerAxle(Profile2DStitcher stitcher)
    {
        double height = axleToHeadTransitionHeight();
        Profile2DBuilder b = stitcher.createProfileBuilder();
        b.add(0,0);
        b.add(0.5*(headDiameter+headDiameterAdjust), 0);
        b.add(0.5*(axleDiameter+axleDiameterAdjust), height);
        b.add(0, height);
        stitcher.add(csg.translate2DY(height), b.getProfile());
    }

    public void addClickerHead(Profile2DStitcher stitcher, double length)
    {
        Profile2DBuilder b = stitcher.createProfileBuilder();
        b.add(0,0);
        b.add(0.5*(headDiameter+headDiameterAdjust), 0);
        b.add(0.5*(headDiameter+headDiameterAdjust), length);
        b.add(0, length);
        stitcher.add(csg.translate2DY(length), b.getProfile());
    }

    public void addClickerAxle(Profile2DStitcher stitcher, double length)
    {
        Profile2DBuilder b = stitcher.createProfileBuilder();
        b.add(0,0);
        b.add(0.5*(axleDiameter+axleDiameterAdjust), 0);
        b.add(0.5*(axleDiameter+axleDiameterAdjust), length);
        b.add(0, length);
        stitcher.add(csg.translate2DY(length), b.getProfile());
    }

    public void addHoleHead(Profile2DStitcher stitcher, double length)
    {
        Profile2DBuilder b = stitcher.createProfileBuilder();
        b.add(0,0);
        b.add(0.5*(headDiameter+headHoleDiameterAdjust), 0);
        b.add(0.5*(headDiameter+headHoleDiameterAdjust), length);
        b.add(0, length);
        stitcher.add(csg.translate2DY(length), b.getProfile());
    }

    public void addHoleAxle(Profile2DStitcher stitcher, double length)
    {
        Profile2DBuilder b = stitcher.createProfileBuilder();
        b.add(0,0);
        b.add(0.5*(axleDiameter+axleHoleDiameterAdjust), 0);
        b.add(0.5*(axleDiameter+axleHoleDiameterAdjust), length);
        b.add(0, length);
        stitcher.add(csg.translate2DY(length), b.getProfile());
    }

    public void addHoleAxleToHoleHead(Profile2DStitcher stitcher)
    {
        double height = axleToHeadTransitionHeight();
        Profile2DBuilder b = stitcher.createProfileBuilder();
        b.add(0,0);
        b.add(0.5*(axleDiameter+axleHoleDiameterAdjust), 0);
        b.add(0.5*(headDiameter+headHoleDiameterAdjust), height);
        b.add(0, height);
        stitcher.add(csg.translate2DY(height), b.getProfile());
    }

    public void addHoleHeadToHoleAxle(Profile2DStitcher stitcher)
    {
        double height = axleToHeadTransitionHeight();
        Profile2DBuilder b = stitcher.createProfileBuilder();
        b.add(0,0);
        b.add(0.5*(headDiameter+headHoleDiameterAdjust), 0);
        b.add(0.5*(axleDiameter+axleHoleDiameterAdjust), height);
        b.add(0, height);
        stitcher.add(csg.translate2DY(height), b.getProfile());
    }

    public Geometry3D getClickerRemoveTool(double length)
    {

        //Tool tip
        //Inner hole
        double barbMove = barbDimensions.barbSize + barbDimensions.barbPushTension + 0.0;
        Geometry3D tip = getInnerCylinderAndSlit(-2.0*barbMove);
        Geometry3D axle = csg.cylinder3D(axleDiameter + axleDiameterAdjust - 0.1, length, 128, false);
        axle = csg.translate3DZ(-length).transform(axle);
        Geometry3D res = csg.union3D(tip, axle);

        Geometry3D restrict = csg.box3D(2*headDiameter,clickerWidth + clickerWidthAdjust - 0.2, length+slitHeight, false);
        restrict = csg.translate3DZ(-length).transform(restrict);
        res = csg.intersection3D(res, restrict);
        return res;
    }

    public Geometry3D getClickerInsertTool(double length)
    {

        Geometry3D tip = getInnerCylinderAndSlit(-0.2);
        tip = csg.translate3DZ(3).transform(tip);
        Geometry3D holder = csg.cylinder3D(11.8, slitHeight+3, 128, false);
        Geometry3D holderHole = csg.cone3D(headDiameter + headDiameterAdjust, axleDiameter + axleDiameterAdjust + 0.2, slitHeight, 128, false);
        holderHole = csg.translate3DZ(3).transform(holderHole);
        holder = csg.difference3D(holder, holderHole);
        holder = csg.union3D(holder, tip);
        Geometry3D axle = csg.cylinder3D(axleDiameter + axleDiameterAdjust - 0.2, length, 128, false);
        axle = csg.translate3DZ(-length).transform(axle);
        Geometry3D res = csg.union3D(holder, axle);

        Geometry3D restrict = csg.box3D(2*headDiameter,clickerWidth + clickerWidthAdjust - 0.2, length+slitHeight+3, false);
        restrict = csg.translate3DZ(-length).transform(restrict);
        res = csg.intersection3D(res, restrict);
        return res;
    }

    private void generateClickerProfile(Profile2DBuilder pb, double length)
    {
        double axleRadius = 0.5 * (axleDiameter + axleDiameterAdjust);
        pb.add(0, barbDimensions.barbFreeSpace);
        pb.add(axleRadius + barbDimensions.relativeBarbTipX,  pb.curY());
        pb.add(axleRadius + barbDimensions.barbSize + barbDimensions.barbPushTension,
                barbDimensions.barbLeadIn - barbDimensions.barbShoulder + barbDimensions.barbPullTension    );
        pb.move(0, barbDimensions.barbShoulder);
        pb.add(axleRadius, pb.curY() + barbDimensions.barbSize);
        if(length > pb.curY())
        {
            pb.add(axleRadius, length);
        }
        pb.add(0, pb.curY());

        Geometry3D cutout = getClickerCutout();
        //Add cutout to profile builder
        pb.cutout(cutout);
    }

    private Geometry3D getClickerCutout()
    {
        //Generate inner hole and slit
        Geometry3D innerHoleAndSlit = getInnerCylinderAndSlit(0);

        //Generate lock cutout
        Geometry3D lockCutout = lockDimensions.getLockCutout(this);

        //Combine all cutouts
        Geometry3D cutout = csg.union3D(innerHoleAndSlit, lockCutout);
        cutout = csg.rotate3DX(csg.degrees(-90)).transform(cutout);
        return cutout;
    }

    private Geometry3D getInnerCylinderAndSlit(double adjust)
    {
        //Generate inner hole
        Geometry3D innerHole = csg.cone3D(innerDiameterLarge + innerDiameterAdjust + adjust, innerDiameterSmall + innerDiameterAdjust + adjust, slitHeight, 128,  false);

        //Generate slit
        List<Vector2D> vertices = new ArrayList<>();
        vertices.add(csg.vector2D(0.5*(slitWidthLarge+adjust), 0));
        vertices.add(csg.vector2D(0.5*(slitWidthSmall+adjust), slitHeight));
        vertices.add(csg.vector2D(-0.5*(slitWidthSmall+adjust), slitHeight));
        vertices.add(csg.vector2D(-0.5*(slitWidthLarge+adjust), 0));
        Geometry2D slit2D = csg.polygon2D(vertices);
        Geometry3D slit = csg.linearExtrude(headDiameter + 2, false, slit2D);
        slit = csg.translate3DZ(-0.5*(headDiameter+2)).transform(slit);
        slit = csg.rotate3DX(csg.degrees(90)).transform(slit);
        return csg.union3D(innerHole, slit);
    }

}
