/**
 * 接口服务
 */
Ext.onReady(function () {
    Ext.BLANK_IMAGE_URL = '../../js/ext/resources/images/default/s.gif';

    Ext.QuickTips.init();
    Ext.form.Field.prototype.msgTarget = 'side';

    var start = 0;
    var pageSize = 15;
    var toolbar = new Ext.Toolbar({
        plain: true,
        width: 350,
        height: 30,
        items: ['接口编码', {
            id: 'userName.tb.info',
            xtype: 'textfield',
            emptyText: '输入接口名称',
            width: 100
        }, {
            xtype: 'tbseparator'
        },
            /* '状态', {
         id: 'status.tb.info',
         xtype: 'combo',
         store: storeStatus,
         valueField: 'value',
         displayField: 'key',
         mode: 'local',
         forceSelection: true,
         triggerAction: 'all',
         emptyText: '--请选择--',
         value: '',
         selectOnFocus: true,
         width: 100
         },*/ {
            xtype: 'tbseparator'
        }, {
            text: '查询',
            iconCls: 'query',
            listeners: {
                click: function () {
                    var userName = Ext.fly("userName.tb.info").dom.value == '输入接口编码'
                        ? null
                        : Ext.getCmp('userName.tb.info').getValue();
                    var status = Ext.fly('status.tb.info').dom.value == '--请选择--'
                        ? null
                        : Ext.getCmp('status.tb.info').getValue();
                    store.setBaseParam('name', userName);
                    //store.setBaseParam('status', status);
                    store.load({
                        params: {
                            start: start,
                            limit: pageSize
                        }
                    });
                }
            }
        }/*, {
                text: '联合查询',
                iconCls: 'query',
                listeners: {
                    click: function () {
                       test_all();
                    }
                }
            }*/]
    });
    var record = new Ext.data.Record.create([
        {name: 'id', mapping: 'id'},
        //{name: 'name', mapping: 'name'},
        //{name: 'tableType', mapping: 'tableType'},
        //{name: 'interfaceType', mapping: 'interfaceType'},
        //{name: 'interfaceDesc', mapping: 'interfaceDesc'},
        //{name: 'containsPerson', mapping: 'containsPerson'},
        //{name: 'tableName', mapping: 'tableName'},
        //{name: 'tableNameEn', mapping: 'tableNameEn'},
        //{name: 'queryField', mapping: 'queryField'},
        {name: 'interfaceNumber', mapping: 'interfaceNumber'},
        //{name: 'conditionsField', mapping: 'conditionsField'},
        //{name: 'status', mapping: 'status'},
        {name: 'accountType', mapping: 'accountType'},
        {name: 'flag', mapping: 'flag'},
        {name: 'apply', mapping: 'apply'}/*,
        {name: 'url', mapping: 'url'}*/
    ]);
    var proxy = new Ext.data.HttpProxy({
        url: "../../InterfaceAction_select.action"
    });
    var reader = new Ext.data.JsonReader({
        totalProperty: "total",
        root: "rows",
        id: 'id'
    }, record);
    var store = new Ext.data.GroupingStore({
        id: "store.info",
        proxy: proxy,
        reader: reader
    });

//    var boxM = new Ext.grid.CheckboxSelectionModel();   //复选框
    var rowNumber = new Ext.grid.RowNumberer();         //自动 编号
    var colM = new Ext.grid.ColumnModel([
//        boxM,
        rowNumber,
        {header: "接口编码", dataIndex: "interfaceNumber", align: 'center', sortable: true, menuDisabled: true},
        //{header: "接口名称", dataIndex: "name", align: 'center', sortable: true, menuDisabled: true},
        //{header: "表名称", dataIndex: "tableName", align: 'center', sortable: true, menuDisabled: true},
        //{header: '英文表名', dataIndex: 'tableNameEn', align: 'center', sortable: true, menuDisabled: true},
        //{header: '查询字段', dataIndex: 'queryField', align: 'center', sortable: true, menuDisabled: true},
        //{header: '条件字段', dataIndex: 'conditionsField', align: 'center', sortable: true, menuDisabled: true},
        //{header: '状态', dataIndex: 'status', align: 'center', sortable: true, menuDisabled: true, renderer: show_status},
        //{header: 'URL', dataIndex: 'url', align: 'center', sortable: true, menuDisabled: true},
        {header: '申请状态', dataIndex: 'apply', align: 'center', sortable: true, menuDisabled: true, renderer: show_apply},
        {
            header: '操作标记',
            dataIndex: 'flag',
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
//        sm:boxM,
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
        tbar: [/*new Ext.Button({
            id: 'btnAdd.info',
            text: '新增',
            iconCls: 'add',
            handler: function () {
                insert_win(grid_panel, store);     //连接到 新增 面板
            }
        }),*/ {
            xtype: 'tbseparator'
        }, toolbar],
        view: new Ext.grid.GroupingView({
            forceFit: true,
            groupingTextTpl: '{text}({[values.rs.length]}条记录)'
        }),
        bbar: page_toolbar
    });
    var port = new Ext.Viewport({
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
function setHeight() {
    var h = document.body.clientHeight - 8;
    return h;
}

function setWidth() {
    return document.body.clientWidth - 8;
}

function show_status(value) {
    if (value == '0') {
        return '<span style="color:red;">未挂接</span>';
    } else if (value == '1') {
        return '<span style="color:green;">已挂接</span>';
    } else {
        return '<span style="color:gray;">未知状态:' + value + '</span>';
    }
}

function show_apply(value, p, r) {
    if (value == 0) {
        return '<span style="color:#7f8080;">无申请</span>';
    } else if (value == 1) {
        return '<span style="color:red;">有申请</span>';
    } else if (value == 3) {
        return '<span style="color:green;">已申请</span>';
    } else {
        return '<span style="color:gray;">未知状态:' + value + '</span>';
    }
}

function show_flag(value, p, r) {
    var flag = value;
    var interfaceId = r.get('id');
    var accountType = r.get('accountType');
    var apply = r.get('apply');
    var test = String.format('<a href="javascript:void(0);" onclick="test(\'' + interfaceId + '\');return false;" style="color: green;">信息查询</a>');
    var applyHtml = String.format('<a href="javascript:void(0);" onclick="apply(\'' + interfaceId + '\');return false;" style="color: green;">申请</a>');
    var _applyHtml = String.format('<span style="color:gray;">信息查询</span>');
    //var del = String.format('<a href="javascript:void(0);" onclick="deleteInterface(\'' + interfaceId + '\');return false;" style="color: green;">删除</a>');
    //var auth = String.format('<a href="javascript:void(0);" onclick="authInterface(\'' + interfaceId + '\');return false;" style="color: green;">授权</a>');
    //var update = String.format('<a href="javascript:void(0);" onclick="update_win();return false;" style="color: green;">修改</a>');
    if (flag == 1) {
        return test + '&nbsp;&nbsp;'
                //+ update + '&nbsp;&nbsp;'
            //+ del + '&nbsp;&nbsp;'
            //+ auth;
    } else if (flag == 2) {  //已授权
        return test + '&nbsp;&nbsp;'
        //+ update;
    } else if (flag == 3) {
        if (apply == 3) {
            return _applyHtml + '&nbsp;&nbsp;'
            /* + update;*/
        }
        return applyHtml + '&nbsp;&nbsp;'
        /* + update;*/
    }
}

/*var dataStatus = [['0', '未挂接'], ['1', '已挂接'], [null, '全部']];
 var storeStatus = new Ext.data.SimpleStore({fields: ['value', 'key'], data: dataStatus});

 var dataDB = [['ORACLE', 'ORACLE'], ['SQLServer', 'SQLServer']];
 var storeDB = new Ext.data.SimpleStore({fields: ['value', 'key'], data: dataDB});

 var dataInterface = [['1', '数据服务']];
 var storeInterface = new Ext.data.SimpleStore({fields: ['value', 'key'], data: dataInterface});

 var dataField = [['ry', '人员'], ['zj', '证件']];
 var storeField = new Ext.data.SimpleStore({fields: ['value', 'key'], data: dataField});

 var dataDepart = [['xz', '刑侦'], ['za', '治安']];
 var storeDepart = new Ext.data.SimpleStore({fields: ['value', 'key'], data: dataDepart});*/

/*var proxy = new Ext.data.HttpProxy({
 url: '../../TablesAction_findStore.action',
 method: "POST"
 });*/

var table_store = new Ext.data.Store({
    reader: new Ext.data.JsonReader({
        fields: ["key", "value"],
        totalProperty: 'totalCount',
        root: 'rows'
    })/*,
     proxy: proxy,
     load: function () {
     var v = Ext.getCmp("update.tableNameEn").getValue();
     Ext.getCmp("update.tableNameEn").setValue(v);
     }*/
});


/**
 * 新增用户
 * @param grid
 * @param store
 */
function insert_win(grid_panel, store) {
    var formPanel = new Ext.form.FormPanel({
        autoScroll: true,
        //layout: 'column',
        height: 100,
        layout: 'form',
        border: false,
        //anchor : '90%',
        labelAlign: 'right',
        labelWidth: 80,
        defaults: {
            anchor: '90%',
            allowBlank: false,
            blankText: '该项不能为空！'
        },
        defaultType: 'textfield',
        plain: true,
        items: [/*{
         plain: true,
         columnWidth: .5,
         border: false,
         layout: 'form',
         items: [{
         plain: true,
         labelAlign: 'right',
         labelWidth: 80,
         defaultType: 'textfield',
         border: false,
         layout: 'form',
         defaults: {
         width: 200,
         allowBlank: false,
         blankText: '该项不能为空！'
         },
         items:*//* [*//*{
         fieldLabel: "表类型",
         hiddenName: 'interfaceObj.tableType',
         xtype: 'combo',
         mode: 'local',
         value: 'ORACLE',
         emptyText: '--请选择--',
         editable: false,
         typeAhead: true,
         forceSelection: true,
         triggerAction: 'all',
         displayField: "key", valueField: "value",
         store: storeDB
         },*/{
            fieldLabel: "接口编码",
            name: 'interfaceObj.interfaceNumber',
            regex: /^.{2,30}$/,
            regexText: '请输入任意2--30个字符',
            emptyText: '请输入任意2--30个字符'
        }, /*{
         fieldLabel: "接口类型",
         value: '1',
         hiddenName: 'interfaceObj.interfaceType',
         xtype: 'combo',
         mode: 'local',
         emptyText: '--请选择--',
         editable: false,
         typeAhead: true,
         forceSelection: true,
         triggerAction: 'all',
         displayField: "key",
         valueField: "value",
         store: storeInterface
         },*/ {
            fieldLabel: "英文表",
            hiddenName: 'interfaceObj.tableNameEn',
            xtype: 'combo',
            emptyText: '--请选择--',
            editable: false,
            typeAhead: true,
            forceSelection: true,
            triggerAction: 'all',
            mode: 'remote',// 指定数据加载方式，如果直接从客户端加载则为local，如果从服务器断加载// 则为remote.默认值为：remote
            displayField: "key",
            valueField: "value",
            store: table_store,
            listeners: {
                'select': function (combo, record, index) {
                    var value = combo.getValue();
                    store.proxy = new Ext.data.HttpProxy({
                        url: "../../TablesAction_findColumnStore.action?table_name=" + value
                    })
                    store.load();
                },
                render: function () {
                    table_store.proxy = new Ext.data.HttpProxy({
                        url: '../../TablesAction_findStore.action',
                        method: "POST"
                    })
                }
            }
        }/*, {
         fieldLabel: "接口简称",
         name: 'interfaceObj.interfaceDesc',
         regex: /^.{2,30}$/,
         regexText: '请输入任意2--30个字符',
         emptyText: '请输入任意2--30个字符'
         }*//*,  {
         fieldLabel: "挂接状态", hiddenName: 'interfaceObj.status',
         xtype: 'combo',
         mode: 'local',
         emptyText: '--请选择--',
         editable: false,
         typeAhead: true,
         forceSelection: true,
         triggerAction: 'all',
         displayField: "key", valueField: "value",
         store: storeStatus,
         value: '0'
         }*/
            /*, {
                fieldLabel: "URL地址",
                name: 'interfaceObj.url',
                //xtype: 'textarea',
                allowBlank: true,
                regex: /^.{0,300}$/,
                regexText: '请输入任意1--300个字符',
                value: 'http://192.18.1.100:8080/axis_service/services'
            }*//*]*/
            /*}*//*]*/
            /*}*//*, {
             plain: true,
             defaultType: 'textfield',
             columnWidth: .5,
             labelAlign: 'right',
             labelWidth: 80,
             border: false,
             layout: 'form',
             defaults: {
             width: 200,
             allowBlank: false,
             blankText: '该项不能为空！'
             },
             items: [/!*{
             fieldLabel: "要素属性",
             hiddenName: 'interfaceObj.containsPerson',
             xtype: 'combo',
             mode: 'local',
             emptyText: '--请选择--',
             editable: false,
             typeAhead: true,
             forceSelection: true,
             triggerAction: 'all',
             displayField: "key", valueField: "value",
             value: 'ry',
             store: storeField
             }, {
             fieldLabel: "管理部门", hiddenName: 'interfaceObj.depart',
             xtype: 'combo',
             mode: 'local',
             emptyText: '--请选择--',
             editable: false,
             typeAhead: true,
             forceSelection: true,
             triggerAction: 'all',
             displayField: "key", valueField: "value", value: 'xz',
             store: storeDepart
             },{
             fieldLabel: "接口名称",
             name: 'interfaceObj.name',
             regex: /^.{2,30}$/,
             regexText: '请输入任意2--30个字符',
             emptyText: '请输入任意2--30个字符',
             listeners: {
             /!*blur:function(){
             var userName = this.getValue();
             if(userName.length>0){
             var myMask = new Ext.LoadMask(Ext.getBody(),{
             msg:'正在校验,请稍后...',
             removeMask:true
             });
             myMask.show();
             Ext.Ajax.request({
             url : '../../AccountAction_checkUserName.action',
             params :{userName:userName},
             method:'POST',
             success : function(r,o) {
             var respText = Ext.util.JSON.decode(r.responseText);
             var msg = respText.msg;
             myMask.hide();
             if(msg != 'true'){
             Ext.MessageBox.show({
             title:'信息',
             width:250,
             msg:msg,
             buttons:{'ok':'确定'},
             icon:Ext.MessageBox.INFO,
             closable:false,
             fn:function(e){
             if(e=='ok'){
             Ext.getCmp('userName.insert.info').setValue('');
             }
             }
             });
             }
             }
             });
             }
             }*!/
             }
             }, {
             fieldLabel: "表名称",
             name: 'interfaceObj.tableName',
             regex: /^.{2,30}$/,
             regexText: '请输入任意2--30个字符',
             emptyText: '请输入任意2--30个字符'
             },*!/ {
             fieldLabel: "URL地址",
             name: 'interfaceObj.url',
             //xtype: 'textarea',
             allowBlank: true,
             regex: /^.{0,300}$/,
             regexText: '请输入任意1--300个字符',
             value: 'http://192.18.1.100:8080/axis_service/services'
             }]
             }*/]
    });

    var record = new Ext.data.Record.create([
        {name: 'id', mapping: 'id'},
        {name: 'Name', mapping: 'Name'},
        {name: 'DataType', mapping: 'DataType'},
        {name: 'Comment', mapping: 'Comment'},
        {name: 'tj_checked', mapping: 'tj_checked'},
        {name: 'zx_checked', mapping: 'zx_checked'}
    ]);

    var reader = new Ext.data.JsonReader({
        totalProperty: "list",
        root: "rows"
    }, record);

    var store = new Ext.data.GroupingStore({
        id: "store.grid.db.info",
        reader: reader
    });

    var zxArray = new Array();
    var tjArray = new Array();

    //===================================================----------------------------------------------------------==============================================
    var start = 0;
    var pageSize = 10;
    //var boxM = new Ext.grid.CheckboxSelectionModel();   //复选框
    var rowNumber = new Ext.grid.RowNumberer();         //自动 编号
    var colM = new Ext.grid.ColumnModel([
        //boxM,
        rowNumber,
        {header: "数据项中文名", dataIndex: "Comment", align: 'center', sortable: true, menuDisabled: true},
        {
            header: "数据项标识",
            dataIndex: "Name",
            align: 'center',
            sortable: true,
            menuDisabled: true
        },
        {
            header: "数据项类型",
            dataIndex: "DataType",
            align: 'center',
            sortable: true,
            menuDisabled: true
        },
        {
            header: "展示字段", dataIndex: "zx_checked", align: 'center', sortable: true, renderer: function (value, o, r) {
            var id = r.get("id");
            if (value == "1") {
                zxArray[id] = {success: true, field: r.get("Name")};
            } else {
                zxArray[id] = {success: false, field: r.get("Name")};
            }
            return '<input  type="checkbox"' + (value == "1" ? "checked='checked'" : "") + 'onclick="updateZx_checked(' + value + ',this.checked)"' + '/>';
        }
        },
        {
            header: "条件字段",
            dataIndex: "tj_checked",
            align: 'center',
            sortable: true,
            renderer: function (value, p, r) {
                var id = r.get("id");
                if (value == "1") {
                    tjArray[id] = {success: true, field: r.get("Name")};
                } else {
                    tjArray[id] = {success: false, field: r.get("Name")};
                }
                return '<input  type="checkbox"' + (value == "1" ? "checked='checked'" : "") + 'onclick="updateTj_checked(' + value + ',this.checked)"' + '/>';
            }
        }
    ]);
    colM.defaultSortable = true;
    var grid = new Ext.grid.EditorGridPanel({
        id: 'external_update_db_source_tables.grid.info',
        plain: true,
        animCollapse: true,
        autoScroll: true,
        height: 310,
        loadMask: {msg: '正在加载数据，请稍后...'},
        border: false,
        collapsible: false,
        cm: colM,
        //sm: boxM,
        store: store,
        stripeRows: true,
        autoExpandColumn: 1,
        clicksToEdit: 1,
        disableSelection: true,
        bodyStyle: 'width:100%',
        selModel: new Ext.grid.RowSelectionModel({singleSelect: true}),
        viewConfig: {
            forceFit: true,
            enableRowBody: true,
            getRowClass: function (record, rowIndex, p, store) {
                return 'x-grid3-row-collapsed';
            }
        }
    });
    var win = new Ext.Window({
        title: "接口注册信息",
        width: 650,
        height: 390,
        layout: 'fit',
        modal: true,
        items: [{
            frame: true,
            autoScroll: true,
            items: [{
                layout: 'fit',
                items: formPanel
            }, grid]
        }],
        bbar: [
            '->',
            {
                id: 'insert_win.info',
                text: '保存',
                handler: function () {
                    if (formPanel.form.isValid()) {
                        formPanel.getForm().submit({
                            url: '../../InterfaceAction_insert.action',
                            method: 'POST',
                            params: {
                                zxArray: Ext.util.JSON.encode(zxArray),
                                tjArray: Ext.util.JSON.encode(tjArray)
                            },
                            waitTitle: '系统提示',
                            waitMsg: '正在保存,请稍后...',
                            success: function (form, action) {
                                var msg = action.result.msg;
                                Ext.MessageBox.show({
                                    title: '信息',
                                    width: 250,
                                    msg: msg,
                                    animEl: 'insert_win.info',
                                    buttons: {'ok': '确定'},
                                    icon: Ext.MessageBox.INFO,
                                    closable: false,
                                    fn: function (e) {
                                        if (e == 'ok') {
                                            grid_panel.getStore().reload();
                                            win.close();
                                        }
                                    }
                                });
                            }
                        });
                    } else {
                        Ext.MessageBox.show({
                            title: '信息',
                            width: 200,
                            msg: '请填写完成再提交!',
                            animEl: 'insert_win.info',
                            buttons: {'ok': '确定'},
                            icon: Ext.MessageBox.ERROR,
                            closable: false
                        });
                    }
                }
            }, {
                text: '关闭',
                handler: function () {
                    win.close();
                }
            }
        ]
    }).show();

    Model.updateZx_checked = function updateZx_checked(value, checked) {
        if (checked) {
            var grid_panel = Ext.getCmp("external_update_db_source_tables.grid.info");
            var r = grid_panel.getSelectionModel().getSelected();
            var id = r.get("id")
            zxArray[id] = {success: true, field: r.get("Name")};
        } else {
            var grid_panel = Ext.getCmp("external_update_db_source_tables.grid.info");
            var recode = grid_panel.getSelectionModel().getSelected();
            var id = recode.get("id");
            zxArray[id] = {success: false, field: r.get("Name")};
        }
    }

    Model.updateTj_checked = function updateTj_checked(value, checked) {
        if (checked) {
            var grid_panel = Ext.getCmp("external_update_db_source_tables.grid.info");
            var r = grid_panel.getSelectionModel().getSelected();
            var id = r.get("id");
            tjArray[id] = {success: true, field: r.get("Name")};
        } else {
            var grid_panel = Ext.getCmp("external_update_db_source_tables.grid.info");
            var recode = grid_panel.getSelectionModel().getSelected();
            var id = recode.get("id");
            tjArray[id] = {success: false, field: r.get("Name")};
        }
    }
}

/*
function update_win() {
    var grid_panel = Ext.getCmp("grid.info");
    var recode = grid_panel.getSelectionModel().getSelected();
    var formPanel = new Ext.form.FormPanel({
        autoScroll: true,
        layout: 'column',
        height: 200,
        border: false,
        plain: true,
        items: [{
            plain: true,
            columnWidth: .5,
            border: false,
            layout: 'form',
            items: [{
                plain: true,
                labelAlign: 'right',
                labelWidth: 80,
                defaultType: 'textfield',
                border: false,
                layout: 'form',
                defaults: {
                    width: 200,
                    allowBlank: false,
                    blankText: '该项不能为空！'
                },
                items: [{
                    fieldLabel: "表类型",
                    hiddenName: 'interfaceObj.tableType',
                    xtype: 'combo',
                    mode: 'local',
                    value: recode.get('tableType'),
                    emptyText: '--请选择--',
                    editable: false,
                    typeAhead: true,
                    forceSelection: true,
                    triggerAction: 'all',
                    displayField: "key", valueField: "value",
                    store: storeDB
                }, {
                    fieldLabel: "接口编码",
                    name: 'interfaceObj.interfaceNumber',
                    value: recode.get('interfaceNumber'),
                    regex: /^.{2,30}$/,
                    regexText: '请输入任意2--30个字符',
                    emptyText: '请输入任意2--30个字符'
                }, {
                    fieldLabel: "接口类型",
                    value: '1',
                    hiddenName: 'interfaceObj.interfaceType',
                    value: recode.get('interfaceType'),
                    xtype: 'combo',
                    mode: 'local',
                    emptyText: '--请选择--',
                    editable: false,
                    typeAhead: true,
                    forceSelection: true,
                    triggerAction: 'all',
                    displayField: "key",
                    valueField: "value",
                    store: storeInterface
                }, {
                    fieldLabel: "英文表名",
                    hiddenName: 'interfaceObj.tableNameEn',
                    value: recode.get('tableNameEn'),
                    xtype: 'combo',
                    emptyText: '--请选择--',
                    editable: false,
                    typeAhead: true,
                    id: 'update.tableNameEn',
                    forceSelection: true,
                    triggerAction: 'all',
                    mode: 'remote',// 指定数据加载方式，如果直接从客户端加载则为local，如果从服务器断加载// 则为remote.默认值为：remote
                    displayField: "key",
                    valueField: "value",
                    store: table_store,
                    listeners: {
                        'select': function (combo, record, index) {
                            var value = combo.getValue();
                            store.proxy = new Ext.data.HttpProxy({
                                url: "../../TablesAction_findColumnStore.action?table_name=" + value
                            })
                            store.load();
                        },
                        render: function () {
                            table_store.proxy = new Ext.data.HttpProxy({
                                url: '../../TablesAction_findStore.action',
                                method: "POST"
                            });
                        }
                    }
                }, {
                    fieldLabel: "接口简称",
                    name: 'interfaceObj.interfaceDesc',
                    value: recode.get('interfaceDesc'),
                    regex: /^.{2,30}$/,
                    regexText: '请输入任意2--30个字符',
                    emptyText: '请输入任意2--30个字符'
                }, {
                    fieldLabel: "挂接状态", hiddenName: 'interfaceObj.status',
                    xtype: 'combo',
                    mode: 'local',
                    value: recode.get('status'),
                    emptyText: '--请选择--',
                    editable: false,
                    typeAhead: true,
                    forceSelection: true,
                    triggerAction: 'all',
                    displayField: "key", valueField: "value",
                    store: storeStatus,
                    value: '0'
                }]
            }]
        }, {
            plain: true,
            defaultType: 'textfield',
            columnWidth: .5,
            labelAlign: 'right',
            labelWidth: 80,
            border: false,
            layout: 'form',
            defaults: {
                width: 200,
                allowBlank: false,
                blankText: '该项不能为空！'
            },
            items: [{
                fieldLabel: "要素属性",
                hiddenName: 'interfaceObj.containsPerson',
                value: recode.get('containsPerson'),
                xtype: 'combo',
                mode: 'local',
                emptyText: '--请选择--',
                editable: false,
                typeAhead: true,
                forceSelection: true,
                triggerAction: 'all',
                displayField: "key", valueField: "value",
                value: 'ry',
                store: storeField
            }, {
                fieldLabel: "管理部门", hiddenName: 'interfaceObj.depart',
                xtype: 'combo',
                value: recode.get('depart'),
                mode: 'local',
                emptyText: '--请选择--',
                editable: false,
                typeAhead: true,
                forceSelection: true,
                triggerAction: 'all',
                displayField: "key", valueField: "value", value: 'xz',
                store: storeDepart
            }, {
                fieldLabel: "接口名称",
                name: 'interfaceObj.name',
                value: recode.get('name'),
                regex: /^.{2,30}$/,
                regexText: '请输入任意2--30个字符',
                emptyText: '请输入任意2--30个字符',
                listeners: {
                    /!*blur:function(){
                     var userName = this.getValue();
                     if(userName.length>0){
                     var myMask = new Ext.LoadMask(Ext.getBody(),{
                     msg:'正在校验,请稍后...',
                     removeMask:true
                     });
                     myMask.show();
                     Ext.Ajax.request({
                     url : '../../AccountAction_checkUserName.action',
                     params :{userName:userName},
                     method:'POST',
                     success : function(r,o) {
                     var respText = Ext.util.JSON.decode(r.responseText);
                     var msg = respText.msg;
                     myMask.hide();
                     if(msg != 'true'){
                     Ext.MessageBox.show({
                     title:'信息',
                     width:250,
                     msg:msg,
                     buttons:{'ok':'确定'},
                     icon:Ext.MessageBox.INFO,
                     closable:false,
                     fn:function(e){
                     if(e=='ok'){
                     Ext.getCmp('userName.insert.info').setValue('');
                     }
                     }
                     });
                     }
                     }
                     });
                     }
                     }*!/
                }
            }, {
                fieldLabel: "表名称",
                name: 'interfaceObj.tableName',
                value: recode.get('tableName'),
                regex: /^.{2,30}$/,
                regexText: '请输入任意2--30个字符',
                emptyText: '请输入任意2--30个字符'
            }, {
                fieldLabel: "URL地址",
                name: 'interfaceObj.url',
                value: recode.get('url'),
                xtype: 'textarea',
                allowBlank: true,
                regex: /^.{0,300}$/,
                regexText: '请输入任意1--300个字符',
                value: 'http://192.18.1.100:8080/axis_service/services'
            }]
        }]
    });

    var record = new Ext.data.Record.create([
        {name: 'id', mapping: 'id'},
        {name: 'Name', mapping: 'Name'},
        {name: 'DataType', mapping: 'DataType'},
        {name: 'Comment', mapping: 'Comment'},
        {name: 'tj_checked', mapping: 'tj_checked'},
        {name: 'zx_checked', mapping: 'zx_checked'}
    ]);

    var reader = new Ext.data.JsonReader({
        totalProperty: "list",
        root: "rows"
    }, record);

    var store = new Ext.data.GroupingStore({
        id: "store.grid.db.info",
        reader: reader
    });

    var zxArray = new Array();
    var tjArray = new Array();

    //===================================================----------------------------------------------------------==============================================
    var start = 0;
    var pageSize = 10;
    //var boxM = new Ext.grid.CheckboxSelectionModel();   //复选框
    var rowNumber = new Ext.grid.RowNumberer();         //自动 编号
    var colM = new Ext.grid.ColumnModel([
        //boxM,
        rowNumber,
        {header: "数据项中文名", dataIndex: "Comment", align: 'center', sortable: true, menuDisabled: true},
        {
            header: "数据项标识",
            dataIndex: "Name",
            align: 'center',
            sortable: true,
            menuDisabled: true
        },
        {
            header: "数据项类型",
            dataIndex: "DataType",
            align: 'center',
            sortable: true,
            menuDisabled: true
        },
        {
            header: "展示字段", dataIndex: "zx_checked", align: 'center', sortable: true, renderer: function (value, o, r) {
            var id = r.get("id");
            if (value == "1") {
                zxArray[id] = {success: true, field: r.get("Name")};
            } else {
                zxArray[id] = {success: false, field: r.get("Name")};
            }
            return '<input  type="checkbox"' + (value == "1" ? "checked='checked'" : "") + 'onclick="updateZx_checked(' + value + ',this.checked)"' + '/>';
        }
        },
        {
            header: "条件字段",
            dataIndex: "tj_checked",
            align: 'center',
            sortable: true,
            renderer: function (value, p, r) {
                var id = r.get("id");
                if (value == "1") {
                    tjArray[id] = {success: true, field: r.get("Name")};
                } else {
                    tjArray[id] = {success: false, field: r.get("Name")};
                }
                return '<input  type="checkbox"' + (value == "1" ? "checked='checked'" : "") + 'onclick="updateTj_checked(' + value + ',this.checked)"' + '/>';
            }
        }
    ]);
    colM.defaultSortable = true;
    var grid = new Ext.grid.EditorGridPanel({
        id: 'external_update_db_source_tables.grid.info',
        plain: true,
        animCollapse: true,
        autoScroll: true,
        height: 310,
        loadMask: {msg: '正在加载数据，请稍后...'},
        border: false,
        collapsible: false,
        cm: colM,
        //sm: boxM,
        store: store,
        stripeRows: true,
        autoExpandColumn: 1,
        clicksToEdit: 1,
        disableSelection: true,
        bodyStyle: 'width:100%',
        selModel: new Ext.grid.RowSelectionModel({singleSelect: true}),
        viewConfig: {
            forceFit: true,
            enableRowBody: true,
            getRowClass: function (record, rowIndex, p, store) {
                return 'x-grid3-row-collapsed';
            }
        }
    });
    var win = new Ext.Window({
        title: "接口修改信息",
        width: 650,
        height: 390,
        layout: 'fit',
        modal: true,
        items: [{
            frame: true,
            autoScroll: true,
            items: [{
                layout: 'fit',
                items: formPanel
            }, grid]
        }],
        bbar: [
            '->',
            {
                id: 'insert_win.info',
                text: '保存',
                handler: function () {
                    alert(zxArray.length);
                    if (formPanel.form.isValid()) {
                        formPanel.getForm().submit({
                            url: '../../InterfaceAction_insert.action',
                            method: 'POST',
                            params: {
                                zxArray: Ext.util.JSON.encode(zxArray),
                                tjArray: Ext.util.JSON.encode(tjArray)
                            },
                            waitTitle: '系统提示',
                            waitMsg: '正在保存,请稍后...',
                            success: function (form, action) {
                                var msg = action.result.msg;
                                Ext.MessageBox.show({
                                    title: '信息',
                                    width: 250,
                                    msg: msg,
                                    animEl: 'insert_win.info',
                                    buttons: {'ok': '确定', 'no': '取消'},
                                    icon: Ext.MessageBox.INFO,
                                    closable: false,
                                    fn: function (e) {
                                        if (e == 'ok') {
                                            grid.render();
                                            store.reload();
                                            win.close();
                                        }
                                    }
                                });
                            }
                        });
                    } else {
                        Ext.MessageBox.show({
                            title: '信息',
                            width: 200,
                            msg: '请填写完成再提交!',
                            animEl: 'insert_win.info',
                            buttons: {'ok': '确定'},
                            icon: Ext.MessageBox.ERROR,
                            closable: false
                        });
                    }
                }
            }, {
                text: '关闭',
                handler: function () {
                    win.close();
                }
            }
        ]
    }).show();

    Model.updateZx_checked = function updateZx_checked(value, checked) {
        if (checked) {
            var grid_panel = Ext.getCmp("external_update_db_source_tables.grid.info");
            var r = grid_panel.getSelectionModel().getSelected();
            var id = r.get("id")
            zxArray[id] = {success: true, field: r.get("Name")};
        } else {
            var grid_panel = Ext.getCmp("external_update_db_source_tables.grid.info");
            var recode = grid_panel.getSelectionModel().getSelected();
            var id = recode.get("id");
            zxArray[id] = {success: false, field: r.get("Name")};
        }
    }

    Model.updateTj_checked = function updateTj_checked(value, checked) {
        if (checked) {
            var grid_panel = Ext.getCmp("external_update_db_source_tables.grid.info");
            var r = grid_panel.getSelectionModel().getSelected();
            var id = r.get("id");
            tjArray[id] = {success: true, field: r.get("Name")};
        } else {
            var grid_panel = Ext.getCmp("external_update_db_source_tables.grid.info");
            var recode = grid_panel.getSelectionModel().getSelected();
            var id = recode.get("id");
            tjArray[id] = {success: false, field: r.get("Name")};
        }
    }
}
*/

var Model = new Object;
function updateTj_checked(value, checked) {
    Model.updateTj_checked(value, checked);
}
function updateZx_checked(value, checked) {
    Model.updateZx_checked(value, checked);
}

function deleteInterface(interfaceId) {
    var grid_panel = Ext.getCmp("grid.info");
    Ext.Msg.confirm("提示", "确定删除服务接口？", function (sid) {
        if (sid == "yes") {
            Ext.Ajax.request({
                url: "../../InterfaceAction_del.action",
                timeout: 20 * 60 * 1000,
                method: "POST",
                params: {interfaceId: interfaceId},
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

function test(interfaceId) {
    var grid_panel = Ext.getCmp("grid.info");
    var recode = grid_panel.getSelectionModel().getSelected();
    var serviceId = recode.get("interfaceNumber");
    window.location.href = "./test.jsp?serviceId="+escape(serviceId);
    /*var grid_panel = Ext.getCmp("grid.info");
    var recode = grid_panel.getSelectionModel().getSelected();
    var formPanel = new Ext.form.FormPanel({
        autoScroll: true,
        layout: 'column',
        border: false,
        height: 100,
        plain: true,
        items: [{
            plain: true,
            columnWidth: .5,
            border: false,
            layout: 'form',
            items: [{
                plain: true,
                labelAlign: 'right',
                labelWidth: 80,
                defaultType: 'textfield',
                border: false,
                layout: 'form',
                defaults: {
                    width: 200,
                    allowBlank: false,
                    blankText: '该项不能为空！'
                },
                items: [/!*{
                    fieldLabel: "接口编码",
                    name: 'interfaceNumber',
                    value: recode.get("interfaceNumber"),
                    id: 'test.interfaceNumer',
                    regex: /^.{2,30}$/,
                    regexText: '请输入任意2--30个字符',
                    emptyText: '请输入任意2--30个字符'

                }*!/
                    {
                        fieldLabel: "身份证号码",
                        xtype: 'textfield',
                        name: 'gmsfhm',
                        id: 'test.gmsfhm',
                        allowBlank: false,
                        regex: /^.{0,18}$/,
                        regexText: '请输入任意1--18个字符'
                    }/!*, {
                    fieldLabel: "条件参数",
                    xtype: 'combo',
                    emptyText: '--请选择--',
                    editable: false,
                    typeAhead: true,
                    forceSelection: true,
                    id: 'test.qy',
                    triggerAction: 'all',
                    mode: 'remote',// 指定数据加载方式，如果直接从客户端加载则为local，如果从服务器断加载// 则为remote.默认值为：remote
                    displayField: "key",
                    valueField: "value",
                    store: table_store,
                    listeners: {
                        render: function () {
                            table_store.proxy = new Ext.data.HttpProxy({
                                url: '../../InterfaceAction_findQyStore.action?serviceId=' + recode.get("interfaceNumber"),
                                method: "POST"
                            })
                        }
                    }
                }, {
                    fieldLabel: "查询条件",
                    xtype: 'textarea',
                    name: 'queryStr',
                    id: 'test.queryStr',
                    value: '1=1',
                    allowBlank: true,
                    regex: /^.{0,3000}$/,
                    regexText: '请输入任意1--3000个字符'
                }*!/]
            }]
        }, {
            plain: true,
            defaultType: 'textfield',
            columnWidth: .5,
            labelAlign: 'right',
            labelWidth: 80,
            border: false,
            layout: 'form',
            defaults: {
                width: 200,
                allowBlank: false,
                blankText: '该项不能为空！'
            },
            items: [/!*{
                fieldLabel: "身份证号码",
                xtype: 'textfield',
                name: 'gmsfhm',
                id: 'test.gmsfhm',
                allowBlank: false,
                regex: /^.{0,18}$/,
                regexText: '请输入任意1--18个字符'
            },*!//!*{
                fieldLabel: "警号编号",
                xtype: 'textfield',
                name: 'policeId',
                id: 'test.policeId',
                allowBlank: false,
                regex: /^.{0,300}$/,
                regexText: '请输入任意1--300个字符'
            }, {
                fieldLabel: "条件值",
                xtype: 'textfield',
                id: 'test.value',
                allowBlank: false,
                regex: /^.{0,300}$/,
                regexText: '请输入任意1--300个字符'
            }, {
                xtype: 'button',
                text: '加入条件',
                width: 80,
                iconCls: 'add',
                //点击事件
                handler: function () {
                    var qy = Ext.getCmp("test.qy").getValue();
                    var v = Ext.getCmp("test.value").getValue();
                    var queryCmp = Ext.getCmp("test.queryStr");
                    if (v == null || qy == null) {
                        Ext.Msg.alert("提示", "请选择条件参数并设置条件值！");
                    } else {
                        var queryStr = queryCmp.getValue();
                        queryStr += " and " + qy + " = '" + v + "'";
                        queryCmp.setValue(queryStr);
                    }
                }
            },*!/ {
                xtype: 'button',
                text: '查询',
                width: 80,
                iconCls: 'query',
                //点击事件
                handler: function () {
                    var serviceId = recode.get("interfaceNumber");
                    var gmsfhm = Ext.getCmp("test.gmsfhm").getValue();
                    //var queryStr = Ext.getCmp("test.queryStr").getValue();
                    //var policeId = Ext.getCmp("test.policeId").getValue();
                    Ext.Ajax.request({
                        url: "../../InterfaceAction_findGrid.action",
                        timeout: 20 * 60 * 1000,
                        method: "POST",
                        params: {
                            serviceId: serviceId,
                             gmsfhm: gmsfhm/!*,
                            queryStr: queryStr,
                            policeId: policeId*!/
                        },
                        success: function (r, o) {
                            var start = 0;
                            var pageSize = 15;
                            var respText = Ext.util.JSON.decode(r.responseText);
                            if (Ext.getCmp("GridPanel") != undefined) {
                                Ext.getCmp("GridPanel").destroy();
                            }
                            if (Ext.getCmp("Store") != undefined) {
                                Ext.getCmp("Store").remove();
                            }
                            var store = new Ext.data.JsonStore({
                                id: "Store",
                                data: respText.data,
                                fields: respText.fieldsNames
                            });

                            var page_toolbar = new Ext.PagingToolbar({
                                pageSize: pageSize,
                                store: store,
                                displayInfo: true,
                                displayMsg: "显示第{0}条记录到第{1}条记录，一共{2}条",
                                emptyMsg: "没有记录",
                                beforePageText: "当前页",
                                afterPageText: "共{0}页"
                            });

                            var cm = new Ext.grid.ColumnModel(respText.columModles);
                            var grid = new Ext.grid.GridPanel({
                                id: "GridPanel",
                                //title:"查询结果",
                                height: 240,
                                width: 980,
                                //anchor:"95%",
                                region: 'center',
                                autoScroll: true,
                                split: true,
                                selModel:new Ext.grid.RowSelectionModel({singleSelect:true}),
                                border: false,
                                viewConfig: {
                                    forceFit: true,
                                    enableRowBody: true,
                                    getRowClass: function (record, rowIndex, p, store) {
                                        return 'x-grid3-row-collapsed';
                                    }
                                },
                                cm: cm,
                                ds: store,
                                bbar: page_toolbar,
                                listeners:{

                                    //单击
                                  /!*  rowclick : function(grid, rowIndex, e){
                                                  alert("rowclick")
                                               },*!/
                                    //双击
                                     rowdblclick:function(grid, rowIndex, e){
                                                   //alert("rowdblclick");
                                                   var recode =  grid.getSelectionModel().getSelected();
                                                   var columModles  = respText.columModles;

                                                     var formPanel = new Ext.form.FormPanel({
                                                         frame: true,
                                                         autoScroll: true,
                                                         baseCls: 'x-plain',
                                                         labelWidth: 150,
                                                         labelAlign: 'right',
                                                         defaultWidth: 280,
                                                         width: 500,
                                                         layout: 'form',
                                                         border: false,
                                                         defaults: {
                                                             width: 250
                                                         }
                                                     });
                                                           // json数组的最外层对象
                                                       Ext.each(columModles,function(items) {
                                                           Ext.each(items,function(item) {
                                                               //alert(item.header + "" + item.dataIndex);
                                                               var lab = item.header;
                                                               var data = item.dataIndex;
                                                               var field = null;
                                                               if (data == "URL" || data == "ZP") {
                                                                   field = new Ext.BoxComponent({
                                                                       //width:125,
                                                                       fieldLabel:lab,
                                                                       xtype:'box',
                                                                       //height:125,
                                                                       autoEl:{
                                                                           tag:'img',
                                                                           src:recode.get(data)
                                                                       }
                                                                   });
                                                               }
                                                               else{
                                                                    field = new Ext.form.DisplayField({
                                                                        fieldLabel: lab,
                                                                        value: recode.get(data)
                                                                    });
                                                                }
                                                               if (field != null) {
                                                                formPanel.add(field);
                                                                formPanel.doLayout();
                                                                }
                                                           });
                                                       });

                                                   var select_Win = new Ext.Window({
                                                       title: "详细",
                                                       width: 650,
                                                       layout: 'fit',
                                                       height: 450,
                                                       modal: true,
                                                       items: formPanel
                                                   });
                                                   select_Win.show();
                                               }
                                }
                            });
                            grid.render("grid");
                        },
                        failure: function (r, o) {
                            var respText = Ext.util.JSON.decode(r.responseText);
                            var msg = respText.msg;
                            Ext.Msg.alert("提示", msg);
                        }
                    });
                }
            }]
        }]
    });

    var win = new Ext.Window({
        title: "信息查询",
        width: 1000,
        height: 400,
        anchor:"95%",
        layout: 'fit',
        modal: true,
        items: [{
            frame: true,
            autoScroll: true,
            items: [{
                layout: 'fit',
                items: formPanel
            }, {
                html: '<div id = "grid"></div>'
            }]
        }]
    }).show();*/
}

function test_all() {
    var formPanel = new Ext.form.FormPanel({
        autoScroll: true,
        layout: 'column',
        border: false,
        height: 100,
        plain: true,
        items: [{
            plain: true,
            columnWidth: .5,
            border: false,
            layout: 'form',
            items: [{
                plain: true,
                labelAlign: 'right',
                labelWidth: 80,
                defaultType: 'textfield',
                border: false,
                layout: 'form',
                defaults: {
                    width: 200,
                    allowBlank: false,
                    blankText: '该项不能为空！'
                },
                items: [
                    {
                        fieldLabel: "条件参数",
                        xtype: 'combo',
                        emptyText: '--请选择--',
                        editable: false,
                        typeAhead: true,
                        forceSelection: true,
                        id: 'test.all.qy',
                        triggerAction: 'all',
                        mode: 'remote',// 指定数据加载方式，如果直接从客户端加载则为local，如果从服务器断加载// 则为remote.默认值为：remote
                        displayField: "key",
                        valueField: "value",
                        store: table_store,
                        listeners: {
                            render: function () {
                                table_store.proxy = new Ext.data.HttpProxy({
                                    url: '../../InterfaceAction_findQyStoreAll.action',
                                    method: "POST"
                                })
                            }
                        }
                    }/*,{
                    fieldLabel: "查询条件",
                    xtype: 'textarea',
                    name: 'queryStr',
                    id: 'test.queryStr',
                    value: '1=1',
                    allowBlank: true,
                    regex: /^.{0,3000}$/,
                    regexText: '请输入任意1--3000个字符'
                }*/]
            }]
        }, {
            plain: true,
            defaultType: 'textfield',
            columnWidth: .5,
            labelAlign: 'right',
            labelWidth: 80,
            border: false,
            layout: 'form',
            defaults: {
                width: 200,
                allowBlank: false,
                blankText: '该项不能为空！'
            },
            items: [ {
                fieldLabel: "条件值",
                xtype: 'textfield',
                id: 'test.all.value',
                allowBlank: false,
                regex: /^.{0,300}$/,
                regexText: '请输入任意1--300个字符'
            },{
                xtype: 'button',
                text: '查询',
                width: 80,
                iconCls: 'query',
                //点击事件
                handler: function () {
                    var qy = Ext.getCmp("test.all.qy").getValue();
                    var value = Ext.getCmp("test.all.value").getValue();
                    Ext.Ajax.request({
                        url: "../../InterfaceAction_findGridAll.action",
                        timeout: 20 * 60 * 1000,
                        method: "POST",
                        params: {
                            tj: qy,
                            value:value
                        },
                        success: function (r, o) {

                            var respText = Ext.util.JSON.decode(r.responseText);

                            //注销信息
                            var zXxx_start = 0;
                            var zXxx_pageSize = 15;
                            if (Ext.getCmp("GridPanel_zXxx") != undefined) {
                                Ext.getCmp("GridPanel_zXxx").destroy();
                            }
                            if (Ext.getCmp("Store_zXxx") != undefined) {
                                Ext.getCmp("Store_zXxx").remove();
                            }
                            var zXxx_store = new Ext.data.JsonStore({
                                id: "Store_zXxx",
                                data: respText.data_zXxx,
                                fields: respText.fieldsNames_zXxx
                            });
                            var zXxx_page_toolbar = new Ext.PagingToolbar({
                                pageSize: zXxx_pageSize,
                                store: zXxx_store,
                                displayInfo: true,
                                displayMsg: "显示第{0}条记录到第{1}条记录，一共{2}条",
                                emptyMsg: "没有记录",
                                beforePageText: "当前页",
                                afterPageText: "共{0}页"
                            });

                            var zXxx_cm = new Ext.grid.ColumnModel(respText.columModles_zXxx);
                            var zXxx_grid = new Ext.grid.GridPanel({
                                id: "GridPanel_zXxx",
                                title:"人口注销信息",
                                height: 240,
                                width: 800,
                                region: 'center',
                                autoScroll: true,
                                split: true,
                                border: false,
                                viewConfig: {
                                    forceFit: true,
                                    enableRowBody: true,
                                    getRowClass: function (record, rowIndex, p, store) {
                                        return 'x-grid3-row-collapsed';
                                    }
                                },
                                cm: zXxx_cm,
                                ds: zXxx_store,
                                bbar: zXxx_page_toolbar
                            });
                            zXxx_grid.render("grid_zXxx");



                            //人口信息
                            var rKxx_start = 0;
                            var rKxx_pageSize = 15;
                            if (Ext.getCmp("GridPanel_rKxx") != undefined) {
                                Ext.getCmp("GridPanel_rKxx").destroy();
                            }
                            if (Ext.getCmp("Store_rKxx") != undefined) {
                                Ext.getCmp("Store_rKxx").remove();
                            }
                            var rKxx_store = new Ext.data.JsonStore({
                                id: "Store_rKxx",
                                data: respText.data_rKxx,
                                fields: respText.fieldsNames_rkxx
                            });

                            var rKxx_page_toolbar = new Ext.PagingToolbar({
                                pageSize: rKxx_pageSize,
                                store: rKxx_store,
                                displayInfo: true,
                                displayMsg: "显示第{0}条记录到第{1}条记录，一共{2}条",
                                emptyMsg: "没有记录",
                                beforePageText: "当前页",
                                afterPageText: "共{0}页"
                            });

                            var rKxx_cm = new Ext.grid.ColumnModel(respText.columModles_rKxx);
                            var rKxx_grid = new Ext.grid.GridPanel({
                                id: "GridPanel_rKxx",
                                title:"人口基本信息",
                                height: 240,
                                width: 750,
                                region: 'center',
                                autoScroll: true,
                                split: true,
                                border: false,
                                viewConfig: {
                                    forceFit: true,
                                    enableRowBody: true,
                                    getRowClass: function (record, rowIndex, p, store) {
                                        return 'x-grid3-row-collapsed';
                                    }
                                },
                                cm: rKxx_cm,
                                ds: rKxx_store,
                                bbar: rKxx_page_toolbar
                            });
                            rKxx_grid.render("grid_rKxx");

                            //人口照片
                            var rKzp_start = 0;
                            var rKzp_pageSize = 15;
                            if (Ext.getCmp("GridPanel_rKzp") != undefined) {
                                Ext.getCmp("GridPanel_rKzp").destroy();
                            }
                            if (Ext.getCmp("Store_rKzp") != undefined) {
                                Ext.getCmp("Store_rKzp").remove();
                            }
                            var rKzp_store = new Ext.data.JsonStore({
                                id: "Store_rKzp",
                                data: respText.data_rKzp,
                                fields: respText.fieldsNames_rkzp
                            });

                            var rKzp_page_toolbar = new Ext.PagingToolbar({
                                pageSize: rKzp_pageSize,
                                store: rKzp_store,
                                displayInfo: true,
                                displayMsg: "显示第{0}条记录到第{1}条记录，一共{2}条",
                                emptyMsg: "没有记录",
                                beforePageText: "当前页",
                                afterPageText: "共{0}页"
                            });

                            var rKzp_cm = new Ext.grid.ColumnModel(respText.columModles_rKzp);
                            var rKzp_grid = new Ext.grid.GridPanel({
                                id: "GridPanel_rKzp",
                                title:"人口照片信息",
                                height: 240,
                                width: 750,
                                region: 'center',
                                autoScroll: true,
                                split: true,
                                border: false,
                                viewConfig: {
                                    forceFit: true,
                                    enableRowBody: true,
                                    getRowClass: function (record, rowIndex, p, store) {
                                        return 'x-grid3-row-collapsed';
                                    }
                                },
                                cm: rKzp_cm,
                                ds: rKzp_store,
                                bbar: rKzp_page_toolbar
                            });
                            rKzp_grid.render("grid_rKzp");


                            //人口要素
                            var rKys_start = 0;
                            var rKys_pageSize = 15;
                            if (Ext.getCmp("GridPanel_rKys") != undefined) {
                                Ext.getCmp("GridPanel_rKys").destroy();
                            }
                            if (Ext.getCmp("Store_rKys") != undefined) {
                                Ext.getCmp("Store_rKys").remove();
                            }
                            var rKys_store = new Ext.data.JsonStore({
                                id: "Store_rKys",
                                data: respText.data_rKys,
                                fields: respText.fieldsNames_rkys
                            });

                            var rKys_page_toolbar = new Ext.PagingToolbar({
                                pageSize: rKys_pageSize,
                                store: rKys_store,
                                displayInfo: true,
                                displayMsg: "显示第{0}条记录到第{1}条记录，一共{2}条",
                                emptyMsg: "没有记录",
                                beforePageText: "当前页",
                                afterPageText: "共{0}页"
                            });

                            var rKys_cm = new Ext.grid.ColumnModel(respText.columModles_rKys);
                            var rKys_grid = new Ext.grid.GridPanel({
                                id: "GridPanel_rKys",
                                title:"人口要素信息",
                                height: 240,
                                width: 750,
                                region: 'center',
                                autoScroll: true,
                                split: true,
                                border: false,
                                viewConfig: {
                                    forceFit: true,
                                    enableRowBody: true,
                                    getRowClass: function (record, rowIndex, p, store) {
                                        return 'x-grid3-row-collapsed';
                                    }
                                },
                                cm: rKys_cm,
                                ds: rKys_store,
                                bbar: rKys_page_toolbar
                            });
                            rKys_grid.render("grid_rKys");
                        },
                        failure: function (r, o) {
                            var respText = Ext.util.JSON.decode(r.responseText);
                            var msg = respText.msg;
                            Ext.Msg.alert("提示", msg);
                        }
                    });
                }
            }]
        }]
    });

    var win = new Ext.Window({
        title: "联合查询",
        width: 800,
        height: 400,
        layout: 'fit',
        modal: true,
        items: [{
            frame: true,
            autoScroll: true,
            items: [{
                layout: 'fit',
                items: formPanel
            }, {
                html: '<div id = "grid_zXxx"></div>'
            }, {
                html: '<div id = "grid_rKxx"></div>'
            }, {
                html: '<div id = "grid_rKzp"></div>'
            }, {
                html: '<div id = "grid_rKys"></div>'
            }]
        }]
    }).show();
}

/**
 * 授权
 * @param grid
 * @param store
 */
function authInterface(value) {
    //==================================== --  -- =============================================================

    var record = new Ext.data.Record.create([
        {name: 'id', mapping: 'id'},
        {name: 'interfaceId', mapping: 'interfaceId'},
        {name: 'userName', mapping: 'userName'},
        {name: 'name', mapping: 'name'},
        {name: 'depart', mapping: 'depart'},
        {name: 'apply', mapping: 'apply'},
        {name: 'checked', mapping: 'checked'}
    ]);
    var proxy = new Ext.data.HttpProxy({
        url: "../../InterfaceAction_selectAuthUser.action"
    });
    var reader = new Ext.data.JsonReader({
        totalProperty: "total",
        root: "rows"
    }, record);
    var store = new Ext.data.GroupingStore({
        id: "store.grid.user.info",
        proxy: proxy,
        reader: reader
    });
    //==================================== --  -- =============================================================
    var start = 0;
    var pageSize = 10;
//    var SelectMap = new Map();
//    var SelectDesMap = new Map();
    var boxM = new Ext.grid.CheckboxSelectionModel();   //复选框
    //var rowNumber = new Ext.grid.RowNumberer();         //自动 编号
    var colM = new Ext.grid.ColumnModel([
        boxM,
        //rowNumber,
        {header: "警号", dataIndex: "userName", align: 'center', sortable: true, menuDisabled: true},
        {header: "姓名", dataIndex: "name", align: 'center', sortable: true, menuDisabled: true},
        {header: "单位", dataIndex: "depart", align: 'center', sortable: true},
        {header: "操作标记", dataIndex: "id", align: 'center', renderer: show_flag_auth}
    ]);
    colM.defaultSortable = true;
    var grid = new Ext.grid.EditorGridPanel({
        id: 'user.grid.info',
        plain: true,
        animCollapse: true,
        autoScroll: true,
        height: 310,
        loadMask: {msg: '正在加载数据，请稍后...'},
        border: false,
        collapsible: false,
        cm: colM,
        sm: boxM,
        store: store,
        stripeRows: true,
        autoExpandColumn: 1,
        clicksToEdit: 1,
        disableSelection: true,
        bodyStyle: 'width:100%',
//        enableDragDrop: true,
        selModel: new Ext.grid.RowSelectionModel({singleSelect: true}),
        viewConfig: {
            forceFit: true,
            enableRowBody: true,
            getRowClass: function (record, rowIndex, p, store) {
                return 'x-grid3-row-collapsed';
            }
        },
        bbar: new Ext.PagingToolbar({
            pageSize: pageSize,
            store: store,
            displayInfo: true,
            displayMsg: "显示第{0}条记录到第{1}条记录，一共{2}条",
            emptyMsg: "没有记录",
            beforePageText: "当前页",
            afterPageText: "共{0}页"
        })
    });
    //==================================== --  -- =============================================================
    var win = new Ext.Window({
        title: "授权设置",
        width: 600,
        height: 350,
        layout: 'fit',
        modal: true,
        items: [grid],
        bbar: ["->", {
            id: 'auth.save.info',
            text: '授权',
            handler: function () {
                Ext.MessageBox.show({
                    title: '信息',
                    msg: '<font color="green">确定要授权?</font>',
                    animEl: 'auth.save.info',
                    buttons: {'ok': '确定', 'no': '取消'},
                    icon: Ext.MessageBox.WARNING,
                    closable: false,
                    fn: function (e) {
                        if (e == 'ok') {
                            var selModel = grid.getSelectionModel();
                            var count = selModel.getCount();
                            var accountIds = new Array();
                            var userNames = new Array();
                            if (count == 0) {
                                Ext.MessageBox.show({
                                    title: '信息',
                                    msg: '<font color="green">您没有勾选任何记录!</font>',
                                    animEl: 'auth.save.info',
                                    buttons: {'ok': '确定'},
                                    icon: Ext.MessageBox.INFO,
                                    closable: false
                                });
                            } else if (count > 0) {
//                                var record = SelectMap.values();
                                var record = selModel.getSelections();
                                for (var i = 0; i < record.length; i++) {
                                    accountIds[i] = record[i].get('id');
                                    userNames[i] = record[i].get('userName');
                                }
                                var myMask = new Ext.LoadMask(Ext.getBody(), {
                                    msg: '正在授权,请稍后',
                                    removeMask: true
                                });
                                myMask.show();
                                Ext.Ajax.request({
                                    url: '../../InterfaceAction_authInterface.action',
                                    params: {userNames: userNames, interfaceId: value},
                                    method: 'POST',
                                    success: function (r, o) {
                                        var respText = Ext.util.JSON.decode(r.responseText);
                                        var msg = respText.msg;
                                        myMask.hide();
                                        Ext.MessageBox.show({
                                            title: '信息',
                                            msg: msg,
                                            buttons: {'ok': '确定'},
                                            icon: Ext.MessageBox.INFO,
                                            closable: false,
                                            fn: function (e) {
                                                if (e == 'ok') {
                                                    grid.render();
                                                    store.reload();
                                                }
                                            }
                                        });
                                    }
                                });


                            }
                        }
                    }
                });
            }
        }, {
            text: '关闭',
            handler: function () {
                win.close();
            }
        }]
    }).show();
    store.load({
        params: {
            start: start, limit: pageSize, interfaceId: value
        }
    });
    store.addListener('load', function () {
        for (var i = 0; i < store.getCount(); i++) {
            var record = store.getAt(i);
            var isChecked = record.data.checked;
            if (isChecked) {
                boxM.selectRow(store.indexOf(record), true);
            }
        }
    });

}

function show_flag_auth(value, p, r) {
    var accountId = value;
    var userName = r.get('userName');
    var interfaceId = r.get('interfaceId');
    var checked = r.get('checked');
    var apply = r.get('apply');
    if (checked) {
        return '<a href="javascript:;" onclick="unAuth(\'' + accountId + '\',\'' + userName + '\',\'' + interfaceId + '\')">解除授权</a>';
    } else {
        if (apply == 1) {
            return '<a href="javascript:;" onclick="authOnly(\'' + accountId + '\',\'' + userName + '\',\'' + interfaceId + '\')">授权</a>';
        }
        return '<font color="gray">未有授权</font>';
    }
}

/**
 *   解除授权
 * @param accountId
 * @param userName
 * @param interfaceId
 */
function unAuth(accountId, userName, interfaceId) {
    Ext.MessageBox.show({
        title: '信息',
        msg: '<font color="green">确定要解除授权?</font>',
        animEl: 'auth.save.info',
        buttons: {'ok': '确定', 'no': '取消'},
        icon: Ext.MessageBox.WARNING,
        closable: false,
        fn: function (e) {
            if (e == 'ok') {
                var myMask = new Ext.LoadMask(Ext.getBody(), {
                    msg: '正在解除授权,请稍后',
                    removeMask: true
                });
                myMask.show();
                Ext.Ajax.request({
                    url: '../../InterfaceAction_unAuthInterface.action',
                    params: {userName: userName, interfaceId: interfaceId},
                    method: 'POST',
                    success: function (r, o) {
                        var respText = Ext.util.JSON.decode(r.responseText);
                        var msg = respText.msg;
                        myMask.hide();
                        Ext.MessageBox.show({
                            title: '信息',
                            msg: msg,
                            buttons: {'ok': '确定'},
                            icon: Ext.MessageBox.INFO,
                            closable: false,
                            fn: function (e) {
                                if (e == 'ok') {
                                    var grid = Ext.getCmp('user.grid.info');
                                    var store = grid.getStore();
                                    grid.render();
                                    store.reload();
                                }
                            }
                        });
                    }
                });
            }
        }
    });
}

/**
 * 单个授权
 * @param accountId
 * @param userName
 * @param interfaceId
 */
function authOnly(accountId, userName, interfaceId) {
    Ext.MessageBox.show({
        title: '信息',
        msg: '<font color="green">确定要授权?</font>',
        animEl: 'auth.save.info',
        buttons: {'ok': '确定', 'no': '取消'},
        icon: Ext.MessageBox.WARNING,
        closable: false,
        fn: function (e) {
            if (e == 'ok') {
                var myMask = new Ext.LoadMask(Ext.getBody(), {
                    msg: '正在解除授权,请稍后',
                    removeMask: true
                });
                myMask.show();
                var userNames = new Array();
                userNames[0] = userName;
                Ext.Ajax.request({
                    url: '../../InterfaceAction_authInterface.action',
                    params: {userNames: userNames, interfaceId: interfaceId},
                    method: 'POST',
                    success: function (r, o) {
                        var respText = Ext.util.JSON.decode(r.responseText);
                        var msg = respText.msg;
                        myMask.hide();
                        Ext.MessageBox.show({
                            title: '信息',
                            msg: msg,
                            buttons: {'ok': '确定'},
                            icon: Ext.MessageBox.INFO,
                            closable: false,
                            fn: function (e) {
                                if (e == 'ok') {
                                    var grid = Ext.getCmp('user.grid.info');
                                    var store = grid.getStore();
                                    var _grid = Ext.getCmp('grid.info');
                                    var _store = _grid.getStore();
                                    grid.render();
                                    store.reload();
                                    _grid.render();
                                    _store.reload();

                                }
                            }
                        });
                    }
                });
            }
        }
    });
}

/**
 * 申请授权
 * @param interfaceId
 */
function apply(interfaceId) {
    Ext.MessageBox.show({
        title: '信息',
        msg: '<font color="green">确定要申请授权?</font>',
        animEl: 'auth.save.info',
        buttons: {'ok': '确定', 'no': '取消'},
        icon: Ext.MessageBox.WARNING,
        closable: false,
        fn: function (e) {
            if (e == 'ok') {
                var myMask = new Ext.LoadMask(Ext.getBody(), {
                    msg: '正在申请授权,请稍后',
                    removeMask: true
                });
                myMask.show();
                Ext.Ajax.request({
                    url: '../../InterfaceAction_applyInterface.action',
                    params: {interfaceId: interfaceId},
                    method: 'POST',
                    success: function (r, o) {
                        var respText = Ext.util.JSON.decode(r.responseText);
                        var msg = respText.msg;
                        myMask.hide();
                        Ext.MessageBox.show({
                            title: '信息',
                            msg: msg,
                            buttons: {'ok': '确定'},
                            icon: Ext.MessageBox.INFO,
                            closable: false,
                            fn: function (e) {
                                if (e == 'ok') {
                                    var grid = Ext.getCmp('grid.info');
                                    var store = grid.getStore();
                                    grid.render();
                                    store.reload();
                                }
                            }
                        });
                    }
                });
            }
        }
    });
}

/**
 * 修改用户
 */
function showUpdateUser() {
    var recordRoleUpdate = new Ext.data.Record.create([{name: 'value', mapping: 'value'}, {
        name: 'key',
        mapping: 'key'
    }]);
    var readerRoleUpdate = new Ext.data.JsonReader({totalProperty: 'total', root: "rows"}, recordRoleUpdate);
    var storeRoleUpdate = new Ext.data.Store({
        url: "../../RoleAction_readNameKeyValue.action?accountType=2",
        reader: readerRoleUpdate,
        listeners: {
            load: function () {
                var value = Ext.getCmp('role.update.info').getValue();
                Ext.getCmp('role.update.info').setValue(value);
            }
        }
    });
    storeRoleUpdate.load();
    var grid = Ext.getCmp('grid.info');
    var store = grid.getStore();
    var selModel = grid.getSelectionModel();
    var formPanel;
    if (selModel.hasSelection()) {
        var selections = selModel.getSelections();
        Ext.each(selections, function (item) {
            var ipType = item.data.ipType;
            var ipTypeT = ipType == true ? true : false;
            var ipTypeF = ipType == true ? false : true;
            formPanel = new Ext.form.FormPanel({
                frame: true,
                autoScroll: true,
                layout: 'column',
                border: false,
                items: [{
                    plain: true,
                    columnWidth: .5,
                    border: false,
                    layout: 'form',
                    items: [{
                        plain: true,
                        labelAlign: 'right',
                        labelWidth: 80,
                        defaultType: 'textfield',
                        border: false,
                        layout: 'form',
                        defaults: {
                            width: 200,
                            allowBlank: false,
                            blankText: '该项不能为空！'
                        },
                        items: [{
                            xtype: 'hidden',
                            name: 'account.accountType',
                            value: 2
                        }, {
                            name: 'account.id',
                            xtype: 'hidden',
                            value: item.data.id
                        }, {
                            name: 'account.userName',
                            xtype: 'hidden',
                            value: item.data.userName
                        }, {
                            id: 'userName.insert.info',
                            fieldLabel: "用户名",
                            xtype: 'displayfield',
                            value: item.data.userName
                        }, {
                            fieldLabel: "真实姓名",
                            name: 'account.name',
                            value: item.data.name,
                            regex: /^.{2,30}$/,
                            regexText: '请输入任意2--30个字符',
                            emptyText: '请输入任意2--30个字符'
                        }, {
                            fieldLabel: "状态", hiddenName: 'account.status', value: item.data.status,
                            xtype: 'combo',
                            mode: 'local',
                            emptyText: '--请选择--',
                            editable: false,
                            typeAhead: true,
                            forceSelection: true,
                            triggerAction: 'all',
                            displayField: "key", valueField: "value",
                            store: storeStatus
                        }, {
                            id: 'password.info',
                            fieldLabel: "密码",
                            name: 'account.password',
                            inputType: 'password',
                            regex: /^.{8,100}$/,
                            regexText: '密码规则:8~100位!',
                            allowBlank: true,
                            listeners: {
                                blur: function () {
                                    var password = this.getValue();
                                    if (password.length > 0) {
                                        var myMask = new Ext.LoadMask(Ext.getBody(), {
                                            msg: '正在校验,请稍后...',
                                            removeMask: true
                                        });
                                        myMask.show();
                                        Ext.Ajax.request({
                                            url: '../../AccountAction_checkPassword.action',
                                            params: {password: password},
                                            method: 'POST',
                                            success: function (r, o) {
                                                var respText = Ext.util.JSON.decode(r.responseText);
                                                var msg = respText.msg;
                                                myMask.hide();
                                                if (msg != 'true') {
                                                    Ext.MessageBox.show({
                                                        title: '信息',
                                                        width: 250,
                                                        msg: msg,
                                                        buttons: {'ok': '确定'},
                                                        icon: Ext.MessageBox.INFO,
                                                        closable: false,
                                                        fn: function (e) {
                                                            if (e == 'ok') {
                                                                Ext.getCmp('password.info').setValue('');
                                                            }
                                                        }
                                                    });
                                                }
                                            }
                                        });
                                    }
                                }
                            }
                        }, {
                            id: 'password2.info',
                            fieldLabel: "确认密码",
                            inputType: 'password',
                            regex: /^.{8,100}$/,
                            regexText: '密码规则:8~100位!',
                            allowBlank: true,
                            listeners: {
                                blur: function () {
                                    var password = Ext.getCmp('password.info').getValue();
                                    if (password.length > 0) {
                                        var password2 = this.getValue();
                                        if (password != password2 && password2.length > 0) {
                                            Ext.MessageBox.show({
                                                title: '信息',
                                                width: 250,
                                                msg: '<font color="red">"确认密码"和"密码"不一致!</font>',
                                                animEl: 'password2.info',
                                                buttons: {'ok': '确定'},
                                                icon: Ext.MessageBox.INFO,
                                                closable: false,
                                                fn: function (e) {
                                                    if (e == 'ok') {
                                                        Ext.getCmp('password2.info').setValue('');
                                                    }
                                                }
                                            });
                                        }
                                    } else {
                                        Ext.MessageBox.show({
                                            title: '信息',
                                            width: 270,
                                            msg: '<font color="red">请先输入"密码",再输入"确认密码"!</font>',
                                            animEl: 'password2.info',
                                            buttons: {'ok': '确定'},
                                            icon: Ext.MessageBox.INFO,
                                            closable: false,
                                            fn: function (e) {
                                                if (e == 'ok') {
                                                    Ext.getCmp('password2.info').setValue('');
                                                }
                                            }
                                        });
                                    }
                                }
                            }
                        }, {
                            id: 'role.update.info',
                            fieldLabel: "角色", hiddenName: 'role.id', value: item.data.roleId,
                            xtype: 'combo',
                            mode: 'local',
                            emptyText: '--请选择--',
                            editable: false,
                            typeAhead: true,
                            forceSelection: true,
                            triggerAction: 'all',
                            displayField: "key", valueField: "value",
                            store: storeRoleUpdate
                        }, {
                            id: 'ipType.info',
                            fieldLabel: '登陆选择',
                            xtype: 'radiogroup',
                            defaultType: 'radio',
                            layout: 'column',
                            items: [
                                {
                                    columnWidth: .5,
                                    boxLabel: 'IP段',
                                    name: 'account.ipType',
                                    inputValue: 1,
                                    checked: ipTypeT
                                },
                                {
                                    columnWidth: .5,
                                    boxLabel: '指定IP',
                                    name: 'account.ipType',
                                    inputValue: 0,
                                    checked: ipTypeF
                                }
                            ],
                            listeners: {
                                change: function (group, ck) {
                                    if (ck.inputValue == 1) {
                                        Ext.getCmp('ipMac.info').hide();
                                        Ext.getCmp('remoteIp.info').disable();
                                        Ext.getCmp('startIp.info').enable();
                                        Ext.getCmp('endIp.info').enable();
                                        Ext.getCmp('ips.info').show();
                                    } else {
                                        Ext.getCmp('ips.info').hide();
                                        Ext.getCmp('startIp.info').disable();
                                        Ext.getCmp('endIp.info').disable();
                                        Ext.getCmp('remoteIp.info').enable();
                                        Ext.getCmp('ipMac.info').show();
                                    }
                                }
                            }
                        }]
                    }, {
                        id: 'ips.info',
                        plain: true,
                        labelAlign: 'right',
                        labelWidth: 80,
                        defaultType: 'textfield',
                        border: false,
                        layout: 'form',
                        defaults: {
                            width: 200,
                            allowBlank: false,
                            blankText: '该项不能为空！'
                        },
                        items: [{
                            id: 'startIp.info',
                            fieldLabel: "开始IP",
                            name: 'account.startIp', value: item.data.startIp,
                            regex: /^.{2,30}$/,
                            regexText: '请输入任意2--30个字符',
                            emptyText: '请输入任意2--30个字符'
                        }, {
                            id: 'endIp.info',
                            fieldLabel: "结束IP",
                            name: 'account.endIp', value: item.data.endIp,
                            regex: /^.{2,30}$/,
                            regexText: '请输入任意2--30个字符',
                            emptyText: '请输入任意2--30个字符'
                        }]
                    }, {
                        id: 'ipMac.info',
                        hidden: true,
                        plain: true,
                        labelAlign: 'right',
                        labelWidth: 80,
                        defaultType: 'textfield',
                        border: false,
                        layout: 'form',
                        defaults: {
                            width: 200,
                            allowBlank: false,
                            blankText: '该项不能为空！'
                        },
                        items: [{
                            id: 'remoteIp.info',
                            fieldLabel: "登录IP",
                            name: 'account.remoteIp', value: item.data.remoteIp,
                            regex: /^.{2,30}$/,
                            regexText: '请输入任意2--30个字符'
                        }]
                    }, {
                        plain: true,
                        labelAlign: 'right',
                        labelWidth: 80,
                        defaultType: 'textfield',
                        border: false,
                        layout: 'form',
                        defaults: {
                            width: 200,
                            allowBlank: false,
                            blankText: '该项不能为空！'
                        },
                        items: [{
                            fieldLabel: "MAC地址",
                            name: 'account.mac', value: item.data.mac,
                            allowBlank: true,
                            regex: /^((([0-9a-fA-F]{2}\-){5}[0-9a-fA-F]{2})|(([0-9a-fA-F]{2}:){5}[0-9a-fA-F]{2}))$/,
                            regexText: '这个不是mac地址:0a-45-be-e6-00-aa或者0a:45:be:e6:00:aa'
                        }]
                    }]
                }, {
                    plain: true,
                    defaultType: 'textfield',
                    columnWidth: .5,
                    labelAlign: 'right',
                    labelWidth: 80,
                    border: false,
                    layout: 'form',
                    defaults: {
                        width: 200,
                        allowBlank: false,
                        blankText: '该项不能为空！'
                    },
                    items: [{
                        fieldLabel: "性别", hiddenName: 'account.sex', value: item.data.sex,
                        xtype: 'combo',
                        mode: 'local',
                        emptyText: '--请选择--',
                        editable: false,
                        typeAhead: true,
                        forceSelection: true,
                        triggerAction: 'all',
                        displayField: "key", valueField: "value",
                        store: storeSex
                    }, {
                        fieldLabel: "部门",
                        name: 'account.depart', value: item.data.depart,
                        regex: /^.{2,30}$/,
                        regexText: '请输入任意2--30个字符',
                        emptyText: '请输入任意2--30个字符'
                    }, {
                        fieldLabel: "职务",
                        name: 'account.title', value: item.data.title,
                        regex: /^.{2,30}$/,
                        regexText: '请输入任意2--30个字符',
                        emptyText: '请输入任意2--30个字符'
                    }, {
                        fieldLabel: "电话",
                        name: 'account.phone', value: item.data.phone,
                        regex: /^.{2,100}$/,
                        regexText: '请输入(例:0571-88880571)',
                        emptyText: '请输入(例:0571-88880571)'
                    }, {
                        fieldLabel: "Email",
                        name: 'account.email', value: item.data.email,
                        regex: /^\w+[\w.]*@[\w.]+\.\w+$/,
                        regexText: '请输入(例:hello@hzih.com)',
                        emptyText: '请输入(例:hello@hzih.com)'
                    }, {
                        fieldLabel: "开始时间",
                        name: 'account.startHour', value: item.data.startHour,
                        regex: /^[0-9]{1,2}$/,
                        regexText: '请输入任意2--30个字符',
                        emptyText: '请输入任意2--30个字符'
                    }, {
                        fieldLabel: "结束时间",
                        name: 'account.endHour', value: item.data.endHour,
                        regex: /^[0-9]{1,2}$/,
                        regexText: '请输入任意2--30个字符',
                        emptyText: '请输入任意2--30个字符'
                    }, {
                        fieldLabel: "描述",
                        name: 'account.description', value: item.data.description,
                        xtype: 'textarea',
                        allowBlank: true,
                        regex: /^.{0,3000}$/,
                        regexText: '请输入任意1--3000个字符'
                    }]
                }]
            });
        });
    }

    var win = new Ext.Window({
        title: "修改信息",
        width: 650,
        layout: 'fit',
        height: 390,
        modal: true,
        items: formPanel,
        listeners: {
            show: function () {
                var ipType = Ext.getCmp('ipType.info').getValue();
                if (ipType.inputValue == 1) {
                    Ext.getCmp('ipMac.info').hide();
                    Ext.getCmp('remoteIp.info').disable();
                    Ext.getCmp('startIp.info').enable();
                    Ext.getCmp('endIp.info').enable();
                    Ext.getCmp('ips.info').show();
                } else {
                    Ext.getCmp('ips.info').hide();
                    Ext.getCmp('startIp.info').disable();
                    Ext.getCmp('endIp.info').disable();
                    Ext.getCmp('remoteIp.info').enable();
                    Ext.getCmp('ipMac.info').show();
                }
            }
        },
        bbar: [
            '->',
            {
                id: 'update_win.info',
                text: '修改',
                handler: function () {
                    Ext.MessageBox.show({
                        title: '信息',
                        width: 250,
                        msg: '确定要修改?',
                        animEl: 'update_win.info',
                        buttons: {'ok': '继续', 'no': '取消'},
                        icon: Ext.MessageBox.WARNING,
                        closable: false,
                        fn: function (e) {
                            if (e == 'ok') {
                                var password = Ext.getCmp('password.info').getValue();
                                var password2 = Ext.getCmp('password2.info').getValue();
                                if (password != password2) {
                                    Ext.MessageBox.show({
                                        title: '信息',
                                        width: 250,
                                        msg: '<font color="red">"确认密码"和"密码"不一致!</font>',
                                        animEl: 'update_win.info',
                                        buttons: {'ok': '确定'},
                                        icon: Ext.MessageBox.INFO,
                                        closable: false,
                                        fn: function (e) {
                                            if (e == 'ok') {
                                                Ext.getCmp('password2.info').setValue('');
                                            }
                                        }
                                    });
                                } else {
                                    if (formPanel.form.isValid()) {
                                        formPanel.getForm().submit({
                                            url: '../../AccountAction_update.action',
                                            method: 'POST',
                                            waitTitle: '系统提示',
                                            waitMsg: '正在修改,请稍后...',
                                            success: function (form, action) {
                                                var msg = action.result.msg;
                                                Ext.MessageBox.show({
                                                    title: '信息',
                                                    width: 250,
                                                    msg: msg,
                                                    animEl: 'update_win.info',
                                                    buttons: {'ok': '确定', 'no': '取消'},
                                                    icon: Ext.MessageBox.INFO,
                                                    closable: false,
                                                    fn: function (e) {
                                                        if (e == 'ok') {
                                                            grid.render();
                                                            store.reload();
                                                            win.close();
                                                        }
                                                    }
                                                });
                                            }
                                        });
                                    } else {
                                        Ext.MessageBox.show({
                                            title: '信息',
                                            width: 200,
                                            msg: '请填写完成再提交!',
                                            animEl: 'update_win.info',
                                            buttons: {'ok': '确定'},
                                            icon: Ext.MessageBox.ERROR,
                                            closable: false
                                        });
                                    }
                                }
                            }
                        }
                    });

                }
            }, {
                text: '关闭',
                handler: function () {
                    win.close();
                }
            }
        ]
    }).show();
}

/**
 * 解锁用户
 * @param value
 */
function unLockUser(id, userName) {
    Ext.MessageBox.show({
        title: '信息',
        msg: '<font color="green">确定要解锁？</font>',
        animEl: id + '.unlock.info',
        width: 260,
        buttons: {'ok': '确定', 'no': '取消'},
        icon: Ext.MessageBox.INFO,
        closable: false,
        fn: function (e) {
            if (e == 'ok') {
                var myMask = new Ext.LoadMask(Ext.getBody(), {
                    msg: '正在解锁,请稍后...',
                    removeMask: true
                });
                myMask.show();
                Ext.Ajax.request({
                    url: '../../AccountAction_unlock.action',
                    params: {id: id, userName: userName},
                    method: 'POST',
                    success: function (r, o) {
                        myMask.hide();
                        var respText = Ext.util.JSON.decode(r.responseText);
                        var msg = respText.msg;
                        Ext.MessageBox.show({
                            title: '信息',
                            width: 250,
                            msg: msg,
                            animEl: id + '.unlock.info',
                            buttons: {'ok': '确定'},
                            icon: Ext.MessageBox.INFO,
                            closable: false,
                            fn: function (e) {
                                if (e == 'ok') {
                                    var grid = Ext.getCmp('grid.info');
                                    var store = grid.getStore();
                                    grid.render();
                                    store.reload();
                                }
                            }
                        });
                    }
                });
            }
        }
    });
}

/**
 * 删除用户
 */
function deleteUser(value) {
    var grid = Ext.getCmp('grid.info');
    var store = grid.getStore();
    var selModel = grid.getSelectionModel();
    var formPanel;
    if (selModel.hasSelection()) {
        var selections = selModel.getSelections();
        Ext.each(selections, function (item) {
            Ext.MessageBox.show({
                title: '信息',
                msg: '<font color="green">确定要删除所选记录？</font>',
                animEl: value + '.delete.info',
                width: 260,
                buttons: {'ok': '确定', 'no': '取消'},
                icon: Ext.MessageBox.INFO,
                closable: false,
                fn: function (e) {
                    if (e == 'ok') {
                        var myMask = new Ext.LoadMask(Ext.getBody(), {
                            msg: '正在删除,请稍后...',
                            removeMask: true
                        });
                        myMask.show();
                        Ext.Ajax.request({
                            url: '../../AccountAction_delete.action',             // 删除 连接 到后台
                            params: {id: value, userName: item.data.userName},
                            method: 'POST',
                            success: function (r, o) {
                                myMask.hide();
                                var respText = Ext.util.JSON.decode(r.responseText);
                                var msg = respText.msg;
                                Ext.MessageBox.show({
                                    title: '信息',
                                    width: 250,
                                    msg: msg,
                                    animEl: value + '.delete.info',
                                    buttons: {'ok': '确定'},
                                    icon: Ext.MessageBox.INFO,
                                    closable: false,
                                    fn: function (e) {
                                        if (e == 'ok') {
                                            grid.render();
                                            store.reload();
                                        }
                                    }
                                });
                            }
                        });
                    }
                }
            });
        });
    }
}

function reSizeImage(width,height,objImg){
    var img = new Image();
    img.src = objImg.src;
    var hRatio;
    var wRatio;
    var Ratio=1;
    var w = img.width;
    var h = img.height;
    wRatio = width/w;
    hRatio = height/h;
    if(width==0&&height==0){
        Ratio =1;
    }else if(width ==0){
        if(hRatio<1) Ratio = hRatio;
    }else if(height==0){
        if(whRatio<1) Ratio = wRatio;
    }else if(wRatio<1||hRatio<1){
        Ratio = (wRatio<=hRatio?wRatio:hRatio);
    }

    if(Ratio<1){
        w = w*Ratio;
        h = h*Ratio;
    }
    objImg.height =h;
    objImg.width=w;
}