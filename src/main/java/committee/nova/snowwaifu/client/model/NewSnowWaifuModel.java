package committee.nova.snowwaifu.client.model;

import committee.nova.snowwaifu.common.entity.impl.SnowWaifuEntity;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class NewSnowWaifuModel extends HumanoidModel<SnowWaifuEntity> {
    public NewSnowWaifuModel(ModelPart root) {
        super(root);
    }

    public static LayerDefinition create() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition partRoot = mesh.getRoot();
        partRoot.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F).texOffs(32, 45).addBox(-4.5F, 10.0F, -2.5F, 9.0F, 14.0F, 5.0F), PartPose.ZERO);
        partRoot.addOrReplaceChild("head", CubeListBuilder.create().addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F), PartPose.ZERO);
        partRoot.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(14, 32).addBox(-1.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F), PartPose.offset(5.0F, 2.0F, 0.0F));
        partRoot.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(16, 48).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F), PartPose.offset(1.9F, 12.0F, 0.0F));
        partRoot.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 48).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F), PartPose.offset(-1.9F, 12.0F, 0.0F));
        partRoot.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(0, 32).addBox(-2.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F), PartPose.offset(-5.0F, 2.0F, 0.0F));
        PartDefinition hat = partRoot.addOrReplaceChild("hat", CubeListBuilder.create().addBox(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F), PartPose.ZERO);
        hat.addOrReplaceChild("crown_front", CubeListBuilder.create().texOffs(24, 0).addBox(-5.0F, -4.0F, 0.0F, 10.0F, 4.0F, 0.0F), PartPose.offsetAndRotation(0.0F, -6.0F, -4.0F, 0.3926991F, 0.0F, 0.0F));
        hat.addOrReplaceChild("crown_right", CubeListBuilder.create().texOffs(24, 4).addBox(-5.0F, -4.0F, 0.0F, 10.0F, 4.0F, 0.0F), PartPose.offsetAndRotation(-4.0F, -6.0F, 0.0F, 0.3926991F, 1.5707964F, 0.0F));
        hat.addOrReplaceChild("crown_left", CubeListBuilder.create().texOffs(44, 4).addBox(-5.0F, -4.0F, 0.0F, 10.0F, 4.0F, 0.0F), PartPose.offsetAndRotation(4.0F, -6.0F, 0.0F, -0.3926991F, 1.5707964F, 0.0F));
        hat.addOrReplaceChild("crown_back", CubeListBuilder.create().texOffs(44, 0).addBox(-5.0F, -4.0F, 0.0F, 10.0F, 4.0F, 0.0F), PartPose.offsetAndRotation(0.0F, -6.0F, 4.0F, -0.3926991F, 0.0F, 0.0F));
        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    public void setupAnim(SnowWaifuEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        ModelPart currentModel;
        if (entity.isBreathing()) {
            float f6 = Mth.sin(this.attackTime * 3.1415927F);
            float f7 = Mth.sin((1.0F - (1.0F - this.attackTime) * (1.0F - this.attackTime)) * 3.1415927F);
            this.rightArm.zRot = 0.0F;
            this.leftArm.zRot = 0.0F;
            this.rightArm.yRot = -(0.1F - f6 * 0.6F);
            this.leftArm.yRot = 0.1F - f6 * 0.6F;
            this.rightArm.xRot = -1.5707964F;
            this.leftArm.xRot = -1.5707964F;
            currentModel = this.rightArm;
            currentModel.xRot -= f6 * 1.2F - f7 * 0.4F;
            currentModel = this.leftArm;
            currentModel.xRot -= f6 * 1.2F - f7 * 0.4F;
            currentModel = this.rightArm;
            currentModel.zRot += Mth.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
            currentModel = this.leftArm;
            currentModel.zRot -= Mth.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
            currentModel = this.rightArm;
            currentModel.xRot += Mth.sin(ageInTicks * 0.067F) * 0.05F;
            currentModel = this.leftArm;
            currentModel.xRot -= Mth.sin(ageInTicks * 0.067F) * 0.05F;
        }
    }
}
