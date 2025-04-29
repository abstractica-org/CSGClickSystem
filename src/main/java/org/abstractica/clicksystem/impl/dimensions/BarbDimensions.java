package org.abstractica.clicksystem.impl.dimensions;

public class BarbDimensions
{
    public final double barbLeadIn;
    public final double barbSize;
    public final double barbShoulder;
    public final double barbFreeSpace;
    public final double relativeBarbTipX;
    public final double barbPullTension;
    public final double barbPushTension;

    public BarbDimensions(double barbLeadIn, double barbSize, double barbShoulder, double barbFreeSpace, double relativeBarbTipX, double barbPullTension, double barbPushTension)
    {
        this.barbLeadIn = barbLeadIn;
        this.barbSize = barbSize;
        this.barbShoulder = barbShoulder;
        this.barbFreeSpace = barbFreeSpace;
        this.relativeBarbTipX = relativeBarbTipX;
        this.barbPullTension = barbPullTension;
        this.barbPushTension = barbPushTension;
    }
}
