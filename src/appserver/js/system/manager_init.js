/**
 * Created by IntelliJ IDEA.
 * User: 钱晓盼
 * Date: 11-10-3
 * Time: 下午2:35
 * 平台初始化
 */
Ext.onReady(function(){
    Ext.BLANK_IMAGE_URL = '../../js/ext/resources/images/default/s.gif';
    Ext.QuickTips.init();
    Ext.form.Field.prototype.msgTarget ='side';

    var record = new Ext.data.Record.create([
        {name:'privated',  mapping:'privated'},
        {name:'ip',        mapping:'ip'},
        {name:'port',      mapping:'port'},
        {name:'protocol', mapping:'protocol'},
        {name:'channelValue', mapping:'channelValue'},
        {name:'tIp',       mapping:'tIp'},
        {name:'tPort',     mapping:'tPort'},
        {name:'auditPort',mapping:'auditPort'},
        {name:'count',     mapping:'count'},
        {name:'size',      mapping:'size'}
    ]);
    var proxy = new Ext.data.HttpProxy({
        url:"../../InitAction_select.action"
    });
    var reader = new Ext.data.JsonReader({
        totalProperty:'total',
        root:'rows'
    },record);
    var store = new Ext.data.Store({
        proxy : proxy,
        reader : reader
    });

    var button = new Ext.Button({
        id:'refresh.info',
        text:'保存',
        handler:function(){
            if(Ext.getCmp('2.channel.info').isVisible()){        //可见
                if(formPanel.form.isValid()
                        &&formPanel_1.form.isValid() ){
                    var privated = Ext.getCmp('privated.info').getValue();
                    /*var protocol = Ext.getCmp('2.protocol.info').getValue();
                    var ip = Ext.getCmp('2.ip.info').getValue();
                    var port = Ext.getCmp('2.port.info').getValue();
                    var tIp = Ext.getCmp('2.tIp.info').getValue();
                    var tPort = Ext.getCmp('2.tPort.info').getValue();
                    var channelValue = Ext.getCmp('2.channelValue.info').getValue();*/
                    formPanel_1.getForm().submit({
                        url:'../../InitAction_update.action',
                        method:'POST',
//                        params:{privated:privated,ip:ip,port:port,tIp:tIp,tPort:tPort,protocol:protocol,channelValue:channelValue},
                        params:{privated:privated},
                        success: function(form,action) {
                            var flag = action.result.msg;
                            Ext.Msg.show({
                                title:'信息',
                                msg:flag,
                                width:300,
                                animEl:'refresh.info',
                                buttons:{'ok':'确定'},
                                icon:Ext.MessageBox.INFO,
                                closable:false,
                                fn:function(e){
                                    if(e=='ok'){
                                        store.reload();
                                        location.reload();
                                    }
                                }
                            });
                        }
                    });
                }else{
                    Ext.Msg.show({
                        title:'信息',
                        msg:'请填写完整!',
                        animEl:'refresh.info',
                        buttons:{'ok':'确定'},
                        icon:Ext.MessageBox.ERROR,
                        closable:false
                    });
                }
            } else {
                if(formPanel_1.form.isValid()){
                    formPanel_1.getForm().submit({
                        url:'../../InitAction_update.action',
                        method:'POST',
                        success: function(form,action) {
                            var flag = action.result.msg;
                            Ext.Msg.show({
                                title:'信息',
                                msg:flag,
                                width:300,
                                animEl:'refresh.info',
                                buttons:{'ok':'确定'},
                                icon:Ext.MessageBox.INFO,
                                closable:false,
                                fn:function(e){
                                    if(e=='ok'){

                                    }
                                }
                            });
                        }
                    });
                }else{
                    Ext.Msg.show({
                        title:'信息',
                        msg:'请填写完整!',
                        animEl:'refresh.info',
                        buttons:{'ok':'确定'},
                        icon:Ext.MessageBox.ERROR,
                        closable:false
                    });
                }
            }
        }
    });
    var formPanel = new Ext.form.FormPanel({
        plain:true,
        labelAlign:'right',
        labelWidth:130,
        defaultType:'textfield',
        defaults:{
            width:200,
            allowBlank:false,
            blankText:'该项不能为空!'
        },
        items:[{
            id:'privated.info',
            xtype:'displayfield',
            fieldLabel:'指定本机为'
        },{
            id:'privated.hidden.info',
            name:'channel.privated',
            xtype:'hidden'
        }]
    });

    var formPanel_1 = new Ext.form.FormPanel({
        plain:true,
        labelAlign:'right',
        labelWidth:130,
        defaultType:'textfield',
        defaults:{
            width:200,
            allowBlank:false,
            blankText:'该项不能为空!'
        },
        items:[{
            id:'1.channelValue.info',
            xtype:'hidden',
            name:'channel.channelValue'
        },{
            id:'1.protocol.info',
            xtype:'hidden',
            name:'channel.protocol'
        },{
            id:'1.ip.info',
            fieldLabel:'源端IP地址',
            xtype:'displayfield'
        },{
            id:'1.ip.hidden.info',
            xtype:'hidden',
            name:'channel.ip'
        },{
            id:'1.port.info',
            xtype:'hidden',
            name:'channel.port',
            value:6666
        },{
            id:'1.tIp.info',
            fieldLabel:'目标IP地址',
            xtype:'displayfield'
        },{
            id:'1.tIp.hidden.info',
            xtype:'hidden',
            name:'channel.tIp'
        },{
            id:'1.tPort.info',
            xtype:'hidden',
            name:'channel.tPort',
            value:6666
        },{
            id:'1.auditPort.info',
            xtype:'hidden',
            name:'channel.auditPort',
            value:6667
        },{
            id:'1.count.info',
            fieldLabel:'审计发送文件条数',
            name:'channel.count',
            regex:/^(5000|[1-4][0-9]{3}|[1-9][0-9]{0,2})$/,
		    regexText:'这个不是100~5000之间的数字',
		    emptyText:'请输入100~5000'
        },{
            id:'1.size.info',
            fieldLabel:'审计发送文件大小',
            xtype:'displayfield',
            value:'1 MB'
        },{
            xtype:'hidden',
            name:'channel.size',
            value:1
        }]
    });

    var formPanel_2 = new Ext.form.FormPanel({
        plain:true,
        labelAlign:'right',
        labelWidth:130,
        defaultType:'textfield',
        defaults:{
            width:200,
            allowBlank:false,
            blankText:'该项不能为空!'
        },
        items:[{
            id:'2.channelValue.info',
            xtype:'hidden',
            name:'channel.channelValue'
        },{
            id:'2.protocol.info',
            xtype:'hidden',
            name:'channel.protocol'
        },{
            id:'2.ip.info',
            fieldLabel:'源端IP地址',
            xtype:'displayfield'
        },{
            id:'2.ip.hidden.info',
            name:'channel.ip',
            xtype:'hidden'
        },{
            id:'2.port.info',
            xtype:'hidden',
            name:'channel.port',
            value:6666
        },{
            id:'2.tIp.info',
            fieldLabel:'目标IP地址',
            xtype:'displayfield'
        },{
            id:'2.tIp.hidden.info',
            xtype:'hidden',
            name:'channel.tIp'
        },{
            id:'2.tPort.info',
            xtype:'hidden',
            name:'channel.tPort',
            value:6666
        }]
    });

    var panel = new Ext.Panel({
        plain:true,
        width:420,
        border:false,
        items:[{
            xtype:'fieldset',
            title:'平台指定',
            width:400,
            items:[formPanel]
        },{
            xtype:'fieldset',
            title:'通道',
            width:400,
            items:[formPanel_1]
        /*},{
            id:'2.channel.info',
            xtype:'fieldset',
            title:'通道二',
            hidden: true,
            width:400,
            items:[formPanel_2]*/
        }]
    });
    var about = new Ext.Panel({
        plain:true,
        width:420,
        border:false,
        items:[{
            xtype:'fieldset',
            title:'说明',
            width:400,
            html:'<font color="green"> 1.源端端口默认为6666;<br>2.目标端端口默认为6666;<br>3.审计端口默认为6667.</font>'
        }]
    });
    var panel2 = new Ext.Panel({
        plain:true,
        width:setWidth(),
        border:false,
        buttonAlign :'left',
        buttons:[
            new Ext.Toolbar.Spacer({width:130}),
            button
        ]
    });
    new Ext.Viewport({
    	layout :'fit',
    	renderTo:Ext.getBody(),
        autoScroll:true,
        items:[{frame:true,autoScroll:true,items:[{layout:'column',plain:true,autoScroll:true,items:[panel,about]},panel2]}]
    });
    store.load();
	store.on('load',function(){
        var channelValue0 = store.getAt(0).get('channelValue');
//        var channelValue1 = store.getAt(1).get('channelValue');
        var index1 = 0;
        /*var index2;
        if(channelValue0==1){
            index1 = 0;
            index2 = 1;
        } else {
            index1 = 1;
            index2 = 0;
        }
		var count = store.getCount();*/

        /*if(count==1){
            Ext.getCmp('2.channel.info').hide();
        } else if(count>1){
            Ext.getCmp('2.channel.info').show();
            Ext.getCmp('2.channelValue.info').setValue(store.getAt(index2).get('channelValue'));
            Ext.getCmp('2.protocol.info').setValue(store.getAt(index2).get('protocol'));
            Ext.getCmp('2.ip.info').setValue(store.getAt(index2).get('ip'));
            Ext.getCmp('2.ip.hidden.info').setValue(store.getAt(index2).get('ip'));
            Ext.getCmp('2.tIp.info').setValue(store.getAt(index2).get('tIp'));
            Ext.getCmp('2.tIp.hidden.info').setValue(store.getAt(index2).get('tIp'));
        }*/
        Ext.getCmp('privated.info').setValue(store.getAt(index1).get('privated')?'可信服务端':'非可信服务端');
        Ext.getCmp('privated.hidden.info').setValue(store.getAt(index1).get('privated'));
        Ext.getCmp('1.channelValue.info').setValue(store.getAt(index1).get('channelValue'));
        Ext.getCmp('1.protocol.info').setValue(store.getAt(index1).get('protocol'));
        Ext.getCmp('1.ip.info').setValue(store.getAt(index1).get('ip'));
        Ext.getCmp('1.ip.hidden.info').setValue(store.getAt(index1).get('ip'));
        Ext.getCmp('1.tIp.info').setValue(store.getAt(index1).get('tIp'));
        Ext.getCmp('1.tIp.hidden.info').setValue(store.getAt(index1).get('tIp'));
        Ext.getCmp('1.count.info').setValue(store.getAt(index1).get('count'));
    });
});

function setWidth(){
    return (document.body.clientWidth-215)/2;
}