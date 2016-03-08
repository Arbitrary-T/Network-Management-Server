package models;

import java.io.Serializable;

/**
 * Created by arbitrary on 2/11/16.
 */
public class Network implements Serializable

{
    private static final long serialVersionUID = 50L;
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

    public void setCurrentStatus(String currentStatus)
    {
        this.currentStatus = currentStatus;
    }

    public String getCountryOfOrigin()
    {
        return countryOfOrigin;
    }

    public void setCountryOfOrigin(String countryOfOrigin) {
        this.countryOfOrigin = countryOfOrigin;
    }

    public String getTopologyStructure()
    {
        return topologyStructure;
    }

    public void setTopologyStructure(String topologyStructure)
    {
        this.topologyStructure = topologyStructure;
    }

    public int getSwitches()
    {
        return switches;
    }

    public void setSwitches(int switches)
    {
        this.switches = switches;
    }

    public int getHubs() {
        return hubs;
    }

    public void setHubs(int hubs)
    {
        this.hubs = hubs;
    }

    public int getNodes()
    {
        return nodes;
    }

    public void setNodes(int nodes)
    {
        this.nodes = nodes;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }
}
