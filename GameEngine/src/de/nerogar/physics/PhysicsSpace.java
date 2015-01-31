package de.nerogar.physics;

import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;

import de.nerogar.render.Texture2D;
import de.nerogar.render.TextureLoader;
import de.nerogar.util.*;

public class PhysicsSpace {
	private final Vectorf<?> DIMENSION;

	private ArrayList<PhysicsBody> staticBodys;
	private ArrayList<PhysicsBody> bodies;

	private final float NOT_MOVING_THRESHOLD = 0.001f;

	//TODO remove
	public Vectorf<?> gravity = new Vector3f(0f, -10f, 0f);
	private Texture2D debugTexture;

	public PhysicsSpace(Vectorf<?> dimension) {
		this.DIMENSION = dimension;
		staticBodys = new ArrayList<PhysicsBody>();
		bodies = new ArrayList<PhysicsBody>();

		debugTexture = TextureLoader.loadTexture("res/textures/test3.png", "debug");
	}

	public void addStaticBody(PhysicsBody staticBody) {
		staticBody.setStatic(true);
		staticBodys.add(staticBody);
	}

	public void addBody(PhysicsBody body) {
		body.setStatic(false);
		bodies.add(body);
	}

	public void clear() {
		staticBodys.clear();
		bodies.clear();
	}

	public void update(float timeDelta) {
		//TODO debug start
		for (PhysicsBody pb : bodies) {
			//System.out.print(pb.getPosition().get(1) + ";  " + pb.isStaticInAxis(1) + " | ");
		}
		//System.out.println();
		//debug end

		Vectorf<?> resetPos = DIMENSION.newInstance(); //to reset after intersection calculation
		Vectorf<?> calculationVel = DIMENSION.newInstance(); //to calculate collisions

		for (PhysicsBody body : bodies) {
			//TODO calc static (broken)
			body.clearStaticAxis();

			resetPos.set(body.getPosition());

			//TODO add all modifier
			body.getVelocity().add((getGravity().multiplied(timeDelta)));
			body.getPosition().add(body.getVelocity().multiplied(timeDelta));

			calculationVel.set(body.getVelocity());
			ArrayList<PhysicsBody> frictionBodies = getIntersecting(body); //all colliding bodies
			ArrayList<InteractingBody> interactingBodies = new ArrayList<InteractingBody>(); //all colliding bodies in a wrapper class

			for (PhysicsBody frictionBody : frictionBodies) {
				InteractingBody interactingBody = new InteractingBody();
				interactingBody.interactingDirection = getIntersectionDirection(body, calculationVel, frictionBody);
				interactingBody.body = frictionBody;
				interactingBodies.add(interactingBody);

				if (frictionBody.isStaticInAxis(interactingBody.interactingDirection)) {
					//resetPos.set(interactingBody.interactingDirection, CollisionResolver.snap(body, calculationVel, interactingBody));
					//body.getVelocity().set(interactingBody.interactingDirection, 0f);
					//body.setStaticInAxis(interactingBody.interactingDirection);
				}
			}

			body.getPosition().set(resetPos);
			body.getPosition().add(body.getVelocity().multiplied(timeDelta));

			ArrayList<PhysicsBody> collidingBodies = getIntersecting(body);

			for (PhysicsBody collidingBody : collidingBodies) {
				InteractingBody interactingBody = null;
				for (InteractingBody temp : interactingBodies) {
					if (temp.equals(collidingBody)) {
						interactingBody = temp;
					}
				}

				if (interactingBody == null) {
					interactingBody = new InteractingBody();
					interactingBody.body = collidingBody;
					interactingBody.interactingDirection = getIntersectionDirection(body, calculationVel, collidingBody);
				}

				//no real calculation, just an attempt to guess if the collision is just sliding over an object
				boolean isCollision = Math.abs(getGravity().get(interactingBody.interactingDirection) * timeDelta) * 2 > Math.abs(body.getVelocity().get(interactingBody.interactingDirection));

				if (isCollision) interactingBody.collision = true;
			}

			if (interactingBodies.size() > 0) CollisionResolver.resolve(body, interactingBodies, calculationVel, timeDelta);

			//stop if moving to slow
			for (int i = 0; i < body.getVelocity().getComponentCount(); i++) {
				if (Math.abs(body.getVelocity().get(i)) < NOT_MOVING_THRESHOLD) {
					body.getVelocity().set(i, 0f);
				}
			}

			//updateBody(timeDelta, body);
		}
	}

