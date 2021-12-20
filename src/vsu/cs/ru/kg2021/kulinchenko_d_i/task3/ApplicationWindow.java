package vsu.cs.ru.kg2021.kulinchenko_d_i.task3;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ApplicationWindow extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JScrollPane workFieldPanel;
    private JTextField valueXTextField;
    private DrawPanel drawPanel;
    private JTextField valueYTextField;
    private JTextField valueRadiusTextField;
    private JButton applyButton;

    public ApplicationWindow() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        applyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    double firstInput = Double.parseDouble(valueXTextField.getText());
                    double secondInput = Double.parseDouble(valueYTextField.getText());
                    double thirdInput = Double.parseDouble(valueRadiusTextField.getText());
                    drawPanel.applyValuesForCircle(firstInput, secondInput, thirdInput);
                } catch (Exception exception) {

                }

            }
        });
        drawPanel.setEditor(new DrawPanel.Editor() {
            @Override
            public void toEditCircle(Circle circle) {
                valueXTextField.setText("" + circle.getFirstPoint().getRealX());
                valueYTextField.setText("" + circle.getFirstPoint().getRealY());
                double radius = Math.sqrt((circle.getSecondPoint().getRealX() - circle.getFirstPoint().getRealX()) * (circle.getSecondPoint().getRealX() - circle.getFirstPoint().getRealX())
                        + (circle.getSecondPoint().getRealY() - circle.getFirstPoint().getRealY()) * (circle.getSecondPoint().getRealY() - circle.getFirstPoint().getRealY()));
                valueRadiusTextField.setText("" + radius);
                applyButton.setEnabled(true);
            }

            @Override
            public void cancel() {
                valueXTextField.setText("");
                valueYTextField.setText("");
                valueRadiusTextField.setText("");
                applyButton.setEnabled(false);
            }
        });
    }

    public static void main(String[] args) {
        ApplicationWindow dialog = new ApplicationWindow();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
