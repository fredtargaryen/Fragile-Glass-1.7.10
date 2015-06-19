package com.fredtargaryen.fragileglass.worldgen;

import com.fredtargaryen.fragileglass.FragileGlassBase;
import cpw.mods.fml.common.IWorldGenerator;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.IChunkProvider;

import java.util.Random;

public class PatchGen implements IWorldGenerator
{
    private float chance;

    public PatchGen () {
        this.chance = (float)(FragileGlassBase.genChance - 1);
    }

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
        BiomeGenBase b = world.getBiomeGenForCoords(chunkX, chunkZ);
        if (b.getEnableSnow())
        {
            if ((int) (random.nextFloat() * this.chance) == 1)
            {
                //Coords of middle of patch
                int midX = (chunkX * 16) + random.nextInt(16);
                int midZ = (chunkZ * 16) + random.nextInt(16);
                //Usually the water level...
                int y = 62;
                int patchRad = (int)(((2*random.nextGaussian()) + FragileGlassBase.avePatchSize)/2);
                int x;
                int z;
                for(int rad = patchRad; rad > 0; rad--)
                {
                    for(double t = 0; t < 360; t += 10)
                    {
                        x = (int)(midX + (rad * Math.cos(t)));
                        z = (int)(midZ + (rad * Math.sin(t)));
                        if(world.getBlock(x, y, z) == Blocks.ice)
                        {
                            //Adds a little randomness to the outside of patches, to avoid perfect circles all the time
                            if(rad > patchRad - 2) {
                                if (random.nextBoolean()) {
                                    world.setBlock(x, y, z, FragileGlassBase.thinIce);
                                }
                            }
                            else
                            {
                                world.setBlock(x, y, z, FragileGlassBase.thinIce);
                            }
                        }
                    }
                }
            }
        }
    }
}