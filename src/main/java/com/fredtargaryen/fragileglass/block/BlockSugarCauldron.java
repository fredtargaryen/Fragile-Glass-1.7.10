package com.fredtargaryen.fragileglass.block;

import com.fredtargaryen.fragileglass.DataReference;
import com.fredtargaryen.fragileglass.FragileGlassBase;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import java.util.Random;

/**
 * Sugar Cauldron Metadata Guide:
 * 0 - Empty (nothing inside)
 * 1 - Has water (water inside)
 * 2 - Has sugar water (lighter coloured water)
 * 3 - Boiling 1 (Same, with about 3 bubbles)
 * 4 - Boiling 2 (Same, with about 6 bubbles)
 * 5 - Boiled (Glass on top)
 */
public class BlockSugarCauldron extends Block
{
    private int rID;
    private Random r = new Random();
    private IIcon sugarWater;
    private IIcon topIcon;

    public BlockSugarCauldron(int rid)
    {
        super(Material.iron);
        this.rID = rid;
        this.setCreativeTab(CreativeTabs.tabTools);
    }

    @Override
    public int getRenderType(){return this.rID;}

    @Override
    public boolean onBlockActivated(World w, int x, int y, int z, EntityPlayer player, int metaIThink, float dunno, float alsoDunno, float notAClue)
    {
        switch(metaIThink)
        {
            case 0:
                if(player.inventory.getCurrentItem().getItem() == Items.water_bucket)
                {
                    player.inventory.setItemStack(new ItemStack(Items.bucket));
                    w.setBlockMetadataWithNotify(x, y, z, 1, 3);
                    return true;
                }
                return false;
            case 1:
                Item i = player.inventory.getCurrentItem().getItem();
                if(i == Items.bucket)
                {
                    player.inventory.setItemStack(new ItemStack(Items.water_bucket));
                    w.setBlockMetadataWithNotify(x, y, z, 0, 3);
                    return true;
                }
                else if(i == Item.getItemFromBlock(FragileGlassBase.sugarBlock))
                {
                    --player.inventory.getCurrentItem().stackSize;
                    w.setBlockMetadataWithNotify(x, y, z, 2, 3);
                    return true;
                }
                return false;
            case 5:
                float f = r.nextFloat() * 0.8F + 0.1F;
                float f1 = r.nextFloat() * 0.8F + 0.1F;
                float f2 = r.nextFloat() * 0.8F + 0.1F;
                EntityItem entityitem = new EntityItem(w, (double)((float)x + f), (double)((float)y + f1), (double)((float)z + f2), new ItemStack(FragileGlassBase.fragileGlass, 16, 0));
                float f3 = 0.05F;
                entityitem.motionX = (double)((float)r.nextGaussian() * f3);
                entityitem.motionY = (double)((float)r.nextGaussian() * f3 + 0.2F);
                entityitem.motionZ = (double)((float)r.nextGaussian() * f3);
                w.setBlockMetadataWithNotify(x, y, z, 0, 3);
                return true;
            default:
                return false;
        }
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister i)
    {
        String mainIconString = DataReference.MODID+":"+this.getUnlocalizedName().substring(5);
        this.blockIcon = i.registerIcon(mainIconString+"_main");
        this.topIcon = i.registerIcon(mainIconString+"_top");
        this.sugarWater = i.registerIcon(DataReference.MODID+":sugarWater");
    }

    public IIcon getSugarWater(){return this.sugarWater;}

    public IIcon getTopIcon(){return this.topIcon;}

    /**
     * Returns which pass should this block be rendered on. 0 for solids and 1 for alpha
     */
    @Override
    @SideOnly(Side.CLIENT)
    public int getRenderBlockPass()
    {
        return 0;
    }

    @Override
    public void onBlockAdded(World w, int x, int y, int z)
    {
        w.setBlockMetadataWithNotify(x, y, z, 0, 3);
    }

    /**
     * Sets the block's bounds for rendering it as an item
     */
    public void setBlockBoundsForItemRender()
    {
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }
}
