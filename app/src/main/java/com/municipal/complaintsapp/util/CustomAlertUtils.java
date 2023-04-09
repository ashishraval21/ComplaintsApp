package com.municipal.complaintsapp.util;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.municipal.complaintsapp.R;

public class CustomAlertUtils {
    private final Listener listener;
    private final ViewGroup viewGroup;

        public interface Listener{
             void callback(View view);
        }


        public CustomAlertUtils(Listener listener, ViewGroup viewGroup){
            this.listener = listener;
            this.viewGroup = viewGroup;
        }


        public void showCustomAlertDialog(String message, Context context) {


            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
            boolean success = message.toLowerCase().contains("success") ? true : false;

            // Inflate the custom layout for the dialog
            LayoutInflater inflater = LayoutInflater.from(context);
            View dialogView = inflater.inflate(R.layout.custom_dialog_layout,viewGroup
            );

            // Set the message and image on the dialog layout
            TextView headerText = dialogView.findViewById(R.id.textTitle);

            Drawable headerTextDrawable = headerText.getBackground();
            headerTextDrawable.setColorFilter(success ? ContextCompat.getColor(context, R.color.successColor) :ContextCompat.getColor(context, R.color.errorColor), PorterDuff.Mode.SRC_IN);
            headerText.setBackground(headerTextDrawable);



            TextView dialogText = dialogView.findViewById(R.id.dialog_text);
            ImageView dialogImage = dialogView.findViewById(R.id.dialog_image);



            Button button = dialogView.findViewById(R.id.buttonAction);
            button.setText("OKAY");
            Drawable buttonDrawable = button.getBackground();


            // Change the color of the drawable
            buttonDrawable.setColorFilter(success ? ContextCompat.getColor(context, R.color.successColor) :ContextCompat.getColor(context, R.color.errorColor), PorterDuff.Mode.SRC_IN);

            // Set the modified drawable as the new background of the button
            button.setBackground(buttonDrawable);
            button.setBackgroundColor(success ? ContextCompat.getColor(context, R.color.successColor) :ContextCompat.getColor(context, R.color.errorColor));
            dialogText.setText(message);
            dialogImage.setImageResource(success ? R.drawable.ic_done_success : R.drawable.ic_done_error);
            headerText.setText(success ? "Success" : "Error");
            builder.setView(dialogView);
            builder.setCancelable(false);
            final AlertDialog finalDialog = builder.create();
            button.setOnClickListener(v -> {finalDialog.dismiss(); listener.callback(v);});
            finalDialog.show();
        }
    }


