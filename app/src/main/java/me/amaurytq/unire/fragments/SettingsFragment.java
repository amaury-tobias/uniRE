package me.amaurytq.unire.fragments;

import android.content.Context;
import android.content.Intent;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import org.json.JSONException;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.amaurytq.unire.R;

public class SettingsFragment extends Fragment {


    private OnFragmentSettingsListener mListener;
    @BindView(R.id.cerrar_sesion) TextView cerrarSesion;
    @BindView(R.id.gesture_overlay) GestureOverlayView gestureOverlayView;
    GestureLibrary gestureLibrary;

    public SettingsFragment() {}


    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //mParam1 = getArguments().getString(ARG_PARAM1);
            //mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        ButterKnife.bind(this, view);

        gestureLibrary = GestureLibraries.fromRawResource(getContext(), R.raw.gestures);
        if (!gestureLibrary.load()) {
            getActivity().finish();
        }

        gestureOverlayView.addOnGesturePerformedListener((overlay, gesture) -> {
            List<Prediction> predictions = gestureLibrary.recognize(gesture);

            if (predictions.size() > 0) {
                Prediction prediction = predictions.get(0);
                if (prediction.score > 1.0) {
                    String name = prediction.name;
                    switch (name) {
                        case "triangle":
                                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                                sharingIntent.setType("text/plain");
                                sharingIntent.putExtra(Intent.EXTRA_TEXT, "Estoy utilizando la App UniRE para conseguir puntos mientras participo en los puntos de reciclaje unete.");
                                startActivity(Intent.createChooser(sharingIntent, "Compartir en..."));

                            break;
                    }
                }
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentSettingsListener) {
            mListener = (OnFragmentSettingsListener) context;
        } else {
            //throw new RuntimeException(context.toString()
             //       + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @OnClick(R.id.cerrar_sesion)
    public void onCerrarSesionClick() {
        mListener.cerrarSesion();
    }

    @OnClick(R.id.share_app)
    public void onShareAppClick() {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, "Estoy utilizando la App UniRE para conseguir puntos mientras participo en los puntos de reciclaje unete.");
        startActivity(Intent.createChooser(sharingIntent, "Compartir en..."));
    }

    public interface OnFragmentSettingsListener {
        void cerrarSesion();
    }
}
