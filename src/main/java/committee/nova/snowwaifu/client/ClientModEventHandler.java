package committee.nova.snowwaifu.client;

import committee.nova.snowwaifu.SnowWaifu;
import committee.nova.snowwaifu.client.model.NewSnowWaifuModel;
import committee.nova.snowwaifu.client.model.SnowWaifuModel;
import committee.nova.snowwaifu.client.renderer.NewSnowWaifuRenderer;
import committee.nova.snowwaifu.client.renderer.SnowWaifuRenderer;
import committee.nova.snowwaifu.common.entity.init.SWEntities;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import twilightforest.client.JappaPackReloadListener;

import java.util.function.BooleanSupplier;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModEventHandler {
    public static final ModelLayerLocation NEW_SNOW_WAIFU_LAYER = new ModelLayerLocation(
            new ResourceLocation(SnowWaifu.MODID, "snow_waifu"),
            "main"
    );

    public static final ModelLayerLocation SNOW_WAIFU_LAYER = new ModelLayerLocation(
            new ResourceLocation(SnowWaifu.MODID, "new_snow_waifu"),
            "main"
    );

    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        final BooleanSupplier jappa = JappaPackReloadListener.INSTANCE.uncachedJappaPackCheck();
        event.registerEntityRenderer(
                SWEntities.SNOW_WAIFU.get(),
                !jappa.getAsBoolean() ?
                        c -> new SnowWaifuRenderer(c, new SnowWaifuModel(c.bakeLayer(SNOW_WAIFU_LAYER))) :
                        c -> new NewSnowWaifuRenderer(c, new NewSnowWaifuModel(c.bakeLayer(NEW_SNOW_WAIFU_LAYER)))
        );
    }

    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(SNOW_WAIFU_LAYER, SnowWaifuModel::create);
        event.registerLayerDefinition(NEW_SNOW_WAIFU_LAYER, NewSnowWaifuModel::create);
    }
}
