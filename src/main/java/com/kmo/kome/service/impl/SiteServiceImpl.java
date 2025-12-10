package com.kmo.kome.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kmo.kome.common.ResultCode;
import com.kmo.kome.common.exception.ServiceException;
import com.kmo.kome.dto.response.SiteInfoResponse;
import com.kmo.kome.entity.Memo;
import com.kmo.kome.entity.Post;
import com.kmo.kome.entity.User;
import com.kmo.kome.service.*;
import lombok.RequiredArgsConstructor;
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

    /**
     * 获取站点的基本信息，包括站点所有者信息和统计数据。
     * <p>
     * 所有者信息包含用户的昵称、头像和个人简介。
     * 统计数据包括站点的文章数、标签数、Memo数和友链数。
     * 如果未找到站点所有者信息，将抛出 ServiceException 异常。
     *
     * @return 封装了站点所有者信息和统计数据的 SiteInfoResponse 对象
     */
    @Override
    public SiteInfoResponse getSiteInfo() {
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
        long postCount = postService.count(new LambdaQueryWrapper<Post>().eq(Post::getStatus, 1));
        long tagCount = tagService.count();
        long memoCount = memoService.count(new LambdaQueryWrapper<Memo>().eq(Memo::getStatus, 1));
        long linkCount = linkService.count();

        SiteInfoResponse.Stats stats = SiteInfoResponse.Stats.builder()
                .postCount(postCount)
                .tagCount(tagCount)
                .memoCount(memoCount)
                .linkCount(linkCount)
                .build();

        return SiteInfoResponse.builder()
                .owner(ownerInfo)
                .stats(stats)
                .build();
    }
}
