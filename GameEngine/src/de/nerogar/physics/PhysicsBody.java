package de.nerogar.physics;

import de.nerogar.util.Vectorf;

public class PhysicsBody {
	public BoundingAABB bounding;
	private Vectorf<?> position;

	private Vectorf<?> velocity;
	private Vectorf<?> force;

	public float mass;
	protected float inverseMass;
	public float friction; // 1 = no friction; 0 = all the friction
	public float stiffness; // used for collisions 0 = no bouncing; 1 = all the bouncing

	private boolean isStatic;
	private boolean[] staticAxis;

	public PhysicsBody(BoundingAABB bounding, Vectorf<?> position) {
		this.bounding = bounding;
		this.position = position;
		this.velocity = position.newInstance();
		this.force = position.newInstance();
		staticAxis = new boolean[position.getComponentCount()];

		this.mass = 1f;
		this.inverseMass = 1f;

		this.friction = 0.9f;
		this.stiffness = 0.5f;
	}

	public boolean intersects(PhysicsBody body) {
		return bounding.intersects(body.bounding, position, body.position);
	}
	
	public boolean intersects(Vectorf<?> point) {
		return bounding.intersects(point, position);
	}

	public void setMass(float mass) {
		this.mass = mass;
		this.inverseMass = 1f / mass;
	}

	public void addForce(Vectorf<?> newForce) {
		velocity.add(newForce.multiplied(inverseMass));
	}

	public Vectorf<?> getPosition() {
		return position;
	}

	public Vectorf<?> getForce() {
		recalculateForce();
		return force;
	}

	private void recalculateForce() {
		force.set(velocity.multiplied(mass));
	}

	public Vectorf<?> getVelocity() {
		return velocity;
	}

	public void clearStaticAxis() {
		for (int i = 0; i < staticAxis.length; i++) {
			staticAxis[i] = false;
		}
	}

	public void setStaticInAxis(int axis) {
		staticAxis[axis] = true;
	}

	public void setStatic(boolean isStatic) {
		this.isStatic = isStatic;
	}

	public boolean isStaticInAxis(int axis) {
		return (isStatic) ? true : staticAxis[axis];
	}

}
