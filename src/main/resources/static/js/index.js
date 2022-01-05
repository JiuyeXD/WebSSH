var client;
var term;



function openTerminal(options) {
    client = new WSSHClient();
    term = new Terminal({
        rendererType: "canvas", //渲染类型
        cols: parseInt($("#terminal").width() / 8.25), //列数
        rows: parseInt($("#terminal").height() / 18),//行数
        cursorBlink: true, // 光标闪烁
        cursorStyle: "block", // 光标样式  null | 'block' | 'underline' | 'bar'
        scrollback: 10240, //回滚
        tabStopWidth: 8, //制表宽度
        screenKeys: true,
        fontWeight: '600',
        fontWeightBold: '900',
        theme: {
            foreground: "#55eac6", //字体
            background: "#000000", //背景色
            cursor: "help", //设置光标
            lineHeight: 20
        }
    });

    term.open(document.getElementById('terminal'));
    //在页面上显示连接中...
    term.writeln('Connecting...');

    term.onData(function (key) {
        var order = {
            "operate": "command",
            "command": key,
            "cols": parseInt(term.cols),
            "rows": parseInt(term.rows)
        };
        client.send(order);
    });

    window.onresize = function(){
        resizeTerminal();
    }
    //执行连接操作
    client.connect({
        onError: function (error) {
            //连接失败回调
            term.writeln("");
            term.writeln('Error: ' + error + '\r\n');
        },
        onConnect: function () {
            //连接成功回调
            client.sendInitData(options);
        },
        onClose: function () {
            //连接关闭回调
            term.writeln("");
            term.writeln("\rconnection closed");
        },
        onData: function (data) {
            //收到数据时回调
            term.write(data);
        },
        onReconnect: function(times) {
            //重连回调
            term.writeln(ordinal_suffix_of(times)+" reconnecting....");
            term.writeln("");
        },
        onOverReconnect: function(times) {
            alert("Reconnection failed more than "+times+" times.");
        }
    });
    function ordinal_suffix_of(i) {
        var j = i % 10,
            k = i % 100;
        if (j === 1 && k !== 11) {
            return i + "st";
        }
        if (j === 2 && k !== 12) {
            return i + "nd";
        }
        if (j === 3 && k !== 13) {
            return i + "rd";
        }
        return i + "th";
    }
}

// 连接
function connect() {
    var ip = document.getElementById('ip').value;
    var port = document.getElementById('port').value;
    var username = document.getElementById('username').value;
    var pwd = document.getElementById('pwd').value;

    openTerminal({
        operate: 'connect',
        host: ip,
        port: port,
        username: username,
        password: pwd,
    });
    document.getElementById("output").style.display = "block";
    document.getElementById("input").style.display = "none";
    resizeTerminal();
}

// 断开连接
function disconnect(){
    client.send({
        "operate": "command"
        , "command": 'logout\r'
    });
    location.reload();
}

// 大小改变
function resizeTerminal() {
    console.log("width: ",$("#terminal").width()," / height: ",$("#terminal").height());
    var c = parseInt($("#terminal").width() / 8.25);
    var r = parseInt($("#terminal").height() / 18);
    term.resize(c, r);
    client.send({
        "operate": "command"
        , "command": ''
        , "cols": c
        , "rows": r
    });
};