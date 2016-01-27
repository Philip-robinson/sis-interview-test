/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.co.rpl.sisTest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sun.net.httpserver.HttpExchange;
import java.io.*;
import java.net.URI;
import java.net.URLEncoder;
import org.apache.log4j.*;
import static org.easymock.EasyMock.*;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author philip
 */
public class TeamHandlerTest {
    
    TeamHandler inst;
    HttpExchange he;
    String CITY="The citY";
    String OWNER="The MAN";
    String COMPETITION="The citY";
    String NAME="SOme name or other";
    String BODY = "{"+
                  "\"city\":\""+CITY+"\","+
                  "\"owner\":\""+OWNER+"\","+
                  "\"competition\":\""+COMPETITION+"\","+
                  "\"name\":\""+NAME+"\"}";
    InputStream body;
    TeamStore teamStore;
    Team team;
    final String ID="27";
    public TeamHandlerTest() {
        BasicConfigurator.resetConfiguration();
        BasicConfigurator.configure();
        Logger.getRootLogger().setLevel(Level.DEBUG);
    }
    
    @Before
    public void setUp() throws UnsupportedEncodingException {
        body = new ByteArrayInputStream(BODY.getBytes("utf-8"));
        he = createMock("he", HttpExchange.class);
        replay(he);

        teamStore = createMock(TeamStore.class);
        replay(teamStore);
        inst=new TeamHandler(teamStore);
        team = new Team(ID, NAME, CITY, OWNER, COMPETITION);
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of handle method, of class TeamHandler.
     */
    @Test
    public void testHandlePUT() throws Exception {
        System.out.println("handle PUT");
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        reset(he);
        expect(he.getRequestMethod()).andReturn("PUT");
        expect(he.getRequestBody()).andReturn(body);
        he.setAttribute("Content-Type", "text/json");
        expect(he.getResponseBody()).andReturn(output);
        he.sendResponseHeaders(eq(200), anyInt());
        replay(he);
        reset(teamStore);
        expect(teamStore.nameExists(NAME)).andReturn(false);
        expect(teamStore.create(NAME, CITY, OWNER, COMPETITION)).andReturn(team);
        replay(teamStore);
        inst.handle(he);
        verify(teamStore);
        verify(he);
    }
    @Test
    public void testHandleGET() throws Exception {
        System.out.println("handle GET");

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        reset(he);
        expect(he.getRequestMethod()).andReturn("GET");
        he.setAttribute("Content-Type", "text/json");
        expect(he.getResponseBody()).andReturn(output);
        he.sendResponseHeaders(eq(200), anyInt());
        expect(he.getRequestURI()).andReturn(new URI("/team/"+ID));
        replay(he);
        reset(teamStore);
        expect(teamStore.get(ID)).andReturn(team);
        replay(teamStore);
        inst.handle(he);
        System.out.println("OUTPUT: "+output.toString("utf-8"));
        JSONObject job = JSON.parseObject(output.toString("utf-8"));
        assertEquals(NAME, job.getString("name"));
        verify(teamStore);
        verify(he);
    }
    
    @Test
    public void testHandlePOST() throws Exception {
        System.out.println("handle POST");

        reset(he);
        expect(he.getRequestMethod()).andReturn("POST");
        he.setAttribute("Content-Type", "text/json");
        he.sendResponseHeaders(200, -1);
        expect(he.getRequestBody()).andReturn(body);
        expect(he.getRequestURI()).andReturn(new URI("/team/"+ID));
        replay(he);
        Team team2 = createMock(Team.class);
        team2.setCity(CITY);
        team2.setCompetition(COMPETITION);
        team2.setOwner(OWNER);
        replay(team2);
        reset(teamStore);
        expect(teamStore.exists(ID)).andReturn(true);
        expect(teamStore.get(ID)).andReturn(team2);
        replay(teamStore);
        inst.handle(he);
        verify(teamStore);
        verify(he);
    }
    
}
