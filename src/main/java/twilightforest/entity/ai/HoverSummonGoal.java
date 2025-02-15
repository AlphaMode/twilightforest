package twilightforest.entity.ai;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.util.Mth;
import twilightforest.entity.boss.SnowQueenEntity;
import twilightforest.entity.boss.SnowQueenEntity.Phase;

import java.util.EnumSet;

import net.minecraft.world.entity.ai.goal.Goal.Flag;

public class HoverSummonGoal extends HoverBaseGoal<SnowQueenEntity> {

	private static final int MAX_MINIONS_AT_ONCE = 4;

	private int seekTimer;

	private final int maxSeekTime;

	public HoverSummonGoal(SnowQueenEntity snowQueen) {
		super(snowQueen, 6F, 6F);

		this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
		this.maxSeekTime = 80;
	}

	@Override
	public boolean canUse() {
		LivingEntity target = this.attacker.getTarget();

		if (target == null) {
			return false;
		} else if (!target.isAlive()) {
			return false;
		} else if (this.attacker.getCurrentPhase() != Phase.SUMMON) {
			return false;
		} else {
			return attacker.getSensing().hasLineOfSight(target);
		}
	}

	@Override
	public boolean canContinueToUse() {
		LivingEntity target = this.attacker.getTarget();

		if (target == null || !target.isAlive()) {
			return false;
		} else if (this.attacker.getCurrentPhase() != Phase.SUMMON) {
			return false;
		} else if (this.seekTimer > this.maxSeekTime) {
			return false;
		} else {
			return this.canEntitySee(this.attacker, hoverPosX, hoverPosY, hoverPosZ);
		}
	}

	@Override
	public void stop() {
	}

	@Override
	public void tick() {

		this.seekTimer++;
		LivingEntity target = this.attacker.getTarget();

		// are we there yet?
		if (this.attacker.distanceToSqr(hoverPosX, hoverPosY, hoverPosZ) <= 1.0F) {
			this.checkAndSummon();

			this.makeNewHoverSpot(target);
		}

		// check if we are at our waypoint target
		double offsetX = this.hoverPosX - this.attacker.getX();
		double offsetY = this.hoverPosY - this.attacker.getY();
		double offsetZ = this.hoverPosZ - this.attacker.getZ();

		double distanceDesired = offsetX * offsetX + offsetY * offsetY + offsetZ * offsetZ;

		distanceDesired = Mth.sqrt((float) distanceDesired);

		// add velocity
		double velX = offsetX / distanceDesired * 0.05D;
		double velY = offsetY / distanceDesired * 0.1D;
		double velZ = offsetZ / distanceDesired * 0.05D;

		// gravity offset
		velY += 0.05F;

		this.attacker.push(velX, velY, velZ);

		// look at target
		if (target != null) {
			this.attacker.lookAt(target, 30.0F, 30.0F);
			this.attacker.getLookControl().setLookAt(target, 30.0F, 30.0F);
		}
	}

	@Override
	protected void makeNewHoverSpot(LivingEntity target) {
		super.makeNewHoverSpot(target);
		this.seekTimer = 0;
	}

	private void checkAndSummon() {
		if (this.attacker.getSummonsRemaining() > 0 && this.attacker.countMyMinions() < MAX_MINIONS_AT_ONCE) {
			this.attacker.summonMinionAt(this.attacker.getTarget());
		}
	}
}
