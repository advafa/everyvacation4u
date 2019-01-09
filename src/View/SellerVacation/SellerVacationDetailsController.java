package View.SellerVacation;

import App.Order;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;


import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import App.User;
import App.Vacation;
import Main.ViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;





public class SellerVacationDetailsController implements Initializable {

    @FXML
    public Label check_in;
    public Label check_out;
    public Label from;
    public Label to;
    public Label airline;
    public Label backf;
    public Label handbag;
    public Label checkbag;
    public Label conectin;
    public Label vac_type;
    public Label tic_type;
    public Label hotel_in;
    public Label hotel_type;
    public Label hotel_raiting;
    public Label num;
    public Label price;
    public Label original_price;
    public Label vac_status;
    public Label seller_email;
    public Label name;


    private ViewModel viewModel;
    private Vacation vacation;


    //    buyertitle
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }




    public void Back(MouseEvent mouseEvent) {
        viewModel.goToSellerVacationsView();
    }


    public void setViewModel(ViewModel viewModel) {
        this.viewModel = viewModel;
    }


    public void loadVacationView(Vacation vacation) {
        this.vacation = vacation;

        this.check_in.setText(vacation.toStringCheckin());
        this.check_out.setText(vacation.toStringCheckout());
        this.from.setText(vacation.getFrom());
        this.to.setText(vacation.getto());
        this.airline.setText(vacation.getAirline());

        if (vacation.getBackFlight()) {
            this.backf.setText("Back flight Included");
        } else {
            this.backf.setText("Back flight *NOT* Included");
        }

        this.handbag.setText(vacation.getHand_bag());
        this.checkbag.setText(vacation.getChecked_bag());
        this.conectin.setText(vacation.getConnec_flight());
        this.vac_type.setText(vacation.getVacation_type());
        this.tic_type.setText(vacation.getTicket_type());


        if (vacation.getHotel()) {
            this.hotel_in.setText("Hotel Included:");
            this.hotel_type.setText(vacation.getHotel_type());
            this.hotel_raiting.setText("Hotel Rating: " + vacation.getHotel_raiting() + " Stars");
        } else {
            this.hotel_in.setText("Hotel *NOT* Included");
            this.hotel_type.setText("");
            this.hotel_raiting.setText("");
        }


        this.num.setText("" + vacation.getNum_of_tickets());
        this.price.setText(vacation.getSale_price() + "$");
        this.original_price.setText(vacation.getOriginal_price() + "$");


        if (vacation.getVacation_status())
                this.vac_status.setText("Vacation Available!!");
        else
                this.vac_status.setText("Vacation is NOT Available!!");


    }
}

//     if (vacation.getVacation_status()) {
//        this.vac_status.setText("Available for sale");
//        dec_btn.setDisable(false);
//        app_btn.setDisable(false);
//        payment.setDisable(false);
//    } else {
//        this.vac_status.setText("Sold out");
//        dec_btn.setDisable(true);
//        app_btn.setDisable(true);
//        payment.setDisable(true);
//    }




//

//
//
//
//        public void ApproveSaleRequest(MouseEvent mouseEvent){
//            if(!viewModel.getVacationStatus(clickedRow.getVacation_id())){
//                viewModel.popAlerterror("This vacation Sold Out!");
//                return;
//            }
//            Order ord=new Order(user.getEmail(),clickedRow.getBuyer_email(),clickedRow.getVacation_id(),false);
//            this.viewModel.setSellerStatus(ord,true);
//
//        }
//
//        public void DeclineSaleRequest(MouseEvent mouseEvent){
//            if(!viewModel.getVacationStatus(clickedRow.getVacation_id())){
//                viewModel.popAlerterror("This vacation Sold Out!");
//                return;
//            }
//            Order ord=new Order(user.getEmail(),clickedRow.getBuyer_email(),clickedRow.getVacation_id(),false);
//            this.viewModel.setSellerStatus(ord,false);
//
//        }
//
//
//    }
//
//public void goToPay(MouseEvent mouseEvent) {
//    if (vacation.getVacation_status() && !order.getBuyer_status() && order.getSeller_status()) {
//        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
//        alert.setContentText("Are you sure this buyer pay in cash?");
//        Optional<ButtonType> result = alert.showAndWait();
//        if (result.get() == ButtonType.OK) {
//            this.viewModel.setVacationStatus(vacation.getVacation_id(), false);
//            this.viewModel.setBuyerStatus(order, true);
//        }
//    } else {
//        if (!vacation.getVacation_status())
//            viewModel.popAlerterror("This vacation Sold Out!");
//        if (order.getBuyer_status())
//            viewModel.popAlerterror("This buyer already pay!");
//        if (!order.getSeller_status())
//            viewModel.popAlerterror("Yoy declined this request!");
//
//
//    }
//
//
//
////}
//
//public void Aprrove(MouseEvent mouseEvent) {
//        if (!vacation.getVacation_status()) {
//        viewModel.popAlerterror("This vacation Sold Out!");
//        return;
//        }
//        this.viewModel.setSellerStatus(order, true);
//
//        }
//
//public void Decline(MouseEvent mouseEvent) {
//        if (!vacation.getVacation_status()) {
//        viewModel.popAlerterror("This vacation Sold Out!");
//        return;
//        }
//        this.viewModel.setSellerStatus(order, false);
//
//        }