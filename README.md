# superActionBar

Highly customizable action bar for Android.

Usage:

Add the view in your layout resource.


&lt;SuperActionBar
    android:id="@+id/actionBar"
    android:layout_width="match_parent"
    android:layout_height="48dp"/&gt;
    
    
Java:
    
    SuperActionBar superActionBar = (SuperActionBar) findViewById(R.id.actionBar);                            
    superActionBar.setLeftMostIconView(R.drawable.ic_back_half)
                  .setTitle(getString(R.string.All_Replies))
                  .setPenultimateRightMostIconView(R.drawable.ic_search_bar)
                  .setTouchListener(new SuperActionBar.TouchListener() {
                            @Override
                            public void onSuperActionBarClicked() {
                            }

                            @Override
                            public void onLeftMostIconClicked() {
                            }

                            @Override
                            public void onTitleClicked() {
                            }

                            @Override
                            public void onSubtitleClicked() {
                            }

                            @Override
                            public void onDropdownClicked() {
                            }

                            @Override
                            public void onPenultimateRightMostIconClicked() {
                            }

                            @Override
                            public void onRightMostIconClicked() {
                            }

                            @Override
                            public void onNextClicked() {
                            }
                        });
                        
                        
 There are many more methods in the SuperActionBar.java file, which give you much finer control.                       
