/**
 * Created by shihaiyang on 2014/5/6.
 */
/**
 *
 * @param serverResp 服务器端的响应，可选的参数
 *　如果你不想自动检测错误，请使用
 *  new OperResult(serverResp,
 *  {autoCheckErr:false}
 *  );
 * @constructor
 */

(function ($, window) {
    //这是环境配置
    var config = {
        /**
         * 服务器处理是否成功
         */
        success: false,
        /**
         * 服务器返回响应码
         */
        code: null,
        /**
         * 服务器端返回的对返回码的描述
         */
        desc: null,
        /**
         * 服务器端返回响应中携带用户真正数据的地方
         */
        data: null,
        /**
         * 指向用户的原始数据
         */
        orgData: null,
        /**
         * 额外数据:一般都是框架使用，目前放置的是一个指向自动处理错误的url
         */
        extraData: null,
        /**
         * 系统级处理是否成功，例如没权限时这里会为false
         */
        sysSuccess:true

    };

    /**
     * 专门用来处理用户的配置
     * @type {{autoCheckErr: boolean}}
     */
    var innerUserCfg = {
        /**
         * 是否自动检测系统错误，包括超时，无权限
         */
        autoCheckErr: true
    };

    window.OperResult = function (serverResp, userConfig) {
        if (!jQuery) {
            throw new Error("请先引用jquery类");
        }

        if (!(this instanceof window.OperResult)) {
            return new window.OperResult(serverResp, userConfig);
        }

        if (arguments.length == 2 && !userConfig) {
            $.extend(innerUserCfg, userConfig);
        }

        if (serverResp) {
            $.extend(config, serverResp);//复制属性到这里
            config.orgData = serverResp;
            if (!innerUserCfg.autoCheckErr) {
                return this;
            }
            if (!(this.checkError())) {
                return this;
            }
        }

    };

    window.OperResult.prototype = {
        constructor: window.OperResult,
        RET_SUCCESS: 0,//成功
        RET_TIME_OUT: -10,//超时，或者没登录
        RET_NOT_AUTHORITY: -20,//没有权限
        RET_SYSTEM_ERR: 10, //ajax中你没有捕获异常
        RET_UNKNOWN_ERR: 20, //未知错误

        /**
         * 用于循环数据
         * @param callback 用于做调时使用的函数
         * @param ctx 上下文，1如果你要循环的值就是data(即他就是一个数组)，你可以不写ctx这个参数
         *                  2 如果你要循环的值是data中的某一个，例如是data.nodes，那么
         *                  　ctx你可以传值为 nodes,表示取的是子结点
         *                  例如:你返回的数据放在data字段中，但是你想循环的是要data.rows(这是一个数组)
         *                  opRet.each(function (elment) {
 *                        redraw(elment.title, elment.createTime.substr(0, 10), elment.id);
 *                    }, "rows" );
         *                  　
         }
         */
        each: function (callback, ctx) {
            var realCallback = callback;
            var realCtx = ctx;
            var i = 0;
            var len = 0;
            var realData = config;
            var ctxArray = null;
            var ele = null;
            //检查回调函数
            if (!realCallback || !($.isFunction(realCallback))) {
                throw new Error(callback + " 不是一个有效的js函数");
            }
            //检查上下文
            if (!ctx || $.trim(ctx).length == 0) {
                realCtx = "data";
            } else if ($.trim(ctx).indexOf("data") != 0) {
                realCtx = "data." + $.trim(ctx);
            }
            ctxArray = $.trim(realCtx).split(".");
            for (i = 0; i < ctxArray.length; ++i) {
                //如果包含这属性
                ele = ctxArray[i];
                if (realData.hasOwnProperty(ele)) {
                    realData = realData[ele];
                } else {
                    throw new Error("属性不存在,属性名为:" + ele);
                }
            }
            //只能操作数组
            if ($.isArray(realData)) {
                len = realData.length;
                for (i = 0; i < len; ++i) {
                    callback(realData[i], i, realData);
                }
            } else {
                throw new Error("你访问的不是一个有效的数组:" + ctx);
            }
        },

        /**
         * 这里是开始自动检查是否有错误
         * true代表有error
         * fale:代表无error
         */
        checkError: function () {
            //服务器有响应值，并且服务器认为已失败
            if (this.isServerRespNotNull() && !config.sysSuccess) {
                var topBody = window.top.document.body;
                var errForm = $("<form>", {
                    method: "post",
                    action: this.getExtraData()
                });
                var errCode = $("<input>", {
                    name: "errForm.errCode",
                    val: this.getCode(),
                    type: "text"
                });
                errCode.appendTo(errForm);
                var errDesc = $("<input>", {
                    name: "errForm.errDesc",
                    val: this.getDesc(),
                    type: "text"
                });
                errDesc.appendTo(errForm);
                errForm.appendTo(topBody);

                $(errForm).submit();

                return true;
            }
            return false;

        },
        /**
         * 得到服务器的原始响应
         * @returns {null}
         */
        getOrgResp: function () {
            return config.orgData;
        },
        /**
         * 得到服务器的返回结果
         * @returns {boolean|*}
         */
        isSuccess: function () {
            return config.success;
        },
        /**
         * 得到服务器是否操时
         * @returns {boolean|*}
         */
        isTimeout: function () {
            return config.code == this.RET_TIME_OUT;
        },

        /**
         *得到服务器中的响应码
         * @returns {null|*}
         */
        getCode: function () {
            return config.code;
        },
        /**
         * 得到服务器中对响应的描述
         * @returns {null|*}
         */
        getDesc: function () {
            return config.desc;
        },
        /**
         * 得到服务器响应中的服务数据
         * @returns {null|*}
         */
        getData: function () {
            return config.data;
        },
        /**
         * 得到额外的数据，一般多为拦截器去取值，开发者大多用不上
         * @returns {null|*}
         */
        getExtraData: function () {
            return config.extraData;
        },
        /**
         * 判断服务器的响应是不是为null或者undefined
         * @returns {null|*}
         */
        isServerRespNull: function () {
            window.OperResult.checkNull(config.orgData);
        },
        /**
         * 语法糖  判断服务器的响应不为空则返回　true
         * @returns {boolean}
         */
        isServerRespNotNull: function () {
            return !(this.isServerRespNull());
        },
        /**
         * 1 页面超时的时候才会有值,并且需要调用BaeAction中的setTimeout()方法
         * 2 取得登录页面的地址,这个里面已经带了上下文地址
         * 所以不用去拼接 rqeust.ContextPath
         */
        getLoginUrl: function () {
            return config.extraData;
        },
        /**
         * 如果超时了，则自动跳转到登录页面
         */
        jumpToLoginIfTimeout: function () {
            this.checkError();
        }

    };


    /**
     * 类方法
     * @param obj
     * @returns {boolean}
     */
    window.OperResult.checkNull = function (obj) {
        return obj === null || obj === undefined;
    };

})(jQuery, window);










