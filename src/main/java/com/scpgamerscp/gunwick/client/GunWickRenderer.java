package com.scpgamerscp.gunwick.client;

import com.scpgamerscp.gunwick.GunWickMod;
import com.scpgamerscp.gunwick.entity.GunWickEntity;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;

public class GunWickRenderer extends HumanoidMobRenderer<GunWickEntity, PlayerModel<GunWickEntity>> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(GunWickMod.MODID, "textures/entity/gun_wick.png");

    public GunWickRenderer(EntityRendererProvider.Context context) {
        super(context, new PlayerModel<>(context.bakeLayer(ModelLayers.PLAYER), false), 0.5F);
        this.addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()));
        PlayerModel<GunWickEntity> model = this.getModel();
        model.hat.visible = true;
        model.jacket.visible = true;
        model.leftSleeve.visible = true;
        model.rightSleeve.visible = true;
        model.leftPants.visible = true;
        model.rightPants.visible = true;
    }

    @Override
    public ResourceLocation getTextureLocation(GunWickEntity entity) {
        return TEXTURE;
    }
}
