package net.surya.tutorialmod;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.Level;

public class KlushaItem extends SwordItem {

    public KlushaItem(Properties properties) {
        super(Tiers.WOOD, 14, -2.4F, properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(
            Level level,
            Player player,
            InteractionHand hand) {

        ItemStack stack = player.getItemInHand(hand);

        if (player.getCooldowns().isOnCooldown(this)) {
            return InteractionResultHolder.fail(stack);
        }

        if (!level.isClientSide) {
            KlushaEntity klusha = new KlushaEntity(
                    level,
                    player,
                    stack.copyWithCount(1)
            );

            klusha.shootFromRotation(
                    player,
                    player.getXRot(),
                    player.getYRot(),
                    0.0F,
                    1.6F,
                    0.0F
            );

            level.addFreshEntity(klusha);
            stack.shrink(1);
            player.getCooldowns().addCooldown(this, 60);
        }

        return InteractionResultHolder.sidedSuccess(
                stack,
                level.isClientSide
        );
    }
}



