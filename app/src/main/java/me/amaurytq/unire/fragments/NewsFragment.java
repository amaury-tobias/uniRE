package me.amaurytq.unire.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.amaurytq.unire.AppController;
import me.amaurytq.unire.Constants;
import me.amaurytq.unire.Models.Noticia;
import me.amaurytq.unire.Models.NoticiasAdapter;
import me.amaurytq.unire.R;

public class NewsFragment extends Fragment implements NoticiasAdapter.NoticiasListener {

    private NoticiasAdapter adapter;
    @BindView(R.id.NewsList) RecyclerView newsList;
    @BindView(R.id.swipeNewsList) SwipeRefreshLayout refreshLayout;

    public NewsFragment() {}

    public static NewsFragment newInstance() {
        NewsFragment fragment = new NewsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        ButterKnife.bind(this, view);
        adapter = new NoticiasAdapter(this);
        newsList.setLayoutManager(new LinearLayoutManager(getContext()));
        newsList.addItemDecoration(new DividerItemDecoration(
                Objects.requireNonNull(getContext()),
                DividerItemDecoration.VERTICAL));
        newsList.setAdapter(adapter);

        refreshLayout.setOnRefreshListener(() -> {
            refreshLayout.setRefreshing(true);
            volleyGetNoticias();
            refreshLayout.setRefreshing(false);
        });
        return view;
    }

    private void volleyGetNoticias() {
        String REQUEST_TAG = "me.amaurytq.unire.volleyGetNoticias";
        String token = Objects.requireNonNull(getContext()).
                getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
                .getString(Constants.BEARER_TOKEN, "");

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                Constants.NOTICIAS_URL,
                null,
                this::responseHandler,
                this::errorHandler
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer ".concat(token));
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
        AppController.getInstance().addToRequestQueue(request, REQUEST_TAG);
    }

    private void responseHandler(JSONArray response) {
        List <Noticia> noticias = new ArrayList<>();
        try {
            for (int i = 0; i < response.length(); i++) {
                JSONObject jsonObject = response.getJSONObject(i);
                Noticia noticia= new Noticia();
                noticia.title = jsonObject.getString("title");
                noticia.author = jsonObject.getString("author");
                noticia.shortBody = jsonObject.getString("shortBody");
                noticia.body = jsonObject.getString("body");
                noticias.add(noticia);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            adapter.setItems(noticias);
            adapter.notifyDataSetChanged();
        }
    }

    private void errorHandler(VolleyError error) {
        error.printStackTrace();
        showToast("Error al obtener los datos de perfil");
    }

    private void showToast (String text) {
        Toast.makeText(getActivity(), text, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(Noticia item) {
        BottomSheetDialogFragment bsdFragment =
                NewsReaderBottomFragment.newInstance(item.title, item.author, item.body);
        bsdFragment.show(getChildFragmentManager(), "NEWS_READER");
    }

}
