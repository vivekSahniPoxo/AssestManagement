package com.example.assestmanagement;

public class DataModel_Inventory {
   String Location;
   String ToolsName;

    public DataModel_Inventory(String location, String toolsName) {
        Location = location;
        ToolsName = toolsName;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getToolsName() {
        return ToolsName;
    }

    public void setToolsName(String toolsName) {
        ToolsName = toolsName;
    }
}
