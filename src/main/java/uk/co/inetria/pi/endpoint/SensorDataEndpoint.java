package uk.co.inetria.pi.endpoint;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.appengine.api.channel.ChannelMessage;
import com.google.appengine.api.channel.ChannelService;
import com.google.appengine.api.channel.ChannelServiceFactory;
import com.google.appengine.api.users.User;
import com.google.gson.Gson;

import uk.co.inetria.pi.entities.Client;
import uk.co.inetria.pi.entities.SensorData;


/**
  * Add your first API methods in this class, or you may create another class. In that case, please
  * update your web.xml accordingly.
 **/
@Api(
		name = "sensor",
		version = "v1",
		scopes = {Constants.EMAIL_SCOPE},
		clientIds = {Constants.WEB_CLIENT_ID, Constants.ANDROID_CLIENT_ID, Constants.IOS_CLIENT_ID},
		audiences = {Constants.ANDROID_AUDIENCE}
)
public class SensorDataEndpoint {
	
	private static final Logger log = Logger.getLogger(SensorDataEndpoint.class.getName());
	
	private static final Gson GSON = new Gson();
	
	
	@ApiMethod(name = "data.create", httpMethod = "post")
	public SensorData create(SensorData data, User user) {
		
	    // check the user is authenticated and authorised
		if(user == null) {
			log.warning("User is not authenticated");
			throw new RuntimeException("Authentication required!");
			
		} else if(!Constants.EMAIL_ADDRESS.equals(user.getEmail())) {
			
			log.warning("User is not authorised, email: " + user.getEmail());
			throw new RuntimeException("Not authorised!");
		}
		
		data.save();
		
		try {
		    // notify the client channels
		    ChannelService channelService = ChannelServiceFactory.getChannelService();

		    List<Client> clients = Client.findAll();
		    String json = GSON.toJson(data);

		    for(Client client: clients) {
		        channelService.sendMessage(new ChannelMessage(client.getId(), json));
		    }
		    
		} catch(Exception e) {
		    log.log(Level.SEVERE, "Failed to notify connected clients", e);
		}
			
		return data;
	}	
	
}
