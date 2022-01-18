package com.example.assestmanagement;

public class DataModel_Inventory {

    private String id;
    private String materialID;
    private String materialName;
    private String materialModel;
    private String location;
    private String materialDepartment;
    private String materialStatus;
    private String rol;
    private String dateOfEntry;
    private String cost;
    private String tagID;
    private String assigned;
    private String assignedToEmpID;
    private String status;
    private boolean Selected;
    String Color;

    public DataModel_Inventory(String materialName, String materialID, String location, String materialDepartment, String tagId) {
        this.materialName = materialName;
        this.materialID = materialID;
        this.location = location;
        this.materialDepartment = materialDepartment;
        this.tagID=tagId;
    }

    public String getColor() {
        return Color;
    }

    public void setColor(String color) {
        Color = color;
    }

    public boolean getSelected() {
        return Selected;
    }

    public void setSelected(boolean selected) {
        Selected = selected;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMaterialID() {
        return materialID;
    }

    public void setMaterialID(String materialID) {
        this.materialID = materialID;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getMaterialModel() {
        return materialModel;
    }

    public void setMaterialModel(String materialModel) {
        this.materialModel = materialModel;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getMaterialDepartment() {
        return materialDepartment;
    }

    public void setMaterialDepartment(String materialDepartment) {
        this.materialDepartment = materialDepartment;
    }

    public String getMaterialStatus() {
        return materialStatus;
    }

    public void setMaterialStatus(String materialStatus) {
        this.materialStatus = materialStatus;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getDateOfEntry() {
        return dateOfEntry;
    }

    public void setDateOfEntry(String dateOfEntry) {
        this.dateOfEntry = dateOfEntry;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getTagID() {
        return tagID;
    }

    public void setTagID(String tagID) {
        this.tagID = tagID;
    }

    public String getAssigned() {
        return assigned;
    }

    public void setAssigned(String assigned) {
        this.assigned = assigned;
    }

    public String getAssignedToEmpID() {
        return assignedToEmpID;
    }

    public void setAssignedToEmpID(String assignedToEmpID) {
        this.assignedToEmpID = assignedToEmpID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
