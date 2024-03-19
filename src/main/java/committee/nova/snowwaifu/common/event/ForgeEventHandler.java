package committee.nova.snowwaifu.common.event;

import committee.nova.snowwaifu.common.entity.impl.SnowWaifuEntity;
import committee.nova.snowwaifu.common.entity.init.SWEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CarvedPumpkinBlock;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.level.block.state.pattern.BlockPattern;
import net.minecraft.world.level.block.state.pattern.BlockPatternBuilder;
import net.minecraft.world.level.block.state.predicate.BlockStatePredicate;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import twilightforest.init.TFBlocks;

@Mod.EventBusSubscriber
public class ForgeEventHandler {
    private static BlockPattern snowwaifuBase;

    @SubscribeEvent
    public static void onBlockPlaced(BlockEvent.EntityPlaceEvent event) {
        if (!event.getPlacedBlock().is(TFBlocks.SNOW_QUEEN_TROPHY.get())) return;
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        final Level level = player.level();
        final BlockPos pos = event.getPos();
        final BlockPattern.BlockPatternMatch match = getSnowwaifuBase().find(level, pos);
        if (match == null) return;
        final SnowWaifuEntity waifu = SWEntities.SNOW_WAIFU.get().create(level);
        if (waifu == null) return;
        CarvedPumpkinBlock.clearPatternBlocks(level, match);
        waifu.setHealth(Math.max(1.0F, .2F * waifu.getMaxHealth()));
        waifu.moveTo(pos.getX() + .5, pos.getY() + .05, pos.getZ() + .5);
        waifu.tame(player);
        level.addFreshEntity(waifu);
        level.playSound(null, waifu, SoundEvents.EVOKER_CAST_SPELL, SoundSource.NEUTRAL, 1.0F, 1.0F);
        CarvedPumpkinBlock.updatePatternBlocks(level, match);
    }

    private static BlockPattern getSnowwaifuBase() {
        if (snowwaifuBase == null) {
            snowwaifuBase = BlockPatternBuilder
                    .start()
                    .aisle(" ", "#", "#")
                    .where('#', BlockInWorld.hasState(BlockStatePredicate.forBlock(Blocks.SNOW_BLOCK)))
                    .build();
        }
        return snowwaifuBase;
    }
}
