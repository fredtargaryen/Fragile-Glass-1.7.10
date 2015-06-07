package com.fredtargaryen.fragileglass.block;

import com.fredtargaryen.fragileglass.FragileGlassBase;
import com.fredtargaryen.fragileglass.tileentity.TileEntityFragile;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockIce;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

public class BlockThinIce extends BlockIce implements ITileEntityProvider
{
    private int renderID;

    public BlockThinIce(int renderID)
    {
        super();
        this.minY = 0.9375;
        this.renderID = renderID;
    }
    public TileEntity createNewTileEntity(World w, int i)
    {
        return new TileEntityFragile();
    }

    /**
     * The type of render function that is called for this block
     */
    public int getRenderType()
    {
        return this.renderID;
    }

    /**
     * Ticks the block if it's been scheduled
     */
    public void updateTick(World p_149674_1_, int p_149674_2_, int p_149674_3_, int p_149674_4_, Random p_149674_5_)
    {
        if (p_149674_1_.getSavedLightValue(EnumSkyBlock.Block, p_149674_2_, p_149674_3_, p_149674_4_) > 11 - this.getLightOpacity())
        {
            if (p_149674_1_.provider.isHellWorld)
            {
                p_149674_1_.setBlockToAir(p_149674_2_, p_149674_3_, p_149674_4_);
                return;
            }
        }
    }

    /**
     * Returns the mobility information of the block, 0 = free, 1 = can't push but can move over, 2 = total immobility
     * and stop pistons
     */
    public int getMobilityFlag()
    {
        return 1;
    }

    /**
     * Returns true if the given side of this block type should be rendered, if the adjacent block is at the given
     * coordinates.
     */
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side)
    {
        if(side == 1)
        {
            return true;
        }
        else if(side == 0)
        {
            return !(world.getBlock(x, y, z) == Blocks.ice);
        }
        else
        {
            return !(world.getBlock(x, y, z) == Blocks.ice || world.getBlock(x, y, z) == FragileGlassBase.thinIce);
        }
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister p_149651_1_)
    {
        this.blockIcon = p_149651_1_.registerIcon("ice");
    }
}
