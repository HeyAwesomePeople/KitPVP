package me.HeyAwesomePeople.kitpvp.custommobs;

import net.minecraft.server.v1_8_R2.EntityHuman;
import net.minecraft.server.v1_8_R2.EntityMonster;
import net.minecraft.server.v1_8_R2.GenericAttributes;
import net.minecraft.server.v1_8_R2.PathfinderGoalFloat;
import net.minecraft.server.v1_8_R2.PathfinderGoalHurtByTarget;
import net.minecraft.server.v1_8_R2.PathfinderGoalLeapAtTarget;
import net.minecraft.server.v1_8_R2.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_8_R2.PathfinderGoalMeleeAttack;
import net.minecraft.server.v1_8_R2.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.v1_8_R2.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_8_R2.PathfinderGoalRandomStroll;
import net.minecraft.server.v1_8_R2.World;

public class AgroSpider extends EntityMonster {
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public AgroSpider(World world) {
		super(world);
		setSize(1.4F, 0.9F);
		
	    this.goalSelector.a(1, new PathfinderGoalFloat(this));
	    this.goalSelector.a(3, new PathfinderGoalLeapAtTarget(this, 0.4F));
	    this.goalSelector.a(4, new PathfinderGoalMeleeAttack(this, 1.0D, false));
	    this.goalSelector.a(5, new PathfinderGoalRandomStroll(this, 0.8D));
	    this.goalSelector.a(6, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
	    this.goalSelector.a(6, new PathfinderGoalRandomLookaround(this));
	    this.targetSelector.a(1, new PathfinderGoalHurtByTarget(this, false, new Class[0]));
	    this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget(this, EntityHuman.class, true));
	}

	protected void initAttributes() {
		super.initAttributes();
		getAttributeInstance(GenericAttributes.maxHealth).setValue(16.0D);
		getAttributeInstance(GenericAttributes.d).setValue(0.300000011920929D);
	}

}
