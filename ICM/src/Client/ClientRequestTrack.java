package Client;

import java.util.Observable;
import java.util.Observer;

import Boundary.RequestInfoController;
import Boundary.RequestTrackController;
import Entity.Request;
import Entity.RequestPhase;
import javafx.application.Platform;
import ocsf.client.ObservableClient;;
/**
 * 
 * in this observer we show the track information about the request to the initiator
 */
public class ClientRequestTrack implements Observer {

	public ClientRequestTrack(ObservableClient client) {
		client.addObserver(this);
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		if (arg1 instanceof Object[]) {
			Object[] arg2 = (Object[]) arg1;
			if (arg2[0] instanceof String) {
				String keymessage = (String) arg2[0];
				if (keymessage.equals("Track request")) {
					if (arg2[1] instanceof RequestPhase) {
						RequestPhase requestphase = (RequestPhase) arg2[1];
						Platform.runLater(new Runnable() {
							@Override
							public void run() {
								RequestTrackController.RequestTrack.SetTrack(requestphase);
							}
						});
					}

				}
			}
		}

	}

}
