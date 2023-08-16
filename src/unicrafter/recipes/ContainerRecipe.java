package unicrafter.recipes;

import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import mindustry.content.Blocks;
import mindustry.ctype.UnlockableContent;
import mindustry.type.Item;
import mindustry.type.ItemStack;
import mindustry.type.Liquid;
import mindustry.type.LiquidStack;
import mindustry.ui.Styles;
import unicrafter.world.UniversalCrafter;
import unicrafter.world.UniversalCrafter.UniversalBuild;

import static mindustry.Vars.iconSmall;

public class ContainerRecipe extends Recipe{
    public ContainerRecipe(String name, UnlockableContent iconContent, float time){
        super(name, iconContent, time);
    }
    public ContainerRecipe(String name, float time){
        super(name, time);
    }

    public Object[] containerReq(Object... items){
        for(int i = 0; i < items.length; i += 2){
            if(items[i] instanceof Item)
                itemReqContainer.add(new ItemStack((Item) items[i], ((Number) items[i + 1]).intValue()));
            else if(items[i] instanceof Liquid)
                liqReqContainer.add(new LiquidStack((Liquid) items[i], ((Number) items[i + 1]).floatValue()));
        }
        return items;
    }
    public Object[] containerOut(Object... items){
        for(int i = 0; i < items.length; i += 2){
            if(items[i] instanceof Item)
                itemOutContainer.add(new ItemStack((Item) items[i], ((Number) items[i + 1]).intValue()));
            else if(items[i] instanceof Liquid)
                liqReqContainer.add(new LiquidStack((Liquid) items[i], ((Number) items[i + 1]).floatValue()));
        }
        return items;
    }

    @Override
    public void addRecipeInputTable(Table table){
        table.table(in -> { //output
            super.addRecipeInputTable(in);

            if(itemReqContainer.size > 0){
                in.row();

                in.image(Blocks.reinforcedContainer.uiIcon).size(iconSmall);
                in.row();

                in.table(Styles.black5, input -> {
                    input.add(contentListTable(null, itemReqContainer, null, time, false)).pad(5f).grow();
                }).pad(5f).grow();
            }
        });
    }

    @Override
    public void addRecipeOutputTable(Table table){
        table.table(out -> { //output
            super.addRecipeOutputTable(out);

            if(itemOutContainer.size > 0){
                out.row();

                out.image(Blocks.reinforcedContainer.uiIcon).size(iconSmall);
                out.row();

                out.table(Styles.black5, output -> output.add(contentListTable(null, itemOutContainer, null, time, true)).pad(5f).grow()
                ).pad(5f).grow();
            }
        });
    }

    public final Seq<ItemStack> itemReqContainer = new Seq<>(4), itemOutContainer = new Seq<>(4);
    public final Seq<LiquidStack> liqReqContainer = new Seq<>(4), liqOutContainer = new Seq<>(4);

    public boolean wantsContainer(UniversalCrafter.UniversalBuild build){
        if(build.container == null) return false;

        for(int i = 0; i < itemReqContainer.size; i++){
            if(!build.container.build.items.has(itemReqContainer)) return false;
        }
        for(int i = 0; i < itemOutContainer.size; i++){
            if(!(build.container.build.items.get(itemOutContainer.get(i).item) + itemOutContainer.get(i).amount > build.block.itemCapacity)) return false;
        }
        return true;
    }

    @Override
    public boolean shouldConsume(UniversalCrafter.UniversalBuild build){
        return wantsContainer(build);
    }

    @Override
    public void craft(UniversalBuild build){
        if(build.container != null){
            itemReqContainer.each(stack -> build.container.build.items.remove(stack.item, stack.amount));
            itemOutContainer.each(stack -> build.container.build.items.add(stack.item, stack.amount));
        }
    }
}
