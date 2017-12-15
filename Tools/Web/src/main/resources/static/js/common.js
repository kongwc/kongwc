var formDataToJson = function(formId) {
    var data;
    console.log($("#" + formId).serializeArray());
    // 有 结构体/结构体数组 参数的页面 (没有文件上传与下载等特殊处理)
    if($("#pageType") != undefined && "1" == $("#pageType").val()) {
        data = formDataToJsonByStructPage(formId);
    }
    else {
        data = formDataToJsonByPage(formId);
    }
    //console.log(JSON.stringify(data));
    return data;
}

var postJsonData = function(uri, jsonData, callback) {
    var jsonStr;
    if(isString(jsonData)){
        jsonStr = jsonData;
    }
    else {
        jsonStr = JSON.stringify(jsonData);
    }
    $.ajax({
        type: "POST",
        url: uri,
        contentType: "application/json",
        dataType: "json",
        data: jsonStr,
        success: function (jsonResult) {
            console.log(jsonResult);
            //alert(JSON.stringify(jsonResult));
            if(callback && typeof(callback) == "function"){
                callback(jsonResult);
            }
        }
    });
}

var formDataToUploadData = function(formId) {
    var formData = new FormData();
    formData.append('file', $('#file')[0].files[0]);
    $("#" + formId).serializeArray().map(function(x){
        //console.log(x.name || "=" || x.value);
        if(x.name && x.value != '') {
            formData.append(x.name, x.value);
        }
    });
    //console.log(formData);
    return formData;
}
var postUploadData = function(uri, formData, callback) {
    $.ajax({
        url : uri,
        type : "POST",
        data : formData,
        cache: false,
        processData : false,
        contentType : false,
        dataType: "json",
        success : function(jsonResult) {
            console.log(jsonResult);
            //alert(JSON.stringify(jsonResult));
            if(callback && typeof(callback) == "function"){
                callback(jsonResult);
            }
        },
        error : function(data) {
            alert(data.status + " : " + data.statusText + " : " + data.responseText);
        }
    });
}

// 普通页面
var formDataToJsonByPage = function(formId) {
    var data = {};
    $("#" + formId).serializeArray().map(function(x){
        //console.log(x.name || "=" || x.value);
        if(x.value != '') {
            var type = $('input[name="'+x.name+'"]', $("#" + formId)).attr("data-type");
            if (data[x.name] !== undefined) {
                if (!data[x.name].push) {
                    data[x.name] = [data[x.name]];
                }

                if(isNumType(type, x.value)) {
                    data[x.name].push(x.value);
                }
                else {
                    data[x.name].push(x.value || '');
                }
            } else {
                if(isNumType(type, x.value)) {
                    data[x.name] = x.value;
                }
                else {
                    data[x.name] = x.value || '';
                }
            }
        }
    });
    return data;
}

