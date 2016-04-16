/**
 * Created by Administrator on 15-5-29.
 */
Ext.onReady(function(){
    Ext.BLANK_IMAGE_URL = '../../js/ext/resources/images/default/s.gif';
    Ext.QuickTips.init();
    Ext.form.Field.prototype.msgTarget = 'side';
    /********************************************* -- grid_panel start -- *******************************************************************************************************/
    var start = 0;			//分页--开始数
    var pageSize = 1000;		//分页--每页数

    var toolbar = new Ext.Toolbar({
        plain:true,
        width : 800,
        height : 30,
        items : [/*new Ext.Button({
            id:'btnFalg.info',
            text:'标记',
            iconCls:'add',
            handler:function(){
                flag_win(grid_panel,store);     //连接到 标记 面板
            }
        }),{
            xtype : 'tbseparator'
        },*/ '目录 ',{
            xtype : 'tbseparator'
        },{
            id:'dir.tb.info',
            xtype:'textfield',
            emptyText :'输入要查询的目录',
//            inputType: 'file',
            value :'/data/flag',
            width : 400
        },{
            xtype : 'tbseparator'
        }, {
            text : '查询',
            iconCls:'query',
            listeners : {
                click : function() {
                    var dir = Ext.fly("dir.tb.info").dom.value == '输入要查询的目录'
                        ? null
                        : Ext.getCmp('dir.tb.info').getValue();
                    store.setBaseParam('dir', dir);
                    store.load({
                        params : {
                            start : start,
                            limit : pageSize
                        }
                    });
                }
            }
        },{
            xtype : 'tbseparator'
        }, {
            text : '启动接收',
            iconCls:'query',
            listeners : {
                click : function() {
                    var dir = Ext.fly("dir.tb.info").dom.value == '输入要查询的目录'
                        ? null
                        : Ext.getCmp('dir.tb.info').getValue();
                    var myMask = new Ext.LoadMask(Ext.getBody(),{
                        msg:'正在校验,请稍后...',
                        removeMask:true
                    });
                    myMask.show();
                    Ext.Ajax.request({
                        url:'../../FileTypeAction_listen.action',
                        params:{dir:dir},
                        method:'POST',
                        success:function(action){
                            var json = Ext.decode(action.responseText);
                            var msg = json.msg;
                            myMask.hide();
                            Ext.MessageBox.show({
                                title:'信息',
                                msg:json.msg ,
                                prompt:false,
                                width:200,
                                buttons:{'ok':'确定'},
                                icon:Ext.MessageBox.ERROR,
                                closable:false,
                                fn:function(e){
                                    if(e=='ok'){
                                    }
                                }
                            });
                        }
                    });
                }
            }
        },{
            xtype : 'tbseparator'
        }, {
            text : '停止接收',
            iconCls:'query',
            listeners : {
                click : function() {
                    var myMask = new Ext.LoadMask(Ext.getBody(),{
                        msg:'正在校验,请稍后...',
                        removeMask:true
                    });
                    myMask.show();
                    Ext.Ajax.request({
                        url:'../../FileTypeAction_listenStop.action',
                        method:'POST',
                        success:function(action){
                            var json = Ext.decode(action.responseText);
                            var msg = json.msg;
                            myMask.hide();
                            Ext.MessageBox.show({
                                title:'信息',
                                msg:json.msg ,
                                prompt:false,
                                width:200,
                                buttons:{'ok':'确定'},
                                icon:Ext.MessageBox.ERROR,
                                closable:false,
                                fn:function(e){
                                    if(e=='ok'){
                                    }
                                }
                            });
                        }
                    });
                }
            }
        }]
    });
    var record = new Ext.data.Record.create([
        {name:'filePath',			mapping:'filePath'},
        {name:'level',				mapping:'level'},
        {name:'accountLevel',	mapping:'accountLevel'}
    ]);
    var proxy = new Ext.data.HttpProxy({
        url:"../../FileTypeAction_selectClientDown.action"
    });
    var reader = new Ext.data.JsonReader({
        totalProperty:"total",
        root:"rows"
    },record);
    var store = new Ext.data.GroupingStore({
        proxy : proxy,
        reader : reader,
        listeners :{
            'loadexception' : function(records,option,success){
                Ext.Msg.alert("错误","目录错误");
            }
        }
    });
    var boxM = new Ext.grid.CheckboxSelectionModel();   //复选框
    var rowNumber = new Ext.grid.RowNumberer();         //自动 编号
    var colM = new Ext.grid.ColumnModel([
        boxM,
        rowNumber,
        {header:"文件名",			dataIndex:"filePath",	   	align:'left'},
        {header:'文件等级',		dataIndex:'level',		    align:'center',width:50, sortable:true, renderer:level_show},
//        {header:'用户等级',		dataIndex:'accountLevel',	align:'center',renderer:accountLevel_show},
        {header:'操作标记',       dataIndex:'flag',                 align:'center',width:50,renderer:flag_show}

    ]);
    colM.defaultSortable = true;
    var page_toolbar = new Ext.PagingToolbar({
        pageSize : pageSize,
        store:store,
        displayInfo:true,
        displayMsg:"显示第{0}条记录到第{1}条记录，一共{2}条",
        emptyMsg:"没有记录",
        beforePageText:"当前页",
        afterPageText:"共{0}页"
    });
    var grid_panel = new Ext.grid.GridPanel({
        id:'grid.proxy.internal.info',
        animCollapse:true,
        height:setHeight(),
        width:setWidth(),
        loadMask:{msg:'正在加载数据，请稍后...'},
        border:false,
        collapsible:false,
        cm:colM,
//        sm:boxM,
        store:store,
        stripeRows:true,
        autoExpandColumn:2,
        disableSelection:true,
        bodyStyle:'width:100%',
        enableDragDrop: true,
        selModel:new Ext.grid.RowSelectionModel({singleSelect:true}),
        viewConfig:{
            forceFit:true,
            enableRowBody:true,
            getRowClass:function(record,rowIndex,p,store){
                return 'x-grid3-row-collapsed';
            }
        },
        tbar:[toolbar],
        view:new Ext.grid.GroupingView({
            forceFit:true,
            groupingTextTpl:'{text}({[values.rs.length]}条记录)'
        }),
        bbar:page_toolbar
    });

    /********************************************* -- grid_panel end   -- *******************************************************************************************************/

    var port = new Ext.Viewport({
        layout:'fit',
        renderTo:Ext.getBody(),
        items:[grid_panel]
    });
    var dir = Ext.fly("dir.tb.info").dom.value == '输入要查询的目录'
                        ? null
                        : Ext.getCmp('dir.tb.info').getValue();
    store.load({
        params:{
            start:start,limit:pageSize,dir:dir
        }
    });
});

