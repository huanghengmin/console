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
        })
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
                    },
                    new Ext.form.TextField({
                        fieldLabel: "服务编码",
                        id: 'test.all.fwid',
                        allowBlank: false,
                    }),
                    new Ext.form.TextField({
                        fieldLabel: "身份证号",
                        id: 'test.all.gmsfhm',
                        allowBlank: false,
                    }),
                    new Ext.form.NumberField({
                        fieldLabel: "开始条数",
                        id: 'test.all.start',
                        value:1,
                        minValue: 1
                    })]
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
            },
                new Ext.form.TextField({
                    fieldLabel: "用户名",
                    id: 'test.all.yhm',
                    allowBlank: false,
                }),
                new Ext.form.TextField({
                    fieldLabel: "密码",
                    inputType:'password',
                    id: 'test.all.mm',
                    allowBlank: false,
                }),
                new Ext.form.NumberField({
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
                    var fwid = Ext.getCmp("test.all.fwid").getValue();
                    var gmsfhm = Ext.getCmp("test.all.gmsfhm").getValue();
                    var yhm = Ext.getCmp("test.all.yhm").getValue();
                    var mm = Ext.getCmp("test.all.mm").getValue();
                    var start = Ext.getCmp("test.all.start").getValue();
                    var end = Ext.getCmp("test.all.end").getValue();
                    Ext.Ajax.request({
                        url: "../../HttpInterfaceAction_find.action",
                        timeout: 20 * 60 * 1000,
                        method: "POST",
                        params: {
                            tj: qy,
                            value:value,
                            fwid:fwid,
                            gmsfhm:gmsfhm,
                            yhm:yhm,
                            mm:mm,
                            start:start,
                            end:end
                        },
                        success: function (r, o) {

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

                            var cm = new Ext.grid.ColumnModel(respText.columModles);
                            var grid = new Ext.grid.GridPanel({
                                id: "GridPanel",
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
                                cm: cm,
                                ds: store
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
                html: '<div id = "grid"></div>'
            }]
        }]
    });

});


function setWidth(){
    return document.body.clientWidth-8;
}