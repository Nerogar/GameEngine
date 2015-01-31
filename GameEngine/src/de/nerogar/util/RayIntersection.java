package de.nerogar.util;

import de.nerogar.physics.PhysicsBody;
import de.nerogar.util.Vector3f;

public class RayIntersection implements Comparable<RayIntersection> {
	public Vector3f intersectionPoint;
	public float distance;
	public PhysicsBody intersectingBody;

	public RayIntersection(Vector3f intersectionPoint, float distance, PhysicsBody intersectingBody) {
		this.intersectionPoint = intersectionPoint;
		this.distance = distance;
		this.intersectingBody = intersectingBody;
	}

	@Override
	public int compareTo(RayIntersection obj) {
		return distance - obj.distance < 0 ? -1 : 1;
	}

}
