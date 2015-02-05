package de.nerogar.render;

public class ScreenProperties {

	public static final ScreenProperties defaultInstance;

	private int renderWidth = 800;
	private int renderHeight = 600;

	protected float fov = 90f;
	protected boolean orthographic = false;

	protected BaseCamera camera = null;
	protected RenderTarget renderTarget = null;

	protected boolean enableDepthTest = true;

	protected float clearColorR = 0.0f;
	protected float clearColorG = 0.0f;
	protected float clearColorB = 0.0f;
	protected float clearColorA = 1.0f;

	public ScreenProperties() {
	}

	public void setScreenDimension(int renderWidth, int renderHeight) {
		this.renderWidth = renderWidth;
		this.renderHeight = renderHeight;
	}

	public void setClearColor(float red, float green, float blue, float alpha) {
		this.clearColorR = red;
		this.clearColorG = green;
		this.clearColorB = blue;
		this.clearColorA = alpha;
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

	public void setDepthTest(boolean enableDepthTest) {
		this.enableDepthTest = enableDepthTest;
	}

	static {
		defaultInstance = new ScreenProperties();
	}
}
