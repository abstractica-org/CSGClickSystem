package org.abstractica.profiles;

import org.abstractica.javacsg.Transform2D;

public interface Profile2DStitcher
{
    Profile2DBuilder createProfileBuilder();
    Profile2DBuilder createProfileBuilder(Transform2D pointTransform);
    void add(Transform2D localEnd, Profile2D profile2D);
    void add(Transform2D worldBegin, Transform2D localEnd, Profile2D profile2D);
    Transform2D getCurrentTransform();
    Profile2D getResult();
    Profile2DStitcher fork(Transform2D localAttach);
}
