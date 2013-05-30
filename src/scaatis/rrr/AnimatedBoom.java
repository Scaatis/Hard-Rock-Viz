package scaatis.rrr;

import java.awt.geom.Point2D;

import scaatis.rrr.SpriteSheet.SpriteSheetResource;

public class AnimatedBoom extends UpdatableObject {
    private double spf;
    private double counter;

    public AnimatedBoom(int id, Point2D location, SpriteSheetResource resource, int fps) {
        super(id, location, resource);
        spf = 1.0 / fps;
        counter = 0;
    }

    @Override
    public void update(double delta) {
        counter += delta;
        while (counter > spf) {
            counter -= spf;
            SpriteSheetResource resource = (SpriteSheetResource) getResource();
            if (resource.getFrame() == resource.getFrames() - 1) {
                destroy();
            } else {
                resource.advanceFrame();
            }
        }
    }

    public int getZ() {
        return super.getZ() + 10;
    }
}
