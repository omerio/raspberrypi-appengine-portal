package uk.co.inetria.pi.servlets;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.channel.ChannelPresence;
import com.google.appengine.api.channel.ChannelService;
import com.google.appengine.api.channel.ChannelServiceFactory;

import uk.co.inetria.pi.entities.Client;

/**
 * Channel handler tracking client connections and disconnections
 * @see https://cloud.google.com/appengine/docs/java/channel/#Java_Tracking_client_connections_and_disconnections
 * @author omerio
 */
public class ChannelHandler extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	private static final Logger log = Logger.getLogger(ChannelHandler.class.getName());

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// In the handler for _ah/channel/connected/
		ChannelService channelService = ChannelServiceFactory.getChannelService();
		ChannelPresence presence = channelService.parsePresence(request);
		
		String clientId = presence.clientId();
		boolean connected = presence.isConnected();
		
		log.info("Client = " + clientId + ", connected = " + connected);
		
		if(!connected) {
			// remove them from the datastore
		    Client.remove(clientId);
		}
	}

}
