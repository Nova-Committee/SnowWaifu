package committee.nova.snowwaifu.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import committee.nova.snowwaifu.client.model.NewSnowWaifuModel;
import committee.nova.snowwaifu.common.entity.impl.SnowWaifuEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.resources.ResourceLocation;
import twilightforest.TwilightForestMod;

public class NewSnowWaifuRenderer extends HumanoidMobRenderer<SnowWaifuEntity, NewSnowWaifuModel> {

    private static final ResourceLocation textureLoc = TwilightForestMod.getModelTexture("snowqueen.png");

    public NewSnowWaifuRenderer(EntityRendererProvider.Context manager, NewSnowWaifuModel model) {
        super(manager, model, 0.625F);
    }

    @Override
    public ResourceLocation getTextureLocation(SnowWaifuEntity entity) {
        return textureLoc;
    }

    @Override
    protected void scale(SnowWaifuEntity queen, PoseStack stack, float partialTicks) {
        float scale = 1.2F;
        stack.scale(scale, scale, scale);
    }

    @Override
    public void render(SnowWaifuEntity queen, float yaw, float partialTicks, PoseStack stack, MultiBufferSource buffer, int light) {
        super.render(queen, yaw, partialTicks, stack, buffer, light);
    }
}
