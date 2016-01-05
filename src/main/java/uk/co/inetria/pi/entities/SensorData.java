/**
 * 
 */
package uk.co.inetria.pi.entities;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

/**
 * @author omerio
 *
 */
@Cache
@Entity
public class SensorData implements Serializable {

	private static final long serialVersionUID = -22601865333588130L;
	
	@Id
	private Long id;
	
	@Index
	private Date dateTime;
	
	private Double value;
	
	@Index
	private String channel;
	
	
	// ----------------------- Datastore CRUD
	


	public Key<SensorData> save()	{
		return ofy().save().entity(this).now();
	}
	
	
	public static List<SensorData> findAll()	{
		return ofy().load().type(SensorData.class).list();
	}
	
	public static List<SensorData> findByDateTime(Date date)   {
	    
	    /*Calendar calendar = new GregorianCalendar();
	    calendar.add(Calendar.DATE, -30);
	    Date date = calendar.getTime();*/
	    
        return ofy().load().type(SensorData.class)
                .filter("dateTime >=", date).list();
    }

	public static List<SensorData> findByChannelStartsWith(String start) {
		
		return ofy().cache(false).load().type(SensorData.class)
	    		.filter("name >=", start)
	    		.filter("name <=", start + "\ufffd").list();
	}
	
	// -----------------------
	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public Date getDateTime() {
		return dateTime;
	}


	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}


	public Double getValue() {
		return value;
	}


	public void setValue(Double value) {
		this.value = value;
	}


	public String getChannel() {
		return channel;
	}


	public void setChannel(String channel) {
		this.channel = channel;
	}

}
