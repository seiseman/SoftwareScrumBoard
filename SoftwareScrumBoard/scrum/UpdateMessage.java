package scrum;

import java.io.Serializable;

public abstract class UpdateMessage implements Serializable {

	/**
	 * It wants us to add a private static final long, but do we want each inherited class to have its one serializedID,
	 * or do casting on the server side?
	 */
	private static final long serialVersionUID = 1L;



}
