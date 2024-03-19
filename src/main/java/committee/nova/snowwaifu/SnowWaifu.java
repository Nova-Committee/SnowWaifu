package committee.nova.snowwaifu;

import committee.nova.snowwaifu.common.config.SWConfig;
import committee.nova.snowwaifu.common.entity.init.SWEntities;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(SnowWaifu.MODID)
public class SnowWaifu {
    public static final String MODID = "snowwaifu";

    public SnowWaifu() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, SWConfig.CFG);
        final IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        SWEntities.ENTITIES.register(bus);
    }
}
