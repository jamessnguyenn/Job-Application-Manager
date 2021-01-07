import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Data extends RecursiveTreeObject<Data>{
    StringProperty jobTitle, company, link, description, status, date;
    private String fullDate;

    public Data(String jobTitle, String company, String link, String description, String status, String date){
        this.jobTitle = new SimpleStringProperty(jobTitle);
        this.company = new SimpleStringProperty(company);
        this.link = new SimpleStringProperty(link); 
        this.description = new SimpleStringProperty(description);
        this.status = new SimpleStringProperty(status);
        this.date = new SimpleStringProperty(date.substring(0,11));
        fullDate = date;

    }
    public String getJobTitle(){
        return jobTitle.getValue();
    }
    public String getCompany(){
        return company.getValue();
    }
    public String getStatus(){
        return status.getValue();
    }
    public String getFullDate(){
        return fullDate;
    }
    public void setStatus(String status){
        this.status = new SimpleStringProperty(status);
    }
    public String getDescription(){
        return description.getValue();
    }
}
