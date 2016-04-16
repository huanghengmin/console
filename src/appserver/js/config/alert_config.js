/*
 *报警配置
 */

Ext.onReady(function() {

	Ext.QuickTips.init();

	// turn on validation errors beside the field globally
	Ext.form.Field.prototype.msgTarget = 'side';
	var fp = new Ext.FormPanel({
		plain : true,
		border : false,
		labelAlign : 'right',
		labelWidth : 140,
		width : '100%',
		waitMsgTarget : true,
		renderTo : document.body,

		items : [new Ext.form.FieldSet({
			title : '邮件报警设置',
			autoHeight : true,
			defaultType : 'textfield',
            width:450,
            defaults:{
                allowBlank:false,
                blankText:'该项不能为空!',
                width:200
            },
			items : [{
				fieldLabel : '邮件服务器地址',
				name : 'server',
				id : 'server',
                regex:/^[\w\.]+\w$/,
                regexText:'只能输入(例:smtp.sina.com)',
                emptyText:'请输入(例:smtp.sina.com)'
			}, {
				fieldLabel : '端口',
				name : 'port',
				id : 'port',
                regex:/^(6553[0-6]|655[0-2][0-9]|65[0-4][0-9]{2}|6[0-4][0-9]{3}|[1-5][0-9]{4}|[1-9][0-9]{3}|[1-9][0-9]{2}|[1-9][0-9]|[1-9])$/,
                regexText:'这个不是端口类型1~65536',
                emptyText:'请输入端口1~65536'
			}, {
				fieldLabel : '发送邮箱地址',
				name : 'email',
				id : 'email',
				vtype : 'email',
                regex:/^\w+[\w.]*@[\w.]+\.\w+$/,
                regexText:'请输入(例:xiaom@hzih.com)',
                emptyText:'请输入(例:xiaom@hzih.com)'
			}, {
				fieldLabel : '用户名',
				name : 'account',
				id : 'account',
                regex:/^(\w{1,63})|(\w+[\w.]*@[\w.]+\.\w+)$/,
                regexText:'只能输入1--63位英文字母、数字和下划线或者(例:xiaom@hzih.com)',
                emptyText:'请输入1--63位英文字母、数字和下划线或者(例:xiaom@hzih.com)'
			}, {
				fieldLabel : '密码',
				name : 'password',
				id : 'password',
				inputType : 'password',
                regex:/^[\@A-Za-z0-9\!\#\$\%\^\&\*\.\~_]{6,22}$/,
                regexText:'只能输入6--22位(a-z、A-Z、0-9、~、!、@、#、$、%、^、&、*或_)',
                emptyText:'请输入6--22位(a-z、A-Z、0-9、~、!、@、#、$、%、^、&、*或_)'
			}, {
				fieldLabel : '报警标题',
				name : 'title',
				id : 'title',
                regex:/^.{1,500}$/,
                regexText:'只能输入1--500位任意字符',
                emptyText:'请输入1--500位任意字符'
			}, {
				fieldLabel : '报警频率(分钟)',
				name : 'mailfrequency',
				id : 'mailfrequency',
                regex:/^([1-9]([0-9]{0,5}|[0-9]{0,4}|[0-9]{0,3}|[0-9]{0,2}|[0-9]|[0-9]{0}))$/,
                regexText:'这个不是数字1--999999(单位:分钟)',
                emptyText:'请输入数字1--999999(单位:分钟)'
            },{
                fieldLabel:'收件人列表',
                xtype:'displayfield',
                value:'<a href="javascript:;" style="color: green;" onclick="show_list();">查看</a>'
			}]
		}), new Ext.form.FieldSet({
			title : '短信报警设置',
			autoHeight : true,
			defaultType : 'textfield',
            width:450,
            defaults:{
                allowBlank:false,
                blankText:'该项不能为空!',
                width:200
            },
			items : [{
				fieldLabel : '服务中心号码',
				name : 'smsnumber',
				id : 'smsnumber',
                regex:/^\d{1,20}$/,
                regexText:'这个不是1-20位的数字',
                emptyText:'请输入1-20位的数字'
			}, {
				fieldLabel : '报警标题',
				name : 'smstitle',
				id : 'smstitle',
                regex:/^.{1,500}$/,
                regexText:'只能输入1--500位任意字符',
                emptyText:'请输入1--500位任意字符'
			}, {
				fieldLabel : '报警频率(分钟)',
				name : 'smsfrequency',
				id : 'smsfrequency',
                regex:/^([1-9]([0-9]{0,5}|[0-9]{0,4}|[0-9]{0,3}|[0-9]{0,2}|[0-9]|[0-9]{0}))$/,
                regexText:'这个不是数字1--999999(单位:分钟)',
                emptyText:'请输入数字1--999999(单位:分钟)'
			}]
		})]
	});

	// 加载配置数据
	if (fp) {
        var myMask = new Ext.LoadMask(Ext.getBody(),{
            msg : '正在加载数据,请稍后...',
            removeMask:true
        });
        myMask.show();
		Ext.Ajax.request({
			url : '../../AlertManagerAction_loadConfig.action',
			params : {
				dc : new Date()
			},
			success : function(response, opts) {
				var data = Ext.util.JSON.decode(response.responseText);
                Ext.fly("server").dom.value = data.server;
                Ext.fly("port").dom.value = data.port;
                Ext.fly("email").dom.value = data.email;
                Ext.fly("account").dom.value = data.account;
                Ext.fly("password").dom.value = data.password;
                Ext.fly("title").dom.value = data.title;
                Ext.fly("mailfrequency").dom.value = data.mailfrequency;
				Ext.fly("smsnumber").dom.value = data.smsnumber;

                Ext.fly("smstitle").dom.value = data.smstitle;
                Ext.fly("smsfrequency").dom.value = data.smsfrequency;
                myMask.hide();
            },
			failure : function(response, opts) {
                myMask.hide();
				Ext.Msg.alert('', "加载配置数据失败，请重试！");
			}
		});
	}
    var button = new Ext.FormPanel({
		plain : true,
		border : false,
        buttonAlign : 'left',
		renderTo : document.body,
		buttons : [new Ext.Toolbar.Spacer({width:140}),{
			text : '短信测试',
			listeners : {
				click : function() {
//					Ext.Msg.alert('温馨提示', '该功能暂时未开放...');
                    Ext.Msg.show({
						width : 200,
						title : "输入接收手机号码",
						prompt : true,
						buttons : Ext.Msg.OKCANCEL,
						fn : function(btnId, text, opt) {
							if (btnId == "ok") {
                                var myMask = new Ext.LoadMask(Ext.getBody(),{
                                    msg : '正在处理,请稍后...',
                                    removeMask:true
                                });
                                myMask.show();
								Ext.Ajax.request({
									url : '../../AlertManagerAction_validateShortMessage.action',
									params : {
										contact : text
									},
									success : function(response, opts) {
                                        var respText = Ext.util.JSON.decode(response.responseText);
                                        var msg = respText.msg;
                                        myMask.hide();
										Ext.Msg.alert('温馨提示', msg);
									}
								});
							}
						}
					});
				}
			}
		}, {
			text : '邮件测试',
			listeners : {
				click : function() {
					Ext.Msg.show({
						width : 200,
						title : "输入接收邮件的E-mail",
						prompt : true,
						buttons : Ext.Msg.OKCANCEL,
						fn : function(btnId, text, opt) {
							if (btnId == "ok") {
                                var myMask = new Ext.LoadMask(Ext.getBody(),{
                                    msg : '正在处理,请稍后...',
                                    removeMask:true
                                });
                                myMask.show();
								Ext.Ajax.request({
									url : '../../AlertManagerAction_validateEmail.action',
									params : {
										contact : text
									},
									success : function(response, opts) {
                                        var respText = Ext.util.JSON.decode(response.responseText);
                                        var msg = respText.msg;
                                        myMask.hide();
										Ext.Msg.alert('温馨提示', msg);
									}
								});
							}
						}
					});
				}
			}
		}, {
			text : '保存',
			listeners : {
				click : function() {
					fp.getForm().submit({
						clientValidation : true,
						url: '../../AlertManagerAction_saveConfig.action',
                        waitTitle :'系统提示',
                        waitMsg :'正在处理...',
						params : {
							newStatus : 'delivered'
						},
						success : function(form, action) {
							Ext.Msg.alert('温馨提示', action.result.msg);
						}
					});

				}
			}
		}]
	});
	new Ext.Viewport({
		layout : 'fit',
		border : false,
		items : [{frame:true,autoScroll:true,items : [fp,button]}]
	});

});

function show_list() {
    var record = new Ext.data.Record.create([
        {name:'email',mapping:'email'},
        {name:'flag',mapping:'flag'}
    ]);
    var proxy = new Ext.data.HttpProxy({
        url:"../../AlertManagerAction_queryEmail.action"
    });
    var reader = new Ext.data.JsonReader({
        totalProperty:"total",
        root:"rows"
    },record);
    var store = new Ext.data.GroupingStore({
        proxy : proxy,
        reader : reader
    });
    var start = 0;			//分页--开始数
	var pageSize = 10;		//分页--每页数
	store.load({
        params:{
            start:start,limit:pageSize
        }
    });
    var email_edit = new Ext.form.TextField({
        id:'email_edit.info',
         regex:/^\w+[\w.]*@[\w.]+\.\w+$/,
                regexText:'请输入(例:xiaom@hzih.com)',
                emptyText:'请输入(例:xiaom@hzih.com)',
        listeners:{
           blur:function(){
               var email = email_edit.getValue();
               var myMask = new Ext.LoadMask(Ext.getBody(),{
                   msg:'正在校验,请稍后...',
                   removeMask:true
               });
               myMask.show();
               Ext.Ajax.request({
                   url:'../../AlertManagerAction_checkEmail.action',
                   method:'POST',
                   params:{email:email},
                   success:function(action){
                       var json = Ext.decode(action.responseText);
                       myMask.hide();
                       if(json.msg != '0000'){
                           Ext.MessageBox.show({
                               title:'信息',
                               msg:json.msg,
                               animEl:'email_edit.info',
                               buttons:Ext.Msg.OK,
                               icon:Ext.MessageBox.WARNING,
                               fn:function(e){
                                   if(e=='ok'){
//                                       Ext.getCmp('innerIpMacBlackGrid.info').getSelectionModel().getSelections();
                                   }
                               }
                           });
                       }
                   }
               });
           }
        }
    });
	var boxM = new Ext.grid.CheckboxSelectionModel();   //复选框
	var rowNumber = new Ext.grid.RowNumberer();         //自动 编号
	var colM = new Ext.grid.ColumnModel([
        rowNumber,
		boxM,
        {header:"Email地址",dataIndex:"email",align:'center',editor:email_edit},
        {header:"操作标记",dataIndex:"flag",align:'center',renderer:show_url_flag,width:40}
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
    var grid = new Ext.grid.EditorGridPanel({
        id:'grid.email.info',
        plain:true,
        renderTo:Ext.getBody(),
        animCollapse:true,
        height:300,width:500,
        loadMask:{msg:'正在加载数据,请稍后...'},
        border:false,
        collapsible:false,
        heightIncrement:true,
        cm:colM,
        sm:boxM,
        store:store,
        clicksToEdit:1,
        autoExpandColumn:2,
        disableSelection:true,
        selModel:new Ext.grid.RowSelectionModel({singleSelect:true}),
        viewConfig:{
            forceFit:true,
            enableRowBody:true,
            getRowClass:function(record,rowIndex,p,store){
            return 'x-grid3-row-collapsed';
            }
        },
        tbar:[
            new Ext.Button({
                id:'btnAdd.email',
                text:'新增',
                iconCls:'add',
                handler:function(){
                    grid.stopEditing();
                    grid.getStore().insert(
                        0,
                        new record({
                            email:'',
                            flag:2
                        })
                    );
                    grid.startEditing(0,0);
                }
            }),
            {xtype:"tbseparator"},
            new Ext.Button ({
                id : 'btnRemove.email',
                text : '删除',
                iconCls : 'remove',
                handler : function() {
                    show_delete(grid,store);         //删除 表格 的 一 行 或多行
                }
            }),
            {xtype:"tbseparator"},
            new Ext.Button({
                id:'btnSave.email',
                text:'保存',
                iconCls:'save',
                handler:function(){
                    show_insert(grid,store);     //连接到 新增 面板
                }
            })
        ],
        view:new Ext.grid.GroupingView({
            forceFit:true,
            groupingTextTpl:'{text}({[values.rs.length]}条记录)'
        }),
        bbar:page_toolbar
    });
    var win = new Ext.Window({
        title:'收件人列表',
        width:510,height:333,
        frame:true,modal:true,
        items:[grid]
    }).show();
}

function show_url_flag(value){
   if(value==2){
        return "<a href='javascript:;' onclick='show_update()' style='color: green;'>修改信息</a>";
   }
}
function show_insert(grid,store){
    var selModel = grid.getSelectionModel();
    var count = selModel.getCount();
    if(count==0){
        Ext.MessageBox.show({
        	title:'信息',
        	width:200,
        	msg:'您没有勾选任何记录!',
        	animEl:'btnSave.email',
        	buttons:{'ok':'确定'},
        	icon:Ext.MessageBox.QUESTION,
        	closable:false
		});

    }else if(count > 0){
        var emailArray = new Array();
        var record = grid.getSelectionModel().getSelections();
        for(var i = 0; i < record.length; i++){
            emailArray[i] = record[i].get('email');
        }
        Ext.MessageBox.show({
            title:'信息',
            width:230,
            msg:'确定要保存所选的所有记录?',
            animEl:'btnSave.email',
            buttons:{'ok':'确定','no':'取消'},
            icon:Ext.MessageBox.WARNING,
            closable:false,
            fn:function(e){
                if(e=='ok'){
                    var myMask = new Ext.LoadMask(Ext.getBody(),{
                        msg:'正在保存,请稍后...',
                        removeMask:true
                    });
                    myMask.show();
                    Ext.Ajax.request({
                        url : '../../AlertManagerAction_insertEmail.action',
                        params :{emailArray : emailArray },
                        success : function(r,o){
                            var respText = Ext.util.JSON.decode(r.responseText);
                            var msg = respText.msg;
                            myMask.hide();
                            Ext.MessageBox.show({
                                title:'信息',
                                width:300,
                                msg:msg,
                                animEl:'btnSave.email',
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
    }
}

function show_delete(grid,store){
    var selModel = grid.getSelectionModel();
    var count = selModel.getCount();
    if(count==0){
    	Ext.MessageBox.show({
        	title:'信息',
        	width:200,
        	msg:'您没有勾选任何记录!',
        	animEl:'btnRemove.email',
        	buttons:{'ok':'确定'},
        	icon:Ext.MessageBox.QUESTION,
        	closable:false
		});

    }else if(count > 0){
        var emailArray = new Array();
        var record = grid.getSelectionModel().getSelections();
        for(var i = 0; i < record.length; i++){
            emailArray[i] = record[i].get('email');
        }
        Ext.MessageBox.show({
            title:'信息',
            width:200,
            msg:'确定要删除所选的所有记录?',
            animEl:'btnRemove.email',
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
                        url : '../../AlertManagerAction_deleteEmail.action',    // 删除 连接 到后台
                        params :{emailArray : emailArray },
                        success : function(r,o){
                            var respText = Ext.util.JSON.decode(r.responseText);
                            var msg = respText.msg;
                            myMask.hide();
                            Ext.MessageBox.show({
                                title:'信息',
                                width:300,
                                msg:msg,
                                animEl:'btnRemove.email',
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
    }
}

function show_update(){
    var grid = Ext.getCmp("grid.email.info");//获取对应grid
    var store = grid.getStore();
    var selModel = grid.getSelectionModel();
    var formPanel;
    if(selModel.hasSelection()){
        var selections = selModel.getSelections();
        Ext.each(selections,function(item){
            formPanel = new Ext.form.FormPanel({
                defaultType : 'textfield',
                labelWidth:110,
                frame:true,
                loadMask : { msg : '正在加载数据，请稍后.....' },
                height : 100,
                labelAlign:'right',
                defaults : {
                    width : 150,
                    allowBlank : false,
                    blankText : '该项不能为空！'
                },
				items : [{
                        id:'email.update.info',
                        fieldLabel:"Email地址",
                        name:'email',
                        value:item.data.email,
                        regex:/^\w+[\w.]*@[\w.]+\.\w+$/,
                        regexText:'请输入(例:xiaom@hzih.com)',
                        emptyText:'请输入(例:xiaom@hzih.com)',
                        listeners:{
                            blur:function(){
                                var email = this.getValue();
                                var oldEmail = item.data.email;
                                if(email!=oldEmail) {
                                    var myMask = new Ext.LoadMask(Ext.getBody(),{
                                        msg:'正在校验,请稍后...',
                                        removeMask:true
                                    });
                                    myMask.show();
                                    Ext.Ajax.request({
                                        url:'../../AlertManagerAction_checkEmail.action',
                                        method:'POST',
                                        params:{email:email},
                                        success:function(action){
                                            var json = Ext.decode(action.responseText);
                                            myMask.hide();
                                            if(json.msg != '0000'){
                                                Ext.MessageBox.show({
                                                    title:'信息',
                                                    msg:json.msg,
                                                    animEl:'email.update.info',
                                                    buttons:{'ok':'确定'},
                                                    icon:Ext.MessageBox.WARNING,
                                                    fn:function(e){
                                                        if(e=='ok'){
                                                            Ext.getCmp('email.update.info').setValue('');
                                                        }
                                                    }
                                                });
                                            }
                                        }
                                    });
                                }
                            }
                        }
                    },
                    {
                        id:'oldEmail.info',
                        xtype:'hidden',
                        name:'oldEmail',
                        value:item.data.email
                    }

                ]
            });
        });
    }
    var win = new Ext.Window({
        title:"Email地址-修改信息",
        width:320,
        height:120,
        layout:'fit',
        modal:true,
        items: [formPanel],
        bbar:[
            new Ext.Toolbar.Fill(),
            new Ext.Button ({
            	id:'update.email.info',
                text : '修改',
                formBind:true,
                allowDepress : false,
                handler : function() {
                    if (formPanel.form.isValid()) {
                        var email = Ext.getCmp('email.update.info').getValue();
                        var oldEmail = Ext.getCmp('oldEmail.info').getValue();
                        if(email!=oldEmail) {
                            Ext.MessageBox.show({
                                title:'信息',
                                width:200,
                                msg:'确定要修改以上内容?',
                                animEl:'update.email.info',
                                buttons:{'ok':'确定','no':'取消'},
                                icon:Ext.MessageBox.WARNING,
                                closable:false,
                                fn:function(e){
                                    if(e=='ok'){
                                        formPanel.getForm().submit({
                                            url :'../../AlertManagerAction_updateEmail.action',
                                            method :'POST',
                                            waitTitle :'系统提示',
                                            waitMsg :'正在保存...',
                                            success : function(form,action) {
                                                Ext.MessageBox.show({
                                                    title:'信息',
                                                    width:300,
                                                    msg:action.result.msg,
                                                    animEl:'update.email.info',
                                                    buttons:{'ok':'确定'},
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
                                    }
                                }
                            });
                        } else {
                            Ext.MessageBox.show({
                                title:'信息',
                                width:200,
                                msg:'请填修改后再提交!',
                                animEl:'update.email.info',
                                buttons:{'ok':'确定'},
                                icon:Ext.MessageBox.WARNING,
                                closable:false
                            });
                        }
                    } else {
                        Ext.MessageBox.show({
       					   title:'信息',
       					   width:200,
       					   msg:'请填写完成再提交!',
       					   animEl:'update.email.info',
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
    });
    win.show();
}