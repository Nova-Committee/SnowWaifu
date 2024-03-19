package committee.nova.snowwaifu.common.entity.ai;

import committee.nova.snowwaifu.common.entity.impl.SnowWaifuEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import twilightforest.entity.ai.goal.HoverBaseGoal;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

public class HoverBeamGoal extends HoverBaseGoal<SnowWaifuEntity> {
    private int hoverTimer;
    private final int maxHoverTime;

    public HoverBeamGoal(SnowWaifuEntity snowQueen, int hoverTime) {
        super(snowQueen, 6.0F, 6.0F);
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        this.maxHoverTime = hoverTime;
        this.hoverTimer = 0;
    }

    public boolean canUse() {
        LivingEntity target = this.attacker.getTarget();
        if (target == null) {
            return false;
        } else return target.isAlive();
    }

    public boolean canContinueToUse() {
        LivingEntity target = this.attacker.getTarget();
        return target != null && target.isAlive();
    }

    public void stop() {
        this.hoverTimer = 0;
        this.attacker.setBreathing(false);
        this.attacker.getNavigation().stop();
    }

    public void tick() {
        LivingEntity target = this.attacker.getTarget();
        if (target == null) {
            this.attacker.getNavigation().stop();
            return;
        }
        float tracking = 1.0F;
        this.attacker.lookAt(target, tracking, tracking);
        this.attacker.getLookControl().setLookAt(target);
        if (target.distanceToSqr(this.hoverPosX, this.hoverPosY, this.hoverPosZ) > 20.0) {
            final Vec3 dir = this.attacker.getEyePosition().subtract(target.getEyePosition()).normalize();
            final Vec3 targetPointRaw = target.getEyePosition().add(dir.scale(10.0));
            this.hoverPosX = targetPointRaw.x;
            this.hoverPosY = target.getEyeY() + 6.0;
            this.hoverPosZ = targetPointRaw.z;
        }


        ++this.hoverTimer;
        if (this.hoverTimer >= this.maxHoverTime) {
            this.attacker.setBreathing(true);
            this.doRayAttack();
        }

        double offsetX = this.hoverPosX - this.attacker.getX();
        double offsetY = this.hoverPosY - this.attacker.getY();
        double offsetZ = this.hoverPosZ - this.attacker.getZ();
        double distanceDesired = Math.sqrt(offsetX * offsetX + offsetY * offsetY + offsetZ * offsetZ);
        if (distanceDesired > 3.0) {
            this.attacker.getNavigation().moveTo(this.hoverPosX, this.hoverPosY, this.hoverPosZ, 1.0);
        } else {
            this.attacker.getNavigation().stop();
        }

    }

    private void doRayAttack() {
        double range = 20.0D;
        double offset = 10.0D;
        Vec3 srcVec = new Vec3(this.attacker.getX(), this.attacker.getY() + 0.25, this.attacker.getZ());
        Vec3 lookVec = this.attacker.getViewVector(1.0F);
        Vec3 destVec = srcVec.add(lookVec.x() * range, lookVec.y() * range, lookVec.z() * range);
        List<Entity> possibleList = this.attacker.level().getEntities(this.attacker, this.attacker.getBoundingBox().move(lookVec.x() * offset, lookVec.y() * offset, lookVec.z() * offset).inflate(range, range, range));
        double hitDist = 0;

        for (Entity possibleEntity : possibleList) {
            if (possibleEntity.isPickable() && possibleEntity != this.attacker && possibleEntity != this.attacker.getOwner()) {
                float borderSize = possibleEntity.getPickRadius();
                AABB collisionBB = possibleEntity.getBoundingBox().inflate(borderSize, borderSize, borderSize);
                Optional<Vec3> interceptPos = collisionBB.clip(srcVec, destVec);

                if (collisionBB.contains(srcVec)) {
                    if (0.0D < hitDist || hitDist == 0.0D) {
                        this.attacker.doBreathAttack(possibleEntity);
                        hitDist = 0.0D;
                    }
                } else if (interceptPos.isPresent()) {
                    double possibleDist = srcVec.distanceTo(interceptPos.get());

                    if (possibleDist < hitDist || hitDist == 0.0D) {
                        this.attacker.doBreathAttack(possibleEntity);
                        hitDist = possibleDist;
                    }
                }
            }
        }
    }
}
