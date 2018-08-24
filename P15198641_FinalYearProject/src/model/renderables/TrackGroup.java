package model.renderables;

import java.util.Iterator;
import java.util.LinkedList;

public class TrackGroup extends Renderable implements Iterable<Track> {
	private static final long serialVersionUID = 1L;
	
	private LinkedList<Track> tracks;
	
	public TrackGroup() {
		tracks = new LinkedList<>();
	}
	
	public void add(Track t) {
		tracks.add(t);
	}
	
	public void remove(Track t) {
		tracks.remove(t);
	}
	
	public void isSelected(boolean selected) {
		for(Track t : tracks) t.isSelected(selected);
	}

	public Iterator<Track> iterator() {
		return tracks.iterator();
	}

}
