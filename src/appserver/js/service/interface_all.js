/**
 * 接口服务
 */
Ext.onReady(function () {
    Ext.BLANK_IMAGE_URL = '../../js/ext/resources/images/default/s.gif';

    Ext.QuickTips.init();
    Ext.form.Field.prototype.msgTarget = 'side';


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
                    },new Ext.form.NumberField({
                        fieldLabel: "开始条数",
                        id: 'test.all.start',
                        value:1,
                        minValue: 1
                    })/*,{
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
            },new Ext.form.NumberField({
                fieldLabel: "结束条数",
                id:'test.all.end',
                value:15,
                minValue: 1
            }),{
                xtype: 'button',
                text: '查询',
                width: 80,
                iconCls: 'query',
                //点击事件
                handler: function () {
                    var qy = Ext.getCmp("test.all.qy").getValue();
                    var value = Ext.getCmp("test.all.value").getValue();
                    var start = Ext.getCmp("test.all.start").getValue();
                    var end = Ext.getCmp("test.all.end").getValue();
                    Ext.Ajax.request({
                        url: "../../InterfaceAction_findGridAll.action",
                        timeout: 20 * 60 * 1000,
                        method: "POST",
                        params: {
                            tj: qy,
                            value:value,
                            start:start,
                            end:end
                        },
                        success: function (r, o) {

                            var respText = Ext.util.JSON.decode(r.responseText);

                            //注销信息
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

                            var zXxx_cm = new Ext.grid.ColumnModel(respText.columModles_zXxx);
                            var zXxx_grid = new Ext.grid.GridPanel({
                                id: "GridPanel_zXxx",
                                title:"人口注销信息",
                                height: 240,
                                width: setWidth(),
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
                                ds: zXxx_store
                            });
                            zXxx_grid.render("grid_zXxx");



                            //人口信息
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

                            var rKxx_cm = new Ext.grid.ColumnModel(respText.columModles_rKxx);
                            var rKxx_grid = new Ext.grid.GridPanel({
                                id: "GridPanel_rKxx",
                                title:"人口基本信息",
                                height: 240,
                                width: setWidth(),
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
                                ds: rKxx_store
                            });
                            rKxx_grid.render("grid_rKxx");

                            //人口照片
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


                            var rKzp_cm = new Ext.grid.ColumnModel(respText.columModles_rKzp);
                            var rKzp_grid = new Ext.grid.GridPanel({
                                id: "GridPanel_rKzp",
                                title:"人口照片信息",
                                height: 240,
                                width: setWidth(),
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
                                ds: rKzp_store
                            });
                            rKzp_grid.render("grid_rKzp");


                            //人口要素
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

                            var rKys_cm = new Ext.grid.ColumnModel(respText.columModles_rKys);
                            var rKys_grid = new Ext.grid.GridPanel({
                                id: "GridPanel_rKys",
                                title:"人口要素信息",
                                height: 240,
                                width: setWidth(),
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
                                ds: rKys_store
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

  /*  var panel = new Ext.Panel({
        plain:true,
        border:false,
        layout: 'fit',
        items:[{
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
    });*/
    new Ext.Viewport({
        layout :'fit',
        renderTo:Ext.getBody(),
        autoScroll:true,
        items:[{
            frame:true,
            autoScroll:true,
            items:[{
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
    });

});


function setWidth(){
    return document.body.clientWidth-8;
}