Ext.onReady(function () {
    Ext.BLANK_IMAGE_URL = '../../js/ext/resources/images/default/s.gif';
    Ext.QuickTips.init();
    Ext.form.Field.prototype.msgTarget = 'side';

    var formPanel = new Ext.form.FormPanel({
        plain: true,
        labelAlign: 'right',
        labelWidth: 100,
        defaultType: 'textfield',
        defaults: {
            allowBlank: false,
            blankText: '该项不能为空!'
        },
        reader: new Ext.data.JsonReader({}, [
            {name: 'RYSBH_XS'},
            {name: 'SJBH_XS'},
            {name: 'XM_XS'},
            {name: 'XB_XS'},
            {name: 'MZ_XS'},
            {name: 'CSRQ_XS'},
            {name: 'SWYY_XS'},
            {name: 'SWSJ_XS'},
            {name: 'SFHM_XS'},
            {name: 'HH_XS'},
            {name: 'HKLX_XS'},
            {name: 'YHZGX_XS'},
            {name: 'JGGJ_XS'},
            {name: 'JGSSX_XS'},
            {name: 'ZPXH_XS'},
            {name: 'ZZPBS_XS'},
            {name: 'URL_XS'},
            {name: 'SSXQ_XS'},
            {name: 'PCSBM_XS'},
            {name: 'GDSJ_XS'},
            {name: 'GLZT_XS'},

            {name: 'RYSBH_TJ'},
            {name: 'SJBH_TJ'},
            {name: 'XM_TJ'},
            {name: 'XB_TJ'},
            {name: 'MZ_TJ'},
            {name: 'CSRQ_TJ'},
            {name: 'SWYY_TJ'},
            {name: 'SWSJ_TJ'},
            {name: 'SFHM_TJ'},
            {name: 'HH_TJ'},
            {name: 'HKLX_TJ'},
            {name: 'YHZGX_TJ'},
            {name: 'JGGJ_TJ'},
            {name: 'JGSSX_TJ'},
            {name: 'ZPXH_TJ'},
            {name: 'ZZPBS_TJ'},
            {name: 'URL_TJ'},
            {name: 'SSXQ_TJ'},
            {name: 'PCSBM_TJ'},
            {name: 'GDSJ_TJ'},
            {name: 'GLZT_TJ'}
        ]),
        items: [
            {
                xtype: 'fieldset',
                title: '显示字段',
                items: [
                    new Ext.form.CompositeField({
                        hideLabel: true,
                        items: [
                            {boxLabel: '人员识别号',xtype: 'checkbox',name: 'RYSBH_XS',inputValue:"1"},
                            {boxLabel: '事件编号',xtype: 'checkbox', name: 'SJBH_XS',inputValue:"1"},
                            {boxLabel: '姓名',xtype: 'checkbox',name: 'XM_XS',inputValue:"1"},
                            {boxLabel: '性别',xtype: 'checkbox', name: 'XB_XS',inputValue:"1"},
                            {boxLabel: '民族',xtype: 'checkbox', name: 'MZ_XS',inputValue:"1"},
                            {boxLabel: '死亡日期',xtype: 'checkbox', name: 'CSRQ_XS',inputValue:"1"},
                            {boxLabel: '死亡原因',xtype: 'checkbox',name: 'SWYY_XS',inputValue:"1"},
                            {boxLabel: '死亡时间',xtype: 'checkbox', name: 'SWSJ_XS',inputValue:"1"},
                            {boxLabel: '身份证号',xtype: 'checkbox', name: 'SFHM_XS',inputValue:"1"},
                            {boxLabel: '户号', xtype: 'checkbox',name: 'HH_XS',inputValue:"1"},
                            {boxLabel: '户口类型', xtype: 'checkbox',name: 'HKLX_XS',inputValue:"1"}
                        ]
                    }),
                    new Ext.form.CompositeField({
                        hideLabel: true,
                        items: [
                            {boxLabel: '与户主关系',xtype: 'checkbox', name: 'YHZGX_XS',inputValue:"1"},
                            {boxLabel: '籍贯国籍',xtype: 'checkbox', name: 'JGGJ_XS',inputValue:"1"},
                            {boxLabel: '籍贯省市县',xtype: 'checkbox', name: 'JGSSX_XS',inputValue:"1"},
                            {boxLabel: '照片序号',xtype: 'checkbox', name: 'ZPXH_XS',inputValue:"1"},
                            {boxLabel: '证件照标识',xtype: 'checkbox', name: 'ZZPBS_XS',inputValue:"1"},
                            {boxLabel: '照片地址',xtype: 'checkbox', name: 'URL_XS',inputValue:"1"},
                            {boxLabel: '所属县区',xtype: 'checkbox', name: 'SSXQ_XS',inputValue:"1"},
                            {boxLabel: '派出所编码',xtype: 'checkbox', name: 'PCSBM_XS',inputValue:"1"},
                            {boxLabel: '古代时间',xtype: 'checkbox', name: 'GDSJ_XS',inputValue:"1"},
                            {boxLabel: '管理状态',xtype: 'checkbox', name: 'GLZT_XS',inputValue:"1"}
                        ]
                    })]
            }, {
                xtype: 'fieldset',
                title: '条件字段',
                items: [
                    new Ext.form.CompositeField({
                    hideLabel: true,
                    items: [
                        {boxLabel: '人员识别号',xtype: 'checkbox',name: 'RYSBH_TJ',inputValue:"1"},
                        {boxLabel: '事件编号',xtype: 'checkbox', name: 'SJBH_TJ',inputValue:"1"},
                        {boxLabel: '姓名',xtype: 'checkbox',name: 'XM_TJ',inputValue:"1"},
                        {boxLabel: '性别',xtype: 'checkbox', name: 'XB_TJ',inputValue:"1"},
                        {boxLabel: '民族',xtype: 'checkbox', name: 'MZ_TJ',inputValue:"1"},
                        {boxLabel: '死亡日期',xtype: 'checkbox', name: 'CSRQ_TJ',inputValue:"1"},
                        {boxLabel: '死亡原因',xtype: 'checkbox',name: 'SWYY_TJ',inputValue:"1"},
                        {boxLabel: '死亡时间',xtype: 'checkbox', name: 'SWSJ_TJ',inputValue:"1"},
                        {boxLabel: '身份证号',xtype: 'checkbox', name: 'SFHM_TJ',inputValue:"1"},
                        {boxLabel: '户号', xtype: 'checkbox',name: 'HH_TJ',inputValue:"1"},
                        {boxLabel: '户口类型', xtype: 'checkbox',name: 'HKLX_TJ',inputValue:"1"}
                    ]
                }),
                    new Ext.form.CompositeField({
                        hideLabel: true,
                        items: [
                            {boxLabel: '与户主关系',xtype: 'checkbox', name: 'YHZGX_TJ',inputValue:"1"},
                            {boxLabel: '籍贯国籍',xtype: 'checkbox', name: 'JGGJ_TJ',inputValue:"1"},
                            {boxLabel: '籍贯省市县',xtype: 'checkbox', name: 'JGSSX_TJ',inputValue:"1"},
                            {boxLabel: '照片序号',xtype: 'checkbox', name: 'ZPXH_TJ',inputValue:"1"},
                            {boxLabel: '证件照标识',xtype: 'checkbox', name: 'ZZPBS_TJ',inputValue:"1"},
                            {boxLabel: '照片地址',xtype: 'checkbox', name: 'URL_TJ',inputValue:"1"},
                            {boxLabel: '所属县区',xtype: 'checkbox', name: 'SSXQ_TJ',inputValue:"1"},
                            {boxLabel: '派出所编码',xtype: 'checkbox', name: 'PCSBM_TJ',inputValue:"1"},
                            {boxLabel: '古代时间',xtype: 'checkbox', name: 'GDSJ_TJ',inputValue:"1"},
                            {boxLabel: '管理状态',xtype: 'checkbox', name: 'GLZT_TJ',inputValue:"1"}
                        ]
                    })
                ]
            }
        ],
        buttons: [
            '->',
            {
                id: 'insert_win.info',
                text: '保存配置',
                handler: function () {
                    if (formPanel.form.isValid()) {
                        formPanel.getForm().submit({
                            url: "../../InterfaceAction_saveField.action",
                            method: 'POST',
                            waitTitle: '系统提示',
                            waitMsg: '正在连接...',
                            success: function () {
                                Ext.MessageBox.show({
                                    title: '信息',
                                    width: 250,
                                    msg: '保存成功,点击返回页面!',
                                    buttons: Ext.MessageBox.OK,
                                    buttons: {'ok': '确定'},
                                    icon: Ext.MessageBox.INFO,
                                    closable: false
                                });
                            },
                            failure: function () {
                                Ext.MessageBox.show({
                                    title: '信息',
                                    width: 250,
                                    msg: '保存失败，请与管理员联系!',
                                    buttons: Ext.MessageBox.OK,
                                    buttons: {'ok': '确定'},
                                    icon: Ext.MessageBox.ERROR,
                                    closable: false
                                });
                            }
                        });
                    } else {
                        Ext.MessageBox.show({
                            title: '信息',
                            width: 200,
                            msg: '请填写完成再提交!',
                            buttons: Ext.MessageBox.OK,
                            buttons: {'ok': '确定'},
                            icon: Ext.MessageBox.ERROR,
                            closable: false
                        });
                    }
                }
            }
        ]
    });

    new Ext.Viewport({
        layout: 'fit',
        renderTo: Ext.getBody(),
        autoScroll: true,
        items: [{
            frame: true,
            autoScroll: true,
            items: [formPanel]
        }]
    });

    // 加载配置数据
    if (formPanel) {
        formPanel.getForm().load({
            url: "../../InterfaceAction_findField.action",
            success: function (form, action) {

            },
            failure: function (form, action) {
                //Ext.Msg.alert('错误', '加载数据出错！');
            }
        });
    }
});


