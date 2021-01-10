package com.febs.web.controller.admin.cms;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.febs.cms.domain.FriendLink;
import com.febs.cms.service.FriendLinkService;
import com.febs.common.annotation.Log;
import com.febs.common.domain.QueryRequest;
import com.febs.common.domain.ResponseBo;
import com.febs.common.utils.FileUtils;
import com.febs.system.domain.MyUser;
import com.febs.web.controller.base.BaseController;

@Controller
@RequestMapping("/admin")
public class FriendLinkController extends BaseController {

	private Logger log = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private FriendLinkService friendLinkService;

	@Log("获取友情链接信息")
	@RequestMapping("/friendLink")
	@PreAuthorize("hasAuthority('friendLink:list')")
	public String index() {
		return "admin/cms/friendLink/friendLink";
	}

	@RequestMapping("/friendLink/getFriendLink")
	@ResponseBody
	public ResponseBo getFriendLink(Long friendLinkId) {
		try {
			FriendLink FriendLink = this.friendLinkService.findById(friendLinkId);
			return ResponseBo.ok(FriendLink);
		} catch (Exception e) {
			log.error("获取友情链接信息失败", e);
			return ResponseBo.error("获取友情链接信息失败，请联系网站管理员！");
		}
	}

	@RequestMapping("/friendLink/list")
	@PreAuthorize("hasAuthority('friendLink:list')")
	@ResponseBody
	public Map<String, Object> friendLinkList(QueryRequest request, FriendLink friendLink) {
		return super.selectByPageNumSize(request, () -> this.friendLinkService.findAllFriendLinks(friendLink));

	}

	@RequestMapping("/friendLink/checkFriendLinkName")
	@ResponseBody
	public boolean checkFriendLinkName(String friendLinkName, String oldFriendLinkName) {
		if (StringUtils.isNotBlank(oldFriendLinkName)
				&& StringUtils.equalsIgnoreCase(friendLinkName, oldFriendLinkName)) {
			return true;
		}
		FriendLink result = this.friendLinkService.findByName(friendLinkName);
		return result == null;
	}

	@Log("新增友情链接 ")
	@PreAuthorize("hasAuthority('friendLink:add')")
	@RequestMapping("/friendLink/add")
	@ResponseBody
	public ResponseBo addFriendLink(FriendLink friendLink) {
		try {
			MyUser currentUser = this.getCurrentUser();
			friendLink.setUserId(currentUser.getUserId());
			this.friendLinkService.addFriendLink(friendLink);
			return ResponseBo.ok("新增友情链接成功！");
		} catch (Exception e) {
			log.error("新增友情链接失败", e);
			return ResponseBo.error("新增友情链接失败，请联系网站管理员！");
		}
	}

	@Log("删除友情链接")
	@PreAuthorize("hasAuthority('friendLink:delete')")
	@RequestMapping("/friendLink/delete")
	@ResponseBody
	public ResponseBo deleteFriendLinks(String ids) {
		try {
			this.friendLinkService.deleteFriendLinks(ids);
			return ResponseBo.ok("删除友情链接成功！");
		} catch (Exception e) {
			log.error("删除友情链接失败", e);
			return ResponseBo.error("删除友情链接失败，请联系网站管理员！");
		}
	}

	@Log("修改友情链接 ")
	@PreAuthorize("hasAuthority('friendLink:update')")
	@RequestMapping("/friendLink/update")
	@ResponseBody
	public ResponseBo updateFriendLink(FriendLink friendLink) {
		try {
			MyUser currentUser = this.getCurrentUser();
			friendLink.setUserId(currentUser.getUserId());
			this.friendLinkService.updateFriendLink(friendLink);
			return ResponseBo.ok("修改友情链接成功！");
		} catch (Exception e) {
			log.error("修改友情链接失败", e);
			return ResponseBo.error("修改友情链接失败，请联系网站管理员！");
		}
	}

	@RequestMapping("/friendLink/excel")
	@ResponseBody
	public ResponseBo friendLinkExcel(FriendLink friendLink) {
		try {
			List<FriendLink> list = this.friendLinkService.findAllFriendLinks(friendLink);
			return FileUtils.createExcelByPOIKit("友情链接表", list, FriendLink.class);
		} catch (Exception e) {
			log.error("导出友情链接信息Excel失败", e);
			return ResponseBo.error("导出Excel失败，请联系网站管理员！");
		}
	}

	@RequestMapping("/friendLink/csv")
	@ResponseBody
	public ResponseBo friendLinkCsv(FriendLink friendLink) {
		try {
			List<FriendLink> list = this.friendLinkService.findAllFriendLinks(friendLink);
			return FileUtils.createCsv("友情链接表", list, FriendLink.class);
		} catch (Exception e) {
			log.error("获取友情链接信息Csv失败", e);
			return ResponseBo.error("导出Csv失败，请联系网站管理员！");
		}
	}
}
