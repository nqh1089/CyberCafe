package Model;

public class Account {

    private int IDAccount;
    private String NameAccount;
    private String PWAccount;
    private String RoleAccount;
    private int cccd;
    private int PhoneNumber;
    private String Email;
    private String Sex;
    private int OnlineStatus;
    private int AccountStatus;
    private String Created_at;

    public Account() {
    }

    public Account(int IDAccount, String NameAccount, String PWAccount, String RoleAccount, int cccd, int PhoneNumber, String Email, String Sex, int OnlineStatus, int AccountStatus, String Created_at) {
        this.IDAccount = IDAccount;
        this.NameAccount = NameAccount;
        this.PWAccount = PWAccount;
        this.RoleAccount = RoleAccount;
        this.cccd = cccd;
        this.PhoneNumber = PhoneNumber;
        this.Email = Email;
        this.Sex = Sex;
        this.OnlineStatus = OnlineStatus;
        this.AccountStatus = AccountStatus;
        this.Created_at = Created_at;
    }

    public int getIDAccount() {
        return IDAccount;
    }

    public void setIDAccount(int IDAccount) {
        this.IDAccount = IDAccount;
    }

    public String getNameAccount() {
        return NameAccount;
    }

    public void setNameAccount(String NameAccount) {
        this.NameAccount = NameAccount;
    }

    public String getPWAccount() {
        return PWAccount;
    }

    public void setPWAccount(String PWAccount) {
        this.PWAccount = PWAccount;
    }

    public String getRoleAccount() {
        return RoleAccount;
    }

    public void setRoleAccount(String RoleAccount) {
        this.RoleAccount = RoleAccount;
    }

    public int getCccd() {
        return cccd;
    }

    public void setCccd(int cccd) {
        this.cccd = cccd;
    }

    public int getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(int PhoneNumber) {
        this.PhoneNumber = PhoneNumber;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public String getSex() {
        return Sex;
    }

    public void setSex(String Sex) {
        this.Sex = Sex;
    }

    public int getOnlineStatus() {
        return OnlineStatus;
    }

    public void setOnlineStatus(int OnlineStatus) {
        this.OnlineStatus = OnlineStatus;
    }

    public int getAccountStatus() {
        return AccountStatus;
    }

    public void setAccountStatus(int AccountStatus) {
        this.AccountStatus = AccountStatus;
    }

    public String getCreated_at() {
        return Created_at;
    }

    public void setCreated_at(String Created_at) {
        this.Created_at = Created_at;
    }

    @Override
    public String toString() {
        return "Account{" + "IDAccount=" + IDAccount + ", NameAccount=" + NameAccount + ", PWAccount=" + PWAccount + ", RoleAccount=" + RoleAccount + ", cccd=" + cccd + ", PhoneNumber=" + PhoneNumber + ", Email=" + Email + ", Sex=" + Sex + ", OnlineStatus=" + OnlineStatus + ", AccountStatus=" + AccountStatus + ", Created_at=" + Created_at + '}';
    }

}