	private void updateBody(float timeDelta, PhysicsBody body) {
		//body.getVelocity().add((getGravity().multiplied(timeDelta)));
		//body.getPosition().add(body.getVelocity().multiplied(timeDelta));
		/*ArrayList<PhysicsBody> intersectingBodies = getIntersecting(body);

		if (intersectingBodies.size() != 0) {
			CollisionResolver.resolve(body, intersectingBodies, timeDelta);
		}

		for (int i = 0; i < body.getVelocity().getComponentCount(); i++) {
			if (Math.abs(body.getVelocity().get(i)) < NOT_MOVING_THRESHOLD) {
				body.getVelocity().set(i, 0f);
			}
		}*/

	}

	private ArrayList<PhysicsBody> getIntersecting(PhysicsBody body) {
		ArrayList<PhysicsBody> intersectingBodies = new ArrayList<PhysicsBody>();

		for (PhysicsBody staticBody : staticBodys) {
			if (body.intersects(staticBody)) {
				intersectingBodies.add(staticBody);
			}
		}

		for (PhysicsBody listBody : bodies) {
			if (listBody != body && body.intersects(listBody)) {
				intersectingBodies.add(listBody);
			}
		}

		return intersectingBodies;
	}

	/*public RayIntersection getIntersecting(Ray ray) {
		ArrayList<RayIntersection> intersectingBodies = new ArrayList<RayIntersection>();

		for (PhysicsBody staticBody : staticBodys) {
			RayIntersection intersection = ray.getIntersectionPoint(staticBody);
			if (intersection != null) {
				intersectingBodies.add(intersection);
			}
		}

		for (PhysicsBody listBody : bodies) {
			RayIntersection intersection = ray.getIntersectionPoint(listBody);
			if (intersection != null) {
				intersectingBodies.add(intersection);
			}
		}

		//RayIntersection[] intersections
		//Collections.sort(intersectingBodies);
		//for (RayIntersection ri : intersectingBodies) {
		//	System.out.println(ri.intersectionPoint);
		//}

		if (intersectingBodies.size() != 0) {
			return intersectingBodies.get(0);
		} else {
			return null;
		}
	}*/

	public RayIntersection getIntersecting(Ray ray) {
		ArrayList<RayIntersection> intersectingBodies = new ArrayList<RayIntersection>();

		//TODO change to partitioned space
		for (PhysicsBody staticBody : staticBodys) {
			RayIntersection intersection = ray.getIntersectionPoint(staticBody);
			if (intersection != null) {
				intersectingBodies.add(intersection);
			}
		}

		for (PhysicsBody listBody : bodies) {
			RayIntersection intersection = ray.getIntersectionPoint(listBody);
			if (intersection != null) {
				intersectingBodies.add(intersection);
			}
		}

		//RayIntersection[] intersections
		/*Collections.sort(intersectingBodies);
		for (RayIntersection ri : intersectingBodies) {
			System.out.println(ri.intersectionPoint);
		}*/

		if (intersectingBodies.size() != 0) {
			return intersectingBodies.get(0);
		} else {
			return null;
		}
	}

	public PhysicsBody getIntersecting(Vectorf<?> point) {
		//TODO change to partitioned space
		for (PhysicsBody staticBody : staticBodys) {
			if (staticBody.intersects(point)) { return staticBody; }
		}

		for (PhysicsBody listBody : bodies) {
			if (listBody.intersects(point)) { return listBody; }
		}

		return null;
	}

