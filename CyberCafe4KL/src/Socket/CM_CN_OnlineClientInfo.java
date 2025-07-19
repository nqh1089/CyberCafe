package Socket; // Đổi package thành Socket

// Lớp POJO (Plain Old Java Object) để lưu thông tin của client đang online
public class CM_CN_OnlineClientInfo {
    private int idAccount;
    private int idComputer;
    private String nameAccount;
    private String nameComputer;
    private int logAccessId; // LogAccessID của phiên hiện tại

    public CM_CN_OnlineClientInfo(int idAccount, int idComputer, String nameAccount, String nameComputer, int logAccessId) {
        this.idAccount = idAccount;
        this.idComputer = idComputer;
        this.nameAccount = nameAccount;
        this.nameComputer = nameComputer;
        this.logAccessId = logAccessId;
    }

    // Getters
    public int getIdAccount() {
        return idAccount;
    }

    public int getIdComputer() {
        return idComputer;
    }

    public String getNameAccount() {
        return nameAccount;
    }

    public String getNameComputer() {
        return nameComputer;
    }
    
    public int getLogAccessId() {
        return logAccessId;
    }

    @Override
    public String toString() {
        return nameComputer + " (" + nameAccount + ")";
    }
}