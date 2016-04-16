/**
 * 接口服务
 */
Ext.onReady(function () {

    Ext.BLANK_IMAGE_URL = '../../js/ext/resources/images/default/s.gif';

    Ext.QuickTips.init();
    Ext.form.Field.prototype.msgTarget = 'side';

    var start = 0;
    var pageSize = 1000;
    var toolbar = new Ext.Toolbar({
        plain: true,
        width: 350,
        height: 30,
        items: ['表名称', {
            id: 'table_name.tb.info',
            xtype: 'textfield',
            emptyText: '输入表名称',
            width: 100
        }, {
            xtype: 'tbseparator'
        }, {
            text: '查询',
            iconCls: 'query',
            listeners: {
                click: function () {
                    var table_name = Ext.fly("table_name.tb.info").dom.value == '输入表名称'
                        ? null
                        : Ext.getCmp('table_name.tb.info').getValue();
                    store.setBaseParam('table_name', table_name);
                    store.load({
                        params: {
                            start: start,
                            limit: pageSize
                        }
                    });
                }
            }
        }]
    });
    var record = new Ext.data.Record.create([
        {name: 'table_name', mapping: 'table_name'},
        {name: 'type', mapping: 'type'},
        {name: 'comment', mapping: 'comment'}
    ]);
    var proxy = new Ext.data.HttpProxy({
        url: "../../TablesAction_find.action"
    });
    var reader = new Ext.data.JsonReader({
        totalProperty: "list",
        root: "rows",
        id: 'id'
    }, record);
    var store = new Ext.data.GroupingStore({
        id: "store.info",
        proxy: proxy,
        reader: reader
    });

    var rowNumber = new Ext.grid.RowNumberer();         //自动 编号
    var colM = new Ext.grid.ColumnModel([
        rowNumber,
        {header: "表名称", dataIndex: "table_name", align: 'center', sortable: true, menuDisabled: true},
        {header: "表描述", dataIndex: "comment", align: 'center', sortable: true, menuDisabled: true},
        {
            header: '操作标记',
            dataIndex: 'type',
            align: 'center',
            sortable: true,
            menuDisabled: true,
            renderer: show_flag,
            width: 100
        }
    ]);
    var page_toolbar = new Ext.PagingToolbar({
        pageSize: pageSize,
        store: store,
        displayInfo: true,
        displayMsg: "显示第{0}条记录到第{1}条记录，一共{2}条",
        emptyMsg: "没有记录",
        beforePageText: "当前页",
        afterPageText: "共{0}页"
    });
    var grid_panel = new Ext.grid.GridPanel({
        id: 'grid.info',
        plain: true,
        height: setHeight(),
        width: setWidth(),
        animCollapse: true,
        loadMask: {msg: '正在加载数据，请稍后...'},
        border: false,
        collapsible: false,
        cm: colM,
        store: store,
        stripeRows: true,
        autoExpandColumn: 2,
        disableSelection: true,
        bodyStyle: 'width:100%',
        enableDragDrop: true,
        selModel: new Ext.grid.RowSelectionModel({singleSelect: true}),
        viewConfig: {
            forceFit: true,
            enableRowBody: true,
            getRowClass: function (record, rowIndex, p, store) {
                return 'x-grid3-row-collapsed';
            }
        },
        tbar: [new Ext.Button({
            id: 'btnAdd.info',
            text: '新增',
            iconCls: 'add',
            handler: function () {
                insert_win();     //连接到 新增面板
            }
        }), {
            xtype: 'tbseparator'
        }/*, toolbar*/],
        view: new Ext.grid.GroupingView({
            forceFit: true,
            groupingTextTpl: '{text}({[values.rs.length]}条记录)'
        }),
        bbar: page_toolbar
    });
    new Ext.Viewport({
        layout: 'fit',
        renderTo: Ext.getBody(),
        items: [grid_panel]
    });
    store.load({
        params: {
            start: start, limit: pageSize
        }
    });
});


function show_flag(value, p, r) {
    if (value == "1") {
    return String.format(
        '<a id="update.info" href="javascript:void(0);" onclick="update();return false;" style="color: green;">修改</a>&nbsp;&nbsp;&nbsp;' +
        '<a id="del.info" href="javascript:void(0);" onclick="del();return false;" style="color: green;">删除</a>&nbsp;&nbsp;&nbsp;'+
        '<a id="viewInfo.info" href="javascript:void(0);" onclick="viewInfo();return false;" style="color: green;">详细</a>&nbsp;&nbsp;&nbsp;'
    );
    }else{
        return String.format(
            '<a id="update.info" href="javascript:void(0);" onclick="update();return false;" style="color: green;">修改</a>&nbsp;&nbsp;&nbsp;' +
            '<a id="viewInfo.info" href="javascript:void(0);" onclick="viewInfo();return false;" style="color: green;">详细</a>&nbsp;&nbsp;&nbsp;'
        );
    }
}

