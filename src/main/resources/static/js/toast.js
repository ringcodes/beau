'use strict';
(function($,window){
	//显示提示信息    toast
	$.fn.toast = function(options){
		var $this = $(this);
		var _this = this;
		return this.each(function(){
			$(this).css({
				position:'relative'
			});
			var top = '';		//bottom的位置

		    var box = '';   //消息元素
		    var defaults = {
		    	position:  			  "absolute", 				//不是body的话就absolute
		    	animateIn:  		  "fadeIn",		//进入的动画
		    	animateOut: 		  "fadeOut",				//结束的动画
				padding:              "10px 20px",              //padding
				background:           "rgba(7,17,27,0.66)",     //背景色
				borderRadius:         "6px",                    //圆角
				duration:             3000,                     //定时器时间
				animateDuration: 	  500, 						//执行动画时间
				fontSize:             14,                   	//字体大小
				content:              "这是一个提示信息",       //提示内容
				color:                "#fff",                   //文字颜色
				top:            	  "80%",                	//bottom底部的位置    具体的数值 或者center  垂直居中
				zIndex:               1000001,                	//层级
				isCenter:   		  true, 					//是否垂直水平居中显示
				closePrev: 			  true, 					//在打开下一个toast的时候立即关闭上一个toast
				onBack: function () {}
		    };

		    var opt = $.extend(defaults,options||{});
		    var t = '';

			// setTimeout(function(){
			//   	box.addClass('show');
			// },10);

			top = opt.isCenter===true? '50%':opt.top;

			defaults.isLowerIe9 = function(){
				return (!window.FormData);
			};

		    defaults.createMessage = function(){
				if(opt.closePrev){
					$('.cpt-toast').remove();
				}
				box = $("<span class='animated "+opt.animateIn+" cpt-toast'></span>").css({
					"position":opt.position,
					"padding":opt.padding,
					"background":opt.background,
					"font-size":opt.fontSize,
					"-webkit-border-radius":opt.borderRadius,
					"-moz-border-radius":opt.borderRadius,
					"border-radius":opt.borderRadius,
					"color":opt.color,
					"top":top,
					"left":"50%",
					"z-index":opt.zIndex,
					"-webkit-transform":'translate3d(-50%,-50%,0)',
			        "-moz-transform":'translate3d(-50%,-50%,0)',
			        "transform":'translate3d(-50%,-50%,0)',
			        '-webkit-animation-duration':opt.animateDuration/1000+'s',
	    			'-moz-animation-duration':opt.animateDuration/1000+'s',
	    			'animation-duration':opt.animateDuration/1000+'s',
				}).html(opt.content).appendTo($this);
				defaults.colseMessage();
		    };

		    defaults.colseMessage = function(){
				var isLowerIe9 = defaults.isLowerIe9();
		    	if(!isLowerIe9){
					t = setTimeout(function(){
			    		box.removeClass(opt.animateIn).addClass(opt.animateOut).on('animationend webkitAnimationEnd mozAnimationEnd MSAnimationEnd oanimationend',function(){
			    			box.remove();
			    			opt.onBack();
			    		});
			    	},opt.duration);
		    	}else{
		    		t = setTimeout(function(){
			    		box.remove();
						opt.onBack();
			    	},opt.duration);
		    	}
		    };

		    defaults.createMessage();
		})
	};
})(jQuery,window);
var toastUtil={};
toastUtil.error=function (msg,top=95) {
	$('body').toast({
		position:'fixed',
		top:top+"px",
		content:msg,
		duration:3000,
		isCenter:false,
		background:'rgba(230,0,0,0.7)',
	});
};
toastUtil.success=function (msg,top, onBack) {
	if (typeof top === "function"){
		onBack=top;
		top=40;
	}
	$('body').toast({
		position:'fixed',
		top:top+"px",
		content:msg,
		duration:3000,
		isCenter:false,
		background:'rgba(1,194,111,1)',
		onBack:onBack
	});
};