	public static int getIntersectionDirection(PhysicsBody movingBody, Vectorf<?> velocity, PhysicsBody intersectedBody) {
		Vector3f intersectionVolume = new Vector3f();

		for (int axis = 0; axis < velocity.getComponentCount(); axis++) {
			if (velocity.get(axis) < 0) {
				intersectionVolume.set(axis, (intersectedBody.getPosition().get(axis) + intersectedBody.bounding.b.get(axis)) - (movingBody.getPosition().get(axis) + movingBody.bounding.a.get(axis)));
			} else {
				intersectionVolume.set(axis, (movingBody.getPosition().get(axis) + movingBody.bounding.b.get(axis)) - (intersectedBody.getPosition().get(axis) + intersectedBody.bounding.a.get(axis)));
			}
			if (intersectionVolume.get(axis) != 0f) intersectionVolume.set(axis, Math.abs(velocity.get(axis)) / intersectionVolume.get(axis));
		}

		return VectorTools.getGreatestComponentIndex(intersectionVolume);
	}

	private Vectorf<?> getGravity() {
		return gravity;
	}

	public void render() {
		for (PhysicsBody body : staticBodys) {
			//renderAABB(body.bounding, body.getPosition(), 1f, 1f, 1f);
			renderTop(body.bounding, body.getPosition());
		}

		for (PhysicsBody body : bodies) {
			//renderAABB(body.bounding, body.getPosition(), 1f, 0f, 0f);
			renderTop(body.bounding, body.getPosition());
		}

	}

	private void renderAABB(BoundingAABB boundAABB, Vector3f offset, float r, float g, float b) {
		glColor3f(r, g, b);

		glPushMatrix();
		glTranslatef(offset.get(0), offset.get(1), offset.get(2));

		glBegin(GL_LINES);

		//top
		glVertex3f(boundAABB.a.get(0), boundAABB.a.get(1), boundAABB.a.get(2));
		glVertex3f(boundAABB.b.get(0), boundAABB.a.get(1), boundAABB.a.get(2));

		glVertex3f(boundAABB.b.get(0), boundAABB.a.get(1), boundAABB.a.get(2));
		glVertex3f(boundAABB.b.get(0), boundAABB.a.get(1), boundAABB.b.get(2));

		glVertex3f(boundAABB.b.get(0), boundAABB.a.get(1), boundAABB.b.get(2));
		glVertex3f(boundAABB.a.get(0), boundAABB.a.get(1), boundAABB.b.get(2));

		glVertex3f(boundAABB.a.get(0), boundAABB.a.get(1), boundAABB.b.get(2));
		glVertex3f(boundAABB.a.get(0), boundAABB.a.get(1), boundAABB.a.get(2));

		//bottom
		glVertex3f(boundAABB.a.get(0), boundAABB.b.get(1), boundAABB.a.get(2));
		glVertex3f(boundAABB.b.get(0), boundAABB.b.get(1), boundAABB.a.get(2));

		glVertex3f(boundAABB.b.get(0), boundAABB.b.get(1), boundAABB.a.get(2));
		glVertex3f(boundAABB.b.get(0), boundAABB.b.get(1), boundAABB.b.get(2));

		glVertex3f(boundAABB.b.get(0), boundAABB.b.get(1), boundAABB.b.get(2));
		glVertex3f(boundAABB.a.get(0), boundAABB.b.get(1), boundAABB.b.get(2));

		glVertex3f(boundAABB.a.get(0), boundAABB.b.get(1), boundAABB.b.get(2));
		glVertex3f(boundAABB.a.get(0), boundAABB.b.get(1), boundAABB.a.get(2));

		//connection

		glVertex3f(boundAABB.a.get(0), boundAABB.a.get(1), boundAABB.a.get(2));
		glVertex3f(boundAABB.a.get(0), boundAABB.b.get(1), boundAABB.a.get(2));

		glVertex3f(boundAABB.b.get(0), boundAABB.a.get(1), boundAABB.a.get(2));
		glVertex3f(boundAABB.b.get(0), boundAABB.b.get(1), boundAABB.a.get(2));

		glVertex3f(boundAABB.b.get(0), boundAABB.a.get(1), boundAABB.b.get(2));
		glVertex3f(boundAABB.b.get(0), boundAABB.b.get(1), boundAABB.b.get(2));

		glVertex3f(boundAABB.a.get(0), boundAABB.a.get(1), boundAABB.b.get(2));
		glVertex3f(boundAABB.a.get(0), boundAABB.b.get(1), boundAABB.b.get(2));

		glEnd();

		glPopMatrix();

		glColor3f(1f, 1f, 1f);
	}