function insert_win() {
    var data = [
        //{Name: "1", DataType: "int1", Comment: "KKKK1"},
        //{Name: "2", DataType: "int2", Comment: "KKKK2"},
        //{Name: "3", DataType: "int3", Comment: "KKKK3"},
        //{Name: "4", DataType: "int4", Comment: "KKKK4"}
    ];

    var proxy = new Ext.data.MemoryProxy(data);

    var Order = Ext.data.Record.create(
        [
            {name: "Name", type: "string", mapping: "Name"},
            {name: "DataType", type: "string", mapping: "DataType"},
            {name: "Comment", type: "string", mapping: "Comment"}
        ]
    );

    var reader = new Ext.data.JsonReader({
        //totalProperty:"total",
        //root:"rows",
    }, Order);

    var store = new Ext.data.Store({
        proxy: proxy,
        reader: reader,
        autoLoad: true,
        pruneModifiedRecords: true
    });

    var cm = new Ext.grid.ColumnModel([
        {header: "字段名称", dataIndex: "Name", editor: new Ext.grid.GridEditor(new Ext.form.TextField())},
        {header: "数据类型", dataIndex: "DataType", editor: new Ext.grid.GridEditor(new Ext.form.TextField())},
        {header: "描述", dataIndex: "Comment", editor: new Ext.grid.GridEditor(new Ext.form.TextField())}
    ]);


    var tbar = new Ext.Toolbar({
        autoWidth: true,
        autoHeight: true,
        items: [
            '表名称',
            new Ext.form.TextField({
                name: 'table_name',
                id: 'tbar.table.table_name',
                allowBlank: false
            }), "-",
            '表描述',
            new Ext.form.TextField({
                name: 'table_des',
                id: 'tbar.table.table_des',
                allowBlank: false
            })
            , "-",
            {
                text: "保存表",
                iconCls: 'storage',
                handler: function () {
                    var store = grid.getStore();
                    if(store.getCount()==0){
                        Ext.Msg.alert("错误", "表字段不能为空");
                        return;
                    }
                    //得到修改过的Recored的集合
                    var modified = store.modified.slice(0);
                    //将数据放到另外一个数组中
                    var jsonArray = [];

                    Ext.each(modified, function (m) {
                        //alert(m.data.ADDRESS);//读取当前被修改的记录的地址
                        //m.data中保存的是当前Recored的所有字段的值json，不包含结构信息
                        jsonArray.push(m.data);
                    });



                    var table_name = Ext.getCmp("tbar.table.table_name").getValue();
                    var table_des = Ext.getCmp("tbar.table.table_des").getValue();
                    if (!table_name) {
                        Ext.Msg.alert("错误", "表名称不能为空");
                        return;
                    }
                    if (!table_des) {
                        Ext.Msg.alert("错误", "表描述不能为空");
                        return;
                    }
                    var r = checkBlank(modified);
                    if (!r) {
                        Ext.Msg.alert("错误", "字段信息不能为空");
                        return;
                    } else {
                        //通过ajax请求将修改的记录发送到服务器最终影响数据库
                        Ext.Ajax.request({
                            method: "post",//最好不要用get请求
                            url: "../../TablesAction_add.action",
                            success: function (response, config) {
                                var json = Ext.util.JSON.decode(response.responseText);
                                Ext.Msg.alert("提交成功", json.msg);
                                win.close();
                                grid.getStore().reload();
                            },
                            params: {
                                table_name: table_name,
                                table_des: table_des,
                                data: Ext.util.JSON.encode(jsonArray)
                            }
                        });
                    }
                }
            }
        ]
    });

    var tbar_render = new Ext.Toolbar({
        autoWidth: true,
        autoHeight: true,
        items: [{
            text: "添加字段",
            cls: "x-btn-text-icon",
            iconCls: 'add',
            handler: function () {
                var initValue = {
                    Name: "",
                    DataType: "",
                    Comment: ""
                };

                var order = new Order(initValue);
                grid.stopEditing();
                grid.getStore().add(order);

                //设置脏数据
                order.dirty = true;
                //只要一个字段值被修改了，该行的所有值都设置为脏读标记
                order.modified = initValue;
                if (grid.getStore().modified.indexOf(order) == -1) {
                    grid.getStore().modified.push(order);
                }
            }

        }, "-",
            {
                text: "删除字段",
                cls: "x-btn-text-icon",
                iconCls: 'remove',
                handler: function () {
                    var sm = grid.getSelectionModel();
                    if (sm.hasSelection()) {
                        Ext.Msg.confirm("提示", "真的要删除选中的行吗？", function (btn) {
                            if (btn == "yes") {
                                var cellIndex = sm.getSelectedCell();//获取被选择的单元格的行和列索引
                                var record = grid.getStore().getAt(cellIndex[0]);
                                grid.getStore().remove(record);
                            }
                        });
                    } else {
                        Ext.Msg.alert("错误", "请先选择删除的行，谢谢");
                    }
                }
            }
        ]
    });

    //EditorGridPanel定义
    var grid = new Ext.grid.EditorGridPanel({
        store: store,
        cm: cm,
        viewConfig: {forceFit: true},
        autoEncode: true, //提交时是否自动转码
        tbar: tbar,
        listeners: {
            render: function () {
                tbar_render.render(this.tbar);
            }
        }
    });


    var win = new Ext.Window({
        //title: '新增',
        width: 600,
        layout: 'fit',
        height: 300,
        modal: true,
        items: grid
    });
    win.show();

    //删除一行时，同步数据库
    grid.getStore().on("remove", function (s, record, index) {
        var table_name = Ext.getCmp("tbar.table.table_name").getValue();
        var id = record.get("id");//因为servlet只处理数组，所以先变成数组
        if (id != "") {
            Ext.Ajax.request({
                method: "post",
                url: "../../TablesAction_mod_delColumn.action",
                params: {table_name:table_name,id: record.get("id")},
                success: function (response, config) {
                    var msg = Ext.util.JSON.decode(response.responseText);
                    Ext.Msg.alert("", msg.msg);
                }
            });
        }
    });

    //验证是否输入的数据是有效的
    var checkBlank = function (modified) {
        var result = true;
        Ext.each(modified, function (record) {
            var keys = record.fields.keys;//获取Record的所有名称
            Ext.each(keys, function (name) {
                //根据名称获取相应的值
                var value = record.data[name];
                if (!value) {
                    //Ext.MessageBox.alert("验证", "对不起，存在未输入项！");
                    result = false;
                    return;
                }
                //找出指定名称所在的列索引
                var colIndex = cm.findColumnIndex(name);
                //根据行列索引找出组件编辑器
                var editor = cm.getCellEditor(colIndex).field;
                //验证是否合法
                var r = editor.validateValue(value);
                if (!r) {
                    //Ext.MessageBox.alert("验证", "对不起，您输入的数据非法！");
                    result = false;
                    return;
                }

            });
        });
        return result;
    }
}

