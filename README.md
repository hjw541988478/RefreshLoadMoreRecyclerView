# RefreshLoadMoreRecyclerView
实现了下拉刷新和上拉加载的RecyclerView的封装。
## Links
博文：[HarvestRecyclerView-RecyclerView下拉刷新上拉加载的封装](http://www.huangjiawen.me/android/HarvestRecyclerView-RecyclerView%E4%B8%8B%E6%8B%89%E5%88%B7%E6%96%B0%E4%B8%8A%E6%8B%89%E5%8A%A0%E8%BD%BD%E7%9A%84%E5%B0%81%E8%A3%85/)   

JCenter库：``compile 'in.harvestday.library:library:0.2'``
## Features
### 支持4种模式
- 既不下拉刷新也不上拉加载
- 只支持下拉刷新
- 只支持上拉加载
- 下拉刷新和上拉加载同时支持

### 支持异常视图的处理
- 空视图
- Loading视图
- 其他异常视图
 
## Todo
- 多种LayoutManager的支持，目前只支持LinearLayoutManager
- 添加删除动画的支持
- ItemClickListener的支持

## ScreenShot
![testdata](http://7xl8mn.com1.z0.glb.clouddn.com/recycler_testdata_view.png)
![menu](http://7xl8mn.com1.z0.glb.clouddn.com/recycler_menu_view.png)
![loading](http://7xl8mn.com1.z0.glb.clouddn.com/recycler_loading_view.png)
![refresh](http://7xl8mn.com1.z0.glb.clouddn.com/recycler_refresh_view.png)
![empty](http://7xl8mn.com1.z0.glb.clouddn.com/recycler_empty_view.png)
![loadend](http://7xl8mn.com1.z0.glb.clouddn.com/recycler_loadend_view.png)
![loadmore](http://7xl8mn.com1.z0.glb.clouddn.com/recycler_loadmore_view.png)

