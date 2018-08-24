package model.renderables;

import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import view.canvas.RailwayCanvas;
import view.railwayimage.RailwayImages;

public class Track extends Renderable {
	private static final long serialVersionUID = 1L;

	private int type;
	private int rotation;
	
	private transient double prevX, prevY;
	private transient double dragX, dragY;
	
	private transient ImageView iv;
	
	public Track(double x, double y, int type) {
		this(x, y, type, 0);
	}
	
	public Track(double x, double y, int type, int rotation) {
		this.type = type;
		this.rotation = rotation;
		prevX = this.x = (x / RailwayCanvas.TRACK_SIZE) * RailwayCanvas.TRACK_SIZE;
		prevY = this.y = (y / RailwayCanvas.TRACK_SIZE) * RailwayCanvas.TRACK_SIZE;
		
		assureImageIsSet();
	}
	
	//track clone
	public Track(Track t) {
		this(t.x, t.y, t.type, t.rotation);
	}

	public void assureImageIsSet() {
		if(iv == null) {
			Image img = RailwayImages.getImageFromID(type);
			iv = new ImageView(img);	
			iv.setX(this.x);
			iv.setY(this.y);
			iv.setRotate(this.rotation);	
			iv.setPickOnBounds(true); //allows mouse event on transparent parts of image
		}
	}
	
	public void isSelected(boolean selected) {
		if(selected) iv.getStyleClass().add("track-selected");
		else iv.getStyleClass().removeAll("track-selected");
	}

	@Override
	public void setX(double x) {
		this.x = x;
		iv.setX(x);
	}

	@Override
	public void setY(double y) {
		this.y = y;
		iv.setY(y);
	}

	public int getRotation() {
		return rotation;
	}
	
	public void rotate(int rotation) {
		this.rotation = this.rotation + rotation % 360;
		iv.setRotate(this.rotation);
	}
	
	public void setTrackImage(ImageView iv) {
		this.iv = iv;
	}
	
	public ImageView getTrackImage() {
		return iv;
	}

	public double getPrevX() {
		return prevX;
	}

	public void setPrevX(double prevX) {
		this.prevX = prevX;
	}

	public double getPrevY() {
		return prevY;
	}

	public void setPrevY(double prevY) {
		this.prevY = prevY;
	}
	
	public double getDragX() {
		return dragX;
	}
	
	public void setDragX(double x) {
		this.dragX = x;
	}
	
	public double getDragY() {
		return dragY;
	}

	public void setDragY(double y) {
		this.dragY = y;
	}
	
	public void setOnMousePressAction(EventHandler<MouseEvent> e) {
		iv.setOnMousePressed(e);
	}
	
	public void setOnMouseDragAction(EventHandler<MouseEvent> e) {
		iv.setOnMouseDragged(e);
	}
	
	public void setOnMouseReleaseAction(EventHandler<MouseEvent> e) {
		iv.setOnMouseReleased(e);
	}
	
}
