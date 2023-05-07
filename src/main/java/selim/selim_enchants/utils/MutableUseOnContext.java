package selim.selim_enchants.utils;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class MutableUseOnContext extends UseOnContext {

	@Nullable
	private Player player;
	private InteractionHand hand;
	private BlockHitResult hitResult;
	private Level level;
	private ItemStack itemStack;

	public MutableUseOnContext(UseOnContext ctx) {
		super(ctx.getLevel(), ctx.getPlayer(), ctx.getHand(), ctx.getItemInHand(), null);
		this.player = ctx.getPlayer();
		this.hand = ctx.getHand();
		this.hitResult = new BlockHitResult(ctx.getClickLocation(), ctx.getClickedFace(), ctx.getClickedPos(),
				ctx.isInside());
		this.level = ctx.getLevel();
		this.itemStack = ctx.getItemInHand();
	}

	public MutableUseOnContext setPlayer(Player player) {
		this.player = player;
		return this;
	}

	public MutableUseOnContext setHand(InteractionHand hand) {
		this.hand = hand;
		return this;
	}

	public MutableUseOnContext setHitResult(BlockHitResult hitResult) {
		this.hitResult = hitResult;
		return this;
	}

	public MutableUseOnContext setLevel(Level level) {
		this.level = level;
		return this;
	}

	public MutableUseOnContext setBlockPos(BlockPos pos) {
		this.hitResult = hitResult.withPosition(pos);
		return this;
	}

	public MutableUseOnContext setItemStack(ItemStack itemStack) {
		this.itemStack = itemStack;
		return this;
	}

	@Override
	public BlockPos getClickedPos() {
		return this.hitResult.getBlockPos();
	}

	@Override
	public Direction getClickedFace() {
		return this.hitResult.getDirection();
	}

	@Override
	public Vec3 getClickLocation() {
		return this.hitResult.getLocation();
	}

	@Override
	public boolean isInside() {
		return this.hitResult.isInside();
	}

	@Override
	public ItemStack getItemInHand() {
		return this.itemStack;
	}

	@Override
	@Nullable
	public Player getPlayer() {
		return this.player;
	}

	@Override
	public InteractionHand getHand() {
		return this.hand;
	}

	@Override
	public Level getLevel() {
		return this.level;
	}

	@Override
	public Direction getHorizontalDirection() {
		return this.player == null ? Direction.NORTH : this.player.getDirection();
	}

	@Override
	public boolean isSecondaryUseActive() {
		return this.player != null && this.player.isSecondaryUseActive();
	}

	@Override
	public float getRotation() {
		return this.player == null ? 0.0F : this.player.getYRot();
	}

}