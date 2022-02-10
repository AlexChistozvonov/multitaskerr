# Multitasker - Android App

> Also see [Update copy](#Update-copy)

### Naming XML markup components IDs
In the new design, use specific prefixes to name IDs for the different types of markup components.
<details> 
  <summary>Click to expand the table with examples of prefixes</summary>
  
Type | Prefix
:---|:---:
Frame Layout | fl_
Linear Layout | ll_
Relative Layout | rl_
ConstraintLayout | cl_
SwipeRefreshLayout | srl_
NestedScrollView | nsv_
TabLayout | tbl_
CardView | cv_
CoordinatorLayout | cdl_
AppBarLayout | abl_
Toolbar | tb_
BottomNavigationView | bnv_
Text View | tv_
Edit Text | et_
Button | btn_
Check Box | cbx_
RadioButton | rb_
Toggle Button | tgb_
Image Button | ib_
Image View | iv_
Progress Bar | pb_
Seek Bar | sb_
Rating Bar | rb_
WebView | wv_
VideoView | vv_
Radio Group | rg_
ViewPager | vp_
RecyclerView | rv_
List View | lv_
Grid View | gv_
Include | incl_
Fragment | frag_
View | without prefix

</details>

Example for TextView:
``` XML
<TextView
    android:id="@+id/tv_name"
    android:layout_width="match_parent"
    android:layout_height="wrap_content" />
```
