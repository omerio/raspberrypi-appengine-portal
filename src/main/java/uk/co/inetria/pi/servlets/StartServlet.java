package uk.co.inetria.pi.servlets;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.appengine.api.channel.ChannelService;
import com.google.appengine.api.channel.ChannelServiceFactory;
import com.google.gson.Gson;

import uk.co.inetria.pi.entities.Client;
import uk.co.inetria.pi.entities.SensorData;


/**
 * Servlet implementation class StartServlet
 */
public class StartServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final Logger log = Logger.getLogger(StartServlet.class.getName());

    private static final String CONTENT_TYPE_JSON = "application/x-json";

    private static final Gson GSON = new Gson();

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // get the session 
        HttpSession session = request.getSession();
        String clientId = session.getId();

        log.info("Sending data to client " + clientId);

        Client client = Client.findById(clientId);

        String token = null;

        if(client == null) {

            ChannelService channelService = ChannelServiceFactory.getChannelService();
            log.info("Creating channel for clientId " + clientId);
            // open the channel
            token = channelService.createChannel(clientId);

            client = new Client(clientId, token);
            client.save();
        }
        
     // use the session id and the channel id for this client
        Map<String, Object> data = new HashMap<>();
        token = client.getToken();
        data.put("token", token);

        //createTestData();
        int minutes = 1;
        String timeRange = request.getParameter("timeRange");

        try {
            minutes = Integer.parseInt(timeRange);
            
        } catch(NumberFormatException e) {
            log.warning("Failed to parse number " + timeRange);
        }

        Calendar calendar = new GregorianCalendar();
        calendar.add(Calendar.MINUTE, -minutes);
        Date date = calendar.getTime();
        
        List<SensorData> sensorDataList = SensorData.findByDateTime(date);

        data.put("data", sensorDataList);

        response.setContentType(CONTENT_TYPE_JSON);
        response.getWriter().print(GSON.toJson(data));

    }

    
    /**
     * Used for testing if you don't have actual sensors
     */
    @SuppressWarnings("unused")
    private void createTestData() {
        
        Calendar calendar = new GregorianCalendar();

        for(int i = 0; i < 20; i++) {

            calendar.add(Calendar.MINUTE, -30);
            Date date = calendar.getTime();

            SensorData sensor = new SensorData();
            sensor.setChannel("temperature");
            sensor.setDateTime(date);
            sensor.setValue(10 + i * Math.random());
            sensor.save();

            sensor = new SensorData();
            sensor.setChannel("voltage");
            sensor.setDateTime(date);
            sensor.setValue(2 + Math.random());
            sensor.save();

            sensor = new SensorData();
            sensor.setChannel("illuminance");
            sensor.setDateTime(date);
            sensor.setValue(300 + i * Math.random());
            sensor.save();
        }
    }

}
