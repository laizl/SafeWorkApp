package ConnectServe;


import java.io.Serializable;
import java.sql.Date;

/**
 * Created by Meng on 2017/1/7.
 */
public class WorkInfo implements Serializable {
    int WorkID;  // --用户名
    String WorkInfo;  //  --密码
    int Salary;
    String WorkDate;
    String DeliverDate;
    int CompanyID;

    public void setAddress(String address) {
        Address = address;
    }

    public String getAddress() {
        return Address;
    }

    String Address;

    public WorkInfo(int WorkID, String WorkInfo,int Salary, String WorkDate, String DeliverDate,int CompanyId,String Address){
        this.WorkDate = WorkDate;
        this.WorkInfo = WorkInfo;
        this.Salary = Salary;
        this.DeliverDate = DeliverDate;
        this.WorkID = WorkID;
        this.CompanyID = CompanyId;
        this.Address = Address;
    }
    public void setCompanyID(int companyID) {
        CompanyID = companyID;
    }

    public int getCompanyID() {
        return CompanyID;
    }
    public void setWorkID(int workID) {
        WorkID = workID;
    }

    public void setWorkInfo(String workInfo) {
        WorkInfo = workInfo;
    }

    public void setSalary(int salary) {
        Salary = salary;
    }

    public void setWorkDate(String workDate) {
        WorkDate = workDate;
    }

    public void setDeliverDate(String deliverDate) {
        DeliverDate = deliverDate;
    }

    public int getWorkID() {
        return WorkID;
    }

    public String getWorkInfo() {
        return WorkInfo;
    }

    public int getSalary() {
        return Salary;
    }

    public String getWorkDate() {
        return WorkDate;
    }

    public String getDeliverDate() {
        return DeliverDate;
    }
}