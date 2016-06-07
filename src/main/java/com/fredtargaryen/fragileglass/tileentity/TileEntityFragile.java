package com.fredtargaryen.fragileglass.tileentity;

import com.fredtargaryen.fragileglass.DataReference;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.*;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TileEntityFragile extends TileEntity
{
    public TileEntityFragile(){super();}

    @Override
    public void updateEntity()
    {
        if(!this.worldObj.isRemote)
        {
            //Get all entities near enough to break it if fast enough
            Block myBlock = this.worldObj.getBlock(this.xCoord, this.yCoord, this.zCoord);
            AxisAlignedBB normAABB = myBlock.getCollisionBoundingBoxFromPool(this.worldObj, this.xCoord, this.yCoord, this.zCoord);
            AxisAlignedBB checkAABB = normAABB.expand(DataReference.GLASS_DETECTION_RANGE, DataReference.GLASS_DETECTION_RANGE, DataReference.GLASS_DETECTION_RANGE);
            List<Entity> entities = this.worldObj.getEntitiesWithinAABBExcludingEntity(null, checkAABB);
            Iterator<Entity> remover = entities.iterator();
            while(remover.hasNext())
            {
                Entity nextEnt = remover.next();
                if(!(nextEnt instanceof EntityLivingBase
                        || nextEnt instanceof EntityArrow
                        || nextEnt instanceof EntityFireball
                        || nextEnt instanceof EntityMinecart
                        || nextEnt instanceof EntityFallingBlock
                        || nextEnt instanceof EntityFireworkRocket
                        || nextEnt instanceof EntityBoat
                        || nextEnt instanceof EntityTNTPrimed))
                {
                    remover.remove();
                }
            }
            if (entities.size() > 0)
            {
                //Check if any of the possible entities are fast enough
                boolean stop = false;
                remover = entities.iterator();
                while(!stop && remover.hasNext())
                {
                    Entity nextEnt = remover.next();
                    if(nextEnt instanceof EntityFallingBlock)
                    {
                        this.worldObj.func_147480_a(this.xCoord, this.yCoord, this.zCoord, false);
                        stop = true;
                    }
                    else
                    {
                        if (Math.abs(nextEnt.motionX) > DataReference.MINIMUM_ENTITY_SPEED ||
                                Math.abs(nextEnt.motionY) > DataReference.MINIMUM_ENTITY_SPEED ||
                                Math.abs(nextEnt.motionZ) > DataReference.MINIMUM_ENTITY_SPEED)
                        {
                            //breaks it
                            this.worldObj.func_147480_a(this.xCoord, this.yCoord, this.zCoord, false);
                            stop = true;
                        }
                    }
                }
            }
        }
    }
}