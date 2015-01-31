package de.nerogar.physics;

public class InteractingBody {
	public PhysicsBody body;
	public int interactingDirection;
	public boolean collision;

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof PhysicsBody) return (((PhysicsBody) obj) == body);
		return false;
	}
}
