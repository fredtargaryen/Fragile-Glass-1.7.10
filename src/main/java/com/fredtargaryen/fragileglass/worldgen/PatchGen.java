package com.fredtargaryen.fragileglass.worldgen;

import com.fredtargaryen.fragileglass.FragileGlassBase;
import cpw.mods.fml.common.IWorldGenerator;
import net.minecraft.block.Block;
import net.minecraft.block.BlockIce;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.IChunkProvider;

import java.util.Random;

public class PatchGen implements IWorldGenerator
{
    //If it goes a long time without genning a patch, forces a bonus patch to gen
    private int timeSinceLastPatch;
    private final int timeToWaitBeforeBonusPatch;

    public PatchGen () {
        this.timeSinceLastPatch = 0;
        this.timeToWaitBeforeBonusPatch = FragileGlassBase.genChance + 1;
    }

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider)
    {
        BiomeGenBase b = world.getBiomeGenForCoords(chunkX, chunkZ);
        if (b.getEnableSnow())
        {
            if(random.nextInt(FragileGlassBase.genChance) == 0 && this.genPatch(random, chunkX, chunkZ, world))
            {
                this.timeSinceLastPatch = 0;
            }
            else if(this.timeSinceLastPatch >= this.timeToWaitBeforeBonusPatch && this.genPatch(random, chunkX, chunkZ, world))
            {
                this.timeSinceLastPatch = 0;
            }
            else
            {
                this.timeSinceLastPatch++;
            }
        }
    }

    private boolean genPatch(Random random, int chunkX, int chunkZ, World world)
    {
        boolean madeThinIce = false;
        int attempts = 0;
        //Try 4 times to generate a patch
        while(!madeThinIce && attempts < 4) {
            //Coords of middle of patch (with midY)
            int midX = (chunkX * 16) + random.nextInt(16);
            int midZ = (chunkZ * 16) + random.nextInt(16);
            int midY = world.getTopSolidOrLiquidBlock(midX, midZ);

            int patchRad = (int) (((2 * random.nextGaussian()) + FragileGlassBase.avePatchSize) / 2);
            for (int rad = patchRad; rad > 0; rad--) {
                for (double t = 0; t < 360; t += 10)
                {
                    int x = (int) (midX + (rad * Math.cos(t)));
                    int z = (int) (midZ + (rad * Math.sin(t)));
                    Block nextBlock = world.getBlock(x, midY, z);
                    if(nextBlock instanceof BlockIce || FragileGlassBase.iceBlocks.contains(Item.getItemFromBlock(nextBlock))) {
                        //Adds a little randomness to the outside of patches, to avoid perfect circles all the time
                        if (rad > patchRad - 2) {
                            if (random.nextBoolean()) {
                                world.setBlock(x, midY, z, FragileGlassBase.thinIce);
                            }
                        } else {
                            world.setBlock(x, midY, z, FragileGlassBase.thinIce);
                        }
                        madeThinIce = true;
                    }
                }
            }
            if (!madeThinIce) {
                ++attempts;
            }
        }
        return madeThinIce;
    }
}
