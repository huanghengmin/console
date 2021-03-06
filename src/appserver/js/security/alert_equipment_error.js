/*
 * 设备故障报警
 */
Ext.onReady(function() {

	Ext.QuickTips.init();
    var pageSize = 15;
    var equRecord = new Ext.data.Record.create([
        {name:'key',mapping:'key'},
        {name:'value',mapping:'value'}
    ]);
    var equStore = new Ext.data.Store({
        url:'../../SecurityAction_equ.action',
        reader:new Ext.data.JsonReader({totalProperty:'total',root:'rows'},equRecord)
    });
    equStore.load();
	var tb = new Ext.Toolbar({
		width : 720,
		height : 30,
		items : ['起始日期：', {
			id : 'startDate',
			xtype : 'datefield',
			name : 'startDate',
			emptyText : '点击输入日期',
			format : 'Y-m-d'
		}, {
			xtype : 'tbspacer',
			width : 10
		}, '结束日期：', {
			id : 'endDate',
			xtype : 'datefield',
			name : 'endDate',
			emptyText : '点击输入日期',
			format : 'Y-m-d'
		}, {
			xtype : 'tbspacer',
			width : 10
		}, '设备名：', new Ext.form.ComboBox({
			id : "equipmentName",
			store : equStore,
			valueField : 'value',
			displayField : 'key',
			mode : 'remote',
			value : '',
			emptyText : '--请选择--',
			forceSelection : true,
			triggerAction : 'all',
			selectOnFocus : true
		}), {
			xtype : 'tbspacer',
			width : 10
		}, '状态',{
            id : 'read.tb.info',
            xtype : 'combo',
            store : new Ext.data.ArrayStore({
                autoDestroy : true,
                fields : ['value', 'key'],
                data : [
                    ['all', '全部'],
                    ['Y', '已读'],
                    ['N', '未读']
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
					var startDate = Ext.fly("startDate").dom.value == '点击输入日期'
							? null
							: Ext.fly("startDate").dom.value;
					var endDate = Ext.fly("endDate").dom.value == '点击输入日期'
							? null
							: Ext.fly("endDate").dom.value;
					var equipmentName = Ext.fly('equipmentName').dom.value == '--请选择--'
                            ? ''
                            : Ext.getCmp("equipmentName").getValue();
                    var read = Ext.fly('read.tb.info').dom.value == '--请选择--'
                            ? ''
                            : Ext.getCmp("read.tb.info").getValue();
                    if(startDate!=null && endDate!=null) {
                        var myMask = new Ext.LoadMask(Ext.getBody(),{
                            msg : '正在处理,请稍后...',
                            removeMask : true
                        });
                        myMask.show();
                        Ext.Ajax.request({
                            url : '../../AuditAction_checkDate.action',
                            params : {startDate:startDate,endDate:endDate},
                            method :'POST',
                            success:function(r,o){
                                myMask.hide();
                                var respText = Ext.util.JSON.decode(r.responseText);
                                var msg = respText.msg;
                                var clear = respText.clear;
                                if(!clear){
                                    Ext.MessageBox.show({
                                        title:'信息',
                                        width:280,
                                        msg:'结束时间不能早于开始时间',
                                        animEl:'endDate.tb.info',
                                        buttons:{'ok':'确定'},
                                        icon:Ext.MessageBox.ERROR,
                                        closable:false,
                                        fn:function(e){
                                            if(e=='ok'){
                                                Ext.getCmp('endDate').setValue('');
                                            }
                                        }
                                    });
                                } else{
                                    store.setBaseParam('startDate', startDate);
                                    store.setBaseParam('endDate', endDate);
                                    store.setBaseParam("equipmentName", equipmentName);
					                store.setBaseParam("read", read);
                                    store.load({
                                        params : {
                                            start : 0,
                                            limit : pageSize
                                        }
                                    });
                                }
                            }
                        });
                    } else {
                        store.setBaseParam('startDate', startDate);
                        store.setBaseParam('endDate', endDate);
                        store.setBaseParam("equipmentName", equipmentName);
					    store.setBaseParam("read", read);
                        store.load({
                            params : {
                                start : 0,
                                limit : pageSize
                            }
                        });
                    }
				}
			}
		}, '-', {
			pressed : false,
			text : '标记为已读',
			id : 'delete_btn',
			handler : function() {
				var selectedRows = grid.getSelectionModel().getSelections();
				if (selectedRows.length == 0) {
					Ext.Msg.alert('警告', '请选中你要标记的行！');
				} else {
					var ids = new Array();
					for (var i = 0; i < selectedRows.length; i++) {
						ids[i] = selectedRows[i].data.id;
					}
                    var myMask = new Ext.LoadMask(Ext.getBody(),{
                        msg:'正在标记,请稍后...',
                        removeMask:true
                    });
                    myMask.show();
                    Ext.Ajax.request({
						url : '../../SecurityAction_setEquRead.action',
						params : {
							ids : ids
						},
						success : function(response, options) {
                            myMask.hide();
                            grid.render();
							store.reload();
						}
					});
				}
			}
		}]
	});
    var record = new Ext.data.Record.create([
        {name:'id',			mapping:'id'},
        {name:'alertTime',		mapping:'alertTime'},
        {name:'equName',		mapping:'equName'},
        {name:'alertInfo',		mapping:'alertInfo'},
        {name:'ip',		mapping:'ip'},
        {name:'isRead',		mapping:'isRead'}
    ]);
    var proxy = new Ext.data.HttpProxy({
        url : '../../SecurityAction_selectEqu.action'
    });
    var reader = new Ext.data.JsonReader({
        totalProperty:'total',
        root:'rows',
        idProperty : 'id'
    },record);
    var store = new Ext.data.GroupingStore({
        id:"store.info",
        proxy : proxy,
        reader : reader
    });

	var sm = new Ext.grid.CheckboxSelectionModel();
    var rowNumber = new Ext.grid.RowNumberer();         //自动 编号
	var grid = new Ext.grid.GridPanel({
		height : "555",
		store : store,
		loadMask : true,
		trackMouseOver : true,// 鼠标悬浮高亮显示
		columnLines : true,// 列分隔符

		// grid columns
		columns : [sm,rowNumber, {
			id : 'id',
			dataIndex : 'id',
			hidden : true
		}, {
            align:'center',
			header : "报警时间",
			dataIndex : 'alertTime',
			width : 150,
			menuDisabled : true,
			sortable : true
		}, {
            align:'center',
			header : "设备名",
			dataIndex : 'equName',
			width : 200,
			menuDisabled : true
		}, {
            align:'center',
			header : "IP地址",
			dataIndex : 'ip',
			width : 150,
			menuDisabled : true
		}, {
            align:'center',
			header : "状态",
			dataIndex : 'isRead',
			width : 100,
			menuDisabled : true,
			renderer : function(v, p, r) {
				return v == 'Y' ? '<font color="green">已读</font>' : '<font color="red">未读</font>';
			}
		}, {
            align:'center',
			header : "报警内容",
			dataIndex : 'alertInfo',
			width : 300,
			menuDisabled : true
		}],
		sm : sm,

        bbar : new Ext.PagingToolbar({
            pageSize : pageSize,
            store:store,
            displayInfo:true,
            displayMsg:"显示第{0}条记录到第{1}条记录，一共{2}条",
            emptyMsg:"没有记录",
            beforePageText:"当前页",
            afterPageText:"共{0}页"
        }),
		tbar : tb,
		viewConfig : {}
	});

	new Ext.Viewport({
		layout : 'fit',
		border : false,
		items : [grid]
	});
	
	store.load({ params : { start : 0, limit : pageSize } });
});
