package de.nerogar.engine;

import de.nerogar.engine.entity.BaseEntity;
import de.nerogar.physics.PhysicsSpace;
import de.nerogar.util.Vector3f;
import de.nerogar.util.Vectorf;

public abstract class BaseWorld {

	protected PhysicsSpace physicsSpace;
	protected EntityList entityList;
	public boolean isStatic;

	public BaseWorld(Vectorf<?> dimension) {
		physicsSpace = new PhysicsSpace(dimension);
		entityList = new EntityList();
	}

	public void update(float timeDelta) {
		entityList.update(timeDelta);

		if (!isStatic) {
			physicsSpace.update(timeDelta);
		}
	}

	public void render() {
		//physicsSpace.render();
		entityList.render();
	}

	public void spawnEntity(BaseEntity entity, Vector3f pos) {
		entity.setWorld(this);
		if (pos != null) entity.teleport(pos);
		entityList.addEntity(entity);
		physicsSpace.addBody(entity);
	}

	public void clear() {
		physicsSpace.clear();
		entityList.clear();
	}

	public EntityList getEntityList() {
		return entityList;
	}

	public PhysicsSpace getPhysicsSpace() {
		return physicsSpace;
	}

	public abstract void load();

	public abstract void save();
}
