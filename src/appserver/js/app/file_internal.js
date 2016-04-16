/**
 * Created by Administrator on 15-5-29.
 */
Ext.onReady(function(){
    Ext.BLANK_IMAGE_URL = '../../js/ext/resources/images/default/s.gif';
    Ext.QuickTips.init();
    Ext.form.Field.prototype.msgTarget = 'side';
    /********************************************* -- grid_panel start -- *******************************************************************************************************/
    var pageSize = 1000;		//分页--每页数

    var start = 0;			//分页--开始数
    var toolbar = new Ext.Toolbar({
        plain:true,
        width : 800,
        height : 30,
        items : [
            new Ext.Button({
                id:'btnRemove.file.external.info',
                text:'删除',
                iconCls:'remove',
                handler:function(){
                    internal_delete_file(grid_panel,store,start,pageSize);
                }
            }),{
                xtype : 'tbseparator'
            }, '文件或目录名 ',{
                xtype : 'tbseparator'
            },{
                id:'dir.tb.info',
                xtype:'textfield',
                emptyText :'输入要查询的文件名',
                width : 400
            },{
                xtype : 'tbseparator'
            }, {
                text : '查询',
                iconCls:'query',
                listeners : {
                    click : function() {
                        var dir = Ext.fly("dir.tb.info").dom.value == '输入要查询的文件名'
                            ? null
                            : Ext.getCmp('dir.tb.info').getValue();
                        store.setBaseParam('fileName', dir);
                        store.load({
                            params : {
                                start : start,
                                limit : pageSize
                            }
                        });
                    }
                }
            }]
    });
    var record = new Ext.data.Record.create([
        {name:'filePath',			mapping:'filePath'},
        {name:'permission',		mapping:'permission'}
    ]);
    var proxy = new Ext.data.HttpProxy({
        url:"../../FileTypeAction_selectResourceExternal.action"
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
        {header:'操作标记',       dataIndex:'permission',      align:'center',width:50,renderer:flag_show}

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
        selModel:new Ext.grid.RowSelectionModel({singleSelect:false}),
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

function flag_show(value,p,r){
    var filePath = r.get('filePath');
    var read;
    var rename;
    var del;
    if(value.indexOf('read')>-1){
        read = "<a href='javascript:;' onclick='download_file(\""+filePath+"\");' style='color: green;'>下载</a>"
    } else {
        read = "<a href='javascript:;' style='color: gray;'>下载</a>";
    }

    if(value.indexOf('rename')>-1){
        rename = "<a href='javascript:;' onclick='rename_file(\""+filePath+"\");' style='color: green;'>重命名</a>"
    } else {
        rename = "<a href='javascript:;' style='color: gray;'>重命名</a>";
    }

    if(value.indexOf('delete')>-1){
        del = "<a href='javascript:;' onclick='delete_file(\""+filePath+"\");' style='color: green;'>删除</a>"
    } else {
        del = "<a href='javascript:;' style='color: gray;'>删除</a>";
    }
    return read + '&nbsp;&nbsp;&nbsp;&nbsp;' + rename + '&nbsp;&nbsp;&nbsp;&nbsp;' + del ;



}

function download_file(filePath) {
    if (!Ext.fly('test')) {
        var frm = document.createElement('form');
        frm.id = 'test';
        frm.name = id;
        frm.style.display = 'none';
        document.body.appendChild(frm);
    }
    Ext.Ajax.request({
        url: '../../FileTypeAction_downloadExternal.action',
        params:{filePath:filePath},
        form: Ext.fly('test'),
        method: 'POST',
        isUpload: true
    });
}

function rename_file(fileName){
    var dir = fileName.substring(0,fileName.lastIndexOf('/')+1)
    var f = fileName.substring(fileName.lastIndexOf('/')+1,fileName.length)
    var formPanel = new Ext.form.FormPanel({
        labelWidth:70,
        frame:true,
        defaults : {
            width : 260,
            allowBlank:false,
            blankText:'该项不能为空'
        },
        labelAlign:'right',
        items : [{
            id:'filePath.info',xtype:'hidden',name:'filePath',value:fileName
        },{
            id:'newPath.info',
            fieldLabel:'新文件名',
            xtype:'textfield',
            name:'newPath',value:f
        }]
    });

    var win = new Ext.Window({
        title:"重命名",
        width:400,
        height:100,
        layout:'fit',
        modal:true,
        items: [formPanel],
        bbar:[
            new Ext.Toolbar.Fill(),
            new Ext.Button ({
                id:'external.SafeS.update.info',
                text : '重命名',
                formBind:true,
                allowDepress : false,
                handler : function() {
                    var filePath = Ext.getCmp('filePath.info').getValue();
                    var newPath = dir + Ext.getCmp('newPath.info').getValue();
                    if (formPanel.form.isValid()) {
                        Ext.MessageBox.show({
                            title:'信息',
                            width:200,
                            msg:'确定要重命名?',
                            animEl:'external.SafeS.update.info',
                            buttons:{'ok':'确定','no':'取消'},
                            icon:Ext.MessageBox.QUESTION,
                            closable:false,
                            fn:function(e){
                                if(e=='ok'){
                                    var myMask = new Ext.LoadMask(Ext.getBody(),{
                                        msg:'正在删除,请稍后...',
                                        removeMask:true
                                    });
                                    myMask.show();
                                    Ext.Ajax.request({
                                        url : '../../FileTypeAction_reNameResourceExternal.action',    // 删除 连接 到后台
                                        params :{filePath : filePath, newPath:newPath},
                                        success : function(r,o){
                                            var respText = Ext.util.JSON.decode(r.responseText);
                                            var msg = respText.msg;
                                            myMask.hide();
                                            Ext.MessageBox.show({
                                                title:'信息',
                                                width:200,
                                                msg:msg,
                                                buttons:{'ok':'确定'},
                                                icon:Ext.MessageBox.INFO,
                                                closable:false,
                                                fn:function(e){
                                                    if(e=='ok'){
                                                        var grid = Ext.getCmp('grid.proxy.internal.info');
                                                        var store = grid.getStore();
                                                        grid.render();
                                                        store.load();
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

function delete_file(fileName){
    var fileArray = new Array();
    fileArray[0] = fileName;
    Ext.MessageBox.show({
        title:'信息',
        width:200,
        msg:'确定要删除所选的记录?',
        buttons:{'ok':'确定','no':'取消'},
        icon:Ext.MessageBox.WARNING,
        closable:false,
        fn:function(e){
            if(e=='ok'){
                var myMask = new Ext.LoadMask(Ext.getBody(),{
                    msg:'正在删除,请稍后...',
                    removeMask:true
                });
                myMask.show();
                Ext.Ajax.request({
                    url : '../../FileTypeAction_deleteResourceExternal.action',    // 删除 连接 到后台
                    params :{fileNames : fileArray },
                    success : function(r,o){
                        var respText = Ext.util.JSON.decode(r.responseText);
                        var msg = respText.msg;
                        myMask.hide();
                        Ext.MessageBox.show({
                            title:'信息',
                            width:200,
                            msg:msg,
                            buttons:{'ok':'确定'},
                            icon:Ext.MessageBox.INFO,
                            closable:false,
                            fn:function(e){
                                if(e=='ok'){
                                    var grid = Ext.getCmp('grid.proxy.internal.info');
                                    var store = grid.getStore();
                                    grid.render();
                                    store.load();
                                }
                            }
                        });
                    }
                });
            }
        }
    });
}

function internal_delete_file(grid,store,start,pageSize){
    var selModel = grid.getSelectionModel();
    var count = selModel.getCount();
    if(count==0){
    	Ext.MessageBox.show({
        	title:'信息',
        	width:200,
        	msg:'您没有勾选任何记录!',
        	animEl:'btnRemove.file.external.info',
        	buttons:{'ok':'确定'},
        	icon:Ext.MessageBox.QUESTION,
        	closable:false
		});

    }else if(count > 0){
        var fileArray = new Array();
        var record = grid.getSelectionModel().getSelections();
        for(var i = 0; i < record.length; i++){
            fileArray[i] = record[i].get('filePath');
        }
        Ext.MessageBox.show({
            title:'信息',
            width:200,
            msg:'确定要删除所选的所有记录?',
            animEl:'btnRemove.file.external.info',
            buttons:{'ok':'确定','no':'取消'},
            icon:Ext.MessageBox.WARNING,
            closable:false,
            fn:function(e){
                if(e=='ok'){
                    var myMask = new Ext.LoadMask(Ext.getBody(),{
                        msg:'正在删除,请稍后...',
                        removeMask:true
                    });
                    myMask.show();
                    Ext.Ajax.request({
                        url : '../../FileTypeAction_deleteResourceExternal.action',    // 删除 连接 到后台
                        params :{fileNames : fileArray },
                        success : function(r,o){
                            var respText = Ext.util.JSON.decode(r.responseText);
                            var msg = respText.msg;
                            myMask.hide();
                            Ext.MessageBox.show({
                                title:'信息',
                                width:200,
                                msg:msg,
                                animEl:'btnRemove.file.external.info',
                                buttons:{'ok':'确定'},
                                icon:Ext.MessageBox.INFO,
                                closable:false,
                                fn:function(e){
                                    if(e=='ok'){
                                        store.load({
                                            params : {
                                                start : start,
                                                limit : pageSize
                                            }
                                        });
                                    }
                                }
                            });
                        }
                    });
                }
            }
        });
    }
}