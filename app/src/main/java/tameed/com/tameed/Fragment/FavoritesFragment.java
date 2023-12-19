package tameed.com.tameed.Fragment;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import java.util.Locale;

import tameed.com.tameed.R;

public class FavoritesFragment extends Fragment {
    ImageView ivhome, ivorder, ivfavorites, ivsetting;
    LinearLayout home, order, favorites, setting;
    private String TAG = "FavoritesFragment";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        String languageToLoad = "ar"; // your language
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getActivity().getResources().updateConfiguration(config,
                getActivity().getResources().getDisplayMetrics());
        View rootView= inflater.inflate(R.layout.fragment_favorites, container, false);

        ivhome = (ImageView) rootView.findViewById(R.id.home_img);
        ivorder = (ImageView) rootView.findViewById(R.id.order_img);
        ivfavorites = (ImageView) rootView.findViewById(R.id.favorites_img);
      //  ivfavorites.setImageResource(R.mipmap.nav_fav_active);
        ivsetting = (ImageView) rootView.findViewById(R.id.setting_img);
        home = (LinearLayout) rootView.findViewById(R.id.toolbar_home);
        order = (LinearLayout) rootView.findViewById(R.id.toolbar_orders);
        favorites = (LinearLayout) rootView.findViewById(R.id.toolbar_favorites);
        setting = (LinearLayout) rootView.findViewById(R.id.toolbar_setting);

        Log.e(TAG,"****************************");

//        home.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), MainActivity.class);
//                startActivity(intent);
//
//            }
//        });
//        order.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), OrdersFragment.class);
//                startActivity(intent);
//
//            }
//        });
//        favorites.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), FavoritesFragment.class);
//                startActivity(intent);
//
//            }
//        });
//        setting.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), SettingFragment.class);
//                startActivity(intent);
//
//            }
//        });

        return rootView;



    }
}
