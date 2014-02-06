package wpWorker;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;

import javax.swing.JOptionPane;

import com.mysql.jdbc.MySQLConnection;

import c10n.C10N;
import dbaccess.DBModelManager;
import dbaccess.data.Employee;
import de.fhbingen.wbs.translation.Messages;
import jdbcConnection.MySqlConnect;
import jdbcConnection.SQLExecuter;
import wpOverview.WPOverviewGUI;

public class ChangePWButtonAction {

    private ChangePW changepw;
    private final Messages messages;

    public ChangePWButtonAction(ChangePW changepw) {
        messages = C10N.get(Messages.class);
        this.changepw = changepw;
        addButtonAction();
    }

    /**
     * fügt actionListener zum "Ok" und "Cancel" Buttons hinzu und ruft
     * entsprechend notwenidge Methoden auf.
     */
    public final void addButtonAction() {
        changepw.gui.txfUser.setText(changepw.usr.getName());

        changepw.gui.btnOk.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {

                if (changepw.checkFieldsFilled()) {

                    // Prüfung ob "altes Passwort korrekt"
                    if (changepw.checkOldPW()) {
                        if (changepw.checkNewPW()) {
                            if (changepw.checkRules()) {
                                Employee emp =
                                        DBModelManager.getEmployeesModel()
                                                .getEmployee(
                                                        changepw.usr.getId());
                                changepw.setNewPassword(emp);
                                WPOverviewGUI.setStatusText(messages
                                        .passwordChangeConfirm());
                                changepw.gui.dispose();
                            } else {
                                JOptionPane
                                        .showMessageDialog(
                                                changepw.gui,
                                                messages.passwordInvalidError()
                                                        + "\n"
                                                        + messages
                                                                .guidelinesPassword(),
                                                null,
                                                JOptionPane.INFORMATION_MESSAGE);
                            }
                        } else {
                            JOptionPane.showMessageDialog(changepw.gui,
                                    messages.passwordsNotMatchingError(), null,
                                    JOptionPane.INFORMATION_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(changepw.gui,
                                messages.passwordOldWrong(), null,
                                JOptionPane.INFORMATION_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(changepw.gui,
                            messages.fillAllFieldsError(), null,
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        changepw.gui.btnCancel.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                changepw.gui.dispose();
            }
        });

    }
}
