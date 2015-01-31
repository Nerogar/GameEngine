package de.nerogar.render;

public class ScreenProperties {

	public static final ScreenProperties defaultInstance;

	private int renderWidth = 800;
	private int renderHeight = 600;

	protected float fov = 90f;
	protected boolean orthographic = false;

	protected BaseCamera camera = null;
	protected RenderTarget renderTarget = null;

	public ScreenProperties() {
	}

	public void setScreenDimension(int renderWidth, int renderHeight) {
		this.renderWidth = renderWidth;
		this.renderHeight = renderHeight;
	}

	public void setRenderTarget(RenderTarget renderTarget) {
		this.renderTarget = renderTarget;
		renderTarget.setScreenProperties(this);
	}

	public int getRenderWidth() {
		return renderWidth;
	}

	public int getRenderHeight() {
		return renderHeight;
	}

	public ScreenProperties(float fov, boolean orthographic) {
		this.fov = fov;
		this.orthographic = orthographic;
	}

	public void setFov(float fov) {
		this.fov = fov;
	}

	public void setOrthographic(boolean orthographic) {
		this.orthographic = orthographic;
	}

	public void setCamera(BaseCamera camera) {
		this.camera = camera;
	}

	static {
		defaultInstance = new ScreenProperties();
	}
}
