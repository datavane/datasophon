/*
/*
 *
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */


// 此配置为系统默认设置，需修改的设置项，在src/config/config.js中添加修改项即可。也可直接在此文件中修改。
module.exports = {
  lang: 'CN',                           //语言，可选 CN(简体)、HK(繁体)、US(英语)，也可扩展其它语言
  theme: {                              //主题
    color: '#2872E0',                    //主题色
    mode: 'dark',                       //主题模式 可选 dark、 light 和 night
    success: '#52c41a',                 //成功色
    warning: '#faad14',                 //警告色
    error: '#f5222f',                   //错误色
  },
  layout: 'side',                       //导航布局，可选 side 和 head，分别为侧边导航和顶部导航
  fixedHeader: false,                   //固定头部状态栏，true:固定，false:不固定
  fixedSideBar: true,                   //固定侧边栏，true:固定，false:不固定
  fixedTabs: false,                      //固定页签头，true:固定，false:不固定
  pageWidth: 'fixed',                   //内容区域宽度，fixed:固定宽度，fluid:流式宽度
  weekMode: false,                      //色弱模式，true:开启，false:不开启
  multiPage: false,                     //多页签模式，true:开启，false:不开启
  cachePage: true,                      //是否缓存页面数据，仅多页签模式下生效，true 缓存, false 不缓存
  hideSetting: false,                   //隐藏设置抽屉，true:隐藏，false:不隐藏
  systemName: 'DataSophon',         //系统名称
  copyright: '',     //copyright
  asyncRoutes: false,                   //异步加载路由，true:开启，false:不开启
  showPageTitle: true,                  //是否显示页面标题（PageLayout 布局中的页面标题），true:显示，false:不显示
  filterMenu: true,                    //根据权限过滤菜单，true:过滤，false:不过滤
  animate: {                            //动画设置
    disabled: true,                    //禁用动画，true:禁用，false:启用
    name: 'bounce',                     //动画效果，支持的动画效果可参考 ./animate.config.js
    direction: 'left'                   //动画方向，切换页面时动画的方向，参考 ./animate.config.js
  },
  footerLinks: [                        //页面底部链接，{link: '链接地址', name: '名称/显示文字', icon: '图标，支持 ant design vue 图标库'}
  ],
}
