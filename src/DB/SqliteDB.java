package DB;

import App.*;
import App.Vacation;

import java.io.File;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;


public class SqliteDB {

    private Connection dbConnection;

    public void connectToDB(String path, Boolean deleteDB) {
        try {
            Class.forName("org.sqlite.JDBC");
            if (deleteDB)
                new File(path).delete();
            dbConnection = DriverManager.getConnection("jdbc:sqlite:" + path);

            createUsersTable();
            createVacationTable();
            createRequestsTable();
            createPaymentsTable();
            createTradeRequestsTable();
            createAirportTable();

            System.out.println("db init");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    //**************** Create Tables **************************//

    private void createUsersTable() throws SQLException {
        execute("CREATE TABLE IF NOT EXISTS Users (\n" +
                "first_name text NOT NULL,\n" +
                "last_name text NOT NULL,\n" +
                "email text PRIMARY KEY, \n" +
                "password text NOT NULL,\n" +
                "birth_date text,\n" +
                "city text NOT NULL,\n" +
                "sign_up_date text);");
    }




    private void createVacationTable() throws SQLException {
        execute("CREATE TABLE IF NOT EXISTS Vacations (\n" +
                "vacation_status int, \n" +
                "seller_email text, \n" +
                "vacation_id int PRIMARY KEY, \n" +
                "fromCountry text, \n" +
                "toCountry text, \n" +
                "checkin text, \n" +
                "checkout text, \n" +
                "airline text, \n" +
                "back_flight int, \n" +
                "hand_bag text, \n" +
                "checked_bag text, \n" +
                "connec_flight text, \n" +
                "vacation_type text, \n" +
                "ticket_type text, \n" +
                "hotel int, \n" +
                "hotel_type text, \n" +
                "hotel_raiting int, \n" +
                "num_of_tickets int, \n" +
                "original_price int, \n" +
                "sale_price int, \n" +
                "off int, \n" +
                "CONSTRAINT FK_Vacations FOREIGN KEY (seller_email) REFERENCES Users(email)) ;");
    }


    private void createRequestsTable() throws SQLException {
        execute("CREATE TABLE IF NOT EXISTS Requests (\n" +
                "seller_email text, \n" +
                "searcher_email text, \n" +
                "vacation_id int, \n" +
                "seller_status int, \n" +
                "searcher_status int, \n" +
                "CONSTRAINT PK_Requests PRIMARY KEY (seller_email, searcher_email, vacation_id), \n" +
                "CONSTRAINT FK_Requests FOREIGN KEY (vacation_id) REFERENCES Vacations(vacation_id),\n" +
                "CONSTRAINT FK_RequestSeller FOREIGN KEY (seller_email) REFERENCES Users(email), \n" +
                "CONSTRAINT FK_RequestSearcher FOREIGN KEY (searcher_email) REFERENCES Users(email));"
        );
    }


    private void createPaymentsTable() throws SQLException {
        execute("CREATE TABLE IF NOT EXISTS Payments (\n" +
                "seller_email text, \n" +
                "buyer_email text, \n" +
                "vacation_id int PRIMARY KEY, \n" +
                "payment_date text, \n" +
                "CONSTRAINT FK_PaymentsSeller FOREIGN KEY (seller_email) REFERENCES Users(email), \n" +
                "CONSTRAINT FK_PaymentsBuyer FOREIGN KEY (buyer_email) REFERENCES Users(email), \n" +
                "CONSTRAINT FK_VacationID FOREIGN KEY (vacation_id) REFERENCES Vacations(vacation_id),\n" +
                "CONSTRAINT FK_PaymentsVacation FOREIGN KEY (vacation_id) REFERENCES Vacations(vacation_id));");
    }


    private void createTradeRequestsTable() throws SQLException {
        execute("CREATE TABLE IF NOT EXISTS TradeRequests (\n" +
                "seller_email text, \n" +
                "trader_email text, \n" +
                "vacation_id int, \n" +
                "vacationtoTrade_id int, \n"+
                "seller_status int, \n" +
                "trader_status int, \n" +
                "CONSTRAINT PK_TradeRequests PRIMARY KEY (vacation_id, vacationtoTrade_id), \n" +
                "CONSTRAINT FK_TradeRequests1 FOREIGN KEY (vacation_id) REFERENCES Vacations(vacation_id),\n" +
                "CONSTRAINT FK_TradeRequests2 FOREIGN KEY (vacationtoTrade_id) REFERENCES Vacations(vacation_id),\n" +
                "CONSTRAINT FK1_TradeRequests FOREIGN KEY (seller_email) REFERENCES Users(email), \n" +
                "CONSTRAINT FK2_TradeRequests FOREIGN KEY (trader_email) REFERENCES Users(email));"
        );
    }

    private void createAirportTable() throws SQLException {
        execute("CREATE TABLE IF NOT EXISTS Airports (\n" +
                "Country text, \n" +
                "City text, \n" +
                "CONSTRAINT PK_TradeRequests PRIMARY KEY (Country, City));"
        );
    }



    //*******************Add *****************************************//


    public void addUser(User user) {
        addUser(user.getFirst_name(), user.getLast_name(), user.getEmail(), user.getPassword(), user.toStringBirth_date(), user.getCity(), user.toStringSignup_date());
    }

    private void addUser(String first_name, String last_name, String email, String password, String birth_date, String city, String sign_up_date) {
        try {
            String query = "INSERT INTO Users \n" +
                    "VALUES ('" + first_name + "','" + last_name + "','" + email + "','" + password + "','" + birth_date + "','" + city + "','" + sign_up_date + "');";
            execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void addVacation(Vacation vac) {
        try {
            int vacation_status = vac.getVacation_status() ? 1 : 0;
            String seller_email = vac.getSeller();
            int vacation_id = getVacationID();
            String from = vac.getFrom();
            String to = vac.getto();
            String checkin = vac.toStringCheckin();
            String checkout = vac.toStringCheckout();
            String airline = vac.getAirline();
            int back_flight = vac.getBackFlight() ? 1 : 0;
            String hand_bag = vac.getHand_bag();
            String checked_bag = vac.getChecked_bag();
            String connec_flight = vac.getConnec_flight();
            String vacation_type = vac.getVacation_type();
            String ticket_type = vac.getTicket_type();

            int hotel;
            String hotel_type;
            int hotel_raiting;
            if (vac.getHotel()) {
                hotel = 1;
                hotel_type = vac.getHotel_type();
                hotel_raiting = vac.getHotel_raiting();
            } else {
                hotel = 0;
                hotel_type = null;
                hotel_raiting = -1;
            }

            int num_of_tickets = vac.getNum_of_tickets();
            int original_price = vac.getOriginal_price();
            int sale_price = vac.getSale_price();
            int off = vac.getOff();

            String query = String.format("INSERT INTO Vacations VALUES(%d,'%s',%d,'%s','%s','%s','%s','%s',%d,'%s','%s','%s','%s','%s',%d,'%s',%d,%d,%d,%d,%d)",
                    vacation_status, seller_email, vacation_id, from, to, checkin, checkout,
                    airline, back_flight, hand_bag, checked_bag, connec_flight, vacation_type, ticket_type, hotel, hotel_type,
                    hotel_raiting, num_of_tickets, original_price, sale_price, off);
            execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

private int getVacationID(){

    try {
        Statement st = dbConnection.createStatement();
        ResultSet resSet = st.executeQuery("SELECT  MAX(vacation_id) FROM Vacations;");
        return resSet.getInt(1) + 1;

    } catch (SQLException e) {
        e.printStackTrace();
        return 1;
    }
}


    public void addReq (Request req) {

        try {

            String seller_email = req.getSeller_email();
            String searcher_email = req.getSearcher_email();
            int vacation_id = req.getVacation_id();
            int seller_status = -1;
            int searcher_status = 0;

            String query = String.format("INSERT INTO Requests VALUES('%s','%s', %d, %d, %d)", seller_email, searcher_email, vacation_id, seller_status, searcher_status);
            execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void addPayment(Payment payment) {

        try {
            String payment_date = payment.toStringPayment_date();

            String query = String.format("INSERT INTO Payments VALUES('%s','%s', %d, '%s')",
                    payment.getSeller_email(), payment.getBuyer_email(), payment.getVacation_id(),payment_date);
            execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addReq (TradeRequest tradeRequest) {

        try {

            String seller_email = tradeRequest.getSeller_email();
            String trader_email = tradeRequest.getSearcher_email();
            int vacation_id = tradeRequest.getVacation_id();
            int vacationtoTrade_id = tradeRequest.getVacationtoTrade_id();
            int seller_status = -1;
            int trader_status = 1;

            String query = String.format("INSERT INTO TradeRequests VALUES('%s', '%s', %d, %d, %d, %d)", seller_email, trader_email, vacation_id, vacationtoTrade_id, seller_status, trader_status);
            execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void addAirportTable(String country, String city){
             try {
            String query = String.format("INSERT INTO Airports VALUES('%s', '%s')", country, city);
            execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    //*************** Delete ******************************

    public void deleteUser(User user) {
        try {
            execute("DELETE FROM Users WHERE Users.email = '" + user.getEmail() + "' ;");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteVacation(Vacation vacation) {
        try {


            execute("DELETE FROM Vacations WHERE Vacations.vacation_id = " + vacation.getVacation_id() + " ;");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteRequest(Request req) {
        try {
            execute("DELETE FROM Requests WHERERequests.seller_email = " + req.getSeller_email() +
                    " AND Requests.searcher_email = " + req.getSearcher_email() + " AND Requests.vacation_id = " + req.getVacation_id() + " ;");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public void deleteVacationsBySeller(String selllerEmail) {

        if(!this.getVacationsByseller_email(selllerEmail).isEmpty())
        try {
            execute("DELETE FROM Vacations WHERE Vacations.seller_email = '" + selllerEmail + "' ;");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteRequestsBySeller(String selllerEmail) {
        if(!this.getRequestsByseller_email(selllerEmail).isEmpty()) {
            try {
                execute("DELETE FROM Requests WHERE Requests.seller_email = " + selllerEmail + "' ;");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void deleteRequestsBySearcher(String searcherEmail) {
        if(!this.getRequestsBySearcher_email(searcherEmail).isEmpty()) {
            try {
                execute("DELETE FROM Requests WHERE Requests.searcher_email = " + searcherEmail + "' ;");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void deleteRequestsByVacationID (int vacation_id) {
        try {
            execute("DELETE FROM Requests WHERE Requests.vacation_id = " + vacation_id + " ;");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void deleteTradeRequestBySeller(String seller_email) {
        if (!this.getTradeRequestByseller_email(seller_email).isEmpty()) {
            try {
                execute("DELETE FROM TradeRequests WHERE TradeRequests.seller_email = " + seller_email + "' ;");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void deleteTradeRequestsByTrader(String trader_email) {
        if(!this.getTradeRequestByTrader_email(trader_email).isEmpty()){
        try {
            execute("DELETE FROM TradeRequests WHERE TradeRequests.trader_email = "  + trader_email + "' ;");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }}

    public void deleteTradeRequestByVacationID (int vacation_id) {
        try {
            execute("DELETE FROM TradeRequests WHERE TradeRequests.vacation_id = " + vacation_id + " ;");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteTradeRequestByTradeVacationID (int vacationtoTrade_id) {
        try {
            execute("DELETE FROM TradeRequests WHERE TradeRequests.vacationtoTrade_id = " + vacationtoTrade_id + " ;");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    //***************Update****************//


    public void UpdateUser(User user) {
        UpdateUsers(user.getFirst_name(), user.getLast_name(), user.getEmail(), user.getPassword(), user.toStringBirth_date(), user.getCity());
    }

    private void UpdateUsers(String first_name, String last_name, String email, String password, String birth_date, String city) {
        try {
            String query = "UPDATE Users SET first_name='" + first_name +
                    "', last_name='" + last_name + "',password='" + password + "',birth_date='" + birth_date + "',city='" + city + "'" +
                    "WHERE email = '" + email + "'";
            execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public void UpdateVacation(Vacation vac) {
        try {

            int vacation_id = vac.getVacation_id();
            String from = vac.getFrom();
            String to = vac.getto();
            String checkin = vac.toStringCheckin();
            String checkout = vac.toStringCheckout();
            String airline = vac.getAirline();
            int back_flight = vac.getBackFlight() ? 1 : 0;
            String hand_bag = vac.getHand_bag();
            String checked_bag = vac.getChecked_bag();
            String connec_flight = vac.getConnec_flight();
            String vacation_type = vac.getVacation_type();
            String ticket_type = vac.getTicket_type();

            int hotel;
            String hotel_type;
            int hotel_raiting;
            if (vac.getHotel()) {
                hotel = 1;
                hotel_type = vac.getHotel_type();
                hotel_raiting = vac.getHotel_raiting();
            } else {
                hotel = 0;
                hotel_type = null;
                hotel_raiting = -1;
            }

            int num_of_tickets = vac.getNum_of_tickets();
            int original_price = vac.getOriginal_price();
            int sale_price = vac.getSale_price();
            int off = vac.getOff();

            String query = "UPDATE Vacations SET fromCountry= '" + from +
                            "', toCountry = '" +to+
                            "', checkin = '" +checkin +
                            "', checkout = '" +checkout+
                            "', airline = '" +airline +
                            "', back_flight = " +back_flight+
                            ", hand_bag  = '" + hand_bag +
                            "', checked_bag = '" + checked_bag +
                            "', connec_flight = '" + connec_flight +
                            "', vacation_type = '" + vacation_type +
                            "', ticket_type  = '" + ticket_type +
                            "', hotel =" +hotel+
                            ", hotel_type = '" +hotel_type+
                            "', hotel_raiting =" +hotel_raiting+
                            ", num_of_tickets = " +num_of_tickets+
                            ", original_price= " +original_price+
                            ", sale_price = " +sale_price+
                            ", off  = " +off+ " WHERE vacation_id = " + vacation_id + "";

            execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void UpdateRequestsSellerStatus(Request req, boolean sellerStatus) {
        try {
            String seller_email = req.getSeller_email();
            String searcher_email = req.getSearcher_email();
            int vacation_id = req.getVacation_id();
            int status = sellerStatus ? 1 : 0;

            String query = "UPDATE Requests SET seller_status=" + status +
                    " WHERE seller_email = '" + seller_email + "' AND searcher_email='" + searcher_email + "' AND vacation_id=" + vacation_id + "";
            execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void UpdateRequestsSearcherStatus(Request req, boolean searcherStatus) {
        try {
            String seller_email = req.getSeller_email();
            String searcher_email = req.getSearcher_email();
            int vacation_id = req.getVacation_id();
            int status = searcherStatus ? 1 : 0;

            String query = "UPDATE Requests SET searcher_status = " + status + "" +
                    " WHERE seller_email = '" + seller_email + "' AND searcher_email='" + searcher_email + "' AND vacation_id=" + vacation_id + "";
            execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void UpdatVacationStatus(int vacation_id, boolean vac_status) {
        try {
            int status = vac_status ? 1 : 0;

            String query = "UPDATE Vacations SET vacation_status = " + status + "" +
                    " WHERE vacation_id = " + vacation_id + "";
            execute(query);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void UpdateTradeRequestSellerStatus(TradeRequest req, boolean sellerStatus) {
        try {
            String seller_email = req.getSeller_email();
            String trader_email = req.getSearcher_email();
            int vacation_id = req.getVacation_id();
            int vacationtoTrade_id = req.getVacationtoTrade_id();
            int status = sellerStatus ? 1 : 0;

            String query = "UPDATE TradeRequests SET seller_status=" + status +
                    " WHERE TradeRequests.vacation_id = " + vacation_id +" AND TradeRequests.vacationtoTrade_id = "+vacationtoTrade_id+ "";
            execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ******************** Get ********************************


    public User getUserByEmail(String email) {
        try {
            Statement st = dbConnection.createStatement();
            ResultSet resultSet = st.executeQuery("SELECT * \n" +
                    "FROM Users \n" +
                    "WHERE Users.email = '" + email + "' ;");
            return getUserFromRow(resultSet);

        } catch (SQLException e) {
            return null;
        }
    }


    public String getUserNameByEmail(String email) {
        try {
            Statement st = dbConnection.createStatement();
            ResultSet resultSet = st.executeQuery("SELECT first_name, last_name\n" +
                    "FROM Users \n" +
                    "WHERE Users.email = '" + email + "' ;");
            String first_name = resultSet.getString("first_name");
            String last_name = resultSet.getString("last_name");
            return first_name + " " + last_name;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Vacation getVacationByVacationId(int vacationId) {
        try {
            Statement st = dbConnection.createStatement();
            ResultSet resSet = st.executeQuery("SELECT * FROM Vacations as p " +
                    "WHERE p.vacation_id = " + vacationId + ";");
            return getVacationFromRow(resSet);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }


    public List<Request> getRequestsByseller_email(String seller_email) {
        try {
            Statement st = dbConnection.createStatement();
            ResultSet resSet = st.executeQuery("SELECT * FROM Requests " +
                    "WHERE Requests.seller_email = '" + seller_email + "';");
            List<Request> requests = new ArrayList<>();
            while (resSet.next()) {
                requests.add(getRequestFromRow(resSet));
            }
            return requests;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }


    public List<Request> getRequestsBySearcher_email(String searcher_email) {
        try {
            Statement st = dbConnection.createStatement();
            ResultSet resSet = st.executeQuery("SELECT * FROM Requests " +
                    "WHERE Requests.searcher_email = '" + searcher_email + "';");

            List<Request> requests = new ArrayList<>();
            while (resSet.next()) {
                requests.add(getRequestFromRow(resSet));
            }
            return requests;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public List<Request> getRequestsByVacationID(int id) {
        try {
            Statement st = dbConnection.createStatement();
            ResultSet resSet = st.executeQuery("SELECT * FROM Requests " +
                    "WHERE Requests.vacation_id = " + id + ";");

            List<Request> requests = new ArrayList<>();
            while (resSet.next()) {
                requests.add(getRequestFromRow(resSet));
            }
            return requests;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<TradeRequest> getTradeRequestsByVacationID(int id) {
        try {
            Statement st = dbConnection.createStatement();
            ResultSet resSet = st.executeQuery("SELECT * FROM TradeRequests " +
                    "WHERE TradeRequests.vacation_id =" + id + " OR TradeRequests.vacationtoTrade_id="+ id+" ;");

            List<TradeRequest> tradeRequests = new ArrayList<>();
            while (resSet.next()) {
                tradeRequests.add(getTradeFromRow(resSet));
            }
            return tradeRequests;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }



    public List<TradeRequest> getTradeRequestByseller_email(String seller_email) {
        try {
            Statement st = dbConnection.createStatement();
            ResultSet resSet = st.executeQuery("SELECT * FROM TradeRequests " +
                    "WHERE TradeRequests.seller_email = '" + seller_email + "' AND TradeRequests.vacation_id in (select vacation_id from Vacations where Vacations.vacation_status=1 )" +
                    "AND TradeRequests.vacationtoTrade_id in (select vacation_id from Vacations where Vacations.vacation_status=1 );");
            List<TradeRequest> tradeRequests = new ArrayList<>();
            while (resSet.next()) {
                tradeRequests.add(getTradeFromRow(resSet));
            }
            return tradeRequests;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


    public List<TradeRequest> getTradeRequestByTrader_email(String trader_email) {
        try {
            Statement st = dbConnection.createStatement();
            ResultSet resSet = st.executeQuery("SELECT * FROM TradeRequests " +
                    "WHERE TradeRequests.trader_email = '" + trader_email + "';");

            List<TradeRequest> tradeRequests = new ArrayList<>();
            while (resSet.next()) {
                tradeRequests.add(getTradeFromRow(resSet));
            }
            return tradeRequests;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }



    public List<Vacation> getAllAvailableVacations() {
        try {
            Statement st = dbConnection.createStatement();
            ResultSet resSet = st.executeQuery("SELECT * FROM Vacations WHERE Vacations.vacation_status=1;");
            List<Vacation> vacations = new ArrayList<>();
            while (resSet.next()) {
                vacations.add(getVacationFromRow(resSet));
            }

            return vacations;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Vacation> getVacationsByseller_email(String seller_email) {
        try {
            Statement st = dbConnection.createStatement();
            ResultSet resSet = st.executeQuery("SELECT * FROM Vacations WHERE Vacations.seller_email='"+seller_email+"';");
            List<Vacation> vacations = new ArrayList<>();
            while (resSet.next()) {
                vacations.add(getVacationFromRow(resSet));
            }

            return vacations;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Vacation> getAvailableVacationsByseller_email(String seller_email) {
        try {
            Statement st = dbConnection.createStatement();
            ResultSet resSet = st.executeQuery("SELECT * FROM Vacations WHERE Vacations.vacation_status=1 AND Vacations.seller_email='"+seller_email+"';");
            List<Vacation> vacations = new ArrayList<>();
            while (resSet.next()) {
                vacations.add(getVacationFromRow(resSet));
            }

            return vacations;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }

    public List<Vacation> getVacationBySimpleSearch(Vacation vacation) {

        try {
            String from=vacation.getFrom();
            String to=vacation.getto();
            String cin=vacation.toStringCheckin();
            String cout=vacation.toStringCheckout();

            Statement st = dbConnection.createStatement();
            ResultSet resSet = st.executeQuery("SELECT * FROM Vacations WHERE Vacations.vacation_status=1 AND Vacations.fromCountry = '" + from +
                    "' AND Vacations.toCountry = '" + to +
                    "' AND Vacations.checkin = '" + cin +
                    "' AND Vacations.checkout='" + cout + "' ;");

            List<Vacation> vacations = new ArrayList<>();
            while (resSet.next()) {
                vacations.add(getVacationFromRow(resSet));
            }
            return vacations;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<String> getAirports() {
        try {
            Statement st = dbConnection.createStatement();
            ResultSet resSet = st.executeQuery("SELECT * FROM Airports ;");

            List<String> airports = new ArrayList<>();
            while (resSet.next()) {
                airports.add(resSet.getString("Country") +" ("+resSet.getString("City")+")" );
            }
            return airports;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    //**********get from row*******************///

    private User getUserFromRow(ResultSet resultSet) throws SQLException {
        String first_name = resultSet.getString("first_name");
        String last_name = resultSet.getString("last_name");
        String email = resultSet.getString("email");
        String password = resultSet.getString("password");
        String sbirth_date = resultSet.getString("birth_date");
        String city = resultSet.getString("city");
        String Ssign_up_date = resultSet.getString("sign_up_date");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate birth_date = LocalDate.parse(sbirth_date, formatter);
        LocalDate sign_up_date = LocalDate.parse(Ssign_up_date, formatter);
        return new User(first_name, last_name, email, password, birth_date, city, sign_up_date);
    }


    private Vacation getVacationFromRow(ResultSet resSet) throws SQLException {
        boolean vacation_status = resSet.getInt("vacation_status") == 1;

        String seller_email = resSet.getString("seller_email");
        int vacation_id = resSet.getInt("vacation_id");

        String from = resSet.getString("fromCountry");
        String to = resSet.getString("toCountry");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate checkin = LocalDate.parse(resSet.getString("checkin"), formatter);
        LocalDate checkout = LocalDate.parse(resSet.getString("checkout"), formatter);

        String airline = resSet.getString("airline");
        boolean back_flight = resSet.getInt("back_flight") == 1;

        String hand_bag = resSet.getString("hand_bag");
        String checked_bag = resSet.getString("checked_bag");

        String connec_flight = resSet.getString("connec_flight");

        String vacation_type = resSet.getString("vacation_type");
        String ticket_type = resSet.getString("ticket_type");

        boolean hotel = resSet.getInt("hotel") == 1;
        String hotel_type = resSet.getString("hotel_type");
        int hotel_raiting = resSet.getInt("hotel_raiting");

        int num_of_tickets = resSet.getInt("num_of_tickets");
        boolean separately = false;

        int original_price = resSet.getInt("original_price");
        int sale_price = resSet.getInt("sale_price");
        int off = resSet.getInt("off");

        Vacation v = new Vacation(seller_email, vacation_id, from, to,
                checkin, checkout, airline, back_flight, hand_bag, checked_bag,
                connec_flight, vacation_type, ticket_type, hotel, hotel_type, hotel_raiting,
                num_of_tickets, separately, original_price, sale_price, off);
        v.setVacation_status(vacation_status);
        return v;
    }


    private Request getRequestFromRow(ResultSet resultSet) throws SQLException {
        String seller_email = resultSet.getString("seller_email");
        String searcher_email = resultSet.getString("searcher_email");
        int vacation_id = resultSet.getInt("vacation_id");
        Boolean seller_status = resultSet.getInt("seller_status") == 1;
        if(resultSet.getInt("seller_status") == -1) seller_status=null;
        Boolean searcher_status = resultSet.getInt("searcher_status") == 1;


        Request request = new Request(seller_email, searcher_email, vacation_id, searcher_status);
        request.setSeller_status(seller_status);
        return request;
    }


    private TradeRequest getTradeFromRow(ResultSet resultSet) throws SQLException {
        String seller_email = resultSet.getString("seller_email");
        String trader_email = resultSet.getString("trader_email");
        int vacation_id = resultSet.getInt("vacation_id");
        int vacationtoTrade_id = resultSet.getInt("vacationtoTrade_id");
        Boolean seller_status = resultSet.getInt("seller_status") == 1;
        if(resultSet.getInt("seller_status") == -1) seller_status=null;
        Boolean trader_status = resultSet.getInt("trader_status") == 1;


        TradeRequest tradeRequest = new TradeRequest(seller_email, trader_email, vacation_id,vacationtoTrade_id, trader_status);
        tradeRequest.setSeller_status(seller_status);
        return tradeRequest;
    }



//    ******************** Check if Exists ***********************************

    public Boolean isUserExists(User user) {
        if (user==null)
            return false;

        try {
            Statement st = dbConnection.createStatement();
            String query = "SELECT * FROM Users as u WHERE u.email = '" + user.getEmail() + "' ;";
            ResultSet resSet = st.executeQuery(query);
            User resUser = getUserFromRow(resSet);
            return resUser!=null;
        } catch (SQLException e) {
            return false;
        }
    }


    public Boolean isReqExists (String seller_email,String searcher_email,int VacationId) {
         try {
            Statement st = dbConnection.createStatement();
            ResultSet resSet = st.executeQuery("SELECT * FROM Requests " +
                    "WHERE Requests.vacation_id = "+ VacationId+" AND  Requests.seller_email = '" + seller_email + "' AND Requests.searcher_email='"+searcher_email+ "' ;");
             Request req=getRequestFromRow(resSet);
            return req!=null;
        } catch (SQLException e) {
            return false;
        }
    }


    public Boolean isReqExists (int VacationId, int VacationtoTrade_id) {
        try {
            Statement st = dbConnection.createStatement();
            ResultSet resSet = st.executeQuery("SELECT * FROM TradeRequests " +
                    "WHERE TradeRequests.vacation_id = "+ VacationId+" AND TradeRequests.vacationtoTrade_id = "+VacationtoTrade_id + " ;");
            TradeRequest req=getTradeFromRow(resSet);
            return req!=null;
        } catch (SQLException e) {
            return false;
        }
    }





//*******************//

    public void close() {
        try {
            dbConnection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void execute(String sql) throws SQLException {
        Statement st = dbConnection.createStatement();
        st.execute(sql);
    }
}

