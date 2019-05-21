package me.amaurytq.unire.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.squareup.picasso.Picasso;

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
import me.amaurytq.unire.Models.Achievement;
import me.amaurytq.unire.Models.AchievementAdapter;
import me.amaurytq.unire.R;

public class ProfileFragment extends Fragment {

    private profileFragmentListener mListener;
    private AchievementAdapter adapter;

    @BindView(R.id.user_picture) ImageView userPicture;
    @BindView(R.id.user_name) TextView userName;
    @BindView(R.id.user_points) TextView userPoints;
    @BindView(R.id.user_achievements) RecyclerView userAchievements;

    public ProfileFragment() {}

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);
        adapter = new AchievementAdapter();
        userAchievements.setLayoutManager(new LinearLayoutManager(getContext()));
        userAchievements.setAdapter(adapter);
        volleyGetProfile();
        return view;
    }


    private void volleyGetProfile() {
        String REQUEST_TAG = "me.amaurytq.unire.volleyGetProfile";

        String token = Objects.requireNonNull(getContext()).
                getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
                .getString(Constants.BEARER_TOKEN, "");

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                Constants.PROFILE_URL,
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
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, REQUEST_TAG);
    }

    private void responseHandler(JSONObject response) {
        try {
            userName.setText(response.getString("matricula"));
            userPoints.setText("Puntos: ".concat(String.valueOf(response.getInt("points"))));
            Picasso.get().load(response.getString("picture")).into(userPicture);

            List<Achievement> achievementList = new ArrayList<>();
            JSONArray achievements = response.getJSONArray("achievements");
            for (int i = 0; i < achievements.length(); i++) {
                JSONObject jsonObject = achievements.getJSONObject(i);
                Achievement achievement = new Achievement();
                achievement.title = jsonObject.getString("title");
                achievement.description = jsonObject.getString("description");
                achievementList.add(achievement);
            }
            adapter.setItems(achievementList);
            adapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void errorHandler(VolleyError error) {
        error.printStackTrace();
        showToast("Error al obtener los datos de perfil");
    }

    private void showToast (String text) {
        if (getActivity() != null)
            Toast.makeText(getActivity(), text, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof profileFragmentListener) {
            mListener = (profileFragmentListener) context;
        } else {
            //throw new RuntimeException(context.toString()
            // + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface profileFragmentListener {
        void onFragmentInteraction(Uri uri);
    }
}
