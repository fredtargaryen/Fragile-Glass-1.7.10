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
            List<Entity> validEnts = new ArrayList<Entity>();
            for (Entity nextEnt : entities) {
                if (nextEnt instanceof EntityLivingBase
                        || nextEnt instanceof EntityArrow
                        || nextEnt instanceof EntityFireball
                        || nextEnt instanceof EntityMinecart
                        || nextEnt instanceof EntityBoat
                        || nextEnt instanceof EntityFireworkRocket
                        || nextEnt instanceof EntityFallingBlock
                        || nextEnt instanceof EntityTNTPrimed) {
                    validEnts.add(0, nextEnt);
                }
            }
            if (validEnts.size() > 0)
            {
                //Check if any of the possible entities are fast enough
                boolean stop = false;
                int count = 0;
                while(!stop && count < validEnts.size())
                {
                    Entity nextEnt = validEnts.get(count);
                    if(nextEnt instanceof EntityFallingBlock)
                    {
                        //breaks it
                        this.worldObj.func_147480_a(this.xCoord, this.yCoord, this.zCoord, false);
                        stop = true;
                    }
                    else
                    {
                        double mx = nextEnt.motionX;
                        double my = nextEnt.motionY;
                        double mz = nextEnt.motionZ;
                        if ((nextEnt.posX < normAABB.minX && mx > DataReference.MINIMUM_ENTITY_SPEED)
                                || (nextEnt.posX > normAABB.maxX && mx < -1 * DataReference.MINIMUM_ENTITY_SPEED)
                                || (nextEnt.posY < normAABB.maxY && my > DataReference.MINIMUM_ENTITY_SPEED)
                                || (nextEnt.posY > normAABB.maxY && my < -1 * DataReference.MINIMUM_ENTITY_SPEED)
                                || (nextEnt.posZ < normAABB.maxZ && mz > DataReference.MINIMUM_ENTITY_SPEED)
                                || (nextEnt.posZ > normAABB.maxZ && mz < -1 * DataReference.MINIMUM_ENTITY_SPEED)) {
                            //breaks it
                            this.worldObj.func_147480_a(this.xCoord, this.yCoord, this.zCoord, false);
                            stop = true;
                        }
                    }
                    count++;
                }
            }
        }
    }
}