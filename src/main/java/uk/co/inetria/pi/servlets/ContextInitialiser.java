/**
 * 
 */
package uk.co.inetria.pi.servlets;

import java.util.logging.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.googlecode.objectify.ObjectifyService;

import uk.co.inetria.pi.entities.Client;
import uk.co.inetria.pi.entities.SensorData;

/**
 * @author omerio
 *
 */
public class ContextInitialiser implements ServletContextListener {
	
	private static final Logger log = Logger.getLogger(ContextInitialiser.class.getName());

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
	 */
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		
		ObjectifyService.register(SensorData.class);
		ObjectifyService.register(Client.class);
		log.info("Context created");
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
	 */
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub

	}

}
