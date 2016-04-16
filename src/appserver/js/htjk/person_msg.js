/**
 * Created by IntelliJ IDEA.
 * User: 钱晓盼
 * Date: 12-6-19
 * Time: 上午10:19
 * To change this template use File | Settings | File Templates.
 * 用户日志审计(用户操作审计表)
 */
Ext.onReady(function() {

	Ext.BLANK_IMAGE_URL = '../../js/ext/resources/images/default/s.gif';

    var start = 0;
    var pageSize = 15;

    var record = new Ext.data.Record.create([
        {name:'id',			mapping:'id'},
        {name:'name',		mapping:'name'},
        {name:'info',		    mapping:'info'},
        {name:'status',	mapping:'status'}
    ]);
    var proxy = new Ext.data.HttpProxy({
        url:'../../HtjkAction_findPerson.action'
    });
    var reader = new Ext.data.JsonReader({
        totalProperty:"total",
        root:"rows",
        id:'id'
    },record);
    var store = new Ext.data.GroupingStore({
        id:"store.info",
        proxy : proxy,
        reader : reader
    });
    var boxM = new Ext.grid.CheckboxSelectionModel({
        handleMouseDown:Ext.emtyFn
    });   //复选框
    var rowNumber = new Ext.grid.RowNumberer();         //自动 编号
    var colM = new Ext.grid.ColumnModel([
//        boxM,
        rowNumber,
        {header:'名称',		dataIndex:'name',		   align:'center',sortable:true,width:150},
        {header:"描述信息",		dataIndex:"info",	       align:'center',sortable:true,width:80},
        {header:'状态',	    dataIndex:'status',	   align:'center',sortable:true,width:80,renderer:show_flag}
    ]);
    /*for(var i=6;i<14;i++){
        colM.setHidden(i,!colM.isHidden(i));                // 加载后 不显示 该项
    }
    colM.defaultSortable = true;*/
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
        height:setHeight(),
        width:setWidth(),
        animCollapse:true,
        loadMask:{msg:'正在加载数据，请稍后...'},
        border:false,
        collapsible:false,
        cm:colM,
//        sm:boxM,
        store:store,
        stripeRows:true,
        autoExpandColumn:'Position',
        disableSelection:true,
        bodyStyle:'width:100%',
//        enableDragDrop: true,
        selModel:new Ext.grid.RowSelectionModel({singleSelect:true}),
        viewConfig:{
            forceFit:true,
            enableRowBody:true,
            getRowClass:function(record,rowIndex,p,store){
                return 'x-grid3-row-collapsed';
            }
        },
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
            start:start,limit:pageSize,accountType:2
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

function show_flag(value){
    if(value=="0"){
        return "<a href='javascript:;' onclick='openService()' style='color: green;'>开启</a>";
    } else {
        return "<a href='javascript:;' onclick='closeService()' style='color: green;'>停止</a>";
    }
}

function openService(){
   var grid =  Ext.getCmp("grid.info");
    Ext.MessageBox.show({
        title: '信息',
        width: 230,
        msg: '确定要开启服务吗?',
        buttons: {'ok': '确定', 'no': '取消'},
        icon: Ext.MessageBox.WARNING,
        closable: false,
        fn: function (e) {
            Ext.Ajax.request({
                url: '../../HtjkAction_modifyPersonOpen.action',
                success: function (r, o) {
                    var respText = Ext.util.JSON.decode(r.responseText);
                    var msg = respText.msg;
                    Ext.MessageBox.show({
                        title: '信息',
                        width: 300,
                        msg: msg,
                        animEl: 'btnSave.email',
                        buttons: {'ok': '确定'},
                        icon: Ext.MessageBox.INFO,
                        closable: false,
                        fn: function (e) {
                            if (e == 'ok') {
                                grid.render();
                                grid.getStore().reload();
                            }
                        }
                    });
                }
            });
        }
    });
}

function closeService(){
    var grid =  Ext.getCmp("grid.info");
    Ext.MessageBox.show({
        title: '信息',
        width: 230,
        msg: '确定要关闭服务吗?',
        buttons: {'ok': '确定', 'no': '取消'},
        icon: Ext.MessageBox.WARNING,
        closable: false,
        fn: function (e) {
            Ext.Ajax.request({
                url: '../../HtjkAction_modifyPersonClose.action',
                success: function (r, o) {
                    var respText = Ext.util.JSON.decode(r.responseText);
                    var msg = respText.msg;
                    Ext.MessageBox.show({
                        title: '信息',
                        width: 300,
                        msg: msg,
                        animEl: 'btnSave.email',
                        buttons: {'ok': '确定'},
                        icon: Ext.MessageBox.INFO,
                        closable: false,
                        fn: function (e) {
                            if (e == 'ok') {
                                grid.render();
                                grid.getStore().reload();
                            }
                        }
                    });
                }
            });
        }
    });
}
