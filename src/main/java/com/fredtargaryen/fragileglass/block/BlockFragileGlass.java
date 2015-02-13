package com.fredtargaryen.fragileglass.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.world.IBlockAccess;

import java.util.Random;

public class BlockFragileGlass extends AnyFragileGlassBlock
{
	public BlockFragileGlass()
	{
		super();
		this.setCreativeTab(CreativeTabs.tabBlock);
	}

    @Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister i)
	{
        this.blockIcon = i.registerIcon("minecraft:glass");
	}
    
	@SideOnly(Side.CLIENT)
    @Override
    public int getRenderBlockPass()
    {
        return 0;
    }
	
	public int quantityDropped(Random par1Random)
    {
        return 0;
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean shouldSideBeRendered(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5)
    {
        return true;
    }
}