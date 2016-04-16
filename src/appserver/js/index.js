/**
 * Created by 钱晓盼 on 14-1-24.
 */
Ext.onReady(function() {
    Ext.BLANK_IMAGE_URL = 'js/ext/resources/images/default/tree/s.gif';
    Ext.QuickTips.init();
    Ext.form.Field.prototype.msgTarget = 'side';

    var northBar = new Ext.Toolbar({
        id: 'northBar',
        items: [{
            xtype: 'tbtext',
            id: 'msg_title',
            text: ''
        },{
            xtype: "tbfill"
//        },{
//            id:'warningMsg',
//            iconCls: 'warning',
//            hidden:true,
//            handler:function(){
//                eastPanel.expand(true);
//            }
        },
            '<a id="sethome.info" onclick="SetHome(this,window.location)"></a>'
            ,{
                xtype: 'tbseparator'
            },{
                pressed:false,
                text:'刷新',
                iconCls: 'refresh',
                handler: function(){
                    var activeTab = centerPanel.getActiveTab();
                    if(activeTab == null) {
                        location.reload();
                    } else {
                        var mID = activeTab.getId();
                        if(activeTab.getStateId()==null){
                            window.frames[0].location.reload();
                        }else{
                            window.parent.document.getElementById('frame_'+mID).contentWindow.location.reload();
                        }
                    }
                }
            },{
                xtype: 'tbseparator'
            }/*,{
                text:'加入收藏',
                iconCls: 'favorite',
                handler: function(){
                    AddFavorite(location.href,document.title);
                }
            },{
                xtype: 'tbseparator'
            },{
                pressed:false,
                text:'帮助',
                iconCls: 'help',
                handler: function(){
                    window.open('help.doc');
                }
            }*/,{
                xtype: 'tbseparator'
            },{
                id:'logout.tb.info',
                text:'退出系统',
                iconCls: 'logout',
                handler: function(){
                    logout();
                }
            }
        ]
    });

    //页面的上部分
    var northPanel=new Ext.Panel({
        region : 'north',			//北部，即顶部，上面
//                        contentEl : 'top-div',		//面板包含的内容
        split : false,
        titlebar: false,
        border: false, 				//是否显示边框
        collapsible: false, 		//是否可以收缩,默认不可以收缩，即不显示收缩箭头
        height : 83,
        html:'<div id="top" style="border:1px solid #564b47;background-color:#fff;height:55;width:100%;background-image: url(img/top.png);">' +
            '<div style="height:55;border:0 solid #564b47;float:right;width:400px;margin:0px 0px 0px 0px;background-image: url(img/top_1.png);">' +
            '</div>' +
            '</div>',
        bbar: northBar
    });

    //左边菜单
    var westPanel = new Ext.Panel({
        title : '系统功能',			//面板名称
        region : 'west',			//该面板的位置，此处是西部 也就是左边
        split : true,				//为true时，布局边框变粗 ,默认为false
        titlebar: true,
        collapsible: true,
        animate: true,
        border : true,
        bodyStyle: 'border-bottom: 0px solid;',
        bodyborder: true,
        width : 200,
        minSize : 100,				//最小宽度
        maxSize : 300,
        layout : 'accordion',
        iconCls : 'title-1',
        layoutConfig : { 			//布局
            titleCollapse : true,
            animate : true
        },
        items : [
        ]
    });

    //页面的中间面板
    var centerPanel = new Ext.TabPanel({
        id: 'mainContent',
        region: 'center',
        deferredRender: false,
        enableTabScroll: true,
        activeTab: 0,
        items: [/*{
            title:'系统状态',
            html:'系统状态'
//            html: '<iframe id="frame_0" width="100%" height="100%" frameborder="0" src="pages/monitor/monitor_system.jsp"></iframe>'
        }*/]
    });
    centerPanel.activate(0);


    new Ext.Viewport({
        layout: 'border',
        loadMask: true,
        items: [northPanel, 	//上
            westPanel,  		//左
            centerPanel		    //中
        ]
    });
    var userName = getJspValue('userName.account.info');
    northBar.get(0).setText("您好！" + userName + " 欢迎登录!");

    reLoadWestPanel(westPanel);

    checkTimeOut();


});

