/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.time.LocalDateTime;


   public class csvbean {
   private String registration_no ="";
   private String renamed_filepath="";
   private String  datetime;
    private String  auth_email;

    public String getAuth_email() {
        return auth_email;
    }

    public void setAuth_email(String auth_email) {
        this.auth_email = auth_email;
    }
    
     public csvbean(){
         System.out.println("csvscript.csvbean.object is created  :::");
     }
   
    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

  
    public String getRegistration_no() {
        return registration_no;
    }

    public void setRegistration_no(String registration_no) {
        this.registration_no = registration_no;
    }

    public String getRenamed_filepath() {
        return renamed_filepath;
    }

    public void setRenamed_filepath(String renamed_filepath) {
        this.renamed_filepath = renamed_filepath;
    }
    
}
