package de.nerogar.engine.entity;

import de.nerogar.engine.BaseWorld;
import de.nerogar.physics.BoundingAABB;
import de.nerogar.physics.PhysicsBody;
import de.nerogar.util.Vectorf;

public abstract class BaseEntity extends PhysicsBody {

	private static int MAX_ID;
	private int id;
	protected BaseWorld world;
	private boolean removeFromWorld;

	public BaseEntity(BoundingAABB bounding, Vectorf<?> position) {
		super(bounding, position);
		id = getNextID();
	}

	public int getID() {
		return id;
	}

	public abstract void update(float timeDelta);

	public abstract void render();

	public void setWorld(BaseWorld world) {
		removeFromWorld = false;
		this.world = world;
	}

	public void teleport(Vectorf<?> newPos) {
		getPosition().set(newPos);
	}

	public void removeFromWorld() {
		if (world != null) {
			removeFromWorld = true;
		}
	}

	public boolean markedToRemoveFromWorld() {
		return removeFromWorld;
	}

	private int getNextID() {
		MAX_ID++;
		return MAX_ID;
	}

}
