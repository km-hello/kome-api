package com.kmo.kome.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kmo.kome.common.ResultCode;
import com.kmo.kome.common.exception.ServiceException;
import com.kmo.kome.dto.request.SetupRequest;
import com.kmo.kome.dto.response.SiteInfoResponse;
import com.kmo.kome.entity.Link;
import com.kmo.kome.entity.Memo;
import com.kmo.kome.entity.Post;
import com.kmo.kome.entity.User;
import com.kmo.kome.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
    private final PasswordEncoder passwordEncoder;

    /**
     * 获取站点管理员的基础信息和统计数据。
     * 该方法用于聚合站点所有者信息、文章统计、标签统计、说说统计以及友链统计，
     * 并返回封装好的站点信息响应对象。
     * <p>
     * 如果站点数据缺失（如无法获取管理者信息），则会抛出业务异常。
     *
     * @return 包含站点管理员信息和统计数据的响应对象 {@code SiteInfoResponse}。
     */
    @Override
    public SiteInfoResponse getAdminSiteInfo() {
        // 所有者信息
        User user = userService.getOne(new LambdaQueryWrapper<User>()
                .eq(User::getIsOwner, true)
                .last("LIMIT 1")
        );
        if(user == null){
            throw new ServiceException(ResultCode.INTERNAL_SERVER_ERROR, "系统数据缺失");
        }

        SiteInfoResponse.OwnerInfo ownerInfo = SiteInfoResponse.OwnerInfo.builder()
                .nickname(user.getNickname())
                .avatar(user.getAvatar())
                .description(user.getDescription())
                .build();

        // 统计信息
        // Post 统计
        long publishedPostCount = postService.count(new LambdaQueryWrapper<Post>().eq(Post::getStatus, 1));
        long draftPostCount = postService.count(new LambdaQueryWrapper<Post>().eq(Post::getStatus, 0));

        // Tag 统计
        long totalTagCount = tagService.count();
        long usedTagCount = tagService.getPublicTagList().size();
        long unusedTagCount = totalTagCount - usedTagCount;

        // Memo 统计
        long publishedMemoCount = memoService.count(new LambdaQueryWrapper<Memo>().eq(Memo::getStatus, 1));
        long draftMemoCount = memoService.count(new LambdaQueryWrapper<Memo>().eq(Memo::getStatus, 0));

        // Link 统计
        long publishedLinkCount = linkService.count(new LambdaQueryWrapper<Link>().eq(Link::getStatus, 1));
        long draftLinkCount = linkService.count(new LambdaQueryWrapper<Link>().eq(Link::getStatus, 0));

        SiteInfoResponse.Stats stats = SiteInfoResponse.Stats.builder()
                .publishedPostCount(publishedPostCount)
                .draftPostCount(draftPostCount)
                .publishedMemoCount(publishedMemoCount)
                .draftMemoCount(draftMemoCount)
                .publishedLinkCount(publishedLinkCount)
                .draftLinkCount(draftLinkCount)
                .usedTagCount(usedTagCount)
                .unusedTagCount(unusedTagCount)
                .build();

        return SiteInfoResponse.builder()
                .owner(ownerInfo)
                .stats(stats)
                .build();
    }

    /**
     * 获取站点的公共信息。
     * 此方法调用管理员站点信息获取方法，过滤掉草稿相关统计数据，
     * 并返回包含公开统计数据和所有者信息的响应对象。
     *
     * @return 包含站点公开信息的响应对象 {@code SiteInfoResponse}。
     */
    @Override
    public SiteInfoResponse getPublicSiteInfo() {
        SiteInfoResponse adminSiteInfo = getAdminSiteInfo();
        return SiteInfoResponse.builder()
                .owner(adminSiteInfo.getOwner())
                .stats(SiteInfoResponse.Stats.builder()
                        .publishedPostCount(adminSiteInfo.getStats().getPublishedPostCount())
                        .draftPostCount(0L)
                        .publishedMemoCount(adminSiteInfo.getStats().getPublishedMemoCount())
                        .draftMemoCount(0L)
                        .publishedLinkCount(adminSiteInfo.getStats().getPublishedLinkCount())
                        .draftLinkCount(0L)
                        .usedTagCount(adminSiteInfo.getStats().getUsedTagCount())
                        .unusedTagCount(0L)
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
     * @param request 设置请求，包含用户名、密码和可选的昵称、头像、简介、邮箱
     * @throws ServiceException 如果系统已初始化
     */
    @Override
    public void setupAdmin(SetupRequest request) {
        // 检查是否已初始化
        if (isInitialized()) {
            throw new ServiceException(ResultCode.BAD_REQUEST, "系统已初始化");
        }

        // 创建管理员账户（可选字段为空时保持 null，由前端处理默认显示）
        User admin = new User();
        admin.setUsername(request.getUsername());
        admin.setPassword(passwordEncoder.encode(request.getPassword()));
        admin.setNickname(request.getNickname());
        admin.setAvatar(request.getAvatar());
        admin.setDescription(request.getDescription());
        admin.setEmail(request.getEmail());
        admin.setIsOwner(true);
        admin.setIsDeleted(false);
        userService.save(admin);
    }
}
