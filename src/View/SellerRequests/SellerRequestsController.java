package View.SellerRequests;

import App.Order;
import App.TableViewClass;
import App.Vacation;
import App.Payment;
import Main.ViewModel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;


public class SellerRequestsController implements Initializable {

//seller_status true=approve, false=decline

    public TableView<TableViewClass> SaleRequstTable;
    public TableColumn<TableViewClass, Integer> colVacationId;
    public TableColumn<TableViewClass, String> colFrom;
    public TableColumn<TableViewClass, String> colTo;
    public TableColumn<TableViewClass, String> colCheckin;
    public TableColumn<TableViewClass, String> colCheckout;
    public TableColumn<TableViewClass, String> colBuyerName;
    public TableColumn<TableViewClass, String> colRequestStatus;


    private ObservableList<TableViewClass> SaleRequst;

    private TableViewClass clickedRow;

    private ViewModel viewModel;

    private Order req;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colVacationId.setCellValueFactory(new PropertyValueFactory<>("vacation_id"));
        colFrom.setCellValueFactory(new PropertyValueFactory<>("from"));
        colTo.setCellValueFactory(new PropertyValueFactory<>("to"));
        colCheckin.setCellValueFactory(new PropertyValueFactory<>("checkin"));
        colCheckout.setCellValueFactory(new PropertyValueFactory<>("checkout"));
        colBuyerName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colRequestStatus.setCellValueFactory(new PropertyValueFactory<>("seller_status"));

        colVacationId.setStyle("-fx-alignment: BASELINE_CENTER");
        colFrom.setStyle("-fx-alignment: BASELINE_CENTER");
        colTo.setStyle("-fx-alignment: BASELINE_CENTER");
        colCheckin.setStyle("-fx-alignment: BASELINE_CENTER");
        colCheckout.setStyle("-fx-alignment: BASELINE_CENTER");
        colBuyerName.setStyle("-fx-alignment: BASELINE_CENTER");
        colRequestStatus.setStyle("-fx-alignment: BASELINE_CENTER");

        colCheckin.setSortType(TableColumn.SortType.DESCENDING);
        colCheckout.setSortType(TableColumn.SortType.DESCENDING);

        SaleRequst = FXCollections.observableArrayList();

        SaleRequstTable.setRowFactory(tv -> {
            TableRow<TableViewClass> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY) {

                    this.clickedRow = row.getItem();
                    this.req= new Order(clickedRow.getSeller_email(), clickedRow.getBuyer_email(), clickedRow.getVacation_id(), false);
                    this.req.setSeller_status(clickedRow.getSellerStatus());

                }
            });
            return row;
        });


    }

    public void setViewModel(ViewModel viewModel) {
        this.viewModel = viewModel;
    }




    public void loadSellerRequests() {
        this.clickedRow = null;
        SaleRequstTable.setItems(FXCollections.observableArrayList());
        SaleRequst = FXCollections.observableArrayList();
        List<Order> SaleRequstList = viewModel.getRequestsByseller_email();
        Vacation vacation;
        String checkin;
        String checkout;
        String from;
        String to;
        String buyer_name;
        String stat;
        TableViewClass addrow;
        for (Order currentReq : SaleRequstList) {
            vacation = viewModel.getVacation(currentReq.getVacation_id());
            if (vacation.getVacation_status()) {
                from = vacation.getFrom();
                to = vacation.getto();
                checkin = vacation.toStringCheckin();
                checkout = vacation.toStringCheckout();
                buyer_name = viewModel.getUserNameByEmail(currentReq.getBuyer_email());
                stat=currentReq.toStringSellerStatus();
                addrow = new TableViewClass(currentReq.getVacation_id(), checkin, checkout, from, to, buyer_name, stat);
                addrow.setBuyer_email(currentReq.getBuyer_email());
                addrow.setSeller_email(currentReq.getSeller_email());
                SaleRequst.add(addrow);
            }
        }


        SaleRequstTable.setItems(SaleRequst);

    }


    public void ApprovePayment(MouseEvent mouseEvent) {
        if (this.clickedRow == null)
            viewModel.popAlertinfo("Please pick a Request row from the Table!");
        else{
        if (this.clickedRow.getSeller_status() == "Declined") {
            viewModel.popAlerterror("You Declined This Sale Request !");
            this.clickedRow = null;
            return;
        }
        if (this.clickedRow.getSeller_status() == "") {
            viewModel.popAlerterror("Please Approved This Sale Request first!");
            this.clickedRow = null;
            return;
        }
        if (this.clickedRow.getSeller_status() == "Approved") {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("Are you sure this buyer payed in cash?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                Payment payment=new Payment(clickedRow.getSeller_email(), clickedRow.getBuyer_email(), clickedRow.getVacation_id());
                this.viewModel.addPayment(payment);
                this.viewModel.setVacationStatus(clickedRow.getVacation_id(), false);
                this.viewModel.popAlertinfo("Your Approve of Payment successfully saved!");
                loadSellerRequests();
            }
        }

    }}



    public void ApproveSaleRequest(MouseEvent mouseEvent){
        if (this.clickedRow == null)
            viewModel.popAlertinfo("Please pick a Request row from the Table!");
        else{
            if(!viewModel.getVacationStatus(clickedRow.getVacation_id())){
                viewModel.popAlerterror("This vacation Sold Out!");
                return;
            }
            this.req.setSeller_status(true);
            this.viewModel.setSellerStatus(this.req,true);
            loadSellerRequests();

        }}

        public void DeclineSaleRequest(MouseEvent mouseEvent) {
            if (this.clickedRow == null)
                viewModel.popAlertinfo("Please pick a Request row from the Table!");
            else {
                if (!viewModel.getVacationStatus(clickedRow.getVacation_id())) {
                    viewModel.popAlerterror("This vacation Sold Out!");
                    return;
                }
                this.req.setSeller_status(false);
                this.viewModel.setSellerStatus(this.req, false);
                loadSellerRequests();

            }
        }

        public void goToDetails(MouseEvent mouseEvent) {
        if (this.clickedRow == null)
            viewModel.popAlertinfo("Please pick a Request row from the Table!");
        else
            viewModel.goToRequestDetails(this.req);
    }



}


