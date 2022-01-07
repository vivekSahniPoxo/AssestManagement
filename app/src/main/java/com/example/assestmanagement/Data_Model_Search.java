package com.example.assestmanagement;

public class Data_Model_Search {
String ToolNmae ;
String ToolsId;

    public String getToolNmae() {
        return ToolNmae;
    }

    public void setToolNmae(String toolNmae) {
        ToolNmae = toolNmae;
    }

    public String getToolsId() {
        return ToolsId;
    }

    public void setToolsId(String toolsId) {
        ToolsId = toolsId;
    }

    public Data_Model_Search(String toolNmae, String toolsId) {
        ToolNmae = toolNmae;
        ToolsId = toolsId;
    }

}
