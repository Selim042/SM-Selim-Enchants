package selim.selim_enchants.entities;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import selim.selim_enchants.SelimEnchants;

public class EntityFlyingTool extends Entity implements IEntityAdditionalSpawnData {

	private ItemStack stack;
	private boolean isIllusory;
	private int age;

	public EntityFlyingTool(World world) {
		super(world);
		this.setSize(0.75f, 0.75f);
	}

	public EntityFlyingTool(World world, ItemStack stack) {
		this(world, stack, false);
	}

	public EntityFlyingTool(World world, ItemStack stack, boolean isIllusory) {
		this(world);
		this.stack = stack;
		this.isIllusory = isIllusory;
	}

	public EntityFlyingTool(World world, ItemStack stack, double x, double y, double z) {
		this(world, stack);
	}

	public EntityFlyingTool(World world, ItemStack stack, boolean isIllusory, double x, double y,
			double z) {
		this(world, stack, isIllusory);
		this.setPosition(x, y, z);
	}

	@Override
	public void writeSpawnData(ByteBuf buff) {
		buff.writeBoolean(this.isIllusory);
		ByteBufUtils.writeItemStack(buff, this.stack);
	}

	@Override
	public void readSpawnData(ByteBuf buf) {
		this.isIllusory = buf.readBoolean();
		this.stack = ByteBufUtils.readItemStack(buf);
	}

	@Override
	protected void entityInit() {
		System.out.println("flying tool init");
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound compound) {
		this.isIllusory = compound.getBoolean("isIllusory");
		this.stack = new ItemStack(compound.getCompoundTag("stack"));
		this.age = compound.getInteger("age");
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound) {
		compound.setBoolean("isIllusory", this.isIllusory);
		compound.setTag("stack", this.stack.serializeNBT());
		compound.setInteger("age", this.age);
	}

	@Override
	public void onUpdate() {
		if (stack == null || stack.isEmpty() || this.age > 100) {
			System.out.println("killing");
			this.setDead();
			return;
		}
		// if (age > 0) {
		// this.setDead();
		// return;
		// }
		age++;
		Vec3d vec = this.getVectorForRotation(this.rotationPitch, this.rotationYaw).normalize();
		this.setPosition(this.posX + vec.x, this.posY + vec.y, this.posZ + vec.z);
		this.doBlockCollisions();
		if (this.isEntityInsideOpaqueBlock()) {
			BlockPos pos = new BlockPos(this);
			IBlockState state = this.world.getBlockState(pos);
			FakePlayer fakePlayer = SelimEnchants
					.getFakePlayer(this.world.getChunkFromBlockCoords(new BlockPos(this)));
			fakePlayer.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, this.stack);
			fakePlayer.rotationYaw = this.rotationYaw;
			fakePlayer.rotationPitch = this.rotationPitch;
			if (ForgeHooks.onBlockBreakEvent(this.world, GameType.SURVIVAL, fakePlayer, pos) != -1) {
				state.getBlock().removedByPlayer(state, world, pos, fakePlayer, true);
				state.getBlock().harvestBlock(world, fakePlayer, pos, state, null, this.stack);
			}
			fakePlayer.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, ItemStack.EMPTY);
			this.setDead();
			return;
		}
		super.onUpdate();
	}

	// @Override
	// public AxisAlignedBB getCollisionBox(Entity entityIn) {
	// return new AxisAlignedBB(-0.325f, -0.325f, -0.325f, 0.325f, 0.325f,
	// 0.325f);
	// }

	// @Override
	// public float getCollisionBorderSize() {
	// return super.getCollisionBorderSize();
	// }

	public ItemStack getItem() {
		return this.stack;
	}

	public int getAge() {
		return this.age;
	}

}
