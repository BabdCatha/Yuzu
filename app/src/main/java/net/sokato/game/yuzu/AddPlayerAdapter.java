package net.sokato.game.yuzu;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AddPlayerAdapter extends RecyclerView.Adapter<AddPlayerAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    public static ArrayList<AddPlayerRow> editModelArrayList;


    public AddPlayerAdapter(Context ctx, ArrayList<AddPlayerRow> editModelArrayList){

        inflater = LayoutInflater.from(ctx);
        this.editModelArrayList = editModelArrayList;
    }

    @Override
    public AddPlayerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.row_player, parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final AddPlayerAdapter.MyViewHolder holder, final int position) {


        holder.editText.setText(editModelArrayList.get(position).getEditTextValue());
        editModelArrayList.get(position).setEdit(holder.editText); //used when we need to set an error on the edittext

    }

    @Override
    public int getItemCount() {
        return editModelArrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        protected EditText editText;
        protected Button button;

        public MyViewHolder(View itemView) {
            super(itemView);

            editText = (EditText) itemView.findViewById(R.id.row_edittext);
            button = itemView.findViewById(R.id.row_button);

            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    editModelArrayList.get(getAdapterPosition()).setEditTextValue(editText.getText().toString());
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editModelArrayList.remove(getAdapterPosition());
                    notifyDataSetChanged();
                }
            });

        }

    }
}