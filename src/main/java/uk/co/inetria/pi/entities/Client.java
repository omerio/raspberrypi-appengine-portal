/**
 * 
 */
package uk.co.inetria.pi.entities;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.List;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

/**
 * Represent a client connected to the Web UI. We use the id to push messages to clients
 * using the Channel API
 * 
 * @author omerio
 *
 */
@Cache
@Entity
public class Client {
    
    @Id
    private String id;
    
    public Client() {
        super();
    }
    
    public Client(String id) {
        super();
        this.id = id;
    }
    
    //------- CRUD

    public static List<Client> findAll()  {
        return ofy().cache(false).load().type(Client.class).list();
    }

    // cached
    public static Client findById(String id) {
        return ofy().load().type(Client.class).id(id).now();
    }
    
    public static void remove(String id) {
        Key<Client> key = Key.create(Client.class, id);
        ofy().delete().key(key).now();
    }

    public Key<Client> save() {
        return ofy().save().entity(this).now();
    }
    
    public void remove()    {
        ofy().delete().entity(this).now();
    }
    
  //-------

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
