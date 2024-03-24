package application;

public interface Value {
    public void onEdit();
    public void onSave() throws Exception;
    public void onCancel();
    public Value connectedTo(UpdateButtonGroup updateButtonGroup);
}
