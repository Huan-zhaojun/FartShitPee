package com.huan.fart_shit_pee.common.Block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.Entity;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class shitBlock extends Block {
    private static final VoxelShape shape = Block.makeCuboidShape(3, 0, 3, 13, 5, 13);

    private static IntegerProperty STATE = IntegerProperty.create("size", 1, 3);

    public shitBlock() {
        super(Properties.create(Material.CLAY, MaterialColor.GRAY).slipperiness(0.8F).sound(SoundType.SLIME).hardnessAndResistance(0.5f).notSolid());
        this.setDefaultState(this.getStateContainer().getBaseState().with(STATE, 1));
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return shape;
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(STATE);
        super.fillStateContainer(builder);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new shitTileEntity();
    }

    /**
     * Block's chance to react to a living entity falling on it.
     */
    public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance) {
        if (entityIn.isSuppressingBounce()) {
            super.onFallenUpon(worldIn, pos, entityIn, fallDistance);
        } else {
            entityIn.onLivingFall(fallDistance, 0.5F);
        }

    }

    public void onLanded(IBlockReader worldIn, Entity entityIn) {
        if (entityIn.isSuppressingBounce()) {
            super.onLanded(worldIn, entityIn);
        } else {
            this.bounceEntity(entityIn);
        }

    }

    private void bounceEntity(Entity entity) {
        Vector3d vector3d = entity.getMotion();
        if (vector3d.y < 0.0D) {
            double d0 = 0.7D;
            entity.setMotion(vector3d.x, -vector3d.y * d0, vector3d.z);
        }

    }

    public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn) {
        double d0 = Math.abs(entityIn.getMotion().y);
        if (d0 < 0.1D && !entityIn.isSteppingCarefully()) {
            double d1 = 0.4D + d0 * 0.2D;
            entityIn.setMotion(entityIn.getMotion().mul(d1, 1.0D, d1));
        }

        super.onEntityWalk(worldIn, pos, entityIn);
    }
}
