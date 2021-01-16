// Made with Blockbench 3.7.5
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports

import net.minecraft.client.renderer.model.ModelRenderer;

public class KittenBeeModel extends EntityModel<Entity> {
	private final ModelRenderer body;
	private final ModelRenderer torso;
	private final ModelRenderer stinger;
	private final ModelRenderer leftAntenna;
	private final ModelRenderer rightAntenna;
	private final ModelRenderer rightWing;
	private final ModelRenderer leftWing;
	private final ModelRenderer frontLegs;
	private final ModelRenderer middleLegs;
	private final ModelRenderer backLegs;

	public KittenBeeModel() {
		textureWidth = 64;
		textureHeight = 64;
		body = new ModelRenderer(this);
		torso = new ModelRenderer(this);
		stinger = new ModelRenderer(this);
		leftAntenna = new ModelRenderer(this);
		rightAntenna = new ModelRenderer(this);
		rightWing = new ModelRenderer(this);
		leftWing = new ModelRenderer(this);
		frontLegs = new ModelRenderer(this);
		middleLegs = new ModelRenderer(this);
		backLegs = new ModelRenderer(this);
		body.setRotationPoint(0.0F, 19.0F, 0.0F);

		torso.setRotationPoint(0.0F, 0.0F, 0.0F);
		body.addChild(torso);
		torso.setTextureOffset(24, 6).addBox(-1.5F, 1.0F, -6.0F, 3.0F, 2.0F, 1.0F, 0.0F, false);
		torso.setTextureOffset(24, 3).addBox(-2.5F, -5.0F, -4.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);
		torso.setTextureOffset(24, 3).addBox(1.5F, -5.0F, -4.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);
		torso.setTextureOffset(0, 0).addBox(-3.5F, -4.0F, -5.0F, 7.0F, 7.0F, 10.0F, 0.0F, false);

		stinger.setRotationPoint(0.0F, 0.0F, 0.0F);
		torso.addChild(stinger);
		stinger.setTextureOffset(3, 1).addBox(0F, -1.0F, 5.0F, 0.0F, 1.0F, 2.0F, 0.0F, false);

		leftAntenna.setRotationPoint(0.0F, -2.0F, -5.0F);
		torso.addChild(leftAntenna);
		leftAntenna.setTextureOffset(2, 0).addBox(1.5F, -2.0F, -3.0F, 1.0F, 2.0F, 3.0F, 0.0F, false);

		rightAntenna.setRotationPoint(0.0F, -2.0F, -5.0F);
		torso.addChild(rightAntenna);
		rightAntenna.setTextureOffset(2, 3).addBox(-2.5F, -2.0F, -3.0F, 1.0F, 2.0F, 3.0F, 0.0F, false);

		rightWing.setRotationPoint(-1.5F, -4.0F, -3.0F);
		body.addChild(rightWing);
		rightWing.setTextureOffset(0, 18).addBox(-9F, 0.0F, 0.0F, 9.0F, 0.0F, 6.0F, 0.0F, false);

		leftWing.setRotationPoint(1.5F, -4.0F, -3.0F);
		body.addChild(leftWing);
		leftWing.setTextureOffset(0, 18).addBox(0F, 0.0F, 0.0F, 9.0F, 0.0F, 6.0F, 0.0F, true);

		frontLegs.setRotationPoint(1.5F, 3.0F, -2.0F);
		body.addChild(frontLegs);
		frontLegs.setTextureOffset(24, 0).addBox(-3F, 0.0F, 0.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
		frontLegs.setTextureOffset(24, 0).addBox(-1F, 0.0F, 0.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);

		middleLegs.setRotationPoint(1.5F, 3.0F, 0.0F);
		body.addChild(middleLegs);
		middleLegs.setTextureOffset(24, 0).addBox(-4F, 0.0F, 0.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
		middleLegs.setTextureOffset(24, 0).addBox(0F, 0.0F, 0.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
		backLegs.setRotationPoint(1.5F, 3.0F, 2.0F);
		body.addChild(backLegs);
		backLegs.setTextureOffset(24, 0).addBox(-4F, 0.0F, 0.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
		backLegs.setTextureOffset(24, 0).addBox(0F, 0.0F, 0.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);

		gel = new ModelRenderer(this);
		gel.setRotationPoint(0.0F, 0.0F, 0.0F);
		torso.addChild(gel);
		gel.setTextureOffset(0, 25).addBox(-3.5F, -4.0F, -5.0F, 7.0F, 7.0F, 10.0F, 0.7F, false);

		crystals = new ModelRenderer(this);
		crystals.setRotationPoint(-0.25F, 1.0F, -6.5F);
		body.addChild(crystals);
		setRotationAngle(crystals, 0.3927F, 0.0F, 0.0F);
		crystals.setTextureOffset(48, 48).addBox(1.0F, -3.8582F, 5.7674F, 2.0F, 2.0F, 2.0F, 0.0F, false);
		crystals.setTextureOffset(48, 52).addBox(-1.0F, -6.0F, 4.0F, 2.0F, 3.0F, 2.0F, 0.0F, false);

		bone = new ModelRenderer(this);
		bone.setRotationPoint(0.0F, 0.2242F, 3.1543F);
		crystals.addChild(bone);
		setRotationAngle(bone, -0.3927F, 0.0F, 0.0F);
		bone.setTextureOffset(48, 57).addBox(-2.0F, -7.7242F, 1.8457F, 3.0F, 4.0F, 3.0F, 0.0F, true);

		bone2 = new ModelRenderer(this);
		bone2.setRotationPoint(-1.0F, -0.5412F, 1.45F);
		crystals.addChild(bone2);
		setRotationAngle(bone2, -0.3927F, 0.0F, 0.0F);
		bone2.setTextureOffset(48, 48).addBox(-1.5F, -5.8588F, 2.6934F, 2.0F, 2.0F, 2.0F, 0.0F, false);
		bone2.setTextureOffset(48, 52).addBox(1.0F, -6.8588F, 5.6934F, 2.0F, 3.0F, 2.0F, 0.0F, true);

		bone5 = new ModelRenderer(this);
		bone5.setRotationPoint(2.0F, 0.0F, 0.0F);
		bone2.addChild(bone5);
		setRotationAngle(bone5, 0.0F, 0.0F, -0.3927F);
		bone5.setTextureOffset(48, 52).addBox(-1.5F, -6.6588F, 5.6934F, 2.0F, 3.0F, 2.0F, 0.0F, true);

		bone3 = new ModelRenderer(this);
		bone3.setRotationPoint(3.0F, -4.4588F, -3.3066F);
		bone2.addChild(bone3);
		setRotationAngle(bone3, 0.0F, 0.0F, 0.5236F);
		bone3.setTextureOffset(56, 51).addBox(-0.7321F, -2.0F, 10.0F, 2.0F, 4.0F, 2.0F, 0.0F, false);

		bone4 = new ModelRenderer(this);
		bone4.setRotationPoint(-5.0981F, 0.634F, 0.0F);
		bone3.addChild(bone4);
		setRotationAngle(bone4, 0.0F, 0.0F, -1.3963F);
		bone4.setTextureOffset(56, 51).addBox(-1.1252F, 1.9F, 11.0F, 2.0F, 4.0F, 2.0F, 0.0F, true);
	}

	@Override
	public void setRotationAngles(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
		//previously the render function, render code was moved to a method below
	}

	@Override
	public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
		body.render(matrixStack, buffer, packedLight, packedOverlay);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}