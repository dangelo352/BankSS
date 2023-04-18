package com.example.banks;

import com.example.banks.Account;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.text.*;
import org.mindrot.jbcrypt.BCrypt;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements javafx.fxml.Initializable {
    // Step 2: Remove the extends Application from the BankingApp class.
// ...

// Step 3: Move the start method from BankingApp to BankingController.
// ...

    // Step 4: Add the @FXML annotation to all of the UI elements that need to be referenced in the controller.
    @FXML
    private Text scenetitle;
    @FXML
    private Label userName;
    @FXML
    private TextField userTextField;
    @FXML
    private Label pw;
    @FXML
    private PasswordField pwBox;
    @FXML
    private Button btn;
    @FXML
    private Text actiontarget;
    @FXML
    private Button btnCreate;
    @FXML
    private Button withdrawButton;
    @FXML
    private Button depositButton;
    @FXML
    private Button Logout;
    @FXML
    private Label balanceLabel;
    @FXML
    private GridPane mainGrid;
    @FXML
    private GridPane createAccountGrid;
    @FXML
    private GridPane bankingScreenGrid;
    @FXML
    private Label amount;
    @FXML
    private TextField amountField;

    // Step 5: Move all of the event handling code from the start method to the appropriate methods in the BankingController.
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //make the button be able to read from the file hash and password and match accordingly
        List<Account> accounts = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("F:\\Users\\Dangelo\\IdeaProjects\\BankSS\\src\\main\\java\\com\\example\\banks\\users.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                try {
                    accounts.add(new Account(parts[0], parts[1], Double.parseDouble(parts[2])));
                } catch (NumberFormatException e) {
                    System.err.println("Error: The third element is not a valid representation of a double");
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("Error: The file does not exist at the specified path");
        } catch (IOException e) {
            e.printStackTrace();
        }

        btn.setOnAction(event -> {
            String username = userTextField.getText();
            String password = pwBox.getText();
            boolean found = false;
            for (Account account : accounts) {
                if (account.getUsername().equals(username) && BCrypt.checkpw(password, account.getPasswordHash())) {
                    found = true;
                    Account.setCurrentUsername(username);
                    mainGrid.setVisible(false);
                    bankingScreenGrid.setVisible(true);
                    balanceLabel.setText("Balance: " + account.getBalance());
                    break;
                }
            }
            if (!found) {
                actiontarget.setFill(Color.FIREBRICK);
                actiontarget.setText("Invalid username or password");
            }
        });

        btnCreate.setOnAction(event -> {
            mainGrid.setVisible(false);
            createAccountGrid.setVisible(true);
        });
    }
}
