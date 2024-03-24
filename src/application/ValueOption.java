package application;

import java.util.function.Function;

import javafx.scene.control.ChoiceBox;

public class ValueOption extends ChoiceBox<String> {
    public Datum datum;
    public boolean updatable;
    public Function<String, String> converion;
    
    public ValueOption(Datum datum) {
        super();
        this.datum = datum;
        updatable = Database.canUpdate(datum.parent.tableName, datum.columnName);
        updatable = true;
        setDisable(true);
    }

    public ValueOption withConversion(Function<String, String> converion) {
        this.converion = converion;
        return this;
    }

    public void onEdit() {
        setDisable(!updatable);
    }

    public void onSave() throws Exception {
        String idk = converion.apply(getValue());
        System.out.println(idk);
        // if (!getValue().equals(datum.originalValue))  {
        //     datum.newValue = getValue();
        //     Database.updateRow(datum.parent.rowID, datum.parent.tableName, datum.columnName, datum.newValue);
        //     datum.originalValue = datum.newValue;
        // }
        setDisable(true);
    }

    public ValueOption connectedTo(UpdateButtonGroup updateButtonGroup) {
        updateButtonGroup.options.add(this);
        return this;
    }


    public void onCancel() {
        setDisable(true);
    }
    
}
