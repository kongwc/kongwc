<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <title>test</title>
    <link rel="stylesheet" href="/layui/css/layui.css" media="all">
    <link rel="stylesheet" href="/css/app.css" media="all">
    <style type="text/css">
        .wrap {
            border-radius: 5px;
            background: white;
            min-height: 100px!important;
            max-width:1300px;
            margin-left: 30px;
            margin-top: 10px;
        }
        .button {
            margin-left: 5px;
            margin-top: 5px;
        }
        .downloadButton {
            float: right;
        }
    </style>
</head>
<body style="padding: 15px;">
    <div class="layui-main" style="width:1650px">
        <fieldset class="layui-elem-field layui-field-title" style="margin-top: 20px;">
            <legend>属性追加</legend>
        </fieldset>
        <div class="wrap layui-bg-green" id="wrap">
            <button id="btnAdd" class="button layui-btn layui-btn-warm layui-btn-sm" style="margin-left: 5px;" onClick='addItem()'>
                <i class="layui-icon">&#xe608;</i>添加属性
            </button>
            <br/>
            <div class="layui-form-item">
                <table class="layui-table" lay-data="{limit: 1000, id: 'tb', skin: 'row', even: true}" lay-filter="tb">
                    <thead>
                    <tr>
                        <th lay-data="{field: 'attrName', edit: 'text'}">属性名</th>
                        <th lay-data="{field: 'attrValue', edit: 'text'}">属性值</th>
                        <th lay-data="{fixed: 'right', width:98, align:'center', toolbar: '#deleteBar'}"></th>
                    </tr>
                    </thead>
                </table>
                <script type="text/html" id="deleteBar">
                    <a class="layui-btn layui-btn-danger layui-btn-sm" lay-event="del"><i class="layui-icon"></i>删除</a>
                </script>
            </div>
            <button id="btnDownload" class="downloadButton layui-btn layui-btn-normal" lay-submit="true">
                <i class="layui-icon">&#xe601;</i>生成下载
            </button>
        </div>
    </div>

    <script src="/js/jquery-3.2.1.min.js"></script>
    <script src="/layui/layui.js" charset="utf-8"></script>
    <script type="text/javascript">
        function addItem() {
            layui.use('table', function(){
                var table = layui.table;

                var attrNames = $("[data-field='attrName']");
                var attrValues = $("[data-field='attrValue']");
                var inputKey;
                var data = [];
                for(var index = 1; index < attrNames.length; index++){
                    data.push({
                        "attrName": attrNames[index].childNodes[0].textContent,
                        "attrValue": attrValues[index].childNodes[0].textContent
                    });
                }
                data.push({"attrName": "", "attrValue": ""});
                var table = layui.table;
                table.init('tb', {
                    data: data
                });
            });
        }

        layui.use('table', function(){
            var table = layui.table;

            table.init('tb', {
                url: '/listData',
                where: {count: 1},
                method: 'post'
            });

            table.on('tool(tb)', function(obj){
                var data = obj.data;
                if(obj.event === 'del'){
                    //layer.confirm('真的删除行么？', {
                    //    title: '确认删除',
                    //    btn: ['删除','取消'],
                    //    icon: 3
                    //}, function(index){
                    //    obj.del();
                    //    layer.close(index);
                    //});
                    obj.del();
                }
            });
        });

        layui.use(['form', 'layer'], function (){
            var layer = layui.layer;
            var $ = layui.jquery;
            var form = layui.form;

            document.getElementById("btnDownload").onclick = function() {
                var attrNames = $("[data-field='attrName']");
                var attrValues = $("[data-field='attrValue']");
                var inputKey;
                var map = {};

                for (var index = 1; index < attrNames.length; index++) {
                    var attrName = attrNames[index].childNodes[0].textContent;
                    var attrValue = attrValues[index].childNodes[0].textContent;
                    if((attrName == null || attrName == '') && attrValue != null && attrValue != ""){
                        layer.msg("属性名不能为空", {icon : 5, anim: 6}, function(){});
                        return false;
                    }
                    if (attrName != null && attrName != ""){
                        map[attrName] = attrValue;
                    }
                }

                $.ajax({
                    type: "POST",
                    url: "/submitXml",
                    contentType: "application/json",
                    data: JSON.stringify(map),
                    success: function(){
                        var form = $("<form>");
                        form.attr("style","display:none");
                        form.attr("target","");
                        form.attr("method","post");
                        form.attr("action","/download");

                        var input = $("<input>");
                        input.attr("type","hidden");
                        input.attr("name","exportData");
                        input.attr("value",(new Date()).getMilliseconds());
                        $("body").append(form);
                        form.append(input);
                        form.submit();
                    },
                    error : function(data) {
                        layer.alert(data.status + " : " + data.statusText + " : " + data.responseText, {title: "错误提示",shadeClose:true});
                    }
                });
            }
        });
    </script>
    <div id="message">
    </div>
</body>
</html>
