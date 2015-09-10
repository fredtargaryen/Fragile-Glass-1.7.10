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
import net.minecraft.init.Blocks;
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
 * 3 - Boiling 1 (Same, very rare bubbles)
 * 4 - Boiling 2 (Same, with about 3 bubbles)
 * 5 - Boiling 3 (Same, with about 6 bubbles)
 * 6 - Boiled (Glass on top)
 */
public class BlockSugarCauldron extends Block
{
    private int rID;
    private IIcon sugarWater;
    private IIcon topIcon;
    private static final int thirdOfCookTime = 80;

    public BlockSugarCauldron(int rid)
    {
        super(Material.iron);
        this.rID = rid;
        this.setCreativeTab(CreativeTabs.tabTools);
    }

    @Override
    public int getRenderType(){return this.rID;}

    @Override
    public boolean onBlockActivated(World w, int x, int y, int z, EntityPlayer player, int haventTheFoggiest, float dunno, float alsoDunno, float notAClue)
    {
        if (w.isRemote)
        {
            return true;
        }
        else
        {
            int i1 = w.getBlockMetadata(x, y, z);
            ItemStack itemstack = player.inventory.getCurrentItem();
            if(i1 == 0)
            {
                if (itemstack == null)
                {
                    return true;
                }
                else
                {
                    if (itemstack.getItem() == Items.water_bucket)
                    {
                        if (!player.capabilities.isCreativeMode)
                        {
                            player.inventory.setInventorySlotContents(player.inventory.currentItem, new ItemStack(Items.bucket));
                        }
                        w.setBlockMetadataWithNotify(x, y, z, 1, 3);
                    }
                    return true;
                }
            }
            else if(i1 == 1)
            {
                if (itemstack == null)
                {
                    return true;
                }
                else
                {
                    Item i = itemstack.getItem();
                    if(i == Item.getItemFromBlock(FragileGlassBase.sugarBlock))
                    {
                        if (!player.capabilities.isCreativeMode)
                        {
                            ItemStack newStack = ItemStack.copyItemStack(itemstack);
                            --newStack.stackSize;
                            player.inventory.setInventorySlotContents(player.inventory.currentItem, newStack);
                        }
                        w.setBlockMetadataWithNotify(x, y, z, 2, 3);
                    }
                    return true;
                }
            }
            else if(i1 == 6)
            {
                ItemStack glassStack = new ItemStack(FragileGlassBase.fragileGlass, 16);
                if (!player.inventory.addItemStackToInventory(glassStack))
                {
                    w.spawnEntityInWorld(new EntityItem(w, (double)x + 0.5D, (double)y + 1.0D, (double)z + 0.5D, glassStack));
                }
                w.setBlockMetadataWithNotify(x, y, z, 0, 3);
                return true;
            }
        }
        return false;
    }

    @Override
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
        w.scheduleBlockUpdate(x, y, z, this, 50);
    }

    @Override
    public void updateTick(World w, int x, int y, int z, Random r)
    {
        int m = w.getBlockMetadata(x, y, z);
        if(m < 2 || m == 6)
        {
            w.scheduleBlockUpdate(x, y, z, this, 50);
        }
        else if(m == 2)
        {
            if(w.getBlock(x, y - 1, z) == Blocks.lit_furnace)
            {
                w.setBlockMetadataWithNotify(x, y, z, 3, 3);
                w.scheduleBlockUpdate(x, y, z, this, thirdOfCookTime);
            }
            else
            {
                w.scheduleBlockUpdate(x, y, z, this, 10);
            }
        }
        else if(m > 6)
        {
            w.setBlockMetadataWithNotify(x, y, z, 0, 3);
            w.scheduleBlockUpdate(x, y, z, this, 50);
        }
        else
        {
            if(w.getBlock(x, y - 1, z) == Blocks.lit_furnace)
            {
                ++m;
                w.setBlockMetadataWithNotify(x, y, z, m, 3);
                w.scheduleBlockUpdate(x, y, z, this, m == 6 ? 50 : thirdOfCookTime);
            }
            else
            {
                --m;
                w.setBlockMetadataWithNotify(x, y, z, m, 3);
                w.scheduleBlockUpdate(x, y, z, this, m == 2 ? 10 : thirdOfCookTime);
            }
        }
    }

    /**
     * Sets the block's bounds for rendering it as an item
     */
    @Override
    public void setBlockBoundsForItemRender()
    {
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }

    /**
     * Is this block (a) opaque and (b) a full 1m cube?  This determines whether or not to render the shared face of two
     * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
     */
    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    /**
     * A randomly called display update to be able to add particles or other items for display
     */
    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World w, int x, int y, int z, Random r)
    {
        int m = w.getBlockMetadata(x, y, z);
        if(m > 2 && m < 6)
        {
            w.spawnParticle("bubble", x + 0.125 + r.nextFloat() * 0.75, y + 1, z + 0.125 + r.nextFloat() * 0.75, 0.0D, 0.2D, 0.0D);
        }
    }

    @Override
    public void breakBlock(World w, int x, int y, int z, Block p_149749_5_, int p_149749_6_)
    {
        if(w.getBlockMetadata(x, y, z) == 6) {
            if (!w.isRemote) {
                w.spawnEntityInWorld(new EntityItem(w, (double) x + 0.5D, (double) y + 1.0D, (double) z + 0.5D, new ItemStack(FragileGlassBase.fragileGlass, 16)));
            }
        }
        super.breakBlock(w, x, y, z, p_149749_5_, p_149749_6_);
    }
}
