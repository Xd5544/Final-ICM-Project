package Boundary;

import java.io.IOException;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.ResourceBundle;

import Client.ClientConsole;
import Entity.Phase;
import Entity.Request;
import Entity.RequestPhase;
import Entity.State;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
/**
 * set Duration to request must take approve from the inspector 
 * 
 *
 */
public class SetDurationController implements Initializable {
	public static SetDurationController duratin;
	public static Stage primaryStage;
	private static ClientConsole cc;
	private AnchorPane lowerAnchorPane;
	@FXML
	private static SplitPane splitpane;
	@FXML
	private DatePicker startDate;
	@FXML
	private DatePicker dueDate;
	@FXML
	private Button save;
	@FXML
	private Label note;
	@FXML
	private Label note2;
	@FXML
	private TextArea ExtensionReasonText;
	@FXML
	private TextField RequestID;
	@FXML
	private TextField RequestPhase;
	@FXML
	private TextField ReaminingTimeForThisPhase;
	@FXML
	private DatePicker dueDateForExtend;
	@FXML
	private Button SendExtraTimeBtn;

	private static RequestPhase rp;
	private static Phase phase;
/**
 * Start set duration GUI
 * @param splitpane GUI
 * @param path path of the GUI
 * @param rp Request-phase
 */
	public void start(SplitPane splitpane, String path, RequestPhase rp) {
		this.splitpane = splitpane;
		primaryStage = LoginController.primaryStage;
		this.cc = LoginController.cc;
		this.rp = rp;
		this.phase = rp.getPhase();

		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
			lowerAnchorPane = loader.load();
			duratin = loader.getController();
			splitpane.getItems().set(1, lowerAnchorPane);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
/**
 * on the save button will send to inspector the duration of the request waiting for approve
 */
	public void saveBtn() {
		LocalDate start = startDate.getValue();
		LocalDate due = dueDate.getValue();
		LocalDate today = LocalDate.now();

		if (start != null && due != null & due.compareTo(start) >= 0 && start.compareTo(today) >= 0) {
			Alert alertWarning = new Alert(AlertType.CONFIRMATION);
			alertWarning.setTitle("Warning Alert Title");
			alertWarning.setHeaderText("confirm!");
			alertWarning.setContentText("Are you sure about the dates you intered, you can't change it later!");
			Optional<ButtonType> result = alertWarning.showAndWait();
			ButtonType button = result.orElse(ButtonType.CANCEL);
			if (button == ButtonType.OK) {
				try {
					LocalDate s = startDate.getValue();
					LocalDate d = dueDate.getValue();
					String keymessage = "save duration";
					LocalDate date[] = { s, d };
					Object[] message = { keymessage, rp.getId(), date, phase };

					LoginController.cc.getClient().sendToServer(message);
					save.setDisable(true);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		} else {
			Alert alertWarning = new Alert(AlertType.WARNING);
			alertWarning.setHeaderText("Warning!");
			alertWarning.setContentText("Please check the dates correctly");
			alertWarning.showAndWait();
		}
	}
/**
 * send extra time request to Inspector for the choosen request 
 * @param e ActionEvent ( Clicking on button)
 */
	public void SendExtraTimeRequestBtn(ActionEvent e) {

		boolean ExtensionReason = ExtensionReasonText.getText().equals("");
		LocalDate due = dueDate.getValue();
		if (rp.getStartDate() == null) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Send Extension Time Request");
			alert.setHeaderText("ERROR");
			alert.setContentText("you should set duration");
			alert.showAndWait();
			return;
		}
		if (rp.getState().equals(State.waitingForApprove)) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Send Extension Time Request");
			alert.setHeaderText("ERROR");
			alert.setContentText("the inspector should approve the duration");
			alert.showAndWait();
			return;
		}
		if (ExtensionReason || due == null) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Send Extension Time Request");
			alert.setHeaderText("ERROR");
			alert.setContentText("please fill all the fields need the red star");
			alert.showAndWait();
			return;

		} else {
			Alert alertWarning = new Alert(AlertType.INFORMATION);
			alertWarning.setTitle("Warning Alert Title");
			alertWarning.setHeaderText("confirm!");
			alertWarning.setContentText("Extension request needed Inspector confirmation!");
			Optional<ButtonType> result = alertWarning.showAndWait();
			ButtonType button = result.orElse(ButtonType.CANCEL);
			if (button == ButtonType.OK) {
				try {

					String keymessage = "send Request extension to inspector for confirm";
					LocalDate d = dueDateForExtend.getValue();
					String Reason = ExtensionReasonText.getText();
					Object[] message = { keymessage, rp, d, Reason };

					LoginController.cc.getClient().sendToServer(message);

				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		}
	}
/**
 * Initialize  Request data from chosen request from table list
 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		if (RequestsWorkedOnController.getRP().getState().equals(State.waitingForApprove)) {
			note.setVisible(false);
			note.setText("this duratin wating for Inapector approve");
			note.setVisible(true);
			save.setDisable(true);
		} else if (RequestsWorkedOnController.getRP().getState().equals(State.wait)
				&& RequestsWorkedOnController.getRP().getStartDate() != null
				&& RequestsWorkedOnController.getRP().getDueDate() != null) {
			note.setVisible(false);
			note.setText("* The Duration after inspector check");
			note.setVisible(true);
			save.setDisable(true);
		}
		if (RequestsWorkedOnController.getRP().getStartDate() != null
				&& RequestsWorkedOnController.getRP().getDueDate() != null) {
			save.setDisable(true);
			dueDate.setValue(RequestsWorkedOnController.getRP().getDueDate().toLocalDate());
			startDate.setValue(RequestsWorkedOnController.getRP().getStartDate().toLocalDate());
		}

		if (RequestsWorkedOnController.getRP().getStartDate() != null
				&& RequestsWorkedOnController.getRP().getDueDate() != null) {
			long millis = System.currentTimeMillis();
			Date date = new java.sql.Date(millis);
			long diff = RequestsWorkedOnController.getRP().getDueDate().getTime() - date.getTime();
			long diffdays = diff / (24 * 60 * 60 * 1000);
			long diffHours = diff / (60 * 60 * 1000) - (diffdays * 24);
			save.setDisable(true);
			dueDate.setValue(RequestsWorkedOnController.getRP().getDueDate().toLocalDate());
			startDate.setValue(RequestsWorkedOnController.getRP().getStartDate().toLocalDate());

			RequestID.setText(Integer.toString(RequestsWorkedOnController.getRP().getId()));
			RequestPhase.setText(RequestsWorkedOnController.getRP().getPhase().toString());
			ReaminingTimeForThisPhase
					.setText(String.valueOf(diffdays) + " Days and " + String.valueOf(diffHours) + " Hours");
			if (diffdays < 3) {
				SendExtraTimeBtn.setDisable(true);
				note2.setVisible(false);
				note2.setText("you can't ask for an extension, the due date is less than 3 days away!");
				note2.setVisible(true);
			}
		}

	}
}
