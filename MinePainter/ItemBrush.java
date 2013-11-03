package hx.MinePainter;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ItemBrush extends Item{

	public static ItemBrush instance;
	
	public ItemBrush(int par1) {
		super(par1);
		setUnlocalizedName("itemBrush");
		setCreativeTab(CreativeTabs.tabTools);
		this.setMaxStackSize(1);
		this.setMaxDamage(240);
		instance = this;
	}
	@Override
	@SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister par1IconRegister)
    {
        this.itemIcon = par1IconRegister.registerIcon("MinePainter:brush");
    }

}
