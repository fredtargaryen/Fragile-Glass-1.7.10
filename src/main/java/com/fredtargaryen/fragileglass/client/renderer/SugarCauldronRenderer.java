package com.fredtargaryen.fragileglass.client.renderer;

import com.fredtargaryen.fragileglass.block.BlockSugarCauldron;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

public class SugarCauldronRenderer implements ISimpleBlockRenderingHandler
{
    private int renderID;

    public SugarCauldronRenderer(int rid){this.renderID = rid;}

    @Override
    public int getRenderId(){
        return this.renderID;
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId) {
        return true;
    }

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer){}

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer)
    {
        Tessellator tessellator = Tessellator.instance;
        tessellator.setBrightness(block.getMixedBrightnessForBlock(renderer.blockAccess, x, y, z));
        int i1 = block.colorMultiplier(renderer.blockAccess, x, y, z);
        float f = (float)(i1 >> 16 & 255) / 255.0F;
        float f1 = (float)(i1 >> 8 & 255) / 255.0F;
        float f2 = (float)(i1 & 255) / 255.0F;
        if (EntityRenderer.anaglyphEnable)
        {
            float f3 = (f * 30.0F + f1 * 59.0F + f2 * 11.0F) / 100.0F;
            float f4 = (f * 30.0F + f1 * 70.0F) / 100.0F;
            float f5 = (f * 30.0F + f2 * 70.0F) / 100.0F;
            f = f3;
            f1 = f4;
            f2 = f5;
        }
        tessellator.setColorOpaque_F(f, f1, f2);

        //ftsugarcauldron_main
        IIcon iicon = renderer.getBlockIcon(block, world, x, y, z, 0);
        double x2 = x + 0.0625;
        double x3 = x + 0.9375;
        double x4 = x + 1;
        double z2 = z + 0.0625;
        double z3 = z + 0.9375;
        double z4 = z + 1;
        double y2 = y + 0.0625;
        double y3 = y + 1;
        double u = iicon.getMinU();
        double u4 = iicon.getMaxU();
        double usize = u4 - u;
        double u2 = u + 0.0625 * usize;
        double u3 = u + 0.9375 * usize;
        double v = iicon.getMinV();
        double v4 = iicon.getMaxV();
        double vsize = v4 - v;
        double v2 = v + 0.0625 * vsize;
        double v3 = v + 0.9375 * vsize;
        //ftsugarcauldron_top
        IIcon iicon3 = ((BlockSugarCauldron) block).getTopIcon();
        double u5 = iicon3.getMinU();
        double u6 = iicon3.getMaxU();
        double v5 = iicon3.getMinV();
        double v6 = iicon3.getMaxV();

        //MAIN CAULDRON
        //TOP FACE
        tessellator.addVertexWithUV(x, y3, z, u6, v6);
        tessellator.addVertexWithUV(x, y3, z4, u6, v5);
        tessellator.addVertexWithUV(x4, y3, z4, u5, v5);
        tessellator.addVertexWithUV(x4, y3, z, u5, v6);
        //BOTTOM FACE
        tessellator.addVertexWithUV(x, y2, z, u4, v4);
        tessellator.addVertexWithUV(x, y2, z4, u4, v);
        tessellator.addVertexWithUV(x4, y2, z4, u, v);
        tessellator.addVertexWithUV(x4, y2, z, u, v4);
        //TOWARDS POSITIVE X
        //inner
        tessellator.addVertexWithUV(x2, y, z, u4, v4);
        tessellator.addVertexWithUV(x2, y3, z, u4, v);
        tessellator.addVertexWithUV(x2, y3, z4, u, v);
        tessellator.addVertexWithUV(x2, y, z4, u, v4);
        //outer
        tessellator.addVertexWithUV(x4, y, z, u4, v4);
        tessellator.addVertexWithUV(x4, y3, z, u4, v);
        tessellator.addVertexWithUV(x4, y3, z4, u, v);
        tessellator.addVertexWithUV(x4, y, z4, u, v4);
        //TOWARDS NEGATIVE X
        //inner
        tessellator.addVertexWithUV(x3, y, z4, u4, v4);
        tessellator.addVertexWithUV(x3, y3, z4, u4, v);
        tessellator.addVertexWithUV(x3, y3, z, u, v);
        tessellator.addVertexWithUV(x3, y, z, u, v4);
        //outer
        tessellator.addVertexWithUV(x, y, z4, u4, v4);
        tessellator.addVertexWithUV(x, y3, z4, u4, v);
        tessellator.addVertexWithUV(x, y3, z, u, v);
        tessellator.addVertexWithUV(x, y, z, u, v4);
        //TOWARDS NEGATIVE Z
        //inner
        tessellator.addVertexWithUV(x, y, z3, u4, v4);
        tessellator.addVertexWithUV(x, y3, z3, u4, v);
        tessellator.addVertexWithUV(x4, y3, z3, u, v);
        tessellator.addVertexWithUV(x4, y, z3, u, v4);
        //outer
        tessellator.addVertexWithUV(x, y, z, u4, v4);
        tessellator.addVertexWithUV(x, y3, z, u4, v);
        tessellator.addVertexWithUV(x4, y3, z, u, v);
        tessellator.addVertexWithUV(x4, y, z, u, v4);
        //TOWARDS POSITIVE Z
        //inner
        tessellator.addVertexWithUV(x4, y, z2, u4, v4);
        tessellator.addVertexWithUV(x4, y3, z2, u4, v);
        tessellator.addVertexWithUV(x, y3, z2, u, v);
        tessellator.addVertexWithUV(x, y, z2, u, v4);
        //outer
        tessellator.addVertexWithUV(x4, y, z4, u4, v4);
        tessellator.addVertexWithUV(x4, y3, z4, u4, v);
        tessellator.addVertexWithUV(x, y3, z4, u, v);
        tessellator.addVertexWithUV(x, y, z4, u, v4);

        int m = world.getBlockMetadata(x, y, z);
        if(m > 0)
        {
            IIcon iicon2;
            if(m > 1 && m < 5)
            {
                iicon2 = ((BlockSugarCauldron) block).getSugarWater();
            }
            else if(m == 1)
            {
                iicon2 = renderer.getBlockIconFromSide(Blocks.water, 0);
            }
            else if(m == 5) {
                iicon2 = renderer.getBlockIcon(Blocks.glass);
            }
            else
            {
                return true;
            }
            //Add icon
            double minu = iicon2.getMinU();
            double maxu = iicon2.getMaxU();
            double minv = iicon2.getMinV();
            double maxv = iicon2.getMaxV();
            tessellator.addVertexWithUV(x3, y3, z2, maxu, maxv);
            tessellator.addVertexWithUV(x3, y3, z3, maxu, minv);
            tessellator.addVertexWithUV(x2, y3, z3, minu, minv);
            tessellator.addVertexWithUV(x2, y3, z2, minu, maxv);
        }
        return true;
    }
}
