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
		entities.sort((a, b) -> (int) (a.getID() - b.getID()));
	}

	public BaseEntity getBuilding(long id) {

		//binary serch
		int l = 0;
		int r = entities.size() - 1;
		int p;
		while (l <= r) {
			p = (l + r) / 2;

			if (entities.get(p).getID() == id) return entities.get(p);
			if (entities.get(p).getID() < id) {
				l = p + 1;
			} else {
				r = p - 1;
			}
		}

		return null;

	}

	public void clear() {
		entities.clear();
	}

}
