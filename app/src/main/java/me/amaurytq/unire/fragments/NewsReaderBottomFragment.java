package me.amaurytq.unire.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import me.amaurytq.unire.R;

/**
 * <p>A fragment that shows a list of items as a modal bottom sheet.</p>
 * <p>You can show this modal bottom sheet from your activity like this:</p>
 * <pre>
 *     NewsReaderBottomFragment.newInstance(30).show(getSupportFragmentManager(), "dialog");
 * </pre>
 * <p>You activity (or fragment) needs to implement {@link NewsReaderBottomFragment.Listener}.</p>
 */
public class NewsReaderBottomFragment extends BottomSheetDialogFragment {

    private static final String ARG_AUTHOR = "new_author";
    private static final String ARG_NEW_BODY = "new_body";

    private Listener mListener;

    public static NewsReaderBottomFragment newInstance(Object... arguments) {
        final NewsReaderBottomFragment fragment = new NewsReaderBottomFragment();
        final Bundle args = new Bundle();

        args.putString(ARG_AUTHOR, (String) arguments[0]);
        args.putString(ARG_NEW_BODY, (String) arguments[1]);

        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_newsreader, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //final String author = getArguments().getString(ARG_AUTHOR);
        //final String body = getArguments().getString(ARG_NEW_BODY);



        //final RecyclerView recyclerView = (RecyclerView) view;
        //recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //recyclerView.setAdapter(new NewsReaderAdapter(getArguments().getInt(ARG_ITEM_COUNT)));
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
