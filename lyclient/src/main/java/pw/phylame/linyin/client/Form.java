/*
 * Copyright 2014-2016 Peng Wan <phylame@163.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package pw.phylame.linyin.client;

import javax.swing.*;
import javax.swing.text.DateFormatter;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.JTextComponent;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Form extends JFrame {
    private JPanel root;
    private JTextArea textArea;
    private JTextField pubTitle;
    private JComboBox<String> pubTag;
    private JTextField pubTagName;
    private JTextField pubTagIntro;
    private JFormattedTextField pubStartTime;
    private JFormattedTextField pubEndTime;
    private JTextArea pubContent;
    private JButton resetButton;
    private JButton publishButton;
    private JLabel pubAuthor;
    private JButton clearButton;
    private JButton randomButton;
    private JList list1;
    private JTextField tfFind;
    private JButton fIndButton;
    private JList list2;
    private JButton unsubscribeButton;
    private JButton btnStartTime;
    private JButton btnEndTime;
    private JButton refreshButton;

    private LyClient client;

    Form(LyClient client) {
        super("Demo for Ly client");
        this.client = client;
        initUI();
        redirectOutput();
        resetForm();
    }

    private void initUI() {
        SimpleDateFormat format = new SimpleDateFormat(client.settings.get("pub.dateFormat", "yyyy-MM-dd h:m:s"));
        DateFormatter df = new DateFormatter(format);
        pubStartTime.setFormatterFactory(new DefaultFormatterFactory(df));
        pubEndTime.setFormatterFactory(new DefaultFormatterFactory(df));

        refreshButton.addActionListener(e -> refreshTags());
        publishButton.addActionListener(e -> publishMessage());
        resetButton.addActionListener(e -> resetForm());
        fIndButton.addActionListener(e -> findTag());
        btnStartTime.addActionListener(e -> pubStartTime.setValue(new Date()));
        btnEndTime.addActionListener(e -> pubEndTime.setValue(new Date()));

        JPopupMenu menu = new JPopupMenu();
        JMenuItem item = new JMenuItem("Clear All");
        item.addActionListener(e -> textArea.setText(null));
        menu.add(item);
        textArea.setComponentPopupMenu(menu);

        getContentPane().add(root);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        pack();
        setSize(Math.max(getWidth(), 800), Math.max(getHeight(), 720));
    }

    private void redirectOutput() {
        PrintStream out = new PrintStream(new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                textArea.append(Character.toString((char) b));
            }

            @Override
            public void write(byte[] b, int off, int len) throws IOException {
                textArea.append(new String(b, off, len));
            }
        }, true);
        System.setOut(out);
        System.setErr(out);
    }

    private void findTag() {
        String s = tfFind.getText();
        if (StringUtils.isBlank(s)) {
            GuiUtils.error("No search content");
            return;
        }

    }

    private boolean validateForm() {
        if (!notBlank(pubTitle, "Title of message cannot be empty or blank")) {
            return false;
        }
        if (!notBlank(pubContent, "Content of message cannot be empty or blank")) {
            return false;
        }
        if (!notNull(pubStartTime, "Start time of message is empty")) {
            return false;
        }
        if (!notNull(pubEndTime, "End time of message is empty")) {
            return false;
        }
        if (pubTag.getSelectedIndex() == pubTag.getItemCount() - 1) {
            GuiUtils.error("No tag selected");
            pubTag.requestFocus();
            return false;
        }

        return true;
    }

    private boolean notBlank(JTextComponent tc, String msg) {
        if (StringUtils.isBlank(tc.getText())) {
            GuiUtils.error(msg);
            tc.requestFocus();
            return false;
        }
        return true;
    }

    private boolean notNull(JFormattedTextField tf, String msg) {
        if (tf.getValue() == null) {
            GuiUtils.error(msg);
            tf.requestFocus();
            return false;
        }
        return true;
    }

    private void publishMessage() {
        if (!validateForm()) {
            return;
        }
    }

    private void resetForm() {
        pubAuthor.setText(client.user.getName());
        refreshTags();
        pubStartTime.setValue(DateUtils.calculateDateByNow('h', 1));
        pubEndTime.setValue(DateUtils.calculateDateByNow('d', 1));
    }

    private void refreshTags() {
        pubTag.removeAllItems();
        pubTag.addItem("--New Tag--");
    }
}
