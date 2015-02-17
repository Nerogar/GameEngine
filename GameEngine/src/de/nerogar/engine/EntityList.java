package de.nerogar.engine;

import java.util.*;

import de.nerogar.engine.entity.BaseEntity;
import de.nerogar.render.Shader;
import de.nerogar.util.Vectorf;

public class EntityList<T extends Vectorf<T>> {
	private List<BaseEntity<T>> entities;

	public EntityList() {
		entities = new ArrayList<BaseEntity<T>>();
	}

	public List<UpdateEvent> update(float timeDelta) {
		Iterator<BaseEntity<T>> iterator = entities.iterator();

		List<UpdateEvent> events = new ArrayList<UpdateEvent>();

		while (iterator.hasNext()) {
			BaseEntity<T> entity = iterator.next();
			if (entity.markedToRemoveFromWorld()) {
				iterator.remove();
			} else {
				entity.update(timeDelta, events);
			}
		}

		return events;
	}

	public void render(Shader shader) {
		for (BaseEntity<T> entity : entities) {
			entity.render(shader);
		}
	}

	public void addEntity(BaseEntity<T> entity) {
		entities.add(entity);
		entities.sort((a, b) -> (int) (a.getID() - b.getID()));
	}

	public List<BaseEntity<T>> getEntitiesWithin(T posSmall, T posBig) {
		List<BaseEntity<T>> candidates = new ArrayList<BaseEntity<T>>();

		entityLoop: for (BaseEntity<T> entity : entities) {
			for (int i = 0; i < entity.getPosition().getComponentCount(); i++) {
				if (entity.getPosition().get(i) < posSmall.get(i) || entity.getPosition().get(i) > posBig.get(i)) continue entityLoop;
			}

			candidates.add(entity);
		}

		return candidates;
	}

	public List<BaseEntity<T>> getEntities() {
		return entities;
	}

	public BaseEntity<T> getEntity(long id) {

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
