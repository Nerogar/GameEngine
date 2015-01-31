package de.nerogar.engine;

import de.nerogar.render.Camera;

public abstract class BaseController {

	protected BaseWorld world;
	protected Camera camera;

	public BaseController(BaseWorld world, Camera camera) {
		this.world = world;
		this.camera = camera;
		init(world);
	}

	protected abstract void init(BaseWorld world);

	public abstract void update(float timeDelta);

}
