package com.yellowhao.controller;

import com.google.gson.Gson;
import com.yellowhao.po.User;
import com.yellowhao.service.BlogService;
import com.yellowhao.service.TagService;
import com.yellowhao.service.TypeService;
import com.yellowhao.service.Userservice;
import com.yellowhao.service.impl.UserServiceImpl;
import com.yellowhao.util.ConstantWxUtils;
import com.yellowhao.util.HttpClientUtils;
import groovy.util.logging.Commons;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;

/**
 * 首页index.html
 */
@Slf4j
@CrossOrigin
@Controller
public class IndexController {

	@Autowired
	private BlogService blogServiceImpl;

	@Autowired
	private TypeService typeServiceImpl;

	@Autowired
	private TagService tagServiceImpl;

	@Autowired
	private Userservice userservice;

	/**
	 * 首页面显示
	 * @param model
	 * @param pageable
	 * @return
	 */
	@GetMapping("/")
	public String index(Model model, @PageableDefault(size = 10, sort = {"updateTime"}, direction = Sort.Direction.DESC)
			Pageable pageable){
		//1. 获取分页的博客列表
		model.addAttribute("page",blogServiceImpl.ListBlog(pageable));
		//2. 获取分类的内容(显示条)
		model.addAttribute("types",typeServiceImpl.listTypeTop(8));
		//3. 获取标签的内容
		model.addAttribute("tags",tagServiceImpl.ListTagTop(10));
		//4. 显示推荐博客列表
		model.addAttribute("recommendBlogs",blogServiceImpl.listRecommendBlogTop(6));
		return "index";
	}

	/**
	 * 导航栏中的搜索功能实现
	 * @param pageable
	 * @return
	 * @param model
	 */
    @PostMapping("/search")
    public String search(@PageableDefault(size = 8, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                         @RequestParam String query, Model model) {
        model.addAttribute("page", blogServiceImpl.ListBlog(pageable, "%"+query+"%"));
        model.addAttribute("query", query);
        return "search";
    }

    @GetMapping("/blog/{id}")
    public  String blog(@PathVariable("id") Long id,Model model){
        model.addAttribute("blog",blogServiceImpl.getAadConvertBlog(id));
        return "blog";
    }

    @GetMapping("/footer/newblog")
    public String newblos(Model model){
		model.addAttribute("newblogs",blogServiceImpl.listRecommendBlogTop(3));
    	return "_fragments :: newblogList";
	}

	@GetMapping("/wx_login")
	public String wxLogin() {

		return "wx_login";
	}

	//2 获取扫描人信息，添加数据
	@GetMapping("/api/ucenter/wx/callback")
	public String callback(String code, String state, HttpSession session) {

    	log.info("获取扫描人信息，添加数据:{}  {}",code,state);
		try {
			//1 获取code值，临时票据，类似于验证码
			//2 拿着code请求 微信固定的地址，得到两个值 accsess_token 和 openid
			String baseAccessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token" +
					"?appid=%s" +
					"&secret=%s" +
					"&code=%s" +
					"&grant_type=authorization_code";
			//拼接三个参数 ：id  秘钥 和 code值
			String accessTokenUrl = String.format(
					baseAccessTokenUrl,
					ConstantWxUtils.WX_OPEN_APP_ID,
					ConstantWxUtils.WX_OPEN_APP_SECRET,
					code
			);
			//请求这个拼接好的地址，得到返回两个值 accsess_token 和 openid
			//使用httpclient发送请求，得到返回结果
			String accessTokenInfo = HttpClientUtils.get(accessTokenUrl);

			//从accessTokenInfo字符串获取出来两个值 accsess_token 和 openid
			//把accessTokenInfo字符串转换map集合，根据map里面key获取对应值
			//使用json转换工具 Gson
			Gson gson = new Gson();
			HashMap mapAccessToken = gson.fromJson(accessTokenInfo, HashMap.class);
			String access_token = (String)mapAccessToken.get("access_token");
			String openid = (String)mapAccessToken.get("openid");

			//把扫描人信息添加数据库里面
			//判断数据表里面是否存在相同微信信息，根据openid判断
			User member = userservice.getByOpenid(openid);

			//3 拿着得到accsess_token 和 openid，再去请求微信提供固定的地址，获取到扫描人信息
			//访问微信的资源服务器，获取用户信息
			String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo" +
					"?access_token=%s" +
					"&openid=%s";
			//拼接两个参数
			String userInfoUrl = String.format(
					baseUserInfoUrl,
					access_token,
					openid
			);
			//发送请求
			String userInfo = HttpClientUtils.get(userInfoUrl);
			//获取返回userinfo字符串扫描人信息
			HashMap userInfoMap = gson.fromJson(userInfo, HashMap.class);
			String nickname = (String)userInfoMap.get("nickname");//昵称
			String headimgurl = (String)userInfoMap.get("headimgurl");//头像
			Date date = new Date();
			if(member == null) {//memeber是空，表没有相同微信数据，进行添加
				member = new User();
				member.setOpenid(openid);
				member.setNickname(nickname);
				member.setAvatar(headimgurl);
				member.setCreateTime(date);
				userservice.save(member);
				session.setAttribute("user",member);
				return "admin/index";
			}
			if (!headimgurl.equals(member.getAvatar())){
				member.setAvatar(headimgurl);
			}
			if (!nickname.equals(member.getNickname())){
				member.setNickname(nickname);
			}
			member.setUpdateTime(date);
			userservice.save(member);
			session.setAttribute("user",member);
			//使用jwt根据member对象生成token字符串
			//String jwtToken = JwtUtils.getJwtToken(member.getId(), member.getNickname());
			//最后：返回首页面，通过路径传递token字符串
			return "admin/index";
		}catch(Exception e) {
			log.info("20001,登录失败");
			e.printStackTrace();
			return "admin/login";
		}
	}

}
