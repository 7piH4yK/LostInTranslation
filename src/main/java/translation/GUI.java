package translation;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import java.awt.event.*;
import java.awt.Dimension;
import java.util.List;
import java.util.ArrayList;
import java.util.Locale;

public class GUI {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CountryCodeConverter countryConverter = new CountryCodeConverter();
            LanguageCodeConverter languageConverter = new LanguageCodeConverter();
            Translator translator = new JSONTranslator();

            List<String> countryCodes = translator.getCountryCodes();
            String[] countryNames = countryCodes.stream()
                    .map(countryConverter::fromCountryCode)
                    .toArray(String[]::new);
            JList<String> countryList = new JList<>(countryNames);
            countryList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            JScrollPane countryScrollPane = new JScrollPane(countryList);
            countryScrollPane.setPreferredSize(new Dimension(200, 200));

            JPanel countryPanel = new JPanel();
            countryPanel.add(new JLabel("Country:"));
            countryPanel.add(countryScrollPane);

            List<String> languageCodes = translator.getLanguageCodes();
            String[] languageNames = languageCodes.stream()
                    .map(languageConverter::fromLanguageCode)
                    .toArray(String[]::new);
            JComboBox<String> languageDropdown = new JComboBox<>(languageNames);

            JPanel languagePanel = new JPanel();
            languagePanel.add(new JLabel("Language:"));
            languagePanel.add(languageDropdown);

            JPanel resultPanel = new JPanel();
            JLabel resultLabel = new JLabel("Translation:");
            resultPanel.add(resultLabel);

            ListSelectionListener updateTranslation = e -> {
                String countryName = countryList.getSelectedValue();
                String languageName = (String) languageDropdown.getSelectedItem();

                if (countryName != null && languageName != null) {
                    String countryCode = countryConverter.fromCountry(countryName);
                    String languageCode = languageConverter.fromLanguage(languageName);

                    String translation = translator.translate(countryCode.toLowerCase(), languageCode.toLowerCase());
                    if (translation == null) translation = "No translation found!";
                    resultLabel.setText(translation);
                }
            };

            countryList.addListSelectionListener(updateTranslation);
            languageDropdown.addActionListener(e -> updateTranslation.valueChanged(null));

            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
            mainPanel.add(countryPanel);
            mainPanel.add(languagePanel);
            mainPanel.add(resultPanel);

            JFrame frame = new JFrame("Country Name Translator");
            frame.setContentPane(mainPanel);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);
        });
    }
}
