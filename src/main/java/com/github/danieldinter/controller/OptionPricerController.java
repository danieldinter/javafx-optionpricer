package com.github.danieldinter.controller;

import com.github.danieldinter.MainApp;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import logic.OptionPricer;
import logic.enums.OptionStyle;
import logic.enums.OptionType;

public class OptionPricerController {

	@FXML
	private Label resultLabel;

	@FXML
	private Button clearButton;
	@FXML
	private Button calculateButton;

	@FXML
	private ChoiceBox<OptionStyle> optionStyleChoiceBox;
	@FXML
	private ChoiceBox<OptionType> optionTypeChoiceBox;

	@FXML
	private TextField strikeTextField;
	@FXML
	private TextField initialUnderlyingPriceTextField;
	@FXML
	private TextField volatilityTextField;
	@FXML
	private TextField timestepLengthTextField;
	@FXML
	private TextField interestRateTextField;
	@FXML
	private TextField timestepsTextField;

	// Reference to the main application.
	private MainApp mainApp;

	/**
	 * The constructor. The constructor is called before the initialize()
	 * method.
	 */
	public OptionPricerController() {
	}

	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {
		// Initialize the person table with the two columns.
		optionStyleChoiceBox.setItems(FXCollections.observableArrayList(OptionStyle.values()));
		optionTypeChoiceBox.setItems(FXCollections.observableArrayList(OptionType.values()));
	}

	/**
	 * Is called by the main application to give a reference back to itself.
	 * 
	 * @param mainApp
	 */
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;

		// Add observable list data to the table
	}
	
	private void checkValidFloatInput(TextField tf, String desc, StringBuffer sb) {
		if (strikeTextField.getText() == null
				|| strikeTextField.getText().length() == 0) {
			sb.append("No " + desc + " given!\n");
		} else {
			try {
				Float.parseFloat(strikeTextField.getText());
			} catch (NumberFormatException e) {
				sb.append("No valid " + desc + " given (must be a float)!\n");
			}
		}		
	}
	
	private void checkValidIntegerInput(TextField tf, String desc, StringBuffer sb) {
		if (strikeTextField.getText() == null
				|| strikeTextField.getText().length() == 0) {
			sb.append("No " + desc + " given!\n");
		} else {
			try {
				Integer.parseInt(strikeTextField.getText());
			} catch (NumberFormatException e) {
				sb.append("No valid " + desc + " given (must be an integer)!\n");
			}
		}		
	}

	/**
	 * Validates the user input in the text fields.
	 * 
	 * @return true if the input is valid
	 */
	private boolean isInputValid() {
		StringBuffer errorBuffer = new StringBuffer();

		if (optionStyleChoiceBox.getValue() == null)
			errorBuffer.append("Choose an option style!\n");

		if (optionTypeChoiceBox.getValue() == null)
			errorBuffer.append("Choose an option type!\n");

		checkValidFloatInput(strikeTextField, "strike", errorBuffer);
		checkValidFloatInput(initialUnderlyingPriceTextField, "initial underlying price", errorBuffer);
		checkValidFloatInput(volatilityTextField, "volatility", errorBuffer);
		checkValidFloatInput(timestepLengthTextField, "time step length", errorBuffer);
		checkValidFloatInput(interestRateTextField, "interest rate", errorBuffer);
		checkValidIntegerInput(timestepsTextField, "timesteps", errorBuffer);

		String errorMessage = errorBuffer.toString();

		if (errorMessage.length() == 0) {
			return true;
		} else {
			// Show the error message.
			Alert alert = new Alert(AlertType.ERROR);
			//alert.initOwner(mainApp.getPrimaryStage());
			alert.setTitle("Input error");
			alert.setHeaderText("Please correct invalid fields");
			alert.setContentText(errorMessage);

			alert.showAndWait();

			return false;
		}
	}

	@FXML
	private void handleCalculate() {
		if (!isInputValid())
			return;

		OptionType optionType = optionTypeChoiceBox.getValue();
		OptionStyle optionStyle = optionStyleChoiceBox.getValue();
		float strike = Float.parseFloat(strikeTextField.getText());
		float initialUnderlyingPrice = Float
				.parseFloat(initialUnderlyingPriceTextField.getText());
		float volatility = Float.parseFloat(volatilityTextField.getText());
		float timeStepLength = Float
				.parseFloat(timestepLengthTextField.getText());
		float interestRate = Float.parseFloat(interestRateTextField.getText());
		int timeSteps = Integer.parseInt(timestepsTextField.getText());

		OptionPricer op = new OptionPricer(optionType, optionStyle, strike,
				initialUnderlyingPrice, volatility, timeStepLength,
				interestRate, timeSteps);

		resultLabel.setText(Double.toString(op.price()));
	}

	@FXML
	private void handleClear() {
		resultLabel.setText("");

		strikeTextField.setText("");
		initialUnderlyingPriceTextField.setText("");
		volatilityTextField.setText("");
		timestepLengthTextField.setText("");
		interestRateTextField.setText("");
		timestepsTextField.setText("");
	}

}