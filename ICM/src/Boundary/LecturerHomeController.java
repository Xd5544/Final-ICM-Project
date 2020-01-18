package Boundary;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import Client.Func;
import Entity.Employee;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuButton;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class LecturerHomeController implements Initializable {
	@FXML
	private Button notifications;
	@FXML
	private Button Homebtn;
	@FXML
	private Button RequestWorkedOnbtn;
	@FXML
	private Button RequestSubmissionbtn;
	@FXML
	private Button ProfileSettingbtn;
	@FXML
	private Button AboutICMbtn;
	@FXML
	private ComboBox Usercombobtn;
	@FXML
	private SplitPane splitpane;
	@FXML
	private AnchorPane lowerAnchorPane;
	@FXML
	private MenuButton UserNameMenu;
	public static Stage primaryStage;
	private static Employee lecturer;
	public static MyRequestsController MyRequests;
	public static ProfileSettingController ProfileSetting;
	public static LecturerHomeController l;


	public void start(Employee lecturer) {
		this.lecturer=lecturer;
		l=this;
		primaryStage=LoginController.primaryStage;

		Platform.runLater(new Runnable() {

			@Override
			public void run() {
			try{
				Parent root = FXMLLoader.load(getClass().getResource("/Boundary/Lecturer-Home.fxml"));
				Scene scene = new Scene(root);
				primaryStage.setScene(scene);
				primaryStage.setResizable(false);
				primaryStage.setTitle("ICM");
				primaryStage.show();
				primaryStage.setOnCloseRequest(event -> {
					System.out.println("EXIT ICM");
					LogOutController logOut = new LogOutController();
					logOut.exit(primaryStage, lecturer);
				});
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	});
}
public Stage getPrimaryStage() {
		return primaryStage;
	}

public void GoToHome(ActionEvent event) throws Exception {
	HomeController home = new HomeController();
	runLater(() -> {
		home.start(splitpane);
	});
}

public void RequestSubmissionAction(ActionEvent event) throws Exception {
	RequestSubmissionController Submit = new RequestSubmissionController();
	runLater(() -> {
		Submit.start(splitpane, lecturer);
	});
}

public void ProfileSettingAction(ActionEvent event) throws Exception {
	ProfileSetting = new ProfileSettingController();
	runLater(() -> {
		ProfileSetting.start(splitpane, lecturer, "Lecturer");
	});
}

public void MyRequestsAction(ActionEvent event) throws Exception {
	MyRequests = new MyRequestsController();
	runLater(() -> {
		MyRequests.start(splitpane, lecturer, "Lecturer");
	});
}

public void AboutICMAction(ActionEvent event) throws Exception {
	AboutICMController about = new AboutICMController();
	runLater(() -> {
	about.start(splitpane);
	});
}

public void LogOutAction(ActionEvent event) throws Exception {
	LogOutController logOut = new LogOutController();
	primaryStage.close();
	runLater(() -> {
	logOut.start(primaryStage, lecturer);
	});
}
@Override
public void initialize(URL location, ResourceBundle resources) {
	// TODO Auto-generated method stub
	UserNameMenu.setText(lecturer.getFirstName() + " " + lecturer.getLastName());
}

private void runLater(Func f) {
	f.call();
	Platform.runLater(() -> {
		try {
			Thread.sleep(10);
			f.call();

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	});
}


}
	
