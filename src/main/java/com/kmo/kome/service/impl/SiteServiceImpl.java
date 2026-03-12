package com.kmo.kome.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kmo.kome.common.ResultCode;
import com.kmo.kome.common.exception.ServiceException;
import com.kmo.kome.dto.request.SetupRequest;
import com.kmo.kome.dto.response.AdminSiteInfoResponse;
import com.kmo.kome.dto.response.PublicSiteInfoResponse;
import com.kmo.kome.entity.Link;
import com.kmo.kome.entity.Memo;
import com.kmo.kome.entity.Post;
import com.kmo.kome.entity.User;
import com.kmo.kome.service.*;
import com.kmo.kome.utils.MessageHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;


/**
 * 站点服务实现类
 * <p>
 * 实现了 {@link SiteService} 接口，提供站点相关的业务逻辑功能。
 * 包括获取站点基本信息、统计数据等。
 * <p>
 * 依赖于多个服务接口 {@link UserService}、{@link PostService}、{@link TagService}、
 * {@link MemoService} 和 {@link LinkService}，通过组合的方式实现业务逻辑。
 */
@Service
@RequiredArgsConstructor
public class SiteServiceImpl implements SiteService {

    private final UserService userService;
    private final PostService postService;
    private final TagService tagService;
    private final MemoService memoService;
    private final LinkService linkService;
    private final MessageHelper messageHelper;

    /**
     * 获取后台站点统计数据。
     * 仅用于管理后台 dashboard，不包含公开 owner 信息。
     * <p>
     * @return 包含后台统计数据的响应对象 {@code AdminSiteInfoResponse}。
     */
    @Override
    public AdminSiteInfoResponse getAdminSiteInfo() {
        // 统计信息
        long publishedPostCount = postService.count(new LambdaQueryWrapper<Post>().eq(Post::getStatus, 1));
        long draftPostCount = postService.count(new LambdaQueryWrapper<Post>().eq(Post::getStatus, 0));

        // Tag 统计
        long usedTagCount = tagService.countUsedTags();
        long unusedTagCount = tagService.count() - usedTagCount;

        // Memo 统计
        long publishedMemoCount = memoService.count(new LambdaQueryWrapper<Memo>().eq(Memo::getStatus, 1));
        long draftMemoCount = memoService.count(new LambdaQueryWrapper<Memo>().eq(Memo::getStatus, 0));

        // Link 统计
        long publishedLinkCount = linkService.count(new LambdaQueryWrapper<Link>().eq(Link::getStatus, 1));
        long draftLinkCount = linkService.count(new LambdaQueryWrapper<Link>().eq(Link::getStatus, 0));

        return AdminSiteInfoResponse.builder()
                .publishedPostCount(publishedPostCount)
                .draftPostCount(draftPostCount)
                .publishedMemoCount(publishedMemoCount)
                .draftMemoCount(draftMemoCount)
                .publishedLinkCount(publishedLinkCount)
                .draftLinkCount(draftLinkCount)
                .usedTagCount(usedTagCount)
                .unusedTagCount(unusedTagCount)
                .build();
    }

    /**
     * 获取站点的公共信息。
     * 直接查询已发布内容的统计数据，不依赖管理员接口。
     *
     * @return 包含站点公开信息的响应对象 {@code PublicSiteInfoResponse}。
     */
    @Override
    public PublicSiteInfoResponse getPublicSiteInfo() {
        User user = userService.getOne(new LambdaQueryWrapper<User>()
                .eq(User::getIsOwner, true)
                .last("LIMIT 1")
        );
        if(user == null){
            throw new ServiceException(ResultCode.INTERNAL_SERVER_ERROR, messageHelper.get("error.site.dataMissing"));
        }

        PublicSiteInfoResponse.OwnerInfo ownerInfo = PublicSiteInfoResponse.OwnerInfo.builder()
                // 如未设置昵称，则返回用户名用于前端显示兜底
                .nickname(user.getNickname() != null ? user.getNickname() : user.getUsername())
                .avatar(user.getAvatar())
                .description(user.getDescription())
                .createdAt(user.getCreateTime())
                .socialLinks(user.getSocialLinks())
                .skills(user.getSkills())
                .build();

        return PublicSiteInfoResponse.builder()
                .owner(ownerInfo)
                .stats(PublicSiteInfoResponse.Stats.builder()
                        .publishedPostCount(postService.count(new LambdaQueryWrapper<Post>().eq(Post::getStatus, 1)))
                        .publishedMemoCount(memoService.count(new LambdaQueryWrapper<Memo>().eq(Memo::getStatus, 1)))
                        .publishedLinkCount(linkService.count(new LambdaQueryWrapper<Link>().eq(Link::getStatus, 1)))
                        .usedTagCount(tagService.countUsedTags())
                        .build())
                .build();
    }

    /**
     * 检查系统是否已初始化
     * 通过检测是否存在管理员账户（isOwner = true）来判断
     *
     * @return true 表示已初始化，false 表示未初始化
     */
    @Override
    public boolean isInitialized() {
        return userService.count(
                new LambdaQueryWrapper<User>().eq(User::getIsOwner, true)
        ) > 0;
    }

    /**
     * 首次设置管理员账户
     * 仅当系统未初始化时可用，创建第一个管理员账户
     *
     * @param request 设置请求，包含用户名、密码及可选的昵称、头像、简介、邮箱
     *                （可选字段未填则为 null）
     * @throws ServiceException 如果系统已初始化
     */
    @Override
    @Transactional(rollbackFor = Exception.class, isolation = Isolation.SERIALIZABLE)
    public void setupAdmin(SetupRequest request) {
        // 检查是否已初始化
        if (isInitialized()) {
            throw new ServiceException(ResultCode.BAD_REQUEST, messageHelper.get("error.site.alreadyInitialized"));
        }
        // 创建管理员账号
        userService.createInitialOwner(request);
    }
}
