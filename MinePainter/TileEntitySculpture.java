package hx.MinePainter;

import java.util.Arrays;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3Pool;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.biome.BiomeGenBase;

public class TileEntitySculpture extends TileEntity implements IBlockAccess{
	
	public static TileEntitySculpture full = new TileEntitySculpture();
	
	byte[] data = new byte[64];
	
	private int biasX,biasY,biasZ;
	
	private boolean needUpdate = false;
	
	public TileEntitySculpture()
	{
		Arrays.fill(data,(byte)-1);
	}
	
	private boolean invalid(int x,int y,int z)
	{
		if(x>=8)return true;
		if(x<0)return true;
		if(y>=8)return true;
		if(y<0)return true;
		if(z>=8)return true;
		if(z<0)return true;
		return false;
	}
	
	public boolean get(int x,int y,int z)
	{
		if(invalid(x,y,z))return false;
		byte strip = data[x*8+y];
		
		return ((strip >> z) & 1) == 1;
	}
	
	public void set (int x,int y,int z)
	{
		x%=8;
		y%=8;
		byte strip = data[x*8+y];
		
		strip |= (1 << z);
		data[x * 8 + y] = strip;
		
		needUpdate = true;
	}
	
	public void del (int x,int y,int z)
	{
		x%=8;
		y%=8;
		byte strip = data[x*8+y];
		
		strip &= (1 << z) ^ -1;
		data[x * 8 + y] = strip;
		
		needUpdate = true;
	}
	
	public void updateEntity()
	{
		if(needUpdate)
		{
			needUpdate = false;
			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		}
	}
	
	//TileEntity util
	
