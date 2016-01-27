/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.co.rpl.sisTest;

import java.util.*;

/**
 *
 * @author philip
 */
public class Team {
   private final String id;
   private String name;
   private String city;
   private String owner;
   private String competition;
   private final Set<String> players;
   private final Date creation;

    public Team(String id, String name, String city, String owner, String competition) {
        this.id=id;
        this.name = name;
        this.city = city;
        this.owner = owner;
        this.competition = competition;
        creation = new Date();
        players=new HashSet<>();
    }

    public synchronized String getId() {
        return id;
    }

    public synchronized void setCity(String city) {
        this.city = city;
    }

    public synchronized void setOwner(String owner) {
        this.owner = owner;
    }

    public synchronized void setCompetition(String competition) {
        this.competition = competition;
    }

    public synchronized String getName() {
        return name;
    }

    public synchronized String getCity() {
        return city;
    }

    public synchronized String getOwner() {
        return owner;
    }

    public synchronized String getCompetition() {
        return competition;
    }

    public synchronized Set<String> getPlayers() {
        return players;
    }

    public synchronized Date getCreation() {
        return creation;
    }

    @Override
    public synchronized String toString() {
        return "Team{" + "id=" + id + ", name=" + name + ", city=" + city + ", owner=" + owner + ", competition=" + competition + ", players=" + players + ", creation=" + creation + '}';
    }


    
   
}
