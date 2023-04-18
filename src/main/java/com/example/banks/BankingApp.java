package com.example.banks;

import org.comtel2000.keyboard.control.KeyboardPane;

import javafx.application.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.text.*;
import javafx.stage.*;
import org.mindrot.jbcrypt.BCrypt;
import javafx.application.Platform;
//we need to import the onscreen keyboard

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class BankingApp extends Application {


    private double balance = 0;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Set up the login screen
        primaryStage.setTitle("Bank Of Florida Poly - Login");
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        grid.setStyle("-fx-background-color: #F5F5F5;");

        // Add Bank logo
        //not shown in the image, but the image is centered in the grid
        Image image = new Image("https://th.bing.com/th/id/OIP.H6YhEG46sz1rmIvyyRdwAgHaHh?pid=ImgDet&rs=1");
        ImageView imageView = new ImageView(image);
        //MAKE THE IMAGE FIT IN A PHONE SCREEN
        //NEED IT RIGHT A LITTLE BIT
        GridPane.setHalignment(imageView,HPos.CENTER);

        grid.add(imageView, 0, 0, 2, 1);




        // Add Welcome Text
        Text welcomeText = new Text("Welcome to Bank Of Florida Poly");
        welcomeText.setFont(Font.font("Tahoma", FontWeight.BOLD, 40));
        grid.add(welcomeText, 0, 1, 2, 1);

        // Add Username field
        Label userName = new Label("Username:");
        userName.setFont(Font.font("Tahoma", FontWeight.BOLD, 20));
        grid.add(userName, 0, 2);

        TextField userTextField = new TextField();
        userTextField.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(userTextField, 1, 2);

        // Add Password field
        Label pw = new Label("Password:");
        pw.setFont(Font.font("Tahoma", FontWeight.BOLD, 20));
        grid.add(pw, 0, 3);

        PasswordField pwBox = new PasswordField();
        pwBox.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(pwBox, 1, 3);
// Add on-screen keyboard to the Password field

// we need to add the keyboard to the grid

        // Add Login button
        Button btn = new Button("Sign in");
        btn.setFont(Font.font("Tahoma", FontWeight.BOLD, 20));
        btn.setTextFill(Color.WHITE);
        btn.setStyle("-fx-background-color: #00A3E3;");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.CENTER_RIGHT);
        hbBtn.getChildren().add(btn);
        grid.add(hbBtn, 1, 4);

        // Add Create Account button
        Button btnCreate = new Button("Create Account");
        btnCreate.setFont(Font.font("Tahoma", FontWeight.BOLD, 20));
        btnCreate.setStyle("-fx-background-color: #00A3E3;");
        btnCreate.setTextFill(Color.WHITE);
        HBox hbBtnCreate = new HBox(10);
        hbBtnCreate.setAlignment(Pos.BOTTOM_LEFT);
        hbBtnCreate.getChildren().add(btnCreate);
        grid.add(hbBtnCreate, 0, 4);

        // Add Error message Text
        final Text actiontarget = new Text();
        actiontarget.setFont(Font.font("Tahoma", FontWeight.BOLD, 20));
        grid.add(actiontarget, 1, 5);

        // Create an ArrayList to store the account information read from the file
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

        // Set up the "Create Account" button to show the account creation view
        btnCreate.setOnAction(event -> {
            showCreateAccountView(primaryStage);
        });

//make the button be able to read from the file hash and password and match accordingly


        btn.setOnAction(event -> {
            String username = userTextField.getText();
            String enteredPassword = pwBox.getText();

            Account foundAccount = null;
            for (Account account : accounts) {
                if (account.getUsername().equals(username)) {
                    foundAccount = account;
                    break;
                }
            }

            if (foundAccount != null && BCrypt.checkpw(enteredPassword, foundAccount.getPasswordHash())) {
                actiontarget.setFill(Color.GREEN);
                actiontarget.setText("Login Successful");

                // Store the balance for the logged-in user
                balance = ((foundAccount.getBalance()));

                // Set the current username
                Account.setCurrentUsername(username);

                showMainView(primaryStage);
            } else {
                actiontarget.setFill(Color.RED);
                actiontarget.setText("Login Failed");
            }
        });
        //lets add the iphone keyboard showing up when the user clicks on the text field
        // Add a touch event handler to the text field


