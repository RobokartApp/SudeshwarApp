package com.ark.robokart_robotics.Model;

public class CourseInclusionModel {

    private String ci_id;
    private String ci_cname;
    private String ci_vname;
    private String ci_vmins;


    public CourseInclusionModel(String ci_id, String ci_cname, String ci_vname, String ci_vmins) {
        this.ci_id = ci_id;
        this.ci_cname = ci_cname;
        this.ci_vname = ci_vname;
        this.ci_vmins = ci_vmins;
    }

    public String getCi_id() {
        return ci_id;
    }

    public void setCi_id(String ci_id) {
        this.ci_id = ci_id;
    }

    public String getCi_cname() {
        return ci_cname;
    }

    public void setCi_cname(String ci_cname) {
        this.ci_cname = ci_cname;
    }

    public String getCi_vname() {
        return ci_vname;
    }

    public void setCi_vname(String ci_vname) {
        this.ci_vname = ci_vname;
    }

    public String getCi_vmins() {
        return ci_vmins;
    }

    public void setCi_vmins(String ci_vmins) {
        this.ci_vmins = ci_vmins;
    }
}
