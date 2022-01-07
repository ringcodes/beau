$(function () {
  // 菜单显示
  const currentUri = location.pathname;
  if (currentUri === '/login.html') {
    $(".header-login-btn").css("display", "none");
  } else {
    $("#web-menu").find("a[href='" + currentUri + "']").addClass("active");
  }
  // 当前用户判断
  $.ajax({
    url: '/auth/current',
    dataType: 'html',
    success: function (html) {
      $("#login-action").html(html);
    }
  })
  // 搜索
  $("#search-key").focus(function () {
    $(this).parent().addClass("input-focus");
  });
  $("#search-key").blur(function () {
    $(this).parent().removeClass("input-focus");
  });
  $("#search-key").on("keydown", function (e) {
    const key = e.which;
    if (key == 13) {
      location.href = "/search.html?key=" + encodeURI($("#search-key").val())
    }
  });
  // 搜索
  $("#btn-search-key").on("click", function () {
    location.href = "/search.html?key=" + encodeURI($("#search-key").val())
  });
  // 登出
  $("#logout-btn").on("click", function () {
    CookieUtil.remove("Token");
    location.reload();
  });
  // 登录
  $("#login-action").on("click", "#login-btn", function () {
    location.href = "/login.html?backUrl=" + encodeURI(location.pathname)
  });
  // 图片加载
  $("img.lazy").lazyload({effect: "fadeIn"});

  $(window).scroll(function () {
    if ($(window).scrollTop() > 50) {
      $("#btn").fadeIn(200);
    } else {
      $("#btn").fadeOut(200);
    }
  });
  //当点击跳转链接后，回到页面顶部位置
  $("#btn").click(function () {
    $('body,html').animate({
        scrollTop: 0
      },
      500);
    return false;
  });
  $(".header_content").css("min-height", $(window).height() - 140);
});
