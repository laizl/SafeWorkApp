package ConnectServe;


import java.io.Serializable;


/**
 * Created by Meng on 2017/1/7.
 */
public class CompanyInfo implements Serializable {
    int CompanyID;
    String CompanyName;  //  --密码
    String CompanyManger;
    String  CompanyPhone;
    String  CompanyEmail;

    public void setCompanyID(int companyID) {
        CompanyID = companyID;
    }

    public void setCompanyName(String companyName) {
        CompanyName = companyName;
    }

    public void setCompanyManger(String companyManger) {
        CompanyManger = companyManger;
    }

    public void setCompanyPhone(String companyPhone) {
        CompanyPhone = companyPhone;
    }

    public void setCompanyEmail(String companyEmail) {
        CompanyEmail = companyEmail;
    }

    public int getCompanyID() {
        return CompanyID;
    }

    public String getCompanyName() {
        return CompanyName;
    }

    public String getCompanyManger() {
        return CompanyManger;
    }

    public String getCompanyPhone() {
        return CompanyPhone;
    }

    public String getCompanyEmail() {
        return CompanyEmail;
    }
}