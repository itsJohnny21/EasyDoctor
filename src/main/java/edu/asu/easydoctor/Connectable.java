package edu.asu.easydoctor;

import java.sql.SQLException;

public interface Connectable {
    public void onEdit();
    public void onSave() throws SQLException;
    public void onCancel();
    public void onError();
    public String toString();
    public void initialize();
}
