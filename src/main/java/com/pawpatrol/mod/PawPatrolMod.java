package com.pawpatrol.mod;

import com.pawpatrol.mod.init.EntityInit;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid = PawPatrolMod.MODID, name = PawPatrolMod.NAME, version = PawPatrolMod.VERSION,
     dependencies = "required-after:forge@[14.23.5.2847,)")
public class PawPatrolMod {

    public static final String MODID = "pawpatrol";
    public static final String NAME = "Paw Patrol Mod";
    public static final String VERSION = "1.0.0";

    public static Logger logger;

    @Mod.Instance(MODID)
    public static PawPatrolMod INSTANCE;

    public static PawPatrolMod instance() { return INSTANCE; }

    public static final CreativeTabs CREATIVE_TAB = new CreativeTabs("pawpatrol") {
        @Override
        public ItemStack getTabIconItem() {
            return EntityInit.CHASE_EGG != null
                ? new ItemStack(EntityInit.CHASE_EGG)
                : ItemStack.EMPTY;
        }
    };

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        EntityInit.preInit();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        EntityInit.init();
        logger.info("Paw Patrol Mod loaded! PAW Patrol is on a roll!");
    }
}
