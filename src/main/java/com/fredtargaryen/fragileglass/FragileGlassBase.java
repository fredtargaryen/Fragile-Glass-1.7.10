/**
 * TO DO
 * Check patches are genning
 * Ent falling not perfect
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
import com.fredtargaryen.fragileglass.worldgen.PatchGen;
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
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.oredict.OreDictionary;

@Mod(modid = DataReference.MODID, version = DataReference.VERSION, name=DataReference.MODNAME)
public class FragileGlassBase
{
	// The instance of your mod that Forge uses.
    @Instance(value = DataReference.MODID)
    public static FragileGlassBase instance;

    public static PatchGen patchGen = new PatchGen();

    //Config vars
    public static boolean genThinIce;
    public static int avePatchSize;
    public static int genChance;

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
        //CONFIG SETUP
        Configuration config = new Configuration(event.getSuggestedConfigurationFile());
        config.load();
        genThinIce = config.getBoolean("genThinIce", "Worldgen", true, "If true, thin ice patches will generate on frozen bodies of water");
        avePatchSize = config.getInt("avePatchSize", "Worldgen", 3, 2, 10, "Average patch diameter");
        genChance = config.getInt("genChance", "Worldgen", 3, 2, 5, "1 in x chance of patch appearing");
        config.save();

        //BLOCK SETUP
        int normalPaneRenderID = RenderingRegistry.getNextAvailableRenderId();
        int stainedPaneRenderID = RenderingRegistry.getNextAvailableRenderId();

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
        thinIce = new BlockThinIce()
                .setBlockName("ftthinice")
                .setStepSound(Block.soundTypeGlass);

        RenderingRegistry.registerBlockHandler(normalPaneRenderID, new PaneRenderer(normalPaneRenderID));
        RenderingRegistry.registerBlockHandler(stainedPaneRenderID, new StainedPaneRenderer(stainedPaneRenderID));

    	//Register blocks
    	GameRegistry.registerBlock(fragileGlass, fragileGlass.getUnlocalizedName().substring(5));
    	GameRegistry.registerBlock(fragilePane, fragilePane.getUnlocalizedName().substring(5));
        GameRegistry.registerBlock(stainedFragileGlass, ItemStainedFragileGlass.class, stainedFragileGlass.getUnlocalizedName().substring(5));
        GameRegistry.registerBlock(stainedFragilePane, ItemStainedFragilePane.class, stainedFragilePane.getUnlocalizedName().substring(5));
    	GameRegistry.registerBlock(sugarBlock, sugarBlock.getUnlocalizedName().substring(5));
        GameRegistry.registerBlock(thinIce, thinIce.getUnlocalizedName().substring(5));
        OreDictionary.registerOre("blockSugar", sugarBlock);

        proxy.registerRenderers();
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
        if(genThinIce) GameRegistry.registerWorldGenerator(patchGen, 0);
    }
        
    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
    	// Stub Method
    }
}