//============================================ -- javascript function -- =============================================================================
function setHeight(){
    var h = document.body.clientHeight-8;
    return h;
}
function setWidth(){
    return document.body.clientWidth-8;
}

function level_show(value,p,r){
    var level = r.get('level');
    if(level == 0){
       return '公开信息';
    } else if(level == 1){
        return "第一级";
    } else if(level == 2){
        return "第二级";
    } else if(level == 3){
        return "第三级";
    } else if(level == 4){
        return "第四级";
    } else if(level == 5){
        return "第五级";
    } else if(level == 6){
        return "第六级";
    }
}

function accountLevel_show(value,p,r){
    var level = r.get('accountLevel');
    if(level == 0){
       return '公开信息';
    } else if(level == 1){
        return "第一级";
    } else if(level == 2){
        return "第二级";
    } else if(level == 3){
        return "第三级";
    } else if(level == 4){
        return "第四级";
    } else if(level == 5){
        return "第五级";
    } else if(level == 6){
        return "第六级";
    }
}

function flag_show(value,p,r){
    var flag = r.get('flag');
    if(flag == 0){
        return "<a href='javascript:;' style='color: gray;'>下载</a>";
    } else {
        var filePath = r.get('filePath');
        var level = r.get('level');
        var accountLevel = r.get('accountLevel');
        return "<a href='javascript:;' onclick='download_file(\""+filePath+"\",\""+level+"\",\""+accountLevel+"\");' style='color: green;'>下载</a>";
//        return "<a href='javascript:;' onclick='download();' style='color: green;'>下载</a>";
    }
}

function download_file(filePath,level,accountLevel) {
    if (!Ext.fly('test')) {
        var frm = document.createElement('form');
        frm.id = 'test';
        frm.name = id;
        frm.style.display = 'none';
        document.body.appendChild(frm);
    }
    Ext.Ajax.request({
        url: '../../FileTypeAction_download.action',
        params:{filePath:filePath,level:level,accountLevel:accountLevel},
        form: Ext.fly('test'),
        method: 'POST',
        isUpload: true
    });
}

