package net.surya.tutorialmod;

import java.util.function.Consumer;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.Level;

import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

@Mod.EventBusSubscriber(
        modid = TutorialMod.MOD_ID,
        bus = Mod.EventBusSubscriber.Bus.FORGE
)
public class KlushaItem extends SwordItem implements GeoItem {

    private static final String COMBO_KEY = "KlushaCombo";

    private static final RawAnimation ATTACK_ANIM =
            RawAnimation.begin().thenPlay("attack");

    private static final RawAnimation ATTACK1A_ANIM =
            RawAnimation.begin().thenPlay("attack1a");

    private final AnimatableInstanceCache cache =
            GeckoLibUtil.createInstanceCache(this);

    public KlushaItem(Properties properties) {
        super(Tiers.WOOD, 14, -2.4F, properties);
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    @Override
    public void registerControllers(
            AnimatableManager.ControllerRegistrar controllers) {

        controllers.add(
                new AnimationController<KlushaItem>(
                        this,
                        "controller",
                        0,
                        state -> PlayState.STOP
                )
                .triggerableAnim("attack", ATTACK_ANIM)
                .triggerableAnim("attack1a", ATTACK1A_ANIM)
        );
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public void initializeClient(
            Consumer<IClientItemExtensions> consumer) {

        consumer.accept(new IClientItemExtensions() {
            private KlushaItemRenderer renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (renderer == null) {
                    renderer = new KlushaItemRenderer();
                }
                return renderer;
            }
        });
    }

    // Отключаем стандартный взмах предмета.
    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
        return true;
    }

    // Правый клик: бросок Клуши — оставляем прежнюю механику.
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

            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }

            player.getCooldowns().addCooldown(this, 60);
        }

        return InteractionResultHolder.sidedSuccess(
                stack,
                level.isClientSide
        );
    }

    // ЛКМ по существу.
    @SubscribeEvent
    public static void onPlayerAttack(AttackEntityEvent event) {
        playAttackAnimation(event.getEntity());
    }

    // ЛКМ по воздуху: отправляем запрос серверу.
    @SubscribeEvent
    public static void onLeftClickEmpty(
            PlayerInteractEvent.LeftClickEmpty event) {

        Player player = event.getEntity();
        ItemStack stack = player.getMainHandItem();

        if (stack.getItem() instanceof KlushaItem) {
            KlushaNetwork.sendAirAttack();
        }
    }

    // Общая логика чередования анимаций.
    public static void playAttackAnimation(Player player) {
        if (!(player.level() instanceof ServerLevel serverLevel)) {
            return;
        }

        ItemStack stack = player.getMainHandItem();

        if (!(stack.getItem() instanceof KlushaItem item)) {
            return;
        }

        int combo = player.getPersistentData().getInt(COMBO_KEY);

        String animation = (combo % 2 == 0)
                ? "attack"
                : "attack1a";

        player.getPersistentData().putInt(COMBO_KEY, combo + 1);

        item.triggerAnim(
                player,
                GeoItem.getOrAssignId(stack, serverLevel),
                "controller",
                animation
        );
    }
}



