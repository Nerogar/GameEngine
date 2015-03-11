package de.nerogar.engine;

import de.nerogar.engine.entity.BaseEntity;
import de.nerogar.physics.PhysicsSpace;
import de.nerogar.render.Shader;
import de.nerogar.util.Vectorf;

public abstract class BaseWorld<T extends Vectorf<T>> {

	protected PhysicsSpace<T> physicsSpace;
	protected EntityList<T> entityList;
	public boolean isStatic;

	public BaseWorld(T dimension) {
		physicsSpace = new PhysicsSpace<T>(dimension);
		entityList = new EntityList<T>();
	}

	public void update(float timeDelta) {
		entityList.update(timeDelta);

		if (!isStatic) {
			physicsSpace.update(timeDelta);
		}
	}

	/**
	 * Calls <code>.render(shader)</code> on every entity.
	 * Override this method if you want to handle rendering yourself.
	 * 
	 * @param shader the currently active shader
	 */
	public void render(Shader shader) {
		//physicsSpace.render();
		entityList.render(shader);
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
