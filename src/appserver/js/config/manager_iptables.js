Ext.onReady(function() {
    Ext.BLANK_IMAGE_URL = '../../js/ext/resources/images/default/s.gif';
    Ext.QuickTips.init();
    Ext.form.Field.prototype.msgTarget = 'side';


    var start = 0;
    var pageSize = 15;

    var resourceRecord = Ext.data.Record.create(
        [
            {name: 'id',mapping:'id'},
            {name:'ip',mapping:'ip'}
        ]);

    var ssh_store = new Ext.data.Store({
        proxy: new Ext.data.HttpProxy({url:'ManagerIptablesAction_select.action',method:'POST'}),

        reader: new Ext.data.JsonReader({
            totalProperty:"total",
            root: 'rows'
        }, resourceRecord)
    });
    ssh_store.load({
        params:{
            type:'ssh'
        }
    });

    var webmin_stroe = new Ext.data.Store({
        proxy: new Ext.data.HttpProxy({url:'ManagerIptablesAction_select.action',method:'POST'}),

        reader: new Ext.data.JsonReader({
            totalProperty:"total",
            root: 'rows'
        }, resourceRecord)
    });
    webmin_stroe.load({
        params:{
            type:'webmin'
        }
    });

    var rowNumber = new Ext.grid.RowNumberer();         //自动 编号
    var resourceCm = new Ext.grid.ColumnModel([
        rowNumber,
//        {header: "编号", width: 50,align:'center', dataIndex: 'id',sortable:true,menuDisabled:true},
        {header: "允许访问IP", width: 50,align:'center', dataIndex: 'ip',sortable:true,menuDisabled:true}
    ]);

    // create the grid
    var ssh_grid = new Ext.grid.GridPanel({
        id:'ssh.iptables',
        store: ssh_store,
        cm: resourceCm,
        title:'SSH防火墙规则',
        viewConfig: {
            forceFit:true
        },
        columnLines: true,
        autoScroll:true,
        loadMask:{msg:'正在加载数据，请稍后...'},
        border:false,
        collapsible:false,
        stripeRows:true,
        autoExpandColumn:'Position',
        enableHdMenu:true,
        enableColumnHide:true,
//        bodyStyle:'width:100%',
        selModel:new Ext.grid.RowSelectionModel({singleSelect:false}),
        height:300,
        frame:true,
        iconCls:'icon-grid',

        // inline toolbars
        tbar:[{
            text: '新增防火墙规则',
            iconCls:'add',
            handler: function(){addresource(ssh_grid,ssh_store,"ssh");}
        },'-',{
            text: '编辑防火墙规则',
            iconCls:'upgrade',
            handler: function(){editresource(ssh_grid,ssh_store,"ssh");}
        },'-',{
            text: '删除防火墙规则',
            iconCls:'remove',
            handler: function(){delresource(ssh_grid,ssh_store,"ssh");}
        },{
            xtype: 'tbseparator'
        },{
                // xtype: 'button',
                text : '应用',
                listeners : {
                    "click" : function() {
                        Ext.Ajax.request({
                            url : 'ManagerIptablesAction_application.action',
                            method:'POST',
                            params:{type:"ssh"},
                            success : function(r,o){
                                var respText = Ext.util.JSON.decode(r.responseText);
                                var msg = respText.msg;
                                Ext.MessageBox.show({
                                    title:'信息',
                                    width:250,
                                    msg:msg,
                                    buttons:{'ok':'确定'},
                                    icon:Ext.MessageBox.INFO,
                                    closable:false,
                                    fn:function(e){
                                        if(e=='ok'){
                                            ssh_grid.render();
                                            ssh_store.reload();
                                        }
                                    }
                                });
                            }
                        });
                    }
                }
            }]
//        bbar:pagebar
    });

    var webmin_grid = new Ext.grid.GridPanel({
        id:'webmin.iptables',
        store: webmin_stroe,
        cm: resourceCm,
        title:'WEBMIN防火墙规则',
        viewConfig: {
            forceFit:true
        },
        columnLines: true,
        autoScroll:true,
        loadMask:{msg:'正在加载数据，请稍后...'},
        border:false,
        collapsible:false,
        stripeRows:true,
        autoExpandColumn:'Position',
        enableHdMenu:true,
        enableColumnHide:true,
//        bodyStyle:'width:100%',
        selModel:new Ext.grid.RowSelectionModel({singleSelect:false}),
        height:300,
        frame:true,
        iconCls:'icon-grid',

        // inline toolbars
        tbar:[{
            text: '新增防火墙规则',
            iconCls:'add',
            handler: function(){addresource(webmin_grid,webmin_stroe,"webmin");}
        },'-',{
            text: '编辑防火墙规则',
            iconCls:'upgrade',
            handler: function(){editresource(webmin_grid,webmin_stroe,"webmin");}
        },'-',{
            text: '删除防火墙规则',
            iconCls:'remove',
            handler: function(){delresource(webmin_grid,webmin_stroe,"webmin");}
        },{
            xtype: 'tbseparator'
        },{
            // xtype: 'button',
            text : '应用',
            listeners : {
                "click" : function() {
                    Ext.Ajax.request({
                        url : 'ManagerIptablesAction_application.action',
                        method:'POST',
                        params:{type:"webmin"},
                        success : function(r,o){
                            var respText = Ext.util.JSON.decode(r.responseText);
                            var msg = respText.msg;
                            Ext.MessageBox.show({
                                title:'信息',
                                width:250,
                                msg:msg,
                                buttons:{'ok':'确定'},
                                icon:Ext.MessageBox.INFO,
                                closable:false,
                                fn:function(e){
                                    if(e=='ok'){
                                        webmin_grid.render();
                                        webmin_stroe.reload();
                                    }
                                }
                            });
                        }
                    });
                }
            }
        }]
//        bbar:pagebar
    });


    var port = new Ext.Viewport({
        layout:'fit',
        renderTo:Ext.getBody(),
        items:[{
            autoScroll:true,
            items:[ssh_grid,webmin_grid]
        }]
    });

    function addresource(grid,store,type){
        var formPanel = new Ext.form.FormPanel({
            frame:true,
            autoScroll:true,
            labelWidth : 120, // label settings here cascade unless overridden
            border:false,
            bodyStyle : 'padding:5px 5px 0',
            width : 500,
            waitMsgTarget : true,
            defaults : {
                width : 230 ,
                allowBlank:false,
                blankText:'该项不能为空'
            },
            defaultType : 'textfield', //
            items:[
                {
                    id:'add.ip',
                    fieldLabel:"允许访问IP",
                    name:'allow_ip',
                    regex:/^((((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9])\.){3}(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[1-9]))|(((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9])\.){3}0\/([0-9]|[1-2][0-9]|3[0-2])))$/,
                    regexText:'这个不是Ip(例:1.1.1.1或1.1.1.0/24)',
                    emptyText:'请输入Ip(例:1.1.1.1或1.1.1.0/24)'
                }]
        });
        var win = new Ext.Window({
            title:'新增防火墙规则',
            width:500,
            layout:'fit',
            height:250,
            modal:true,
            items:formPanel,
            bbar:[
                '->',{
                    id:'insert_win.info',
                    text:'保存',
                    handler:function(){
                        if (formPanel.form.isValid()) {
                            formPanel.getForm().submit({
                                url :'ManagerIptablesAction_insert.action',
                                method :'POST',
                                params:{type:type},
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
                }
            ]
        });
        win.show();
    }

    function editresource(grid,store,type){
        var selModel = grid.getSelectionModel();
        if(selModel.hasSelection()){
            var selections = selModel.getSelections();
            Ext.each(selections,function(item){
                var formPanel = new Ext.form.FormPanel({
                    frame:true,
                    autoScroll:true,
                    labelWidth : 120, // label settings here cascade unless overridden
                    border:false,
                    bodyStyle : 'padding:5px 5px 0',
                    width : 500,
                    waitMsgTarget : true,
                    defaults : {
                        width : 230
//                        allowBlank:false
                    },
                    defaultType : 'textfield', //
                    items:[{
                        id:'update.ip',
                        fieldLabel:"允许访问IP",
                        name:'allow_ip',
                        regex:/^((((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9])\.){3}(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[1-9]))|(((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9])\.){3}0\/([0-9]|[1-2][0-9]|3[0-2])))$/,
                        regexText:'这个不是Ip(例:1.1.1.1或1.1.1.0/24)',
                        emptyText:'请输入Ip(例:1.1.1.1或1.1.1.0/24)',
                        value:item.data.ip
                    }]
                });
                var win = new Ext.Window({
                    title:'修改允许访问IP',
                    width:500,
                    layout:'fit',
                    height:250,
                    modal:true,
                    items:formPanel,
                    bbar:[
                        '->',{
                            id:'update_win.info',
                            text:'保存',
                            handler:function(){
                                if (formPanel.form.isValid()) {
                                    formPanel.getForm().submit({
                                        url :'ManagerIptablesAction_update.action',
                                        method :'POST',
                                        params:{type:type,'old_ip':item.data.ip},
                                        waitTitle :'系统提示',
                                        waitMsg :'正在保存,请稍后...',
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
                    ]
                });
                win.show();
            })
        } else{
            Ext.MessageBox.show({
                title:'信息',
                width:200,
                msg:'请选择一行数据进行修改!',
                animEl:'update_win.info',
                buttons:{'ok':'确定'},
                icon:Ext.MessageBox.INFO,
                closable:false
            });
        }
    }

    function delresource(grid,store,type){
        var selModel = grid.getSelectionModel();
        if(selModel.hasSelection()){
            var selections = selModel.getSelections();
            Ext.each(selections,function(item){
                Ext.MessageBox.show({
                    title:'信息',
                    msg:'<font color="green">确定要删除所选记录？</font>',
                    animEl:'delete_win.info',
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
                                url : 'ManagerIptablesAction_delete.action',             // 删除 连接 到后台
                                params :{'type':type,'allow_ip':item.data.ip},
                                method:'POST',
                                success : function(r,o){
                                    myMask.hide();
                                    var respText = Ext.util.JSON.decode(r.responseText);
                                    var msg = respText.msg;
                                    Ext.MessageBox.show({
                                        title:'信息',
                                        width:250,
                                        msg:msg,
                                        animEl:'delete_win.info',
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

});