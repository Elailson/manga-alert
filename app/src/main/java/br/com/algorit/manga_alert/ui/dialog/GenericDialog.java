package br.com.algorit.manga_alert.ui.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import br.com.algorit.manga_alert.R;

public class GenericDialog {

    private final Context context;

    public GenericDialog(Context context) {
        this.context = context;
    }

    public void show(String mensagem) {
        View view = LayoutInflater.from(context).inflate(R.layout.mensagem, null);
        TextView textView = view.findViewById(R.id.mensagem);
        textView.setText(mensagem);
        new AlertDialog.Builder(context)
                .setView(view)
                .show();
    }
}