// 有结构体的页面
var formDataToJsonByStructPage = function(formId) {
    var data = {};
    $("#" + formId).serializeArray().map(function(x){
        //if(x.value != '') {
            //console.log(x.name || "=" || x.value);
            var itemname = x.name;
            var itemval = x.value;
            // 结构体 (只支持二重嵌套)
            if(itemname.indexOf(".") > 0) {
                var nArr = itemname.split(".");
                //console.log(nArr);
                //console.log(nArr.length);
                if(nArr.length > 3) {
                    // TODO
                    alert("不支持！！！");
                    return {};
                }
                var itemtype0 = $("#" + nArr[0] + "_type").val();
                // 父节点是结构体数组
                if(itemtype0.indexOf("数组") > 0) {
                    // 有两层嵌套
                    if(nArr.length == 3) {
                        // TODO 暂不支持
                        alert("TODO 暂不支持");
                        return false;

                        // 数组里的数组
                        var subarr = nArr[1].match(/\[(\S*)\]/);
                        if(subarr != null) {
                        }
                        // 数组里的非数组
                        else {

                        }
                    }
                    // 只有一层嵌套
                    else {
                        if (data[nArr[0]] !== undefined) {
                            var arrlen = data[nArr[0]].length;
                            if(data[nArr[0]][arrlen-1][nArr[1]] !== undefined) {
                                data[nArr[0]][arrlen] = {};
                                data[nArr[0]][arrlen][nArr[1]] = itemval;
                            }
                            else {
                                data[nArr[0]][arrlen-1][nArr[1]] = itemval;
                            }
                        }
                        else {
                            data[nArr[0]] = new Array();
                            data[nArr[0]][0] = {};
                            data[nArr[0]][0][nArr[1]] = itemval;
                        }
                    }
                }
                // 父节点是单个对象
                else {
                    // 有两层嵌套
                    if(nArr.length == 3) {
                        var subarr = nArr[1].match(/\[(\S*)\]/);
                        // 数组
                        if(subarr != null) {
                            var narr1name = nArr[1].replace(subarr[0],"");
                            var narr1index = parseInt(subarr[1]);
                            var itemtype1 = $("#" + narr1name + "_type").val();
                            if (data[nArr[0]] !== undefined) {
                                if (data[nArr[0]][narr1name] !== undefined) {
                                    if(data[nArr[0]][narr1name][narr1index] !== undefined) {
                                        if(data[nArr[0]][narr1name][narr1index][nArr[2]] !== undefined) {
                                            if (!data[nArr[0]][narr1name][narr1index][nArr[2]].push) {
                                                data[nArr[0]][narr1name][narr1index][nArr[2]] = [data[nArr[0]][narr1name][narr1index][nArr[2]]];
                                            }
                                            data[nArr[0]][narr1name][narr1index][nArr[2]].push(itemval);
                                        }
                                        else {
                                            data[nArr[0]][narr1name][narr1index][nArr[2]] = itemval;
                                        }
                                    }
                                    else {
                                        data[nArr[0]][narr1name][narr1index] = {}
                                        data[nArr[0]][narr1name][narr1index][nArr[2]] = itemval;
                                    }
                                }
                                else {
                                    data[nArr[0]][narr1name] = new Array();
                                    data[nArr[0]][narr1name][0] = {};
                                    data[nArr[0]][narr1name][0][nArr[2]] = itemval;
                                }
                            }
                            else {
                                data[nArr[0]] = {};
                                data[nArr[0]][narr1name] = new Array();
                                data[nArr[0]][narr1name][0] = {};
                                data[nArr[0]][narr1name][0][nArr[2]] = itemval;
                            }
                        }
                        // 非数组
                        else {
                            var itemtype1 = $("#" + nArr[1] + "_type").val();
                            if (data[nArr[0]] !== undefined) {
                                if (data[nArr[0]][nArr[1]] !== undefined) {
                                    if (data[nArr[0]][nArr[1]][nArr[2]] !== undefined) {
                                        if (!data[nArr[0]][nArr[1]][nArr[2]].push) {
                                            data[nArr[0]][nArr[1]][nArr[2]] = [data[nArr[0]][nArr[1]][nArr[2]]];
                                        }
                                        data[nArr[0]][nArr[1]][nArr[2]].push(itemval);
                                    }
                                    else {
                                        data[nArr[0]][nArr[1]][nArr[2]] = itemval;
                                    }
                                }
                                else {
                                    data[nArr[0]][nArr[1]] = {};
                                    data[nArr[0]][nArr[1]][nArr[2]] = itemval;
                                }
                            }
                            else {
                                data[nArr[0]] = {};
                                data[nArr[0]][nArr[1]] = {};
                                data[nArr[0]][nArr[1]][nArr[2]] = itemval;
                            }
                        }
                    }
                    // 只有一层嵌套
                    else {
                        if (data[nArr[0]] !== undefined) {
                            if (data[nArr[0]][nArr[1]] !== undefined) {
                                if (!data[nArr[0]][nArr[1]].push) {
                                    data[nArr[0]][nArr[1]] = [data[nArr[0]][nArr[1]]];
                                }
                                data[nArr[0]][nArr[1]].push(itemval);
                            }
                            else {
                                data[nArr[0]][nArr[1]] = itemval;
                            }
                        }
                        else {
                            data[nArr[0]] = {};
                            data[nArr[0]][nArr[1]] = itemval;
                        }
                    }
                }
            }
            else {
                if (data[itemname] !== undefined) {
                    if (!data[itemname].push) {
                        data[itemname] = [data[itemname]];
                    }
                    data[itemname].push(itemval);
                } else {
                    data[itemname] = itemval;
                }
            }
        //}
    });
    deleteEmptyProperty(data);
    // 清除数组里的null
    deleteNullJsonObjInArray(data);
    //console.log(data);
    return data;
}

//判断obj是否为json对象
function isJson(obj){
    var isjson = typeof(obj) == "object" && Object.prototype.toString.call(obj).toLowerCase() == "[object object]" && !obj.length;
    return isjson;
}
function isString(str){
    return (typeof str=='string') && str.constructor==String;
}

function isNumType(type, val) {
    if(type == "long" || type == "int" || type == "double") {
        if(!isNaN(val)) {
            return true;
        }
    }
    return false;
}

function deleteEmptyStr(object){
    for (var i in object) {
        var value = object[i];
        // console.log('typeof object[' + i + ']', (typeof value));
        if (typeof value === 'object') {
            deleteEmptyStr(value);
        } else {
            if (value === '' || value === null || value === undefined) {
                delete object[i];
            }
        }
    }
}
function deleteEmptyProperty(object){
    for (var i in object) {
        var value = object[i];
        // console.log('typeof object[' + i + ']', (typeof value));
        if (typeof value === 'object') {
            if (Array.isArray(value)) {
                if (value.length == 0) {
                    delete object[i];
                    console.log('delete Array', i);
                    continue;
                }
            }
            deleteEmptyProperty(value);
            if (isEmpty(value)) {
                console.log('isEmpty true', i, value);
                delete object[i];
                //splice();
                console.log('delete a empty object');
            }
        } else {
            if (value === '' || value === null || value === undefined) {
                delete object[i];
            }
        }
    }
}
function deleteNullJsonObjInArray(object){
    for (var x in object) {
        var value = object[x];
        if (typeof value === 'object') {
            if (Array.isArray(value)) {
                // 清除数组里的null
                var len = value.length;
                for(var i = len-1; i >= 0; i--) {
                    if(value[i] == null || value[i] == undefined) {
                        value.splice(i, 1);
                    }
                }
            }
            else {
                deleteNullJsonObjInArray(value);
            }
        }
    }
}
function isEmpty(object) {
    for (var name in object) {
        if(object[name]) {
            return false;
        }
    }
    return true;
}











