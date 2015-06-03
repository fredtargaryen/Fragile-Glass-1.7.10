/**
 * TO DO
 * Thin Ice sounds wrong when breaking
 * Thin Ice texture is wrong
 * Thin Ice sides aren't hidden at the right time (check)
 * Thin Ice worldgen
 */

package com.fredtargaryen.fragileglass;

import com.fredtargaryen.fragileglass.block.*;
import com.fredtargaryen.fragileglass.client.renderer.PaneRenderer;
import com.fredtargaryen.fragileglass.client.renderer.StainedPaneRenderer;
import com.fredtargaryen.fragileglass.client.renderer.ThinIceRenderer;
import com.fredtargaryen.fragileglass.item.ItemStainedFragileGlass;
import com.fredtargaryen.fragileglass.item.ItemStainedFragilePane;
import com.fredtargaryen.fragileglass.proxy.CommonProxy;
import com.fredtargaryen.fragileglass.tileentity.TileEntityFragile;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

@Mod(modid = DataReference.MODID, version = DataReference.VERSION, name=DataReference.MODNAME)
public class FragileGlassBase
{
	// The instance of your mod that Forge uses.
    @Instance(value = DataReference.MODID)
    public static FragileGlassBase instance;
    
    //Declare all blocks here
    public static Block fragileGlass;
	public static Block fragilePane;
    public static Block stainedFragileGlass;
    public static Block stainedFragilePane;
	public static Block sugarBlock;
    public static Block thinIce;
    
    // Says where the client and server 'proxy' code is loaded.
    @SidedProxy(clientSide=DataReference.CLIENTPROXYPATH, serverSide=DataReference.SERVERPROXYPATH)
    public static CommonProxy proxy;
        
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        int normalPaneRenderID = RenderingRegistry.getNextAvailableRenderId();
        int stainedPaneRenderID = RenderingRegistry.getNextAvailableRenderId();
        int thinIceRenderID = RenderingRegistry.getNextAvailableRenderId();

    	fragileGlass = new BlockFragileGlass()
    			.setBlockName("ftfragileglass")
    			.setStepSound(Block.soundTypeGlass);
    	fragilePane = new BlockFragilePane(normalPaneRenderID)
    			.setBlockName("ftfragilepane")
    			.setStepSound(Block.soundTypeGlass);
        stainedFragileGlass = new BlockStainedFragileGlass()
                .setBlockName("ftstainedfragileglass")
                .setStepSound(Block.soundTypeGlass);
        stainedFragilePane = new BlockStainedFragilePane(stainedPaneRenderID)
                .setBlockName("ftstainedfragilepane")
                .setStepSound(Block.soundTypeGlass);
    	sugarBlock = new SugarBlock()
    			.setBlockName("ftsugarblock")
                .setStepSound(Block.soundTypeGravel)
    			.setBlockTextureName(DataReference.MODID+":ftsugarblock");
        thinIce = new BlockThinIce(thinIceRenderID)
                .setBlockName("ftthinice");

        RenderingRegistry.registerBlockHandler(normalPaneRenderID, new PaneRenderer(normalPaneRenderID));
        RenderingRegistry.registerBlockHandler(stainedPaneRenderID, new StainedPaneRenderer(stainedPaneRenderID));
        RenderingRegistry.registerBlockHandler(thinIceRenderID, new ThinIceRenderer(thinIceRenderID));

    	//Register blocks
    	GameRegistry.registerBlock(fragileGlass, fragileGlass.getUnlocalizedName().substring(5));
    	GameRegistry.registerBlock(fragilePane, fragilePane.getUnlocalizedName().substring(5));
        GameRegistry.registerBlock(stainedFragileGlass, ItemStainedFragileGlass.class, stainedFragileGlass.getUnlocalizedName().substring(5));
        GameRegistry.registerBlock(stainedFragilePane, ItemStainedFragilePane.class, stainedFragilePane.getUnlocalizedName().substring(5));
    	GameRegistry.registerBlock(sugarBlock, sugarBlock.getUnlocalizedName().substring(5));
        GameRegistry.registerBlock(thinIce, thinIce.getUnlocalizedName().substring(5));
        OreDictionary.registerOre("blockSugar", sugarBlock);
    }
        
    @EventHandler
    public void load(FMLInitializationEvent event)
    {
    	//Recipes
    	GameRegistry.addRecipe(new ItemStack(sugarBlock, 1), "xxx", "xxx", "xxx",
    			'x', Items.sugar);
    	GameRegistry.addShapelessRecipe(new ItemStack(Items.sugar, 9), new ItemStack(sugarBlock));
    	GameRegistry.addSmelting(sugarBlock, new ItemStack(fragileGlass, 64), 0.1F);
    	GameRegistry.addRecipe(new ItemStack(fragilePane, 16), "xxx", "xxx",
    	        'x', fragileGlass);
        for(int meta = 0; meta < 16; meta++)
        {
            GameRegistry.addRecipe(new ItemStack(stainedFragileGlass, 8, meta), "xxx", "xox", "xxx",
                    'x', new ItemStack(fragileGlass), 'o', new ItemStack(Items.dye, 1, meta));
            GameRegistry.addRecipe(new ItemStack(stainedFragilePane, 16, meta), "xxx", "xxx",
                    'x', new ItemStack(stainedFragileGlass, 1, meta));
        }

        GameRegistry.registerTileEntity(TileEntityFragile.class, "glassTE");

    	proxy.registerRenderers();
    }
        
    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
    	// Stub Method
    }
}