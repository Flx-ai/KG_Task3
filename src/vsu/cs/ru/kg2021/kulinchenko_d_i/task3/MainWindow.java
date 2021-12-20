package vsu.cs.ru.kg2021.kulinchenko_d_i.task3;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {

    public MainWindow() throws HeadlessException {
        this.setSize(1600, 1200);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.add(new DrawPanel());
    }
}