function flag_win(grid,store){
    var infoLevelRecord = new Ext.data.Record.create([
        {name:'value',mapping:'value'},
        {name:'key',mapping:'key'}
    ]);
    var infoLevelReader = new Ext.data.JsonReader({ totalProperty:'total',root:"rows"},infoLevelRecord);
    var storeInfoLevel = new Ext.data.Store({
        url:'../../ManagerSecurityLevelAction_readLevelKeyValue.action',
        reader:infoLevelReader,
        listeners : {
            load : function(){
                var infoLevel = Ext.getCmp('infoLevel.info').getValue();
                Ext.getCmp('infoLevel.info').setValue(infoLevel);
            }
        }
    });
    storeInfoLevel.load();
    var store = grid.getStore();
    var selModel = grid.getSelectionModel();
    var formPanel;
    if(selModel.hasSelection()){
        var selections = selModel.getSelections();
        Ext.each(selections,function(item){
            var filePath = item.data.filePath;
            var level = item.data.level;
            var accountLevel = item.data.accountLevel;
            if(level>0) {
                Ext.Msg.alert("错误","不能转化!");
            } else{

                formPanel = new Ext.form.FormPanel({
                    labelWidth:150,
                    frame:true,
                    defaults : {
                        width : 200,
                        allowBlank:false,
                        blankText:'该项不能为空'
                    },
                    fileUpload :true,
                    labelAlign:'right',
//                    reader:
//                        new Ext.data.JsonReader(
//                            {},
//                            [{name:"id"},{name:"isFilter"},{name:"isVirusScan"},{name:"infoLevel"}]
//                        ),
                    items : [{
                        width:370,
                        title:'安全属性 -- 使用说明',
                        xtype:'fieldset',
                        html:"<font color='green'>1.所有项都为必填项；"
                    },{
                        xtype:'hidden',name:'filePath',value:filePath
                    },{
                        xtype:'hidden',name:'accountLevel',value:accountLevel
                    },{
                        fieldLabel:"启用内容过滤",
                        layout:'column',
                        defaultType: 'radio',
                        items: [
                            { columnWidth:.5, boxLabel: '是', name: 'isFilter', inputValue: true },
                            { columnWidth:.5, boxLabel: '否', name: 'isFilter', inputValue: false,  checked: true }
                        ]
                    },{
                        fieldLabel:"启用病毒扫描",
                        layout:'column',
                        defaultType: 'radio',
                        items: [
                            { columnWidth:.5, boxLabel: '是', name: 'isVirusScan', inputValue: true },
                            { columnWidth:.5, boxLabel: '否', name: 'isVirusScan', inputValue: false,  checked: true }
                        ]
                    },{
                        id:'infoLevel.info',
                        fieldLabel:'保密等级',
                        xtype:'combo',
                        width:140,
                        hiddenName:'infoLevel',value:'0',
                        mode:'local',
                        emptyText :'--请选择--',
                        editable : false,
                        typeAhead:true,
                        forceSelection: true,
                        triggerAction:'all',
                        displayField:"key",valueField:"value",
                        store: storeInfoLevel
                    },{
                        fieldLabel:"处理后是否删除",
                        layout:'column',
                        defaultType: 'radio',
                        items: [
                            { columnWidth:.5, boxLabel: '是', name: 'isDelete', inputValue: true },
                            { columnWidth:.5, boxLabel: '否', name: 'isDelete', inputValue: false,  checked: true }
                        ]
                    }]
                });
//                if(formPanel){
//                    formPanel.getForm().load({
//                        url:"../../AccountAction_selectAccountFlag.action",
//                        params :{id:item.data.id,userName:item.data.userName},
//                        success:function(c,d){},
//                        failure:function(c,d){
//                            Ext.Msg.alert("错误","加载数据出错！")
//                        }
//                    })
//                }
            }
        });
    }
    var win = new Ext.Window({
        title:"用户标记",
        width:400,
        height:300,
        layout:'fit',
        modal:true,
        items: [formPanel],
        bbar:[
            new Ext.Toolbar.Fill(),
            new Ext.Button ({
                id:'external.SafeS.update.info',
                text : '设置',
                formBind:true,
                allowDepress : false,
                handler : function() {
                    if (formPanel.form.isValid()) {
                        Ext.MessageBox.show({
                            title:'信息',
                            width:250,
                            msg:'确定要重新设置?',
                            animEl:'external.SafeS.update.info',
                            buttons:{'ok':'确定','no':'取消'},
                            icon:Ext.MessageBox.WARNING,
                            closable:false,
                            fn:function(e){
                                if(e=='ok'){
                                    formPanel.getForm().submit({
                                        url :'../../FileTypeAction_updateClient.action',
                                        method :'POST',
                                        waitTitle :'系统提示',
                                        waitMsg :'正在设置,请稍后...',
                                        success : function(form,action) {
                                            Ext.MessageBox.show({
                                                title:'信息',
                                                width:250,
                                                msg:action.result.msg,
                                                animEl:'external.SafeS.update.info',
                                                buttons:{'ok':'确定','no':'取消'},
                                                icon:Ext.MessageBox.INFO,
                                                closable:false,
                                                fn:function(e){
                                                    if(e=='ok'){
                                                        win.close();
                                                    }
                                                }
                                            });
                                        }
                                    });
                                }
                            }
                        });
                    } else {
                        Ext.MessageBox.show({
                            title:'信息',
                            width:250,
                            msg:'请填写完成再提交!',
                            animEl:'external.SafeS.update.info',
                            buttons:{'ok':'确定'},
                            icon:Ext.MessageBox.ERROR,
                            closable:false
                        });
                    }
                }
            }),
            new Ext.Button ({
                allowDepress : false,
                handler : function() {win.close();},
                text : '关闭'
            })
        ]
    }).show();
}