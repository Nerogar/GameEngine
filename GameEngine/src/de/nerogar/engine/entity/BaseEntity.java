package de.nerogar.engine.entity;

import java.util.List;

import de.nerogar.engine.BaseWorld;
import de.nerogar.engine.UpdateEvent;
import de.nerogar.physics.BoundingAABB;
import de.nerogar.physics.PhysicsBody;
import de.nerogar.util.Vectorf;

public abstract class BaseEntity<T extends Vectorf<T>> extends PhysicsBody<T> {

	private static long MAX_ID;
	private long id;
	protected BaseWorld<T> world;
	private boolean removeFromWorld;

	public BaseEntity(BoundingAABB<T> bounding, T position) {
		this(getNextID(), bounding, position);
	}

	public BaseEntity(long id, BoundingAABB<T> bounding, T position) {
		super(bounding, position);
		if (id > MAX_ID) MAX_ID = id;
		this.id = id;
	}

	public long getID() {
		return id;
	}

	public abstract void update(float timeDelta, List<UpdateEvent> events);

	public abstract void render();

	public void setWorld(BaseWorld<T> world) {
		removeFromWorld = false;
		this.world = world;
	}

	public void teleport(T newPos) {
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
