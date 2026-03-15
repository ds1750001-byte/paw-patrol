package com.pawpatrol.mod.init;

import com.pawpatrol.mod.PawPatrolMod;
import com.pawpatrol.mod.entity.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class EntityInit {

    public static Item CHASE_EGG;
    public static Item RUBBLE_EGG;
    public static Item MARSHALL_EGG;
    public static Item ROCKY_EGG;
    public static Item EVEREST_EGG;
    public static Item SKYE_EGG;

    public static void preInit() {
        EntityRegistry.registerModEntity(new ResourceLocation(PawPatrolMod.MODID, "chase"),
            EntityChase.class, "chase", 1, PawPatrolMod.instance(), 64, 1, true);
        EntityRegistry.registerModEntity(new ResourceLocation(PawPatrolMod.MODID, "rubble"),
            EntityRubble.class, "rubble", 2, PawPatrolMod.instance(), 64, 1, true);
        EntityRegistry.registerModEntity(new ResourceLocation(PawPatrolMod.MODID, "marshall"),
            EntityMarshall.class, "marshall", 3, PawPatrolMod.instance(), 64, 1, true);
        EntityRegistry.registerModEntity(new ResourceLocation(PawPatrolMod.MODID, "rocky"),
            EntityRocky.class, "rocky", 4, PawPatrolMod.instance(), 64, 1, true);
        EntityRegistry.registerModEntity(new ResourceLocation(PawPatrolMod.MODID, "everest"),
            EntityEverest.class, "everest", 5, PawPatrolMod.instance(), 64, 1, true);
        EntityRegistry.registerModEntity(new ResourceLocation(PawPatrolMod.MODID, "skye"),
            EntitySkye.class, "skye", 6, PawPatrolMod.instance(), 64, 1, true);
    }

    public static void init() {
        CHASE_EGG = new ItemMonsterPlacer(EntityChase.class)
            .setRegistryName(new ResourceLocation(PawPatrolMod.MODID, "chase_egg"))
            .setUnlocalizedName("chase_egg").setCreativeTab(PawPatrolMod.CREATIVE_TAB);
        ForgeRegistries.ITEMS.register(CHASE_EGG);

        RUBBLE_EGG = new ItemMonsterPlacer(EntityRubble.class)
            .setRegistryName(new ResourceLocation(PawPatrolMod.MODID, "rubble_egg"))
            .setUnlocalizedName("rubble_egg").setCreativeTab(PawPatrolMod.CREATIVE_TAB);
        ForgeRegistries.ITEMS.register(RUBBLE_EGG);

        MARSHALL_EGG = new ItemMonsterPlacer(EntityMarshall.class)
            .setRegistryName(new ResourceLocation(PawPatrolMod.MODID, "marshall_egg"))
            .setUnlocalizedName("marshall_egg").setCreativeTab(PawPatrolMod.CREATIVE_TAB);
        ForgeRegistries.ITEMS.register(MARSHALL_EGG);

        ROCKY_EGG = new ItemMonsterPlacer(EntityRocky.class)
            .setRegistryName(new ResourceLocation(PawPatrolMod.MODID, "rocky_egg"))
            .setUnlocalizedName("rocky_egg").setCreativeTab(PawPatrolMod.CREATIVE_TAB);
        ForgeRegistries.ITEMS.register(ROCKY_EGG);

        EVEREST_EGG = new ItemMonsterPlacer(EntityEverest.class)
            .setRegistryName(new ResourceLocation(PawPatrolMod.MODID, "everest_egg"))
            .setUnlocalizedName("everest_egg").setCreativeTab(PawPatrolMod.CREATIVE_TAB);
        ForgeRegistries.ITEMS.register(EVEREST_EGG);

        SKYE_EGG = new ItemMonsterPlacer(EntitySkye.class)
            .setRegistryName(new ResourceLocation(PawPatrolMod.MODID, "skye_egg"))
            .setUnlocalizedName("skye_egg").setCreativeTab(PawPatrolMod.CREATIVE_TAB);
        ForgeRegistries.ITEMS.register(SKYE_EGG);

        RenderingRegistry.registerEntityRenderingHandler(EntityChase.class,
            manager -> new com.pawpatrol.mod.render.RenderChase(manager));
        RenderingRegistry.registerEntityRenderingHandler(EntityRubble.class,
            manager -> new com.pawpatrol.mod.render.RenderRubble(manager));
        RenderingRegistry.registerEntityRenderingHandler(EntityMarshall.class,
            manager -> new com.pawpatrol.mod.render.RenderMarshall(manager));
        RenderingRegistry.registerEntityRenderingHandler(EntityRocky.class,
            manager -> new com.pawpatrol.mod.render.RenderRocky(manager));
        RenderingRegistry.registerEntityRenderingHandler(EntityEverest.class,
            manager -> new com.pawpatrol.mod.render.RenderEverest(manager));
        RenderingRegistry.registerEntityRenderingHandler(EntitySkye.class,
            manager -> new com.pawpatrol.mod.render.RenderSkye(manager));
    }
}
