package model.renderables;

import java.io.Serializable;

public abstract class Renderable implements Serializable {
	private static final long serialVersionUID = 1L;
	
	protected double x, y;
	
	public abstract void isSelected(boolean selected);
	
	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}
}
