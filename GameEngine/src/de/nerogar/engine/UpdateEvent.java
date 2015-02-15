package de.nerogar.engine;

import java.util.List;

public interface UpdateEvent {

	public void process(List<UpdateEventInterface> connections);

}
