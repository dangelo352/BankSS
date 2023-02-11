package com.example.banks;

import javafx.application.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.text.*;
import javafx.stage.*;
import org.mindrot.jbcrypt.BCrypt;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BankingApp extends Application {

    double balance = 1000;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Banking Application");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Text scenetitle = new Text("       Bank Of Florida Poly");
        scenetitle.setFont(Font.font("Comic Sans", FontWeight.BOLD, 20));
        grid.add(scenetitle, 0, 0, 2, 1);

        Label userName = new Label("Username:");
        grid.add(userName, 0, 1);

        TextField userTextField = new TextField();
        grid.add(userTextField, 1, 1);

        Label pw = new Label("Password:");
        grid.add(pw, 0, 2);

        PasswordField pwBox = new PasswordField();
        grid.add(pwBox, 1, 2);

        Button btn = new Button("Sign in");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);
        grid.add(hbBtn, 1, 4);

        final Text actiontarget = new Text();
        grid.add(actiontarget, 1, 6);

        Button btnCreate = new Button("Create Account");
        HBox hbBtnCreate = new HBox(10);
        hbBtnCreate.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtnCreate.getChildren().add(btnCreate);
        grid.add(hbBtnCreate, 0, 4);

        btnCreate.setOnAction(event -> {
            showCreateAccountView(primaryStage);
        });

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



        Scene scene = new Scene(grid, 300, 275);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    private void showMainView(Stage primaryStage) {


        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        grid.setStyle("-fx-background-color: white;");

        Text scenetitle = new Text("Banking Transactions");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);


        Button withdrawButton = new Button("Withdraw");
        withdrawButton.setTextFill(Color.WHITE);
        withdrawButton.setStyle("-fx-background-color: #ff0000;");
        HBox hbWithdrawButton = new HBox(10);
        hbWithdrawButton.setAlignment(Pos.BOTTOM_RIGHT);
        hbWithdrawButton.getChildren().add(withdrawButton);
        grid.add(hbWithdrawButton, 0, 2);

        withdrawButton.setOnAction(event -> {
            showBankingScreen(primaryStage, "Withdraw");
        });

        Button depositButton = new Button("Deposit");
        depositButton.setTextFill(Color.WHITE);
        depositButton.setStyle("-fx-background-color: red;");
        HBox hbDepositButton = new HBox(10);
        hbDepositButton.setAlignment(Pos.BOTTOM_LEFT);
        hbDepositButton.getChildren().add(depositButton);
        grid.add(hbDepositButton, 1, 2);

        depositButton.setOnAction(event -> {
            //
            showBankingScreen(primaryStage, "Deposit");
        });
        //makes a balance label and add it to the grid also make sure that it shows up in the deposit screen and withdraw screen
        Label balanceLabel = new Label("Balance: " + balance);

        grid.add(balanceLabel, 0, 4);
        Scene scene = new Scene(grid, 300, 275);
        primaryStage.setScene(scene);

    }

    private void showCreateAccountView(Stage primaryStage) {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Text scenetitle = new Text("Create Account");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);

        Label userName = new Label("Username:");
        grid.add(userName, 0, 1);

        TextField userTextField = new TextField();
        grid.add(userTextField, 1, 1);

        Label pw = new Label("Password:");
        grid.add(pw, 0, 2);

        PasswordField pwBox = new PasswordField();
        grid.add(pwBox, 1, 2);

        Button btn = new Button("Create");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);
        grid.add(hbBtn, 1, 4);

        final Text actiontarget = new Text();
        grid.add(actiontarget, 1, 6);
        //add back button
        Button back = new Button("Back");
        HBox backbtn = new HBox(10);
        backbtn.setAlignment(Pos.BOTTOM_LEFT);
        backbtn.getChildren().add(back);
        grid.add(backbtn, 2, 4);

        grid.add(back, 1, 4);


        back.setOnAction(event -> {
            start(primaryStage);
        });

        btn.setOnAction(event -> {
            String username = userTextField.getText();
            String password = pwBox.getText();
            String passwordHash = BCrypt.hashpw(password, BCrypt.gensalt());

            try (FileWriter fw = new FileWriter("F:\\Users\\Dangelo\\IdeaProjects\\BankS\\src\\main\\java\\com\\example\\banks\\users.txt", true)) {
                fw.write(username + "," + passwordHash + "," + balance + "\n");
                actiontarget.setFill(Color.GREEN);
                actiontarget.setText("Account created successfully");
            } catch (IOException e) {
                e.printStackTrace();
            }

        });

        Scene scene = new Scene(grid, 300, 275);
        primaryStage.setScene(scene);
        primaryStage.show();



    }



//lets add balance amounts saved to each account made


    private void showBankingScreen(Stage primaryStage, String type) {

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        grid.setStyle("-fx-background-color: white;");

        Text scenetitle = new Text(type + " Transactions");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);

        Label amount = new Label("Amount:");
        grid.add(amount, 0, 2);

        TextField amountField = new TextField();
        grid.add(amountField, 1, 2);

        Button btn = new Button(type);
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);
        grid.add(hbBtn, 1, 4);


        final Text actiontarget = new Text();
        grid.add(actiontarget, 1, 6);
//Make a Deposit button set on action and a withdraw button set on action and deposit adds money to the balance and withdraws money from the balance


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





        Scene scene = new Scene(grid, 300, 275);
        primaryStage.setScene(scene);
        primaryStage.show();



    }





}