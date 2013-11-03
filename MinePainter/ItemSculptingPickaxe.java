package hx.MinePainter;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ItemSculptingPickaxe extends Item{

	public ItemSculptingPickaxe(int id) {
		super(id);
		maxStackSize = 1;
		this.setMaxDamage(240);
		setCreativeTab(CreativeTabs.tabTools);
		setUnlocalizedName("itemSculptingPickaxe");
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister par1IconRegister)
    {
        this.itemIcon = par1IconRegister.registerIcon("");
    }
}
