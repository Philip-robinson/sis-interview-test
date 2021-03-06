/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.co.rpl.sisTest;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author philip
 */
public class TeamStore {
    private Map<String, Team> store = new HashMap<>();
    private Map<String, String> nameToId = new HashMap<>();
    private AtomicInteger id=new AtomicInteger(1);

    
    public Team get(String id){
        return store.get(id);
    }

    public synchronized Team create(String name, String city, 
                       String owner, String competition){
        String id =nameToId.get(name);
        if (id==null) id = ""+ this.id.incrementAndGet();
        Team t = new Team(id, name, city, owner, competition) ;
        store.put(id, t);
        nameToId.put(name, id);
        return t;
    }

    public synchronized Map<String, Team> getAll() {
        return new HashMap(store);
    }

    public synchronized boolean exists(String id) {
        return store.get(id) != null;
    }
    public synchronized boolean nameExists(String name) {
        return nameToId.get(name) != null;
    }

    public synchronized void delete(String id) {
        store.remove(id);
    }

    public synchronized String getId(String name) {
        return nameToId.get(name);
    }
}
