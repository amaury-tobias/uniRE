package me.amaurytq.unire.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.amaurytq.unire.R;


public class NavigationFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    @BindView(R.id.map)
    MapView mapView;

    public NavigationFragment() {
    }

    public static NavigationFragment newInstance() {
        NavigationFragment fragment = new NavigationFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Context ctx = getContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        ButterKnife.bind(this, view);

        mapView.setMultiTouchControls(true);
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        //mapView.setBuiltInZoomControls(false);
        //mapView.setMultiTouchControls(false);

        MyLocationNewOverlay mLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(Objects.requireNonNull(getContext())), mapView);
        mLocationOverlay.enableMyLocation();
        mapView.getOverlays().add(mLocationOverlay);

        //BRUJULA
        CompassOverlay mCompassOverlay = new CompassOverlay(getContext(), new InternalCompassOrientationProvider(getContext()), mapView);
        mCompassOverlay.enableCompass();
        mapView.getOverlays().add(mCompassOverlay);

        //BARRA DE ESCALA
        final Context context = this.getActivity();
        final DisplayMetrics dm = context.getResources().getDisplayMetrics();
        ScaleBarOverlay mScaleBarOverlay = new ScaleBarOverlay(mapView);
        mScaleBarOverlay.setCentred(true);
        mScaleBarOverlay.setScaleBarOffset(dm.widthPixels / 2, 10);
        mapView.getOverlays().add(mScaleBarOverlay);

        List<String> descriptions = new ArrayList<>();
        descriptions.add("Centro de recoleccion de bottellas para la ayuda de muchos niños");
        descriptions.add("Centro de recoleccion de carton y papel");
        descriptions.add("Centro de recoleccion y reciclaje de electronicos, ofrece recompensas\n*1Kg: 5 Puntos\n*2Kg: 15 puntos");
        //ITEMS
        ArrayList<OverlayItem> items = new ArrayList<>();
        items.add(new OverlayItem("FOD", "Botellas", new GeoPoint(25.727171, -100.311819)));
        items.add(new OverlayItem("FACPyA", "Papel y cartón", new GeoPoint(25.727335, -100.309330)));
        items.add(new OverlayItem("FCFM", "Desechos tecnoloógicos", new GeoPoint(25.725398, -100.315279)));
        ItemizedOverlayWithFocus<OverlayItem> mOverlay = new ItemizedOverlayWithFocus<>(items,
                new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
                    @Override
                    public boolean onItemSingleTapUp(final int index, final OverlayItem item) {
                        BottomSheetDialogFragment bsdFragment =
                                NewsReaderBottomFragment.newInstance(item.getTitle(), item.getSnippet(), descriptions.get(index));
                        bsdFragment.show(getChildFragmentManager(), "NEWS_READER");
                        return true;
                    }

                    @Override
                    public boolean onItemLongPress(final int index, final OverlayItem item) {
                        return false;
                    }
                }, Objects.requireNonNull(ctx));
        mOverlay.setFocusItemsOnTap(true);
        mapView.getOverlays().add(mOverlay);

        locationManager = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);





        //POSITION
        IMapController mapController = mapView.getController();
        mapController.setZoom(17f);
        GeoPoint startPoint = getCurrentLocation();
        mapController.animateTo(startPoint);
        return view;
    }

    private GeoPoint getCurrentLocation() throws SecurityException{
        Location location = null;
        List<String> providers = locationManager.getProviders(true);

        for (String provider : providers) {

            Location temp = locationManager.getLastKnownLocation(provider);
            if (temp== null)
                continue;
            if (location == null || temp.getAccuracy() < location.getAccuracy())
                location = temp;
        }
        if (location == null)
            return new GeoPoint(25.727171, -100.311819);
        return new GeoPoint(location.getAltitude(), location.getLongitude());
    }

    LocationManager locationManager;

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            //throw new RuntimeException(context.toString()
            //        + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
