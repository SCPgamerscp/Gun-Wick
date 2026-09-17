package com.scpgamerscp.gunwick.client;

import com.scpgamerscp.gunwick.entity.WicksWolfEntity;
import net.minecraft.client.model.WolfModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class WicksWolfRenderer extends MobRenderer<WicksWolfEntity, WolfModel<WicksWolfEntity>> {
    private static final ResourceLocation WOLF_LOCATION = new ResourceLocation("textures/entity/wolf/wolf.png");
    private static final ResourceLocation WOLF_TAME_LOCATION = new ResourceLocation("textures/entity/wolf/wolf_tame.png");
    private static final ResourceLocation WOLF_ANGRY_LOCATION = new ResourceLocation("textures/entity/wolf/wolf_angry.png");

    public WicksWolfRenderer(EntityRendererProvider.Context context) {
        super(context, new WolfModel<>(context.bakeLayer(ModelLayers.WOLF)), 0.5F);
    }

    @Override
    protected float getBob(WicksWolfEntity wolf, float partialTicks) {
        return wolf.getTailAngle();
    }

    @Override
    public ResourceLocation getTextureLocation(WicksWolfEntity wolf) {
        if (wolf.isTame()) {
            return WOLF_TAME_LOCATION;
        }
        return wolf.isAngry() ? WOLF_ANGRY_LOCATION : WOLF_LOCATION;
    }
}
