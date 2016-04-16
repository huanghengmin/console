/**
 * 管理员账号管理
 */
Ext.onReady(function() {

	Ext.BLANK_IMAGE_URL = '../../js/ext/resources/images/default/s.gif';

	Ext.QuickTips.init();
	Ext.form.Field.prototype.msgTarget = 'side';

    var start = 0;
    var pageSize = 15;
    var toolbar = new Ext.Toolbar({
        plain:true,
        width : 380,
        height : 30,
        items : [ '管理员账号名',{
            id:'userName.tb.info',
            xtype:'textfield',
            emptyText :'输入管理员账号名',
            width : 100
        },{
            xtype : 'tbseparator'
        },'状态',{
            id : 'status.tb.info',
            xtype : 'combo',
            store : new Ext.data.ArrayStore({
                autoDestroy : true,
                fields : ['value', 'key'],
                data : [
                    ['', '全部'],
                    ['1', '有效'],
                    ['0', '无效'],
                    ['2', '删除'],
                    ['3', '锁定']
                ]
            }),
            valueField : 'value',
            displayField : 'key',
            mode : 'local',
            forceSelection : true,
            triggerAction : 'all',
            emptyText : '--请选择--',
            value : '',
            selectOnFocus : true,
            width : 100
        }, {
            xtype : 'tbseparator'
        }, {
            text : '查询',
            iconCls:'query',
            listeners : {
                click : function() {
                    var userName = Ext.fly("userName.tb.info").dom.value == '输入管理员账号名'
                        ? null
                        : Ext.getCmp('userName.tb.info').getValue();
                    var status = Ext.fly('status.tb.info').dom.value == '--请选择--'
                        ? null
                        :Ext.getCmp('status.tb.info').getValue();
                    store.setBaseParam('userName', userName);
                    store.setBaseParam('status', status);
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
        {name:'id',			mapping:'id'},
        {name:'name',			mapping:'name'},
        {name:'userName',		mapping:'userName'},
        {name:'sex',		    mapping:'sex'},
        {name:'password',		mapping:'password'},
        {name:'phone',			mapping:'phone'},
        {name:'status',		mapping:'status'},
        {name:'email',			mapping:'email'},
        {name:'title',			mapping:'title'},
        {name:'description',	mapping:'description'},
        {name:'ipType',		mapping:'ipType'},
        {name:'remoteIp',		mapping:'remoteIp'},
        {name:'startIp',		mapping:'startIp'},
        {name:'endIp',			mapping:'endIp'},
        {name:'startHour',	mapping:'startHour'},
        {name:'endHour',		mapping:'endHour'},
        {name:'roleName',		mapping:'roleName'},
        {name:'roleId',		mapping:'roleId'},
        {name:'mac',    		mapping:'mac'},
        {name:'depart',		mapping:'depart'},
        {name:'unLock',		mapping:'unLock'}
    ]);
    var proxy = new Ext.data.HttpProxy({
        url:"../../AccountAction_select.action?accountType=1"
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

//    var boxM = new Ext.grid.CheckboxSelectionModel();   //复选框
    var rowNumber = new Ext.grid.RowNumberer();         //自动 编号
    var colM = new Ext.grid.ColumnModel([
//        boxM,
        rowNumber,
        {header:"管理员账号名",			dataIndex:"userName",  align:'center',sortable:true,menuDisabled:true},
        {header:"真实姓名",		dataIndex:"name",	      align:'center',sortable:true,menuDisabled:true},
        {header:'电话',		    dataIndex:'phone',	  align:'center',sortable:true,menuDisabled:true},
        {header:'状态',		    dataIndex:'status',    align:'center',sortable:true,menuDisabled:true, renderer:show_status},
        {header:'操作标记',		dataIndex:'id',		  align:'center',sortable:true,menuDisabled:true, renderer:show_flag,	width:100}
    ]);
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
        tbar:[new Ext.Button({
            id:'btnAdd.info',
            text:'新增',
            iconCls:'add',
            handler:function(){
                insert_win(grid_panel,store);     //连接到 新增 面板
            }
        }),{
            xtype : 'tbseparator'
        },toolbar],
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
            start:start,limit:pageSize
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

function show_status(value){
    if (value == '0') {
		return '<span style="color:red;">无效</span>';
	} else if(value=='1'){
		return '<span style="color:green;">有效</span>';
	} else if(value=='2'){
		return '<span style="color:gray;">删除</span>';
	} else if(value=='3'){
		return '<span style="color:blue;">锁定</span>';
	}
}

function show_flag(value, p, r){
    var userName = r.get('userName');
    var unLock = r.get('unLock');
    var status = r.get('status');
    var unLockHtml = '';
    if(unLock==1 && status == '3'){
        unLockHtml = String.format('<a id="\''+value+'\'.unlock.info" href="javascript:void(0);" onclick="unLockUser(\''+value+'\',\''+userName+'\');return false;" style="color: green;">解锁</a>');
    } else{
        unLockHtml = String.format('<font color="gray">解锁</font>');
    }

    if( userName=='admin' ||
        userName=='auditadmin' ||
        userName=='authadmin' ||
        userName=='configadmin'){
        return String
            .format(
                '<a href="javascript:void(0);" onclick="showUpdateUser();return false;" style="color: green;">修改</a>'
            + '&nbsp;&nbsp;'
            + '<font color="gray">删除</font>'
            + '&nbsp;&nbsp;'
            + unLockHtml);
    }else{
        return String
            .format('<a href="javascript:void(0);" onclick="showUpdateUser();return false;" style="color: green;">修改</a>'
            + '&nbsp;&nbsp;'
            + '<a id="\''+value+'\'.delete.info" href="javascript:void(0);" onclick="deleteUser(\''+value+'\');return false;" style="color: green;">删除</a>'
            + '&nbsp;&nbsp;'
            + unLockHtml);
    }
}

var dataSex = [['1','男'],['0','女']];
var storeSex = new Ext.data.SimpleStore({fields:['value','key'],data:dataSex});
var dataStatus = [['1','有效'],['0','无效'],['2','删除'],['3','锁定']];
var storeStatus = new Ext.data.SimpleStore({fields:['value','key'],data:dataStatus});
var recordRole = new Ext.data.Record.create([{name:'value',mapping:'value'}, {name:'key',mapping:'key'}]);
var readerRole = new Ext.data.JsonReader({ totalProperty:'total',root:"rows"},recordRole);
var storeRole = new Ext.data.Store({
    url:"../../RoleAction_readNameKeyValue.action?accountType=1",
    reader:readerRole
});
storeRole.load();

/**
 * 新增管理员账号
 * @param grid
 * @param store
 */
function insert_win(grid,store){
    var formPanel = new Ext.form.FormPanel({
        frame:true,
        autoScroll:true,
        layout:'column',
        border:false,
        items:[{
            plain:true,
            columnWidth :.5,
            border:false,
            layout: 'form',
            items:[{
                plain:true,
                labelAlign:'right',
                labelWidth:80,
                defaultType:'textfield',
                border:false,
                layout: 'form',
                defaults:{
                    width:200,
                    allowBlank:false,
                    blankText:'该项不能为空！'
                },
                items:[{
                    xtype:'hidden',
                    name:'account.accountType',
                    value:1
                },{
                    id:'userName.insert.info',
                    fieldLabel:"管理员账号名",
                    name:'account.userName',
                    regex:/^.{2,30}$/,
                    regexText:'请输入任意2--30个字符',
                    emptyText:'请输入任意2--30个字符',
                    listeners:{
                        blur:function(){
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
                        }
                    }
                },{
                    fieldLabel:"真实姓名",
                    name:'account.name',
                    regex:/^.{2,30}$/,
                    regexText:'请输入任意2--30个字符',
                    emptyText:'请输入任意2--30个字符'
                },{
                    fieldLabel:"状态",hiddenName:'account.status',
                    xtype:'combo',
                    mode:'local',
                    emptyText :'--请选择--',
                    editable : false,
                    typeAhead:true,
                    forceSelection: true,
                    triggerAction:'all',
                    displayField:"key",valueField:"value",
                    store:storeStatus,
                    value:'1'
                },{
                    id:'password.info',
                    fieldLabel:"密码",
                    name:'account.password',
                    inputType: 'password',
                    regex:/^.{6,100}$/,
                    regexText:'密码规则:6~100位!',
                    emptyText:'请输入密码!',
                    listeners:{
                        blur:function(){
                            var password = this.getValue();
                            if(password.length>0){
                                var myMask = new Ext.LoadMask(Ext.getBody(),{
                                    msg:'正在校验,请稍后...',
                                    removeMask:true
                                });
                                myMask.show();
                                Ext.Ajax.request({
                                    url : '../../AccountAction_checkPassword.action',
                                    params :{password:password},
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
                },{
                    id:'password2.info',
                    fieldLabel:"确认密码",
                    inputType: 'password',
                    regex:/^.{6,100}$/,
                    regexText:'密码规则:6~100位!',
                    emptyText:'请输入密码!',
                    listeners:{
                        blur : function(){
                            var password = Ext.getCmp('password.info').getValue();
                            if(password.length>0){
                                var password2 = this.getValue();
                                if(password != password2&&password2.length>0){
                                    Ext.MessageBox.show({
                                        title:'信息',
                                        width:250,
                                        msg:'<font color="red">"确认密码"和"密码"不一致!</font>',
                                        animEl:'password2.info',
                                        buttons:{'ok':'确定'},
                                        icon:Ext.MessageBox.INFO,
                                        closable:false,
                                        fn:function(e){
                                            if(e=='ok'){
                                                Ext.getCmp('password2.info').setValue('');
                                            }
                                        }
                                    });
                                }
                            } else {
                                Ext.MessageBox.show({
                                    title:'信息',
                                    width:270,
                                    msg:'<font color="red">请先输入"密码",再输入"确认密码"!</font>',
                                    animEl:'password2.info',
                                    buttons:{'ok':'确定'},
                                    icon:Ext.MessageBox.INFO,
                                    closable:false,
                                    fn:function(e){
                                        if(e=='ok'){
                                            Ext.getCmp('password2.info').setValue('');
                                        }
                                    }
                                });
                            }
                        }
                    }
                },{
                    fieldLabel:"角色",hiddenName:'role.id',
                    xtype:'combo',
                    mode:'local',
                    emptyText :'--请选择--',
                    editable : false,
                    typeAhead:true,
                    forceSelection: true,
                    triggerAction:'all',
                    displayField:"key",valueField:"value",
                    store:storeRole
                },{
                    fieldLabel:'登陆选择',
                    xtype:'radiogroup',
                    defaultType: 'radio',
                    layout:'column',
                    items: [
                        {columnWidth:.5,boxLabel: 'IP段', name: 'account.ipType', inputValue: 1, checked: true},
                        {columnWidth:.5,boxLabel: '指定IP', name: 'account.ipType', inputValue: 0}
                    ],
                    listeners:{
                        change : function(group,ck){
                            if(ck.inputValue==1){
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
            },{
                id:'ips.info',
                plain:true,
                labelAlign:'right',
                labelWidth:80,
                defaultType:'textfield',
                border:false,
                layout: 'form',
                defaults:{
                    width:200,
                    allowBlank:false,
                    blankText:'该项不能为空！'
                },
                items:[{
                    id:'startIp.info',
                    fieldLabel:"开始IP",
                    name:'account.startIp',
                    regex:/^.{2,30}$/,
                    regexText:'请输入任意2--30个字符',
                    emptyText:'请输入任意2--30个字符'
                },{
                    id:'endIp.info',
                    fieldLabel:"结束IP",
                    name:'account.endIp',
                    regex:/^.{2,30}$/,
                    regexText:'请输入任意2--30个字符',
                    emptyText:'请输入任意2--30个字符'
                }]
            },{
                id:'ipMac.info',
                hidden:true,
                plain:true,
                labelAlign:'right',
                labelWidth:80,
                defaultType:'textfield',
                border:false,
                layout: 'form',
                defaults:{
                    width:200,
                    allowBlank:false,
                    blankText:'该项不能为空！'
                },
                items:[{
                    id:'remoteIp.info',
                    fieldLabel:"登录IP",
                    name:'account.remoteIp',
                    regex:/^.{2,30}$/,
                    regexText:'请输入任意2--30个字符'
                }]
            },{
                plain:true,
                labelAlign:'right',
                labelWidth:80,
                defaultType:'textfield',
                border:false,
                layout: 'form',
                defaults:{
                    width:200,
                    allowBlank:false,
                    blankText:'该项不能为空！'
                },
                items:[{
                    fieldLabel:"MAC地址",
                    name:'account.mac',
                    allowBlank:true,
                    regex:/^((([0-9a-fA-F]{2}\-){5}[0-9a-fA-F]{2})|(([0-9a-fA-F]{2}:){5}[0-9a-fA-F]{2}))$/,
                    regexText:'这个不是mac地址:0a-45-be-e6-00-aa或者0a:45:be:e6:00:aa'
                }]
            }]
        },{
            plain:true,
            defaultType:'textfield',
            columnWidth :.5,
            labelAlign:'right',
            labelWidth:80,
            border:false,
            layout: 'form',
            defaults:{
                width:200,
                allowBlank:false,
                blankText:'该项不能为空！'
            },
            items:[{
                fieldLabel:"电话",
                name:'account.phone',
                regex:/^.{2,100}$/,
                regexText:'请输入(例:0571-88880571)',
                emptyText:'请输入(例:0571-88880571)'
            },{
                fieldLabel:"开始时间",
                name:'account.startHour',
                regex:/^[0-9]{1,2}$/,
                regexText:'请输入任意2--30个字符',
                emptyText:'请输入任意2--30个字符',
                value:9
            },{
                fieldLabel:"结束时间",
                name:'account.endHour',
                regex:/^[0-9]{1,2}$/,
                regexText:'请输入任意2--30个字符',
                emptyText:'请输入任意2--30个字符',
                value:18
            },{
                fieldLabel:"描述",
                name:'account.description',
                xtype:'textarea',
                allowBlank:true,
                regex:/^.{0,3000}$/,
                regexText:'请输入任意1--3000个字符',
                value:'这是一个管理员账号信息'
            },{
                //fieldLabel:"性别",
                hidden:true,
                hiddenName:'account.sex',
                xtype:'combo',
                mode:'local',
                emptyText :'--请选择--',
                editable : false,
                typeAhead:true,
                forceSelection: true,
                triggerAction:'all',
                displayField:"key",valueField:"value",
                store:storeSex,
                value:'1'
            },{
                //fieldLabel:"部门",
                hidden:true,
                name:'account.depart',
                regex:/^.{2,30}$/,
                regexText:'请输入任意2--30个字符',
                emptyText:'请输入任意2--30个字符',
                value:'信息部'
            },{
                //fieldLabel:"职务",
                hidden:true,
                name:'account.title',
                regex:/^.{2,30}$/,
                regexText:'请输入任意2--30个字符',
                emptyText:'请输入任意2--30个字符',
                value:'主任'
            },{
                //fieldLabel:"Email",
                hidden:true,
                name:'account.email',
                regex:/^\w+[\w.]*@[\w.]+\.\w+$/,
                regexText:'请输入(例:hello@hzih.com)',
                emptyText:'请输入(例:hello@hzih.com)',
                value:'hello@hzih.net'
            }]
        }]
    });
    var win = new Ext.Window({
        title:"新增信息",
        width:650,
        layout:'fit',
        height:390,
        modal:true,
        items:formPanel,
        listeners:{
            show:function(){
                Ext.getCmp('ipMac.info').hide();
                Ext.getCmp('remoteIp.info').disable();
                Ext.getCmp('startIp.info').enable();
                Ext.getCmp('endIp.info').enable();
                Ext.getCmp('ips.info').show();
            }
        },
        bbar:[
            '->',
            {
                id:'insert_win.info',
                text:'保存',
                handler:function(){
                    if (formPanel.form.isValid()) {
                        formPanel.getForm().submit({
                            url :'../../AccountAction_insert.action',
                            method :'POST',
                            waitTitle :'系统提示',
                            waitMsg :'正在保存,请稍后...',
                            success : function(form,action) {
                                var msg = action.result.msg;
                                Ext.MessageBox.show({
                                    title:'信息',
                                    width:250,
                                    msg:msg,
                                    animEl:'insert_win.info',
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
                            width:200,
                            msg:'请填写完成再提交!',
                            animEl:'insert_win.info',
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
            }
        ]
    }).show();
}

/**
 * 标记管理员账号
 * @param grid
 * @param store
 */
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
            var userName = item.data.userName;
//            var isFilter = item.data.isFilter;
//            var isFilterT;
//            var isFilterF;
//            if(isFilter=='true'||isFilter==true){
//            	isFilterT = true;
//            	isFilterF = false;
//            }else if(isFilter=='false'||isFilter == false){
//            	isFilterT = false;
//            	isFilterF = true;
//            }
//            var isVirusScan = item.data.isVirusScan;
//            var isVirusScanT;
//            var isVirusScanF;
//            if(isVirusScan=='true'||isVirusScan==true){
//            	isVirusScanT = true;
//            	isVirusScanF = false;
//            }else if(isVirusScan=='false'||isVirusScan == false){
//            	isVirusScanT = false;
//            	isVirusScanF = true;
//            }
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
                reader:
                    new Ext.data.JsonReader(
                        {},
                        [{name:"id"},{name:"isFilter"},{name:"isVirusScan"},{name:"infoLevel"}]
                    ),
                items : [{
                    width:370,
                    title:'安全属性 -- 使用说明',
                    xtype:'fieldset',
                    html:"<font color='green'>1.所有项都为必填项；"
                },{
                    xtype:'hidden',name:'userName',value:item.data.userName
                },{
                    xtype:'hidden',name:'id'
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
                    hiddenName:'infoLevel',
                    mode:'local',
                    emptyText :'--请选择--',
                    editable : false,
                    typeAhead:true,
                    forceSelection: true,
                    triggerAction:'all',
                    displayField:"key",valueField:"value",
                    store: storeInfoLevel
                }]
            });
            if(formPanel){
                formPanel.getForm().load({
                    url:"../../AccountAction_selectAccountFlag.action",
                    params :{id:item.data.id,userName:item.data.userName},
                    success:function(c,d){},
                    failure:function(c,d){
                        Ext.Msg.alert("错误","加载数据出错！")
                    }
                })
            }
        });
    }
    var win = new Ext.Window({
        title:"管理员账号标记",
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
                                        url :'../../AccountAction_updateFlag.action',
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

/**
 * 修改管理员账号
 */
function showUpdateUser(){
    var recordRoleUpdate = new Ext.data.Record.create([{name:'value',mapping:'value'}, {name:'key',mapping:'key'}]);
    var readerRoleUpdate = new Ext.data.JsonReader({ totalProperty:'total',root:"rows"},recordRoleUpdate);
    var storeRoleUpdate = new Ext.data.Store({
        url:"../../RoleAction_readNameKeyValue.action?accountType=1",
        reader:readerRoleUpdate,
        listeners : {
            load : function(){
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
    if(selModel.hasSelection()){
        var selections = selModel.getSelections();
        Ext.each(selections,function(item){
            var ipType = item.data.ipType;
            var ipTypeT = ipType==true?true:false;
            var ipTypeF = ipType==true?false:true;
            formPanel = new Ext.form.FormPanel({
                frame:true,
                autoScroll:true,
                layout:'column',
                border:false,
                items:[{
                    plain:true,
                    columnWidth :.5,
                    border:false,
                    layout: 'form',
                    items:[{
                        plain:true,
                        labelAlign:'right',
                        labelWidth:80,
                        defaultType:'textfield',
                        border:false,
                        layout: 'form',
                        defaults:{
                            width:200,
                            allowBlank:false,
                            blankText:'该项不能为空！'
                        },
                        items:[{
                            xtype:'hidden',
                            name:'account.accountType',
                            value:1
                        },{
                            name:'account.id',
                            xtype:'hidden',
                            value:item.data.id
                        },{
                            name:'account.userName',
                            xtype:'hidden',
                            value:item.data.userName
                        },{
                            id:'userName.insert.info',
                            fieldLabel:"管理员账号名",
                            xtype:'displayfield',
                            value:item.data.userName
                        },{
                            fieldLabel:"真实姓名",
                            name:'account.name',
                            value:item.data.name,
                            regex:/^.{2,30}$/,
                            regexText:'请输入任意2--30个字符',
                            emptyText:'请输入任意2--30个字符'
                        },{
                            fieldLabel:"状态",hiddenName:'account.status', value:item.data.status,
                            xtype:'combo',
                            mode:'local',
                            emptyText :'--请选择--',
                            editable : false,
                            typeAhead:true,
                            forceSelection: true,
                            triggerAction:'all',
                            displayField:"key",valueField:"value",
                            store:storeStatus
                        },{
                            id:'password.info',
                            fieldLabel:"密码",
                            name:'account.password',
                            inputType: 'password',
                            allowBlank:true,
                            regex:/^.{6,100}$/,
                            regexText:'密码规则:6~100位!',
                            listeners:{
                                blur:function(){
                                    var password = this.getValue();
                                    if(password.length>0){
                                        var myMask = new Ext.LoadMask(Ext.getBody(),{
                                            msg:'正在校验,请稍后...',
                                            removeMask:true
                                        });
                                        myMask.show();
                                        Ext.Ajax.request({
                                            url : '../../AccountAction_checkPassword.action',
                                            params :{password:password},
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
                        },{
                            id:'password2.info',
                            fieldLabel:"确认密码",
                            inputType: 'password',
                            regex:/^.{6,100}$/,
                            regexText:'密码规则:6~100位!',
                            allowBlank:true,
                            listeners:{
                                blur : function(){
                                    var password = Ext.getCmp('password.info').getValue();
                                    if(password.length>0){
                                        var password2 = this.getValue();
                                        if(password != password2&&password2.length>0){
                                            Ext.MessageBox.show({
                                                title:'信息',
                                                width:250,
                                                msg:'<font color="red">"确认密码"和"密码"不一致!</font>',
                                                animEl:'password2.info',
                                                buttons:{'ok':'确定'},
                                                icon:Ext.MessageBox.INFO,
                                                closable:false,
                                                fn:function(e){
                                                    if(e=='ok'){
                                                        Ext.getCmp('password2.info').setValue('');
                                                    }
                                                }
                                            });
                                        }
                                    } else {
                                        Ext.MessageBox.show({
                                            title:'信息',
                                            width:270,
                                            msg:'<font color="red">请先输入"密码",再输入"确认密码"!</font>',
                                            animEl:'password2.info',
                                            buttons:{'ok':'确定'},
                                            icon:Ext.MessageBox.INFO,
                                            closable:false,
                                            fn:function(e){
                                                if(e=='ok'){
                                                    Ext.getCmp('password2.info').setValue('');
                                                }
                                            }
                                        });
                                    }
                                }
                            }
                        },{
                            id:'role.update.info',
                            fieldLabel:"角色",hiddenName:'role.id',value:item.data.roleId,
                            xtype:'combo',
                            mode:'local',
                            emptyText :'--请选择--',
                            editable : false,
                            typeAhead:true,
                            forceSelection: true,
                            triggerAction:'all',
                            displayField:"key",valueField:"value",
                            store:storeRoleUpdate
                        },{
                            id:'ipType.info',
                            fieldLabel:'登陆选择',
                            xtype:'radiogroup',
                            defaultType: 'radio',
                            layout:'column',
                            items: [
                                {columnWidth:.5,boxLabel: 'IP段', name: 'account.ipType', inputValue: 1, checked: ipTypeT},
                                {columnWidth:.5,boxLabel: '指定IP', name: 'account.ipType', inputValue: 0, checked:ipTypeF}
                            ],
                            listeners:{
                                change : function(group,ck){
                                    if(ck.inputValue==1){
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
                    },{
                        id:'ips.info',
                        plain:true,
                        labelAlign:'right',
                        labelWidth:80,
                        defaultType:'textfield',
                        border:false,
                        layout: 'form',
                        defaults:{
                            width:200,
                            allowBlank:false,
                            blankText:'该项不能为空！'
                        },
                        items:[{
                            id:'startIp.info',
                            fieldLabel:"开始IP",
                            name:'account.startIp',value:item.data.startIp,
                            regex:/^.{2,30}$/,
                            regexText:'请输入任意2--30个字符',
                            emptyText:'请输入任意2--30个字符'
                        },{
                            id:'endIp.info',
                            fieldLabel:"结束IP",
                            name:'account.endIp',value:item.data.endIp,
                            regex:/^.{2,30}$/,
                            regexText:'请输入任意2--30个字符',
                            emptyText:'请输入任意2--30个字符'
                        }]
                    },{
                        id:'ipMac.info',
                        hidden:true,
                        plain:true,
                        labelAlign:'right',
                        labelWidth:80,
                        defaultType:'textfield',
                        border:false,
                        layout: 'form',
                        defaults:{
                            width:200,
                            allowBlank:false,
                            blankText:'该项不能为空！'
                        },
                        items:[{
                            id:'remoteIp.info',
                            fieldLabel:"登录IP",
                            name:'account.remoteIp',value:item.data.remoteIp,
                            regex:/^.{2,30}$/,
                            regexText:'请输入任意2--30个字符'
                        }]
                    },{
                        plain:true,
                        labelAlign:'right',
                        labelWidth:80,
                        defaultType:'textfield',
                        border:false,
                        layout: 'form',
                        defaults:{
                            width:200,
                            allowBlank:false,
                            blankText:'该项不能为空！'
                        },
                        items:[{
                            fieldLabel:"MAC地址",
                            name:'account.mac',value:item.data.mac,
                            allowBlank:true,
                            regex:/^((([0-9a-fA-F]{2}\-){5}[0-9a-fA-F]{2})|(([0-9a-fA-F]{2}:){5}[0-9a-fA-F]{2}))$/,
                            regexText:'这个不是mac地址:0a-45-be-e6-00-aa或者0a:45:be:e6:00:aa'
                        }]
                    }]
                },{
                    plain:true,
                    defaultType:'textfield',
                    columnWidth :.5,
                    labelAlign:'right',
                    labelWidth:80,
                    border:false,
                    layout: 'form',
                    defaults:{
                        width:200,
                        allowBlank:false,
                        blankText:'该项不能为空！'
                    },
                    items:[{
                        fieldLabel:"电话",
                        name:'account.phone', value:item.data.phone,
                        regex:/^.{2,100}$/,
                        regexText:'请输入(例:0571-88880571)',
                        emptyText:'请输入(例:0571-88880571)'
                    },{
                        fieldLabel:"开始时间",
                        name:'account.startHour',value:item.data.startHour,
                        regex:/^[0-9]{1,2}$/,
                        regexText:'请输入任意2--30个字符',
                        emptyText:'请输入任意2--30个字符'
                    },{
                        fieldLabel:"结束时间",
                        name:'account.endHour',value:item.data.endHour,
                        regex:/^[0-9]{1,2}$/,
                        regexText:'请输入任意2--30个字符',
                        emptyText:'请输入任意2--30个字符'
                    },{
                        fieldLabel:"描述",
                        name:'account.description',value:item.data.description,
                        xtype:'textarea',
                        allowBlank:true,
                        regex:/^.{0,3000}$/,
                        regexText:'请输入任意1--3000个字符'
                    },{
                        //fieldLabel:"性别",
                        hidden:true,
                        hiddenName:'account.sex',value:item.data.sex,
                        xtype:'combo',
                        mode:'local',
                        emptyText :'--请选择--',
                        editable : false,
                        typeAhead:true,
                        forceSelection: true,
                        triggerAction:'all',
                        displayField:"key",valueField:"value",
                        store:storeSex
                    },{
                        //fieldLabel:"部门",
                        hidden:true,
                        name:'account.depart',value:item.data.depart,
                        regex:/^.{2,30}$/,
                        regexText:'请输入任意2--30个字符',
                        emptyText:'请输入任意2--30个字符'
                    },{
                        //fieldLabel:"职务",
                        hidden:true,
                        name:'account.title',value:item.data.title,
                        regex:/^.{2,30}$/,
                        regexText:'请输入任意2--30个字符',
                        emptyText:'请输入任意2--30个字符'
                    },{
                        //fieldLabel:"Email",
                        hidden:true,
                        name:'account.email',value:item.data.email,
                        regex:/^\w+[\w.]*@[\w.]+\.\w+$/,
                        regexText:'请输入(例:hello@hzih.com)',
                        emptyText:'请输入(例:hello@hzih.com)'
                    }]
                }]
            });
        });
    }

    var win = new Ext.Window({
        title:"修改信息",
        width:650,
        layout:'fit',
        height:390,
        modal:true,
        items:formPanel,
        listeners:{
            show:function(){
                var ipType = Ext.getCmp('ipType.info').getValue();
                if(ipType.inputValue==1){
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
        bbar:[
            '->',
            {
                id:'update_win.info',
                text:'修改',
                handler:function(){
                    Ext.MessageBox.show({
                        title:'信息',
                        width:250,
                        msg:'确定要修改?',
                        animEl:'update_win.info',
                        buttons:{'ok':'继续','no':'取消'},
                        icon:Ext.MessageBox.WARNING,
                        closable:false,
                        fn:function(e){
                            if(e == 'ok'){
                                var password = Ext.getCmp('password.info').getValue();
                                var password2 = Ext.getCmp('password2.info').getValue();
                                if(password!=password2){
                                    Ext.MessageBox.show({
                                        title:'信息',
                                        width:250,
                                        msg:'<font color="red">"确认密码"和"密码"不一致!</font>',
                                        animEl:'update_win.info',
                                        buttons:{'ok':'确定'},
                                        icon:Ext.MessageBox.INFO,
                                        closable:false,
                                        fn:function(e){
                                            if(e=='ok'){
                                                Ext.getCmp('password2.info').setValue('');
                                            }
                                        }
                                    });
                                } else {
                                    if (formPanel.form.isValid())  {
                                        formPanel.getForm().submit({
                                            url :'../../AccountAction_update.action',
                                            method :'POST',
                                            waitTitle :'系统提示',
                                            waitMsg :'正在修改,请稍后...',
                                            success : function(form,action) {
                                                var msg = action.result.msg;
                                                Ext.MessageBox.show({
                                                    title:'信息',
                                                    width:250,
                                                    msg:msg,
                                                    animEl:'update_win.info',
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
                                            width:200,
                                            msg:'请填写完成再提交!',
                                            animEl:'update_win.info',
                                            buttons:{'ok':'确定'},
                                            icon:Ext.MessageBox.ERROR,
                                            closable:false
                                        });
                                    }
                                }
                            }
                        }
                    });

                }
            },{
                text:'关闭',
                handler:function(){
                    win.close();
                }
            }
        ]
    }).show();
}

/**
 * 解锁管理员
 * @param value
 */
function unLockUser(id,userName){

    Ext.MessageBox.show({
        title:'信息',
        msg:'<font color="green">确定要解锁？</font>',
        animEl:id+'.unlock.info',
        width:260,
        buttons:{'ok':'确定','no':'取消'},
        icon:Ext.MessageBox.INFO,
        closable:false,
        fn:function(e){
            if(e == 'ok'){
                var myMask = new Ext.LoadMask(Ext.getBody(),{
                    msg : '正在解锁,请稍后...',
                    removeMask:true
                });
                myMask.show();
                Ext.Ajax.request({
                    url : '../../AccountAction_unlock.action',
                    params :{id:id,userName:userName},
                    method:'POST',
                    success : function(r,o){
                        myMask.hide();
                        var respText = Ext.util.JSON.decode(r.responseText);
                        var msg = respText.msg;
                        Ext.MessageBox.show({
                            title:'信息',
                            width:250,
                            msg:msg,
                            animEl:id+'.unlock.info',
                            buttons:{'ok':'确定'},
                            icon:Ext.MessageBox.INFO,
                            closable:false,
                            fn:function(e){
                                if(e=='ok'){
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
 * 删除管理员账号
 */
function deleteUser(value){
    var grid = Ext.getCmp('grid.info');
    var store = grid.getStore();
    var selModel = grid.getSelectionModel();
    var formPanel;
    if(selModel.hasSelection()){
        var selections = selModel.getSelections();
        Ext.each(selections,function(item){
            Ext.MessageBox.show({
                title:'信息',
                msg:'<font color="green">确定要删除所选记录？</font>',
                animEl:value+'.delete.info',
                width:260,
                buttons:{'ok':'确定','no':'取消'},
                icon:Ext.MessageBox.INFO,
                closable:false,
                fn:function(e){
                    if(e == 'ok'){
                        var myMask = new Ext.LoadMask(Ext.getBody(),{
                            msg : '正在删除,请稍后...',
                            removeMask:true
                        });
                        myMask.show();
                        Ext.Ajax.request({
                            url : '../../AccountAction_delete.action',             // 删除 连接 到后台
                            params :{id:value,userName:item.data.userName},
                            method:'POST',
                            success : function(r,o){
                                myMask.hide();
                                var respText = Ext.util.JSON.decode(r.responseText);
                                var msg = respText.msg;
                                Ext.MessageBox.show({
                                    title:'信息',
                                    width:250,
                                    msg:msg,
                                    animEl:value+'.delete.info',
                                    buttons:{'ok':'确定'},
                                    icon:Ext.MessageBox.INFO,
                                    closable:false,
                                    fn:function(e){
                                        if(e=='ok'){
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