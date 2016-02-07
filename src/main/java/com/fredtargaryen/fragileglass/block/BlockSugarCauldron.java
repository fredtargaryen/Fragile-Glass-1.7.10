package com.fredtargaryen.fragileglass.block;

import com.fredtargaryen.fragileglass.DataReference;
import com.fredtargaryen.fragileglass.FragileGlassBase;
import com.fredtargaryen.fragileglass.client.particle.EntityMyBubbleFX;
import com.fredtargaryen.fragileglass.client.particle.EntityMySplashFX;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
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
    private final int rID;
    private IIcon wallIcon;
    private IIcon sugarWater;
    private IIcon topIcon;
    private static final int thirdOfCookTime = 100;

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
            if(w.getBlockMetadata(x, y, z) == 1)
            {
                this.splash(w, x, y, z);
            }
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
                        w.playSoundEffect((double) x, (double) y, (double) z, "random.splash", 1.0F, 1.0F);
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
                        w.playSoundEffect((double) x, (double) y, (double) z, "random.splash", 1.0F, 1.0F);
                        w.setBlockMetadataWithNotify(x, y, z, 2, 3);
                    }
                    return true;
                }
            }
            else if(i1 == 6)
            {
                w.spawnEntityInWorld(new EntityItem(w, (double)x + 0.5D, (double)y + 1.0D, (double)z + 0.5D, new ItemStack(FragileGlassBase.fragileGlass, 16)));
                w.spawnEntityInWorld(new EntityXPOrb(w, (double)x + 0.5D, (double)y + 1.0D, (double)z + 0.5D, 4));
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
        this.blockIcon = i.registerIcon(mainIconString);
        this.wallIcon = i.registerIcon(mainIconString+"_main");
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
        Block blockBelow = w.getBlock(x, y - 1, z);
        if(m < 2 || m == 6)
        {
            w.scheduleBlockUpdate(x, y, z, this, 50);
        }
        else if(m == 2)
        {
            if(blockBelow == Blocks.lit_furnace || blockBelow == Blocks.fire || blockBelow == Blocks.lava)
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
            if(blockBelow == Blocks.lit_furnace || blockBelow == Blocks.fire || blockBelow == Blocks.lava)
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
            boolean shouldBubble = true;
            if(m == 3)
            {
                shouldBubble = r.nextInt(4) == 0;
            }
            else if(m == 4)
            {
                shouldBubble = r.nextBoolean();
            }
            if(shouldBubble)
            {
                this.spawnEntityFX(new EntityMyBubbleFX(w, x + 0.125 + r.nextFloat() * 0.75, y + 1, z + 0.125 + r.nextFloat() * 0.75, 0.0D, 0.00D, 0.0D));
            }
        }
    }

    @Override
    public void onBlockDestroyedByPlayer(World w, int x, int y, int z, int m)
    {
        if(m == 6 && !w.isRemote)
        {
            EntityItem entityItem = new EntityItem(w, (double) x + 0.5D, (double) y + 1.0D, (double) z + 0.5D, new ItemStack(FragileGlassBase.fragileGlass, 16));
            entityItem.motionY = 0.3F;
            w.spawnEntityInWorld(entityItem);
            w.spawnEntityInWorld(new EntityXPOrb(w, x + 0.5, y + 0.5, z + 0.5, 4));
        }
        super.onBlockDestroyedByPlayer(w, x, y, z, m);
    }

    @SideOnly(Side.CLIENT)
    private void splash(World w, int x, int y, int z)
    {
        this.spawnEntityFX(new EntityMySplashFX(w, x + 0.5, y + 1.0, z + 0.5));
        this.spawnEntityFX(new EntityMySplashFX(w, x + 0.5, y + 1.0, z + 0.5));
        this.spawnEntityFX(new EntityMySplashFX(w, x + 0.5, y + 1.0, z + 0.5));
        this.spawnEntityFX(new EntityMySplashFX(w, x + 0.5, y + 1.0, z + 0.5));
        this.spawnEntityFX(new EntityMySplashFX(w, x + 0.5, y + 1.0, z + 0.5));
        this.spawnEntityFX(new EntityMySplashFX(w, x + 0.5, y + 1.0, z + 0.5));
    }

    /*
     * Makes particles not spawn if out of render range - thanks to LapisSea
     * For use instead of addEffect
     */
    @SideOnly(Side.CLIENT)
    private void spawnEntityFX(EntityFX particleFX)
    {
        if (particleFX.worldObj.isRemote)
        {
            Minecraft mc = Minecraft.getMinecraft();
            if (mc != null && mc.renderViewEntity != null && mc.effectRenderer != null)
            {
                int i = mc.gameSettings.particleSetting;
                double d6 = mc.renderViewEntity.posX - particleFX.posX;
                double d7 = mc.renderViewEntity.posY - particleFX.posY;
                double d8 = mc.renderViewEntity.posZ - particleFX.posZ;
                double d9 = Math.sqrt(mc.gameSettings.renderDistanceChunks) * 45;
                if (i <= 1)
                {
                    if (d6 * d6 + d7 * d7 + d8 * d8 <= d9 * d9)
                        Minecraft.getMinecraft().effectRenderer.addEffect(particleFX);
                }
            }
        }
    }

    public IIcon getWallIcon()
    {
        return this.wallIcon;
    }
}