function update() {
    var grid_panel = Ext.getCmp("grid.info");
    var recode = grid_panel.getSelectionModel().getSelected();
    if (!recode) {
        Ext.Msg.alert("提示", "请选择一条记录!");
    } else {
        var Order = Ext.data.Record.create(
            [
                {name: "id", type: "string", mapping: "id"},
                {name: "Name", type: "string", mapping: "Name"},
                {name: "DataType", type: "string", mapping: "DataType"},
                {name: "Comment", type: "string", mapping: "Comment"}
            ]
        );

        var proxy = new Ext.data.HttpProxy({
            url: "../../TablesAction_findTable.action?table_name="+recode.get("table_name")
        });

        var reader = new Ext.data.JsonReader({
            totalProperty: "list",
            root: "rows",
            id: 'id'
        }, Order);

        var store = new Ext.data.Store({
            proxy: proxy,
            reader: reader,
            autoLoad: true,
            pruneModifiedRecords: true
        });

        var cm = new Ext.grid.ColumnModel([
            {header: "字段名称", dataIndex: "Name",editor:new Ext.grid.GridEditor(new Ext.form.TextField())},
            {header: "数据类型", dataIndex: "DataType", editor: new Ext.grid.GridEditor(new Ext.form.TextField())},
            {header: "描述", dataIndex: "Comment", editor: new Ext.grid.GridEditor(new Ext.form.TextField())}
        ]);


        var tbar = new Ext.Toolbar({
            autoWidth: true,
            autoHeight: true,
            items: [
                '表名称',
                new Ext.form.TextField({
                    name: 'table_name',
                    value:recode.get("table_name"),
                    readOnly:true,
                    id: 'tbar.table.table_name',
                    allowBlank: false
                }), "-",
                '表描述',
                new Ext.form.TextField({
                    name: 'table_des',
                    value:recode.get("comment"),
                    id: 'tbar.table.table_des',
                    allowBlank: false
                })
                , "-",
                {
                    text: "保存表",
                    iconCls: 'storage',
                    handler: function () {
                        var store = grid.getStore();
                        if (store.getCount() == 0) {
                            Ext.Msg.alert("错误", "表字段不能为空");
                            return;
                        }
                        //得到修改过的Recored的集合
                        var modified = store.modified.slice(0);

                        //将数据放到另外一个数组中
                        var jsonArray = [];

                        Ext.each(modified, function (m) {
                            //alert(m.data.ADDRESS);//读取当前被修改的记录的地址
                            //m.data中保存的是当前Recored的所有字段的值json，不包含结构信息
                            jsonArray.push(m.data);
                        });


                        var table_name = Ext.getCmp("tbar.table.table_name").getValue();
                        var table_des = Ext.getCmp("tbar.table.table_des").getValue();
                        if (!table_name) {
                            Ext.Msg.alert("错误", "表名称不能为空");
                            return;
                        }
                        if (!table_des) {
                            Ext.Msg.alert("错误", "表描述不能为空");
                            return;
                        }
                        var r = checkBlank(modified);
                        if (!r) {
                            Ext.Msg.alert("错误", "字段信息不能为空");
                            return;
                        } else {
                            //通过ajax请求将修改的记录发送到服务器最终影响数据库
                            Ext.Ajax.request({
                                method: "post",//最好不要用get请求
                                url: "../../TablesAction_mod.action",
                                success: function (response, config) {
                                    var json = Ext.util.JSON.decode(response.responseText);
                                    Ext.Msg.alert("提交成功", json.msg);
                                    win.close();
                                    grid.getStore().reload();
                                },
                                params: {
                                    table_name: table_name,
                                    table_des: table_des,
                                    data: Ext.util.JSON.encode(jsonArray)
                                }
                            });
                        }
                    }
                }
            ]
        });

        var tbar_render = new Ext.Toolbar({
            autoWidth: true,
            autoHeight: true,
            items: [{
                text: "添加字段",
                cls: "x-btn-text-icon",
                iconCls: 'add',
                handler: function () {
                    var initValue = {
                        id: null,
                        Name: "",
                        DataType: "",
                        Comment: ""
                    };

                    var order = new Order(initValue);
                    grid.stopEditing();
                    grid.getStore().add(order);

                    //设置脏数据
                    order.dirty = true;
                    //只要一个字段值被修改了，该行的所有值都设置为脏读标记
                    order.modified = initValue;
                    if (grid.getStore().modified.indexOf(order) == -1) {
                        grid.getStore().modified.push(order);
                    }
                }

            }, "-",
                {
                    text: "删除字段",
                    cls: "x-btn-text-icon",
                    iconCls: 'remove',
                    handler: function () {
                        var sm = grid.getSelectionModel();
                        if (sm.hasSelection()) {
                            Ext.Msg.confirm("提示", "真的要删除选中的行吗？", function (btn) {
                                if (btn == "yes") {
                                    var cellIndex = sm.getSelectedCell();//获取被选择的单元格的行和列索引
                                    var record = grid.getStore().getAt(cellIndex[0]);
                                    grid.getStore().remove(record);
                                }
                            });
                        } else {
                            Ext.Msg.alert("错误", "请先选择删除的行，谢谢");
                        }
                    }
                }
            ]
        });

        //EditorGridPanel定义
        var grid = new Ext.grid.EditorGridPanel({
            store: store,
            cm: cm,
            viewConfig: {forceFit: true},
            autoEncode: true, //提交时是否自动转码
            tbar: tbar,
            listeners: {
                render: function () {
                    tbar_render.render(this.tbar);
                }
            }
        });


        var win = new Ext.Window({
            //title: '新增',
            width: 600,
            layout: 'fit',
            height: 300,
            modal: true,
            items: grid
        });
        win.show();

        //删除一行时，同步数据库
        grid.getStore().on("remove", function (s, record, index) {
            var table_name = Ext.getCmp("tbar.table.table_name").getValue();
            var id = record.get("id");//因为servlet只处理数组，所以先变成数组
            if (id != "") {
                Ext.Ajax.request({
                    method: "post",
                    url: "../../TablesAction_mod_delColumn.action",
                    params: {table_name:table_name,id: record.get("id")},
                    success: function (response, config) {
                        var msg = Ext.util.JSON.decode(response.responseText);
                        Ext.Msg.alert("", msg.msg);
                    }
                });
            }
        });

        //验证是否输入的数据是有效的
        var checkBlank = function (modified) {
            var result = true;
            Ext.each(modified, function (record) {
                var keys = record.fields.keys;//获取Record的所有名称
                Ext.each(keys, function (name) {
                    //根据名称获取相应的值
                    if(name=="id"){

                    }else {
                        var value = record.data[name];
                        if (!value) {
                            //Ext.MessageBox.alert("验证", "对不起，存在未输入项！");
                            result = false;
                            return;
                        }
                        //找出指定名称所在的列索引
                        var colIndex = cm.findColumnIndex(name);
                        //根据行列索引找出组件编辑器
                        var editor = cm.getCellEditor(colIndex).field;
                        //验证是否合法
                        var r = editor.validateValue(value);
                        if (!r) {
                            //Ext.MessageBox.alert("验证", "对不起，您输入的数据非法！");
                            result = false;
                            return;
                        }
                    }
                });
            });
            return result;
        }
    }
}

