/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.co.rpl.sisTest;

import com.alibaba.fastjson.*;
import static com.alibaba.fastjson.JSON.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.*;
import java.net.URLDecoder;
import java.util.Set;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;

/**
 *
 * @author philip
 */
class TeamHandler implements HttpHandler {
    Logger LOG = getLogger(TeamHandler.class);
    private final TeamStore teamStore;
    public TeamHandler(TeamStore teamStore) {
        this.teamStore=teamStore;
    }

    @Override
    public void handle(HttpExchange he) throws IOException {
        try{
            switch(he.getRequestMethod()){
                case "GET":processGet(he); break;
                case "PUT":processPut(he);break;
                case "POST":processPost(he);break;
                case "DELETE":processDelete(he);break;
                default:notFound(he);
            }
        }catch(IOException e){
            LOG.error(e.getMessage(), e);
            internalError(he);
        }catch(JSONException e){
            LOG.error(e.getMessage(), e);
            notUnderstood(he);
        }
    }

    private void processGet(HttpExchange he) throws IOException {
        String[] segments = he.getRequestURI().getPath().split("/");
        if (segments.length==3) {
            String id = segments[2];
            if ("ALL".equals(id))writeOut(he, toJSONString(teamStore.getAll()));
            else{
                LOG.debug("getting {}", id);
                Team team = teamStore.get(id);
                LOG.debug("Got {}", team);
                if (team==null) notFound(he);
                else{
                    LOG.debug("Writing {}", team);
                    writeOut(he, toJSONString(team));
                }
            }
        }else notFound(he);
    }

    private void processPut(HttpExchange he) throws JSONException, IOException{
        LOG.info("Create new entry");
        JSONObject req = readRequest(he);
        LOG.debug("Creating {}", req);
        String name = req.getString("name");
        String owner = req.getString("owner");
        String city = req.getString("city");
        String competition = req.getString("competition");
        if (name != null && owner != null && city != null && competition != null){
            LOG.debug("Creted");
            if (teamStore.nameExists(name))
                alreadyExists(he, teamStore.getId(name));
            Team team = teamStore.create(name, city, owner, competition);
            LOG.debug("Created");
            JSONArray players = req.getJSONArray("players");
            if (players != null){
                Set<String> playerList = team.getTeam();
                players.forEach(pl-> {
                    playerList.add(pl.toString());
                    LOG.debug("Adding player {}", pl);
                });
            }
            writeOut(he, toJSONString(team));
        }else illegalInput(he);
    }

    private void processPost(HttpExchange he) throws IOException {
        JSONObject req = readRequest(he);
        String[] segments = he.getRequestURI().getPath().split("/");
        if (segments.length!=3) illegalInput(he);
        String name = URLDecoder.decode(segments[2], "utf-8");
        String owner = req.getString("owner");
        String city = req.getString("city");
        String competition = req.getString("competition");
        if (name != null){
            if (!teamStore.exists(name)) notExists(he);
            Team team = teamStore.get(name);
            if (owner != null) team.setOwner(owner);
            if (city != null) team.setCity(city);
            if (competition != null) team.setCompetition(competition);
            JSONArray players = req.getJSONArray("players");
            if (players != null){
                Set<String> playerList = team.getTeam();
                playerList.clear();
                players.forEach(pl-> playerList.add(pl.toString()));
            }
            success(he);
        }else illegalInput(he);
    }

    private void processDelete(HttpExchange he) throws IOException {
        String[] segments = he.getRequestURI().getPath().split("/");
        if (segments.length==3) {
            String name = segments[2];
            Team team = teamStore.get(name);
            if (team==null) notFound(he);
            else teamStore.delete(name);
            success(he);
        }else notFound(he);
    }

    private void notFound(HttpExchange he) throws IOException {
        error(he, 404);
    }

    private void writeOut(HttpExchange he, String json) throws IOException {
        LOG.debug("Happy write out {}", json);
        send(he, 200, json);
    }

    private void notUnderstood(HttpExchange he) throws IOException {
        error(he, 400);
    }

    private JSONObject readRequest(HttpExchange he)
    throws IOException{
        try (InputStream is = he.getRequestBody()){
            try(ByteArrayOutputStream bos = new ByteArrayOutputStream()){
                byte[] buf = new byte[1000];
                int len;
                while ((len=is.read(buf))>=0) bos.write(buf, 0, len);
                return JSON.parseObject(bos.toString("utf-8"));
            }
        }
    }

    private void illegalInput(HttpExchange he) throws IOException {
        error(he, 400);
    }

    private void alreadyExists(HttpExchange he, String path) throws IOException {
        he.setAttribute("Location", path);
        error(he, 301);
    }

    private void success(HttpExchange he) throws IOException {
        send(he, 200, "");
    }

    private void notExists(HttpExchange he) throws IOException {
        error(he, 404);
    }

    private void error(HttpExchange he, int httpCode) throws IOException {
        send(he, httpCode, "");
    }

    private void send(HttpExchange he, int httpCode, String payload) 
        throws UnsupportedEncodingException, IOException {
        LOG.debug("Sending {}, {}", httpCode, payload);
        he.setAttribute("Content-Type", "text/json");
        byte[] res = payload.getBytes("utf-8");
        he.sendResponseHeaders(httpCode, res.length==0?-1:res.length);
        if (res.length>0) try(OutputStream os = he.getResponseBody()){
            LOG.debug("Writing "+payload);
            os.write(res);
        }
    }

    private void internalError(HttpExchange he) throws IOException {
        error(he, 500);
    }
    
}
