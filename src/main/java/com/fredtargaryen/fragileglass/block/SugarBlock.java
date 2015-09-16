package com.fredtargaryen.fragileglass.block;

import com.fredtargaryen.fragileglass.DataReference;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockFalling;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;

public class SugarBlock extends BlockFalling
{
	public SugarBlock()
	{
		super();
		this.setCreativeTab(CreativeTabs.tabMisc);
	}
	
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister i)
	{
		this.blockIcon = i.registerIcon(DataReference.MODID+":"+this.getUnlocalizedName().substring(5));
	}
}
