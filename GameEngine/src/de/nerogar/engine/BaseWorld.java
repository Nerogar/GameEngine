package de.nerogar.engine;

import java.util.List;

import de.nerogar.engine.entity.BaseEntity;
import de.nerogar.physics.PhysicsSpace;
import de.nerogar.util.Vectorf;

public abstract class BaseWorld<T extends Vectorf<T>> {

	protected PhysicsSpace<T> physicsSpace;
	protected EntityList<T> entityList;
	public boolean isStatic;

	public BaseWorld(T dimension) {
		physicsSpace = new PhysicsSpace<T>(dimension);
		entityList = new EntityList<T>();
	}

	public List<UpdateEvent> update(float timeDelta) {
		List<UpdateEvent> events = entityList.update(timeDelta);

		if (!isStatic) {
			physicsSpace.update(timeDelta);
		}

		return events;
	}

	public void render() {
		//physicsSpace.render();
		entityList.render();
	}

	public void spawnEntity(BaseEntity<T> entity, T pos) {
		entity.setWorld(this);
		if (pos != null) entity.teleport(pos);
		entityList.addEntity(entity);
		physicsSpace.addBody(entity);
	}

	public void clear() {
		physicsSpace.clear();
		entityList.clear();
	}

	public EntityList<T> getEntityList() {
		return entityList;
	}

	public PhysicsSpace<T> getPhysicsSpace() {
		return physicsSpace;
	}

	public abstract void load();

	public abstract void save();
}
