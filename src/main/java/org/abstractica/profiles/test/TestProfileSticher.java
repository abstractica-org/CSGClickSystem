package org.abstractica.profiles.test;

import org.abstractica.javacsg.JavaCSG;
import org.abstractica.javacsg.JavaCSGFactory;
import org.abstractica.profiles.Profile2D;
import org.abstractica.profiles.Profile2DBuilder;
import org.abstractica.profiles.Profile2DStitcher;
import org.abstractica.profiles.impl.Profile2DStitcherGeometry2DImpl;

public class TestProfileSticher
{
    public static void main(String[] args)
    {
        JavaCSG csg = JavaCSGFactory.createDefault();
        Profile2DStitcher stitcher = new Profile2DStitcherGeometry2DImpl(csg);
        for(int i = 0; i < 4; ++i)
        {
            addProfile(csg, stitcher);
        }
        Profile2D p = stitcher.getResult();

        csg.view(p.getGeometry2D());

    }

    public static void addProfile(JavaCSG csg, Profile2DStitcher stitcher)
    {
        Profile2DBuilder b = stitcher.createProfileBuilder(csg.rotate2D(csg.degrees(45)));
        b.add(0,0);
        b.add(100, 0);
        b.add(100, 100);
        b.add(0, 100);
        b.cutout(csg.sphere3D(30, 64, true));
        stitcher.add(csg.translate2D(0, 100), b.getProfile());
    }
}
