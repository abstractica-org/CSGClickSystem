package org.abstractica.clicksystem;

import org.abstractica.clicksystem.impl.ClickSystemImpl;
import org.abstractica.clicksystem.impl.dimensions.BarbDimensions;
import org.abstractica.clicksystem.impl.dimensions.ClickerDimensions;
import org.abstractica.clicksystem.impl.dimensions.LockDimensions;
import org.abstractica.javacsg.JavaCSG;

public class ClickSystemFactory
{
    public static ClickSystem system_12_8_6_hard(JavaCSG csg)
    {
        BarbDimensions barbDimensions = new BarbDimensions(
                1.6,
                0.6,
                0.2,
                0.2,
                -0.2,
                0.0,
                0.0);
        LockDimensions lockDimensions = new LockDimensions(csg,
                0.4,
                0.8,
                0.2,
                0.4,
                0.2,
                -0.2,
                1,
                -0.2,
                2.8,
                -0.2,
                3,
                -0.2,
                -0.2);
        ClickerDimensions clickerDimensions = new ClickerDimensions(csg, barbDimensions, lockDimensions,
                12,6, 0, 0.2,
                8, 0.0, 0.2,
                0.0, 0.2,
                4.6, 5.6, 0.0,
                4.8, 2.8, 3.8);
        return new ClickSystemImpl(clickerDimensions);
    }

    public static ClickSystem system_12_8_6_soft(JavaCSG csg)
    {
        BarbDimensions barbDimensions = new BarbDimensions(
                1.6,
                0.6,
                0.2,
                0.2,
                -0.4,
                0.0,
                -0.4);
        LockDimensions lockDimensions = new LockDimensions(csg,
                0.4,
                0.8,
                0.2,
                0.4,
                0.2,
                -0.2,
                1,
                -0.2,
                2.8,
                -0.2,
                3,
                -0.2,
                -0.2);
        ClickerDimensions clickerDimensions = new ClickerDimensions(csg, barbDimensions, lockDimensions,
                12,6, 0, 0.2,
                8, 0.0, 0.25,
                0.0, 0.25,
                4.6, 5.6, 0.0,
                4.8, 2.8, 3.8);
        return new ClickSystemImpl(clickerDimensions);
    }

    public static ClickSystem system_12_8_6_medium(JavaCSG csg)
    {
        BarbDimensions barbDimensions = new BarbDimensions(
                1.6,
                0.6,
                0.2,
                0.2,
                -0.4,
                0.0,
                -0.2);
        LockDimensions lockDimensions = new LockDimensions(csg,
                0.4,
                0.8,
                0.2,
                0.4,
                0.2,
                -0.2,
                1,
                -0.2,
                2.8,
                -0.2,
                3,
                -0.2,
                -0.2);
        ClickerDimensions clickerDimensions = new ClickerDimensions(csg, barbDimensions, lockDimensions,
                12,6, 0, 0.2,
                8, 0.0, 0.25,
                0.0, 0.25,
                4.6, 5.6, 0.0,
                4.8, 2.8, 3.8);
        return new ClickSystemImpl(clickerDimensions);
    }

}
