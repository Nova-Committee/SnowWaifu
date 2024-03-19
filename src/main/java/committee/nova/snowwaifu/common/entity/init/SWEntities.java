package committee.nova.snowwaifu.common.entity.init;

import committee.nova.snowwaifu.SnowWaifu;
import committee.nova.snowwaifu.common.entity.impl.SnowWaifuEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class SWEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, SnowWaifu.MODID);

    public static final RegistryObject<EntityType<SnowWaifuEntity>> SNOW_WAIFU;

    static {
        SNOW_WAIFU = ENTITIES.register("snow_waifu", () -> EntityType.Builder
                .of(SnowWaifuEntity::new, MobCategory.CREATURE)
                .sized(0.7F, 2.2F)
                .build("snowwaifu.snow_waifu")
        );
    }

    @SubscribeEvent
    public static void addEntityAttributes(EntityAttributeCreationEvent event) {
        event.put(SNOW_WAIFU.get(), SnowWaifuEntity.registerAttributes().build());
    }
}
