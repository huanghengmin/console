/**
 * 过滤管理
 */
Ext.onReady(function(){
    Ext.BLANK_IMAGE_URL = '../../js/ext/resources/images/default/s.gif';
//    Ext.QuickTips.init();
    Ext.form.Field.prototype.msgTarget = 'side';

    var start = 0;			//分页--开始数
    var pageSize = 30;		//分页--每页数
    var record = new Ext.data.Record.create([
        {name:'id',			mapping:'id'},
        {name:'name',		    mapping:'name'},
        {name:'permission',	mapping:'permission'},
        {name:'autoUnLock',	mapping:'autoUnLock'}
    ]);
    var proxy = new Ext.data.HttpProxy({
        url:"../../RoleAction_selectRoleType.action"
    });
    var reader = new Ext.data.JsonReader({
        totalProperty:"total",
        root:"rows"
    },record);
    var store = new Ext.data.GroupingStore({
        id:"store.info",
        proxy : proxy,
        reader : reader
    });

    var boxM = new Ext.grid.CheckboxSelectionModel();   //复选框
    var rowNumber = new Ext.grid.RowNumberer();         //自动 编号
    var colM = new Ext.grid.ColumnModel([
//        boxM,
        rowNumber,
        { sortable:true, header:"<div style='text-align:center'>角色类型</div>",	dataIndex:"name", align:'center'  },
        { sortable:true, header:"<div style='text-align:center'>权限</div>",	dataIndex:"permission", align:'left',renderer:show_permission },
        { sortable:true, header:"<div style='text-align:center'>自动解锁</div>",	dataIndex:"autoUnLock", align:'center',renderer:show_auto },
        { header:'操作标记',			dataIndex:'id',					align:'center',		renderer: showURL_flag,width:100}

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
        id:'grid.info',
        plain:true,
        hieght:setHeight(),
        width:setWidth(),
        animCollapse:true,
        loadMask:{msg:'正在加载数据，请稍后...'},
        border:false,
        collapsible:false,
        columnLines : true,
        cm:colM,
        sm:boxM,
        store:store,
        stripeRows:true,
        disableSelection:true,
        bodyStyle:'width:100%',
        selModel:new Ext.grid.RowSelectionModel({singleSelect:true}),
        viewConfig:{
            forceFit:true,
            enableRowBody:true,
            getRowClass:function(record,rowIndex,p,store){
                return 'x-grid3-row-collapsed';
            }
        },
        tbar:[
            /*new Ext.Button({
                id:'btnAdd.info',
                text:'新增',
                iconCls:'add',
                handler:function(){
                    insert_win(grid_panel,store);     //连接到 新增 面板
                }
            }),
            {xtype:"tbseparator"},
			new Ext.Button ({
			    id : 'btnRemove.info',
                text : '删除',
				iconCls : 'remove',
				handler : function() {
					delete_row(grid_panel,store);    //删除 表格 的 一 行 或多行
				}
            })*/
        ],
        view:new Ext.grid.GroupingView({
            forceFit:true,
            groupingTextTpl:'{text}({[values.rs.length]}条记录)'
        }),
        bbar:page_toolbar
    });
    var port = new Ext.Viewport({
        layout:'fit',
        renderTo: Ext.getBody(),
        items:[grid_panel]
    });
    store.load({
        params:{
            start:start,limit:pageSize,accountType:1
        }
    });
});
function setHeight(){
	var h = document.body.clientHeight-8;
	return h;
}

function setWidth(){
    return document.body.clientWidth-8;
}
//============================================ -- javascript function -- =============================================================================

function show_auto(value){
    if(value==0||value=='0'){
        return "<font style='color:red'>否</font>";
    }else{
        return "<font style='color:green'>是</font>";
    }

}
function show_permission(value){
    if(value!=undefined && (value != 'null'||value!='NULL') && value.length > 1){
        var p = value.split(',');
        var permission = '目录&nbsp;&nbsp;';
        for(var i=0;i< p.length;i++){
            var per = p[i];
            if(per == 'read'){
                permission += '下载&nbsp;&nbsp;';
            } else if(per == 'rename') {
                permission += '重命名&nbsp;&nbsp;';
            } else if(per == 'delete') {
                permission += '删除&nbsp;&nbsp;';
            }
        }
        return permission;
    }
    return value;
}
function showURL_flag(value){
	return "<a href='javascript:;' onclick='update_win();' style='color: green;'>修改</a>";
}

