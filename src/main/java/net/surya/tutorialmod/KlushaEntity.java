package net.surya.tutorialmod;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class KlushaEntity extends ThrowableItemProjectile {

    private double distanceTravelled = 0.0;
    private boolean returning = false;
    private boolean itemReturned = false;
    private int returnDelay = 0;

    public KlushaEntity(
            EntityType<? extends KlushaEntity> type,
            Level level) {
        super(type, level);
    }

    public KlushaEntity(
            Level level,
            LivingEntity owner,
            ItemStack stack) {
        super(ModEntities.KLUSHA.get(), owner, level);
        setItem(stack.copyWithCount(1));
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.KLUSHA.get();
    }

    @Override
    protected float getGravity() {
        return 0.0F;
    }

    @Override
    public boolean hurt(
            net.minecraft.world.damagesource.DamageSource source,
            float amount) {
        return false;
    }

    @Override
    public void tick() {
        super.tick();

        // Вращение модели во время полёта
        if (!returning && !isRemoved()) {
            setYRot(getYRot() + 25.0F);
            setXRot(getXRot() + 25.0F);
        }

        if (level().isClientSide) {
            return;
        }

        Entity owner = getOwner();

        if (returning) {
            if (returnDelay > 0) {
                returnDelay--;
                setDeltaMovement(Vec3.ZERO);
                return;
            }

            if (!(owner instanceof Player player) || !player.isAlive()) {
                discard();
                return;
            }

            Vec3 difference =
                    player.getEyePosition().subtract(position());

            if (difference.lengthSqr() < 2.25) {
                if (!itemReturned) {
                    ItemStack stack = getItem().copyWithCount(1);
                    itemReturned = true;

                    if (!player.getInventory().add(stack)) {
                        player.drop(stack, false);
                    }
                }

                discard();
                return;
            }

            setDeltaMovement(difference.normalize().scale(1.5));
            hasImpulse = true;
            return;
        }

        distanceTravelled += getDeltaMovement().length();

        if (distanceTravelled >= 15.0) {
            returning = true;
            returnDelay = 10;
            setDeltaMovement(Vec3.ZERO);
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        if (level().isClientSide || returning) {
            return;
        }

        Entity target = result.getEntity();
        Entity owner = getOwner();

        if (target instanceof LivingEntity livingTarget
                && owner instanceof LivingEntity attacker
                && target != owner) {
            livingTarget.hurt(
                    damageSources().thrown(this, attacker),
                    12.0F
            );
        }

        returning = true;
        returnDelay = 10;
        setDeltaMovement(Vec3.ZERO);
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        if (!level().isClientSide && !returning) {
            returning = true;
            returnDelay = 10;
            setDeltaMovement(Vec3.ZERO);
        }
    }
}



