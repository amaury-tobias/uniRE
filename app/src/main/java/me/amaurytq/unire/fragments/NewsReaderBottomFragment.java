package me.amaurytq.unire.fragments;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import me.amaurytq.unire.R;

import static android.text.Layout.JUSTIFICATION_MODE_INTER_WORD;


public class NewsReaderBottomFragment extends BottomSheetDialogFragment {

    private static final String ARG_TITLE = "title";
    private static final String ARG_AUTHOR = "new_author";
    private static final String ARG_BODY = "body";

    private String title;
    private String author;
    private String body;

    private Listener mListener;

    public static NewsReaderBottomFragment newInstance(Object... arguments) {
        final NewsReaderBottomFragment fragment = new NewsReaderBottomFragment();
        fragment.title = (String) arguments[0];
        fragment.author = (String) arguments[1];
        fragment.body = (String) arguments[2];
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_newsreader, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ((TextView) view.findViewById(R.id.title)).setText(title);
        ((TextView) view.findViewById(R.id.author)).setText(author);
        ((TextView) view.findViewById(R.id.body)).setText(body);

        ((TextView) view.findViewById(R.id.body)).setJustificationMode(JUSTIFICATION_MODE_INTER_WORD);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        final Fragment parent = getParentFragment();
        if (parent != null) {
           // mListener = (Listener) parent;
        } else {
            //mListener = (Listener) context;
        }
    }

    @Override
    public void onDetach() {
        mListener = null;
        super.onDetach();
    }

    public interface Listener {
        void onNewsReaderClicked(int position);
    }

}
