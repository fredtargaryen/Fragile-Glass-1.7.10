package com.fredtargaryen.fragileglass.client.renderer;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;

public class ThinIceRenderer implements ISimpleBlockRenderingHandler
{
    private int renderID;

    public ThinIceRenderer(int rID)
    {
        this.renderID = rID;
    }

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer)
    {
        Tessellator tessellator = Tessellator.instance;
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        tessellator.startDrawingQuads();
        renderBlock(tessellator, 0, 0, 0, block, renderer, false);
        tessellator.draw();
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer)
    {
        Tessellator tessellator = Tessellator.instance;
        this.renderBlock(tessellator, x, y, z, block, renderer, true);
        return true;
    }

    public void renderBlock(Tessellator tessellator, int x, int y, int z, Block block, RenderBlocks renderer, boolean inWorld)
    {
        if(inWorld) {
            tessellator.setBrightness(block.getMixedBrightnessForBlock(renderer.blockAccess, x, y, z));
        }
        else
        {
            tessellator.setBrightness(15728640);
        }
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
        IIcon iicon;

        if (renderer.hasOverrideBlockTexture())
        {
            iicon = renderer.overrideBlockTexture;
        }
        else
        {
            iicon = renderer.getBlockIconFromSideAndMetadata(block, 0, 0);
        }

        double minu = (double)iicon.getMinU();
        double maxu = (double)iicon.getMaxU();
        double minv = (double)iicon.getMinV();
        double maxv = (double)iicon.getMaxV();
        tessellator.addTranslation(x, y, z);
        //bottom
        tessellator.addVertexWithUV(1.0, 0.9375, 1.0, maxu, maxv);
        tessellator.addVertexWithUV(1.0, 0.9375, 0.0, maxu, minv);
        tessellator.addVertexWithUV(0.0, 0.9375, 0.0, minu, minv);
        tessellator.addVertexWithUV(0.0, 0.9375, 1.0, minu, maxv);
        //top
        tessellator.addVertexWithUV(1.0, 1.0, 0.0, maxu, maxv);
        tessellator.addVertexWithUV(1.0, 1.0, 1.0, maxu, minv);
        tessellator.addVertexWithUV(0.0, 1.0, 1.0, minu, minv);
        tessellator.addVertexWithUV(0.0, 1.0, 0.0, minu, maxv);

        maxv = minv + ((1.0/16.0) * (maxv - minv));
        //front
        tessellator.addVertexWithUV(1.0, 0.9375, 0.0, maxu, maxv);
        tessellator.addVertexWithUV(1.0, 1.0, 0.0, maxu, minv);
        tessellator.addVertexWithUV(0.0, 1.0, 0.0, minu, minv);
        tessellator.addVertexWithUV(0.0, 0.9375, 0.0, minu, maxv);
        //back
        tessellator.addVertexWithUV(0.0, 0.9375, 1.0, maxu, maxv);
        tessellator.addVertexWithUV(0.0, 1.0, 1.0, maxu, minv);
        tessellator.addVertexWithUV(1.0, 1.0, 1.0, minu, minv);
        tessellator.addVertexWithUV(1.0, 0.9375, 1.0, minu, maxv);
        //left
        tessellator.addVertexWithUV(0.0, 0.9375, 0.0, maxu, maxv);
        tessellator.addVertexWithUV(0.0, 1.0, 0.0, maxu, minv);
        tessellator.addVertexWithUV(0.0, 1.0, 1.0, minu, minv);
        tessellator.addVertexWithUV(0.0, 0.9375, 1.0, minu, maxv);
        //right
        tessellator.addVertexWithUV(1.0, 0.9375, 1.0, maxu, maxv);
        tessellator.addVertexWithUV(1.0, 1.0, 1.0, maxu, minv);
        tessellator.addVertexWithUV(1.0, 1.0, 0.0, minu, minv);
        tessellator.addVertexWithUV(1.0, 0.9375, 0.0, minu, maxv);

        tessellator.addTranslation(-x, -y, -z);
    }
    @Override
    public boolean shouldRender3DInInventory(int modelId) {
        return true;
    }

    @Override
    public int getRenderId() {
        return this.renderID;
    }
}
