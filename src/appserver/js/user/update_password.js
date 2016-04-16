Ext.onReady(function(){
    Ext.QuickTips.init();
    Ext.form.Field.prototype.msgTarget="side";
    Ext.apply(Ext.form.VTypes,{
        //验证方法
        password:function(val,field){//val指这里的文本框值，field指这个文本框组件
            if(field.password.password_id){
                //password是自定义的配置参数，一般用来保存另外的组件的id值
                var pwd=Ext.get(field.password.password_id);//取得user_password的那个id的值
                return (val==pwd.getValue());//验证
            }
            return true;
        },
        //验证提示错误提示信息(注意：方法名+Text)
        passwordText: "两次密码输入不一致!"
    });
    var b = new Ext.FormPanel({
        plain:true,title:"",border:false,labelAlign:"right",
        labelWidth:250,width:"100%",waitMsgTarget:true,
        buttonAlign: 'left',
        renderTo:document.body,
        items:[
            new Ext.form.FieldSet({
                title:"修改密码",autoHeight:true,defaultType:"textfield",
                items:[{
                    fieldLabel : '当前密码',
                    inputType:"password",
                    name : 'pwd',
                    id : 'pwd',
                    allowBlank:false,
                    width : 150
                }, {
                    fieldLabel : '输入新密码',
                    inputType:"password",
                    name : 'newpwd',
                    id : 'newpwd',
                    allowBlank:false,
                    width : 150
                }, {
                    fieldLabel : '再次输入新密码',
                    inputType:"password",
                    name : 'rnewpwd',
                    id : 'rnewpwd',
                    width : 150,
                    password:{password_id:'newpwd'},
                    allowBlank:false,
                    vtype:'password'
                }]
            })
        ]
    });
    var c = new Ext.FormPanel({
        plain:true,
        border:false,
        buttonAlign: 'left',
        renderTo:document.body,
        buttons:[new Ext.Toolbar.Spacer({width:250}),{
            text:"保存",
            listeners:{
                click:function(){
                    b.getForm().submit({
                        clientValidation:true,
                        url:"../../AccountAction_updatePwd.action",
                        waitMsg:'正在处理,请稍后...',
                        waitTitle :'系统提示',
                        success:function(form,action){
                            Ext.MessageBox.show({
                                title:'信息',
                                msg:action.result.msg,
                                width:300,
                                buttons:{'ok':'确定'},
                                icon:Ext.MessageBox.INFO,
                                closable:false
                            });
                        }
                    })
                }
            }
        }]
    });
    var a=new Ext.Viewport({
        layout:"fit",
        border:false,
        items:[{frame:true,items:[b,c]}]
    })
});