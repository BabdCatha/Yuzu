package net.sokato.game.yuzu;

import android.widget.EditText;

public class AddPlayerRow {

    private String editTextValue;
    private EditText edit;

    public String getEditTextValue() {
        return editTextValue;
    }

    public void setEditTextValue(String editTextValue) {
        this.editTextValue = editTextValue;
    }

    public void setEdit(EditText editText){
        this.edit = editText;
    }

    public void setError(String error){
        try{
            edit.setError(error);
        }catch (Exception e){

        }
    }

}