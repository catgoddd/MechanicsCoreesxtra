package me.deecaad.core.mechanics.defaultmechanics;

import me.deecaad.core.MechanicsCore;
import me.deecaad.core.file.SerializeData;
import me.deecaad.core.file.SerializerException;
import me.deecaad.core.mechanics.CastData;
import me.deecaad.core.utils.VectorUtil;
import org.bukkit.NamespacedKey;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PushMechanic extends Mechanic {

    private double speed;
    private double verticalVelocity; // static upward velocity

    /**
     * Default constructor for serializer.
     */
    public PushMechanic() {
    }

    public PushMechanic(double speed, double verticalVelocity) {
        this.speed = speed;
        this.verticalVelocity = verticalVelocity;
    }

    public double getSpeed() {
        return speed;
    }

    public double getVerticalVelocity() {
        return verticalVelocity;
    }

    @Override
    protected void use0(CastData cast) {

        // We must have an entity to push
        if (cast.getTarget() == null)
            return;

        // Get horizontal direction vector only (ignore Y)
        Vector velocity = cast.getTargetLocation().subtract(cast.getSourceLocation()).toVector();
        velocity.setY(0);

        // If direction is zero, do nothing
        if (VectorUtil.isZero(velocity))
            return;

        // Normalize horizontal vector and multiply by speed
        velocity.normalize().multiply(speed);

        // Set static upward velocity
        velocity.setY(verticalVelocity);

        // Apply to entity
        cast.getTarget().setVelocity(velocity);
    }

    @Override
    public @NotNull NamespacedKey getKey() {
        return new NamespacedKey(MechanicsCore.getInstance(), "push");
    }

    @Override
    public @Nullable String getWikiLink() {
        return "https://cjcrafter.gitbook.io/mechanics/mechanics/push";
    }

    @NotNull @Override
    public Mechanic serialize(@NotNull SerializeData data) throws SerializerException {
        double speed = data.of("Speed").assertExists().getDouble().getAsDouble();
        double verticalVelocity = data.of("Vertical_Velocity").getDouble().orElse(0.5); // default Y velocity

        return applyParentArgs(data, new PushMechanic(speed, verticalVelocity));
    }
}
