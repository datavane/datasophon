<div align="center">
         <a href="https://github.com/datasophon/datasophon" target="_blank" rel="noopener noreferrer">
           <img src="website/static/img/logo.png" width="20%" height="20%" alt="DataSophon Logo" />
        </a>
 <h1>DataSophon</h1>
 <h3>帮助您更容易地管理和监控集群</h3>
</div>

<p align="center">
  <img src="https://img.shields.io/github/release/datasophon/datasophon.svg">
  <img src="https://img.shields.io/github/stars/datasophon/datasophon">
  <img src="https://img.shields.io/github/forks/datasophon/datasophon">
  <a href="https://www.apache.org/licenses/LICENSE-2.0.html"><img src="https://img.shields.io/badge/license-Apache%202-4EB1BA.svg"></a>
  <p align="center">
    <a href="https://datasophon.github.io/datasophon-website/">官网</a> |
    <a href="https://github.com/datasophon/datasophon/blob/main/README.md">English</a>
  </p>
</p>
<h3>觉得不错的话，star fork下，欢迎社区开发者共建DataSophon</h3>
# dataSophon-init使用说明
前言：当前版本是根据centos8.5进行适配的，其它类型和版本的操作系统目前不能完全支持，需要对shell脚本和本地YUM离线安装包进行适配
1、将dataSophon-init整个目录的内容放到规划的集群主节点的/data目录下(mkdir /data)；
2、将 private-yum-library.tar.gz YUM离线安装包 移到 /data 文件夹并解压；
3、将packages.tar.gz 离线依赖库移到 /data/dataSophon-init下 并解压；
4、