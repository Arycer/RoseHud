package me.arycer.rosehud.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(InGameHud.class)
public class MixinStatusEffectHud {
    @ModifyVariable(method = "renderStatusEffectOverlay", at = @At("STORE"), ordinal = 3)
    private int moveDown(int i) {
        return MinecraftClient.getInstance().textRenderer.fontHeight + 13;
    }

    @ModifyVariable(method = "renderStatusEffectOverlay", at = @At("STORE"), ordinal = 2)
    private int moveLeft(int i) {
        return i - 1;
    }
}
