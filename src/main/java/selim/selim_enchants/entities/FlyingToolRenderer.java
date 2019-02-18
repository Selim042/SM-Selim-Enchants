package selim.selim_enchants.entities;

import java.util.List;
import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class FlyingToolRenderer extends Render<EntityFlyingTool> {

	private final RenderItem itemRenderer;
	private final Random random = new Random();

	public FlyingToolRenderer(RenderManager manager, RenderItem itemRenderer) {
		super(manager);
		this.itemRenderer = itemRenderer;
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityFlyingTool entity) {
		return TextureMap.LOCATION_BLOCKS_TEXTURE;
	}

	@Override
	public void doRender(EntityFlyingTool entity, double x, double y, double z, float entityYaw,
			float partialTicks) {
		ItemStack itemStack = entity.getItem();
		if (itemStack == null)
			return;
		int seed = itemStack.isEmpty() ? 187
				: Item.getIdFromItem(itemStack.getItem()) + itemStack.getMetadata();
		this.random.setSeed((long) seed);
		boolean flag = false;

		if (this.bindEntityTexture(entity)) {
			this.renderManager.renderEngine.getTexture(this.getEntityTexture(entity))
					.setBlurMipmap(false, false);
			flag = true;
		}

		GlStateManager.enableRescaleNormal();
		GlStateManager.alphaFunc(516, 0.1F);
		GlStateManager.enableBlend();
		RenderHelper.enableStandardItemLighting();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
				GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE,
				GlStateManager.DestFactor.ZERO);
		GlStateManager.pushMatrix();
		IBakedModel ibakedmodel = this.itemRenderer.getItemModelWithOverrides(itemStack, entity.world,
				(EntityLivingBase) null);
		int modelCount = this.transformModelCount(entity, x, y, z, partialTicks, ibakedmodel);
		boolean is3D = ibakedmodel.isGui3d();

		if (!is3D) {
			float transX = -0.0F * (float) (modelCount - 1) * 0.5F;
			float transY = -0.0F * (float) (modelCount - 1) * 0.5F;
			float transZ = -0.09375F * (float) (modelCount - 1) * 0.5F;
			GlStateManager.translate(transX, transY, transZ);
		}

		if (this.renderOutlines) {
			GlStateManager.enableColorMaterial();
			GlStateManager.enableOutlineMode(this.getTeamColor(entity));
		}

		// Vec3d lookVec = Vec3d.fromPitchYaw(entity.rotationPitch,
		// entity.rotationYaw);
		// GlStateManager.rotate(lookVec.)
		// float yaw = Math.abs(entity.rotationYaw);
		// if (yaw > 90 && yaw < 180)
		// yaw = -yaw;
		GlStateManager.rotate(entity.rotationYaw - 90, 0.0f, -1.0f, 0.0f);
		GlStateManager.rotate(entity.rotationPitch, 0.0f, 0.0f, 1.0f);

		GlStateManager.scale(1.5f, 1.5f, 1.5f);
		if (modelCount != 0) {
			if (is3D) {
				GlStateManager.pushMatrix();

				IBakedModel transformedModel = ForgeHooksClient.handleCameraTransforms(ibakedmodel,
						ItemCameraTransforms.TransformType.GROUND, false);
				this.renderItem(itemStack, transformedModel);
				GlStateManager.popMatrix();
			} else {
				GlStateManager.pushMatrix();

				IBakedModel transformedModel = ForgeHooksClient.handleCameraTransforms(ibakedmodel,
						ItemCameraTransforms.TransformType.GROUND, false);
				this.renderItem(itemStack, transformedModel);
				GlStateManager.popMatrix();
				GlStateManager.translate(0.0F, 0.0F, 0.0F);
			}
		}

		if (this.renderOutlines) {
			GlStateManager.disableOutlineMode();
			GlStateManager.disableColorMaterial();
		}

		GlStateManager.popMatrix();
		GlStateManager.disableRescaleNormal();
		GlStateManager.disableBlend();
		this.bindEntityTexture(entity);

		if (flag)
			this.renderManager.renderEngine.getTexture(this.getEntityTexture(entity))
					.restoreLastBlurMipmap();

		super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}

	private int transformModelCount(EntityFlyingTool entity, double x, double y, double z,
			float partialTicks, IBakedModel model) {
		ItemStack stack = entity.getItem();
		Item item = stack.getItem();

		if (item == null)
			return 0;
		else {
			GlStateManager.translate((float) x, (float) y + (entity.height / 2), (float) z);
			float rotatePos = -(((float) entity.getAge() + partialTicks) / 2.5F)
					* (180F / (float) Math.PI);
			 GlStateManager.rotate(rotatePos, 0.0F, 0.0F, 1.0F);

			return 1;
		}
	}

	private void renderItem(ItemStack stack, IBakedModel model) {
		if (!stack.isEmpty()) {
			GlStateManager.pushMatrix();
			GlStateManager.translate(-0.5F, -0.5F, -0.5F);

			if (model.isBuiltInRenderer()) {
				// GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
				GlStateManager.enableRescaleNormal();
				stack.getItem().getTileEntityItemStackRenderer().renderByItem(stack);
			} else {
				this.renderModel(model, stack);

				if (stack.hasEffect()) {
					this.renderEffect(model);
				}
			}

			GlStateManager.popMatrix();
		}
	}

	private void renderModel(IBakedModel model, ItemStack stack) {
		this.renderModel(model, -1, stack);
	}

	private void renderModel(IBakedModel model, int color, ItemStack stack) {
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		bufferbuilder.begin(7, DefaultVertexFormats.ITEM);

		for (EnumFacing enumfacing : EnumFacing.values()) {
			this.renderQuads(bufferbuilder, model.getQuads((IBlockState) null, enumfacing, 0L), color,
					stack);
		}

		this.renderQuads(bufferbuilder, model.getQuads((IBlockState) null, (EnumFacing) null, 0L), color,
				stack);
		tessellator.draw();
	}

	private static final ResourceLocation RES_ITEM_GLINT = new ResourceLocation(
			"textures/misc/enchanted_item_glint.png");

	private void renderEffect(IBakedModel model) {
		GlStateManager.depthMask(false);
		GlStateManager.depthFunc(514);
		GlStateManager.disableLighting();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_COLOR, GlStateManager.DestFactor.ONE);
		Minecraft.getMinecraft().getTextureManager().bindTexture(RES_ITEM_GLINT);
		GlStateManager.matrixMode(5890);
		GlStateManager.pushMatrix();
		GlStateManager.scale(8.0F, 8.0F, 8.0F);
		float f = (float) (Minecraft.getSystemTime() % 3000L) / 3000.0F / 8.0F;
		GlStateManager.translate(f, 0.0F, 0.0F);
		GlStateManager.rotate(-50.0F, 0.0F, 0.0F, 1.0F);
		this.renderModel(model, -8372020, ItemStack.EMPTY);
		GlStateManager.popMatrix();
		GlStateManager.pushMatrix();
		GlStateManager.scale(8.0F, 8.0F, 8.0F);
		float f1 = (float) (Minecraft.getSystemTime() % 4873L) / 4873.0F / 8.0F;
		GlStateManager.translate(-f1, 0.0F, 0.0F);
		GlStateManager.rotate(10.0F, 0.0F, 0.0F, 1.0F);
		this.renderModel(model, -8372020, ItemStack.EMPTY);
		GlStateManager.popMatrix();
		GlStateManager.matrixMode(5888);
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA,
				GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		GlStateManager.enableLighting();
		GlStateManager.depthFunc(515);
		GlStateManager.depthMask(true);
		Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
	}

	private void renderQuads(BufferBuilder renderer, List<BakedQuad> quads, int color, ItemStack stack) {
		boolean flag = color == -1 && !stack.isEmpty();
		int i = 0;

		for (int j = quads.size(); i < j; ++i) {
			BakedQuad bakedquad = quads.get(i);
			int k = color;

			if (flag && bakedquad.hasTintIndex()) {
				k = Minecraft.getMinecraft().getItemColors().colorMultiplier(stack,
						bakedquad.getTintIndex());

				if (EntityRenderer.anaglyphEnable) {
					k = TextureUtil.anaglyphColor(k);
				}

				k = k | -16777216;
			}

			net.minecraftforge.client.model.pipeline.LightUtil.renderQuadColor(renderer, bakedquad, k);
		}
	}

}