/**
 * 获取jsp页面参数
 * @param id
 * @returns {panel.value|*|value|formPanel.value|ping_formPanel.value|formPanel_1.value}
 */
function getJspValue(id){
    return document.getElementById(id).value;
}

/**
 * 加载菜单
 * @param westPanel
 */
function reLoadWestPanel (westPanel) {
    Ext.Ajax.request({
        url: 'AccountAction_indexPermission.action',
        success: function(response){
            var result = Ext.util.JSON.decode(response.responseText);
            var total = result.total;
            for(var i = 0; i < total; i ++) {
                var row = result.rows[i];
                var p = new Ext.Panel({
                    id:'tree-menu-' + row.id,
                    title: row.title,
                    border: false,
                    iconCls: row.iconCls,
                    bodyStyle: 'border-bottom: 1px solid;padding-top: 5px;padding-left: 15px;',
                    autoScroll: true,
                    items: []
                });
                var menu_root_node = new Ext.tree.TreeNode({
                    text : row.title,
                    expanded : false
                });
                var sun = row.sun;
                var count = sun.length;
                for(var j = 0 ; j < count; j ++) {
                    var mrn_1 = new Ext.tree.TreeNode({
                        id: 'mrn_' + i + '_'+j,
                        text: sun[j].title,
                        leaf: true ,
                        url: sun[j].url
                    }) ;
                    menu_root_node.appendChild(mrn_1);
                }
                var tree_menu = new Ext.tree.TreePanel({
                    border: false,
                    root: menu_root_node,
                    rootVisible: false,
                    listeners: {
                        click: nodeClick
                    }
                });
                p.add(tree_menu);
                westPanel.add(p);
                westPanel.doLayout();
            }
        }
    });
}

/**
 * 打开链接
 * @param node
 * @param e
 */
function nodeClick(node, e){
    var centerPanel = Ext.getCmp('mainContent');
    if (node.isLeaf()){
        var _url = node.attributes.url ;
        if (_url != ''){
            if(_url.indexOf('?')>0)
                _url += '&time=' + new Date().getTime();
            else
                _url += '?time=' + new Date().getTime();
        }
        var _tab = centerPanel.getComponent(node.id) ;
        if(!_tab){
            centerPanel.add({
                id: node.id ,
                title: node.text ,
                closable: true ,
                iconCls: node.attributes.iconCls,
                html: '<iframe id="frame_'+node.id+'" width="100%" height="100%" frameborder="0" src="'+_url+'"></iframe>',
                listeners:{
                    show:function(){
                        var mID = centerPanel.getActiveTab().getId();
                        if(centerPanel.getActiveTab().getStateId()==null){
                            window.frames[0].location.reload();
                        }else{
                            if(window.parent.document.getElementById('frame_'+mID)!=null){
                                window.parent.document.getElementById('frame_'+mID).contentWindow.location.reload();
                            }
                        }
                    }
                }
            }) ;
        }
        centerPanel.setActiveTab(node.id) ;
    }
}

/**
 * 设置网站为首页
 * @param obj
 * @param vrl
 * @constructor
 */
function SetHome(obj,vrl){
    try {
        obj.style.behavior='url(#default#homepage)';obj.setHomePage(vrl);
    } catch(e){
        if(window.netscape) {
            try {
                netscape.security.PrivilegeManager.enablePrivilege("UniversalXPConnect");
            }
            catch (e) {
                alert("此操作被浏览器拒绝！\n请在浏览器地址栏输入“about:config”并回车\n然后将[signed.applets.codebase_principal_support]设置为'true'");
            }
            var prefs = Components.classes['@mozilla.org/preferences-service;1'].getService(Components.interfaces.nsIPrefBranch);
            prefs.setCharPref('browser.startup.homepage',vrl);
        }
    }
}

