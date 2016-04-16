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
        items : [{
            text : '采集',
            iconCls:'add',
            listeners : {
                click : function() {
                    var myMask = new Ext.LoadMask(Ext.getBody(),{
                        msg:'正在采集,请稍后...',
                        removeMask:true
                    });
                    myMask.show();
                    Ext.Ajax.request({
                        url : '../../FileTypeAction_checkResource.action',    //
                        success : function(r,o){
                            var respText = Ext.util.JSON.decode(r.responseText);
                            var msg = respText.msg;
                            myMask.hide();
                            if(msg=='000'){
                                store.setBaseParam('isRemote', true);
                                store.load({
                                    params : {
                                        start : start,
                                        limit : pageSize
                                    }
                                });
                            } else {
                                Ext.MessageBox.show({
                                    title:'信息',
                                    width:200,
                                    msg:msg,
                                    buttons:{'ok':'确定'},
                                    icon:Ext.MessageBox.WARNING,
                                    closable:false,
                                    fn:function(e){
                                        if(e=='ok'){

                                        }
                                    }
                                });
                            }
                        }
                    });
                }
            }
        },{
            xtype : 'tbseparator'
        },new Ext.Button({
                id:'btnRemove.file.external.info',
                text:'删除',
                iconCls:'remove',
                handler:function(){
                    external_delete_file(grid_panel,store,start,pageSize);

                }
        }),{
            xtype : 'tbseparator'
        }, '文件或目录名 ',{
            xtype : 'tbseparator'
        },{
            id:'dir.tb.info',
            xtype:'textfield',
            emptyText :'输入要查询的名字',
//            inputType: 'file',
            width : 400
        },{
            xtype : 'tbseparator'
        }, {
            text : '查询',
            iconCls:'query',
            listeners : {
                click : function() {
                    var dir = Ext.fly("dir.tb.info").dom.value == '输入要查询的名字'
                        ? null
                        : Ext.getCmp('dir.tb.info').getValue();
                    store.setBaseParam('fileName', dir);
                    store.setBaseParam('isRemote', false);
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
        {name:'filePath',			mapping:'filePath'}
//        {name:'level',				mapping:'level'},
//        {name:'accountLevel',	mapping:'accountLevel'}
    ]);
    var proxy = new Ext.data.HttpProxy({
        url:"../../FileTypeAction_selectResource.action"
    });
    var reader = new Ext.data.JsonReader({
        totalProperty:"total",
        root:"rows"
    },record);
    var store = new Ext.data.GroupingStore({
        proxy : proxy,
        reader : reader
    });
    var boxM = new Ext.grid.CheckboxSelectionModel();   //复选框
    var rowNumber = new Ext.grid.RowNumberer();         //自动 编号
    var colM = new Ext.grid.ColumnModel([
        boxM,
        rowNumber,
        {header:"文件名",			dataIndex:"filePath",	   	align:'left'}
//        {header:'文件等级',		dataIndex:'level',		    align:'center',width:50,sortable:true,renderer:level_show}
//        {header:'用户等级',		dataIndex:'accountLevel',	align:'center',renderer:accountLevel_show}

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
    store.load({
        params:{
            start:start,limit:pageSize,isRemote:false
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

function external_delete_file(grid,store,start,pageSize){
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
                        url : '../../FileTypeAction_deleteResource.action',    // 删除 连接 到后台
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
                                        store.setBaseParam('isRemote', false);
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