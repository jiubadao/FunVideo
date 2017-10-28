package com.zw.funvideo.home;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toolbar;

import com.zw.funvideo.R;
import com.zw.funvideo.data.Source;
import com.zw.funvideo.data.prefs.SourceManager;
import com.zw.funvideo.utils.AnimUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhangwei on 17/10/28.
 */

public class HomeActivity extends AppCompatActivity{


    @BindView(R.id.drawer)
    DrawerLayout drawer;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.filters)
    RecyclerView filtersList;

    FilterAdapter filtersAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ButterKnife.bind(this);

        drawer.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);

        setActionBar(toolbar);

        if (savedInstanceState == null) {
            animateToolbar();
        }


        filtersAdapter = new FilterAdapter(this, SourceManager.getSources(this),null);



        filtersList.setAdapter(filtersAdapter);
        filtersList.setItemAnimator(new FilterAdapter.FilterAnimator());
        filtersAdapter.registerFilterChangedCallback(filtersChangedCallbacks);
    }


    private void animateToolbar() {
        // this is gross but toolbar doesn't expose it's children to animate them :(
        View t = toolbar.getChildAt(0);
        if (t != null && t instanceof TextView) {
            TextView title = (TextView) t;

            // fade in and space out the title.  Animating the letterSpacing performs horribly so
            // fake it by setting the desired letterSpacing then animating the scaleX ¯\_(ツ)_/¯
            title.setAlpha(0f);
            title.setScaleX(0.8f);

            title.animate()
                    .alpha(1f)
                    .scaleX(1f)
                    .setStartDelay(300)
                    .setDuration(900)
                    .setInterpolator(AnimUtils.getFastOutSlowInInterpolator(this));
        }
    }

    // listener for notifying adapter when data sources are deactivated
    private FilterAdapter.FiltersChangedCallbacks filtersChangedCallbacks =
            new FilterAdapter.FiltersChangedCallbacks() {
                @Override
                public void onFiltersChanged(Source changedFilter) {
                    if (!changedFilter.active) {
                      //  adapter.removeDataSource(changedFilter.key);
                    }
                  //  checkEmptyState();
                }

                @Override
                public void onFilterRemoved(Source removed) {
                   /* adapter.removeDataSource(removed.key);
                    checkEmptyState();*/
                }
            };


}