	@Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
		super.writeToNBT(par1NBTTagCompound);
		par1NBTTagCompound.setByteArray("sculpture", data);
    }
	
	@Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
		super.readFromNBT(par1NBTTagCompound);
		data = par1NBTTagCompound.getByteArray("sculpture");
		needUpdate = true;
    }
	
	@Override
    public Packet getDescriptionPacket()
    {
        NBTTagCompound tag = new NBTTagCompound();
        this.writeToNBT(tag);
        return new Packet132TileEntityData(this.xCoord, this.yCoord, this.zCoord, 1, tag);
    }

    @Override
    public void onDataPacket(INetworkManager net, Packet132TileEntityData pkt)
    {
        readFromNBT(pkt.customParam1);
    }
    
	//IBlockAccess

	public void bias(int x,int y,int z)
	{
		biasX = x;
		biasY = y;
		biasZ = z;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean isBlockOpaqueCube(int var1, int var2, int var3) {
		return get(var1 - this.xCoord + biasX,
				   var2 - this.yCoord + biasY,
				   var3 - this.zCoord + biasZ);
	}
	
	@Override
	public int getBlockId(int var1, int var2, int var3) {
		return get(var1 - this.xCoord + biasX,
				   var2 - this.yCoord + biasY,
				   var3 - this.zCoord + biasZ) ? ((BlockSculpture)this.getBlockType()).materialBlock(this.getBlockMetadata()).blockID : 0;
	}

	@Override
	public TileEntity getBlockTileEntity(int var1, int var2, int var3) {
		return null;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getLightBrightnessForSkyBlocks(int var1, int var2, int var3, int var4) {
		return worldObj.getLightBrightnessForSkyBlocks(var1, var2, var3, var4);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public float getBrightness(int var1, int var2, int var3, int var4) {
		return worldObj.getBrightness(var1, var2, var3, var4);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public float getLightBrightness(int var1, int var2, int var3) {
		return worldObj.getLightBrightness(var1, var2, var3);
	}

	@Override
	public int getBlockMetadata(int var1, int var2, int var3) {
		return 0;
	}

	@Override
	public Material getBlockMaterial(int var1, int var2, int var3) {
		return Material.rock;
	}

	@Override
	public boolean isBlockNormalCube(int var1, int var2, int var3) {
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean isAirBlock(int var1, int var2, int var3) {
		return !get(var1 - this.xCoord + biasX,
				   var2 - this.yCoord + biasY,
				   var3 - this.zCoord + biasZ);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public BiomeGenBase getBiomeGenForCoords(int var1, int var2) {
		return null;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getHeight() {
		return 8;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean extendedLevelsInChunkCache() {
		return worldObj.extendedLevelsInChunkCache();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean doesBlockHaveSolidTopSurface(int var1, int var2, int var3) {
		return get(var1 - this.xCoord + biasX,
				   var2 - this.yCoord + biasY,
				   var3 - this.zCoord + biasZ);
	}

	@Override
	public Vec3Pool getWorldVec3Pool() {
		return this.worldObj.getWorldVec3Pool();
	}

	@Override
	public boolean isBlockProvidingPowerTo(int var1, int var2, int var3,
			int var4) {
		return false;
	}
	
	public int[] rayTrace(Vec3 start, Vec3 dist)
	{
		int hitX = -1;
		int hitY = -1;
		int hitZ = -1;
		
		for(int x=0;x<8;x++)
			for(int y=0;y<8;y++)
				for(int z=0;z<8;z++)
				{
					if(!get(x,y,z))continue;
					
					if(hitX == -1);
					else if( (x!= hitX) && (x>hitX) == (dist.xCoord>0) )
						continue;
					else if( (y!= hitY) && (y>hitY) == (dist.yCoord>0) )
						continue;
					else if( (z!= hitZ) && (z>hitZ) == (dist.zCoord>0) )
						continue;
					
					if(!cross(xCoord + x/8f, yCoord + y/8f, zCoord + z/8f, start, dist))continue;
					
					hitX = x;
					hitY = y;
					hitZ = z;
				}
		
		if(hitX == -1)return null;
		return new int[]{hitX, hitY, hitZ};
	}
	
	private boolean cross(float f,float g,float h, Vec3 st, Vec3 dist)
	{
		Vec3 normal = dist.crossProduct(Vec3.createVectorHelper(f + 1/16f, g + 1/16f, h + 1/16f).subtract(st))	
						.crossProduct(dist).normalize();
				
		
		boolean hasPos = false;
		boolean hasNeg = false;		
		for(int i = 0; i < 8; i ++)
		{
			Vec3 vert = Vec3.createVectorHelper(f + (i&1)/8f , g + ((i&2)>>1)/8f, h + ((i&4)>>2)/8f);
			vert = vert.subtract(st);
			
			if(vert.dotProduct(normal)>0) hasPos = true;
			else hasNeg = true;
			
			if(hasPos && hasNeg)return true;
		}

		return false;
	}
	
	public static int[] selectionBox(int[] selectionPoint, int mode, int axis, int[] minmax)
	{
		if(selectionPoint == null)return null;
		
		int[] box = new int[]{selectionPoint[0],
								  selectionPoint[1],
								  selectionPoint[2],
								  selectionPoint[0]+1,
								  selectionPoint[1]+1,
								  selectionPoint[2]+1};
		switch(mode)
		{
		case 0:
			return box;
		case 1:
			box[axis]=minmax[axis];
			box[axis+3]=minmax[axis+3];
			return box;
		case 2:
			for(int i =0;i<3;i++)
				if(i!=axis)
					{
					box[i]=minmax[i];
					box[i+3]=minmax[i+3];
					}
			return box;
		}
		return null;
	}
	
	public static int getMode(ItemStack is)
	{
		if(is == null)return -1;
		if(is.itemID == Item.pickaxeWood.shiftedIndex)return 0;
		if(is.itemID == Item.pickaxeStone.shiftedIndex)return 1;
		if(is.itemID == Item.pickaxeSteel.shiftedIndex)return 2;
		return -1;
	}
	
	public static int getAxis(Vec3 look)
	{
		double[] val = new double[] { Math.abs(look.xCoord),Math.abs(look.yCoord),Math.abs(look.zCoord)};
		
		for(int i =0;i<3;i++)
		{
			boolean max = true;
			for(int j =0;j<3;j++)
				if(val[j]>val[i])max = false;
			if(max)return i;
		}
		return 0;
	}
	
	public void updateBounds(Block block)
	{
		int[] minmax = getMinMax();
		
		if(minmax[0] < minmax[3])
			block.setBlockBounds(minmax[0]/8f, minmax[1]/8f, minmax[2]/8f, minmax[3]/8f, minmax[4]/8f, minmax[5]/8f);
		else block.setBlockBounds(0, 0, 0, 1,1,1);
	}
	
	public int[] getMinMax()
	{
		int minX,minY,minZ,maxX,maxY,maxZ;
		minX=minY=minZ=8;
		maxX=maxY=maxZ=0;
		
		for(int _x = 0;_x<8;_x++)
			for(int _y = 0;_y<8;_y++)
				for(int _z = 0;_z<8;_z++)
					if(get(_x,_y,_z))
					{
						minX = Math.min(_x, minX);
						minY = Math.min(_y, minY);
						minZ = Math.min(_z, minZ);
						
						maxX = Math.max(_x+1, maxX);
						maxY = Math.max(_y+1, maxY);
						maxZ = Math.max(_z+1, maxZ);
						
					}
		
		return new int[]{minX,minY,minZ,maxX,maxY,maxZ};
	}
}