package models;

import java.io.Serializable;

/**
 * Created by Talal Mahmood on 11/02/2016.
 * SID 5296251
 * Coventry University
 */

public class Network implements Serializable
{
    private static final long SERIAL_VERSION_UID = 50L;
    private int id;
    private int nodes;
    private int hubs;
    private int switches;
    private String topologyStructure;
    private String countryOfOrigin;
    private String currentStatus;

    public Network(int id, int nodes, int hubs, int switches, String topologyStructure, String countryOfOrigin, String currentStatus)
    {
        setId(id);
        setNodes(nodes);
        setHubs(hubs);
        setSwitches(switches);
        setTopologyStructure(topologyStructure);
        setCountryOfOrigin(countryOfOrigin);
        setCurrentStatus(currentStatus);
    }

    public String getCurrentStatus()
    {
        return currentStatus;
    }

    private void setCurrentStatus(String currentStatus)
    {
        this.currentStatus = currentStatus;
    }

    public String getCountryOfOrigin()
    {
        return countryOfOrigin;
    }

    private void setCountryOfOrigin(String countryOfOrigin) {
        this.countryOfOrigin = countryOfOrigin;
    }

    public String getTopologyStructure()
    {
        return topologyStructure;
    }

    private void setTopologyStructure(String topologyStructure)
    {
        this.topologyStructure = topologyStructure;
    }

    public int getSwitches()
    {
        return switches;
    }

    private void setSwitches(int switches)
    {
        this.switches = switches;
    }

    public int getHubs() {
        return hubs;
    }

    private void setHubs(int hubs)
    {
        this.hubs = hubs;
    }

    public int getNodes()
    {
        return nodes;
    }

    private void setNodes(int nodes)
    {
        this.nodes = nodes;
    }

    public int getId()
    {
        return id;
    }

    private void setId(int id)
    {
        this.id = id;
    }
}
