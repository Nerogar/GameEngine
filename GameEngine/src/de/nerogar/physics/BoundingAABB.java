package de.nerogar.physics;

import de.nerogar.util.Vectorf;

public class BoundingAABB {

	public Vectorf<?> a;
	public Vectorf<?> b;

	public BoundingAABB(Vectorf<?> a, Vectorf<?> b) {
		this.a = a;
		this.b = b;
	}

	public boolean intersects(BoundingAABB bounding) {
		for (int i = 0; i < a.getComponentCount(); i++) {
			if (bounding.b.get(i) <= a.get(i) || bounding.a.get(i) >= b.get(i)) return false;
		}
		return true;

		//return !(bounding.b.getX() <= a.getX() || bounding.a.getX() >= b.getX() || bounding.b.getY() <= a.getY() || bounding.a.getY() >= b.getY() || bounding.b.getZ() <= a.getZ() || bounding.a.getZ() >= b.getZ());
	}

	public boolean intersects(BoundingAABB bounding, Vectorf<?> ownOffset, Vectorf<?> boundingOffset) {
		for (int i = 0; i < a.getComponentCount(); i++) {
			if (bounding.b.get(i) + boundingOffset.get(i) <= a.get(i) + ownOffset.get(i) || bounding.a.get(i) + boundingOffset.get(i) >= b.get(i) + ownOffset.get(i)) return false;
		}
		return true;

		//return !(bounding.b.getX() + boundingOffset.getX() <= a.getX() + ownOffset.getX() || bounding.a.getX() + boundingOffset.getX() >= b.getX() + ownOffset.getX() || bounding.b.getY() + boundingOffset.getY() <= a.getY() + ownOffset.getY() || bounding.a.getY() + boundingOffset.getY() >= b.getY() + ownOffset.getY() || bounding.b.getZ() + boundingOffset.getZ() <= a.getZ() + ownOffset.getZ() || bounding.a.getZ() + boundingOffset.getZ() >= b.getZ() + ownOffset.getZ());
	}

	public boolean intersects(Vectorf<?> point, Vectorf<?> ownOffset) {
		for (int i = 0; i < a.getComponentCount(); i++) {
			if (point.get(i) <= a.get(i) + ownOffset.get(i) || point.get(i) >= b.get(i) + ownOffset.get(i)) return false;
		}
		return true;
	}
}
