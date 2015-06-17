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
    private boolean hasMentioned;

    public PatchGen () {
        this.chance = (float)(FragileGlassBase.genChance - 1);
        this.hasMentioned = false;
    }

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
        BiomeGenBase b = world.getBiomeGenForCoords(chunkX, chunkZ);
        if (b.getEnableSnow())
        {
            //if ((int) (random.nextFloat() * this.chance) == 1)
            //{
                int minX = chunkX * 16;
                int minZ = chunkZ * 16;
                int y = 0;
                while(!world.canBlockSeeTheSky(minX, y, minZ))
                {
                    y++;
                }
                int patchRad = (int)(((2*random.nextGaussian()) + FragileGlassBase.avePatchSize)/2);
                //Coords of middle of patch
                int midX = minX + (int)(random.nextFloat() * 16);
                int midZ = minZ + (int)(random.nextFloat() * 16);
                System.out.println("Genning a patch at "+midX+", "+midZ);
                int x;
                int z;
                for(int rad = patchRad; patchRad > 0; patchRad--)
                {
                    for(double t = 0; t > 360; t += 11.25)
                    {
                        x = (int)(midX + (rad * Math.cos(t)));
                        z = (int)(midZ + (rad * Math.sin(t)));
                        if(world.getBlock(x, y, z) == Blocks.ice)
                        {
                            if(!this.hasMentioned)
                            {
                                System.out.println("There's a block of ice at x: " + x + ", z: " + z + "!");
                                this.hasMentioned = true;
                            }
                            world.setBlock(x, y, z, FragileGlassBase.thinIce);
                        }
                    }
                }
            //}
        }
    }
}
