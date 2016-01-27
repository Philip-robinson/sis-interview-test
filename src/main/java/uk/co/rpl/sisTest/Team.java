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
   private final Set<String> team;
   private final Date creation;

    public Team(String id, String name, String city, String owner, String competition) {
        this.id=id;
        this.name = name;
        this.city = city;
        this.owner = owner;
        this.competition = competition;
        creation = new Date();
        team=new HashSet<>();
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void setCompetition(String competition) {
        this.competition = competition;
    }

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    public String getOwner() {
        return owner;
    }

    public String getCompetition() {
        return competition;
    }

    public Set<String> getTeam() {
        return team;
    }

    public Date getCreation() {
        return creation;
    }

    @Override
    public String toString() {
        return "Team{" + "id=" + id + ", name=" + name + ", city=" + city + ", owner=" + owner + ", competition=" + competition + ", team=" + team + ", creation=" + creation + '}';
    }


    
   
}
