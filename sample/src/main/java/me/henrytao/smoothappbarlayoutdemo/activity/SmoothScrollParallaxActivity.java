/*
 * Copyright 2016 "Henry Tao <hi@henrytao.me>"
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.henrytao.smoothappbarlayoutdemo.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.henrytao.mdcore.utils.ResourceUtils;
import me.henrytao.recyclerview.SimpleRecyclerViewAdapter;
import me.henrytao.recyclerview.holder.HeaderHolder;
import me.henrytao.smoothappbarlayout.SmoothAppBarLayout;
import me.henrytao.smoothappbarlayoutdemo.R;
import me.henrytao.smoothappbarlayoutdemo.apdater.DynamicAdapter;
import me.henrytao.smoothappbarlayoutdemo.util.Utils;

public class SmoothScrollParallaxActivity extends BaseActivity {

  @Bind(R.id.recycler_view_target)
  RecyclerView vRecyclerView;

  @Bind(R.id.toolbar)
  Toolbar vToolbar;

  @Bind(R.id.smooth_app_bar_layout)
  SmoothAppBarLayout vSmoothAppBarLayout;

  private DynamicAdapter<String> mAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_smooth_scroll_parallax);
    ButterKnife.bind(this);

    // This is important for making activity under status_bar. It also can be done theme.
    ResourceUtils.enableTranslucentStatus(this);

    setSupportActionBar(vToolbar);
    vToolbar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        onBackPressed();
      }
    });

    mAdapter = new DynamicAdapter<>(Utils.getSampleData());
    RecyclerView.Adapter adapter = new SimpleRecyclerViewAdapter(mAdapter) {
      @Override
      public RecyclerView.ViewHolder onCreateFooterViewHolder(LayoutInflater layoutInflater, ViewGroup viewGroup) {
        return null;
      }

      @Override
      public RecyclerView.ViewHolder onCreateHeaderViewHolder(LayoutInflater layoutInflater, ViewGroup viewGroup) {
        return new HeaderHolder(layoutInflater, viewGroup, R.layout.item_header_spacing_with_fit_system_windows);
      }
    };

    vRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    vRecyclerView.setAdapter(adapter);

    vRecyclerView.postDelayed(new Runnable() {
      @Override
      public void run() {
        vRecyclerView.smoothScrollToPosition(20);
      }
    }, 2000);

    ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
      @Override
      public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
      }

      @Override
      public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        mAdapter.remove((int) viewHolder.itemView.getTag(R.id.tag_position));
      }
    });
    itemTouchHelper.attachToRecyclerView(vRecyclerView);
  }
}
