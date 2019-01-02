package View.ProfileView;



import java.net.URL;
import java.util.ResourceBundle;

import App.User;
import Main.ViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

public class ProfileViewController implements Initializable{

        @FXML
        public javafx.scene.control.Label first_name;
        public javafx.scene.control.Label last_name;
        public javafx.scene.control.Label email;
        public javafx.scene.control.Label password;
        public javafx.scene.control.Label birth_date;
        public javafx.scene.control.Label city;

        private ViewModel viewModel;
        private User user;

        @Override
        public void initialize(URL url, ResourceBundle rb) {

            user=viewModel.getUser();
            if(this.viewModel.isUserExists(user)){
                this.first_name.setText(user.getFirst_name());
                this.last_name.setText(user.getLast_name());
                this.email.setText(user.getEmail());
                this.password.setText(user.getPassword());
                this.birth_date.setText((user.getBirth_date().toString()));
                this.city.setText(user.getCity());}
            else{
                viewModel.popAlerterror("Please Sign in!");
            }
        }



    public void setViewModel(ViewModel viewModel) { this.viewModel = viewModel; }


}
