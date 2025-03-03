package cn.noryea.nocompassreequip.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.minecraft.world.item.CompassItem.isLodestoneCompass;

@Environment(EnvType.CLIENT)
@Mixin(ItemInHandRenderer.class)
public abstract class MixinItemInHandRenderer {

    @Shadow
    private float mainHandHeight;
    @Shadow
    private ItemStack mainHandItem;
    @Shadow
    private ItemStack offHandItem;
    @Final
    @Shadow
    private Minecraft minecraft;

    @Unique
    private static int oldSlot = 0;

    @Inject(method = "tick", at = @At("HEAD"))
    private void disableLodeStoneTrackerReequip(CallbackInfo ci) {
        if (this.minecraft.player == null) return;
        LocalPlayer localplayer = this.minecraft.player;

        ItemStack mainHandHeld = localplayer.getMainHandItem();
        ItemStack offHandHeld = localplayer.getOffhandItem();

        if (isLodestoneCompass(mainHandItem) && isLodestoneCompass(mainHandHeld)) {
            int slot = localplayer.getInventory().selected;
            if (slot != oldSlot) {
                if (mainHandHeight < 0.1F)
                    oldSlot = slot;
            } else {
                mainHandItem = mainHandHeld;
            }
        }

        if (isLodestoneCompass(offHandItem) && isLodestoneCompass(offHandHeld)) {
            offHandItem = offHandHeld;
        }
    }
}
