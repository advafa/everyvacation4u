package Model;

import App.*;
import App.Vacation;
import DB.SqliteDB;
import java.util.List;

public class Model {

    private final SqliteDB db;

    public Model() {
        this.db = new SqliteDB();
        db.connectToDB("myDB.db",false);
    }

    //  ******************** Add functions ***********************************
    public void addUser(User user) {db.addUser(user); }
    public void addVacation(Vacation aVacation) {db.addVacation(aVacation); }
    public void addReq(Order order) {
        db.addReq(order);
    }
    public void addPayment(Payment payment) {db.addPayment(payment);}

    //  ******************** Check if Exists ***********************************
    public Boolean isUserExists(User user) { return db.isUserExists(user);}

    //  ******************** Delete ***********************************
    public void deleteUser(User user){db.deleteUser(user);}
    public void deleteVacation(Vacation aVacation) {db.deleteVacation(aVacation);}
    public void deleteOrder(Order order){db.deleteOrder(order);}

    public void deleteVacationsBySeller(String userEmail){db.deleteVacationsBySeller(userEmail);}
    public void deleteRequestsBySeller(String selllerEmail){db.deleteRequestsBySeller(selllerEmail);}
    public void deleteRequestsByBuyer(String buyerEmail){db.deleteRequestsBySeller(buyerEmail);}




    //  ******************** Get ***********************************
    public User loadUser(String userEmail) {return db.getUserByEmail(userEmail);}
    public String getUserNameByEmail(String email){return db.getUserNameByEmail(email);}
    public Vacation getVacationByVacationId(int VacationId){return db.getVacationByVacationId(VacationId);}
    public List<Order> getOrdersByseller_email(String seller_email){return db.getOrdersByseller_email(seller_email);}
    public List<Order> getOrdersByBuyer_email(String buyer_email){return db.getOrdersByBuyer_email(buyer_email);}
    public List<Vacation> getAllAvailableVacations(){return db.getAllAvailableVacations();}
    public List<Vacation> getVacationBySimpleSearch(Vacation aVacation){return db.getVacationBySimpleSearch(aVacation);}
    public List<Vacation> getVacationsByseller_email(String seller_email){return db.getVacationsByseller_email(seller_email);}
    public List<Vacation> getAvailableVacationsByseller_email(String seller_email){return db.getAvailableVacationsByseller_email(seller_email);}



    //*****************Update************************
    public void UpdateUser(User user){db.UpdateUser(user);}
    public void UpdateOrdersSellerStatus(Order ord, boolean sellerStatus) {db.UpdateOrdersSellerStatus(ord,sellerStatus);};
    public void UpdateOrdersBuyerStatus(Order ord, boolean buyerStatus) {db.UpdateOrdersBuyerStatus(ord,buyerStatus);}
    public void UpdatVacationStatus(int vacation_id, boolean vac_status) {db.UpdatVacationStatus(vacation_id,vac_status);}
    public void UpdateVacation(Vacation vac){db.UpdateVacation(vac);}
}