function viewInfo() {
    var grid_panel = Ext.getCmp("grid.info");
    var recode = grid_panel.getSelectionModel().getSelected();
    if (!recode) {
        Ext.Msg.alert("提示", "请选择一条记录!");
    } else {
        var Order = Ext.data.Record.create(
            [
                {name: "id", type: "string", mapping: "id"},
                {name: "Name", type: "string", mapping: "Name"},
                {name: "DataType", type: "string", mapping: "DataType"},
                {name: "Comment", type: "string", mapping: "Comment"}
            ]
        );

        var proxy = new Ext.data.HttpProxy({
            url: "../../TablesAction_findTable.action?table_name="+recode.get("table_name")
        });

        var reader = new Ext.data.JsonReader({
            totalProperty: "list",
            root: "rows",
            id: 'id'
        }, Order);

        var store = new Ext.data.Store({
            proxy: proxy,
            reader: reader,
            autoLoad: true,
            pruneModifiedRecords: true
        });

        var cm = new Ext.grid.ColumnModel([
            {header: "字段名称", dataIndex: "Name",editor:new Ext.grid.GridEditor(new Ext.form.TextField())},
            {header: "数据类型", dataIndex: "DataType", editor: new Ext.grid.GridEditor(new Ext.form.TextField())},
            {header: "描述", dataIndex: "Comment", editor: new Ext.grid.GridEditor(new Ext.form.TextField())}
        ]);


        var tbar = new Ext.Toolbar({
            autoWidth: true,
            autoHeight: true,
            items: [
                '表名称',
                new Ext.form.TextField({
                    name: 'table_name',
                    value:recode.get("table_name"),
                    readOnly:true,
                    id: 'tbar.table.table_name',
                    allowBlank: false
                }), "-",
                '表描述',
                new Ext.form.TextField({
                    name: 'table_des',
                    readOnly:true,
                    value:recode.get("comment"),
                    id: 'tbar.table.table_des',
                    allowBlank: false
                })
            ]
        });

        //EditorGridPanel定义
        var grid = new Ext.grid.EditorGridPanel({
            store: store,
            cm: cm,
            viewConfig: {forceFit: true},
            autoEncode: true, //提交时是否自动转码
            tbar: tbar
        });


        var win = new Ext.Window({
            //title: '新增',
            width: 600,
            layout: 'fit',
            height: 300,
            modal: true,
            items: grid
        });
        win.show();
    }
}

function del() {
    var grid_panel = Ext.getCmp("grid.info");
    var recode = grid_panel.getSelectionModel().getSelected();
    if (!recode) {
        Ext.Msg.alert("提示", "请选择一条记录!");
    } else {
        Ext.Msg.confirm("提示", "确定删除数据表？", function (sid) {
            if (sid == "yes") {
                Ext.Ajax.request({
                    url: "../../TablesAction_del.action",
                    timeout: 20 * 60 * 1000,
                    method: "POST",
                    params: {table_name: recode.get('table_name')},
                    success: function (r, o) {
                        var respText = Ext.util.JSON.decode(r.responseText);
                        var msg = respText.msg;
                        Ext.Msg.alert("提示", msg);
                        grid_panel.getStore().reload();
                    },
                    failure: function (r, o) {
                        var respText = Ext.util.JSON.decode(r.responseText);
                        var msg = respText.msg;
                        Ext.Msg.alert("提示", msg);
                    }
                });
            }
        });
    }
}

function setHeight() {
    var h = document.body.clientHeight - 8;
    return h;
}

function setWidth() {
    return document.body.clientWidth - 8;
}