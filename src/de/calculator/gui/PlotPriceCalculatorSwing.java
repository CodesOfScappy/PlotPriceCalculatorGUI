package de.calculator.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * Ein einfaches Swing-Anwendung zur Berechnung des Grundstückspreises inklusive
 * Provision und Mehrwertsteuer.
 */
@SuppressWarnings("serial")
public class PlotPriceCalculatorSwing extends JFrame {
	private JTextField lengthField;
	private JTextField widthField;
	private JTextField pricePerSquareMeterField;
	private JLabel plotPriceLabel;
	private JLabel totalWithCommissionLabel;
	private JLabel totalWithVATLabel;

	private static final BigDecimal COMMISSION_RATE = new BigDecimal("0.05");
	private static final BigDecimal VAT_RATE = new BigDecimal("0.19");

	/**
	 * Konstruktor der Anwendung. Initialisiert die GUI-Komponenten und setzt das
	 * Layout.
	 */
	public PlotPriceCalculatorSwing() {
		setTitle("Grundstückspreis Berechnung");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800, 600);
		setResizable(false);
		setLayout(new GridLayout(0, 2, 10, 10)); // Vertikales Layout mit Abstand

		lengthField = new JTextField();
		widthField = new JTextField();
		pricePerSquareMeterField = new JTextField();

		add(new JLabel("Länge des Grundstücks (m):"));
		add(lengthField);
		add(new JLabel("Breite des Grundstücks (m):"));
		add(widthField);
		add(new JLabel("Preis pro Quadratmeter (€/m²):"));
		add(pricePerSquareMeterField);

		JButton calculateButton = new JButton("Berechnen");
		calculateButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				calculateAndDisplay();
			}
		});

		add(calculateButton);
		add(new JLabel());

		plotPriceLabel = new JLabel();
		totalWithCommissionLabel = new JLabel();
		totalWithVATLabel = new JLabel();

		add(new JLabel("Grundstückspreis:"));
		add(plotPriceLabel);
		add(new JLabel("Grundstückspreis inkl. " + (COMMISSION_RATE.multiply(new BigDecimal("100")) + "% Provision:")));
		add(totalWithCommissionLabel);
		add(new JLabel("Grundstückspreis inkl. " + (COMMISSION_RATE.multiply(new BigDecimal("100")) + "% Provision und "
				+ (VAT_RATE.multiply(new BigDecimal("100")) + "% Mehrwertsteuer:"))));
		add(totalWithVATLabel);

		pack();
		setLocationRelativeTo(null);
	}

	/**
	 * Berechnet den Grundstückspreis anhand der gegebenen Werten und zeigt die
	 * Ergebnisse auf der GUI an.
	 */
	private void calculateAndDisplay() {
		try {
			BigDecimal length = readPositiveBigDecimalInput(lengthField.getText());
			BigDecimal width = readPositiveBigDecimalInput(widthField.getText());
			BigDecimal pricePerSquareMeter = readPositiveBigDecimalInput(pricePerSquareMeterField.getText());

			BigDecimal plotPrice = calculatePlotPrice(length, width, pricePerSquareMeter);
			BigDecimal commission = calculateCommission(plotPrice);
			BigDecimal totalWithCommission = plotPrice.add(commission);
			BigDecimal vatOnCommission = calculateVAT(commission);
			BigDecimal totalWithVAT = totalWithCommission.add(vatOnCommission);

			DecimalFormat decimalFormat = new DecimalFormat("#.00");
			plotPriceLabel.setText(decimalFormat.format(plotPrice) + " €");
			totalWithCommissionLabel.setText(decimalFormat.format(totalWithCommission) + " €");
			totalWithVATLabel.setText(decimalFormat.format(totalWithVAT) + " €");
		} catch (NumberFormatException e) {
			// Fehlermeldung für ungültige Eingabe anzeigen
			JOptionPane.showMessageDialog(this, "Ungültige Eingabe. Bitte geben Sie eine positive Zahl ein.", "Fehler",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Konvertiert den gegebenen Input-String in eine positive BigDecimal-Zahl.
	 *
	 * @param input Der Input-String.
	 * @return Eine positive BigDecimal-Zahl, die aus dem Input-String konvertiert
	 *         wurde.
	 * @throws NumberFormatException Wenn der Input ungültig ist.
	 */
	private BigDecimal readPositiveBigDecimalInput(String input) {
		try {
			BigDecimal value = new BigDecimal(input);
			if (value.compareTo(BigDecimal.ZERO) > 0) {
				return value;
			} else {
				throw new NumberFormatException();
			}
		} catch (NumberFormatException e) {
			// Fehlermeldung für ungültige Eingabe anzeigen
			JOptionPane.showMessageDialog(this, "Ungültige Eingabe. Bitte geben Sie eine positive Zahl ein.", "Fehler",
					JOptionPane.ERROR_MESSAGE);
			throw e;
		}
	}

	/**
	 * Berechnet den Grundstückspreis basierend auf Länge, Breite und Preis pro
	 * Quadratmeter.
	 *
	 * @param length              Die Länge des Grundstücks.
	 * @param width               Die Breite des Grundstücks.
	 * @param pricePerSquareMeter Der Preis pro Quadratmeter.
	 * @return Der berechnete Grundstückspreis.
	 */
	private BigDecimal calculatePlotPrice(BigDecimal length, BigDecimal width, BigDecimal pricePerSquareMeter) {
		return length.multiply(width).multiply(pricePerSquareMeter);
	}

	/**
	 * Berechnet die Provision basierend auf dem gegebenen Grundstückspreis.
	 *
	 * @param plotPrice Der Grundstückspreis.
	 * @return Die berechnete Provision.
	 */
	private BigDecimal calculateCommission(BigDecimal plotPrice) {
		return plotPrice.multiply(COMMISSION_RATE);
	}

	/**
	 * Berechnet die Mehrwertsteuer basierend auf dem gegebenen Betrag.
	 *
	 * @param amount Der Betrag, auf den die Mehrwertsteuer berechnet werden soll.
	 * @return Die berechnete Mehrwertsteuer.
	 */
	private BigDecimal calculateVAT(BigDecimal amount) {
		return amount.multiply(VAT_RATE);
	}

	/**
	 * Der Einstiegspunkt der Anwendung.
	 *
	 * @param args Die Befehlszeilenargumente (nicht verwendet).
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				PlotPriceCalculatorSwing frame = new PlotPriceCalculatorSwing();
				frame.setVisible(true);
			}
		});
	}
}
