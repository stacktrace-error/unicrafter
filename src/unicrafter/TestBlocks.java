package unicrafter;

import arc.struct.Seq;
import mindustry.content.Blocks;
import mindustry.content.Fx;
import mindustry.content.Liquids;
import mindustry.content.UnitTypes;
import mindustry.world.Block;
import unicrafter.recipes.ChanceRecipe;
import unicrafter.world.UniversalCrafter;
import unicrafter.world.draw.DrawConstruction;

import static mindustry.content.Items.silicon;
import static mindustry.type.Category.liquid;
import static mindustry.type.ItemStack.with;
import static mindustry.world.meta.BuildVisibility.sandboxOnly;

public class TestBlocks{

    public static Block largeTankRefabricator;

    public static void load(){
        largeTankRefabricator = new UniversalCrafter("large-tank-refabricator"){{
            requirements(liquid, sandboxOnly, with());
            size = 5;
            rotate = true;
            outputsPayload = true;

            solid = false;
            vanillaIO = true;
            regionSuffix = "-dark";

            //for a 1:1 reconstructor, you want...
            instantInput = false; //...the payload to move in before despawning.
            instantFirstOutput = true; //...the created payload to appear immediately, without an effect.

            payDespawnEffect = Fx.none; //removing the despawn effect here because it might look weird

            recipes = Seq.with(
                    new ChanceRecipe("wall-to-tanks", Blocks.tankRefabricator,50){{
                        req(silicon, 150, Blocks.tungstenWallLarge, 1, Liquids.cyanogen, 1, "power", 1f);
                        out(UnitTypes.precept, 1);

                        addChanceOut(0.2f, UnitTypes.conquer, 1);
                        addChanceOut(0.1f, UnitTypes.stell, 3);

                        isUnit = true; //makes unit related map rules apply to the recipe

                        drawer = new DrawConstruction(true); //reconstruct makes it draw the last payload it received, only for 1:1 recons
                    }}
            );
        }};
    }
}
