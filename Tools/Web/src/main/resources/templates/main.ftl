<!DOCTYPE HTML>
<html>
<head>
    <meta charset="utf-8" />
    <title>简便工具集合</title>
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1"/>
    <link rel="stylesheet" href="/font-awesome/css/font-awesome.min.css" media="all">
    <link rel="stylesheet" href="/layui/css/layui.css" media="all">
    <link rel="stylesheet" href="/css/app.css" media="all">
</head>

<body>
    <div class="layui-layout layui-layout-admin kit-layout-admin">
        <div class="layui-header">
            <div class="layui-logo" style="width:220px">简便工具集合</div>
            <ul class="layui-nav layui-layout-right kit-nav">

            </ul>
        </div>
        <div class="layui-side layui-bg-black kit-side">
            <div class="layui-side-scroll">
                <div class="kit-side-fold"><i class="fa fa-navicon" aria-hidden="true"></i></div>
                <!-- 左侧导航区域（可配合layui已有的垂直导航） -->
                <ul class="layui-nav layui-nav-tree" lay-filter="kitNavbar" kit-navbar>
                    <li class="layui-nav-item layui-nav-itemed">
                        <a class="" href="javascript:;"><i class="fa fa-plug" aria-hidden="true"> 一 </i>、<span>文件生成工具</span></a>
                        <dl class="layui-nav-child" id="viewMenuDl1">
        					<dd>
        						<a id="xml-gen-url" href="javascript:;" kit-target data-options="{url:'xml-generator',icon:'&#xe6c6;',title:'自定义Xml文件生成',id:'1_1'}">
        							<i class="layui-icon"></i><span>自定义Xml文件生成</span>
        						</a>
        					</dd>
                        </dl>
                    </li>
                </ul>
            </div>
        </div>
        <div class="layui-body" id="container">
            <!-- 内容主体区域 -->
            <div style="padding: 15px;">主体内容加载中,请稍等...</div>
        </div>
        <div class="layui-footer">
            <!-- 底部固定区域 -->
            2017 &copy;
            <a href="#">金陵科技</a>版权所有  由卷益信息提供开发技术支持.
        </div>
    </div>
    <script src="/js/jquery-3.2.1.min.js"></script>
    <script src="/layui/layui.js"></script>
    <script>
        layui.config({
            base: '/js/'
        }).use(['app'], function() {
            var app = layui.app,
                $ = layui.jquery,
                layer = layui.layer;

            //主入口
            app.set({
                type: 'iframe'
            }).init();
        });

        layui.use(['layer', 'form'], function(){
            var layer = layui.layer
                ,form = layui.form;

            form.on('select(contenttype)', function(data){
                //console.log(data.elem); //得到select原始DOM对象
                console.log(data.value); //得到被选中的值
                //console.log(data.othis); //得到美化后的DOM对象
            });

            form.on('select(attrfilter)', function(data){
                var objid = 'inp_' + data.elem.id;
                console.log(data.elem.id);
                console.log(data.value); //得到被选中的值
                $("#" + objid).attr("name", data.value);
            });

            //layer.msg('页面加载完成！');
        });
    </script>
</body>
</html>