	private void renderTop(BoundingAABB boundAABB, Vectorf<?> offset) {
		glPushMatrix();
		glTranslatef(offset.get(0), offset.get(1), offset.get(2));

		//glEnable(GL_TEXTURE_2D);
		if (debugTexture != null) debugTexture.bind();
		glBegin(GL_QUADS);

		//top
		glNormal3f(0f, 1f, 0f);
		glTexCoord2f(0f, 0f);
		glVertex3f(boundAABB.a.get(0), boundAABB.b.get(1), boundAABB.a.get(2));
		glTexCoord2f(0f, 1f);
		glVertex3f(boundAABB.a.get(0), boundAABB.b.get(1), boundAABB.b.get(2));
		glTexCoord2f(1f, 1f);
		glVertex3f(boundAABB.b.get(0), boundAABB.b.get(1), boundAABB.b.get(2));
		glTexCoord2f(1f, 0f);
		glVertex3f(boundAABB.b.get(0), boundAABB.b.get(1), boundAABB.a.get(2));

		//left
		glNormal3f(-1f, 0f, 0f);
		glTexCoord2f(0f, 0f);
		glVertex3f(boundAABB.a.get(0), boundAABB.a.get(1), boundAABB.a.get(2));
		glTexCoord2f(0f, 1f);
		glVertex3f(boundAABB.a.get(0), boundAABB.a.get(1), boundAABB.b.get(2));
		glTexCoord2f(1f, 1f);
		glVertex3f(boundAABB.a.get(0), boundAABB.b.get(1), boundAABB.b.get(2));
		glTexCoord2f(1f, 0f);
		glVertex3f(boundAABB.a.get(0), boundAABB.b.get(1), boundAABB.a.get(2));

		//right
		glNormal3f(1f, 0f, 0f);
		glTexCoord2f(0f, 0f);
		glVertex3f(boundAABB.b.get(0), boundAABB.a.get(1), boundAABB.a.get(2));
		glTexCoord2f(0f, 1f);
		glVertex3f(boundAABB.b.get(0), boundAABB.b.get(1), boundAABB.a.get(2));
		glTexCoord2f(1f, 1f);
		glVertex3f(boundAABB.b.get(0), boundAABB.b.get(1), boundAABB.b.get(2));
		glTexCoord2f(1f, 0f);
		glVertex3f(boundAABB.b.get(0), boundAABB.a.get(1), boundAABB.b.get(2));

		//front
		glNormal3f(0f, 0f, 1f);
		glTexCoord2f(0f, 0f);
		glVertex3f(boundAABB.a.get(0), boundAABB.a.get(1), boundAABB.b.get(2));
		glTexCoord2f(0f, 1f);
		glVertex3f(boundAABB.b.get(0), boundAABB.a.get(1), boundAABB.b.get(2));
		glTexCoord2f(1f, 1f);
		glVertex3f(boundAABB.b.get(0), boundAABB.b.get(1), boundAABB.b.get(2));
		glTexCoord2f(1f, 0f);
		glVertex3f(boundAABB.a.get(0), boundAABB.b.get(1), boundAABB.b.get(2));

		//back
		glNormal3f(0f, 0f, -1f);
		glTexCoord2f(0f, 0f);
		glVertex3f(boundAABB.a.get(0), boundAABB.a.get(1), boundAABB.a.get(2));
		glTexCoord2f(0f, 1f);
		glVertex3f(boundAABB.a.get(0), boundAABB.b.get(1), boundAABB.a.get(2));
		glTexCoord2f(1f, 1f);
		glVertex3f(boundAABB.b.get(0), boundAABB.b.get(1), boundAABB.a.get(2));
		glTexCoord2f(1f, 0f);
		glVertex3f(boundAABB.b.get(0), boundAABB.a.get(1), boundAABB.a.get(2));

		glEnd();
		//glDisable(GL_TEXTURE_2D);

		glPopMatrix();
	}

}
