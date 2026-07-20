package com.tianji.notification.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tianji.notification.domain.po.Notification;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * 通知记录 Mapper
 */
public interface NotificationMapper extends BaseMapper<Notification> {

    /** 统计用户未读通知数量 */
    @Select("SELECT COUNT(*) FROM notification WHERE user_id = #{userId} AND is_read = 0 AND deleted = 0")
    int countUnreadByUserId(@Param("userId") Long userId);

    /** 标记所有通知为已读 */
    @Update("UPDATE notification SET is_read = 1, read_time = NOW() " +
            "WHERE user_id = #{userId} AND is_read = 0 AND deleted = 0")
    int markAllReadByUserId(@Param("userId") Long userId);
}