/**
 * 设置加入收藏夹
 * @param sURL
 * @param sTitle
 * @constructor
 */
function AddFavorite(sURL, sTitle){
    try {
        window.external.addFavorite(sURL, sTitle);
    } catch (e) {
        try {
            window.sidebar.addPanel(sTitle, sURL, "");
        } catch (e) {
            alert("加入收藏失败，请使用Ctrl+D进行添加");
        }
    }
}

/**
 * 退出系统
 */
function logout(){
    Ext.Msg.confirm("确认","确认退出系统吗？",function(btn){
        if (btn == 'yes') {
            window.location = "logout.action";
        }else{
            return false;
        }
    });
}


var idx = 0;
var idx2 = 0;
/**
 * 超时校验和告警检查
 */
function checkTimeOut() {
    var soundManager = new SoundManager();
    soundManager.debugMode = false;
    soundManager.url = 'sound/swf';
    soundManager.beginDelayedInit();
    soundManager.onload = function() {
        soundManager.createSound({
            id: 'msgSound',
            url: 'sound/mp3/msg.mp3'
        });
    }
    //检查会话是否超时
    idx = 0;
    idx2 = 0;
    var task = {
        run : function() {
            Ext.Ajax.request({
                url: 'checkTimeout.action',
                success: function(response){
                    var result = response.responseText;
                    if(result!=null&&result.length>0){
                        alert("会话过期，请重新登录");
//                        parent.location.href="login.jsp";
                        window.location = "logout.action";
                    }
                }
            });
            if(idx==0){
                Ext.Ajax.request({
                    url: 'AlertAction_refreshAlerts.action',
                    success: function(response){
                        var result = Ext.util.JSON.decode(response.responseText);
                        if(result.device>0||result.business>0||result.security>0){
                            var qq = new Ext.ux.ToastWindow({
                                title: '报警提示',
                                html: result.time + ' 收到' + result.device+ '条设备报警信息，<br/>'+
                                    result.time+' 收到'+result.business+'条业务报警信息，<br/>'+
                                    result.time+ ' 收到'+result.security+'条安全报警信息<br>' +
                                    '<a href="javascript:void(0);" onclick="cacheFresh();">暂时不刷新</a>',
                                iconCls: 'bjgl',
                                autoShow:true
                            });
                            qq.animShow();
                            soundManager.play('msgSound');
                        }
                    }
                });

            } else if (idx>0&&idx<20){
                idx ++;
            }else if(idx>=20){
                idx = 0;
            }
            if(idx2==0){
                Ext.Ajax.request({
                    url: 'AlertAction_refreshDiskUseAlerts.action',
                    success: function(response){
                        var result = Ext.util.JSON.decode(response.responseText);
                        if(result.alert == 1){
                            var qq = new Ext.ux.ToastWindow({
                                title: '报警提示',
                                html: result.time+' 收到'+result.alert+'条报警信息:审计库容量达到警戒值'+
                                    result.dbUsed+'，请看[报警阀值设置]<br/>'+
                                    result.diskMsg+'<br><a href="javascript:void(0);" onclick="cacheFresh2();">暂时不刷新</a>',
                                iconCls: 'bjgl',
                                autoShow:true
                            });
                            qq.animShow();
                            soundManager.play('msgSound');
                        }else if(result.alert == 2){
                            alert("会话过期，请重新登录");
//                            parent.location.href="login.jsp";
                            window.location = "logout.action";
                        }
                    }
                });
            } else if (idx>20&&idx2<20){
                idx2 ++;
            }else if(idx2>=20){
                idx2 = 0;
            }
        },
        interval : 10000
    };
    Ext.TaskMgr.start(task);

}
function cacheFresh(){
   idx ++;
   return idx;
}

function cacheFresh2(){
   idx2 ++;
   return idx2;
}



