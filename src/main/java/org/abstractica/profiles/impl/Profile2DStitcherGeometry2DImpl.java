package org.abstractica.profiles.impl;

import org.abstractica.javacsg.*;
import org.abstractica.profiles.Profile2D;
import org.abstractica.profiles.Profile2DBuilder;
import org.abstractica.profiles.Profile2DStitcher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Profile2DStitcherGeometry2DImpl implements Profile2DStitcher
{
    private final JavaCSG csg;
    private final List<Geometry2D> geometryList;
    private final List<Geometry3D> cutoutList;
    private final List<Geometry3D> addOnsList;
    private final List<Profile2DStitcherGeometry2DImpl> forks;
    private Transform2D curTransform2D;

    public Profile2DStitcherGeometry2DImpl(JavaCSG csg)
    {
        this(csg, csg.identity2D());
    }

    public Profile2DStitcherGeometry2DImpl(JavaCSG csg, Transform2D curTransform2D)
    {
        this.csg = csg;
        this.forks = new ArrayList<>();
        this.geometryList = new ArrayList<>();
        this.addOnsList = new ArrayList<>();
        this.cutoutList = new ArrayList<>();
        this.curTransform2D = curTransform2D;
    }

    @Override
    public Profile2DBuilder createProfileBuilder()
    {
        return createProfileBuilder(csg.identity2D());
    }

    @Override
    public Profile2DBuilder createProfileBuilder(Transform2D pointTransform)
    {
        return new Profile2DBuilderImpl(csg, pointTransform);
    }

    @Override
    public void add(Transform2D localEnd, Profile2D profile2D)
    {
        Transform3D curTransform3D = curTransform2D.asTransform3D();
        geometryList.add(curTransform2D.transform(profile2D.getGeometry2D()));
        for(Geometry3D addOn : profile2D.getAddOns())
        {
            addOnsList.add(curTransform3D.transform(addOn));
        }
        for(Geometry3D cutout : profile2D.getCutouts())
        {
            cutoutList.add(curTransform3D.transform(cutout));
        }
        curTransform2D = csg.compose2D(curTransform2D, localEnd);
    }

    @Override
    public void add(Transform2D worldBegin, Transform2D localEnd, Profile2D profile2D)
    {
        curTransform2D = worldBegin;
        add(localEnd, profile2D);
    }

    @Override
    public Transform2D getCurrentTransform()
    {
        return curTransform2D;
    }

    @Override
    public Profile2D getResult()
    {
        List<Geometry2D> geometry2DList = new ArrayList<>();
        List<Geometry3D> addOnList = new ArrayList<>();
        List<Geometry3D> cutoutList = new ArrayList<>();
        collectAll(geometry2DList, addOnList, cutoutList);
        return new Profile2DImpl(csg.union2D(geometry2DList), addOnList, cutoutList);
    }

    @Override
    public Profile2DStitcher fork(Transform2D localAttach)
    {
        Profile2DStitcherGeometry2DImpl fork = new Profile2DStitcherGeometry2DImpl(csg, csg.compose2D(curTransform2D, localAttach));
        forks.add(fork);
        return fork;
    }

    private void collectAll(List<Geometry2D> geometryList, List<Geometry3D> addOnList, List<Geometry3D> cutoutList)
    {
        geometryList.addAll(this.geometryList);
        addOnList.addAll(addOnsList);
        cutoutList.addAll(this.cutoutList);
        for(Profile2DStitcherGeometry2DImpl fork : forks)
        {
            fork.collectAll(geometryList, addOnList, cutoutList);
        }
    }


    private class Profile2DImpl implements Profile2D
    {
        private final Geometry2D geometry;
        private final List<Geometry3D> addOns;
        private final List<Geometry3D> cutouts;

        private Profile2DImpl(Geometry2D geometry, List<Geometry3D> addOns, List<Geometry3D> cutouts)
        {
            this.geometry = geometry;
            this.addOns = Collections.unmodifiableList(addOns);
            this.cutouts = Collections.unmodifiableList(cutouts);

        }

        @Override
        public Geometry2D getGeometry2D()
        {
            return geometry;
        }

        @Override
        public List<Geometry3D> getAddOns()
        {
            return addOns;
        }

        @Override
        public List<Geometry3D> getCutouts()
        {
            return cutouts;
        }
    }

    private class Profile2DBuilderImpl implements Profile2DBuilder
    {
        private final JavaCSG csg;
        private final Transform2D pointTransform;
        private final List<Vector2D> vertices;
        private ArrayList<Geometry3D> addOns;
        private ArrayList<Geometry3D> cutouts;
        private double curX, curY;


        public Profile2DBuilderImpl(JavaCSG csg, Transform2D pointTransform)
        {
            this.csg = csg;
            this.pointTransform = pointTransform;
            this.vertices = new ArrayList<>();
            this.addOns = new ArrayList<>();
            this.cutouts = new ArrayList<>();
            this.curX = 0;
            this.curY = 0;
        }

        @Override
        public void add(double x, double y)
        {
            curX = x;
            curY = y;
            vertices.add(pointTransform.transformPoint(csg.vector2D(x, y)));
        }

        @Override
        public double curX()
        {
            return curX;
        }

        @Override
        public double curY()
        {
            return curY;
        }

        @Override
        public void addOn(Geometry3D addOn)
        {
            addOns.add(pointTransform.asTransform3D().transform(addOn));
        }

        @Override
        public void cutout(Geometry3D cutout)
        {
            cutouts.add(pointTransform.asTransform3D().transform(cutout));
        }

        @Override
        public Profile2D getProfile()
        {
            addOns.trimToSize();
            cutouts.trimToSize();
            return new Profile2DImpl(csg.polygon2D(vertices), addOns, cutouts);
        }
    }
}