// Add a touch event handler to the password field



        // Get the user's screen dimensions
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

        // Set the scene size to a percentage of the screen size
        Scene scene = new Scene(grid, screenBounds.getWidth() * 0.8, screenBounds.getHeight() * 0.8);

        // Set the stage size to match the scene size
        primaryStage.setScene(scene);

        // Show the stage
        primaryStage.show();

    }


    private void showMainView(Stage primaryStage) {
        // Create a new pane to hold the main view components
        BorderPane pane = new BorderPane();
        pane.setPadding(new Insets(20));

        // Add the sidebar to the left
        VBox sidebar = new VBox(20);
        sidebar.setPadding(new Insets(20));
        sidebar.setAlignment(Pos.TOP_LEFT);
        pane.setLeft(sidebar);

        // Add the bank name to the sidebar
        Text bankName = new Text("Bank Of Florida Poly");
        bankName.setFont(Font.font("Tahoma", FontWeight.BOLD, 20));
        sidebar.getChildren().add(bankName);

        // Add a separator to the sidebar
        Separator separator = new Separator();
        separator.setPrefWidth(150);
        sidebar.getChildren().add(separator);

        // Add the user's name to the sidebar
        Text username = new Text("Logged in as: " + Account.getCurrentUsername());
        username.setFont(Font.font("Tahoma", FontWeight.NORMAL, 16));
        sidebar.getChildren().add(username);

        // Add a separator to the sidebar
        separator = new Separator();
        separator.setPrefWidth(150);
        sidebar.getChildren().add(separator);

        // Add the bank name to the sidebar
        Text Broke  = new Text("Broke status: " + balance);
        //tell them they broke if balance is less than 50000
        if (balance < 1000) {
            Broke.setFill(Color.RED);
        }
        //hide if they are not broke
        else {
            Broke.setVisible(false);
        }
        Text Rich  = new Text("Rich status: " + balance);
        if (balance > 50000) {
            Rich.setFill(Color.GREEN);
        }
        //hide if they are not rich
        else {
            Rich.setVisible(false);
        }
        Rich.setFont(Font.font("Tahoma", FontWeight.BOLD, 20));

        Broke.setFont(Font.font("Tahoma", FontWeight.BOLD, 20));
        sidebar.getChildren().add(Broke);
        sidebar.getChildren().add(Rich);


        // Add a separator to the sidebar
         separator = new Separator();
        separator.setPrefWidth(150);
        sidebar.getChildren().add(separator);
        //IF THEY GOT 50000 OR MORE LETS MAKE A RICH MAN STATUS



        // Add a label for the title
        Label titleLabel = new Label("Banking Transactions");
        titleLabel.setFont(Font.font("Tahoma", FontWeight.BOLD, 30));
        pane.setTop(titleLabel);

        // Create a VBox to hold the buttons and balance label
        VBox vbox = new VBox(20);
        vbox.setAlignment(Pos.CENTER);
        pane.setCenter(vbox);

        // Add a button for withdrawing money
        Button withdrawButton = new Button("Withdraw");
        withdrawButton.setTextFill(Color.WHITE);
        withdrawButton.setStyle("-fx-background-color: #ff0000; -fx-font-size: 20px; -fx-padding: 10px 20px;");
        withdrawButton.setOnAction(event -> {
            showBankingScreen(primaryStage, "Withdraw");
        });
        vbox.getChildren().add(withdrawButton);

        // Add a button for depositing money
        Button depositButton = new Button("Deposit");
        depositButton.setTextFill(Color.WHITE);
        depositButton.setStyle("-fx-background-color: red; -fx-font-size: 20px; -fx-padding: 10px 20px;");
        depositButton.setOnAction(event -> {
            showBankingScreen(primaryStage, "Deposit");
        });
        vbox.getChildren().add(depositButton);

        // Add a label for the balance
        Label balanceLabel = new Label("Balance: " + balance);
        balanceLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        vbox.getChildren().add(balanceLabel);

        // Add a button to log out
        Button logoutButton = new Button("Logout");
        logoutButton.setStyle("-fx-background-color: #e30909; -fx-font-size: 16px; -fx-padding: 10px 20px;");
        logoutButton.setOnAction(event -> {
            start(primaryStage);
        });
        pane.setRight(logoutButton);

        // Create a new scene and set it on the primary stage
        // Get the user's screen dimensions
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

        // Set the scene size to a percentage of the screen size
        Scene scene = new Scene(pane, screenBounds.getWidth() * 0.8, screenBounds.getHeight() * 0.8);

        // Set the stage size to match the scene size
        primaryStage.setScene(scene);

        // Show the stage
        primaryStage.show();
    }


    private void showCreateAccountView(Stage primaryStage) {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        grid.setStyle("-fx-background-color: #F5F5F5;");

        Text scenetitle = new Text("Create Account");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 30));
        grid.add(scenetitle, 0, 0, 2, 1);

        Label userName = new Label("Username:");
        userName.setFont(Font.font("Tahoma", FontWeight.NORMAL, 18));
        grid.add(userName, 0, 1);

        TextField userTextField = new TextField();
        userTextField.setFont(Font.font("Tahoma", FontWeight.NORMAL, 18));
        grid.add(userTextField, 1, 1);




        Label pw = new Label("Password:");
        pw.setFont(Font.font("Tahoma", FontWeight.NORMAL, 18));
        grid.add(pw, 0, 2);

        PasswordField pwBox = new PasswordField();
        pwBox.setFont(Font.font("Tahoma", FontWeight.NORMAL, 18));
        grid.add(pwBox, 1, 2);

        Button btn = new Button("Create");
        btn.setFont(Font.font("Tahoma", FontWeight.NORMAL, 18));
        btn.setTextFill(Color.WHITE);
        btn.setStyle("-fx-background-color: #4285F4;");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_LEFT);
        hbBtn.getChildren().add(btn);
        grid.add(hbBtn, 1, 4);

        final Text actiontarget = new Text();
        actiontarget.setFont(Font.font("Tahoma", FontWeight.NORMAL, 18));
        grid.add(actiontarget, 1, 5);

        //add back button
        Button back = new Button("Back");
        back.setFont(Font.font("Tahoma", FontWeight.NORMAL, 18));
        back.setTextFill(Color.WHITE);
        back.setStyle("-fx-background-color: #DB4437;");
        HBox backbtn = new HBox(10);
        backbtn.setAlignment(Pos.TOP_LEFT);
        backbtn.getChildren().add(back);
        grid.add(backbtn, 2, 0);

        back.setOnAction(event -> {
            start(primaryStage);
        });

        btn.setOnAction(event -> {
            String username = userTextField.getText();
            String password = pwBox.getText();
            String passwordHash = BCrypt.hashpw(password, BCrypt.gensalt());

            try (FileWriter fw = new FileWriter("F:\\Users\\Dangelo\\IdeaProjects\\BankSS\\src\\main\\java\\com\\example\\banks\\users.txt", true)) {
                fw.write(username + "," + passwordHash + "," + balance + "\n");
                actiontarget.setFill(Color.GREEN);
                actiontarget.setText("Account created successfully");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        //i need to make a iphone keyboard in replit pop up when i click on the text field
        //how do i do that
        //answer: i cant do that in replit so i will just do it in intellij and then copy and paste the code here
        // Show the keyboard when the TextField is clicked



        // Get the user's screen dimensions
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

        // Set the scene size to a percentage of the screen size
        Scene scene = new Scene(grid, screenBounds.getWidth() * 0.8, screenBounds.getHeight() * 0.8);

        // Set the stage size to match the scene size
        primaryStage.setScene(scene);

        // Show the stage
        primaryStage.show();

    }


//lets add balance amounts saved to each account made

    //how

    private void showBankingScreen(Stage primaryStage, String type) {

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(20);
        grid.setPadding(new Insets(25, 25, 25, 25));
        grid.setStyle("-fx-background-color: #F5F5F5;");

        Text scenetitle = new Text(type + " Transactions");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.BOLD, 30));
        grid.add(scenetitle, 0, 0, 2, 1);

        Label amount = new Label("Amount:");
        amount.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(amount, 0, 1);

        Label balanceLabel = new Label("Balance:");
        balanceLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(balanceLabel, 0, 4);

        TextField balanceField = new TextField();
        balanceField.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        balanceField.setText("$" + balance);
        balanceField.setEditable(false);
        grid.add(balanceField, 1, 4);


        Label nameLabel = new Label("Name:");
        nameLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(nameLabel, 0, 3);

        TextField nameField = new TextField();
        nameField.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        nameField.setText(Account.getCurrentUsername());
        nameField.setEditable(false);
        grid.add(nameField, 1, 3);


        TextField amountField = new TextField();
        amountField.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(amountField, 1, 1);

        Button btn = new Button(type);
        btn.setFont(Font.font("Tahoma", FontWeight.BOLD, 20));
        btn.setTextFill(Color.WHITE);
        if (type.equals("Withdraw")) {
            btn.setStyle("-fx-background-color: #FF5E5E;");
        } else {
            btn.setStyle("-fx-background-color: #00A3E3;");
        }
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.CENTER_RIGHT);
        hbBtn.getChildren().add(btn);
        grid.add(hbBtn, 1, 2);

        Button back = new Button("Back");
        back.setFont(Font.font("Tahoma", FontWeight.BOLD, 20));
        back.setStyle("-fx-background-color: #F5F5F5;");
        back.setTextFill(Color.BLACK);
        HBox backbtn = new HBox(10);
        backbtn.setAlignment(Pos.CENTER_LEFT);
        backbtn.getChildren().add(back);
        grid.add(backbtn, 0, 2);

        final Text actiontarget = new Text();
        actiontarget.setFont(Font.font("Tahoma", FontWeight.BOLD, 20));
        grid.add(actiontarget, 1, 3);

        back.setOnAction(event -> {
            showMainView(primaryStage);
        });

        btn.setOnAction(event -> {
            String amountString = amountField.getText();
            double amountValue;
            String currentUsername = ""; // The value of this variable should be set appropriately
            //we need to make currentUsername the username that is logged in
            currentUsername = Account.getCurrentUsername();
            List<Account> users = new ArrayList<>();

            try {
                amountValue = Double.parseDouble(amountString);

                if (type.equals("Withdraw") && amountValue > balance) {
                    actiontarget.setFill(Color.RED);
                    actiontarget.setText("Insufficient Funds");
                    return;
                }

                if (type.equals("Withdraw")) {
                    balance = balance - amountValue;
                } else if (type.equals("Deposit")) {
                    balance = balance + amountValue;
                } else {
                    actiontarget.setFill(Color.RED);
                    actiontarget.setText("Invalid type: Only 'Withdraw' or 'Deposit' allowed");
                    return;
                }

                // Read the users.txt file and store the information in an ArrayList
                try (BufferedReader br = new BufferedReader(new FileReader("F:\\Users\\Dangelo\\IdeaProjects\\BankSS\\src\\main\\java\\com\\example\\banks\\users.txt"))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        String[] tokens = line.split(",");
                        Account user = new Account(tokens[0], tokens[1], Double.parseDouble(tokens[2]));
                        users.add(user);
                        if (user.getUsername().equals(currentUsername)) {
                            user.setBalance(balance);
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Error reading from file: " + e.getMessage());
                    actiontarget.setFill(Color.RED);
                    actiontarget.setText("Error reading from file: " + e.getMessage());
                    return;
                }
                // Write the updated information back to the users.txt file
                try (BufferedWriter bw = new BufferedWriter(new FileWriter("F:\\Users\\Dangelo\\IdeaProjects\\BankSS\\src\\main\\java\\com\\example\\banks\\users.txt"))) {
                    for (Account user : users) {
                        bw.write(user.getUsername() + "," + user.getPasswordHash() + "," + user.getBalance());
                        bw.newLine();
                    }
                } catch (IOException e) {
                    System.out.println("Error writing to file: " + e.getMessage());
                    actiontarget.setFill(Color.RED);
                    actiontarget.setText("Error writing to file: " + e.getMessage());
                    return;
                }

                actiontarget.setFill(Color.GREEN);
                actiontarget.setText(type + " Successful");
                showMainView(primaryStage);
            } catch (NumberFormatException e) {
                actiontarget.setFill(Color.RED);
                actiontarget.setText("Invalid Input: Enter a valid number");
            }
        });

        // Get the user's screen dimensions
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

        // Set the scene size to a percentage of the screen size
        Scene scene = new Scene(grid, screenBounds.getWidth() * 0.8, screenBounds.getHeight() * 0.8);

        // Set the stage size to match the scene size
        primaryStage.setScene(scene);

        // Show the stage
        primaryStage.show();
    }
    }
