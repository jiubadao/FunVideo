package com.zw.funvideo.home;

import android.app.ActivityManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.StyleSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.WindowInsets;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zw.funvideo.R;
import com.zw.funvideo.data.Source;
import com.zw.funvideo.data.prefs.SourceManager;
import com.zw.funvideo.utils.AnimUtils;

import butterknife.BindInt;
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
    @BindView(R.id.grid) RecyclerView grid;

    @BindView(R.id.filters)
    RecyclerView filtersList;

    @BindView(android.R.id.empty)
    ProgressBar loading;

    FilterAdapter filtersAdapter;

    GridLayoutManager layoutManager;

    private TextView noFiltersEmptyText;


    boolean connected = true;

    @BindInt(R.integer.num_columns) int columns;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ButterKnife.bind(this);

        /*drawer.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);*/


        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            animateToolbar();
        }

        filtersAdapter = new FilterAdapter(this, SourceManager.getSources(this),null);

     //   grid.setAdapter(adapter);

        layoutManager = new GridLayoutManager(this, columns);
      /*  layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return adapter.getItemColumnSpan(position);
            }
        });*/
        grid.setLayoutManager(layoutManager);
        grid.addOnScrollListener(toolbarElevation);
       /* grid.addOnScrollListener(new InfiniteScrollListener(layoutManager, dataManager) {
            @Override
            public void onLoadMore() {
                dataManager.loadAllDataSources();
            }
        });*/
        grid.setHasFixedSize(true);
       /* grid.addItemDecoration(new GridItemDividerDecoration(adapter.getDividedViewHolderClasses(),
                this, R.dimen.divider_height, R.color.divider));*/
     //   grid.setItemAnimator(new HomeGridItemAnimator());


        // drawer layout treats fitsSystemWindows specially so we have to handle insets ourselves
        drawer.setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() {
            @Override
            public WindowInsets onApplyWindowInsets(View v, WindowInsets insets) {
                // inset the toolbar down by the status bar height
                ViewGroup.MarginLayoutParams lpToolbar = (ViewGroup.MarginLayoutParams) toolbar
                        .getLayoutParams();
                lpToolbar.topMargin += insets.getSystemWindowInsetTop();
                lpToolbar.leftMargin += insets.getSystemWindowInsetLeft();
                lpToolbar.rightMargin += insets.getSystemWindowInsetRight();
                toolbar.setLayoutParams(lpToolbar);

                // inset the grid top by statusbar+toolbar & the bottom by the navbar (don't clip)
            /*    grid.setPadding(
                        grid.getPaddingLeft() + insets.getSystemWindowInsetLeft(), // landscape
                        insets.getSystemWindowInsetTop()
                                + ViewUtils.getActionBarSize(HomeActivity.this),
                        grid.getPaddingRight() + insets.getSystemWindowInsetRight(), // landscape
                        grid.getPaddingBottom() + insets.getSystemWindowInsetBottom());

                // inset the fab for the navbar
                ViewGroup.MarginLayoutParams lpFab = (ViewGroup.MarginLayoutParams) fab
                        .getLayoutParams();
                lpFab.bottomMargin += insets.getSystemWindowInsetBottom(); // portrait
                lpFab.rightMargin += insets.getSystemWindowInsetRight(); // landscape
                fab.setLayoutParams(lpFab);

                View postingStub = findViewById(R.id.stub_posting_progress);
                ViewGroup.MarginLayoutParams lpPosting =
                        (ViewGroup.MarginLayoutParams) postingStub.getLayoutParams();
                lpPosting.bottomMargin += insets.getSystemWindowInsetBottom(); // portrait
                lpPosting.rightMargin += insets.getSystemWindowInsetRight(); // landscape
                postingStub.setLayoutParams(lpPosting);*/

                // we place a background behind the status bar to combine with it's semi-transparent
                // color to get the desired appearance.  Set it's height to the status bar height
                View statusBarBackground = findViewById(R.id.status_bar_background);
                FrameLayout.LayoutParams lpStatus = (FrameLayout.LayoutParams)
                        statusBarBackground.getLayoutParams();
                lpStatus.height = insets.getSystemWindowInsetTop();
                statusBarBackground.setLayoutParams(lpStatus);

                // inset the filters list for the status bar / navbar
                // need to set the padding end for landscape case
                final boolean ltr = filtersList.getLayoutDirection() == View.LAYOUT_DIRECTION_LTR;
                filtersList.setPaddingRelative(filtersList.getPaddingStart(),
                        filtersList.getPaddingTop() + insets.getSystemWindowInsetTop(),
                        filtersList.getPaddingEnd() + (ltr ? insets.getSystemWindowInsetRight() :
                                0),
                        filtersList.getPaddingBottom() + insets.getSystemWindowInsetBottom());

                // clear this listener so insets aren't re-applied
                drawer.setOnApplyWindowInsetsListener(null);

                return insets.consumeSystemWindowInsets();
            }
        });

        setupTaskDescription();



        filtersList.setAdapter(filtersAdapter);
        filtersList.setItemAnimator(new FilterAdapter.FilterAnimator());
        filtersAdapter.registerFilterChangedCallback(filtersChangedCallbacks);
    }

    private void setupTaskDescription() {
        Bitmap overviewIcon =
                BitmapFactory.decodeResource(getResources(), getApplicationInfo().icon);
        setTaskDescription(new ActivityManager.TaskDescription(getString(R.string.app_name),
                overviewIcon,
                ContextCompat.getColor(this, R.color.primary)));
        overviewIcon.recycle();
    }


    private RecyclerView.OnScrollListener toolbarElevation = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            // we want the grid to scroll over the top of the toolbar but for the toolbar items
            // to be clickable when visible. To achieve this we play games with elevation. The
            // toolbar is laid out in front of the grid but when we scroll, we lower it's elevation
            // to allow the content to pass in front (and reset when scrolled to top of the grid)
            if (newState == RecyclerView.SCROLL_STATE_IDLE
                    && layoutManager.findFirstVisibleItemPosition() == 0
                    && layoutManager.findViewByPosition(0).getTop() == grid.getPaddingTop()
                    && toolbar.getTranslationZ() != 0) {
                // at top, reset elevation
                toolbar.setTranslationZ(0f);
            } else if (newState == RecyclerView.SCROLL_STATE_DRAGGING
                    && toolbar.getTranslationZ() != -1f) {
                // grid scrolled, lower toolbar to allow content to pass in front
                toolbar.setTranslationZ(-1f);
            }
        }
    };


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
                    checkEmptyState();
                }

                @Override
                public void onFilterRemoved(Source removed) {
                   // adapter.removeDataSource(removed.key);
                    checkEmptyState();
                }
            };



    void checkEmptyState() {
       // if (adapter.getDataItemCount() == 0) {
            // if grid is empty check whether we're loading or if no filters are selected
            if (filtersAdapter.getEnabledSourcesCount() > 0) {
                if (connected) {
                    loading.setVisibility(View.VISIBLE);
                    setNoFiltersEmptyTextVisibility(View.GONE);
                }
            } else {
                loading.setVisibility(View.GONE);
                setNoFiltersEmptyTextVisibility(View.VISIBLE);
            }
            toolbar.setTranslationZ(0f);
        /*} else {
            loading.setVisibility(View.GONE);
            setNoFiltersEmptyTextVisibility(View.GONE);
        }*/
    }

    private void setNoFiltersEmptyTextVisibility(int visibility) {
        if (visibility == View.VISIBLE) {
            if (noFiltersEmptyText == null) {
                // create the no filters empty text
                ViewStub stub = (ViewStub) findViewById(R.id.stub_no_filters);
                noFiltersEmptyText = (TextView) stub.inflate();
                String emptyText = getString(R.string.no_filters_selected);
                int filterPlaceholderStart = emptyText.indexOf('\u08B4');
                int altMethodStart = filterPlaceholderStart + 3;
                SpannableStringBuilder ssb = new SpannableStringBuilder(emptyText);
                // show an image of the filter icon
                ssb.setSpan(new ImageSpan(this, R.drawable.ic_filter_small,
                                ImageSpan.ALIGN_BASELINE),
                        filterPlaceholderStart,
                        filterPlaceholderStart + 1,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                // make the alt method (swipe from right) less prominent and italic
                ssb.setSpan(new ForegroundColorSpan(
                                ContextCompat.getColor(this, R.color.text_secondary_light)),
                        altMethodStart,
                        emptyText.length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                ssb.setSpan(new StyleSpan(Typeface.ITALIC),
                        altMethodStart,
                        emptyText.length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                noFiltersEmptyText.setText(ssb);
                noFiltersEmptyText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        drawer.openDrawer(GravityCompat.END);
                    }
                });
            }
            noFiltersEmptyText.setVisibility(visibility);
        } else if (noFiltersEmptyText != null) {
            noFiltersEmptyText.setVisibility(visibility);
        }
    }


        @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_filter:
                drawer.openDrawer(GravityCompat.END);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}
