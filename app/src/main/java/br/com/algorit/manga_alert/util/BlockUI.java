package br.com.algorit.manga_alert.util;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import java.util.Objects;

import br.com.algorit.manga_alert.R;

public class BlockUI {

    private Context context;
    private AlertDialog dialog;

    public BlockUI(Context context) {
        this.context = context;
    }

    public void start() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this.context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.loading, null);

        alertDialog.setView(view);
        dialog = alertDialog.create();
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.show();
    }

    public void startWithCuriosity() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Objects.requireNonNull(this.context), android.R.style.Theme_Black_NoTitleBar_Fullscreen);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = Objects.requireNonNull(inflater).inflate(R.layout.loading_with_curiosity, null);

        alertDialog.setView(view);
        dialog = alertDialog.create();
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);

        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);

        dialog.show();
    }

    public void exibeDialog(String mensagem) {
        View view = LayoutInflater.from(context).inflate(R.layout.mensagem, null);
        TextView textView = view.findViewById(R.id.mensagem);
        textView.setText(mensagem);
        new android.app.AlertDialog.Builder(context)
                .setView(view)
                .show();
    }

    public void stop() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }
}
