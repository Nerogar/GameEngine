package de.nerogar.engine.entity;

import de.nerogar.engine.BaseWorld;
import de.nerogar.physics.BoundingAABB;
import de.nerogar.physics.PhysicsBody;
import de.nerogar.util.Vectorf;

public abstract class BaseEntity extends PhysicsBody {

	private static long MAX_ID;
	private long id;
	protected BaseWorld world;
	private boolean removeFromWorld;

	public BaseEntity(BoundingAABB bounding, Vectorf<?> position) {
		this(getNextID(), bounding, position);
	}

	public BaseEntity(long id, BoundingAABB bounding, Vectorf<?> position) {
		super(bounding, position);
		if (id > MAX_ID) MAX_ID = id;
		this.id = id;
	}

	public long getID() {
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

	protected static long getNextID() {
		MAX_ID++;
		return MAX_ID;
	}

}
