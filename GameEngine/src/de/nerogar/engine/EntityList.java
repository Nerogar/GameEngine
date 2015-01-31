package de.nerogar.engine;

import java.util.*;

import de.nerogar.engine.entity.BaseEntity;

public class EntityList {
	private List<BaseEntity> entities;

	public EntityList() {
		entities = new ArrayList<BaseEntity>();
	}

	public void update(float timeDelta) {
		Iterator<BaseEntity> iterator = entities.iterator();

		while (iterator.hasNext()) {
			BaseEntity entity = iterator.next();
			if (entity.markedToRemoveFromWorld()) {
				iterator.remove();
			} else {
				entity.update(timeDelta);
			}
		}

	}

	public void render() {
		for (BaseEntity entity : entities) {
			entity.render();
		}
	}

	public void addEntity(BaseEntity entity) {
		entities.add(entity);
	}

	public void clear() {
		entities.clear();
	}

}
