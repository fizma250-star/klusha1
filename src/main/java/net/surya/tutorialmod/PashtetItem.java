package net.surya.tutorialmod;

import java.util.function.Consumer;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;

import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

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
public class PashtetItem extends SwordItem implements GeoItem {

    private static final String COMBO_KEY = "PashtetCombo";

    private static final RawAnimation ATTACK1 =
            RawAnimation.begin().thenPlay("attack1");

    private static final RawAnimation ATTACK2 =
            RawAnimation.begin().thenPlay("attack2");

    private final AnimatableInstanceCache cache =
            GeckoLibUtil.createInstanceCache(this);

    public PashtetItem(Properties properties) {
        super(Tiers.NETHERITE, 20, -2.4F, properties);
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    @Override
    public void registerControllers(
            AnimatableManager.ControllerRegistrar controllers) {

        controllers.add(
                new AnimationController<PashtetItem>(
                        this,
                        "controller",
                        0,
                        state -> PlayState.STOP
                )
                .triggerableAnim("attack1", ATTACK1)
                .triggerableAnim("attack2", ATTACK2)
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
            private PashtetItemRenderer renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (renderer == null) {
                    renderer = new PashtetItemRenderer();
                }
                return renderer;
            }
        });
    }

    // Отключаем стандартный взмах.
    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
        return true;
    }

    // Удар по существу.
    @SubscribeEvent
    public static void onPlayerAttack(AttackEntityEvent event) {
        Player player = event.getEntity();

        if (player.level() instanceof ServerLevel) {
            playNextAttackAnimation(player);
        }
    }

    // Взмах по воздуху.
    @SubscribeEvent
    public static void onLeftClickEmpty(
            PlayerInteractEvent.LeftClickEmpty event) {

        Player player = event.getEntity();

        if (!(player.getMainHandItem().getItem()
                instanceof PashtetItem)) {
            return;
        }

        if (player.level().isClientSide) {
            PashtetNetwork.CHANNEL.sendToServer(
                    new PashtetAirAttackPacket()
            );
        }
    }

    // Чередование двух анимаций


на сервере.
    public static void playNextAttackAnimation(Player player) {
        if (!(player.level() instanceof ServerLevel serverLevel)) {
            return;
        }

        ItemStack stack = player.getMainHandItem();

        if (!(stack.getItem() instanceof PashtetItem item)) {
            return;
        }

        int combo = player.getPersistentData().getInt(COMBO_KEY);

        String animation = (combo % 2 == 0)
                ? "attack1"
                : "attack2";

        player.getPersistentData().putInt(COMBO_KEY, combo + 1);

        item.triggerAnim(
                player,
                GeoItem.getOrAssignId(stack, serverLevel),
                "controller",
                animation
        );
    }
}



