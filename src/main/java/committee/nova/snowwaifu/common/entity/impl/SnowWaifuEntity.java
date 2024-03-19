package committee.nova.snowwaifu.common.entity.impl;

import committee.nova.snowwaifu.common.config.SWConfig;
import committee.nova.snowwaifu.common.entity.ai.FollowOwnerGoal;
import committee.nova.snowwaifu.common.entity.ai.HoverBeamGoal;
import committee.nova.snowwaifu.common.entity.ai.OwnerHurtByTargetGoal;
import committee.nova.snowwaifu.common.entity.ai.OwnerHurtTargetGoal;
import committee.nova.snowwaifu.common.entity.api.TamableMob;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.items.ItemHandlerHelper;
import twilightforest.entity.IBreathAttacker;
import twilightforest.init.TFItems;
import twilightforest.init.TFParticleType;
import twilightforest.init.TFSounds;
import twilightforest.util.EntityUtil;

public class SnowWaifuEntity extends TamableMob implements IBreathAttacker {
    private static final EntityDataAccessor<Boolean> BEAM_FLAG;

    public SnowWaifuEntity(EntityType<? extends SnowWaifuEntity> type, Level world) {
        super(type, world);
        this.moveControl = new FlyingMoveControl(this, 360, true);
        this.xpReward = 0;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new HoverBeamGoal(this, 20));
        this.goalSelector.addGoal(5, new MeleeAttackGoal(this, 1.0, true));
        this.goalSelector.addGoal(6, new FollowOwnerGoal(this, 2.0D, 10.0F, 2.0F, true));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Mob.class, true,
                l -> l instanceof Enemy
        ));
    }

    @Override
    protected int calculateFallDamage(float p_21237_, float p_21238_) {
        return super.calculateFallDamage(p_21237_, p_21238_);
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    public static AttributeSupplier.Builder registerAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.23).add(Attributes.FLYING_SPEED, 0.23).add(Attributes.ATTACK_DAMAGE, 7.0).add(Attributes.FOLLOW_RANGE, 40.0).add(Attributes.MAX_HEALTH, SWConfig.snowWaifuMaxHealth.get()).add(Attributes.KNOCKBACK_RESISTANCE, 0.75);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.getEntityData().define(BEAM_FLAG, false);
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        FlyingPathNavigation flyingpathnavigation = new FlyingPathNavigation(this, level);
        flyingpathnavigation.setCanOpenDoors(true);
        flyingpathnavigation.setCanFloat(true);
        flyingpathnavigation.setCanPassDoors(true);
        return flyingpathnavigation;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return TFSounds.SNOW_QUEEN_AMBIENT.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return TFSounds.SNOW_QUEEN_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return TFSounds.SNOW_QUEEN_DEATH.get();
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.onGround() && this.getDeltaMovement().y < 0.0D) {
            this.setDeltaMovement(this.getDeltaMovement().multiply(1.0D, 0.6D, 1.0D));
        }
        if (this.level().isClientSide()) this.spawnParticles();
    }

    private void spawnParticles() {
        for (int i = 0; i < 3; ++i) {
            float px = (this.getRandom().nextFloat() - this.getRandom().nextFloat()) * 0.3F;
            float py = this.getEyeHeight() + (this.getRandom().nextFloat() - this.getRandom().nextFloat()) * 0.5F;
            float pz = (this.getRandom().nextFloat() - this.getRandom().nextFloat()) * 0.3F;
            this.level().addParticle(TFParticleType.SNOW_GUARDIAN.get(), this.xOld + (double) px, this.yOld + (double) py, this.zOld + (double) pz, 0.0, 0.0, 0.0);
        }

        if (this.isBreathing() && this.isAlive()) {
            Vec3 look = this.getViewVector(1.0F);
            double dist = 0.5;
            double px = this.getX() + look.x() * dist;
            double py = this.getY() + 1.7000000476837158 + look.y() * dist;
            double pz = this.getZ() + look.z() * dist;

            for (int i = 0; i < 10; ++i) {
                double dx = look.x();
                double dy = look.y();
                double dz = look.z();
                double spread = 2.0 + this.getRandom().nextDouble() * 2.5;
                double velocity = 2.0 + this.getRandom().nextDouble() * 0.15;
                dx += this.getRandom().nextGaussian() * 0.0075 * spread;
                dy += this.getRandom().nextGaussian() * 0.0075 * spread;
                dz += this.getRandom().nextGaussian() * 0.0075 * spread;
                dx *= velocity;
                dy *= velocity;
                dz *= velocity;
                this.level().addParticle(TFParticleType.ICE_BEAM.get(), px, py, pz, dx, dy, dz);
            }
        }

    }

    @Override
    public void tick() {
        super.tick();
        if (this.deathTime > 0) {
            for (int i = 0; i < 5; ++i) {
                double d = this.getRandom().nextGaussian() * 0.02;
                double d1 = this.getRandom().nextGaussian() * 0.02;
                double d2 = this.getRandom().nextGaussian() * 0.02;
                this.level().addParticle(this.getRandom().nextBoolean() ? ParticleTypes.EXPLOSION : ParticleTypes.POOF, this.getX() + (double) (this.getRandom().nextFloat() * this.getBbWidth() * 2.0F) - (double) this.getBbWidth(), this.getY() + (double) (this.getRandom().nextFloat() * this.getBbHeight()), this.getZ() + (double) (this.getRandom().nextFloat() * this.getBbWidth() * 2.0F) - (double) this.getBbWidth(), d, d1, d2);
            }
        }
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (player.getItemInHand(hand).is(Items.FLINT_AND_STEEL) && player == getOwner() && player.isShiftKeyDown()) {
            this.playSound(SoundEvents.ENCHANTMENT_TABLE_USE, 1.0F, 1.0F);
            this.discard();
            ItemHandlerHelper.giveItemToPlayer(player, TFItems.SNOW_QUEEN_TROPHY.get().getDefaultInstance());
            return InteractionResult.CONSUME;
        }
        return super.mobInteract(player, hand);
    }

    @Override
    public int getMaxHeadXRot() {
        return 85;
    }

    @Override
    public int getHeadRotSpeed() {
        return 360;
    }

    @Override
    public boolean removeWhenFarAway(double distance) {
        return false;
    }

    @Override
    public boolean wantsToAttack(LivingEntity target, LivingEntity owner) {
        if (target == owner) return false;
        return !(target instanceof SnowWaifuEntity);
    }

    @Override
    public void die(DamageSource cause) {
        super.die(cause);
        final ItemEntity trophy = new ItemEntity(level(), getX(), getEyeY() + 1.0, getZ(), TFItems.SNOW_QUEEN_TROPHY.get().getDefaultInstance());
        level().addFreshEntity(trophy);
    }

    @Override
    public boolean doHurtTarget(Entity entity) {
        return entity.hurt(level().damageSources().mobAttack(this), (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE));
    }

    @Override
    public void lavaHurt() {
        if (!this.fireImmune()) {
            this.setSecondsOnFire(5);
            if (this.hurt(this.damageSources().lava(), 4.0F)) {
                this.playSound(SoundEvents.GENERIC_BURN, 0.4F, 2.0F + this.random.nextFloat() * 0.4F);
                EntityUtil.killLavaAround(this);
            }
        }
    }

    public boolean isBreathing() {
        return this.getEntityData().get(BEAM_FLAG);
    }

    public void setBreathing(boolean flag) {
        this.getEntityData().set(BEAM_FLAG, flag);
    }

    public void doBreathAttack(Entity target) {
        final float dmg = SWConfig.snowWaifuBreathAttackDamage.get().floatValue();
        if (target.hurt(level().damageSources().mobAttack(this), dmg)) heal(dmg / 2);
    }

    @Override
    protected boolean canRide(Entity entity) {
        return false;
    }

    @Override
    public boolean isPushedByFluid(FluidType type) {
        return false;
    }

    @Override
    protected float getWaterSlowDown() {
        return 1.0F;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putDouble("health_percent", this.getHealth() / this.getMaxHealth());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("health_percent", 6)) {
            final double percent = Mth.clamp(tag.getDouble("health_percent"), .01, 1.0);
            this.setHealth((float) (percent * SWConfig.snowWaifuMaxHealth.get()));
        }
    }

    static {
        BEAM_FLAG = SynchedEntityData.defineId(SnowWaifuEntity.class, EntityDataSerializers.BOOLEAN);
    }
}
