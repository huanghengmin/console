/**
 * Created by Administrator on 2015/12/4.
 */

Ext.onReady(function () {
    Ext.BLANK_IMAGE_URL = '../../js/ext/resources/images/default/s.gif';
    Ext.QuickTips.init();
    Ext.form.Field.prototype.msgTarget = 'side';

    function GeRequest(){
        var url = location.search;
        var theRequest = new Object();
        if(url.indexOf("?")!=-1){
            var str = url.substr(1);
            var strs = str.split("&");
            for(var i = 0;i<strs.length;i++){
                theRequest[strs[i].split("=")[0]]=unescape(strs[i].split("=")[1]);
            }
        }
        return theRequest;
    }

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
                    /*{
                     fieldLabel: "接口编码",
                     name: 'interfaceNumber',
                     value: recode.get("interfaceNumber"),
                     id: 'test.interfaceNumer',
                     regex: /^.{2,30}$/,
                     regexText: '请输入任意2--30个字符',
                     emptyText: '请输入任意2--30个字符'

                     }*/
                    {
                        fieldLabel: "身份证号码",
                        xtype: 'textfield',
                        name: 'gmsfhm',
                        id: 'test.gmsfhm',
                        allowBlank: false,
                        regex: /^.{0,18}$/,
                        regexText: '请输入任意1--18个字符'
                    }
                    /*, {
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
            items: [/*{
             fieldLabel: "身份证号码",
             xtype: 'textfield',
             name: 'gmsfhm',
             id: 'test.gmsfhm',
             allowBlank: false,
             regex: /^.{0,18}$/,
             regexText: '请输入任意1--18个字符'
             },*/
                /*{
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
                 },*/ {
                    xtype: 'button',
                    text: '查询',
                    width: 80,
                    iconCls: 'query',
                    //点击事件
                    handler: function () {
                        var myMask = new Ext.LoadMask(Ext.getBody(),{
                            msg : '正在查询,请稍后...',
                            removeMask : true
                        });
                        myMask.show();
                        var request = GeRequest();
                        var serviceId =unescape(request["serviceId"]);
                        var gmsfhm = Ext.getCmp("test.gmsfhm").getValue();
                        //var queryStr = Ext.getCmp("test.queryStr").getValue();
                        //var policeId = Ext.getCmp("test.policeId").getValue();
                        Ext.Ajax.request({
                            url: "../../InterfaceAction_findGrid.action",
                            timeout: 20 * 60 * 1000,
                            method: "POST",
                            params: {
                                serviceId: serviceId,
                                gmsfhm: gmsfhm/*,
                                 queryStr: queryStr,
                                 policeId: policeId*/
                            },
                            success: function (r, o) {
                                myMask.hide();
                                var start = 0;
                                var pageSize = 15;
                                var respText = Ext.util.JSON.decode(r.responseText);
                                if (respText.success == true) {
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
                                        selModel: new Ext.grid.RowSelectionModel({singleSelect: true}),
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
                                        listeners: {
                                            //单击
                                            /*  rowclick : function(grid, rowIndex, e){
                                             alert("rowclick")
                                             },*/
                                            //双击
                                            rowdblclick: function (grid, rowIndex, e) {
                                                //alert("rowdblclick");
                                                var recode = grid.getSelectionModel().getSelected();
                                                var columModles = respText.columModles;

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
                                                Ext.each(columModles, function (items) {
                                                    Ext.each(items, function (item) {
                                                        //alert(item.header + "" + item.dataIndex);
                                                        var lab = item.header;
                                                        var data = item.dataIndex;
                                                        var field = null;
                                                        if (data == "URL" || data == "ZP") {
                                                            field = new Ext.BoxComponent({
                                                                //width:125,
                                                                fieldLabel: lab,
                                                                xtype: 'box',
                                                                //height:125,
                                                                autoEl: {
                                                                    tag: 'img',
                                                                    src: recode.get(data)
                                                                }
                                                            });
                                                        } else if (data == "XB") {
                                                            var value = show_sex(recode.get(data));
                                                            field = new Ext.form.DisplayField({
                                                                fieldLabel: lab,
                                                                value: value
                                                            });
                                                        } else if (data == "MZ") {
                                                            var value = show_mz(recode.get(data));
                                                            field = new Ext.form.DisplayField({
                                                                fieldLabel: lab,
                                                                value: value
                                                            });
                                                        }/* else if (data == "SSXQ") {
                                                            var value = show_ssxq(recode.get(data));
                                                            field = new Ext.form.DisplayField({
                                                                fieldLabel: lab,
                                                                value: value
                                                            });
                                                        }*/ else {
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
                                }else{
                                    Ext.MessageBox.show({
                                        title:'信息',
                                        width:250,
                                        msg:respText.msg,
                                        animEl:'insert_win.info',
                                        buttons:{'no':'退出'},
                                        icon:Ext.MessageBox.INFO,
                                        closable:false
                                    });
                                }
                            },
                            failure: function (r, o) {
                                myMask.hide();
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

function show_sex(value){
    if (value == '1') {
        return '<span>男</span>';
    } else {
        return '<span>女</span>';
    }
}

/*
function show_ssxq(value){
    return '<span>'+value+'</span>';
}
*/

function show_mz(value){
    switch (value){
        case '01':
            return '<span>汉族</span>';
        case   '02':
            return '<span>蒙古族</span>';
        case   '03':
            return '<span>回族</span>';
        case    '04':
            return '<span>藏族</span>';
        case    '05':
            return '<span>维吾尔族</span>';
        case    '06':
            return '<span>苗族</span>';
        case    '07':
            return '<span>彝族</span>';
        case    '08':
            return '<span>壮族</span>';
        case    '09':
            return '<span>布依族</span>';
        case    '10':
            return '<span>朝鲜族</span>';
        case    '11':
            return '<span>满族</span>';
        case   '12':
            return '<span>侗族</span>';
        case     '13':
            return '<span>瑶族</span>';
        case    '14':
            return '<span>白族</span>';
        case    '15':
            return '<span>土家族</span>';
        case   '16':
            return '<span>哈尼族</span>';
        case     '17':
            return '<span>哈萨克族</span>';
        case    '18':
            return '<span>傣族</span>';
        case    '19':
            return '<span>黎族</span>';
        case    '20':
            return '<span>傈僳族</span>';
        case    '21':
            return '<span>佤族</span>';
        case     '22':
            return '<span>畲族</span>';
        case   '23':
            return '<span>高山族</span>';
        case   '24':
            return '<span>拉祜族</span>';
        case   '25':
            return '<span>水族</span>';
        case    '26':
            return '<span>东乡族</span>';
        case    '27':
            return '<span>纳西族</span>';
        case    '28':
            return '<span>景颇族</span>';
        case    '29':
            return '<span>柯尔克孜族</span>';
        case   '30':
            return '<span>土族</span>';
        case    '31':
            return '<span>达斡尔族</span>';
        case    '32':
            return '<span>仫佬族</span>';
        case    '33':
            return '<span>羌族</span>';
        case   '34':
            return '<span>布朗族</span>';
        case   '35':
            return '<span>撒拉族</span>';
        case   '36':
            return '<span>毛南族</span>';
        case   '37':
            return '<span>仡佬族</span>';
        case   '38':
            return '<span>锡伯族</span>';
        case    '39':
            return '<span>阿昌族</span>';
        case    '40':
            return '<span>普米族</span>';
        case    '41':
            return '<span>塔吉克族</span>';
        case    '42':
            return '<span>怒族</span>';
        case    '43':
            return '<span>乌孜别克族</span>';
        case   '44':
            return '<span>俄罗斯族</span>';
        case   '45':
            return '<span>鄂温克族</span>';
        case   '46':
            return '<span>德昂族</span>';
        case   '47':
            return '<span>保安族</span>';
        case   '48':
            return '<span>裕固族</span>';
        case   '49':
            return '<span>京族</span>';
        case    '50':
            return '<span>塔塔尔族</span>';
        case   '51':
            return '<span>独龙族</span>';
        case    '52':
            return '<span>鄂伦春族</span>';
        case   '53':
            return '<span>赫哲族</span>';
        case   '54':
            return '<span>门巴族</span>';
        case   '55':
            return '<span>珞巴族</span>';
        case    '56':
            return '<span>基诺族</span>';
        case    '60':
            return '<span>不详</span>';
        case    '91':
            return '<span>外籍人员</span>';
        case    '92':
            return '<span>无国籍人员</span>';
        case   '97':
            return '<span>其他</span>';
        case  '98':
            return '<span>外国血统、中国籍人士</span>';
    }
}