function update_win(){
    var grid = Ext.getCmp('grid.info');
    var store = Ext.getCmp('grid.info').getStore();
    var selModel = grid.getSelectionModel();
    var formPanel;
    if(selModel.hasSelection()){
        var selections = selModel.getSelections();
        Ext.each(selections,function(item){

            var p = item.data.permission.split(',');
            var isRead = false;
            var isRename = false;
            var isDelete = false;
            for(var i=0;i< p.length;i++){
                var per = p[i];
                if(per == 'read'){
                    isRead = true;
                } else if(per == 'rename') {
                    isRename = true;
                } else if(per == 'delete') {
                    isDelete = true;
                }
            }
            var name;
            if(item.data.id > 4) {
                name = new Ext.form.TextField({fieldLabel:"角色类型名",	name:'roleType.name',	value: item.data.name});
            } else {
                name = new Ext.form.DisplayField({fieldLabel:"角色类型名",value:item.data.name});
            }

            var dir = new Ext.form.Checkbox({
                boxLabel:"目录",id:"dir", checked:true,disabled:true

            });

            var read = new Ext.form.Checkbox({
                boxLabel:"下载",id:"read", checked:isRead,
                name:"roleType.read"
            });
            var rename = new Ext.form.Checkbox({
                boxLabel:"重命名",id:"rename", checked:isRename,
                name:"roleType.rename"
            });
            var del = new Ext.form.Checkbox({
                boxLabel:"删除",id:"delete",checked:isDelete,
                name:"roleType.delete"
            });

            var autoUnLock = new Ext.form.Checkbox({
                boxLabel:"自动解锁",id:"unLock",checked:((item.data.autoUnLock=='0'||item.data.autoUnLock==0)?false:true),
                name:"roleType.unLock"
            });

            formPanel = new Ext.form.FormPanel({
                defaultType:'textfield',
                frame:true,
                labelAlign:'right',
                autoScroll:true,
                labelWidth:80,
                defaults:{
                    width:280,
                    allowBlank:false,
                    blankText:'该项不能为空！'
                },
                items:[
                    {name:'roleType.id',value: item.data.id,xtype:'hidden'},
					name,
                    dir,
                    read,
                    rename,
                    del,
                    autoUnLock
                ]
            });
        });
    }
    var win = new Ext.Window({
        title:"修改信息",
        width:420,
		layout:'fit',
        height:220,
        modal:true,
        items:formPanel,
        bbar:['->', {
        	id:'update.win.info',
        	text:'修改',
        	handler:function(){
        		if (formPanel.form.isValid()) {        			
        			formPanel.getForm().submit({
        				url :'../../RoleAction_updateRoleType.action',
        				method :'POST',
        				waitTitle :'系统提示',
        				waitMsg :'正在修改,请稍后...',
        				success : function(form,action) {
							var msg = action.result.msg;
        					Ext.MessageBox.show({
        						title:'信息',
        						width:260,
        						msg: msg ,
        						animEl:'update.win.info',
        						buttons:{'ok':'确定','no':'取消'},
        						icon:Ext.MessageBox.INFO,
        						closable:false,
        						fn:function(e){
        							if(e=='ok'){
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
        				title:'信息',
        				width:260,
        				msg:'请填写完成再提交!',
        				animEl:'update.win.info',
        				buttons:{'ok':'确定'},
        				icon:Ext.MessageBox.ERROR,
        				closable:false
        			});
        		}
        	}
        },{
        	text:'关闭',
        	handler:function(){
        		win.close();
        	}
        }]
        	
    }).show();

}

function detail_win(){
    var grid = Ext.getCmp('grid.info');
    var selModel = grid.getSelectionModel();
    var formPanel;
    if(selModel.hasSelection()){
        var selections = selModel.getSelections();
        Ext.each(selections,function(item){
            formPanel = new Ext.form.FormPanel({
                defaultType:'displayfield',
                frame:true,
                labelAlign:'right',
                autoScroll:true,
                labelWidth:80,
                defaults:{
                    width:280,
                    allowBlank:false,
                    blankText:'该项不能为空！'
                },
                items:[
                    {fieldLabel:"内容ID", 	value: item.data.id},
                	{fieldLabel:"过滤内容",	value: item.data.filter}
                ]
            });
        });
    }
    var win = new Ext.Window({
        title:"详细信息",
        width:420,
		layout:'fit',
        height:180,
        modal:true,
        items:formPanel,
        bbar:[
        	'->',
        	{
        		text:'保存',
        		handler:function(){
        			win.close();
        		}
        	},
        	{
        		text:'关闭',
        		handler:function(){
        			win.close();
        		}
        	}
        ]
    }).show();
}