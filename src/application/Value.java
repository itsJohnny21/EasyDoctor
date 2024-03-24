package application;

import java.sql.SQLException;

public interface Value {
    public void onEdit();
    public void onSave() throws SQLException;
    public void onCancel();
    public void onError();
    public Value connectedTo(UpdateButtonGroup updateButtonGroup);
}
